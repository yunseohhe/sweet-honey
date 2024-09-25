package com.sparta.sweethoney.domain.user.util;

import com.sparta.sweethoney.domain.user.entity.User;
import com.sparta.sweethoney.domain.user.entity.UserRole;
import com.sparta.sweethoney.domain.user.entity.UserStatus;

public class UserMockDataUtil {

    public static User user() {
        return new User("email", "name", "password", UserRole.GUEST, UserStatus.ACTIVE);
    }
}
