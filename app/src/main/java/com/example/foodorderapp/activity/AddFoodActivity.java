package com.example.foodorderapp.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.foodorderapp.ControllerApplication;
import com.example.foodorderapp.R;
import com.example.foodorderapp.constant.Constant;
import com.example.foodorderapp.constant.GlobalFunction;
import com.example.foodorderapp.databinding.ActivityAddFoodBinding;
import com.example.foodorderapp.model.CartFood;
import com.example.foodorderapp.model.Food;
import com.example.foodorderapp.model.Image;
import com.example.foodorderapp.utils.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddFoodActivity extends BaseActivity {

    private ActivityAddFoodBinding mActivityAddFoodBinding;
    private boolean isUpdate;
    private CartFood mCartFood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityAddFoodBinding = ActivityAddFoodBinding.inflate(getLayoutInflater());
        setContentView(mActivityAddFoodBinding.getRoot());

        getDataIntent();
        initToolbar();
        initView();

        mActivityAddFoodBinding.btnAddOrEdit.setOnClickListener(v -> addOrEditFood());
    }

    private void getDataIntent() {
        Bundle bundleReceived = getIntent().getExtras();
        if (bundleReceived != null) {
            isUpdate = true;
            mCartFood = (CartFood) bundleReceived.get(Constant.KEY_INTENT_FOOD_OBJECT);
        }
    }

    private void initToolbar() {
        mActivityAddFoodBinding.toolbar.imgBack.setVisibility(View.VISIBLE);
        mActivityAddFoodBinding.toolbar.imgCart.setVisibility(View.GONE);

        mActivityAddFoodBinding.toolbar.imgBack.setOnClickListener(v -> onBackPressed());
    }

    private void initView() {
        if (isUpdate) {
            mActivityAddFoodBinding.toolbar.tvTitle.setText(getString(R.string.edit_food));
            mActivityAddFoodBinding.btnAddOrEdit.setText(getString(R.string.action_edit));

            mActivityAddFoodBinding.edtName.setText(mCartFood.getName());
            mActivityAddFoodBinding.edtDescription.setText(mCartFood.getDescription());
            mActivityAddFoodBinding.edtPrice.setText(String.valueOf(mCartFood.getPrice()));
            mActivityAddFoodBinding.edtDiscount.setText(String.valueOf(mCartFood.getSale()));
            mActivityAddFoodBinding.edtImage.setText(mCartFood.getImage());
            mActivityAddFoodBinding.edtImageBanner.setText(mCartFood.getBanner());
            mActivityAddFoodBinding.edtOtherImage.setText(getTextOtherImages());
        } else {
            mActivityAddFoodBinding.toolbar.tvTitle.setText(getString(R.string.add_food));
            mActivityAddFoodBinding.btnAddOrEdit.setText(getString(R.string.action_add));
        }
    }

    private String getTextOtherImages() {
        String result = "";
        if (mCartFood == null || mCartFood.getImages() == null || mCartFood.getImages().isEmpty()) {
            return result;
        }
        for (Image image : mCartFood.getImages()) {
            if (StringUtil.isEmpty(result)) {
                result = result + image.getUrl();
            } else {
                result = result + ";" + image.getUrl();
            }
        }
        return result;
    }

    private void addOrEditFood() {
        String strName = mActivityAddFoodBinding.edtName.getText().toString().trim();
        String strDescription = mActivityAddFoodBinding.edtDescription.getText().toString().trim();
        String strPrice = mActivityAddFoodBinding.edtPrice.getText().toString().trim();
        String strDiscount = mActivityAddFoodBinding.edtDiscount.getText().toString().trim();
        String strImage = mActivityAddFoodBinding.edtImage.getText().toString().trim();
        String strImageBanner = mActivityAddFoodBinding.edtImageBanner.getText().toString().trim();
        boolean isPopular = mActivityAddFoodBinding.chbPopular.isChecked();
        String strOtherImages = mActivityAddFoodBinding.edtOtherImage.getText().toString().trim();
        List<Image> listImages = new ArrayList<>();
        if (!StringUtil.isEmpty(strOtherImages)) {
            String[] temp = strOtherImages.split(";");
            for (String strUrl : temp) {
                Image image = new Image(strUrl);
                listImages.add(image);
            }
        }

        if (StringUtil.isEmpty(strName)) {
            Toast.makeText(this, getString(R.string.msg_name_food_require), Toast.LENGTH_SHORT).show();
            return;
        }

        if (StringUtil.isEmpty(strDescription)) {
            Toast.makeText(this, getString(R.string.msg_description_food_require), Toast.LENGTH_SHORT).show();
            return;
        }

        if (StringUtil.isEmpty(strPrice)) {
            Toast.makeText(this, getString(R.string.msg_price_food_require), Toast.LENGTH_SHORT).show();
            return;
        }

        if (StringUtil.isEmpty(strDiscount)) {
            Toast.makeText(this, getString(R.string.msg_discount_food_require), Toast.LENGTH_SHORT).show();
            return;
        }

        if (StringUtil.isEmpty(strImage)) {
            Toast.makeText(this, getString(R.string.msg_image_food_require), Toast.LENGTH_SHORT).show();
            return;
        }

        if (StringUtil.isEmpty(strImageBanner)) {
            Toast.makeText(this, getString(R.string.msg_image_banner_food_require), Toast.LENGTH_SHORT).show();
            return;
        }

        // Update food
        if (isUpdate) {
            showProgressDialog(true);
            Map<String, Object> map = new HashMap<>();
            map.put("name", strName);
            map.put("description", strDescription);
            map.put("price", Integer.parseInt(strPrice));
            map.put("sale", Integer.parseInt(strDiscount));
            map.put("image", strImage);
            map.put("banner", strImageBanner);
            map.put("popular", isPopular);
            if (!listImages.isEmpty()) {
                map.put("images", listImages);
            }

            ControllerApplication.get(this).getFoodDatabaseReference()
                    .child(String.valueOf(mCartFood.getId())).updateChildren(map, (error, ref) -> {
                showProgressDialog(false);
                Toast.makeText(AddFoodActivity.this,
                        getString(R.string.msg_edit_food_success), Toast.LENGTH_SHORT).show();
                GlobalFunction.hideSoftKeyboard(this);
            });
            return;
        }

        // Add food
        showProgressDialog(true);
        long foodId = System.currentTimeMillis();
        Food food = new Food(foodId, strName, strDescription, Integer.parseInt(strPrice),
                Integer.parseInt(strDiscount), strImage, strImageBanner);
        if (!listImages.isEmpty()) {
            food.setImages(listImages);
        }
        ControllerApplication.get(this).getFoodDatabaseReference()
                .child(String.valueOf(foodId)).setValue(food, (error, ref) -> {
            showProgressDialog(false);
            mActivityAddFoodBinding.edtName.setText("");
            mActivityAddFoodBinding.edtDescription.setText("");
            mActivityAddFoodBinding.edtPrice.setText("");
            mActivityAddFoodBinding.edtDiscount.setText("");
            mActivityAddFoodBinding.edtImage.setText("");
            mActivityAddFoodBinding.edtImageBanner.setText("");
            mActivityAddFoodBinding.chbPopular.setChecked(false);
            mActivityAddFoodBinding.edtOtherImage.setText("");
            GlobalFunction.hideSoftKeyboard(this);
            Toast.makeText(this, getString(R.string.msg_add_food_success), Toast.LENGTH_SHORT).show();
        });
    }
}