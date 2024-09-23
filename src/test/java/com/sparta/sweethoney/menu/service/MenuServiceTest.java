package com.sparta.sweethoney.menu.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.sparta.sweethoney.domain.common.dto.AuthUser;
import com.sparta.sweethoney.domain.common.exception.menu.NotFoundMenuException;
import com.sparta.sweethoney.domain.menu.dto.request.PostMenuRequestDto;
import com.sparta.sweethoney.domain.menu.dto.request.PutMenuRequestDto;
import com.sparta.sweethoney.domain.menu.dto.response.DeleteMenuResponseDto;
import com.sparta.sweethoney.domain.menu.dto.response.PostMenuResponseDto;
import com.sparta.sweethoney.domain.menu.dto.response.PutMenuResponseDto;
import com.sparta.sweethoney.domain.menu.entity.Menu;
import com.sparta.sweethoney.domain.menu.entity.MenuStatus;
import com.sparta.sweethoney.domain.menu.repository.MenuRepository;
import com.sparta.sweethoney.domain.menu.service.MenuService;
import com.sparta.sweethoney.domain.store.entity.Store;
import com.sparta.sweethoney.domain.store.repository.StoreRepository;
import com.sparta.sweethoney.domain.user.entity.User;
import com.sparta.sweethoney.domain.user.entity.UserRole;
import com.sparta.sweethoney.domain.user.entity.UserStatus;
import com.sparta.sweethoney.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {
    @Mock
    private MenuRepository menuRepository;

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AmazonS3Client s3Client;

    @InjectMocks
    private MenuService menuService;

    @Test
    public void 메뉴_정상_등록() throws IOException {
        // given
        Long userId = 1L;
        AuthUser authUser = new AuthUser(userId, "name", "a@a.com", UserRole.OWNER);

        User user = new User("a@a.com", "name", "password", authUser.getUserRole(), UserStatus.ACTIVE);

        Long storeId = 1L;
        Store store = new Store();
        ReflectionTestUtils.setField(store, "id", storeId);

        PostMenuRequestDto PostRequestDto = new PostMenuRequestDto();
        ReflectionTestUtils.setField(PostRequestDto, "name", "라면");
        ReflectionTestUtils.setField(PostRequestDto, "price", 1000);
        ReflectionTestUtils.setField(PostRequestDto, "status", MenuStatus.ACTIVE);

        MultipartFile image = new MockMultipartFile(
                "test.jpg", "test.jpg", "image/jpeg", "test image content".getBytes()
        );
        ReflectionTestUtils.setField(menuService, "bucket", "test-bucket");

        Menu menu = new Menu(PostRequestDto, store, "http://test-url.com/test.jpg");
        ReflectionTestUtils.setField(menu, "id", 1L);

        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));
        given(storeRepository.findById(anyLong())).willReturn(Optional.of(store));
        when(s3Client.getUrl(anyString(), anyString())).thenReturn(new URL("http://test-url.com/test.jpg"));
        given(menuRepository.save(any())).willReturn(menu);

        // when
        PostMenuResponseDto responseDto = menuService.addMenu(authUser, storeId, PostRequestDto, image);

        // then
        assertNotNull(responseDto);
        assertEquals("라면", responseDto.getName());
        assertEquals(1000, responseDto.getPrice());
        assertEquals(1L, responseDto.getId());
        assertEquals("http://test-url.com/test.jpg", responseDto.getImageUrl());
    }

    @Test
    public void 메뉴_정상_수정() throws IOException {
        // given
        Long userId = 1L;
        AuthUser authUser = new AuthUser(userId, "name", "a@a.com", UserRole.OWNER);

        User user = new User("a@a.com", "name", "password", authUser.getUserRole(), UserStatus.ACTIVE);

        Long storeId = 1L;
        Store store = new Store();
        ReflectionTestUtils.setField(store, "id", storeId);

        PostMenuRequestDto PostRequestDto = new PostMenuRequestDto();
        ReflectionTestUtils.setField(PostRequestDto, "name", "라면");
        ReflectionTestUtils.setField(PostRequestDto, "price", 1000);
        ReflectionTestUtils.setField(PostRequestDto, "status", MenuStatus.ACTIVE);

        MultipartFile image = new MockMultipartFile(
                "test1.jpg", "test1.jpg", "image/jpeg", "test image content".getBytes()
        );
        ReflectionTestUtils.setField(menuService, "bucket", "test-bucket");

        Long menuId = 1L;
        Menu menu = new Menu(PostRequestDto, store, "http://test-url.com/test.jpg");
        ReflectionTestUtils.setField(menu, "id", menuId);

        // 변경
        PutMenuRequestDto requestDto = new PutMenuRequestDto();
        ReflectionTestUtils.setField(requestDto, "name", "신라면");
        ReflectionTestUtils.setField(requestDto, "price", 500);

        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));
        given(storeRepository.findById(anyLong())).willReturn(Optional.of(store));
        given(menuRepository.findByIdAndStoreId(menuId, store.getId())).willReturn(Optional.of(menu));
        when(s3Client.getUrl(anyString(), anyString())).thenReturn(new URL("http://test-url.com/test1.jpg"));

        // when
        PutMenuResponseDto responseDto = menuService.updateMenu(authUser, storeId, menuId, requestDto, image);

        // then
        assertNotNull(responseDto);
        assertEquals("신라면", responseDto.getName());
        assertEquals(500, responseDto.getPrice());
        assertEquals(1L, responseDto.getId());
        assertEquals("http://test-url.com/test1.jpg", responseDto.getImageUrl());
    }

    @Test
    public void 메뉴를_찾지_못하면_예외처리가_발생한다() throws IOException {
        // given
        Long userId = 1L;
        AuthUser authUser = new AuthUser(userId, "name", "a@a.com", UserRole.OWNER);

        User user = new User("a@a.com", "name", "password", authUser.getUserRole(), UserStatus.ACTIVE);

        Long storeId = 1L;
        Store store = new Store();
        ReflectionTestUtils.setField(store, "id", storeId);

        PostMenuRequestDto PostRequestDto = new PostMenuRequestDto();
        ReflectionTestUtils.setField(PostRequestDto, "name", "라면");
        ReflectionTestUtils.setField(PostRequestDto, "price", 1000);
        ReflectionTestUtils.setField(PostRequestDto, "status", MenuStatus.ACTIVE);

        MultipartFile image = new MockMultipartFile(
                "test.jpg", "test.jpg", "image/jpeg", "test image content".getBytes()
        );
        ReflectionTestUtils.setField(menuService, "bucket", "test-bucket");

        Long menuId = 1L;
        Menu menu = new Menu(PostRequestDto, store, "http://test-url.com/test.jpg");
        ReflectionTestUtils.setField(menu, "id", menuId);

        PutMenuRequestDto requestDto = new PutMenuRequestDto();
        ReflectionTestUtils.setField(requestDto, "name", "신라면");
        ReflectionTestUtils.setField(requestDto, "price", 500);

        given(userRepository.findById(authUser.getId())).willReturn(Optional.of(user));
        given(storeRepository.findById(store.getId())).willReturn(Optional.of(store));
        given(menuRepository.findByIdAndStoreId(menuId, store.getId())).willReturn(Optional.empty());
        when(s3Client.getUrl(anyString(), anyString())).thenReturn(new URL("http://test-url.com/test.jpg"));

        // when
        NotFoundMenuException exception = assertThrows(NotFoundMenuException.class , () ->{
                    menuService.updateMenu(authUser, storeId, menuId, requestDto, image);
                });

        // then
        assertEquals("NOT_FOUND_MENU 해당 메뉴가 존재하지 않습니다.", exception.getMessage());
        assertEquals(400, exception.getHttpStatus().value());
    }

    @Test
    public void 메뉴_정상_삭제() throws IOException {
        // given
        Long userId = 1L;
        AuthUser authUser = new AuthUser(userId, "name", "a@a.com", UserRole.OWNER);

        User user = new User("a@a.com", "name", "password", authUser.getUserRole(), UserStatus.ACTIVE);

        Long storeId = 1L;
        Store store = new Store();
        ReflectionTestUtils.setField(store, "id", storeId);

        PostMenuRequestDto PostRequestDto = new PostMenuRequestDto();
        ReflectionTestUtils.setField(PostRequestDto, "name", "라면");
        ReflectionTestUtils.setField(PostRequestDto, "price", 1000);
        ReflectionTestUtils.setField(PostRequestDto, "status", MenuStatus.ACTIVE);

        MultipartFile image = new MockMultipartFile(
                "test.jpg", "test.jpg", "image/jpeg", "test image content".getBytes()
        );
        ReflectionTestUtils.setField(menuService, "bucket", "test-bucket");

        Long menuId = 1L;
        Menu menu = new Menu(PostRequestDto, store, "http://test-url.com/test.jpg");
        ReflectionTestUtils.setField(menu, "id", menuId);

        given(userRepository.findById(authUser.getId())).willReturn(Optional.of(user));
        given(storeRepository.findById(store.getId())).willReturn(Optional.of(store));
        given(menuRepository.findByIdAndStoreId(menuId, store.getId())).willReturn(Optional.of(menu));

        // when
        DeleteMenuResponseDto responseDto = menuService.deleteMenu(authUser, storeId, menuId);

        // then
        assertNotNull(responseDto);
        assertEquals(1L, responseDto.getId());
        assertEquals(MenuStatus.INACTIVE, menu.getStatus());
    }
}
