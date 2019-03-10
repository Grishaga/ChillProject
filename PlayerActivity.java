package sound.chill.com.mychilloutplayer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static sound.chill.com.mychilloutplayer.AudioPlayerCreate.mp;

public class PlayerActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String PREFS = "prefs";
    private static final String SAVED_POSITION = "position";
    SharedPreferences sharedPreferences;
    RevealAnimation mRevealAnimation;
    FloatingActionButton floatingActionButton;
    ImageButton previousButton, playPause, nextButton;
    ImageView imageView;
    AudioPlayerCreate audioPlayerCreate;
    Context context;
    TextView textView;
    private int linkIndex;
    public boolean mPprepeared;
    private String[] radioLincs;
    private int[] imagrecourses = new int[]{R.drawable.lofi, R.drawable.ambient, R.drawable.terrace, R.drawable.acoustic, R.drawable.chill_out,
            R.drawable.enigmatik, R.drawable.dimension, R.drawable.ambient_sleeping_pill, R.drawable.sleep_radio, R.drawable.lounge_dream, R.drawable.buddha_bar,
            R.drawable.nirvana_meditation, R.drawable.relaxing_positive, R.drawable.erotica, R.drawable.echoes_of_bluemars, R.drawable.deep_space_one, R.drawable.seasaltradio,
            R.drawable.classic_radio_nature, R.drawable.nrj_relax, R.drawable.sixteen_bit, R.drawable.chocolate};
    Intent intent;
    SharedPreferences.Editor editor;
    private static ArrayList<DataModel> data;
    public static ProgressBar progressBar;
    public static boolean playerPreppared = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        data = new ArrayList<>();
        String[] names;
        names = getResources().getStringArray(R.array.radio_station_names);
        for (int i = 0; i < imagrecourses.length; i++) {
            data.add(new DataModel(names[i], imagrecourses[i]
            ));
        }
        playerPreppared = true;
        progressBar = findViewById(R.id.playScreenProgresBar);
        radioLincs = new String[]{getResources().getString(R.string.lofi), getResources().getString(R.string.ambient), getResources().getString(R.string.terrace),
                getResources().getString(R.string.acoustic), getResources().getString(R.string.chill_out), getResources().getString(R.string.enigmatic_station),
                getResources().getString(R.string.dimensione_relax), getResources().getString(R.string.ambient_sleeping_pill), getResources().getString(R.string.sleep_radio),
                getResources().getString(R.string.a_lounge_dream), getResources().getString(R.string.radio_buddha_bar), getResources().getString(R.string.nirvana_meditation),
                getResources().getString(R.string.relaxing_positive), getResources().getString(R.string.erotica), getResources().getString(R.string.echoes_of_bluemars),
                getResources().getString(R.string.deep_space_one), getResources().getString(R.string.seasaltradio), getResources().getString(R.string.classikradio_nature),
                getResources().getString(R.string.nrj_relax), getResources().getString(R.string.sixteen_bit), getResources().getString(R.string.chocholat)};

        intent = getIntent();
        sharedPreferences = getSharedPreferences(PREFS, Activity.MODE_PRIVATE);
        linkIndex = sharedPreferences.getInt(SAVED_POSITION, 0);
        imageView = findViewById(R.id.playScreenImage);
        imageView.setImageResource(imagrecourses[linkIndex]);
        textView = findViewById(R.id.textPlayScreen);
        textView.setText(data.get(linkIndex).getName());
        floatingActionButton = findViewById(R.id.floatingActionButtonList);
        floatingActionButton.setOnClickListener(this);
        previousButton = findViewById(R.id.setPreviousStationButton);
        previousButton.setOnClickListener(this);
        playPause = findViewById(R.id.setPlayPausButton);
        playPause.setOnClickListener(this);
        nextButton = findViewById(R.id.setNextStationButton);
        nextButton.setOnClickListener(this);
        LinearLayout rootLayout = findViewById(R.id.activity_player); //there you have to get the root layout of your second activity
        mRevealAnimation = new RevealAnimation(rootLayout, intent, this);
//        restore saved state
        if (savedInstanceState!=null){
            linkIndex = savedInstanceState.getInt(SAVED_POSITION);
            progressBar.setVisibility(View.VISIBLE);
            AudioPlayerCreate.startAudio();
            playPause.setImageResource(R.drawable.ic_pause_dark_blue);
        }

        if (AudioPlayerCreate.mp.isPlaying()) {
            playPause.setImageResource(R.drawable.ic_pause_dark_blue);
        }
    }

    @Override
    public void onBackPressed() {
        mRevealAnimation.unRevealActivity();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SAVED_POSITION, linkIndex);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.floatingActionButtonList: {
                mRevealAnimation.unRevealActivity();
            }break;
            case R.id.setPreviousStationButton: {
                if (isNetworkConnected()) {
                    linkIndex--;
                    if (linkIndex == -1){
                        linkIndex = radioLincs.length-1;
                    }
                    AudioPlayerCreate.mpReset();
                    AudioPlayerCreate.prepereMediaPlayer(context, radioLincs[linkIndex]);
                    progressBar.setVisibility(View.VISIBLE);
                    if (mp.isPlaying()) {
                        AudioPlayerCreate.mpReset();
                        AudioPlayerCreate.prepereMediaPlayer(context, radioLincs[linkIndex]);
                        progressBar.setVisibility(View.VISIBLE);
                        AudioPlayerCreate.startAudio();
                        playPause.setImageResource(R.drawable.ic_pause_dark_blue);
                        textView.setText(data.get(linkIndex).getName());
                        imageView.setImageResource(data.get(linkIndex).getImage());
                    }
                    AudioPlayerCreate.startAudio();
                    textView.setText(data.get(linkIndex).getName());
                    imageView.setImageResource(data.get(linkIndex).getImage());
                    playPause.setImageResource(R.drawable.ic_pause_dark_blue);
                } else
                    Toast.makeText(PlayerActivity.this, R.string.no_internet_conection, Toast.LENGTH_SHORT).show();
            }break;
            case R.id.setPlayPausButton: {
                if (AudioPlayerCreate.mp.isPlaying()){
                    AudioPlayerCreate.mp.pause();
                    mPprepeared = true;
                    playPause.setImageResource(R.drawable.ic_play_dark_blue);
                }else if (mPprepeared){
                    AudioPlayerCreate.mp.start();
                    playPause.setImageResource(R.drawable.ic_pause_dark_blue);
                }else {
                    progressBar.setVisibility(View.VISIBLE);
                    AudioPlayerCreate.prepereMediaPlayer(context, radioLincs[linkIndex]);
                    AudioPlayerCreate.startAudio();
                    playPause.setImageResource(R.drawable.ic_pause_dark_blue);
                }
            }break;
            case R.id.setNextStationButton: {
                if (isNetworkConnected()) {
                    linkIndex++;
                    if (linkIndex == radioLincs.length){
                        linkIndex = 0;
                    }
                    AudioPlayerCreate.mpReset();
                    AudioPlayerCreate.prepereMediaPlayer(context, radioLincs[linkIndex]);
                    progressBar.setVisibility(View.VISIBLE);
                    if (mp.isPlaying()) {
                        AudioPlayerCreate.mpReset();
                        AudioPlayerCreate.prepereMediaPlayer(context, radioLincs[linkIndex]);
                        progressBar.setVisibility(View.VISIBLE);
                        AudioPlayerCreate.startAudio();
                        playPause.setImageResource(R.drawable.ic_pause_dark_blue);
                        textView.setText(data.get(linkIndex).getName());
                        imageView.setImageResource(data.get(linkIndex).getImage());
                    }
                    AudioPlayerCreate.startAudio();
                    textView.setText(data.get(linkIndex).getName());
                    imageView.setImageResource(data.get(linkIndex).getImage());
                    playPause.setImageResource(R.drawable.ic_pause_dark_blue);
                } else
                    Toast.makeText(PlayerActivity.this, R.string.no_internet_conection, Toast.LENGTH_SHORT).show();
            }break;
        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        return cm.getActiveNetworkInfo() != null;
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
