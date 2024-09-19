package com.sparta.sweethoney.domain.menu.service;

import com.sparta.sweethoney.domain.menu.dto.request.PutMenuRequestDto;
import com.sparta.sweethoney.domain.menu.dto.response.DeleteMenuResponseDto;
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

    /* 메뉴 수정 */
    @Transactional
    public PutMenuResponseDto updateMenu(Long storeId, Long menuId, PutMenuRequestDto requestDto) {
        // 가게 조회
        Store store = storeRepository.findById(storeId).orElseThrow(() ->
                new IllegalArgumentException("해당 가게가 없습니다."));

        // 메뉴 조회
        Menu menu = menuRepository.findByIdAndStoreId(menuId, store.getId()).orElseThrow(() ->
                new IllegalArgumentException("해당 메뉴가 없습니다."));

        // 메뉴 수정
        menu.update(requestDto);

        // Dto 변환
        PutMenuResponseDto responseDto = new PutMenuResponseDto(menu);

        return responseDto;
    }

    @Transactional
    public DeleteMenuResponseDto deleteMenu(Long storeId, Long menuId) {
        // 가게 조회
        Store store = storeRepository.findById(storeId).orElseThrow(() ->
                new IllegalArgumentException("해당 가게가 없습니다."));

        // 메뉴 조회
        Menu menu = menuRepository.findByIdAndStoreId(menuId, store.getId()).orElseThrow(() ->
                new IllegalArgumentException("해당 메뉴가 없습니다."));

        // 미판매인지 확인
        if (menu.getStatus() == MenuStatus.INACTIVE) {
            throw new IllegalArgumentException("해당 메뉴는 이미 미판매 입니다.");
        }

        // 메뉴 삭제(미판매로 변환)
        menu.delete(MenuStatus.INACTIVE);

        // dto 변환
        DeleteMenuResponseDto responseDto = new DeleteMenuResponseDto(menu);

        return responseDto;
    }
}
