package com.example.rinnv.tieuluancnpm.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.rinnv.tieuluancnpm.Entity.Maintopic;
import com.example.rinnv.tieuluancnpm.R;

import java.util.ArrayList;

import static com.example.rinnv.tieuluancnpm.Adapter.Adapter_Topic.removeCharAt;

/**
 * Created by rinnv on 25/10/2016.
 */

public class Adapter_Maintopic extends BaseAdapter {
    private ArrayList<Maintopic> items;
    private LayoutInflater itemInflater;
    private Context context;

    @Override
    public int getCount() {
        return items.size();
    }

    public Adapter_Maintopic(Context c, ArrayList<Maintopic> the_Items) {
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

        RelativeLayout Layout = (RelativeLayout) itemInflater.inflate(R.layout.item_layout, parent, false);
        ImageView imageView = (ImageView) Layout.findViewById(R.id.imageView);
        TextView countView = (TextView) Layout.findViewById(R.id.count);
        TextView titleView2 = (TextView) Layout.findViewById(R.id.itemtitle2);
        ProgressBar progressBar = (ProgressBar) Layout.findViewById(R.id.seekBar);
        TextView titleView = (TextView) Layout.findViewById(R.id.itemtitle);

        Maintopic item = items.get(position);
        titleView.setText(item.getMaintopic_Tittle().toUpperCase());
        countView.setText(item.getCountTopic()+" Topics");
        titleView2.setText(item.getMaintopic_Tittle_VN());
        String s= item.getMaintopic_Tittle().toString().toLowerCase();
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
        i=-1;
        while(i!=-2) {
            i = s.indexOf("&");

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
        progressBar.setProgress(item.getMaintopic_Process());


        Layout.setTag(position);
        return Layout;

    }
}

