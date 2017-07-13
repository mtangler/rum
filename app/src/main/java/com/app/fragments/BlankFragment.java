package com.app.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.rum.R;

public class BlankFragment extends BaseFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.frag_blank, null);
        return v;
    }

    @Override
    protected void onFragAttached() {
        super.onFragAttached();
    }

    @Override
    protected void onFragDettached() {
        super.onFragDettached();
    }

    public static BlankFragment newInstance() {
        return new BlankFragment();
    }

}