package com.modyf.anime_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

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
    Integer b;
    List<String> Lines;
    public static int getStringIdentifier(Context context, String name){
        return context.getResources().getIdentifier(name, "array", context.getPackageName());
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
        mRecyclerView = findViewById(R.id.recycleview);
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
                String fire = animeList.get(position).getText3();
                Intent intent = new Intent(getBaseContext(), cda.class);
                intent.putExtra("link", fire);
                Log.d("kiedy", "teraz");
                startActivity(intent);
            }
        });

    }

}
