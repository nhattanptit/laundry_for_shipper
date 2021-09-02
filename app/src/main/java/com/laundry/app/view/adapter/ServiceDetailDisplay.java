package com.laundry.app.view.adapter;



import com.laundry.app.R;
import com.laundry.app.dto.sevicedetail.ServiceDetailDto;
import com.laundry.base.BaseDisplay;

public class ServiceDetailDisplay extends BaseDisplay<ServiceDetailDto> {

    @Override
    protected int getIcon() {
        switch (data.getServiceDetailType()){
            case T_SHIRT:
                return R.drawable.tshirt_icon;
            case OUTER_WEAR:
                return R.drawable.outer_wear_icon;
            case BOTTOM:
                return R.drawable.bottom_icon;
            case DRESSES:
                return R.drawable.dress_icon;
            case HOME:
                return R.drawable.home_curtain_icon;
            case OTHER:
            case WOOLEN_BlANKET:
            case DUVET:
            case COMFORTER:
                return R.drawable.other_icon;
            default:
                return INVALID_RESOURCE;
        }
    }
}
