package com.modyf.anime_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private ExampleAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    ArrayList<ExampleItem> animeList;
    private int zdj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        animeList = new ArrayList<>();
        zdj=R.drawable.fmab;
        add("Fullmetal Alchemist", "Brotherhood", "fmab");
        zdj=R.drawable.btooom;
        add("Btooom!", getString(R.string.p), "btooom");
        zdj=R.drawable.campione;
        add("Campione!", getString(R.string.p), "campione");
        zdj=R.drawable.codebreaker;
        add("Code:Breaker", getString(R.string.p), "code_breaker");
        zdj=R.drawable.katanagatari;
        add("Katanagatari", getString(R.string.p), "katanagatari");
        zdj=R.drawable.oda_nobuna_no_yabou;
        add("Oda Nobuna no Yabou", getString(R.string.p), "oda_nobuna_no_yabou");
        zdj=R.drawable.robotic_notes;
        add("Robotics;Notes", getString(R.string.p), "robotics_notes");
        zdj=R.drawable.shinsekai_yori;
        add("Shinsekai Yori", getString(R.string.p), "shinsekai_yori");
        zdj=R.drawable.kanon_2006;
        add("Kanon", "2006", "kanon2006");
        zdj=R.drawable.kokoroconnect;
        add("Kokoro Connect", getString(R.string.p), "kokoro_connect");
        zdj=R.drawable.nana;
        add("Nana", getString(R.string.p), "nana");
        zdj=R.drawable.taritari;
        add("Tari Tari", getString(R.string.p), "tari_tari");
        zdj=R.drawable.tonari_no_kaibutsu_kun;
        add("Tonari no Kaibutsu-kun", getString(R.string.p), "tonari_no_kaibutsu_kun");
        zdj=R.drawable.clannad_after_story;
        add("Clannad", getString(R.string.p), "Clannad_1");
        add("Clannad After Story", getString(R.string.d), "Clannad_2");
        add("Clannad", getString(R.string.s), "Clannad_odcinki_specjalne");
        add("Clannad", getString(R.string.k), "Clannad_filmy_kinowe");
        zdj=R.drawable.codegeass;
        add("Code Geass", getString(R.string.p), "CodeGeass_1");
        add("Code Geass R2", getString(R.string.d), "CodeGeass_2");
        add("Code Geass", getString(R.string.s), "CodeGeass_odcinki_specjalne");
        add("Code Geass", getString(R.string.k), "CodeGeass_filmy_kinowe");
        add("Code Geass: Boukoku no Akito", getString(R.string.sb), "CodeGeass_boukoku_no_akito");
        zdj=R.drawable.danmachi;
        add("Danmachi", getString(R.string.p), "Danmachi_1");
        add("Danmachi II", getString(R.string.d), "Danmachi_2");
        add("Danmachi", getString(R.string.s), "Danmachi_odcinki_specjalne");
        add("Danmachi", getString(R.string.k), "Danmachi_filmy_kinowe");
        add("Danmachi: Sword Oratoria", getString(R.string.sb), "Danmachi_Sword_Oratoria");
        zdj=R.drawable.gintama;
        add("Gintama", getString(R.string.p), "Gintama_1");
        add("Gintama`", getString(R.string.d), "Gintama_2");
        add("Gintama°", getString(R.string.t), "Gintama_3");
        add("Gintama.", getString(R.string.c), "Gintama_4");
        add("Gintama. Porori Hen", getString(R.string.pi), "Gintama_5");
        add("Gintama.: Shirogane no Tamashii-hen", getString(R.string.sz), "Gintama_6");
        add("Gintama", getString(R.string.s), "Gintama_odcinki_specjalne");
        add("Gintama", getString(R.string.k), "Gintama_filmy_kinowe");
        zdj=R.drawable.hunterxhunter;
        add("HunterxHunter", "2011", "HunterxHunter_2011");
        add("HunterxHunter", getString(R.string.k), "HunterxHunter_filmy_kinowe");
        add("HunterxHunter", "1999", "HunterxHunter_1999");
        add("HunterxHunter", "OVA", "HunterxHunter_ova");
        add("HunterxHunter", "Greed Island", "HunterxHunter_Greed_Island");
        add("HunterxHunter", "Greed Island Final", "HunterxHunter_Greed_Island_Final");
        zdj=R.drawable.shinchou_yuusha;
        add("Shinchou Yuusha", getString(R.string.p), "Shinchou_Yuusha_1");
        zdj=R.drawable.assassinspride;
        add("Assassins Pride", getString(R.string.p), "ap_1");
        zdj=R.drawable.arifureta;
        add("Arifureta", getString(R.string.p), "arifureta_1");
        add("Arifureta", getString(R.string.s), "arifureta_odcinki_specjalne");
        zdj=R.drawable.blackclover;
        add("Black Clover", getString(R.string.p), "blackclover_1");
        add("Black Clover", getString(R.string.s), "blackclover_odcinki_specjalne");
        add("Mugyutto! Black Clover", "Seria poboczna", "blackclover_Mugyutto");
        add("Black Clover: Petit Clover Advance", "Seria poboczna", "blackclover_Petit_Clover_Advance");
        zdj=R.drawable.bleach;
        add("Bleach", getString(R.string.p), "bleach_1");
        add("Bleach", getString(R.string.k), "bleach_filmy_kinowe");
        add("Bleach", getString(R.string.s), "bleach_odcinki_specjalne");
        zdj=R.drawable.bnha;
        add("Boku no Hero Academia", getString(R.string.p), "bnha_1");
        add("Boku no Hero Academia", getString(R.string.d), "bnha_2");
        add("Boku no Hero Academia", getString(R.string.t), "bnha_3");
        add("Boku no Hero Academia", getString(R.string.c), "bnha_4");
        add("Boku no Hero Academia", getString(R.string.s), "bnha_odcinki_specjalne");
        zdj=R.drawable.naruto;
        add("Naruto", getString(R.string.p), "naruto");
        add("Naruto Shippuuden", getString(R.string.d), "naruto_shippuuden");
        add("Boruto", getString(R.string.t), "boruto");
        add("Naruto", getString(R.string.k), "naruto_filmy_kinowe");
        add("Naruto", getString(R.string.s), "naruto_odcinki_specjalne");
        add("Rock Lee i przyjaciele", "Seria poboczna", "rock_lee_i_przyjaciele");
        zdj=R.drawable.naruto;
        add("Darwin's Game", getString(R.string.p), "dg_1");
        zdj=R.drawable.infinite_dendrogram;
        add("Infinite Dendrogram", getString(R.string.p), "dendrogram_1");
        zdj=R.drawable.kyokousuiri;
        add("Kyokou Suiri", getString(R.string.p), "kyokousuiri_1");
        zdj=R.drawable.onepiece;
        add("One Piece", getString(R.string.p), "one_piece_1");
        add("One Piece", getString(R.string.k), "one_piece_filmy_kinowe");
        add("One Piece", "Odcinki specjalne nadawane w kinach", "one_piece_spec_kinowki");
        add("One Piece", "OVA", "one_piece_ova");
        add("One Piece", "Straw Hat Theater", "one_piece_teatrzyk");
        add("One Piece", "Special na telefony", "one_piece_telefon");
        add("One Piece", "Special na tv", "one_piece_tv");
        zdj=R.drawable.plunderer;
        add("Plunderer", getString(R.string.p), "plunderer_1");
        zdj=R.drawable.somali;
        add("Somali to Mori no Kamisama", getString(R.string.p), "somali_1");
        zdj=R.drawable.drstone;
        add("Dr. Stone", getString(R.string.p), "drstone_1");
        zdj=R.drawable.fairytail;
        add("Fairy Tail", getString(R.string.p), "fairy_tail_1");
        add("Fairy Tail", getString(R.string.d), "fairy_tail_2");
        add("Fairy Tail", getString(R.string.t), "fairy_tail_3");
        add("Fairy Tail", getString(R.string.k), "fairy_tail_filmy_kinowe");
        add("Fairy Tail", getString(R.string.s), "fairy_tail_odcinki_specjalne");
        add("Fairy Tail", getString(R.string.s), "fairy_tail_odcinki_specjalne");
        zdj=R.drawable.klk;
        add("Kill la Kill", getString(R.string.p), "klk_1");
        add("Kill la Kill", "OVA", "klk_ova");
        zdj=R.drawable.knm;
        add("Kenja no Mago", getString(R.string.p), "knm_1");
        zdj=R.drawable.lh;
        add("Log Horizon", getString(R.string.p), "lh_1");
        add("Log Horizon", getString(R.string.d), "lh_2");
        zdj=R.drawable.magi;
        add("Magi: The Labyrinth of Magic", getString(R.string.p), "magi_1");
        add("Magi: The Kingdom of Magic", getString(R.string.d), "magi_2");
        zdj=R.drawable.ngnl;
        add("No Game No Life", getString(R.string.p), "ngnl_1");
        add("No Game No Life", getString(R.string.s), "ngnl_odcinki_specjalne");
        add("No Game No Life", getString(R.string.k), "ngnl_filmy_kinowe");
        zdj=R.drawable.vs;
        add("Vinland Saga", getString(R.string.p), "vs_1");
        add("Vinland Saga", getString(R.string.d), "vs_2");
        zdj=R.drawable.snk;
        add("Shingeki no Kyojin", getString(R.string.p), "snk_1");
        add("Shingeki no Kyojin", getString(R.string.d), "snk_2");
        add("Shingeki no Kyojin", getString(R.string.t), "snk_3");
        add("Shingeki no Kyojin", "teatrzyk obrazkowy", "snk_teatrzyk");
        add("Shingeki no Kyojin", getString(R.string.k), "snk_filmy_kinowe");
        add("Shingeki! Kyojin Chuugakkou", "Seria poboczna", "snk_gimnazjum_tytanow");
        add("Shingeki no Kyojin: Kuinaki Sentaku", "Seria poboczna", "snk_bez_zalu");
        add("Shingeki no Kyojin", "OVA", "snk_ova");
        zdj=R.drawable.sng;
        add("Suisei no Gargantia", getString(R.string.p), "sng_1");
        add("Suisei no Gargantia", "OVA", "sng_ova");
        zdj=R.drawable.swordartonline;
        add("Sword Art Online", getString(R.string.p), "swordartonline_1");
        add("Sword Art Online", getString(R.string.d), "swordartonline_2");
        add("Sword Art Online", getString(R.string.t), "swordartonline_3");
        add("Sword Art Online Alternative: GGO", "Seria poboczna", "swordartonline_Alternative_GGO");
        add("Sword Art Online", getString(R.string.s), "swordartonline_wydanie_specjalne");
        add("Sword Art Online", getString(R.string.k), "swordartonline_filmy_kinowe");
        add("Sword Art Online: Sword Art Offline", "OVA", "swordartonline_offline_1");
        add("Sword Art Online: Sword Art Offline II", "OVA", "swordartonline_offline_2");
        add("Sword Art Online: Sword Art Offline wydanie specjalne", "OVA", "swordartonline_offline_wydanie_specjalne");
        add("Sword Art Online: Sword Art Offline Ordinal Scale", "OVA", "swordartonline_offline_os");
        zdj=R.drawable.tate;
        add("Tate no Yuusha no Nariagari", getString(R.string.p), "tate_1");
        zdj=R.drawable.tate;
        add("Tensei Shitara Slime Datta Ken", getString(R.string.p), "slime_1");
        add("Tensei Shitara Slime Datta Ken", getString(R.string.s), "slime_odcinki_specjalne");
        zdj=R.drawable.tate;
        add("Tensei Shitara Slime Datta Ken", getString(R.string.p), "slime_1");
        zdj=R.drawable.ynn;
        add("Yakusoku no Neverland", getString(R.string.p), "ynn_1");
        setupRecyclerView();
    }
    private void add(String tytul, String opis, String fire){
        animeList.add(new ExampleItem(zdj, tytul, opis, fire, true));
    }
    private void setupRecyclerView(){
        mRecyclerView = findViewById(R.id.recycleview);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                mLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        mAdapter =new ExampleAdapter(animeList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new ExampleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String fire = animeList.get(position).getText3();
                Intent intent = new Intent(getBaseContext(), seria.class);
                intent.putExtra("firebase", fire);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.example_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.actrion_search);
        searchItem.expandActionView();
        getSupportActionBar().setTitle("");
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setIconified(false);
        searchView.clearFocus();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

}