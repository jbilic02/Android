package com.example.mcqapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.skydoves.medal.MedalAnimation;
import com.skydoves.medal.MedalDirection;

import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;

public class Done extends AppCompatActivity {
    Button btnTryAgain;
    TextView txtResultScore, getTxtResultQuestion;
    ProgressBar progressBar;


    FirebaseDatabase database;
    DatabaseReference quesiton_score;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_done);

        //firebase je baza podataka u stvarnom vremenu te ne podrzava uvjet višestrukog upita
        database = FirebaseDatabase.getInstance();
        quesiton_score = database.getReference("Question_Score"); //spremanje scorea u tu listu

        txtResultScore = findViewById(R.id.txtTotalScore);
        getTxtResultQuestion = findViewById(R.id.txtTotalQuestioin);
        progressBar = findViewById(R.id.doneProgressBar);

        btnTryAgain = findViewById(R.id.btnTryAgain);



        btnTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Done.this, Start.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                Done.this.finish();
            }

        });
        //dohvati podatke od bundle te ih postavi za prikaz, sluzi za prijenos podataka izmedu aktivnosti
        Bundle extra = getIntent().getExtras(); //vrijednosti se preslikavaju u kljuceve niza koji se koriste za dohvacanje vrijednosti
        if (extra != null) {
            int score = extra.getInt("SCORE");
            int totalQuestion = extra.getInt("TOTAL");
            int correctAnswer = extra.getInt("CORRECT");

            txtResultScore.setText(String.format("SCORE : %d", score));
            getTxtResultQuestion.setText(String.format("CORRECT : %d/%d", correctAnswer, totalQuestion));


            //dodavanje medalje ako su sva pitanja tocna
            MedalAnimation medalAnimation = new MedalAnimation.Builder()
                    .setDirection(MedalDirection.RIGHT)
                    .setDegreeX(360)
                    .setDegreeZ(360)
                    .setSpeed(3200)
                    .setTurn(4)
                    .setLoop(10)
                    .build();
            ImageView imageView = findViewById(R.id.my_medal5);
            imageView.setVisibility(View.INVISIBLE);

            if(correctAnswer == totalQuestion)
            {
                imageView.startAnimation(medalAnimation);
                imageView.setVisibility(View.VISIBLE);
            }
            else
            {
                //imageView.setVisibility(View.INVISIBLE);
                imageView.clearAnimation();

            }

            progressBar.setMax(totalQuestion);
            progressBar.setProgress(correctAnswer);
            progressBar.getProgressDrawable().setColorFilter(
                    Color.WHITE, android.graphics.PorterDuff.Mode.SRC_IN);

            ////uploadanje u bazu podataka
            //            quesiton_score.child(String.format("%s_%s", Common.currentUser.getUserName(), Common.categoryId)).setValue(
            //                    new QuestionScore(String.format("%s_%s",Common.currentUser.getUserName(),
            //                            Common.categoryId),
            //                            Common.currentUser.getUserName(),
            //                            String.valueOf(score)));
        }


    }
}