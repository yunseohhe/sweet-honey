package com.sparta.sweethoney.domain.order.cart.controller;

import com.sparta.sweethoney.domain.common.annotation.Auth;
import com.sparta.sweethoney.domain.common.dto.AuthUser;
import com.sparta.sweethoney.domain.order.cart.dto.request.CartRequestDto;
import com.sparta.sweethoney.domain.order.cart.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping
    public void addMenuInCart(@Auth AuthUser authUser, @Validated CartRequestDto cartRequestDto) {
        Long userId = authUser.getId();

        cartService.addCart(userId, cartRequestDto);
    }
}
