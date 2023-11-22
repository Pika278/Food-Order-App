package com.example.foodorderapp.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorderapp.constant.Constant;
import com.example.foodorderapp.databinding.ItemCartBinding;
import com.example.foodorderapp.model.CartFood;
import com.example.foodorderapp.utils.GlideUtils;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private final List<CartFood> mListFoods;
    private final IClickListener iClickListener;

    public interface IClickListener {
        void clickDeleteFood(CartFood cartFood, int position);

        void updateItemFood(CartFood cartFood, int position);
    }

    public CartAdapter(List<CartFood> mListFoods, IClickListener iClickListener) {
        this.mListFoods = mListFoods;
        this.iClickListener = iClickListener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCartBinding itemCartBinding = ItemCartBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new CartViewHolder(itemCartBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartFood cartFood = mListFoods.get(position);
        if (cartFood == null) {
            return;
        }
        GlideUtils.loadUrl(cartFood.getImage(), holder.mItemCartBinding.imgFoodCart);
        holder.mItemCartBinding.tvFoodNameCart.setText(cartFood.getName());


        String strFoodPriceCart = cartFood.getPrice() + Constant.CURRENCY;
        if (cartFood.getSale() > 0) {
            strFoodPriceCart = cartFood.getRealPrice() + Constant.CURRENCY;
        }
        holder.mItemCartBinding.tvFoodPriceCart.setText(strFoodPriceCart);
        holder.mItemCartBinding.tvCount.setText(String.valueOf(cartFood.getCount()));

        holder.mItemCartBinding.tvSubtract.setOnClickListener(v -> {
            String strCount = holder.mItemCartBinding.tvCount.getText().toString();
            int count = Integer.parseInt(strCount);
            if (count <= 1) {
                return;
            }
            int newCount = count - 1;
            holder.mItemCartBinding.tvCount.setText(String.valueOf(newCount));

            int totalPrice = cartFood.getRealPrice() * newCount;
            cartFood.setCount(newCount);
            cartFood.setTotalPrice(totalPrice);

            iClickListener.updateItemFood(cartFood, holder.getAdapterPosition());
        });

        holder.mItemCartBinding.tvAdd.setOnClickListener(v -> {
            int newCount = Integer.parseInt(holder.mItemCartBinding.tvCount.getText().toString()) + 1;
            holder.mItemCartBinding.tvCount.setText(String.valueOf(newCount));

            int totalPrice = cartFood.getRealPrice() * newCount;
            cartFood.setCount(newCount);
            cartFood.setTotalPrice(totalPrice);

            iClickListener.updateItemFood(cartFood, holder.getAdapterPosition());
        });

        holder.mItemCartBinding.tvDelete.setOnClickListener(v
                -> iClickListener.clickDeleteFood(cartFood, holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return null == mListFoods ? 0 : mListFoods.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {

        private final ItemCartBinding mItemCartBinding;

        public CartViewHolder(ItemCartBinding itemCartBinding) {
            super(itemCartBinding.getRoot());
            this.mItemCartBinding = itemCartBinding;
        }
    }
}
