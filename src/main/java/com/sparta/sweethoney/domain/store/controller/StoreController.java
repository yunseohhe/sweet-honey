package com.sparta.sweethoney.domain.store.controller;

import com.sparta.sweethoney.domain.common.annotation.Auth;
import com.sparta.sweethoney.domain.common.dto.AuthUser;
import com.sparta.sweethoney.domain.store.dto.request.StoreRequest;
import com.sparta.sweethoney.domain.store.enums.AdStatus;
import com.sparta.sweethoney.domain.store.service.StoreService;
import com.sparta.sweethoney.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stores")
public class StoreController {
    private final StoreService storeService;

    /**
     * 가게 생성
     *
     * @param authUser         로그인한 유저
     * @param storeSaveRequest 가게 생성에 필요한 정보(가게이름, 가게오픈시간, 가게마감시간, 가게 최소 가격)
     * @return 생성된 가게의 정보(가게 ID, 가게 이름, 가게 오픈시간, 가게 마감 시간, 가게 최소 가격)
     */
    @PostMapping
    public ResponseEntity<ApiResponse<?>> createStore(
            @Auth AuthUser authUser,
            @RequestBody StoreRequest storeSaveRequest
    ) {
        return ResponseEntity.ok(ApiResponse.success(storeService.createStore(authUser, storeSaveRequest)));
    }

    /**
     * 가게 수정
     *
     * @param storeId            가게 ID
     * @param storeUpdateRequest 수정할 가게의 정보(가게이름, 가게오픈시간, 가게마감시간, 가게 최소 가격)
     * @param authUser           로그인한 유저
     * @return 수정된 가게 정보(가게 ID, 이름, 가게 오픈시간, 가게 마감 시간, 가게 최소 가격)
     */
    @PutMapping("/{storeId}")
    public ResponseEntity<ApiResponse<?>> updateStore(
            @PathVariable("storeId") Long storeId,
            @RequestBody StoreRequest storeUpdateRequest,
            @Auth AuthUser authUser
    ) {
        return ResponseEntity.ok(ApiResponse.success(storeService.updateStore(storeId, storeUpdateRequest, authUser)));
    }

    /**
     * 가게 광고 상태 설정/해제
     *
     * @param storeId  광고 상태를 설정할 가게의 ID
     * @param authUser 로그인한 유저
     * @param adStatus 설정할 광고 상태 (AdStatus.NONE 또는 AdStatus.ADVERTISED)
     * @return 광고 상태 변경 성공 메시지
     */
    @PostMapping("/{storeId}/set-ad-status")
    public ResponseEntity<ApiResponse<?>> setAdStatus(
            @PathVariable("storeId") Long storeId,
            @Auth AuthUser authUser,
            @RequestParam(name = "AdStatus") AdStatus adStatus
    ) {
        storeService.setAdStatus(storeId, authUser, adStatus);
        return ResponseEntity.ok(ApiResponse.success("광고 상태가 변경되었습니다."));
    }

    /**
     * 가게 일괄 조회하는 로직
     *
     * @return 모든 가게 정보 목록(가게 ID, 가게 이름, 가게 오픈시간, 가게 마감 시간, 가게 최소 가격)
     */
    @GetMapping
    public ResponseEntity<ApiResponse<?>> getStores(@RequestParam(name = "name", required = false) String name) {
        return ResponseEntity.ok(ApiResponse.success(storeService.getStores(name)));
    }

    /**
     * 가게 단건 조회 (메뉴 포함)
     *
     * @param storeId 조회할 가게의 ID
     * @return 조회한 가게 상세 정보(가게 ID, 가게 이름, 가게 오픈시간, 가게 마감 시간, 가게 최소 가격)와 메뉴 정보
     */
    @GetMapping("/{storeId}")
    public ResponseEntity<ApiResponse<?>> getStore(@PathVariable("storeId") Long storeId) {
        return ResponseEntity.ok(ApiResponse.success(storeService.getStore(storeId)));
    }

    /**
     * 가게 폐업
     *
     * @param storeId 폐업할 가게 ID
     */
    @DeleteMapping("/{storeId}")
    public ResponseEntity<ApiResponse<?>> deleteStore(@PathVariable("storeId") Long storeId, @Auth AuthUser authUser) {
        storeService.deleteStore(storeId, authUser);
        return ResponseEntity.ok(ApiResponse.success("폐업이 완료되었습니다."));
    }
}
