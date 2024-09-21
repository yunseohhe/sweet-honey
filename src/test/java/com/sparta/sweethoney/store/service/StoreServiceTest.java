package com.sparta.sweethoney.store.service;

import com.sparta.sweethoney.domain.common.dto.AuthUser;
import com.sparta.sweethoney.domain.common.exception.GlobalException;
import com.sparta.sweethoney.domain.menu.repository.MenuRepository;
import com.sparta.sweethoney.domain.store.dto.request.StoreRequest;
import com.sparta.sweethoney.domain.store.dto.response.StoreResponse;
import com.sparta.sweethoney.domain.store.entity.Store;
import com.sparta.sweethoney.domain.store.repository.StoreRepository;
import com.sparta.sweethoney.domain.store.service.StoreService;
import com.sparta.sweethoney.domain.user.entity.User;
import com.sparta.sweethoney.domain.user.entity.UserRole;
import com.sparta.sweethoney.domain.user.entity.UserStatus;
import com.sparta.sweethoney.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;


import java.sql.Ref;
import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class StoreServiceTest {
    @Mock
    private StoreRepository storeRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MenuRepository menuRepository;

    @InjectMocks
    private StoreService storeService;

    @Test
    public void 가게_생성_정상() {
        // given : owner인 유저가 주어지고, 가게 생성 요청 정보도 주어지고
        Long userId = 1L;
        AuthUser authUser = new AuthUser(userId, "name", "test@gamil.com", UserRole.OWNER);
        User user = new User("test@gamil.com", "name", "password", authUser.getUserRole(), UserStatus.ACTIVE);

        StoreRequest storeRequest = new StoreRequest("test가게이름", LocalTime.of(7, 0), LocalTime.of(21, 0), 1000);
        Store newStore = new Store(storeRequest, user);

        given(userRepository.findById(authUser.getId())).willReturn(Optional.of(user));
        given(storeRepository.countByUserId(user.getId())).willReturn(0);
        given(storeRepository.save(any())).willReturn(newStore);

        // when : 주어진 유저가 가게 생성을 요청하면
        StoreResponse response = storeService.createStore(authUser, storeRequest);

        // then : 가게가 생성되고, StoreResponse가 반환
        assertNotNull(response);
        assertEquals("test가게이름", response.getName());
        assertEquals(LocalTime.of(7, 0), response.getOpenTime());
        assertEquals(LocalTime.of(21, 0), response.getCloseTime());
        assertEquals(1000, response.getMinOrderPrice());
    }

    @Test
    public void 가게_수정_정상() {
        // given : owner인 유저가 주어지고, 가게 정보도 주어지고, 수정할 가게 요청 정보가 주어진다
        Long userId = 1L;
        AuthUser authUser = new AuthUser(userId, "name", "test@gamil.com", UserRole.OWNER);
        User user = new User("test@gamil.com", "name", "password", UserRole.OWNER, UserStatus.ACTIVE);
        ReflectionTestUtils.setField(user, "id", userId);

        Long storeId = 1L;
        StoreRequest storeRequest = new StoreRequest("원래가게이름", LocalTime.of(7, 0), LocalTime.of(21, 0), 1000);
        Store originalStore = new Store(storeRequest, user);
        ReflectionTestUtils.setField(originalStore, "id", storeId);

        StoreRequest storeUpdateRequest = new StoreRequest("수정된가게이름", LocalTime.of(8, 0), LocalTime.of(22, 0), 1500);

        given(storeRepository.findById(anyLong())).willReturn(Optional.of(originalStore));

        // when : 가게 수정 요청
        StoreResponse response = storeService.updateStore(storeId, storeUpdateRequest, authUser);


        // then : 가게가 수정되고, StoreResponse가 반환
        assertNotNull(response);
        assertEquals("수정된가게이름", response.getName());
        assertEquals(LocalTime.of(8, 0), response.getOpenTime());
        assertEquals(LocalTime.of(22, 0), response.getCloseTime());
        assertEquals(1500, response.getMinOrderPrice());
    }

    @Test
    public void 가게_수정_실패_로그인한_유저와_가게의_소유주가_불일치 () {
        // given : owner인 유저가 주어지고, 가게 정보도 주어지고, 수정할 가게 요청 정보가 주어진다
        Long userId = 1L;
        AuthUser authUser = new AuthUser(userId, "name", "test@gamil.com", UserRole.OWNER);
        User user = new User("test@gamil.com", "name", "password", UserRole.OWNER, UserStatus.ACTIVE);
        ReflectionTestUtils.setField(user, "id", userId);

        User user2 = new User("test2@gamil.com", "name2", "password", UserRole.OWNER, UserStatus.ACTIVE);
        ReflectionTestUtils.setField(user2, "id", 2L);

        Long storeId = 1L;
        StoreRequest storeRequest = new StoreRequest("원래가게이름", LocalTime.of(7, 0), LocalTime.of(21, 0), 1000);
        Store originalStore = new Store(storeRequest, user2);
        ReflectionTestUtils.setField(originalStore, "id", storeId);

        given(storeRepository.findById(anyLong())).willReturn(Optional.of(originalStore));


        // when & then: 소유주가 아닐 때 예외 발생 확인
        GlobalException exception = assertThrows(GlobalException.class, () -> {
            storeService.updateStore(storeId, new StoreRequest("수정된가게이름", LocalTime.of(8, 0), LocalTime.of(22, 0), 1500), authUser);
        });

        // 예외 메시지 검증
        assertEquals("NOT_OWNER_OF_STORE 해당 가게의 소유자가 아닙니다.", exception.getMessage());
    }
}
