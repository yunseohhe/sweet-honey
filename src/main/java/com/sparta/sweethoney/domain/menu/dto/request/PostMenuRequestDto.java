package com.sparta.sweethoney.domain.menu.dto.request;

import com.sparta.sweethoney.domain.menu.entity.MenuStatus;
import lombok.Getter;

@Getter
public class PostMenuRequestDto {
    private String name;
    private int price;
    private MenuStatus status;
}
