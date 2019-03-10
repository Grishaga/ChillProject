package sound.chill.com.mychilloutplayer;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import sound.chill.com.mychilloutplayer.billing.SkuActivity;

import static sound.chill.com.mychilloutplayer.AudioPlayerCreate.mp;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    protected SharedPreferences sharedPreferences;
    protected SharedPreferences.Editor editor;
    private static final String PREFS = "prefs";
    public static final String SAVED_POSITION = "position";
    public static final String IMAGE_SEND_INTENT = "send intent image index";
    protected AudioPlayerCreate audioPlayerCreate;
    protected RecyclerView mRecyclerView;
    protected RadioListAdapter mAdapter;
    private static ArrayList<DataModel> data;
    private int linkIndex;
    private TextView playTextView;
    private String[] radioLincs;
    private ImageView playImageView;
    public static ProgressBar progressBar;
    public boolean mPprepeared;
    private HeadPhoneReceiver mHeadPhoneReceiver = new HeadPhoneReceiver();
    public static FloatingActionButton fab;
    private int[] imagrecourses = new int[]{R.drawable.lofi, R.drawable.ambient, R.drawable.terrace, R.drawable.acoustic, R.drawable.chill_out,
            R.drawable.enigmatik, R.drawable.dimension, R.drawable.ambient_sleeping_pill, R.drawable.sleep_radio, R.drawable.lounge_dream, R.drawable.buddha_bar,
            R.drawable.nirvana_meditation, R.drawable.relaxing_positive, R.drawable.erotica, R.drawable.echoes_of_bluemars, R.drawable.deep_space_one, R.drawable.seasaltradio,
            R.drawable.classic_radio_nature, R.drawable.nrj_relax, R.drawable.sixteen_bit, R.drawable.chocolate};
    protected Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        radioLincs = new String[]{getResources().getString(R.string.lofi), getResources().getString(R.string.ambient), getResources().getString(R.string.terrace),
                getResources().getString(R.string.acoustic), getResources().getString(R.string.chill_out), getResources().getString(R.string.enigmatic_station),
                getResources().getString(R.string.dimensione_relax), getResources().getString(R.string.ambient_sleeping_pill), getResources().getString(R.string.sleep_radio),
                getResources().getString(R.string.a_lounge_dream), getResources().getString(R.string.radio_buddha_bar), getResources().getString(R.string.nirvana_meditation),
                getResources().getString(R.string.relaxing_positive), getResources().getString(R.string.erotica), getResources().getString(R.string.echoes_of_bluemars),
                getResources().getString(R.string.deep_space_one), getResources().getString(R.string.seasaltradio), getResources().getString(R.string.classikradio_nature),
                getResources().getString(R.string.nrj_relax), getResources().getString(R.string.sixteen_bit), getResources().getString(R.string.chocholat)};
        playImageView = findViewById(R.id.playImage);
        fab = findViewById(R.id.fab);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        playTextView = findViewById(R.id.playView);
        progressBar = findViewById(R.id.progressBar);

        //Instantiating the MediaPlayer class
        String[] names;
        names = getResources().getStringArray(R.array.radio_station_names);
        data = new ArrayList<>();
        for (int i = 0; i < imagrecourses.length; i++) {
            data.add(new DataModel(names[i], imagrecourses[i]
            ));
        }

        // Get a handle to the RecyclerView.
        mRecyclerView = findViewById(R.id.recyclerview);
        sharedPreferences = getSharedPreferences(PREFS, Activity.MODE_PRIVATE);
        linkIndex = sharedPreferences.getInt(SAVED_POSITION, 0);
        fab.setImageResource(R.drawable.ic_play_white);
        audioPlayerCreate = new AudioPlayerCreate();
        playTextView.setText(data.get(linkIndex).getName());
        playImageView.setImageResource(data.get(linkIndex).getImage());
        AudioPlayerCreate.prepereMediaPlayer(context, radioLincs[linkIndex]);
        fab.setOnClickListener(this);

        if (savedInstanceState!=null){
            linkIndex = savedInstanceState.getInt(SAVED_POSITION);
            progressBar.setVisibility(View.VISIBLE);
            AudioPlayerCreate.startAudio();
            fab.setImageResource(R.drawable.ic_pause_white);
        }
        mAdapter =new RadioListAdapter(this,data, new RadioListAdapter.CustomItemClickListener() {
        @Override
        public void onItemClick (View v,int position){
            linkIndex = position;
            if (isNetworkConnected()) {
                AudioPlayerCreate.mpReset();
                AudioPlayerCreate.prepereMediaPlayer(context, radioLincs[linkIndex]);
                progressBar.setVisibility(View.VISIBLE);

                if (mp.isPlaying()) {
                    AudioPlayerCreate.mpReset();
                    AudioPlayerCreate.prepereMediaPlayer(context, radioLincs[linkIndex]);
                    progressBar.setVisibility(View.VISIBLE);
                    AudioPlayerCreate.startAudio();
                    playTextView.setText(data.get(linkIndex).getName());
                    playImageView.setImageResource(data.get(linkIndex).getImage());
                }
                AudioPlayerCreate.startAudio();
                playTextView.setText(data.get(linkIndex).getName());
                playImageView.setImageResource(data.get(linkIndex).getImage());
            } else
                Toast.makeText(MainActivity.this, R.string.no_internet_conection, Toast.LENGTH_SHORT).show();
        }
    });
        // Connect the adapter with the RecyclerView.
        mRecyclerView.setAdapter(mAdapter);
// Give the RecyclerView a default layout manager.
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
// Animate PlayActivity
        LinearLayout constraintLayout = findViewById(R.id.linearLayout);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();
//register filter for headset plugged
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_HEADSET_PLUG);
        this.registerReceiver(mHeadPhoneReceiver, filter);
    }


    @Override
    public void onClick(View view) {
        if (mp.isPlaying()){
            mp.pause();
            mPprepeared = true;
            fab.setImageResource(R.drawable.ic_play_white);
        } else if (mPprepeared){
            mp.start();
            fab.setImageResource(R.drawable.ic_pause_white);
        } else if (isNetworkConnected()){
            progressBar.setVisibility(View.VISIBLE);
            AudioPlayerCreate.mpReset();
            AudioPlayerCreate.prepereMediaPlayer(context, radioLincs[linkIndex]);
            AudioPlayerCreate.startAudio();
            fab.setImageResource(R.drawable.ic_pause_white);
        }else Toast.makeText(MainActivity.this, R.string.no_internet_conection, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SAVED_POSITION, linkIndex);
    }

    //internet availability check
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        return cm.getActiveNetworkInfo() != null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

         if (id == R.id.nav_rate) {
             rateApp();
         } else if (id == R.id.nav_share) {
            shareApp();
        } else if (id == R.id.nav_send) {
            sendingEmail();
        }else if (id == R.id.nav_donate) {
            Intent intent = new Intent(this, SkuActivity.class);
            startActivity(intent);
         }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void startPlayActivity(View view) {
        startRevealActivity(view);
        Intent intent = new Intent();
        intent.putExtra(IMAGE_SEND_INTENT, linkIndex);
    }

    //activity animation
    private void startRevealActivity(View v) {
        //calculates the center of the View v you are passing
        int revealX = (int) (mRecyclerView.getX() + mRecyclerView.getWidth()/2);
        int revealY = (int) (mRecyclerView.getY() + mRecyclerView.getHeight())+100;

        //create an intent, that launches the second activity and pass the x and y coordinates
        Intent intent = new Intent(this, PlayerActivity.class);
        intent.putExtra(RevealAnimation.EXTRA_CIRCULAR_REVEAL_X, revealX);
        intent.putExtra(RevealAnimation.EXTRA_CIRCULAR_REVEAL_Y, revealY);

        //just start the activity as an shared transition, but set the options bundle to null
        ActivityCompat.startActivity(this, intent, null);

        //to prevent strange behaviours override the pending transitions
        overridePendingTransition(0, 0);
    }

    //headphone connection tracking
    private class HeadPhoneReceiver extends BroadcastReceiver {
        private boolean headsetConnected = false;
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.hasExtra("state")){
                if (headsetConnected && intent.getIntExtra("state", 0) == 0){
                    headsetConnected = false;
                    if (mp.isPlaying()){
                        mp.pause();
                        mPprepeared = true;
                        fab.setImageResource(R.drawable.ic_play_white);
                    }
                } else if (!headsetConnected && intent.getIntExtra("state", 0) == 1){
                    headsetConnected = true;
                }
            }
        }
    }

    public void sendingEmail() {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", "grisha.gorbaba@gmail.com", null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
        emailIntent.putExtra(Intent.EXTRA_TEXT, R.string.Hi);
        if (emailIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(Intent.createChooser(emailIntent, getString(R.string.a_wish)));
        }
    }

    public void rateApp(){
        final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    public void shareApp(){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=" + getPackageName());
        startActivity(Intent.createChooser(intent, getString(R.string.share_app)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mp.isPlaying()){
            fab.setImageResource(R.drawable.ic_pause_white);
        }else {
            fab.setImageResource(R.drawable.ic_play_white);
        }
        linkIndex = sharedPreferences.getInt(SAVED_POSITION, 0);
        playTextView.setText(data.get(linkIndex).getName());
        playImageView.setImageResource(data.get(linkIndex).getImage());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        this.unregisterReceiver(mHeadPhoneReceiver);
        super.onDestroy();
        mp.release();
    }

    @Override
    protected void onPause() {
        super.onPause();
        sharedPreferences = getSharedPreferences(PREFS, MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putInt(SAVED_POSITION, linkIndex);
        editor.apply();
    }
}
