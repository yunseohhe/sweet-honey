package com.sparta.sweethoney.domain.store.controller;

import com.sparta.sweethoney.domain.common.annotation.Auth;
import com.sparta.sweethoney.domain.common.dto.AuthUser;
import com.sparta.sweethoney.domain.store.dto.request.StoreRequest;
import com.sparta.sweethoney.domain.store.dto.response.StoreDetailResponse;
import com.sparta.sweethoney.domain.store.dto.response.StoreResponse;
import com.sparta.sweethoney.domain.store.service.StoreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class StoreController {
    private final StoreService storeService;

    /**
     * 가게 생성
     *
     * @param authUser 로그인한 유저
     * @param storeSaveRequest 가게 생성에 필요한 정보(가게이름, 가게오픈시간, 가게마감시간, 가게 최소 가격)
     * @return 생성된 가게의 정보(가게 ID, 가게 이름, 가게 오픈시간, 가게 마감 시간, 가게 최소 가격)
     *
     */
    @PostMapping("/stores")
    public ResponseEntity<StoreResponse> createStore(
            @Auth AuthUser authUser,
            @RequestBody StoreRequest storeSaveRequest
    ) {
        return ResponseEntity.ok(storeService.createStore(authUser, storeSaveRequest));
    }

    /**
     * 가게 수정
     *
     * @param storeId 가게 ID
     * @param storeUpdateRequest 수정할 가게의 정보(가게이름, 가게오픈시간, 가게마감시간, 가게 최소 가격)
     * @param authUser 로그인한 유저
     * @return 수정된 가게 정보(가게 ID, 이름, 가게 오픈시간, 가게 마감 시간, 가게 최소 가격)
     *
     */
    @PutMapping("/stores/{storeId}")
    public ResponseEntity<StoreResponse> updateStore(
            @PathVariable("storeId") Long storeId,
            @RequestBody StoreRequest storeUpdateRequest,
            @Auth AuthUser authUser
    ) {
        return ResponseEntity.ok(storeService.updateStore(storeId, storeUpdateRequest, authUser));
    }

    /**
     * 가게 일괄 조회하는 로직
     *
     * @return 모든 가게 정보 목록(가게 ID, 가게 이름, 가게 오픈시간, 가게 마감 시간, 가게 최소 가격)
     *
     */
    @GetMapping("/stores")
    public ResponseEntity<List<StoreResponse>> getStores() {
        return ResponseEntity.ok(storeService.getStores());
    }

    /**
     * 가게 단건 조회 (메뉴 포함)
     *
     * @param storeId 조회할 가게의 ID
     * @return 조회한 가게 상세 정보(가게 ID, 가게 이름, 가게 오픈시간, 가게 마감 시간, 가게 최소 가격)
     *
     */
    @GetMapping("/stores/{storeId}")
    public ResponseEntity<StoreDetailResponse> getStore(@PathVariable("storeId") Long storeId) {
        return ResponseEntity.ok(storeService.getStore(storeId));
    }

    /**
     * 가게 폐업
     *
     * @param storeId 폐업할 가게 ID
     *
     */
    @DeleteMapping("/stores/{storeId}")
    public void deleteStore(@PathVariable("storeId") Long storeId, @Auth AuthUser authUser) {
        storeService.deleteStore(storeId, authUser);
    }
}
