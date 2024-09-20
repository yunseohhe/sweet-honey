package com.sparta.sweethoney.domain.menu.controller;

import com.sparta.sweethoney.domain.common.annotation.Auth;
import com.sparta.sweethoney.domain.common.dto.AuthUser;
import com.sparta.sweethoney.domain.menu.dto.request.PostMenuRequestDto;
import com.sparta.sweethoney.domain.menu.dto.request.PutMenuRequestDto;
import com.sparta.sweethoney.domain.menu.dto.response.DeleteMenuResponseDto;
import com.sparta.sweethoney.domain.menu.dto.response.PostMenuResponseDto;
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
    @PostMapping("{storeId}/menus")
    public PostMenuResponseDto addMenu(
            @Auth AuthUser authUser,
            @PathVariable Long storeId,
            @RequestBody PostMenuRequestDto requestDto
    ) {
        return service.addMenu(authUser, storeId, requestDto);
    }


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

    /**
     * 메뉴 삭제
     * @param storeId 가게 ID
     * @param menuId 메뉴 ID
     * @return 삭제한 메뉴 ID
     */
    @DeleteMapping("/{storeId}/menus/{menuId}")
    public DeleteMenuResponseDto deleteMenu(
            @PathVariable Long storeId,
            @PathVariable Long menuId
    ) {
        return service.deleteMenu(storeId, menuId);
    }
}
