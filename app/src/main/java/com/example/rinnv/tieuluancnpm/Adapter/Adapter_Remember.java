package com.example.rinnv.tieuluancnpm.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.rinnv.tieuluancnpm.DatabaseUtility.SQLiteDataController;
import com.example.rinnv.tieuluancnpm.Entity.Word;
import com.example.rinnv.tieuluancnpm.FrameWork.SaveObject;
import com.example.rinnv.tieuluancnpm.MiniFragment.MenuWordFragment;
import com.example.rinnv.tieuluancnpm.R;

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
        ImageButton detail = (ImageButton) Layout.findViewById(R.id.btn_detail);
        final SQLiteDataController db = new SQLiteDataController(parent.getContext());


        final Word item = items.get(position);
        titleView.setText(item.getWord_Title().toUpperCase()+item.getWord_Type().toLowerCase());
        titleView2.setText(item.getWord_Title_VN());
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
        detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Adapter_Word.Item[] items = {
                        new Adapter_Word.Item("See relationship", R.drawable.detail2),
                        new Adapter_Word.Item("Add relationship", R.drawable.add),
                        new Adapter_Word.Item("Delete this word", R.drawable.del2),
                        new Adapter_Word.Item("Go to topic",R.drawable.goto2)
                };

                ListAdapter adapter = new ArrayAdapter<Adapter_Word.Item>(
                        parent.getContext(),
                        R.layout.dialog_single_choice,
                        R.id.text1,
                        items){
                    public View getView(int position, View convertView, ViewGroup parent) {
                        //Use super class to create the View
                        View v = super.getView(position, convertView, parent);
                        TextView tv = (TextView)v.findViewById(R.id.text1);
                        ImageView img = (ImageView)v.findViewById(R.id.img1);
                        //Put the image on the TextView
                        tv.setText(items[position].text);
                        img.setBackgroundResource(items[position].icon);

                        return v;
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(parent.getContext());
                builder.setTitle("Single Choice");
                builder.setAdapter(adapter, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        switch (which)
                        {
                            case 0:
                                new MenuWordFragment().createDetailView(parent.getContext(), (Activity) parent.getContext(),item);
                                break;
                            case 1:
                                new MenuWordFragment().createAddRelationShipView(parent.getContext(),item);
                                break;
                            case 2:
                                new MenuWordFragment().createDeleteWordView(parent.getContext(),item,true);
                                break;
                            case 3:
                                new MenuWordFragment().createGotoWordDetail(parent.getContext(),item);
                                break;
                            default:
                                break;
                        }
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                builder.create().show();
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
