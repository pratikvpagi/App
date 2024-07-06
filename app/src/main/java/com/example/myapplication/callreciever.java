package com.example.myapplication;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.telephony.TelephonyManager;

public class callreciever extends BroadcastReceiver {
    private static int incomingCallCount = 0;
    private MediaPlayer mediaPlayer;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null && intent.getAction().equals(TelephonyManager.ACTION_PHONE_STATE_CHANGED)) {
            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
            if (state != null && state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                // Check if phone is in silent mode
                AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                if (audioManager != null && audioManager.getRingerMode() == AudioManager.RINGER_MODE_SILENT) {
                    incomingCallCount++;
                    if (incomingCallCount == 3) {
                        // Trigger alert (beep sound and notification)
                        playAlertSound(context);

                        incomingCallCount = 0; // Reset count after alert
                    }
                }
            }
        }
    }

    private void playAlertSound(Context context) {
        // Release any previously allocated MediaPlayer resources
        releaseMediaPlayer();

        // Initialize MediaPlayer with beep sound file
        mediaPlayer = MediaPlayer.create(context, R.raw.beep);

        // Set looping to false to play sound once
        mediaPlayer.setLooping(false);

        // Start playing the beep sound
        mediaPlayer.start();

        // Release MediaPlayer resources after sound completes
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                releaseMediaPlayer();
            }
        });
    }

    private void releaseMediaPlayer() {
        // Release MediaPlayer resources
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }


}
