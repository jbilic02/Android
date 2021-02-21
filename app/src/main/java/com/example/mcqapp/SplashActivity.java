package com.example.mcqapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class SplashActivity extends AppCompatActivity {
    private TextView appName;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        appName = findViewById(R.id.app_name);
        Typeface typeface= ResourcesCompat.getFont(this, R.font.blacklist); //font 'blacklist' za ime app
        appName.setTypeface(typeface);

        Animation anim = AnimationUtils.loadAnimation(this,R.anim.animacija);
        appName.setAnimation(anim);

        mAuth = FirebaseAuth.getInstance(); //inicijalizacija

        // Access a Cloud Firestore instance from your Activity
        //DbQuery.g_firestore = FirebaseFirestore.getInstance();


        //thread ce nakon 3.5sek preko intenta pokrenuti mainactivity
        new Thread() {
            @Override
            public void run () {
                try {
                    sleep(3500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //provjera ako je korisnik vec logiran u app, prebaci ga u mainActivity
                if (mAuth.getCurrentUser() != null)
                {
                    //ako e uspjesno ucitanje kategorija tada se prebaci na mainActivity
                    Intent intent = new Intent(SplashActivity.this,MainActivity.class);
                    startActivity(intent);
                    SplashActivity.this.finish(); //close splashActivity

                }
                else //u suprotnom na login activity
                {
                    Intent intent = new Intent(SplashActivity.this,LoginActivity.class);
                    startActivity(intent); //prebaci na LoginActivity
                    SplashActivity.this.finish(); //close splashActivity
                }

            }
        }.start();
    }
}