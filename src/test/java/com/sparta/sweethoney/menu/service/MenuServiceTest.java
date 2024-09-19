package com.sparta.sweethoney.menu.service;

import com.sparta.sweethoney.domain.menu.dto.request.PutMenuRequestDto;
import com.sparta.sweethoney.domain.menu.dto.response.PutMenuResponseDto;
import com.sparta.sweethoney.domain.menu.entity.Menu;
import com.sparta.sweethoney.domain.menu.entity.MenuStatus;
import com.sparta.sweethoney.domain.menu.repository.MenuRepository;
import com.sparta.sweethoney.domain.menu.service.MenuService;
import com.sparta.sweethoney.domain.store.entity.Store;
import com.sparta.sweethoney.domain.store.repository.StoreRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {
    @Mock
    private MenuRepository menuRepository;

    @Mock
    private StoreRepository storeRepository;

    @InjectMocks
    private MenuService menuService;

    @Test
    public void 메뉴_정상_수정() {
        // given
        Long storeId = 1L;
        Store store = new Store();
        ReflectionTestUtils.setField(store, "id", storeId);

        Long menuId = 1L;
        Menu menu = new Menu("라면", 1000, MenuStatus.ACTIVE, store);
        ReflectionTestUtils.setField(menu, "id", menuId);

        PutMenuRequestDto requestDto = new PutMenuRequestDto();
        ReflectionTestUtils.setField(requestDto, "name", "신라면");
        ReflectionTestUtils.setField(requestDto, "price", 500);

        given(storeRepository.findById(store.getId())).willReturn(Optional.of(store));
        given(menuRepository.findByIdAndStoreId(menuId, store.getId())).willReturn(Optional.of(menu));

        // when
        PutMenuResponseDto responseDto = menuService.updateMenu(storeId, menuId, requestDto);

        // then
        assertNotNull(responseDto);
        assertEquals("신라면", responseDto.getName());
        assertEquals(500, responseDto.getPrice());
        assertEquals(1L, responseDto.getId());
    }
}
