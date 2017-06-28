package com.example.rinnv.tieuluancnpm.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
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
import android.widget.GridView;

import com.example.rinnv.tieuluancnpm.Adapter.Adapter_Maintopic;
import com.example.rinnv.tieuluancnpm.Adapter.Adapter_Remember;
import com.example.rinnv.tieuluancnpm.DatabaseUtility.SQLiteDataController;
import com.example.rinnv.tieuluancnpm.Entity.Maintopic;
import com.example.rinnv.tieuluancnpm.FrameWork.CreateItemType;
import com.example.rinnv.tieuluancnpm.FrameWork.PracticeType;
import com.example.rinnv.tieuluancnpm.FrameWork.SaveObject;
import com.example.rinnv.tieuluancnpm.LockScreen.LockScreenService;
import com.example.rinnv.tieuluancnpm.MiniFragment.MenuPracticeFragment;
import com.example.rinnv.tieuluancnpm.R;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static com.example.rinnv.tieuluancnpm.FrameWork.SaveObject.mTts;

public class MainActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    public static SQLiteDataController db;
    final Context context = this;
    public static View rootView;
    public static GridView listView_Maintopic, listView_Remember;
    public static Adapter_Maintopic adapterMaintopic;
    public static Adapter_Remember adapterRemember;

    private static final int SPEECH_API_CHECK = 0;

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
    protected void onResume() {
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.invalidate();
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SaveObject.rootContext = getApplicationContext();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().hide();


        db = SQLiteDataController.GetSQLController();


        CheckTTS();


        Intent intent = new Intent(MainActivity.this, LockScreenService.class);
        startService(intent);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        SaveObject.remindWord = db.getListRemindWord();


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
                    return "Learned words";

            }
            return null;
        }
    }

    public static class PlaceholderFragment extends Fragment {
        private static final String ARG_SECTION_NUMBER = "section_number";

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
                        SaveObject.currentMaintopic = maintopic;
                        Intent intent = new Intent(getContext(), Topic_Activity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                        startActivity(intent);
                    }
                });

                listView_Maintopic.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                        final Maintopic maintopic = (Maintopic) listView_Maintopic.getItemAtPosition(position);
                        new AlertDialog.Builder(getContext()).setIcon(android.R.drawable.ic_dialog_alert)
                                .setTitle("Xóa Main topic").setMessage("Bạn có chắc muốn xóa chủ đề chính này không?")
                                .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        db.deleteMaintopic(maintopic);
                                        adapterMaintopic = new Adapter_Maintopic(getContext(), db.getListMainTopic());
                                        listView_Maintopic.setAdapter(adapterMaintopic);
                                        listView_Maintopic.invalidate();
                                    }

                                })
                                .setNegativeButton("Hủy", null).show();

                        return true;
                    }
                });
                final FloatingActionMenu materialDesignFAM;
                final com.github.clans.fab.FloatingActionButton floatingActionButton1,
                        floatingActionButton2, floatingActionButton3, floatingActionButton4,
                        floatingActionButton5, floatingActionButton6, floatingActionButton7;


                materialDesignFAM = (FloatingActionMenu) rootView.findViewById(R.id.material_design_android_floating_action_menu);
                floatingActionButton1 = (com.github.clans.fab.FloatingActionButton) rootView.findViewById(R.id.action1);
                floatingActionButton4 = (com.github.clans.fab.FloatingActionButton) rootView.findViewById(R.id.action4);
                floatingActionButton5 = (com.github.clans.fab.FloatingActionButton) rootView.findViewById(R.id.action5);
                floatingActionButton6 = (FloatingActionButton) rootView.findViewById(R.id.action6);
                floatingActionButton4.setVisibility(View.VISIBLE);
                floatingActionButton6.setVisibility(View.VISIBLE);

                //nut lam practice
                floatingActionButton1.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        materialDesignFAM.close(false);
                        new MenuPracticeFragment().createMenuPractice(container.getContext(), PracticeType.allMainTopic);
                    }
                });
                floatingActionButton5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        materialDesignFAM.close(false);
                        new MenuPracticeFragment().createMenuSearchWord(container.getContext());

                    }
                });

                floatingActionButton4.setLabelText("Add Maintopic");
                floatingActionButton4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        materialDesignFAM.close(false);
                        new MenuPracticeFragment().createMenuAddItem(container.getContext(), rootView, CreateItemType.Maintopic);

                    }
                });


                floatingActionButton6.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        materialDesignFAM.close(false);
                        new MenuPracticeFragment().createMenuImpot_Export(container.getContext());

                    }
                });
            } else {
                if (tab == 2) {

                    listView_Remember = (GridView) rootView.findViewById(R.id.list_item);
                    adapterRemember = new Adapter_Remember(getContext(), db.getLisCheckedtWord());
                    listView_Remember.setAdapter(adapterRemember);

                    final FloatingActionMenu materialDesignFAM;
                    com.github.clans.fab.FloatingActionButton floatingActionButton1;
                    com.github.clans.fab.FloatingActionButton floatingActionButton5;


                    materialDesignFAM = (FloatingActionMenu) rootView.findViewById(R.id.material_design_android_floating_action_menu);
                    floatingActionButton1 = (com.github.clans.fab.FloatingActionButton) rootView.findViewById(R.id.action1);
                    floatingActionButton5 = (com.github.clans.fab.FloatingActionButton) rootView.findViewById(R.id.action5);

                    floatingActionButton1.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            materialDesignFAM.close(false);
                            new MenuPracticeFragment().createMenuPractice(container.getContext(), PracticeType.onlyRemindWord);

                        }
                    });
                    floatingActionButton5.setOnClickListener(new View.OnClickListener() {
                        public void onClick(final View v) {
                            materialDesignFAM.close(false);
                            new MenuPracticeFragment().createMenuSearchWord(container.getContext());

                        }
                    });
                }
            }

            return rootView;
        }
    }
}
