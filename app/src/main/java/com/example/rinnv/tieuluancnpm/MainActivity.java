package com.example.rinnv.tieuluancnpm;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionMenu;

import java.io.IOException;
import java.util.Locale;

import static com.example.rinnv.tieuluancnpm.SaveObject.mTts;

public class MainActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private static SQLiteDataController db;
    final Context context = this;
    static View rootView;
    private static GridView listView_Maintopic;
    private static Adapter_Maintopic adapterMaintopic;

    private static final int SPEECH_API_CHECK = 0;
    public String TAG = "Tag";


    public void CheckTTS() {

        Intent checkIntent = new Intent();
        checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkIntent, SPEECH_API_CHECK);
    }

    @Override
    protected void onActivityResult(
            int requestCode, int resultCode, Intent data) {


        if (requestCode == SPEECH_API_CHECK) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                // success, create the TTS instance
                mTts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int i) {
                        if (i == TextToSpeech.SUCCESS) {

                            int result = mTts.setLanguage(Locale.getDefault());


                            if (result == TextToSpeech.LANG_MISSING_DATA
                                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                                Log.e("TTS", "This Language is not supported");
                            } else {


                            }
                        } else {
                            // Initialization failed.
                            Log.e("app", "Could not initialize TextToSpeech.");
                        }
                    }
                });
            } else {
                // missing data, install it
                Intent installIntent = new Intent();
                installIntent.setAction(
                        TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installIntent);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().hide();


        // test
        createDB();
        db = new SQLiteDataController(this);

        CheckTTS();



        Intent intent = new Intent(MainActivity.this,LockScreenService.class);
        startService(intent);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        SaveObject.remindWord =db.getListRemindWord();

        
        /*final android.support.design.widget.FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if (tabLayout.getSelectedTabPosition()==0) {
                    // get prompts.xml view
                    LayoutInflater li = LayoutInflater.from(context);
                    View promptsView = li.inflate(R.layout.layout_add_maintopic, null);

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            context);
                    final EditText Maintopic_EN = (EditText) promptsView.findViewById(R.id.mainTopic_EN);
                    final EditText Maintopic_VN = (EditText) promptsView.findViewById(R.id.mainTopic_VN);
                    // set dialog message
                    alertDialogBuilder
                            .setView(promptsView)
                            .setCancelable(false)
                            .setPositiveButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            // bien kiem tra cho phep luu
                                            Snackbar.make(view, "Tap to undo this add", Snackbar.LENGTH_LONG)
                                                    .setCallback(new Snackbar.Callback() {
                                                        @Override
                                                        public void onDismissed(Snackbar snackbar, int event) {
                                                            switch (event) {
                                                                case Snackbar.Callback.DISMISS_EVENT_ACTION:
                                                                    Toast.makeText(context, "Undo Complete", Toast.LENGTH_LONG).show();
                                                                    break;
                                                                case Snackbar.Callback.DISMISS_EVENT_TIMEOUT:

                                                                    boolean x = db.insertMaintopic(Maintopic_EN.getText().toString().trim(),
                                                                            Maintopic_VN.getText().toString().trim());

                                                                    adapterMaintopic = new Adapter_Maintopic(context, db.getListMainTopic());
                                                                    listView_Maintopic.setAdapter(adapterMaintopic);
                                                                    listView_Maintopic.invalidate();

                                                                    Toast.makeText(context, x ? "Add Main topic Successfull" : "Fail to do this", Toast.LENGTH_LONG).show();
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
                                    })
                            .setNegativeButton("Cancel",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });

                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    // show it
                    alertDialog.show();
                }else  if (tabLayout.getSelectedTabPosition()==1) {
                    Intent intent = new Intent(MainActivity.this, Game.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("type", 1);
                    intent.putExtra("level", "rememberWord");
                    startActivity(intent);
                }

            }
        });
*/
    }

    private void createDB() {
        SQLiteDataController sql = new SQLiteDataController(this);
        try {
            sql.isCreatedDatabase();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Main Topic";
                case 1:
                    return "Learned word";

            }
            return null;
        }
    }

    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                                 Bundle savedInstanceState) {
            rootView = inflater.inflate(R.layout.fragment_main, container, false);
            int tab = getArguments().getInt(ARG_SECTION_NUMBER);
            if (tab == 1) {
                final Context context = getContext();
                listView_Maintopic = (GridView) rootView.findViewById(R.id.list_item);
                adapterMaintopic = new Adapter_Maintopic(getContext(), db.getListMainTopic());
                listView_Maintopic.setAdapter(adapterMaintopic);
                listView_Maintopic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        Maintopic maintopic = (Maintopic) listView_Maintopic.getItemAtPosition(position);
                        SaveObject.saveMaintopic = maintopic;

                        Intent intent = new Intent(getContext(), Topic_Activity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                        startActivity(intent);
                    }
                });

                listView_Maintopic.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                        final Maintopic maintopic = (Maintopic) listView_Maintopic.getItemAtPosition(position);

                        new AlertDialog.Builder(getContext())
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setTitle("Delete Main topic")
                                .setMessage("Are you sure you want to delete this Topic?")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        db.deleteMaintopic(maintopic);
                                        adapterMaintopic = new Adapter_Maintopic(getContext(), db.getListMainTopic());
                                        listView_Maintopic.setAdapter(adapterMaintopic);
                                        listView_Maintopic.invalidate();

                                    }

                                })
                                .setNegativeButton("No", null)
                                .show();

                        return true;
                    }
                });
                final   FloatingActionMenu materialDesignFAM;
                final com.github.clans.fab.FloatingActionButton floatingActionButton1, floatingActionButton2, floatingActionButton3, floatingActionButton4;


                materialDesignFAM = (FloatingActionMenu) rootView.findViewById(R.id.material_design_android_floating_action_menu);
                floatingActionButton1 = (com.github.clans.fab.FloatingActionButton) rootView.findViewById(R.id.action1);
                floatingActionButton2 = (com.github.clans.fab.FloatingActionButton) rootView.findViewById(R.id.action2);
                floatingActionButton3 = (com.github.clans.fab.FloatingActionButton) rootView.findViewById(R.id.action3);
                floatingActionButton4 = (com.github.clans.fab.FloatingActionButton) rootView.findViewById(R.id.action4);
                   floatingActionButton4.setVisibility(View.VISIBLE);

                floatingActionButton1.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        //TODO something when floating action menu first item clicked
                        Intent intent = new Intent(context, Game.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("type", 1);
                        intent.putExtra("level", "all");
                        startActivity(intent);
                        materialDesignFAM.close(false);
                    }
                });
                floatingActionButton2.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        //TODO something when floating action menu second item clicked

                        Intent intent = new Intent(context, Game.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("type", 2);
                        intent.putExtra("level", "all");
                        startActivity(intent);
                        materialDesignFAM.close(false);

                    }
                });
                floatingActionButton3.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        //TODO something when floating action menu third item clicked

                        Intent intent = new Intent(context, Test.class);
                        intent.putExtra("level", "all");
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        materialDesignFAM.close(false);
                    }
                });



                floatingActionButton4.setLabelText("Them Main Topic");
                floatingActionButton4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LayoutInflater li = LayoutInflater.from(context);
                        View promptsView = li.inflate(R.layout.layout_add_maintopic, null);

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                context);

                        final EditText Maintopic_EN = (EditText) promptsView.findViewById(R.id.mainTopic_EN);
                        final EditText Maintopic_VN = (EditText) promptsView.findViewById(R.id.mainTopic_VN);
                        // set dialog message
                        alertDialogBuilder
                                .setView(promptsView)
                                .setCancelable(false)
                                .setPositiveButton("OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                // bien kiem tra cho phep luu
                                                Snackbar.make(rootView, "Tap to undo this add", Snackbar.LENGTH_LONG)
                                                        .setCallback(new Snackbar.Callback() {
                                                            @Override
                                                            public void onDismissed(Snackbar snackbar, int event) {
                                                                switch (event) {
                                                                    case Snackbar.Callback.DISMISS_EVENT_ACTION:
                                                                        Toast.makeText(context, "Undo Complete", Toast.LENGTH_LONG).show();
                                                                        break;
                                                                    case Snackbar.Callback.DISMISS_EVENT_TIMEOUT:

                                                                        boolean x = db.insertMaintopic(Maintopic_EN.getText().toString().trim(),
                                                                                Maintopic_VN.getText().toString().trim());

                                                                        adapterMaintopic = new Adapter_Maintopic(context, db.getListMainTopic());
                                                                        listView_Maintopic.setAdapter(adapterMaintopic);
                                                                        listView_Maintopic.invalidate();

                                                                        Toast.makeText(context, x ? "Add Main topic Successfull" : "Fail to do this", Toast.LENGTH_LONG).show();
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
                                        })
                                .setNegativeButton("Cancel",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        });

                        // create alert dialog
                        AlertDialog alertDialog = alertDialogBuilder.create();

                        // show it
                        alertDialog.show();
                    }
                });



            } else {
                if (tab == 2) {
                    final GridView listView_Maintopic = (GridView) rootView.findViewById(R.id.list_item);
                    Adapter_Remember adapterRemember = new Adapter_Remember(getContext(), db.getLisCheckedtWord());
                    listView_Maintopic.setAdapter(adapterRemember);
                    listView_Maintopic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            Word word = (Word) listView_Maintopic.getItemAtPosition(position);
                            mTts.speak(word.getWord_Title().trim(), TextToSpeech.QUEUE_FLUSH, null);

                        }
                    });

                   final FloatingActionMenu materialDesignFAM;
                    com.github.clans.fab.FloatingActionButton floatingActionButton1, floatingActionButton2, floatingActionButton3, floatingActionButton4;


                    materialDesignFAM = (FloatingActionMenu) rootView.findViewById(R.id.material_design_android_floating_action_menu);
                    floatingActionButton1 = (com.github.clans.fab.FloatingActionButton) rootView.findViewById(R.id.action1);
                    floatingActionButton2 = (com.github.clans.fab.FloatingActionButton) rootView.findViewById(R.id.action2);
                    floatingActionButton3 = (com.github.clans.fab.FloatingActionButton) rootView.findViewById(R.id.action3);
                    floatingActionButton4 = (com.github.clans.fab.FloatingActionButton) rootView.findViewById(R.id.action4);


                    final Context context = getContext();
                    floatingActionButton1.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            //TODO something when floating action menu first item clicked
                            Intent intent = new Intent(context, Game.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra("type", 1);
                            intent.putExtra("level", "rememberWord");
                            startActivity(intent);
                            materialDesignFAM.close(false);
                        }
                    });
                    floatingActionButton2.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            //TODO something when floating action menu second item clicked

                            Intent intent = new Intent(context, Game.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra("type", 2);
                            intent.putExtra("level", "rememberWord");
                            startActivity(intent);
                            materialDesignFAM.close(false);

                        }
                    });
                    floatingActionButton3.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            //TODO something when floating action menu third item clicked

                            Intent intent = new Intent(context, Test.class);
                            intent.putExtra("level", "rememberWord");
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            materialDesignFAM.close(false);
                        }
                    });


                }
            }

            return rootView;
        }
    }
}
