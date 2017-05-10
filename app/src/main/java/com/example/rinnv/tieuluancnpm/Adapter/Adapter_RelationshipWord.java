package com.example.rinnv.tieuluancnpm.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.rinnv.tieuluancnpm.DatabaseUtility.SQLiteDataController;
import com.example.rinnv.tieuluancnpm.Entity.WordRelationShip;
import com.example.rinnv.tieuluancnpm.R;

import java.util.ArrayList;

/**
 * Created by rinnv on 5/10/2017.
 */

public class Adapter_RelationshipWord extends BaseAdapter {
    private ArrayList<WordRelationShip> wordRelationShips;
    private LayoutInflater itemInflater;
    private Context context;

    public Adapter_RelationshipWord(Context c, ArrayList<WordRelationShip> the_Items) {
        context = c;
        wordRelationShips = the_Items;
        itemInflater = LayoutInflater.from(c);
    }

    @Override
    public int getCount() {
        return wordRelationShips.size();
    }

    @Override
    public Object getItem(int i) {
        return wordRelationShips.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {


        LinearLayout Layout = (LinearLayout) itemInflater.inflate(R.layout.item_detail_word_layoout, viewGroup, false);
        TextView EN = (TextView) Layout.findViewById(R.id.relationshipEN);
        TextView VN = (TextView) Layout.findViewById(R.id.relationshipVN);
        Button delete = (Button) Layout.findViewById(R.id.delete);


        WordRelationShip  wordRelationShip= wordRelationShips.get(i);
        EN.setText(wordRelationShip.getWord_Title().toUpperCase());
        VN.setText(wordRelationShip.getWord_Title_VN());

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(context)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Xóa từ vựng")
                        .setMessage("Bạn có chắc muốn xóa từ vựng này không?")
                        .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                WordRelationShip RelationShip = wordRelationShips.get(i);
                                SQLiteDataController sqLiteDataController = new SQLiteDataController(context);
                                sqLiteDataController.deleteRelationShip(RelationShip);
                                wordRelationShips= sqLiteDataController.GetRalationShipWord(RelationShip.getWord_Root());
                                notifyDataSetChanged();
                            }

                        })
                        .setNegativeButton("Hủy", null)
                        .show();
            }
        });

        return Layout;
    }
}
