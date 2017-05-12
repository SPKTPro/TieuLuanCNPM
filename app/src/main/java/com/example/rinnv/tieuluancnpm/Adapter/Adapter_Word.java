package com.example.rinnv.tieuluancnpm.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rinnv.tieuluancnpm.Activity.Word_Activity;
import com.example.rinnv.tieuluancnpm.DatabaseUtility.SQLiteDataController;
import com.example.rinnv.tieuluancnpm.Entity.Word;
import com.example.rinnv.tieuluancnpm.FrameWork.SaveObject;
import com.example.rinnv.tieuluancnpm.MiniFragment.MenuWordFragment;
import com.example.rinnv.tieuluancnpm.R;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by rinnv on 25/10/2016.
 */


public class Adapter_Word extends BaseAdapter {
    private ArrayList<Word> items;
    private LayoutInflater itemInflater;
    private Context mContext;
    private View dialogView;
    protected Dialog dialog;
    ImageButton btnPronoun;
    protected ImageView star1;
    protected ImageView star2;
    protected ImageView star3;
    protected static String selectedWord;
    private Activity parentActivity;


    @Override
    public int getCount() {
        return items.size();
    }

    public Adapter_Word(Context c, ArrayList<Word> the_Items, Activity parentActivity) {
        items = the_Items;
        itemInflater = LayoutInflater.from(c);
        mContext = c;
        this.parentActivity = parentActivity;
        dialogView = LayoutInflater.from(mContext).inflate(R.layout.recoder_layout, null);
        dialog = new Dialog(mContext);
        btnPronoun = (ImageButton) dialogView.findViewById(R.id.imageButton);
        star1 = (ImageView) dialogView.findViewById(R.id.star1);
        star2 = (ImageView) dialogView.findViewById(R.id.star2);
        star3 = (ImageView) dialogView.findViewById(R.id.star3);
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
      //  TextView score = (TextView) Layout.findViewById(R.id.pronoun);
        ImageButton btnCheckSpell = (ImageButton) Layout.findViewById(R.id.btnCheckSpell);
        ImageButton btnDetail = (ImageButton) Layout.findViewById(R.id.btn_detail);
        final SQLiteDataController db = new SQLiteDataController(parent.getContext());

        final Word item = items.get(position);
        titleView.setText(item.getWord_Title());
        titleView2.setText(item.getWord_Title_VN());
       // score.setText("" + item.getWord_Pronoun());


        CheckBox checkBox = (CheckBox) Layout.findViewById(R.id.checkBox);
        checkBox.setFocusable(false);
        checkBox.setFocusableInTouchMode(false);
        checkBox.setChecked(item.getWord_check());
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d("Tag", "onCheckedChanged: ");
                db.CheckWord(isChecked, item);
                items = db.getListWord(SaveObject.saveTopic);
            }
        });

        dialog.setTitle("Pronounciation");
        dialog.setContentView(dialogView);

        //nút gọi dialog
        btnCheckSpell.setFocusable(false);
        btnCheckSpell.setFocusableInTouchMode(false);
        btnCheckSpell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int a = Integer.parseInt(item.getWord_Pronoun() + "");
                //chỗ này display chứ ko phải reset
                DisplayStar(a);
                selectedWord = item.getWord_Title();
                dialog.show();

            }
        });

        // nut goi voice recognize
        btnPronoun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((Word_Activity) mContext).isConnected()) {
                    ResetStar();
                    dialog.dismiss();
                    ((Word_Activity) mContext).startSpeechToText(selectedWord);
                } else {
                    Toast.makeText(mContext, "Please Connect to Internet", Toast.LENGTH_LONG).show();
                }
            }
        });


        //nut xem vi du, cac tu loai lien quan
        btnDetail.setFocusable(false);
        btnDetail.setFocusableInTouchMode(false);
        btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(item);
            }
        });

        Layout.setTag(position);
        return Layout;
    }

    protected void ResetStar() {
        star1.setImageResource(R.drawable.star1);
        star2.setImageResource(R.drawable.star1);
        star3.setImageResource(R.drawable.star1);
    }

    protected void DisplayStar(int count) {
        Log.d(TAG, "refreshDialog reci: " + count);
        ResetStar();
        if (count != 0) {
            if (count == 1)
                star3.setImageResource(R.drawable.star2);
            else if (count == 2) {
                star3.setImageResource(R.drawable.star2);
                star2.setImageResource(R.drawable.star2);
            } else {
                star1.setImageResource(R.drawable.star2);
                star2.setImageResource(R.drawable.star2);
                star3.setImageResource(R.drawable.star2);
            }
        }
    }

    public void RefreshDialogView(int a) {
        DisplayStar(a);
        Log.d(TAG, "RefreshDialogView: " + dialog.isShowing());
        dialogView.invalidate();
        dialog.show();
    }
    public static class Item{
        public final String text;
        public final int icon;
        public Item(String text, Integer icon) {
            this.text = text;
            this.icon = icon;
        }
        @Override
        public String toString() {
            return text;
        }
    }
    private void showDialog(final Word word)
    {
        /*
        final String[] commandArray=new String[] {"See relationship","Add relationship","Delete this word"};
        android.app.AlertDialog.Builder builder = new AlertDialog.Builder(parentActivity);
        builder.setTitle("Single Choice");
        builder.setItems(commandArray, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                switch (which)
                {
                    case 0:
                        new MenuWordFragment().createDetailView(mContext,parentActivity,word);
                        break;
                    case 1:
                        new MenuWordFragment().createAddRelationShipView(mContext,word);
                        break;
                    case 2:
                        new MenuWordFragment().createDeleteWordView(mContext,word);
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
        AlertDialog alert = builder.create();
        alert.show();
*/
        final Item[] items = {
                new Item("See relationship", R.drawable.detail2),
                new Item("Add relationship", R.drawable.add),
                new Item("Delete this word", R.drawable.del2),//no icon for this one
        };

        ListAdapter adapter = new ArrayAdapter<Item>(
                mContext,
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

/*
        new AlertDialog.Builder(mContext)
                .setTitle("Share Appliction")
                .setAdapter(adapter, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {

                    }
                }).show();*/

        android.app.AlertDialog.Builder builder = new AlertDialog.Builder(parentActivity);
        builder.setTitle("Single Choice");
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                switch (which)
                {
                    case 0:
                        new MenuWordFragment().createDetailView(mContext,parentActivity,word);
                        break;
                    case 1:
                        new MenuWordFragment().createAddRelationShipView(mContext,word);
                        break;
                    case 2:
                        new MenuWordFragment().createDeleteWordView(mContext,word);
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
        AlertDialog alert = builder.create();
        alert.show();
    }

}

