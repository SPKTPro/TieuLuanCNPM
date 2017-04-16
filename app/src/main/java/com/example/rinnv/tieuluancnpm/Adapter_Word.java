package com.example.rinnv.tieuluancnpm;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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


    @Override
    public int getCount() {
        return items.size();
    }

    public Adapter_Word(Context c, ArrayList<Word> the_Items) {
        items = the_Items;
        itemInflater = LayoutInflater.from(c);
        mContext = c;
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
        TextView score = (TextView) Layout.findViewById(R.id.pronoun);
        ImageButton btnCheckSpell = (ImageButton) Layout.findViewById(R.id.btnCheckSpell);
        final SQLiteDataController db = new SQLiteDataController(parent.getContext());

        final Word item = items.get(position);
        titleView.setText(item.getWord_Title());
        titleView2.setText(item.getWord_Title_VN());
        score.setText("" + item.getWord_Pronoun());


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

        dialog.setTitle("Pronunciation");
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
        Log.d(TAG, "RefreshDialogView: "+dialog.isShowing());
        dialogView.invalidate();
        dialog.show();
    }


}

