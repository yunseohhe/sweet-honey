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
     * @param authUser 로그인 되있는 사용자(OWNER)
     * @param storeSaveRequest 가게이름, 가게오픈시간, 가게마감시간, 가게 최소 가격
     * @return 가게 ID, 가게 이름, 가게 오픈시간, 가게 마감 시간, 가게 최소 가격
     *
     * @author 황윤서
     */
    @PostMapping("/stores")
    public ResponseEntity<StoreResponse> createStore(
            @Auth AuthUser authUser,
            @Valid @RequestBody StoreRequest storeSaveRequest
    ) {
        return ResponseEntity.ok(storeService.createStore(authUser, storeSaveRequest));
    }

    /**
     * 가게 수정
     *
     * @param storeId 가게 ID
     * @param storeUpdateRequest 수정할 가게이름, 가게오픈시간, 가게마감시간, 가게 최소 가격
     * @param authUser 로그인 되있는 사용자(OWNER)
     * @return 가게 ID, 수정된 가게 이름, 가게 오픈시간, 가게 마감 시간, 가게 최소 가격
     *
     * @author 황윤서
     */
    @PutMapping("/stores/{storeId}")
    public ResponseEntity<StoreResponse> updateStore(
            @PathVariable Long storeId,
            @Valid @RequestBody StoreRequest storeUpdateRequest,
            @Auth AuthUser authUser
    ) {
        return ResponseEntity.ok(storeService.updateStore(storeId, storeUpdateRequest, authUser));
    }

    /**
     * 가게 일괄 조회하는 로직
     *
     * @return 가게 ID, 가게 이름, 가게 오픈시간, 가게 마감 시간, 가게 최소 가격 등 일괄 목록
     *
     * @author 황윤서
     */
    @GetMapping("/stores")
    public ResponseEntity<List<StoreResponse>> getStores() {
        return ResponseEntity.ok(storeService.getStores());
    }

    /**
     * 가게 단건 조회 (메뉴 포함)
     *
     * @param storeId 가게 ID
     * @return 가게 ID, 가게 이름, 가게 오픈시간, 가게 마감 시간, 가게 최소 가격 등 단건 목록
     *
     * @author 황윤서
     */
    @GetMapping("/stores/{storeId}")
    public ResponseEntity<StoreDetailResponse> getStore(@PathVariable("storeId") Long storeId) {
        return ResponseEntity.ok(storeService.getStore(storeId));
    }

    /**
     *
     * @param storeId 가게 ID
     *
     * @author 황윤서
     */
//    @DeleteMapping("/stores/{storeId}")
//    public void deleteStore(@PathVariable("storeId") Long storeId) {
//        storeService.deleteStore(storeId);
//    }
}
