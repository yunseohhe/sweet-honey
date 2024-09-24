package com.sparta.sweethoney.domain.order.cart.service;

import com.sparta.sweethoney.domain.common.exception.menu.NotFoundMenuException;
import com.sparta.sweethoney.domain.menu.entity.Menu;
import com.sparta.sweethoney.domain.menu.repository.MenuRepository;
import com.sparta.sweethoney.domain.order.cart.Cart;
import com.sparta.sweethoney.domain.order.cart.dto.request.CartRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {
    private final RedisTemplate<String, Cart> rt;
    private final MenuRepository menuRepository;

    public void addCart(Long userId, CartRequestDto cartRequestDto) {
        //회원의 장바구니 조회
        Cart cart = getCart(userId);

        //24시간이 넘으면 기존 장바구니 삭제
        if (isExpiredTime(cart)) {
            cart.clear();
        }

        //다른 가게 주문이면 기존 장바구니 삭제
        if (isNotSameStore(cartRequestDto.getStoreId(), cart)) {
            cart.clear();
        }

        Long menuId = cartRequestDto.getMenuId();
        Menu menu = menuRepository.findById(menuId).orElseThrow(NotFoundMenuException::new);

        cart.getMenus().add(menu);
    }

    /* 회원의 장바구니 조회 */
    private Cart getCart(Long userId) {
        //Redis key
        String key = "cartUser:" + userId;
        Optional<Cart> optionalCart = Optional.ofNullable(rt.opsForValue().get(key));
        Cart cart = optionalCart.orElseGet(Cart::new);
        return cart;
    }

    /* 마지막 업데이트 이후로 24시간이 지났는지 검증 */
    private static boolean isExpiredTime(Cart cart) {
        return Duration.between(cart.getLastUpdate(), LocalDateTime.now()).toHours() >= 24;
    }

    /* 다른 가게 메뉴 검증 */
    private static boolean isNotSameStore(Long storeId, Cart cart) {
        return !(cart.getStoreId().equals(storeId));
    }


}
