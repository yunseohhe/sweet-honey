package com.sparta.sweethoney.domain.store.service;

import com.sparta.sweethoney.domain.common.dto.AuthUser;
import com.sparta.sweethoney.domain.common.exception.GlobalException;
import com.sparta.sweethoney.domain.common.exception.GlobalExceptionConst;
import com.sparta.sweethoney.domain.menu.dto.response.GetMenuResponseDto;
import com.sparta.sweethoney.domain.menu.entity.Menu;
import com.sparta.sweethoney.domain.menu.entity.MenuStatus;
import com.sparta.sweethoney.domain.menu.repository.MenuRepository;
import com.sparta.sweethoney.domain.store.dto.request.StoreRequest;
import com.sparta.sweethoney.domain.store.dto.response.StoreDetailResponse;
import com.sparta.sweethoney.domain.store.dto.response.StoreResponse;
import com.sparta.sweethoney.domain.store.entity.Store;
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

<<<<<<< HEAD
        // 사장님이 운영 중인 가게 수 제한 확인 (최대 3개)
//        if (storeRepository.countByOwner(owner) >= 3) {
//            throw new IllegalStateException("사장님은 최대 3개의 가게만 운영할 수 있습니다.");
//        }
=======
        // 사장님이 운영 중인 가게 수는 최대 3개까지 생성 가능
        if (storeRepository.countByUserId(owner.getId()) == 3) {
            throw new GlobalException(GlobalExceptionConst.MAX_STORE_LIMIT);
        }
>>>>>>> 5487380e2e759cbf7ba9d09f10331bbe573975d3

        // Entity 변환
        Store newStore = new Store(storeSaveRequest, owner);

        // DB에 저장하면서 StoreResponse 반환
        return mapToStoreResponse(storeRepository.save(newStore));
    }

    // 가게 수정 로직
    @Transactional
    public StoreResponse updateStore(Long storeId, StoreRequest storeUpdateRequest, AuthUser authUser) {
        // 가게가 존재하는지 조회
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new GlobalException(GlobalExceptionConst.NOT_FOUND_STORE));

        // 현재 로그인한 유저가 가게의 소유주인지 확인
        if (!store.getUser().getId().equals(authUser.getId())) {
            throw new GlobalException(GlobalExceptionConst.NOT_OWNER_OF_STORE);
        }

        // 가게 수정
        store.update(storeUpdateRequest);

        // Dto 반환
        return mapToStoreResponse(store);
    }

    //가게 일괄 조회
    public List<StoreResponse> getStores() {
        List<Store> storeList = storeRepository.findAll();

        return storeList.stream()
                .map(this::mapToStoreResponse)
                .collect(Collectors.toList());
    }

    //가게 단건 조회
    public StoreDetailResponse getStore(Long storeId) {
        // 가게가 존재하는지 조회
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new GlobalException(GlobalExceptionConst.NOT_FOUND_STORE));

        // 가게기준으로 활성화 상태인 메뉴만을 가져오는 로직
        List<GetMenuResponseDto> activeMenus = menuRepository.findByStoreId(storeId)
                .map(menus -> menus.stream()
                        .filter(menu -> menu.getStatus() == MenuStatus.ACTIVE)
                        .map(GetMenuResponseDto::new)
                        .toList())
                // 만약 메뉴가 없을 경우, NOT_FOUND_MENU 예외를 던짐
                .orElseThrow(() -> new GlobalException(GlobalExceptionConst.NOT_FOUND_MENU));

        // Dto 반환
        return new StoreDetailResponse(
                store.getId(),
                store.getName(),
                store.getOpenTime(),
                store.getCloseTime(),
                store.getMinOrderPrice(),
                activeMenus
        );
    }

    // 가게 폐업
    @Transactional
    public void deleteStore(Long storeId, AuthUser authUser) {
        // 가게가 존재하는지 조회
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new GlobalException(GlobalExceptionConst.NOT_FOUND_STORE));

        // 현재 로그인한 유저가 가게의 소유주인지 확인
        if (!store.getUser().getId().equals(authUser.getId())) {
            throw new GlobalException(GlobalExceptionConst.NOT_OWNER_OF_STORE);
        }

        // 등록되있는 메뉴가 있는지 확인 후
        List<Menu> menuList = menuRepository.findByStoreId(storeId).orElseThrow(() ->
                new GlobalException(GlobalExceptionConst.NOT_FOUND_MENU));

        // 활성화되어 있는 메뉴들은 비활성화 처리
        for (Menu menu : menuList) {
            if (menu.getStatus() == MenuStatus.ACTIVE) {
                menu.delete(MenuStatus.INACTIVE);
            }

            // 가게 폐업
            store.terminated();
        }
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
