package com.laundry.app.view.adapter;

import androidx.databinding.ViewDataBinding;

import com.laundry.app.R;
import com.laundry.app.databinding.ProductItemBinding;
import com.laundry.app.model.Product;
import com.laundry.base.BaseAdapter;

public class ProductionAdapter extends BaseAdapter {
    @Override
    protected int getLayoutResource(int viewType) {
        return R.layout.product_item;
    }

    @Override
    protected Object getDataInPosition(int position) {
        return getDataList().get(position);
    }

    @Override
    protected BaseVH<?> onCreateVH(int viewType, ViewDataBinding viewDataBinding) {
        return new ProductVH(viewDataBinding);
    }

    class ProductVH extends BaseVH<Product> {

        private final ProductItemBinding binding;

        public ProductVH(ViewDataBinding viewDataBinding) {
            super(viewDataBinding);
            binding = (ProductItemBinding) viewDataBinding;
        }

        @Override
        public void bind(Product item) {
            binding.productionImage.setImageResource(item.getProductImage());
            binding.productionText.setText(item.getProductName());
        }
    }
}
