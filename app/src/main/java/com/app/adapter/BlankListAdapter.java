package com.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.app.holder.General;

import java.util.ArrayList;

public class BlankListAdapter extends BaseAdapter {

    int layout_identifier = 0;

    Context context;
    private ArrayList<General> list;

    public BlankListAdapter(Context mainActivity, ArrayList<General> items) {
        this.context = mainActivity;
        this.list = items;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    static class ViewHolder {
        TextView tvText;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        ViewHolder holder = null;

        if (view == null) {
            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            view = mInflater.inflate(layout_identifier, null);

            holder = new ViewHolder();

            // holder.tvText = (TextView) view.findViewById(R.id.tvText);

            view.setTag(holder);

        } else {
            holder = (ViewHolder) view.getTag();
        }

        try {

           // holder.tvText.setText(list.get(position).getS1());

        } catch (Exception e) {

        }

        return view;
    }
}