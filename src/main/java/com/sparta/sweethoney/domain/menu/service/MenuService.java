package com.sparta.sweethoney.domain.menu.service;

import com.sparta.sweethoney.domain.menu.dto.request.PutMenuRequestDto;
import com.sparta.sweethoney.domain.menu.dto.response.PutMenuResponseDto;
import com.sparta.sweethoney.domain.menu.repository.MenuRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MenuService {
    private final MenuRepository menuRepository;

    /* 메뉴 수정 */
    @Transactional
    public PutMenuResponseDto updateMenu(Long storeId, Long menuId, PutMenuRequestDto requestDto) {
        // 가게 조회

        // 메뉴 조회

        // 메뉴 수정

        // Dto 변환

        return null;
    }
}
