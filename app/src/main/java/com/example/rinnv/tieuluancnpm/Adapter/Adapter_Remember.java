package com.example.rinnv.tieuluancnpm.Adapter;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.rinnv.tieuluancnpm.R;
import com.example.rinnv.tieuluancnpm.DatabaseUtility.SQLiteDataController;
import com.example.rinnv.tieuluancnpm.FrameWork.SaveObject;
import com.example.rinnv.tieuluancnpm.Entity.Word;

import java.util.ArrayList;

import static com.example.rinnv.tieuluancnpm.FrameWork.SaveObject.mTts;

/**
 * Created by rinnv on 25/10/2016.
 */

public class Adapter_Remember extends BaseAdapter {
    public ArrayList<Word> items;
    public LayoutInflater itemInflater;

    @Override
    public int getCount() {
        return items.size();
    }

    public Adapter_Remember(Context c, ArrayList<Word> the_Items) {
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
    public View getView(int position, final View convertView, final ViewGroup parent) {

        final RelativeLayout Layout = (RelativeLayout) itemInflater.inflate(R.layout.remember_word_layout,
                parent,
                false);

        TextView titleView = (TextView) Layout.findViewById(R.id.itemtitle);
        TextView titleView2 = (TextView) Layout.findViewById(R.id.itemtitle2);
        final SQLiteDataController db = new SQLiteDataController(parent.getContext());


        final Word item = items.get(position);
        titleView.setText(item.getWord_Title().toUpperCase()+item.getWord_Type().toLowerCase());
        titleView2.setText(item.getWord_Title_VN().toLowerCase());
        final CheckBox checkBox = (CheckBox) Layout.findViewById(R.id.checkBox);

        checkBox.setFocusable(false);
        checkBox.setFocusableInTouchMode(false);


        Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTts.speak(item.getWord_Title().trim(), TextToSpeech.QUEUE_FLUSH, null);
            }
        });

        checkBox.setChecked(item.getWord_Remind());
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                db.CheckWordRemind(isChecked, item);
                SaveObject.remindWord = db.getListRemindWord();
                items = db.getLisCheckedtWord();
            }
        });


        checkBox.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (checkBox.isChecked()) {
                    for (Word item : items) {
                        db.CheckWordRemind(false, item);
                        checkBox.setChecked(false);

                    }
                } else {
                    for (Word item : items) {
                        db.CheckWordRemind(true, item);
                        checkBox.setChecked(true);
                    }
                }


                SaveObject.remindWord = db.getListRemindWord();
                 ArrayList<Word> dataitems = db.getLisCheckedtWord();

                refresAdapter(dataitems);

                parent.invalidate();
                parent.clearFocus();
                return true;
            }
        });


        Layout.setTag(position);
        return Layout;

    }

    public synchronized void refresAdapter(ArrayList<Word> dataitems) {

        items.clear();
        items.addAll(dataitems);
        notifyDataSetChanged();
    }
}
