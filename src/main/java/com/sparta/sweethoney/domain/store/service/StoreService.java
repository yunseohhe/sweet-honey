package com.sparta.sweethoney.domain.store.service;

import com.sparta.sweethoney.domain.common.dto.AuthUser;
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
        User owner = userRepository.findById(authUser.getId())
                .orElseThrow(() -> new IllegalArgumentException("사장님을 찾을 수 없습니다."));

        // 사장님이 운영 중인 가게 수 제한 확인 (최대 3개)
//        if (storeRepository.countByOwner(owner) >= 3) {
//            throw new IllegalStateException("사장님은 최대 3개의 가게만 운영할 수 있습니다.");
//        }

        Store newStore = new Store(
                storeSaveRequest.getName(),
                storeSaveRequest.getOpenTime(),
                storeSaveRequest.getCloseTime(),
                storeSaveRequest.getMinOrderPrice(),
                owner);

        Store savedStore = storeRepository.save(newStore);

        return mapToStoreResponse(savedStore);
    }

    // 가게 수정 로직
    @Transactional
    public StoreResponse updateStore(Long storeId, StoreRequest storeUpdateRequest, AuthUser authUser) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("가게를 찾을 수 없습니다."));

        // 가게의 소유주가 현재 로그인한 유저인지 확인
        if (!store.getUser().getId().equals(authUser.getId())) {
            throw new IllegalStateException("해당 가게의 소유자가 아닙니다.");
        }

        store.update(
                storeUpdateRequest.getName(),
                storeUpdateRequest.getOpenTime(),
                storeUpdateRequest.getCloseTime(),
                storeUpdateRequest.getMinOrderPrice());

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
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("가게를 찾을 수 없습니다."));

        // 가게에 등록된 메뉴 목록 조회
        List<Menu> menus = menuRepository.findByStoreId(storeId)
                .orElseThrow(() -> new IllegalArgumentException("해당 가게에 등록된 메뉴가 없습니다."));

        List<GetMenuResponseDto> activeMenus = menus.stream()
                .filter(menu -> menu.getStatus() == MenuStatus.ACTIVE)  // ACTIVE 상태인 메뉴만 필터링
                .map(GetMenuResponseDto::new)  // GetMenuResponseDto로 변환
                .toList();

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
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("가게를 찾을 수 없습니다."));

        if (!store.getUser().getId().equals(authUser.getId())) {
            throw new IllegalStateException("해당 가게의 소유자가 아닙니다.");
        }

        List<Menu> menuList = menuRepository.findByStoreId(storeId).orElseThrow(() ->
                new IllegalArgumentException("해당 가게의 메뉴가 없습니다."));

        for (Menu menu : menuList) {
            menu.delete(MenuStatus.INACTIVE);
        }

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
