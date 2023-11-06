package com.example.foodorderapp.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.viewpager2.widget.ViewPager2;

import com.example.foodorderapp.model.CartFood;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.example.foodorderapp.ControllerApplication;
import com.example.foodorderapp.R;
import com.example.foodorderapp.activity.FoodDetailActivity;
import com.example.foodorderapp.activity.MainActivity;
import com.example.foodorderapp.adapter.FoodGridAdapter;
import com.example.foodorderapp.constant.Constant;
import com.example.foodorderapp.constant.GlobalFunction;
import com.example.foodorderapp.databinding.FragmentHomeBinding;
import com.example.foodorderapp.model.Food;
import com.example.foodorderapp.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends BaseFragment {

    private FragmentHomeBinding mFragmentHomeBinding;

    private List<CartFood> mListCartFood;

//    private final Handler mHandlerBanner = new Handler();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false);

        getListFoodFromFirebase("");
        initListener();

        return mFragmentHomeBinding.getRoot();
    }

    @Override
    protected void initToolbar() {
        if (getActivity() != null) {
            ((MainActivity) getActivity()).setToolBar(true, getString(R.string.home));
        }
    }

    private void initListener() {
        mFragmentHomeBinding.edtSearchName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Do nothing
            }

            @Override
            public void afterTextChanged(Editable s) {
                String strKey = s.toString().trim();
                if (strKey.equals("") || strKey.length() == 0) {
                    if (mListCartFood != null) mListCartFood.clear();
                    getListFoodFromFirebase("");
                }
            }
        });

        mFragmentHomeBinding.imgSearch.setOnClickListener(view -> searchFood());

        mFragmentHomeBinding.edtSearchName.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchFood();
                return true;
            }
            return false;
        });
    }

    private void displayListFoodSuggest() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        mFragmentHomeBinding.rcvFood.setLayoutManager(gridLayoutManager);

        FoodGridAdapter mFoodGridAdapter = new FoodGridAdapter(mListCartFood, this::goToFoodDetail);
        mFragmentHomeBinding.rcvFood.setAdapter(mFoodGridAdapter);
    }

    private void getListFoodFromFirebase(String key) {
        if (getActivity() == null) {
            return;
        }
        ControllerApplication.get(getActivity()).getFoodDatabaseReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mFragmentHomeBinding.layoutContent.setVisibility(View.VISIBLE);
                mListCartFood = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    CartFood cartFood = dataSnapshot.getValue(CartFood.class);
                    if (cartFood == null) {
                        return;
                    }

                    if (StringUtil.isEmpty(key)) {
                        mListCartFood.add(0, cartFood);
                    } else {
                        if (GlobalFunction.getTextSearch(cartFood.getName()).toLowerCase().trim()
                                .contains(GlobalFunction.getTextSearch(key).toLowerCase().trim())) {
                            mListCartFood.add(0, cartFood);
                        }
                    }
                }
//                displayListFoodPopular();
                displayListFoodSuggest();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                GlobalFunction.showToastMessage(getActivity(), getString(R.string.msg_get_date_error));
            }
        });
    }

    private void searchFood() {
        String strKey = mFragmentHomeBinding.edtSearchName.getText().toString().trim();
        if (mListCartFood != null) mListCartFood.clear();
        getListFoodFromFirebase(strKey);
        GlobalFunction.hideSoftKeyboard(getActivity());
    }

    private void goToFoodDetail(@NonNull CartFood cartFood) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.KEY_INTENT_FOOD_OBJECT, cartFood);
        GlobalFunction.startActivity(getActivity(), FoodDetailActivity.class, bundle);
    }

//    @Override
//    public void onPause() {
//        super.onPause();
//        mHandlerBanner.removeCallbacks(mRunnableBanner);
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        mHandlerBanner.postDelayed(mRunnableBanner, 3000);
//    }
}
