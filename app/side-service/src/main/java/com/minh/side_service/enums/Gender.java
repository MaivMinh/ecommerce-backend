package com.minh.side_service.enums;

import lombok.Getter;

@Getter
public enum Gender {
    MALE("Nam"),
    FEMALE("Nữ"),
    OTHER("Khác");

    private final String description;

    Gender(String description) {
        this.description = description;
    }

}
