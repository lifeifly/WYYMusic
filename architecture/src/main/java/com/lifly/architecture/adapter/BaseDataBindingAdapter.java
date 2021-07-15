package com.lifly.architecture.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseDataBindingAdapter<M, B extends ViewDataBinding> extends ListAdapter<M, RecyclerView.ViewHolder> {
    protected Context mContext;

    private OnItemClickListener<M> mOnItemClickListener;
    private OnItemLongClickListener<M> mOnLongClickListener;

    protected BaseDataBindingAdapter(@NonNull @NotNull DiffUtil.ItemCallback<M> diffCallback) {
        super(diffCallback);
    }

    @Override
    public void submitList(@Nullable @org.jetbrains.annotations.Nullable List<M> list) {
        super.submitList(list, () -> {
            super.submitList(list == null ? new ArrayList<>() : new ArrayList<>(list));
        });
    }

    @NonNull
    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        B binding = DataBindingUtil.inflate(LayoutInflater.from(mContext), this.getLayoutResId(viewType), parent, false);
        BaseBindingViewHolder holder = new BaseBindingViewHolder(binding.getRoot());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    int position = holder.getAdapterPosition();
                    mOnItemClickListener.onItemClick(getItem(position), position);
                }
            }
        });
        holder.itemView.setOnLongClickListener(v -> {
            if (mOnLongClickListener != null) {
                int position = holder.getAdapterPosition();
                mOnLongClickListener.onItemLongClick(getItem(position), position);
                return true;
            }
            return false;
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        B binding = DataBindingUtil.getBinding(holder.itemView);
        this.onBindItem(binding, getItem(position), holder);
        if (binding != null) {
            binding.executePendingBindings();
        }
    }

    /**
     * 注意：
     * RecyclerView 中的数据有位置改变（比如删除）时一般不会重新调用 onBindViewHolder() 方法，除非这个元素不可用。
     * 为了实时获取元素的位置，我们通过 ViewHolder.getBindingAdapterPosition() 方法。
     *
     * @param binding .
     * @param item    .
     * @param holder  .
     */
    protected abstract void onBindItem(B binding, M item, RecyclerView.ViewHolder holder);

    protected abstract @LayoutRes
    int getLayoutResId(int viewType);

    public void setmOnItemClickListener(OnItemClickListener<M> mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public void setmOnLongClickListener(OnItemLongClickListener<M> mOnLongClickListener) {
        this.mOnLongClickListener = mOnLongClickListener;
    }

    public static class BaseBindingViewHolder extends RecyclerView.ViewHolder {
        BaseBindingViewHolder(View itemView) {
            super(itemView);
        }
    }

    public interface OnItemClickListener<M> {
        void onItemClick(M item, int position);
    }

    public interface OnItemLongClickListener<M> {
        void onItemLongClick(M item, int position);
    }
}
