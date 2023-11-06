package com.example.foodorderapp.adapter;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorderapp.constant.Constant;
import com.example.foodorderapp.databinding.ItemFoodGridBinding;
import com.example.foodorderapp.listener.IOnClickFoodItemListener;
import com.example.foodorderapp.model.CartFood;
import com.example.foodorderapp.model.Food;
import com.example.foodorderapp.utils.GlideUtils;

import java.util.List;

public class FoodGridAdapter extends RecyclerView.Adapter<FoodGridAdapter.FoodGridViewHolder> {

    private final List<CartFood> mListFoods;
    public final IOnClickFoodItemListener iOnClickFoodItemListener;

    public FoodGridAdapter(List<CartFood> mListFoods, IOnClickFoodItemListener iOnClickFoodItemListener) {
        this.mListFoods = mListFoods;
        this.iOnClickFoodItemListener = iOnClickFoodItemListener;
    }

    @NonNull
    @Override
    public FoodGridViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFoodGridBinding itemFoodGridBinding = ItemFoodGridBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new FoodGridViewHolder(itemFoodGridBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodGridViewHolder holder, int position) {
        CartFood cartFood = mListFoods.get(position);
        if (cartFood == null) {
            return;
        }
        GlideUtils.loadUrl(cartFood.getImage(), holder.mItemFoodGridBinding.imgFood);
        if (cartFood.getSale() <= 0) {
            holder.mItemFoodGridBinding.tvSaleOff.setVisibility(View.GONE);
            holder.mItemFoodGridBinding.tvPrice.setVisibility(View.GONE);

            String strPrice = cartFood.getPrice() + Constant.CURRENCY;
            holder.mItemFoodGridBinding.tvPriceSale.setText(strPrice);
        } else {
            holder.mItemFoodGridBinding.tvSaleOff.setVisibility(View.VISIBLE);
            holder.mItemFoodGridBinding.tvPrice.setVisibility(View.VISIBLE);

            String strSale = "Giảm " + cartFood.getSale() + "%";
            holder.mItemFoodGridBinding.tvSaleOff.setText(strSale);

            String strOldPrice = cartFood.getPrice() + Constant.CURRENCY;
            holder.mItemFoodGridBinding.tvPrice.setText(strOldPrice);
            holder.mItemFoodGridBinding.tvPrice.setPaintFlags(holder.mItemFoodGridBinding.tvPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            String strRealPrice = cartFood.getRealPrice() + Constant.CURRENCY;
            holder.mItemFoodGridBinding.tvPriceSale.setText(strRealPrice);
        }
        holder.mItemFoodGridBinding.tvFoodName.setText(cartFood.getName());

        holder.mItemFoodGridBinding.layoutItem.setOnClickListener(v -> iOnClickFoodItemListener.onClickItemFood(cartFood));
    }

    @Override
    public int getItemCount() {
        return null == mListFoods ? 0 : mListFoods.size();
    }

    public static class FoodGridViewHolder extends RecyclerView.ViewHolder {

        private final ItemFoodGridBinding mItemFoodGridBinding;

        public FoodGridViewHolder(ItemFoodGridBinding itemFoodGridBinding) {
            super(itemFoodGridBinding.getRoot());
            this.mItemFoodGridBinding = itemFoodGridBinding;
        }
    }
}
