package com.modyf.anime_app;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
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
    boolean watch=true;
    WebView strona;
    String link;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    private PlayerView playerView;
    private SimpleExoPlayer player;
    private DefaultDataSourceFactory defaultSourceFactory;
    boolean koniec=false;
    ProgressDialog progressDialog;
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


    private boolean checkIfAlreadyhavePermission() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int result2 = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED && result2 == result;
    }

    private void requestForSpecificPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 101);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 101:
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    requestForSpecificPermission();
                }
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void pobierz(){
        Log.d("gdzie jestem", "1");
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
        Log.d("gdzie jestem", "2");
        webView.setWebViewClient(new WebViewClient() {
            boolean zyje = true;
            @Override
            public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    if (url.contains("https://www.cda.pl/video/")) {
                        webView.loadUrl("javascript:(function() {" +
                                "window.location.href = document.getElementsByTagName('html')[0].innerHTML.substring(document.getElementsByTagName('html')[0].innerHTML.search(\"preload=\\\"none\\\"\") + 20, document.getElementsByTagName('html')[0].innerHTML.search(\"mp4\")+3);" +
                                "    })();");
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (zyje) {
                                    webView.reload();
                                }
                            }
                        }, 1000);
                    } else {
                        Log.d("cojest", url);
                        zyje=false;
                        webView.destroy();
                        showprogress(5);
                        new DownloadFile().execute(url);
                    }
            }
        });
    }
    public void showprogress(int a){
        progressDialog.setProgress(a);
        progressDialog.show();
    }
    private void przygotujfilm(String url){
        if (!seria) {
            final ProgressiveMediaSource extractorMediaSource =
                    new ProgressiveMediaSource.Factory(defaultSourceFactory).createMediaSource(Uri.parse(url));
            player.prepare(extractorMediaSource);
            LinearLayout znikaj=findViewById(R.id.znikaj);
            znikaj.setVisibility(View.GONE);
            final Long time = sharedPref.getLong(link, 0);
            player.seekTo(time);
            czygra = true;
            playerView.setVisibility(View.VISIBLE);
            player.setPlayWhenReady(true);
            playerView.setBackgroundColor(Color.BLACK);
            Log.d("stronawebview", "destroy1");
            player.addListener(new ExoPlayer.EventListener() {
                @Override
                public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                    if (playbackState == ExoPlayer.STATE_IDLE) {
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
                        koniec = false;
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
                        koniec = true;
                    }
                }
            });

        }
    }
    @SuppressLint("SourceLockedOrientationActivity")
    private void przelacz(){
        seria=!seria;
        final LinearLayout znikaj=findViewById(R.id.znikaj);
        if(!seria) {
            czygra = false;
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getSupportActionBar().hide();
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            znikaj.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
            player = new SimpleExoPlayer.Builder(this).build();
            playerView.setPlayer(player);
            defaultSourceFactory = new DefaultDataSourceFactory(this,
                    Util.getUserAgent(this, "anime_app"));
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/anime_app/" + link + ".mp4";
            final File film = new File(path);
            if (sharedPref.getBoolean(link + "czypobrane", false) && film.exists()) {
                przygotujfilm(path);
            }
            else {
                final ConstraintLayout Layout = findViewById(R.id.parent);
                final WebView webView = new WebView(this);
                strona = webView;
                webView.setVisibility(View.GONE);
                Layout.addView(webView);
                String newUA = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.117 Safari/537.36";
                webView.setWebViewClient(new WebViewClient());
                webView.getSettings().setJavaScriptEnabled(true);
                webView.getSettings().setLoadsImagesAutomatically(false);
                webView.getSettings().setUserAgentString(newUA);
                webView.loadUrl("https://www.cda.pl/video/" + link);
                webView.setWebViewClient(new WebViewClient() {
                    boolean zyje = true;
                    @Override
                    public void onPageFinished(WebView view, String url) {
                        if (!seria) {
                            super.onPageFinished(view, url);
                            if (url.contains("https://www.cda.pl/video/")) {
                                Log.d("cojest", "cda");
                                webView.loadUrl("javascript:(function() {" +
                                        "window.location.href = document.getElementsByTagName('html')[0].innerHTML.substring(document.getElementsByTagName('html')[0].innerHTML.search(\"preload=\\\"none\\\"\") + 20, document.getElementsByTagName('html')[0].innerHTML.search(\"mp4\")+3);" +
                                        "    })();");
                                final Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (zyje) {
                                            webView.reload();
                                        }
                                    }
                                }, 1000);
                            } else {
                                Log.d("cojest", "inna");
                                webView.setVisibility(View.GONE);
                                webView.destroy();
                                zyje = false;
                                przygotujfilm(url);
                            }
                        }
                    }
                });
            }
        }
        else{
            this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getSupportActionBar().show();
            playerView.setVisibility(View.GONE);
            playerView.setPlayer(null);
            player.release();
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
            mRecyclerView.setVisibility(View.VISIBLE);
            znikaj.setVisibility(View.GONE);
        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        animeList = new ArrayList<>();
        setContentView(R.layout.activity_seria);
        if (!checkIfAlreadyhavePermission()) {
            requestForSpecificPermission();
        }
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
                if(watch){
                przelacz();}
                else{
                    String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/anime_app/"+link+".mp4";
                    final File film = new File(path);
                    if(!sharedPref.getBoolean(link+"czypobrane", false) || !film.exists()) {
                        showprogress(0);
                        pobierz();
                    }
                    else{
                        AlertDialog.Builder alertDialogBuilder=
                                new AlertDialog.Builder(seria.this)
                                .setMessage("Plik już pobrany. Usunąć?")
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                            if(film.exists()) {
                                                film.delete();
                                            }
                                            editor.putBoolean(link + "czypobrane", false);
                                            editor.commit();
                                            }
                                        })
                                        .setNegativeButton("Nie", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                    }
                                                }
                                        );
                        alertDialogBuilder.show();
                    }
                }
            }
        });

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.pasek, menu);
        final MenuItem item = menu.findItem(R.id.zdjnapasku);
        MenuItem.OnMenuItemClickListener click = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(watch) {
                    item.setIcon(R.drawable.ic_download);
                    progressDialog = new ProgressDialog(seria.this);
                    progressDialog.setMessage("Pobieranie");
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    progressDialog.setMax(100);
                }
                else{
                    item.setIcon(R.drawable.ic_watch);
                }
                watch=!watch;
                return false;
            }
        };
        item.setOnMenuItemClickListener(click);
        return true;
    }

    protected void blad() {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/anime_app/" + link + ".mp4";
        File film = new File(path);
        if (film.exists()) {
            film.delete();
        }
        showprogress(0);
        progressDialog.dismiss();
        AlertDialog.Builder alertDialogBuilder =
                new AlertDialog.Builder(seria.this)
                        .setMessage("Pobieranie przerwane. Spróbować jeszcze raz?")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                showprogress(0);
                                pobierz();
                            }
                        })
                        .setNegativeButton("Nie", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                }
                        );
        alertDialogBuilder.show();
    }

private class DownloadFile extends AsyncTask<String, String, String> {
    boolean bladwystapil=false;
    @Override
    protected String doInBackground(String... furl) {
        int count;
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/anime_app/";
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdir();
        }
        try {
            URL url = new URL(furl[0]);
            Log.d("cojest", String.valueOf(url));
            URLConnection connection = url.openConnection();
            connection.connect();
            int lenghtOfFile = connection.getContentLength();
            Log.d("cojest", String.valueOf(lenghtOfFile));
            InputStream input = new BufferedInputStream(url.openStream(), 8192);
            OutputStream output = new FileOutputStream(path + link+".mp4");
            byte[] data = new byte[1024];
            long total = 0;
            while ((count = input.read(data)) != -1) {
                total += count;
                publishProgress("" + (int) Math.floor((total * 95) / lenghtOfFile));
                output.write(data, 0, count);
            }
            output.flush();
            output.close();
            input.close();
            editor.putBoolean(link + "czypobrane", true);
            editor.commit();
            bladwystapil=false;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.d("bladpobierania", String.valueOf(e));
            bladwystapil=true;
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("bladpobierania", String.valueOf(e));
            bladwystapil=true;
        }
        return null;
    }

    public void onProgressUpdate(String... progress) {
        showprogress(Integer.parseInt(progress[0]) + 5);

    }

    protected void onPostExecute(String file_url) {
        if(bladwystapil){
            blad();
        }
        else {
            showprogress(0);
            progressDialog.dismiss();
            AlertDialog.Builder alertDialogBuilder =
                    new AlertDialog.Builder(seria.this)
                            .setMessage("Pobieranie ukończone")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
            alertDialogBuilder.show();
        }
    }
}
}
