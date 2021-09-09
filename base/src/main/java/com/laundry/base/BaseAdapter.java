package com.laundry.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseAdapter extends RecyclerView.Adapter<BaseAdapter.BaseVH<Object>> {

    public static final int INVALID_RESOURCE = -1;
    protected List<Object> dataList;
    protected Context context;

    @Override
    public int getItemCount() {
        return dataList != null ? dataList.size() : 0;
    }

    protected abstract int getLayoutResource(int viewType);

    protected abstract Object getDataInPosition(int position);

    protected abstract BaseVH<?> onCreateVH(int viewType, ViewDataBinding viewDataBinding);

    public abstract void submitList(List<? extends Object> list);

    @NonNull
    @Override
    public BaseVH<Object> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), getLayoutResource(viewType), parent, false);
        return (BaseVH<Object>) onCreateVH(viewType, binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseVH<Object> holder, int position) {
        Object object = getDataInPosition(holder.getAdapterPosition());
        holder.bind(object);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseVH<Object> holder, int position, @NonNull List<Object> payloads) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position);
        } else {
            holder.bind(getDataInPosition(holder.getAdapterPosition()), payloads);
        }
    }

    // sử dụng để set list
    public void setDataList(List<Object> list) {
        this.dataList = list;
        notifyDataSetChanged();
    }

    // sử dụng để reset list display về object
    protected void resetDataList(List<? extends BaseDisplay> list){
        List<Object> dataSet = new ArrayList<>();
        dataSet.addAll(list);
        setDataList(dataSet);
    }

    public void add(Object item) {
        this.dataList.add(item);
        notifyItemInserted(getItemCount());
    }

    public void add(Object item, int position) {
        this.dataList.add(position, item);
        notifyItemInserted(position);
        notifyItemRangeChanged(position, getItemCount());
    }

    public void update(Object item, int position) {
        this.dataList.set(position, item);
        notifyItemChanged(position);
    }

    public void delete(Object item, int position) {
        this.dataList.remove(item);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount());
    }

    public class BaseVH<T> extends RecyclerView.ViewHolder {

        public BaseVH(@NonNull ViewDataBinding viewDataBinding) {
            super(viewDataBinding.getRoot());
        }

        public void bind(T item) {
        }

        public void bind(T item, List<Object> payloads) {
        }
    }
}
