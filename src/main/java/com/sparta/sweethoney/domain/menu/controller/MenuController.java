package com.sparta.sweethoney.domain.menu.controller;

import com.sparta.sweethoney.domain.menu.dto.request.PutMenuRequestDto;
import com.sparta.sweethoney.domain.menu.dto.response.PutMenuResponseDto;
import com.sparta.sweethoney.domain.menu.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stores")
@RequiredArgsConstructor
public class MenuController {
    private final MenuService service;

    // 메뉴 생성

    /**
     * 메뉴 수정 API
     * @param storeId 가게 ID
     * @param menuId 메뉴 ID
     * @param requestDto 메뉴 이름, 메뉴 가격
     * @return 메뉴 ID, 메뉴 이름, 메뉴 가격
     */
    @PutMapping("/{storeId}/menus/{menuId}")
    public PutMenuResponseDto updateMenu(
            @PathVariable Long storeId,
            @PathVariable Long menuId,
            @RequestBody PutMenuRequestDto requestDto
    ) {
        return service.updateMenu(storeId, menuId, requestDto);
    }

    // 메뉴 삭제
}
