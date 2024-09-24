package com.sparta.sweethoney.domain.menu.controller;

import com.sparta.sweethoney.domain.common.annotation.Auth;
import com.sparta.sweethoney.domain.common.dto.AuthUser;
import com.sparta.sweethoney.domain.menu.dto.request.PostMenuRequestDto;
import com.sparta.sweethoney.domain.menu.dto.request.PutMenuRequestDto;
import com.sparta.sweethoney.domain.menu.dto.response.DeleteMenuResponseDto;
import com.sparta.sweethoney.domain.menu.dto.response.PostMenuResponseDto;
import com.sparta.sweethoney.domain.menu.dto.response.PutMenuResponseDto;
import com.sparta.sweethoney.domain.menu.service.MenuService;
import com.sparta.sweethoney.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/stores")
@RequiredArgsConstructor
public class MenuController {
    private final MenuService service;

    /**
     * 메뉴 추가 API
     * @param authUser 유저 ID, 유저 이름, 유저 이메일, 유저 권한
     * @param storeId 가게 ID
     * @param requestDto 메뉴 이름, 메뉴 가격, 메뉴 상태
     * @return 메뉴 ID, 메뉴 이름, 메뉴 가격, 메뉴 상태
     */
    @PostMapping("{storeId}/menus")
    public ResponseEntity<ApiResponse<PostMenuResponseDto>> addMenu(
            @Auth AuthUser authUser,
            @PathVariable(name = "storeId") Long storeId,
            @RequestPart(name = "requestDto") PostMenuRequestDto requestDto,
            @RequestPart(name = "image", required = false) MultipartFile image
            ) throws IOException {
        return ResponseEntity.ok(ApiResponse.success(service.addMenu(authUser, storeId, requestDto, image)));
    }


    /**
     * 메뉴 수정 API
     * @param storeId 가게 ID
     * @param menuId 메뉴 ID
     * @param requestDto 메뉴 이름, 메뉴 가격
     * @return 메뉴 ID, 메뉴 이름, 메뉴 가격
     */
    @PutMapping("/{storeId}/menus/{menuId}")
    public ResponseEntity<ApiResponse<PutMenuResponseDto>> updateMenu(
            @Auth AuthUser authUser,
            @PathVariable(name = "storeId") Long storeId,
            @PathVariable(name = "menuId") Long menuId,
            @RequestBody PutMenuRequestDto requestDto,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) throws IOException {
        return ResponseEntity.ok(ApiResponse.success(service.updateMenu(authUser, storeId, menuId, requestDto, image)));
    }

    /**
     * 메뉴 삭제
     * @param storeId 가게 ID
     * @param menuId 메뉴 ID
     * @return 삭제한 메뉴 ID
     */
    @DeleteMapping("/{storeId}/menus/{menuId}")
    public ResponseEntity<ApiResponse<DeleteMenuResponseDto>> deleteMenu(
            @Auth AuthUser authUser,
            @PathVariable(name = "storeId") Long storeId,
            @PathVariable(name = "menuId") Long menuId
    ) {
        return ResponseEntity.ok(ApiResponse.success(service.deleteMenu(authUser, storeId, menuId)));
    }
}
