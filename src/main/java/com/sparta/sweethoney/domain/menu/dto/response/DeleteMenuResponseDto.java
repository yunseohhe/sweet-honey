package com.sparta.sweethoney.domain.menu.dto.response;

import com.sparta.sweethoney.domain.menu.entity.Menu;
import lombok.Getter;

@Getter
public class DeleteMenuResponseDto {
    private Long id;

    public DeleteMenuResponseDto(Menu menu) {
        this.id = menu.getId();
    }
}
