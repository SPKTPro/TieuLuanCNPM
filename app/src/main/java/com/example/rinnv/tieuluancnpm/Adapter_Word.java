package com.example.rinnv.tieuluancnpm;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by rinnv on 25/10/2016.
 */

public class Adapter_Word extends BaseAdapter {
    private ArrayList<Word> items;
    private LayoutInflater itemInflater;

    @Override
    public int getCount() {
        return items.size();
    }

    public Adapter_Word(Context c, ArrayList<Word> the_Items) {
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
    public View getView(int position, View convertView, final ViewGroup parent) {

        RelativeLayout Layout = (RelativeLayout) itemInflater.inflate(R.layout.item_layout_word,
                parent,
                false);

        TextView titleView = (TextView) Layout.findViewById(R.id.itemtitle);
        TextView titleView2 = (TextView) Layout.findViewById(R.id.itemtitle2);
        final SQLiteDataController db = new SQLiteDataController(parent.getContext());



        final Word item = items.get(position);
        titleView.setText(item.getWord_Title() );
        titleView2.setText(item.getWord_Title_VN() );
        CheckBox checkBox = (CheckBox) Layout.findViewById(R.id.checkBox);
        checkBox.setFocusable(false);
        checkBox.setFocusableInTouchMode(false);
        checkBox.setChecked(item.getWord_check());
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                db.CheckWord(isChecked,item);
            }
        });
        Layout.setTag(position);
        return Layout;

    }
}
