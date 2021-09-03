package com.laundry.app.view.adapter;

import com.laundry.app.R;
import com.laundry.app.databinding.ServiceItemBinding;
import com.laundry.app.dto.servicelist.ServiceListDto;
import com.laundry.base.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

import androidx.databinding.ViewDataBinding;

public class ServiceListAdapter extends BaseAdapter {

    private IServiceAllCallback mCallback;

    public void setCallback(IServiceAllCallback callback) {
        this.mCallback = callback;
    }

    @Override
    protected int getLayoutResource(int viewType) {
        return R.layout.service_item;
    }

    @Override
    protected Object getDataInPosition(int position) {
        return dataList.get(position);
    }

    @Override
    protected BaseVH<?> onCreateVH(int viewType, ViewDataBinding viewDataBinding) {
        return new ServicesListVH(viewDataBinding);
    }

    @Override
    public void submitList(List<? extends Object> list) {
        List<ServiceListDisplay> displayList = new ArrayList<>();
        for (Object obj : list) {
            ServiceListDisplay display = new ServiceListDisplay();
            display.data = ((ServiceListDto) obj);
            displayList.add(display);
        }
        resetDataList(displayList);
    }

    class ServicesListVH extends BaseVH<ServiceListDisplay> {

        private final ServiceItemBinding binding;

        public ServicesListVH(ViewDataBinding viewDataBinding) {
            super(viewDataBinding);
            binding = (ServiceItemBinding) viewDataBinding;

            binding.getRoot().setOnClickListener(view -> {
                ServiceListDisplay display = (ServiceListDisplay) getDataInPosition(getAbsoluteAdapterPosition());
                ServiceListDto item = display.data;
                mCallback.onClickItem(getAbsoluteAdapterPosition(), item);
            });
        }

        @Override
        public void bind(ServiceListDisplay item) {
            binding.serviceImage.setImageResource(item.getIcon());
            binding.serviceName.setText(item.data.name);
        }
    }

    public interface IServiceAllCallback {
        void onClickItem(int position, ServiceListDto item);
    }
}
