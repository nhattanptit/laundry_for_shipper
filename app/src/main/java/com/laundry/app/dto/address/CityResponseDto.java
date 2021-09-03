package com.laundry.app.dto.address;

import java.util.ArrayList;

public class CityResponseDto {
    public String level1Id;
    public String name;
    public String type;
    public ArrayList<DistrictResponseDto> level2s;
    public boolean isSelected;

    public CityResponseDto(String level1Id, String name, String type, ArrayList<DistrictResponseDto> level2s) {
        this.level1Id = level1Id;
        this.name = name;
        this.type = type;
        this.level2s = level2s;
    }
}
