package com.example.foodorderapp.fragment.admin;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.foodorderapp.model.CartFood;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.example.foodorderapp.ControllerApplication;
import com.example.foodorderapp.R;
import com.example.foodorderapp.activity.AddFoodActivity;
import com.example.foodorderapp.activity.AdminMainActivity;
import com.example.foodorderapp.adapter.AdminFoodAdapter;
import com.example.foodorderapp.constant.Constant;
import com.example.foodorderapp.constant.GlobalFunction;
import com.example.foodorderapp.databinding.FragmentAdminHomeBinding;
import com.example.foodorderapp.fragment.BaseFragment;
import com.example.foodorderapp.listener.IOnManagerFoodListener;
import com.example.foodorderapp.model.Food;
import com.example.foodorderapp.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class AdminHomeFragment extends BaseFragment {

    private FragmentAdminHomeBinding mFragmentAdminHomeBinding;
    private List<CartFood> mListCartFood;
    private AdminFoodAdapter mAdminFoodAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFragmentAdminHomeBinding = FragmentAdminHomeBinding.inflate(inflater, container, false);

        initView();
        initListener();
        getListFood("");
        return mFragmentAdminHomeBinding.getRoot();
    }

    @Override
    protected void initToolbar() {
        if (getActivity() != null) {
            ((AdminMainActivity) getActivity()).setToolBar(getString(R.string.home));
        }
    }

    private void initView() {
        if (getActivity() == null) {
            return;
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mFragmentAdminHomeBinding.rcvFood.setLayoutManager(linearLayoutManager);
        mListCartFood = new ArrayList<>();
        mAdminFoodAdapter = new AdminFoodAdapter(mListCartFood, new IOnManagerFoodListener() {
            @Override
            public void onClickUpdateFood(CartFood cartFood) {
                onClickEditFood(cartFood);
            }

            @Override
            public void onClickDeleteFood(CartFood cartFood) {
                deleteFoodItem(cartFood);
            }
        });
        mFragmentAdminHomeBinding.rcvFood.setAdapter(mAdminFoodAdapter);
    }

    private void initListener() {
        mFragmentAdminHomeBinding.btnAddFood.setOnClickListener(v -> onClickAddFood());

        mFragmentAdminHomeBinding.imgSearch.setOnClickListener(view1 -> searchFood());

        mFragmentAdminHomeBinding.edtSearchName.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchFood();
                return true;
            }
            return false;
        });

        mFragmentAdminHomeBinding.edtSearchName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String strKey = s.toString().trim();
                if (strKey.equals("") || strKey.length() == 0) {
                    searchFood();
                }
            }
        });
    }

    private void onClickAddFood() {
        GlobalFunction.startActivity(getActivity(), AddFoodActivity.class);
    }

    private void onClickEditFood(CartFood cartFood) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.KEY_INTENT_FOOD_OBJECT, cartFood);
        GlobalFunction.startActivity(getActivity(), AddFoodActivity.class, bundle);
    }

    private void deleteFoodItem(CartFood cartFood) {
        new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.msg_delete_title))
                .setMessage(getString(R.string.msg_confirm_delete))
                .setPositiveButton(getString(R.string.action_ok), (dialogInterface, i) -> {
                    if (getActivity() == null) {
                        return;
                    }
                    ControllerApplication.get(getActivity()).getFoodDatabaseReference()
                            .child(String.valueOf(cartFood.getId())).removeValue((error, ref) ->
                                    Toast.makeText(getActivity(),
                                            getString(R.string.msg_delete_movie_successfully), Toast.LENGTH_SHORT).show());
                })
                .setNegativeButton(getString(R.string.action_cancel), null)
                .show();
    }

    private void searchFood() {
        String strKey = mFragmentAdminHomeBinding.edtSearchName.getText().toString().trim();
        if (mListCartFood != null) {
            mListCartFood.clear();
        } else {
            mListCartFood = new ArrayList<>();
        }
        getListFood(strKey);
        GlobalFunction.hideSoftKeyboard(getActivity());
    }

    public void getListFood(String keyword) {
        if (getActivity() == null) {
            return;
        }
        ControllerApplication.get(getActivity()).getFoodDatabaseReference()
                .addChildEventListener(new ChildEventListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {
                        CartFood cartFood = dataSnapshot.getValue(CartFood.class);
                        if (cartFood == null || mListCartFood == null || mAdminFoodAdapter == null) {
                            return;
                        }
                        if (StringUtil.isEmpty(keyword)) {
                            mListCartFood.add(0, cartFood);
                        } else {
                            if (GlobalFunction.getTextSearch(cartFood.getName()).toLowerCase().trim()
                                    .contains(GlobalFunction.getTextSearch(keyword).toLowerCase().trim())) {
                                mListCartFood.add(0, cartFood);
                            }
                        }
                        mAdminFoodAdapter.notifyDataSetChanged();
                    }

                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, String s) {
                        CartFood cartFood = dataSnapshot.getValue(CartFood.class);
                        if (cartFood == null || mListCartFood == null
                                || mListCartFood.isEmpty() || mAdminFoodAdapter == null) {
                            return;
                        }
                        for (int i = 0; i < mListCartFood.size(); i++) {
                            if (cartFood.getId() == mListCartFood.get(i).getId()) {
                                mListCartFood.set(i, cartFood);
                                break;
                            }
                        }
                        mAdminFoodAdapter.notifyDataSetChanged();
                    }

                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                        CartFood cartFood = dataSnapshot.getValue(CartFood.class);
                        if (cartFood == null || mListCartFood == null
                                || mListCartFood.isEmpty() || mAdminFoodAdapter == null) {
                            return;
                        }
                        for (CartFood food : mListCartFood) {
                            if (cartFood.getId() == food.getId()) {
                                mListCartFood.remove(food);
                                break;
                            }
                        }
                        mAdminFoodAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, String s) {
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
    }
}
