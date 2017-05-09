package com.example.rinnv.tieuluancnpm.MiniFragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rinnv.tieuluancnpm.Adapter.Adapter_Word;
import com.example.rinnv.tieuluancnpm.Entity.Word;
import com.example.rinnv.tieuluancnpm.Entity.WordRelationShip;
import com.example.rinnv.tieuluancnpm.FrameWork.SaveObject;
import com.example.rinnv.tieuluancnpm.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.rinnv.tieuluancnpm.Activity.MainActivity.db;
import static com.example.rinnv.tieuluancnpm.Activity.Word_Activity.adapterWord;
import static com.example.rinnv.tieuluancnpm.Activity.Word_Activity.listView_Word;

/**
 * Created by rinnv on 5/9/2017.
 */

public class MenuWordFragment {
    public void createDetailView(final Context context, final Activity parentActivity, final Word item) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(parentActivity);
        dialogBuilder.setTitle("Detail: ");
        LayoutInflater inflater = parentActivity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_detail_dialog, null);
        List<WordRelationShip> wordRelationShips = db.GetRalationShipWord(item.getWord_Id());

        TextView textEN = (TextView) dialogView.findViewById(R.id.example_EN);
        textEN.setText("EX English: " + item.getExample());

        TextView textVN = (TextView) dialogView.findViewById(R.id.example_VN);
        textVN.setText("EX Viet nam: " + item.getExample_VN());

        ListView listView = (ListView) dialogView.findViewById(R.id.lst_relationship);
        List<HashMap<String, String>> fillMaps = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < wordRelationShips.size(); i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("Word_Title", wordRelationShips.get(i).getWord_Title());
            map.put("Word_Title_VN", wordRelationShips.get(i).getWord_Title_VN());
            fillMaps.add(map);
        }

        if (fillMaps.size() > 0) {
            listView.setAdapter(new SimpleAdapter(context, fillMaps, R.layout.item_detail_word_layoout,
                    new String[]{"Word_Title", "Word_Title_VN"}, new int[]{R.id.relationshipEN, R.id.relationshipVN}));
        }

        dialogBuilder.setCancelable(true).setView(dialogView).create().show();
    }

    public void createAddRelationShipView(final Context context) {
        Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
    }
    public void createDeleteWordView(final  Context context,final Word word)
    {
        new AlertDialog.Builder(context)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Xóa từ vựng")
                .setMessage("Bạn có chắc muốn xóa từ vựng này không?")
                .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db.deleteWord(word);
                        adapterWord = new Adapter_Word(context, db.getListWord(SaveObject.saveTopic), (Activity) context);
                        listView_Word.setAdapter(adapterWord);
                        listView_Word.invalidate();
                    }

                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}
