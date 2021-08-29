package com.laundry.app.view.activity;

import android.content.Intent;
import android.view.View;

import com.laundry.app.R;
import com.laundry.app.constant.Constant;
import com.laundry.app.databinding.ActivitySwitchModeBinding;
import com.laundry.app.dto.Role;
import com.laundry.base.BaseActivity;

public class SwitchModeActivity extends BaseActivity<ActivitySwitchModeBinding> implements View.OnClickListener {

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_switch_mode;
    }

    @Override
    public void onInitView() {
        getBinding().switchToCustomer.setOnClickListener(this);
        getBinding().switchToShipper.setOnClickListener(this);
    }

    @Override
    public void onViewClick() {

    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this, HomeActivity.class);
        switch (view.getId()) {
            case R.id.switch_to_customer:
                intent.putExtra(Constant.ROLE_SWITCH, Role.CUSTOMER);
                break;
            case R.id.switch_to_shipper:
                intent.putExtra(Constant.ROLE_SWITCH, Role.SHIPPER);
                break;
            default:
                break;
        }
        startActivity(intent);
    }
}