package com.example.rinnv.tieuluancnpm;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by rinnv on 25/10/2016.
 */

public class Adapter_Topic extends BaseAdapter {
    private ArrayList<Topic> items;
    private LayoutInflater itemInflater;
    private Context context;

    @Override
    public int getCount() {
        return items.size();
    }

    public Adapter_Topic(Context c, ArrayList<Topic> the_Items)
    {
        context = c;
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
        RelativeLayout Layout = (RelativeLayout)itemInflater.inflate(R.layout.item_layout,
                parent,
                false);

        ImageView imageView = (ImageView) Layout.findViewById(R.id.imageView);
        TextView titleView  = (TextView)Layout.findViewById(R.id.itemtitle);
        ProgressBar progressBar = (ProgressBar) Layout.findViewById(R.id.seekBar);
        TextView titleView2  = (TextView)Layout.findViewById(R.id.itemtitle2);

        Topic item = items.get(position);
        titleView.setText(item.getTopic_Title()+" - "+item.getCountWord() +" word(s)");


        titleView2.setText(item.getTopic_Title_VN());
        String s= item.getTopic_Title().toString().toLowerCase();
        int i=-1;
        while(i!=-2) {
            i = s.indexOf(" ");
            if(i!=-1) {
                s = removeCharAt(s, i);
                i=-1;
            }
            else
             i=-2;
        }
        int imageResource = Layout.getResources().getIdentifier(s, "drawable",context.getPackageName());
        if(imageResource==0) {
            imageView.setImageResource(R.drawable.newtopic);
        }
        else
            imageView.setImageResource(imageResource);

        progressBar.setMax(100);
        progressBar.setProgress(item.getTopic_Process());


        Layout.setTag(position);
        return Layout;

    }
    public static String removeCharAt(String s, int pos) {
        return s.substring(0, pos) + s.substring(pos + 1);
    }
}

