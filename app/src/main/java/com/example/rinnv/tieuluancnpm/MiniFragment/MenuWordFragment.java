package com.example.rinnv.tieuluancnpm.MiniFragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.rinnv.tieuluancnpm.Activity.Word_Activity;
import com.example.rinnv.tieuluancnpm.Adapter.Adapter_RelationshipWord;
import com.example.rinnv.tieuluancnpm.Adapter.Adapter_Remember;
import com.example.rinnv.tieuluancnpm.Adapter.Adapter_Word;
import com.example.rinnv.tieuluancnpm.DatabaseUtility.SQLiteDataController;
import com.example.rinnv.tieuluancnpm.Entity.Maintopic;
import com.example.rinnv.tieuluancnpm.Entity.Topic;
import com.example.rinnv.tieuluancnpm.Entity.Word;
import com.example.rinnv.tieuluancnpm.Entity.WordRelationShip;
import com.example.rinnv.tieuluancnpm.FrameWork.CreateItemType;
import com.example.rinnv.tieuluancnpm.FrameWork.SaveObject;
import com.example.rinnv.tieuluancnpm.R;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;
import static com.example.rinnv.tieuluancnpm.Activity.MainActivity.adapterRemember;
import static com.example.rinnv.tieuluancnpm.Activity.MainActivity.db;
import static com.example.rinnv.tieuluancnpm.Activity.MainActivity.listView_Remember;
import static com.example.rinnv.tieuluancnpm.Activity.Word_Activity.adapterWord;
import static com.example.rinnv.tieuluancnpm.Activity.Word_Activity.listView_Word;

/**
 * Created by rinnv on 5/9/2017.
 */

public class MenuWordFragment {

    public static void createDetailView(final Context context, final Activity parentActivity, final Word item) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(parentActivity);
        dialogBuilder.setTitle("Detail: ");
        LayoutInflater inflater = parentActivity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_detail_dialog, null);
        ArrayList<WordRelationShip> wordRelationShips = db.GetRalationShipWord(item.getWord_Id());

        ListView listView = (ListView) dialogView.findViewById(R.id.lst_relationship);

        if (wordRelationShips.size() > 0) {
            Adapter_RelationshipWord adapter_relationshipWord = new Adapter_RelationshipWord(context, wordRelationShips);
            listView.setAdapter(adapter_relationshipWord);
        }

        dialogBuilder.setCancelable(true).setView(dialogView).create().show();
    }

    public static void createAddRelationShipView(final Context context, final Word word) {
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.layout_add_word, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);
        final EditText EN = (EditText) promptsView.findViewById(R.id.mainTopic_EN);
        final EditText VN = (EditText) promptsView.findViewById(R.id.mainTopic_VN);
        final EditText Type = (EditText) promptsView.findViewById(R.id.loaitu);
        final String id = "" + word.getWord_Id();
        alertDialogBuilder.setView(promptsView).setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        boolean x = db.insertRelationship(id, EN.getText().toString().trim(), VN.getText().toString().trim(),
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

    public static void createDeleteWordView(final Context context, final Word word, boolean fromRemidWord) {
        final boolean RemindWord = Boolean.valueOf(fromRemidWord);
        new AlertDialog.Builder(context)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Xóa từ vựng")
                .setMessage("Bạn có chắc muốn xóa từ vựng này không?")
                .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db.deleteWord(word);
                        if (RemindWord) {
                            adapterRemember = new Adapter_Remember(context, db.getLisCheckedtWord());
                            listView_Remember.setAdapter(adapterRemember);
                            listView_Remember.invalidate();

                        } else {
                            adapterWord = new Adapter_Word(context, db.getListWord(SaveObject.saveTopic), (Activity) context);
                            listView_Word.setAdapter(adapterWord);
                            listView_Word.invalidate();
                        }
                    }

                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    // this is only called in remind page
    public void createGotoWordDetail(final Context context, final Word wordinput) {
        ArrayList<Word> listResult = new ArrayList<Word>();
        listResult = db.SearchWord(wordinput.getWord_Title().trim().toUpperCase());
        Word detailWord = listResult.get(0);

        Maintopic maintopic = new Maintopic(detailWord.getWord_Pronoun(), detailWord.getExample(), detailWord.Maintopic_Tile, 0, 0);
        Topic topic = new Topic(detailWord.getWord_Pronoun(), detailWord.getTopic_Id(), detailWord.getExample_VN(), null, 0, 0);
        SaveObject.currentMaintopic = maintopic;
        SaveObject.saveTopic = topic;
        Intent intent = new Intent(context, Word_Activity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("Focus", detailWord.getWord_Id());
        context.startActivity(intent);

    }

    public static void createCopyView(final Context mContext, final Word word, final boolean isCopy, final View rootView) {

        try {
            Dialog dialog = null;
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mContext);
            dialogBuilder.setTitle("Maintopic: ");
            final List<Maintopic> maintopics = SQLiteDataController.GetSQLController().getListMainTopic();
            CharSequence[] ms = new CharSequence[maintopics.size()];

            for (int i = 0; i < maintopics.size(); i++) {
                ms[i] = maintopics.get(i).getMaintopic_Tittle();
            }
            dialogBuilder.setItems(ms, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Maintopic maintopic = maintopics.get(i);
                    CreateChooseTopic(maintopic, mContext, word, isCopy, rootView);
                }
            });
            dialog = dialogBuilder.create();
            dialog.show();
        } catch (Exception ex) {
            Log.e(TAG, "createCopyView: ", ex);
        }

    }

    private static void CreateChooseTopic(Maintopic maintopic, final Context context, final Word word, final boolean isCopy, final View rootView) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setTitle("Detail: ");
        final List<Topic> topics = SQLiteDataController.GetSQLController().getListTopic(maintopic);
        CharSequence[] ms = new CharSequence[topics.size()];
        for (int i = 0; i < topics.size(); i++) {
            ms[i] = topics.get(i).getTopic_Title();
        }
        dialogBuilder.setItems(ms, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Topic topic = topics.get(i);
                createSnackBar(context, rootView, word.getWord_Title(), word.getWord_Title_VN(), word.getWord_Type().replace("(", "").replace(")", ""), topic, isCopy, word.getWord_Id());

            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        dialogBuilder.create().show();
    }

    private static void createSnackBar(final Context context, View rootView, final String ITemEN, final String ITemVN, @Nullable final String WordType,
                                       final Topic topic, final boolean isCopy, final int wordId) {
        Snackbar.make(rootView, "Chọn UNDO để hủy thao tác", Snackbar.LENGTH_LONG)
                .setCallback(new Snackbar.Callback() {
                    @Override
                    public void onDismissed(Snackbar snackbar, int event) {
                        switch (event) {
                            case Snackbar.Callback.DISMISS_EVENT_ACTION:
                                Toast.makeText(context, "Hủy thao tác", Toast.LENGTH_LONG).show();
                                break;
                            case Snackbar.Callback.DISMISS_EVENT_TIMEOUT:
                                boolean isExist = db.isExist(ITemEN, topic.getTopic_Id());
                                if (isExist) {
                                    Toast.makeText(context, "this word is exist", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                if (isCopy) {
                                    CopyWord(context, topic.getTopic_Id(), ITemEN, ITemVN, WordType, wordId);
                                } else {
                                    MoveWord(context, topic.getTopic_Id(), wordId);
                                }
                                break;
                        }
                    }

                })
                .setAction("Undo", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                })
                .setActionTextColor(Color.RED)
                .show();
    }

    private static void CopyWord(Context context, String destinationTopicId, String WordEN, String WordVN, String wordType, int rootWordID) {
        boolean x = db.insertWord(destinationTopicId, WordEN, WordVN, wordType);
        Word word = db.getWordByNameAndTopicID(destinationTopicId, WordEN);
        // copy relation word
        List<WordRelationShip> wordRelationShips = db.GetRalationShipWord(rootWordID);
        for (WordRelationShip w : wordRelationShips) {
            x = db.insertRelationship(String.valueOf(word.getWord_Id()), w.getWord_Title(), w.getWord_Title_VN(), w.getWordType().replace("(", "").replace(")", ""));
        }
        if (x) {
            Toast.makeText(context, "Copy word successfull", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Fail", Toast.LENGTH_SHORT).show();
        }
    }

    private static void MoveWord(Context context, String destinationTopicId, int rootWordID) {
        boolean x = false;
        String SourceTopic = SaveObject.saveTopic.getTopic_Id();
        x = db.MoveWord(SourceTopic, rootWordID, destinationTopicId);

        if (x) {
            Toast.makeText(context, "Move word successfull", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Fail", Toast.LENGTH_SHORT).show();
        }
    }
}
