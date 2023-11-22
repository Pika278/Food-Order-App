package com.example.foodorderapp.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.foodorderapp.model.CartFood;
import com.example.foodorderapp.model.Food;
import com.example.foodorderapp.model.Order;

import java.util.List;

@Dao
public interface FoodDAO {

    @Insert
    void insertFood(CartFood cartFood);

    @Query("SELECT * FROM cartFood")
    List<CartFood> getListFoodCart();

    @Query("SELECT * FROM cartFood WHERE id=:id")
    List<CartFood> checkFoodInCart(long id);

    @Delete
    void deleteFood(CartFood cartFood);

    @Update
    void updateFood(CartFood cartFood);

    @Query("DELETE from cartFood")
    void deleteAllFood();
}