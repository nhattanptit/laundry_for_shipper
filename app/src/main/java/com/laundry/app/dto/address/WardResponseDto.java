package com.laundry.app.dto.address;

public class WardResponseDto {

    public String level3Id;
    public String name;
    public String type;
    public boolean isSelected;

    public WardResponseDto(String level3Id, String name, String type) {
        this.level3Id = level3Id;
        this.name = name;
        this.type = type;
    }
}
