package com.example.foodorderapp.listener;

import com.example.foodorderapp.model.Order;

public interface IOnClickUpdateStatusListener {
    void onClickUpdateStatusConfirm(Order order);
    void onClickUpdateStatusPrepare(Order order);
    void onClickUpdateStatusDeliver(Order order);
    void onClickUpdateStatusComplete(Order order);

}
