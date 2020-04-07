package com.modyf.anime_app;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.audio.AudioAttributes;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;


public class cda extends AppCompatActivity {
    String link;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    private PlayerView playerView;
    private SimpleExoPlayer player;
    private DefaultDataSourceFactory defaultSourceFactory;
    boolean koniec=false;
    private void zapisz(){
        Long x=player.getCurrentPosition();
        editor.putLong(link, x);
        Log.d("czas", String.valueOf(x));
        editor.commit();
    }

    @Override
    public void onPause() {
        super.onPause();
        if(koniec) {
            zapisz();
            player.setPlayWhenReady(false);
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        //sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        //final Long time = sharedPref.getLong(link, 0);
        //player.seekTo(time);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_cda);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        link = getIntent().getStringExtra("link");
        sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        final Long time = sharedPref.getLong(link, 0);
        final OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                zapisz();
                playerView.setPlayer(null);
                player.release();
                finish();
            }
        };
        this.getOnBackPressedDispatcher().addCallback(this, callback);
        final WebView webView = findViewById(R.id.webView);
        String newUA= "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.117 Safari/537.36";
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadsImagesAutomatically(false);
        webView.getSettings().setUserAgentString(newUA);
        webView.loadUrl("https://www.cda.pl/video/"+link);
        playerView = findViewById(R.id.player_view);
        player = new SimpleExoPlayer.Builder(this).build();
        playerView.setPlayer(player);
        defaultSourceFactory = new DefaultDataSourceFactory(this,
                Util.getUserAgent(this, "anime_app"));
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url){
                super.onPageFinished(view, url);
                if(url.contains("https://www.cda.pl/video/")) {
                    Log.d("cojest", "cda");
                    webView.loadUrl("javascript:(function() {" +
                            "window.location.href = document.getElementsByTagName('html')[0].innerHTML.substring(document.getElementsByTagName('html')[0].innerHTML.search(\"preload=\\\"none\\\"\") + 20, document.getElementsByTagName('html')[0].innerHTML.search(\"mp4\")+3);" +
                            "    })();");
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            webView.reload();
                        }
                    }, 1000);
                }
                else{
                    Log.d("cojest", "inna");
                    ProgressiveMediaSource extractorMediaSource =
                            new ProgressiveMediaSource.Factory(defaultSourceFactory).createMediaSource(Uri.parse(url));
                    player.prepare(extractorMediaSource);
                    webView.setVisibility(View.GONE);
                    LinearLayout znikaj = findViewById(R.id.znikaj);
                    znikaj.setVisibility(View.GONE);
                    playerView.setVisibility(View.VISIBLE);
                    player.setPlayWhenReady(true);
                    player.seekTo(time);
                    playerView.setBackgroundColor(Color.BLACK);
                    webView.destroy();
                    player.addListener(new ExoPlayer.EventListener() {
                        @Override
                        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                            if (playbackState == ExoPlayer.STATE_IDLE || playbackState == ExoPlayer.STATE_ENDED ||
                                    !playWhenReady) {

                                playerView.setKeepScreenOn(false);
                            } else { // STATE_IDLE, STATE_ENDED
                                // This prevents the screen from getting dim/lock
                                playerView.setKeepScreenOn(true);
                            }
                            if (player.getPlaybackState() == ExoPlayer.STATE_ENDED) {
                                koniec=false;
                                editor.putLong(link, 0);
                                Log.d("czas", "koniec");
                                editor.commit();
                                playerView.setPlayer(null);
                                player.release();
                                finish();
                            }
                            if (player.getPlaybackState() == ExoPlayer.STATE_READY) {
                                AudioAttributes audioAttributes = new AudioAttributes.Builder()
                                        .setUsage(C.USAGE_MEDIA)
                                        .setContentType(C.CONTENT_TYPE_MOVIE)
                                        .build();
                                player.setAudioAttributes(audioAttributes, /* handleAudioFocus= */ true);
                                koniec=true;
                            }
                        }

                    });
                }
            }
        });
    }
}
