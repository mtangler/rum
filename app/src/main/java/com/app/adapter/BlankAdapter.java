package com.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.core.LogEx;
import com.app.holder.General;

import java.util.ArrayList;

public class BlankAdapter extends RecyclerView
        .Adapter<BlankAdapter.ViewHolder> {

    int layoutIdentifier = 0; // R.layout.name;


    public static ArrayList<General> dataList;
    Context ctx;
    LACallback rvCallback;

    public BlankAdapter(Context ctx, ArrayList<General> myDataset, LACallback tabCallback) {
        dataList = myDataset;
        this.ctx = ctx;
        this.rvCallback = tabCallback;
        initAdapter();
    }

    private void initAdapter() {
        try {


        } catch (Exception e) {
            LogEx.print(e);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        // Define views here
        // e.g TextView tv;

        // @BindView(R.id.tvTitle)
        // TextView tvTitle;

        public ViewHolder(View itemView) {
            super(itemView);

            // ButterKnife.bind(this, itemView);

            // Find views here
            // e.g tv = itemView.findViewById
        }
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        try {

            // Set view values here
            // e.g. holder.tvNam.setText(dataList.get(position).getUser_name());

        } catch (Exception e) {
            LogEx.print(e);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(layoutIdentifier, parent, false);
        ViewHolder dataObjectHolder = new ViewHolder(view);
        return dataObjectHolder;
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

}
