package com.laundry.app.view.fragment;

import com.laundry.app.R;
import com.laundry.app.model.Setting;
import com.laundry.app.view.adapter.SettingAdapter;
import com.laundry.app.model.Student;
import com.laundry.app.databinding.SettingFragmentBinding;
import com.laundry.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

public class SettingFragment extends BaseFragment<SettingFragmentBinding> {

    List<Object> settings = new ArrayList<>();

    @Override
    protected int getLayoutResource() {
        return R.layout.setting_fragment;
    }

    @Override
    public void onPreInitView() {
        settings.add(new Student("1"));
        settings.add(new Setting("a"));
        settings.add(new Setting("b"));
        settings.add(new Setting("b"));
        settings.add(new Setting("b"));
        settings.add(new Setting("b"));
        settings.add(new Setting("c"));
        settings.add(new Setting("d"));
        settings.add(new Setting("e"));
        settings.add(new Setting("e"));
        settings.add(new Student("1"));
    }

    @Override
    public void onInitView() {
        SettingAdapter adapter = new SettingAdapter();
        getBinding().rvSetting.setAdapter(adapter);
        adapter.setDataList(settings);
    }

    @Override
    public void onViewClick() {
        getBinding().tvSetting.setOnClickListener(v -> {
            navigateTo(R.id.action_settingFragment_to_collectionFragment);
        });
    }

}


