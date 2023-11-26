package com.example.foodorderapp.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.foodorderapp.R;
import com.example.foodorderapp.activity.MainActivity;
import com.example.foodorderapp.constant.GlobalFunction;
import com.example.foodorderapp.databinding.FragmentContactBinding;

public class ContactFragment extends BaseFragment{
    private FragmentContactBinding mFragmentContactBinding;
    @Override
    protected void initToolbar() {
        if (getActivity() != null) {
            ((MainActivity) getActivity()).setToolBar(false, getString(R.string.contact));
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFragmentContactBinding = FragmentContactBinding.inflate(inflater,container,false);

        mFragmentContactBinding.layoutFacebook.setOnClickListener(view ->
                GlobalFunction.onClickOpenFacebook(this.getActivity()));
        mFragmentContactBinding.layoutZalo.setOnClickListener(view ->
                GlobalFunction.onClickOpenZalo(this.getActivity()));
        mFragmentContactBinding.layoutGmail.setOnClickListener(view ->
                GlobalFunction.onClickOpenGmail(this.getActivity()));
        mFragmentContactBinding.layoutHotline.setOnClickListener(view ->
                GlobalFunction.callPhoneNumber(this.getActivity()));
        mFragmentContactBinding.layoutLocation.setOnClickListener(view ->
                GlobalFunction.onClickOpenLocation(this.getActivity()));

        return mFragmentContactBinding.getRoot();
    }

}
