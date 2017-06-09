package com.example.rinnv.tieuluancnpm.MiniFragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rinnv.tieuluancnpm.Activity.Game;
import com.example.rinnv.tieuluancnpm.Activity.Test;
import com.example.rinnv.tieuluancnpm.Activity.Topic_Activity;
import com.example.rinnv.tieuluancnpm.Activity.Word_Activity;
import com.example.rinnv.tieuluancnpm.Adapter.Adapter_Maintopic;
import com.example.rinnv.tieuluancnpm.Adapter.Adapter_Topic;
import com.example.rinnv.tieuluancnpm.Adapter.Adapter_Word;
import com.example.rinnv.tieuluancnpm.DatabaseUtility.ExportDatabaseCSVTask;
import com.example.rinnv.tieuluancnpm.DatabaseUtility.FileDialog;
import com.example.rinnv.tieuluancnpm.Entity.Maintopic;
import com.example.rinnv.tieuluancnpm.Entity.Topic;
import com.example.rinnv.tieuluancnpm.Entity.Word;
import com.example.rinnv.tieuluancnpm.FrameWork.CreateItemType;
import com.example.rinnv.tieuluancnpm.FrameWork.SaveObject;
import com.example.rinnv.tieuluancnpm.FrameWork.Utility;
import com.example.rinnv.tieuluancnpm.R;

import java.util.ArrayList;
import java.util.List;

import static com.example.rinnv.tieuluancnpm.Activity.MainActivity.adapterMaintopic;
import static com.example.rinnv.tieuluancnpm.Activity.MainActivity.db;
import static com.example.rinnv.tieuluancnpm.Activity.MainActivity.listView_Maintopic;
import static com.example.rinnv.tieuluancnpm.Activity.Topic_Activity.adapter_topic;
import static com.example.rinnv.tieuluancnpm.Activity.Topic_Activity.listView_Topic;
import static com.example.rinnv.tieuluancnpm.Activity.Word_Activity.adapterWord;
import static com.example.rinnv.tieuluancnpm.Activity.Word_Activity.listView_Word;
import static com.example.rinnv.tieuluancnpm.FrameWork.CreateItemType.Topic;

/**
 * Created by rinnv on 5/6/2017.
 */

public class MenuPracticeFragment {
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
    public void createMenuPractice(final Context context, final String level) {
     //   CharSequence[] array = {"Game 1", "Game 2", "Game chinh ta"};
        final Adapter_Word.Item[] items = {
                new Adapter_Word.Item("Game trắc nghiệm E-V", R.drawable.game1),
                new Adapter_Word.Item("Game trắc nghiệm V-E", R.drawable.game2),
                new Adapter_Word.Item("Game điền từ", R.drawable.game3),//no icon for this one
        };
        ListAdapter adapter = new ArrayAdapter<Adapter_Word.Item>(
                context,
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
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setTitle("Single Choice");
        builder.setAdapter(adapter, new DialogInterface.OnClickListener()  {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(context, Game.class);
                        switch (i) {
                            case 0:
                                intent.putExtra("type", 1);
                                break;
                            case 1:
                                intent.putExtra("type", 2);
                                break;
                            case 2:
                                intent = new Intent(context, Test.class);
                                break;
                            default:
                                Toast.makeText(context, "Error: Please select agian", Toast.LENGTH_SHORT).show();
                                break;

                        }
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("level", level);
                        context.startActivity(intent);
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).create()
                .show();

    }

    public void createMenuSearchWord(final Context context) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Nhập từ cần tra");
        final EditText input = new EditText(context);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (input.getText().toString().length() > 0) {
                            ArrayList<Word> listResult = new ArrayList<Word>();
                            listResult = db.SearchWord(input.getText().toString().trim().toUpperCase());
                            if (listResult.isEmpty()) {
                                new AlertDialog.Builder(context)
                                        .setTitle("No result")
                                        .setMessage("There is no result")
                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialogx, int which) {
                                                dialogx.dismiss();
                                            }
                                        })
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .show();


                                dialog.dismiss();
                            } else {

                                final AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
                                builderSingle.setIcon(android.R.drawable.ic_menu_help);
                                builderSingle.setTitle("Select One Word");
                                final ArrayList<Word> finalListResult1 = listResult;
                                final ArrayAdapter arrayAdapter = new ArrayAdapter(context, android.R.layout.simple_list_item_2,
                                        android.R.id.text1, finalListResult1) {
                                    @Override
                                    public View getView(int position, View convertView, ViewGroup parent) {
                                        View view = super.getView(position, convertView, parent);
                                        TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                                        TextView text2 = (TextView) view.findViewById(android.R.id.text2);

                                        text1.setText(finalListResult1.get(position).getWord_Title().toString());
                                        text2.setText(finalListResult1.get(position).getWord_Title_VN().toString());
                                        return view;
                                    }
                                };

                                builderSingle.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });

                                builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, final int which) {
                                        String strName = finalListResult1.get(which).getWord_Title().toString();
                                        String strMean = finalListResult1.get(which).getWord_Title_VN().toString();
                                        String mainTopic = "", Topics = "";
                                        if (finalListResult1.get(which).getExample().toString().length() > 0) {
                                            mainTopic = "Main topic: " + finalListResult1.get(which).getExample().toString();
                                            Topics = "Topic:" + finalListResult1.get(which).getExample_VN().toString();
                                            strMean = strMean + "\n" + mainTopic + "\n" + Topics;
                                        }

                                        AlertDialog.Builder builderInner = new AlertDialog.Builder(context);
                                        builderInner.setMessage(strMean);
                                        builderInner.setTitle(strName);
                                        builderInner.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                                builderSingle.show();
                                            }
                                        });
                                        builderInner.setPositiveButton("Go to topic", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                Word word = finalListResult1.get(which);
                                                Maintopic maintopic = new Maintopic(0,word.getExample(),null,0,0);
                                                Topic topic = new Topic(0,word.getTopic_Id(),word.getExample_VN(),null,0,0);
                                                SaveObject.currentMaintopic = maintopic;
                                                SaveObject.saveTopic=topic;
                                                Intent intent = new Intent(context, Word_Activity.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                                context.startActivity(intent);
                                            }
                                        });

                                        builderInner.show();
                                    }
                                });
                                builderSingle.show();
                                dialog.dismiss();
                            }


                        } else {
                            AlertDialog.Builder builderInner = new AlertDialog.Builder(context);
                            builderInner.setMessage("Error");
                            builderInner.setTitle("Please check your input");
                            builderInner.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            builderInner.show();
                        }
                    }
                }
        );
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();

    }

    public void createMenuAddItem(final Context context, final View rootView, final int itemType) {
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.layout_add_word, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);
        final EditText Maintopic_EN = (EditText) promptsView.findViewById(R.id.mainTopic_EN);
        final EditText Maintopic_VN = (EditText) promptsView.findViewById(R.id.mainTopic_VN);
        final EditText Type = (EditText) promptsView.findViewById(R.id.loaitu);
        if (itemType == CreateItemType.Maintopic || itemType == Topic) {
            {
                Type.setHeight(0);
                Type.setHint("");
                Type.setClickable(false);
            }
        }
        // set dialog message
        alertDialogBuilder.setView(promptsView).setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // bien kiem tra cho phep luu
                        if (Maintopic_EN.getText().toString().contentEquals("") || Maintopic_VN.getText().toString().isEmpty()
                                || Maintopic_VN.getText().toString().contentEquals("") || Maintopic_VN.getText().toString().isEmpty()) {
                            Toast.makeText(context, "Please check your input", Toast.LENGTH_LONG).show();
                            return;
                        }

                        if (itemType == CreateItemType.Maintopic || itemType == Topic) {
                            createSnackBar(context, rootView, Maintopic_EN.getText().toString().trim(),
                                    Maintopic_VN.getText().toString().trim(), null,itemType);
                            return;
                        }
                        //add word function
                        if (itemType == CreateItemType.Word) {
                            final String ITemEN = Maintopic_EN.getText().toString().trim();
                            final String ITemVN = Maintopic_VN.getText().toString().trim();
                            final List<String> suggestWord = Utility.CheckWord(ITemEN);
                            if (!suggestWord.contains(ITemEN) && suggestWord.size() > 0) {
                                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setIconAttribute(android.R.attr.alertDialogIcon)
                                        .setTitle("Which word do you mean ?")
                                        .setItems(suggestWord.toArray(new String[suggestWord.size()]),
                                                new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        final String wordEN = suggestWord.get(i);
                                                        createItem(context, wordEN, ITemVN.trim(),Type.getText().toString().trim()
                                                                , CreateItemType.Word);
                                                    }
                                                }).create().show();

                            } else {
                                if (suggestWord.contains(ITemEN)) {
                                    createSnackBar(context, rootView, ITemEN, Maintopic_VN.getText().toString().trim(),
                                            Type.getText().toString().trim() ,CreateItemType.Word);
                                } else {
                                    if (suggestWord.size() == 0) {
                                        String message = "This word may be misspelled. Are you sure to save ?";
                                        if (!((Word_Activity) context).isConnected())
                                            message = "There is no Internet connection. \n " + message;
                                        new AlertDialog.Builder(context)
                                                .setTitle("Confirm")
                                                .setMessage(message)
                                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        createSnackBar(context, rootView, ITemEN,
                                                                ITemVN,Type.getText().toString().trim(), CreateItemType.Word);
                                                    }
                                                })
                                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {

                                                    }
                                                })
                                                .setIcon(android.R.drawable.ic_dialog_alert)
                                                .show();
                                    }
                                }

                            }

                        }
                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        alertDialogBuilder.create().show();
    }

    public void createMenuImpot_Export(final Context context) {
      //  CharSequence[] array = {"Import database", "Export database"};
        final Adapter_Word.Item[] items = {
                new Adapter_Word.Item("Import data", R.drawable.import1),
                new Adapter_Word.Item("Export data", R.drawable.export),
        };
        ListAdapter adapter = new ArrayAdapter<Adapter_Word.Item>(
                context,
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

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setTitle("Single Choice");
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i) {
                            case 0:
                                FileDialog fileDialog = new FileDialog((Activity) context,
                                        Environment.getExternalStorageDirectory().getAbsolutePath());
                                fileDialog.setFileEndsWith("xls");
                                fileDialog.showDialog();

                                break;
                            case 1:
                                ExportDatabaseCSVTask task = new ExportDatabaseCSVTask();
                                task.execute();
                                break;

                            default:
                                Toast.makeText(context, "Error: Please select agian", Toast.LENGTH_SHORT).show();
                                break;

                        }
                    }
                }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).create()
                .show();

    }

    private void createItem(final Context context, final String ITemEN, final String ITemVN, @Nullable String wordType, int level) {

        if (level == CreateItemType.Maintopic) {
            boolean x = db.insertMaintopic(ITemEN,
                    ITemVN);

            adapterMaintopic = new Adapter_Maintopic(context, db.getListMainTopic());
            listView_Maintopic.setAdapter(adapterMaintopic);
            listView_Maintopic.invalidate();
            Toast.makeText(context, x ? "Thêm main topic thành công" : "Thêm thất bại", Toast.LENGTH_LONG).show();

        }
        if (level == Topic) {
            boolean x = db.insertTopic(ITemEN, ITemVN, SaveObject.currentMaintopic.getMaintopic_ID());
            adapter_topic = new Adapter_Topic(context, db.getListTopic(SaveObject.currentMaintopic));
            listView_Topic.setAdapter(adapter_topic);
            listView_Topic.invalidate();
            Toast.makeText(context, x ? "Thêm topic thành công" : "Thêm thất bại", Toast.LENGTH_LONG).show();

        }
        if (level == CreateItemType.Word) {
            boolean isExist = db.isExist(ITemEN, SaveObject.saveTopic.getTopic_Id());
            if (isExist) {
                Toast.makeText(context, "this word is exist", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean x = db.insertWord(SaveObject.saveTopic.getTopic_Id(), ITemEN, ITemVN,wordType);
            ArrayList<Word> words = db.getListWord(SaveObject.saveTopic);
            adapterWord = new Adapter_Word(context, words, (Activity) context);
            listView_Word.setAdapter(adapterWord);
            listView_Word.invalidate();
            Toast.makeText(context, x ? "Thêm từ vựng thành công" : "Thêm thất bại", Toast.LENGTH_LONG).show();
        }

    }

    private void createSnackBar(final Context context, View rootView, final String ITemEN, final String ITemVN, @Nullable final String WordType, final int level) {
        Snackbar.make(rootView, "Chọn UNDO để hủy thao tác", Snackbar.LENGTH_LONG)
                .setCallback(new Snackbar.Callback() {
                    @Override
                    public void onDismissed(Snackbar snackbar, int event) {
                        switch (event) {
                            case Snackbar.Callback.DISMISS_EVENT_ACTION:
                                Toast.makeText(context, "Hủy thao tác", Toast.LENGTH_LONG).show();
                                break;
                            case Snackbar.Callback.DISMISS_EVENT_TIMEOUT:
                                createItem(context, ITemEN, ITemVN,WordType, level);
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
}
