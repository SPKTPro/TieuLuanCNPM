package com.example.rinnv.tieuluancnpm.MiniFragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.rinnv.tieuluancnpm.Adapter.Adapter_RelationshipWord;
import com.example.rinnv.tieuluancnpm.Adapter.Adapter_Word;
import com.example.rinnv.tieuluancnpm.Entity.Word;
import com.example.rinnv.tieuluancnpm.Entity.WordRelationShip;
import com.example.rinnv.tieuluancnpm.FrameWork.SaveObject;
import com.example.rinnv.tieuluancnpm.R;

import java.util.ArrayList;

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
        ArrayList<WordRelationShip> wordRelationShips = db.GetRalationShipWord(item.getWord_Id());

        ListView listView = (ListView) dialogView.findViewById(R.id.lst_relationship);

        if (wordRelationShips.size() > 0) {
            Adapter_RelationshipWord adapter_relationshipWord = new Adapter_RelationshipWord(context,wordRelationShips);
            listView.setAdapter(adapter_relationshipWord);
        }

        dialogBuilder.setCancelable(true).setView(dialogView).create().show();
    }

    public void createAddRelationShipView(final Context context, final Word word) {
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.layout_add_word, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);
        final EditText EN = (EditText) promptsView.findViewById(R.id.mainTopic_EN);
        final EditText VN = (EditText) promptsView.findViewById(R.id.mainTopic_VN);
        final EditText Type = (EditText) promptsView.findViewById(R.id.loaitu);
        final String id= ""+word.getWord_Id();
        alertDialogBuilder.setView(promptsView).setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                          boolean x = db.insertRelationship(id, EN.getText().toString().trim() , VN.getText().toString().trim(),
                                Type.getText().toString().trim());
                        Toast.makeText(context, x ? "Thêm thành công" : "Thêm thất bại", Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                }).create().show();


    }

    public void createDeleteWordView(final Context context, final Word word) {
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
