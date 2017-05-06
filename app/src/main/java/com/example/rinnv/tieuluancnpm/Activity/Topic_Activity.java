package com.example.rinnv.tieuluancnpm.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.rinnv.tieuluancnpm.Adapter.Adapter_Topic;
import com.example.rinnv.tieuluancnpm.DatabaseUtility.SQLiteDataController;
import com.example.rinnv.tieuluancnpm.Entity.Maintopic;
import com.example.rinnv.tieuluancnpm.Entity.Topic;
import com.example.rinnv.tieuluancnpm.FrameWork.CreateItemType;
import com.example.rinnv.tieuluancnpm.FrameWork.PracticeType;
import com.example.rinnv.tieuluancnpm.FrameWork.SaveObject;
import com.example.rinnv.tieuluancnpm.MiniFragment.MenuPracticeFragment;
import com.example.rinnv.tieuluancnpm.R;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

public class Topic_Activity extends AppCompatActivity {

    FloatingActionMenu materialDesignFAM;
    FloatingActionButton floatingActionButton1;
    FloatingActionButton floatingActionButton4;
    final Context context = this;

    public static Adapter_Topic adapter_topic;
    public static GridView listView_Topic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Maintopic Maintopic_choosen = SaveObject.currentMaintopic;
        setTitle(Maintopic_choosen.getMaintopic_Tittle() + " - " + Maintopic_choosen.getMaintopic_Tittle_VN());


        final SQLiteDataController db = new SQLiteDataController(this);
        listView_Topic = (GridView) findViewById(R.id.list_item);
        adapter_topic = new Adapter_Topic(this, db.getListTopic(Maintopic_choosen));
        listView_Topic.setAdapter(adapter_topic);

        listView_Topic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SaveObject.saveTopic = (Topic) listView_Topic.getItemAtPosition(position);
                Intent intent = new Intent(Topic_Activity.this, Word_Activity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });

        listView_Topic.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                final Topic topic = (Topic) listView_Topic.getItemAtPosition(position);
                new AlertDialog.Builder(context)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Xóa topic")
                        .setMessage("Bạn có chắc muốn xóa chủ đề này không?")
                        .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                db.deleteTopic(topic);
                                adapter_topic = new Adapter_Topic(context, db.getListTopic(Maintopic_choosen));
                                listView_Topic.setAdapter(adapter_topic);
                                listView_Topic.invalidate();

                            }

                        })
                        .setNegativeButton("Hủy", null)
                        .show();


                return true;
            }
        });
        materialDesignFAM = (FloatingActionMenu) findViewById(R.id.material_design_android_floating_action_menu);
        floatingActionButton1 = (FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item1);
        floatingActionButton4 = (FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item4);


        floatingActionButton1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                materialDesignFAM.close(false);
                new MenuPracticeFragment().createMenuPractice(Topic_Activity.this, PracticeType.oneMaintopic);
            }
        });


        floatingActionButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                materialDesignFAM.close(false);
                new MenuPracticeFragment().createMenuAddItem(Topic_Activity.this,findViewById(android.R.id.content) ,CreateItemType.Topic);
            }
        });


    }

}
