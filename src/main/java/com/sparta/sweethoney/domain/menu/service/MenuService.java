package com.sparta.sweethoney.domain.menu.service;

import com.sparta.sweethoney.domain.common.dto.AuthUser;
import com.sparta.sweethoney.domain.menu.dto.request.PostMenuRequestDto;
import com.sparta.sweethoney.domain.menu.dto.request.PutMenuRequestDto;
import com.sparta.sweethoney.domain.menu.dto.response.DeleteMenuResponseDto;
import com.sparta.sweethoney.domain.menu.dto.response.PostMenuResponseDto;
import com.sparta.sweethoney.domain.menu.dto.response.PutMenuResponseDto;
import com.sparta.sweethoney.domain.menu.entity.Menu;
import com.sparta.sweethoney.domain.menu.entity.MenuStatus;
import com.sparta.sweethoney.domain.menu.repository.MenuRepository;
import com.sparta.sweethoney.domain.store.entity.Store;
import com.sparta.sweethoney.domain.store.repository.StoreRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MenuService {
    private final MenuRepository menuRepository;
    private final StoreRepository storeRepository;

    /* 메뉴 생성 */
    @Transactional
    public PostMenuResponseDto addMenu(AuthUser authUser, Long storeId, PostMenuRequestDto requestDto) {
        // 유저 권한 확인
        MenuStatus status = requestDto.getStatus();

        // 가게 조회
        Store store = findStoreOrElseThrow(storeId);

        // Entity 변환
        Menu menu = new Menu(requestDto, status, store);

        // DB 저장 하면서 responseDto 반환
        return new PostMenuResponseDto(menuRepository.save(menu));
    }

    /* 메뉴 수정 */
    @Transactional
    public PutMenuResponseDto updateMenu(Long storeId, Long menuId, PutMenuRequestDto requestDto) {
        // 가게 조회
        Store store = findStoreOrElseThrow(storeId);

        // 메뉴 조회
        Menu menu = findMenuOrElseThrow(menuId, store.getId());

        // 메뉴 수정
        menu.update(requestDto);

        // Dto 반환
        return new PutMenuResponseDto(menu);
    }

    /* 메뉴 삭제 */
    @Transactional
    public DeleteMenuResponseDto deleteMenu(Long storeId, Long menuId) {
        // 가게 조회
        Store store = findStoreOrElseThrow(storeId);

        // 메뉴 조회
        Menu menu = findMenuOrElseThrow(menuId, store.getId());

        // 미판매인지 확인
        if (menu.getStatus() == MenuStatus.INACTIVE) {
            throw new IllegalArgumentException("해당 메뉴는 이미 미판매 입니다.");
        }

        // 메뉴 삭제(미판매로 변환)
        menu.delete(MenuStatus.INACTIVE);

        // Dto 반환
        return new DeleteMenuResponseDto(menu);
    }

    /* 가게 조회 */
    private Store findStoreOrElseThrow(Long storeId) {
        return storeRepository.findById(storeId).orElseThrow(() ->
                new IllegalArgumentException("해당 가게가 없습니다."));
    }

    /* 메뉴 조회 */
    private Menu findMenuOrElseThrow(Long menuId, Long storeId) {
        return menuRepository.findByIdAndStoreId(menuId, storeId).orElseThrow(() ->
                new IllegalArgumentException("해당 메뉴가 없습니다."));
    }
}
