package com.laundry.app.view.adapter;

import com.laundry.app.R;
import com.laundry.app.dto.servicelist.ServiceListDto;
import com.laundry.base.BaseDisplay;

public class ServiceListDisplay extends BaseDisplay<ServiceListDto> {

    @Override
    public int getIcon() {
        switch (data.getServiceType()) {
            case CLEAN:
                return R.drawable.wash_and_iron_icon;
            case DRY_CLEAN_AND_IRON:
                return R.drawable.ironing_icon;
            case DRY_CLEAN:
                return R.drawable.dry_cleaning_icon;
            case BLANKET:
                return R.drawable.wash_blanket_icon;
            default:
                return INVALID_RESOURCE;
        }
    }
}
