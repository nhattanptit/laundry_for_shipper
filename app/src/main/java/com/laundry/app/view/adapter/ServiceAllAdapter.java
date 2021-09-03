package com.laundry.app.view.adapter;

import androidx.databinding.ViewDataBinding;

import com.laundry.app.R;
import com.laundry.app.databinding.ServiceAllItemBinding;
import com.laundry.app.dto.serviceall.ServiceAllDto;
import com.laundry.base.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

public class ServiceAllAdapter extends BaseAdapter {

    private IServiceAllCallback mCallback;

    public void setCallback(IServiceAllCallback callback) {
        this.mCallback = callback;
    }

    @Override
    protected int getLayoutResource(int viewType) {
        return R.layout.service_all_item;
    }

    @Override
    protected Object getDataInPosition(int position) {
        return dataList.get(position);
    }

    @Override
    protected BaseVH<?> onCreateVH(int viewType, ViewDataBinding viewDataBinding) {
        return new ServicesAllVH(viewDataBinding);
    }

    @Override
    public void submitList(List<? extends Object> list) {
        List<ServiceAllDisplay> displayList = new ArrayList<>();
        for (Object obj : list) {
            ServiceAllDisplay display = new ServiceAllDisplay();
            display.setData((ServiceAllDto) obj);
            displayList.add(display);
        }
        resetDataList(displayList);
    }

    class ServicesAllVH extends BaseVH<ServiceAllDisplay> {

        private final ServiceAllItemBinding binding;

        public ServicesAllVH(ViewDataBinding viewDataBinding) {
            super(viewDataBinding);
            binding = (ServiceAllItemBinding) viewDataBinding;

            binding.getRoot().setOnClickListener(view -> {
                ServiceAllDisplay display = (ServiceAllDisplay) getDataInPosition(getAbsoluteAdapterPosition());
                ServiceAllDto item = display.getData();
                mCallback.onClickItem(getAbsoluteAdapterPosition(), item);
            });
        }

        @Override
        public void bind(ServiceAllDisplay item) {
            binding.serviceAllImage.setImageResource(item.getIcon());
            binding.serviceAllText.setText(item.getData().getName());
        }
    }

    public interface IServiceAllCallback {
        void onClickItem(int position, ServiceAllDto item);
    }
}
