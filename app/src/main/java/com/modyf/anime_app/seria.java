package com.modyf.anime_app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
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
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.audio.AudioAttributes;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class seria extends AppCompatActivity {
    String TAG="wazne";
    ArrayList map;
    private RecyclerView mRecyclerView;
    private ExampleAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    ArrayList<ExampleItem> animeList;
    boolean seria=true;
    List<String> Lines;

    WebView strona;
    String link;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    private PlayerView playerView;
    private SimpleExoPlayer player;
    private DefaultDataSourceFactory defaultSourceFactory;
    boolean koniec=false;
    boolean czygra=false;
    private void zapisz(){
        if(czygra) {
            Long x = player.getCurrentPosition();
            editor.putLong(link, x);
            Log.d("czas", String.valueOf(x));
            editor.commit();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(koniec && !seria) {
            zapisz();
            player.setPlayWhenReady(false);
        }
    }


    public static int getStringIdentifier(Context context, String name){
        return context.getResources().getIdentifier(name, "array", context.getPackageName());
    }
    @SuppressLint("SourceLockedOrientationActivity")
    private void przelacz(){
        seria=!seria;
        if(!seria){
            czygra=false;
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getSupportActionBar().hide();
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            final LinearLayout znikaj=findViewById(R.id.znikaj);
            znikaj.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
            final ConstraintLayout Layout = findViewById(R.id.parent);
            final WebView webView = new WebView(this);
            strona=webView;
            webView.setVisibility(View.GONE);
            Layout.addView(webView);
            String newUA= "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.117 Safari/537.36";
            webView.setWebViewClient(new WebViewClient());
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setLoadsImagesAutomatically(false);
            webView.getSettings().setUserAgentString(newUA);
            webView.loadUrl("https://www.cda.pl/video/"+link);
            player = new SimpleExoPlayer.Builder(this).build();
            playerView.setPlayer(player);
            defaultSourceFactory = new DefaultDataSourceFactory(this,
                    Util.getUserAgent(this, "anime_app"));
            webView.setWebViewClient(new WebViewClient(){
                boolean zyje=true;
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
                                if(zyje){
                                    webView.reload();
                                }
                            }
                        }, 1000);
                    }
                    else{
                        Log.d("cojest", "inna");
                        final ProgressiveMediaSource extractorMediaSource =
                                new ProgressiveMediaSource.Factory(defaultSourceFactory).createMediaSource(Uri.parse(url));
                        player.prepare(extractorMediaSource);
                        webView.setVisibility(View.GONE);
                        znikaj.setVisibility(View.GONE);
                        final Long time = sharedPref.getLong(link, 0);
                        player.seekTo(time);
                        czygra=true;
                        playerView.setVisibility(View.VISIBLE);
                        player.setPlayWhenReady(true);
                        playerView.setBackgroundColor(Color.BLACK);
                        webView.destroy();
                        zyje=false;
                        Log.d("stronawebview", "destroy1");
                        player.addListener(new ExoPlayer.EventListener() {
                            @Override
                            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                                if(playbackState == ExoPlayer.STATE_IDLE){
                                    zapisz();
                                    final Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            player.prepare(extractorMediaSource);
                                            final Long time = sharedPref.getLong(link, 0);
                                            player.seekTo(time);
                                        }
                                    }, 3000);
                                }
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
                                    przelacz();
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
        else{
            this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getSupportActionBar().show();
            playerView.setVisibility(View.GONE);
            playerView.setPlayer(null);
            player.release();
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
            mRecyclerView.setVisibility(View.VISIBLE);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        animeList = new ArrayList<>();
        setContentView(R.layout.activity_seria);
        String fire = getIntent().getStringExtra("firebase");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(fire);
        Lines= Arrays.asList(getResources().getStringArray(getStringIdentifier(this, fire)));
        mRecyclerView = findViewById(R.id.recycleview);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                map = (ArrayList)dataSnapshot.getValue();
                add();
                setupRecyclerView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value.", databaseError.toException());

            }
        });
        sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        playerView = findViewById(R.id.player_view);
        final OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                if(seria) {
                    finish();
                }
                else{
                    zapisz();
                    przelacz();
                }
            }
        };
        this.getOnBackPressedDispatcher().addCallback(this, callback);
    }
    private void add(){
        for(int i=0; i<Lines.size(); i=i+2) {
            animeList.add(new ExampleItem(R.drawable.ic_search_black_24dp, Lines.get(i), "", Lines.get(i+1), false));
        }
        if(map != null && !map.isEmpty()) {
            for (int i = 0; i < map.size(); i = i + 2) {
                animeList.add(new ExampleItem(R.drawable.ic_search_black_24dp, (String) map.get(i), "", (String) map.get(i + 1), false));
            }
        }
    }
    private void setupRecyclerView(){
        LinearLayout znikaj = findViewById(R.id.znikaj);
        znikaj.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
        mLayoutManager = new LinearLayoutManager(this);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                mLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        mRecyclerView.setHasFixedSize(true);
        mAdapter =new ExampleAdapter(animeList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new ExampleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                link = animeList.get(position).getText3();
                przelacz();
            }
        });

    }

}
