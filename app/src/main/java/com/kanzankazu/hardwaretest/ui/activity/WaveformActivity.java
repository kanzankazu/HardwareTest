
package com.kanzankazu.hardwaretest.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.Transaction;
import com.kanzankazu.hardwaretest.R;
import com.kanzankazu.hardwaretest.ui.adapter.AudioDataReceivedListener;
import com.kanzankazu.hardwaretest.ui.adapter.PlaybackListener;
import com.kanzankazu.hardwaretest.ui.adapter.PlaybackThread;
import com.kanzankazu.hardwaretest.ui.adapter.RecordingThread;
import com.kanzankazu.hardwaretest.util.ListArrayUtil;
import com.newventuresoftware.waveform.WaveformView;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;
import java.util.Arrays;

public class WaveformActivity extends AppCompatActivity {

    private WaveformView waveformViewRecordfvbi;
    private WaveformView waveformViewPlaybackfvbi;
    private RecordingThread mRecordingThread;
    private PlaybackThread mPlaybackThread;
    private static final int REQUEST_RECORD_AUDIO = 13;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waveform);

        initComponent();
        initContent();
        initListener();

        AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        if(audioManager.isWiredHeadsetOn()==true){
            Toast.makeText(this, "Lepaskan headset.. dan mulai test kembali", Toast.LENGTH_SHORT).show();
            finish();
        }

        waveformViewRecordfvbi = (WaveformView) findViewById(R.id.waveformViewRecord);
        waveformViewPlaybackfvbi = (WaveformView) findViewById(R.id.waveformViewPlayback);

        mRecordingThread = new RecordingThread(new AudioDataReceivedListener() {
            @Override
            public void onAudioDataReceived(short[] data) {
                waveformViewRecordfvbi.setSamples(data);
                Log.i("datanya adl", Arrays.toString(data));
                ListArrayUtil a= new ListArrayUtil();

                if(ListArrayUtil.isIntArrayContainInt2(data,40000)){
                    Log.i("datanya2"," ok");
                    mRecordingThread.stopRecording();
                    mPlaybackThread.stopPlayback();
                    setResult(Activity.RESULT_OK);
                    finish();
                }
            }
        });

        short[] samples = null;
        try {
            samples = getAudioSample();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.i("datanya adl",String.valueOf(samples));

        if (samples != null) {
            mPlaybackThread = new PlaybackThread(samples, new PlaybackListener() {
                @Override
                public void onProgress(int progress) {
                    waveformViewPlaybackfvbi.setMarkerPosition(progress);
                }

                @Override
                public void onCompletion() {
                    waveformViewPlaybackfvbi.setMarkerPosition(waveformViewPlaybackfvbi.getAudioLength());
                }
            });
            waveformViewPlaybackfvbi.setChannels(1);
            waveformViewPlaybackfvbi.setSampleRate(PlaybackThread.SAMPLE_RATE);
            waveformViewPlaybackfvbi.setSamples(samples);

            if (!mPlaybackThread.playing()) {
                mRecordingThread.stopRecording();
                startAudioRecordingSafe();
                mPlaybackThread.startPlayback();
            } else {
                mPlaybackThread.stopPlayback();
                mRecordingThread.stopRecording();
            }
        }
    }

    private void initComponent() {

    }

    private void initContent() {

    }

    private void initListener() {

    }

    private void startAudioRecordingSafe() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_GRANTED) {
            mRecordingThread.startRecording();
        } else {
            requestMicrophonePermission();

        }
    }

    private void requestMicrophonePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.RECORD_AUDIO)) {
            // Show dialog explaining why we need record audio
            Snackbar.make(waveformViewRecordfvbi, "Microphone access is required in order to record audio",
                    Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityCompat.requestPermissions(WaveformActivity.this, new String[]{android.Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_AUDIO);
                }
            }).show();
        } else {
            ActivityCompat.requestPermissions(WaveformActivity.this, new String[]{android.Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_AUDIO);
        }
    }

    private short[] getAudioSample() throws IOException {
        InputStream is = getResources().openRawResource(R.raw.audio);
        byte[] data;
        try {
            data = IOUtils.toByteArray(is);

        } finally {
            if (is != null) {
                is.close();
            }
        }

        ShortBuffer sb = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer();
        short[] samples = new short[sb.limit()];
//        Log.i("datanya adl",String.valueOf(samples));
        sb.get(samples);
        return samples;
    }

    @Override
    protected void onStop() {
        super.onStop();
        mRecordingThread.stopRecording();
        mPlaybackThread.stopPlayback();
    }

    /*@Override
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
        if (id == R.id.menukanza) {

        }

        return super.onOptionsItemSelected(item);
    }*/

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_RECORD_AUDIO && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            mRecordingThread.stopRecording();
        }
    }

    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_CANCELED);
        finish();
        overridePendingTransition(R.anim.masuk_dari_kiri_ke_kanan, R.anim.keluar_ke_kanan);
    }
}