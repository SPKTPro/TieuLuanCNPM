package com.example.rinnv.tieuluancnpm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by rinnv on 25/10/2016.
 */

public class Adapter_Maintopic extends BaseAdapter {
    private ArrayList<Maintopic> items;
    private LayoutInflater itemInflater;

    @Override
    public int getCount() {
        return items.size();
    }

    public Adapter_Maintopic(Context c, ArrayList<Maintopic> the_Items)
    {
        items = the_Items;
        itemInflater = LayoutInflater.from(c);
    }


    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LinearLayout Layout = (LinearLayout)itemInflater.inflate(R.layout.item_layout,
                parent,
                false);

        TextView titleView  = (TextView)Layout.findViewById(R.id.itemtitle);
        ProgressBar progressBar = (ProgressBar) Layout.findViewById(R.id.seekBar);


        Maintopic item = items.get(position);
        titleView.setText(item.getMaintopic_Tittle() + " - " + item.getMaintopic_Tittle_VN());
        titleView.setTextSize(20);

        progressBar.setMax(100);
        progressBar.setProgress(item.getMaintopic_Process()+ 50);


        Layout.setTag(position);
        return Layout;

    }
}

