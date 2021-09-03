package com.laundry.app.dto.address;

import java.util.ArrayList;

public class DistrictResponseDto {
    public String level2Id;
    public String name;
    public String type;
    public ArrayList<WardResponseDto> level3s;
    public boolean isSelected;

    public DistrictResponseDto(String level2Id, String name, String type, ArrayList<WardResponseDto> level3s) {
        this.level2Id = level2Id;
        this.name = name;
        this.type = type;
        this.level3s = level3s;
    }
}
