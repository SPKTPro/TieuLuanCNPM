package com.example.rinnv.tieuluancnpm;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import com.github.clans.fab.FloatingActionMenu;
import com.github.clans.fab.FloatingActionButton;

public class Topic_Activity extends AppCompatActivity {

    FloatingActionMenu materialDesignFAM;
    FloatingActionButton floatingActionButton1, floatingActionButton2, floatingActionButton3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Maintopic Maintopic_choosen = SaveObject.saveMaintopic;
        setTitle(Maintopic_choosen.getMaintopic_Tittle() +" - " +Maintopic_choosen.getMaintopic_Tittle_VN());


        SQLiteDataController db = new SQLiteDataController(this);
        final GridView listView_Topic = (GridView) findViewById(R.id.list_item);
        Adapter_Topic adapter_topic = new Adapter_Topic(this,db.getListTopic(Maintopic_choosen));
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



        materialDesignFAM = (FloatingActionMenu) findViewById(R.id.material_design_android_floating_action_menu);
        floatingActionButton1 = (FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item1);
        floatingActionButton2 = (FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item2);
        floatingActionButton3 = (FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item3);

        floatingActionButton1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //TODO something when floating action menu first item clicked
                Intent intent = new Intent(Topic_Activity.this, Game.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("type",1);
                intent.putExtra("level","maintopic");
                startActivity(intent);
                materialDesignFAM.close(false);
            }
        });
        floatingActionButton2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //TODO something when floating action menu second item clicked

                Intent intent = new Intent(Topic_Activity.this, Game.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("type",2);
                intent.putExtra("level","maintopic");
                startActivity(intent);
                materialDesignFAM.close(false);

            }
        });
        floatingActionButton3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //TODO something when floating action menu third item clicked

                Intent intent = new Intent(Topic_Activity.this, Test.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });



    }

}
