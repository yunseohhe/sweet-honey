package com.sparta.sweethoney.domain.store.service;

import com.sparta.sweethoney.domain.common.dto.AuthUser;
import com.sparta.sweethoney.domain.common.exception.menu.NotFoundMenuException;
import com.sparta.sweethoney.domain.common.exception.store.MaxStoreLimitException;
import com.sparta.sweethoney.domain.common.exception.store.NotFoundStoreException;
import com.sparta.sweethoney.domain.common.exception.store.NotOwnerOfStoreException;
import com.sparta.sweethoney.domain.menu.dto.response.GetMenuResponseDto;
import com.sparta.sweethoney.domain.menu.entity.Menu;
import com.sparta.sweethoney.domain.menu.entity.MenuStatus;
import com.sparta.sweethoney.domain.menu.repository.MenuRepository;
import com.sparta.sweethoney.domain.store.dto.request.StoreRequest;
import com.sparta.sweethoney.domain.store.dto.response.StoreDetailResponse;
import com.sparta.sweethoney.domain.store.dto.response.StorePutResponse;
import com.sparta.sweethoney.domain.store.dto.response.StoreResponse;
import com.sparta.sweethoney.domain.store.entity.Store;
import com.sparta.sweethoney.domain.store.enums.AdStatus;
import com.sparta.sweethoney.domain.store.enums.StoreStatus;
import com.sparta.sweethoney.domain.store.repository.StoreRepository;
import com.sparta.sweethoney.domain.user.entity.User;
import com.sparta.sweethoney.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreService {
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;
    private final MenuRepository menuRepository;

    // 가게 생성 로직
    @Transactional
    public StoreResponse createStore(AuthUser authUser, StoreRequest storeSaveRequest) {
        // 가게를 생성하려는 유저가 사장님인지 확인
        User owner = userRepository.findById(authUser.getId())
                .orElseThrow(() -> new IllegalArgumentException("사장님을 찾을 수 없습니다."));

        // 사장님이 운영 중인 가게 수는 최대 3개까지 생성 가능 (폐업 상태 가게는 제외)
        if (storeRepository.countByUserIdAndStoreStatus(owner.getId(), StoreStatus.OPERATING) == 3) {
            throw new MaxStoreLimitException();
        }

        // Entity 변환
        Store newStore = new Store(storeSaveRequest, owner);

        // DB에 저장하면서 StoreResponse 반환
        return mapToStoreResponse(storeRepository.save(newStore));
    }

    // 가게 수정 로직
    @Transactional
    public StorePutResponse updateStore(Long storeId, StoreRequest storeUpdateRequest, AuthUser authUser) {

        // 가게가 존재하는지 조회
        Store store = storeRepository.findById(storeId)
                .orElseThrow(NotFoundStoreException::new);

        // 현재 로그인한 유저가 가게의 소유주인지 확인
        if (!store.getUser().getId().equals(authUser.getId())) {
            throw new NotOwnerOfStoreException();
        }

        // 가게 수정
        store.update(storeUpdateRequest);

        // 수정된 가게 정보를 StorePutResponse(DTO)로 반환
        return new StorePutResponse(
                store.getId(),
                store.getName(),
                store.getOpenTime(),
                store.getCloseTime(),
                store.getMinOrderPrice(),
                store.getNotice()
        );
    }

    // 광고 설정 로직
    @Transactional
    public void setAdStatus(Long storeId, AuthUser authUser, AdStatus adStatus) {
        // 가게가 존재하는지 조회
        Store store = storeRepository.findById(storeId)
                .orElseThrow(NotFoundStoreException::new);

        // 사장님인지 확인
        if (!store.getUser().getId().equals(authUser.getId())) {
            throw new NotOwnerOfStoreException();
        }

        // 광고 상태 설정
        store.setAdStatus(adStatus);
    }

    //가게 일괄 조회
    public List<StoreResponse> getStores(String name) {
        // 가게명을 포함하는 가게들만 검색 (name이 null 또는 빈 문자열이 아닐 경우)
        List<Store> storeList;
        if (name != null && !name.isBlank()) {
            storeList = storeRepository.searchStores(name, StoreStatus.OPERATING);
        } else {
            // name이 없을 때 모든 가게를 검색하는 걸 막기 위해 빈 리스트 반환 또는 예외 처리
            throw new NotFoundStoreException();
        }

        // 포함된 가게명이 존재하는지 확인
        if (storeList.isEmpty()) {
            throw new NotFoundStoreException();
        }

        return storeList.stream()
                .map(this::mapToStoreResponse)
                .collect(Collectors.toList());
    }

    //가게 단건 조회
    public StoreDetailResponse getStore(Long storeId) {
        // 운영 중인 가게만 조회
        Store store = storeRepository.findByIdAndStoreStatus(storeId, StoreStatus.OPERATING)
                .orElseThrow(NotFoundStoreException::new);

        // 가게기준으로 활성화 상태인 메뉴만을 가져오는 로직
        List<GetMenuResponseDto> activeMenus = menuRepository.findByStoreId(storeId)
                .map(menus -> menus.stream()
                        .filter(menu -> menu.getStatus() == MenuStatus.ACTIVE)
                        .map(GetMenuResponseDto::new)
                        .toList())
                // 만약 메뉴가 없을 경우, NOT_FOUND_MENU 예외를 던짐
                .orElseThrow(NotFoundMenuException::new);

        // Dto 반환
        return new StoreDetailResponse(
                store.getId(),
                store.getName(),
                store.getOpenTime(),
                store.getCloseTime(),
                store.getMinOrderPrice(),
                store.getNotice(),
                activeMenus
        );
    }

    // 가게 폐업
    @Transactional
    public void deleteStore(Long storeId, AuthUser authUser) {
        // 가게가 존재하는지 조회
        Store store = storeRepository.findById(storeId)
                .orElseThrow(NotFoundStoreException::new);

        // 현재 로그인한 유저가 가게의 소유주인지 확인
        if (!store.getUser().getId().equals(authUser.getId())) {
            throw new NotOwnerOfStoreException();
        }

        // 등록되있는 메뉴가 있는지 확인 후
        List<Menu> menuList = menuRepository.findByStoreId(storeId)
                .orElseThrow(NotFoundMenuException::new);

        // 활성화되어 있는 메뉴들은 비활성화 처리
        for (Menu menu : menuList) {
            if (menu.getStatus() == MenuStatus.ACTIVE) {
                menu.delete(MenuStatus.INACTIVE);
            }
        }

        // 가게 폐업
        store.terminated();
    }

    // StoreResponse 객체 생성 메서드
    private StoreResponse mapToStoreResponse(Store store) {
        return new StoreResponse(
                store.getId(),
                store.getName(),
                store.getOpenTime(),
                store.getCloseTime(),
                store.getMinOrderPrice()
        );
    }
}
