package com.example.mcqapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.security.KeyStore;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private FrameLayout main_frame;

    //navigacija po mainActivityu
    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    //prebacivanje prema odabranoj kategoriji- setFragment
                    switch (item.getItemId())
                    {
                        case R.id.nav_home:
                            setFragment(new CategoryFragment());
                            return true;
                        case R.id.nav_leaderboard:
                            setFragment(new LeaderBoardFragment());
                            return true;
                        case R.id.nav_account:
                            setFragment(new AccountFragment());
                            return true;
                    }
                    return false;
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //inicijalizacija
        bottomNavigationView = findViewById(R.id.bottom_nav_bar);
        main_frame  = findViewById(R.id.main_frame);
        //listener za odredene aktivnosti koje kliknemo (home,leaderboard,account)
        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);

        setFragment(new CategoryFragment());
    }


    private void setFragment(Fragment fragment)
    {
        //minjanje fragmenti kad biramo na bottom navigation
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(main_frame.getId(),fragment); //postavlja se fragment na transakciju
        transaction.commit();
    }


}