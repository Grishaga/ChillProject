package sound.chill.com.mychilloutplayer;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.view.View;
public class AudioPlayerCreate {

    static MediaPlayer mp;
    private static boolean isplayingAudio=false;

    static void prepereMediaPlayer(Context c, String idradioLink){
        mp = new MediaPlayer();
        try {
            mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mp.setDataSource(idradioLink);
            mp.prepareAsync();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void mpReset(){
        mp.reset();
    }

    static void startAudio(){
        if(!mp.isPlaying())
        {
            isplayingAudio=true;
            mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    MainActivity.progressBar.setVisibility(View.INVISIBLE);
                    MainActivity.fab.setImageResource(R.drawable.ic_pause_white);
                    if (PlayerActivity.playerPreppared){
                    PlayerActivity.progressBar.setVisibility(View.INVISIBLE);
                        isplayingAudio=true;
                    }
                    mp.start();
                }
            });
        }
    }


    public static void stopAudio() {
        isplayingAudio = false;
        mp.stop();
    }
}
