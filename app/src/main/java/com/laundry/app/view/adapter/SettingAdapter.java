package com.laundry.app.view.adapter;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;

import com.laundry.app.R;
import com.laundry.app.databinding.ItemSettingBinding;
import com.laundry.app.databinding.ItemStudentBinding;
import com.laundry.app.model.Setting;
import com.laundry.app.model.Student;
import com.laundry.base.BaseAdapter;

import java.util.List;

public class SettingAdapter extends BaseAdapter {

    public static final int VIEW_TYPE_SETTING = 1;
    public static final int VIEW_TYPE_STUDENT = 2;

    @Override
    public int getItemViewType(int position) {
        if (position == 0 || position == dataList.size() - 1) {
            return VIEW_TYPE_STUDENT;
        } else {
            return VIEW_TYPE_SETTING;
        }
    }

    @Override
    protected int getLayoutResource(int viewType) {
        switch (viewType) {
            case VIEW_TYPE_SETTING:
                return R.layout.item_setting;
            case VIEW_TYPE_STUDENT:
                return R.layout.item_student;
            default:
                return INVALID_RESOURCE;
        }
    }

    @Override
    protected Object getDataInPosition(int position) {
        return dataList.get(position);
    }

    @Override
    protected BaseVH<?> onCreateVH(int viewType, ViewDataBinding viewDataBinding) {
        if (viewType == VIEW_TYPE_SETTING) {
            return new SettingVH(viewDataBinding);
        } else {
            return new StudentVH(viewDataBinding);
        }
    }

    @Override
    public void submitList(List<?> list) {

    }

    class SettingVH extends BaseVH<Setting> {

        private final ItemSettingBinding binding;

        public SettingVH(@NonNull ViewDataBinding viewDataBinding) {
            super(viewDataBinding);
            binding = (ItemSettingBinding) viewDataBinding;
        }

        @Override
        public void bind(Setting item) {
            binding.tvTitle.setText(item.getTitle());
        }
    }

    class StudentVH extends BaseVH<Student> {

        private final ItemStudentBinding binding;

        public StudentVH(@NonNull ViewDataBinding viewDataBinding) {
            super(viewDataBinding);
            binding = (ItemStudentBinding) viewDataBinding;
        }

        @Override
        public void bind(Student item) {
            binding.tvTitle.setText(item.getName());
        }
    }
}
