package com.example.foodorderapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorderapp.R;
import com.example.foodorderapp.constant.Constant;
import com.example.foodorderapp.databinding.ItemAdminOrderBinding;
import com.example.foodorderapp.listener.IOnClickUpdateStatusListener;
import com.example.foodorderapp.model.Order;
import com.example.foodorderapp.utils.DateTimeUtils;

import java.util.List;

public class AdminOrderAdapter extends RecyclerView.Adapter<AdminOrderAdapter.AdminOrderViewHolder> {

    private Context mContext;
    private final List<Order> mListOrder;
//    private final IUpdateStatusListener mIUpdateStatusListener;
//
//    public interface IUpdateStatusListener {
//        void updateStatus(Order order);
//    }
    private final IOnClickUpdateStatusListener iOnClickUpdateStatusListener;

    public AdminOrderAdapter(Context mContext, List<Order> mListOrder, IOnClickUpdateStatusListener iOnClickUpdateStatusListener) {
        this.mContext = mContext;
        this.mListOrder = mListOrder;
        this.iOnClickUpdateStatusListener = iOnClickUpdateStatusListener;

    }

    @NonNull
    @Override
    public AdminOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAdminOrderBinding itemAdminOrderBinding = ItemAdminOrderBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false);
        return new AdminOrderViewHolder(itemAdminOrderBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminOrderViewHolder holder, int position) {
        Order order = mListOrder.get(position);
        if (order == null) {
            return;
        }
        if (order.getStatus() != null && order.getStatus().equalsIgnoreCase("Đã hoàn thành")) {
            holder.mItemAdminOrderBinding.layoutItem.setBackgroundColor(mContext.getResources().getColor(R.color.black_overlay));
        } else {
            holder.mItemAdminOrderBinding.layoutItem.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        }
        holder.mItemAdminOrderBinding.tvStatus.setText(String.valueOf(order.getStatus()));
        holder.mItemAdminOrderBinding.tvId.setText(String.valueOf(order.getId()));
        holder.mItemAdminOrderBinding.tvEmail.setText(order.getEmail());
        holder.mItemAdminOrderBinding.tvName.setText(order.getName());
        holder.mItemAdminOrderBinding.tvPhone.setText(order.getPhone());
        holder.mItemAdminOrderBinding.tvAddress.setText(order.getAddress());
        holder.mItemAdminOrderBinding.tvMenu.setText(order.getFoods());
        holder.mItemAdminOrderBinding.tvDate.setText(DateTimeUtils.convertTimeStampToDate(order.getId()));

        String strAmount = order.getAmount() + Constant.CURRENCY;
        holder.mItemAdminOrderBinding.tvTotalAmount.setText(strAmount);

        String paymentMethod = "";
        if (Constant.TYPE_PAYMENT_CASH == order.getPayment()) {
            paymentMethod = Constant.PAYMENT_METHOD_CASH;
        }
        holder.mItemAdminOrderBinding.tvPayment.setText(paymentMethod);

        if(order.getStatus().equalsIgnoreCase("Chờ xác nhận")){
            holder.mItemAdminOrderBinding.tvStatus.setOnClickListener(
                    v -> iOnClickUpdateStatusListener.onClickUpdateStatusPrepare(order));
        }
        if(order.getStatus().equalsIgnoreCase("Đang chuẩn bị đơn hàng")){
            holder.mItemAdminOrderBinding.tvStatus.setOnClickListener(
                    v -> iOnClickUpdateStatusListener.onClickUpdateStatusDeliver(order));
        }
        if(order.getStatus().equalsIgnoreCase("Đang giao hàng")){
            holder.mItemAdminOrderBinding.tvStatus.setOnClickListener(
                    v -> iOnClickUpdateStatusListener.onClickUpdateStatusComplete(order));
        }

    }

    @Override
    public int getItemCount() {
        if (mListOrder != null) {
            return mListOrder.size();
        }
        return 0;
    }

    public void release() {
        mContext = null;
    }

    public static class AdminOrderViewHolder extends RecyclerView.ViewHolder {

        private final ItemAdminOrderBinding mItemAdminOrderBinding;

        public AdminOrderViewHolder(@NonNull ItemAdminOrderBinding itemAdminOrderBinding) {
            super(itemAdminOrderBinding.getRoot());
            this.mItemAdminOrderBinding = itemAdminOrderBinding;
        }
    }
}
