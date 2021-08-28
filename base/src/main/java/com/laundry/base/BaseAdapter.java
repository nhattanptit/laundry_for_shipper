package com.laundry.base;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public abstract class BaseAdapter extends RecyclerView.Adapter<BaseAdapter.BaseVH<Object>> {

    public static final int INVALID_RESOURCE = -1;
    private List<Object> mDataList;

    public void setDataList(List<Object> mDataList) {
        this.mDataList = mDataList;
        notifyDataSetChanged();
    }

    public List<Object> getDataList() {
        return mDataList;
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    protected abstract int getLayoutResource(int viewType);

    protected abstract Object getDataInPosition(int position);

    protected abstract BaseVH<?> onCreateVH(int viewType, ViewDataBinding viewDataBinding);

    @NonNull
    @Override
    public BaseVH<Object> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(getLayoutResource(viewType), parent, false);
        ViewDataBinding binding =  DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), getLayoutResource(viewType), parent, false);
        return (BaseVH<Object>) onCreateVH(viewType, binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseVH<Object> holder, int position) {
        Object object = getDataInPosition(holder.getAdapterPosition());
        holder.bind(object);
    }

    //    @Override
//    public void onBindViewHolder(@NonNull BaseVH<Object> holder, int position) {
//
//    }

//    @Override
//    public void onBindViewHolder(@NonNull BaseVH<Object> holder, int position, @NonNull List<Object> payloads) {
//        if (payloads.isEmpty()) {
//            onBindViewHolder(holder, position);
//        } else {
//            holder.bind(getDataInPosition(holder.getAdapterPosition()), payloads);
//        }
//    }

//    void add(T item) {
//        this.mDataList.add(item);
//        notifyItemInserted(getItemCount());
//    }
//
//    void add(T item, int position) {
//        this.mDataList.add(position, item);
//        notifyItemInserted(position);
//        notifyItemRangeChanged(position, getItemCount());
//    }
//
//    void update(T item, int position) {
//        this.mDataList.set(position, item);
//        notifyItemChanged(position);
//    }
//
//    void delete(T item, int position) {
//        this.mDataList.remove(item);
//        notifyItemRemoved(position);
//        notifyItemRangeChanged(position, getItemCount());
//    }

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
