package com.laundry.app.view.adapter;

import android.annotation.SuppressLint;

import androidx.databinding.ViewDataBinding;

import com.laundry.app.R;
import com.laundry.app.constant.Constant;
import com.laundry.app.databinding.OrderConfirmItemBinding;
import com.laundry.app.databinding.ServiceDetailItemBinding;
import com.laundry.app.dto.sevicedetail.ServiceDetailDto;
import com.laundry.base.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

public class ServicesOrderAdapter extends BaseAdapter {

    private IServiceDetailCallback mCallback;
    public SERVICES_DETAIL_VIEW_TYPE typeService = SERVICES_DETAIL_VIEW_TYPE.SERVICES_DETAIL;

    public void setCallback(IServiceDetailCallback callback) {
        this.mCallback = callback;
    }

    @Override
    protected int getLayoutResource(int viewType) {
        switch (typeService) {
            case SERVICES_DETAIL:
                return R.layout.service_detail_item;
            case ORDER:
                return R.layout.order_confirm_item;
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
        if (typeService == SERVICES_DETAIL_VIEW_TYPE.SERVICES_DETAIL) {
            return new ServiceDetailVH(viewDataBinding);
        } else {
            return new OrderConfirmVH(viewDataBinding);
        }
    }

    /** Reset data to object  */
    @Override
    public void submitList(List<? extends Object> list) {
        List<ServiceDetailDisplay> displayList = new ArrayList<>();
        for (Object obj : list) {
            ServiceDetailDisplay display = new ServiceDetailDisplay();
            display.data = (ServiceDetailDto) obj;
            displayList.add(display);
        }
        resetDataList(displayList);
    }

    /** View holder of screen Service Detail List*/
    private class ServiceDetailVH extends BaseVH<ServiceDetailDisplay> {

        private ServiceDetailItemBinding binding;

        public ServiceDetailVH(ViewDataBinding viewDataBinding) {
            super(viewDataBinding);
            binding = (ServiceDetailItemBinding) viewDataBinding;
            binding.addImage.setOnClickListener(view -> {
                ServiceDetailDto item = getItem(getAbsoluteAdapterPosition());
                if (item.quantity >= 0) {
                    binding.quantityText.setText(String.valueOf(++item.quantity));
                    item.totalPrice = item.price * item.quantity;
                    mCallback.onClickItem(getAbsoluteAdapterPosition(), item);
                }
            });
            binding.minusImage.setOnClickListener(view -> {
                ServiceDetailDto item = getItem(getAbsoluteAdapterPosition());
                if (item.quantity > 0) {
                    binding.quantityText.setText(String.valueOf(--item.quantity));
                    item.totalPrice = item.price * item.quantity;
                    mCallback.onClickItem(getAbsoluteAdapterPosition(), item);
                }
            });
        }

        @Override
        public void bind(ServiceDetailDisplay item) {
            super.bind(item);
            binding.categoryImage.setImageResource(item.getIcon());
            binding.categoryText.setText(item.data.name);
            binding.priceText.setText(String.valueOf("$" + item.data.price));
        }

        private ServiceDetailDto getItem(int position) {
            ServiceDetailDisplay detailDisplay =
                    (ServiceDetailDisplay) getDataInPosition(position);
            return detailDisplay.data;
        }
    }

    /** View holder of screen Order Confirm List*/
    private class OrderConfirmVH extends BaseVH<ServiceDetailDisplay> {

        private OrderConfirmItemBinding binding;

        public OrderConfirmVH(ViewDataBinding viewDataBinding) {
            super(viewDataBinding);
            binding = (OrderConfirmItemBinding) viewDataBinding;
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void bind(ServiceDetailDisplay item) {
            super.bind(item);
            binding.nameCategory.setText(item.data.name);
            binding.quantityText.setText(String.valueOf(item.data.quantity));
            binding.xText.setText(R.string.x);
            binding.price.setText(item.data.price * item.data.quantity + "$");
        }
    }

    public interface IServiceDetailCallback {
        void onClickItem(int position, ServiceDetailDto item);
    }

    /** Use enum for display list service detail and list selected order confirm */
    public enum SERVICES_DETAIL_VIEW_TYPE {
        SERVICES_DETAIL,
        ORDER
    }
}
