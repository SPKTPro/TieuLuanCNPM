package com.example.rinnv.tieuluancnpm;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

/**
 * Created by rinnv on 25/10/2016.
 */

public class Adapter_Word extends BaseAdapter
{
    private ArrayList<Word> items;
    private LayoutInflater itemInflater;
    private Context mContext;

    @Override
    public int getCount() {
        return items.size();
    }

    public Adapter_Word(Context c, ArrayList<Word> the_Items) {
        items = the_Items;
        itemInflater = LayoutInflater.from(c);
        mContext=c;
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

        final RelativeLayout Layout = (RelativeLayout) itemInflater.inflate(R.layout.item_layout_word,
                parent,
                false);

        final TextView titleView = (TextView) Layout.findViewById(R.id.itemtitle);
        TextView titleView2 = (TextView) Layout.findViewById(R.id.itemtitle2);
        Button btnCheckSpell =(Button) Layout.findViewById(R.id.btnCheckSpell);
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
                Log.d("Tag", "onCheckedChanged: ");
                db.CheckWord(isChecked,item);
                items = db.getListWord(SaveObject.saveTopic);
            }
        });

        btnCheckSpell.setFocusable(false);
        btnCheckSpell.setFocusableInTouchMode(false);
        btnCheckSpell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(((Word_Activity)mContext).isConnected()){
                    ((Word_Activity)mContext).startSpeechToText(item.getWord_Title().toString());                }
                else{
                    Toast.makeText(mContext, "Plese Connect to Internet", Toast.LENGTH_LONG).show();
                }
            }
        });

        Layout.setTag(position);


        return Layout;

    }


}
