package com.laundry.app.view.adapter;

import androidx.databinding.ViewDataBinding;

import com.laundry.app.R;
import com.laundry.app.databinding.ServiceDetailItemBinding;
import com.laundry.app.dto.sevicedetail.ServiceDetailDto;
import com.laundry.base.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

public class ServiceDetailAdapter extends BaseAdapter {
    @Override
    protected int getLayoutResource(int viewType) {
        return R.layout.service_detail_item;
    }

    @Override
    protected Object getDataInPosition(int position) {
        return dataList.get(position);
    }

    @Override
    protected BaseVH<?> onCreateVH(int viewType, ViewDataBinding viewDataBinding) {
        return new ServiceDetailVH(viewDataBinding);
    }

    @Override
    public void submitList(List<? extends Object> list) {
        List<ServiceDetailDisplay> displayList = new ArrayList<>();
        for (Object obj : list) {
            ServiceDetailDisplay display = new ServiceDetailDisplay();
            display.setData((ServiceDetailDto) obj);
            displayList.add(display);
        }
        resetDataList(displayList);
    }


    private class ServiceDetailVH extends BaseVH<ServiceDetailDisplay> {

        private ServiceDetailItemBinding binding;

        public ServiceDetailVH(ViewDataBinding viewDataBinding) {
            super(viewDataBinding);
            binding = (ServiceDetailItemBinding) viewDataBinding;
        }

        @Override
        public void bind(ServiceDetailDisplay item) {
            super.bind(item);
            binding.categoryImage.setImageResource(item.getIcon());
            binding.categoryText.setText(item.getData().getName());
            binding.priceText.setText(String.valueOf(item.getData().getPrice()));
        }
    }
}
