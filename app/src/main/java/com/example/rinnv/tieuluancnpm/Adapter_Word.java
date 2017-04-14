package com.example.rinnv.tieuluancnpm;

import android.app.Dialog;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Locale;
import java.util.StringTokenizer;

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
        TextView score = (TextView) Layout.findViewById(R.id.pronoun);
        ImageButton btnCheckSpell =(ImageButton) Layout.findViewById(R.id.btnCheckSpell);
        final SQLiteDataController db = new SQLiteDataController(parent.getContext());



        final Word item = items.get(position);
        titleView.setText(item.getWord_Title() );
        titleView2.setText(item.getWord_Title_VN() );
        score.setText(""+item.getWord_Pronoun());




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

        final Dialog dialog = new Dialog(mContext);
        dialog.setTitle("Pronunciation");
        dialog.setContentView(R.layout.recoder_layout);
        ImageButton btnPronoun = (ImageButton) dialog.findViewById(R.id.imageButton);
        final ImageView star1 =(ImageView) dialog.findViewById(R.id.star1);
        final ImageView star2 =(ImageView) dialog.findViewById(R.id.star2);
        final ImageView star3 =(ImageView) dialog.findViewById(R.id.star3);
        btnCheckSpell.setFocusable(false);
        btnCheckSpell.setFocusableInTouchMode(false);
        btnPronoun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(((Word_Activity)mContext).isConnected()){
                    ((Word_Activity)mContext).startSpeechToText(item.getWord_Title().toString());                }
                else{
                    Toast.makeText(mContext, "Please Connect to Internet", Toast.LENGTH_LONG).show();
                }
            }
        });
        btnCheckSpell.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                dialog.show();
                int a =Integer.parseInt(item.getWord_Pronoun()+"") ;
                Log.d("tag",a+"");

                if(a!=0) {
                    if (a == 1)
                        star3.setImageResource(R.drawable.star2);
                    else
                        if (a == 2) {
                        star3.setImageResource(R.drawable.star2);
                        star2.setImageResource(R.drawable.star2);
                    } else {
                        star1.setImageResource(R.drawable.star2);
                        star2.setImageResource(R.drawable.star2);
                        star3.setImageResource(R.drawable.star2);
                    }
                }

            }
        });


        Layout.setTag(position);


        return Layout;

    }


}
