package com.example.foodorderapp.listener;

import com.example.foodorderapp.model.CartFood;

public interface IOnManagerFoodListener {
    void onClickUpdateFood(CartFood cartFood);
    void onClickDeleteFood(CartFood cartFood);
}
