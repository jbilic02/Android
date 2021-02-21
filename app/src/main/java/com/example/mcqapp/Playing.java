package com.example.mcqapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mcqapp.Common.Common;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.rpc.context.AttributeContext;
import com.muddzdev.styleabletoast.StyleableToast;
import com.skydoves.medal.MedalAnimation;
import com.skydoves.medal.MedalDirection;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;

public class Playing extends AppCompatActivity implements View.OnClickListener{
    final static long INTERVAL = 1000; //sek
    final static long TIMEOUT = 11000; //stavia san 11 jer ako je 10 kaska sekundu
    int progressValue = 0;
    CountDownTimer mCountDown;
    int index = 0, score = 0, thisQuestion = 0, totalQuestion, correctAnwer;



    ProgressBar progressBar;
    ImageView question_image;
    Button btnA, btnB, btnC, btnD;
    TextView txtScore, txtQuestionNum, question_text;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing);


        //Views, inicijalizacija
        txtScore = (TextView)findViewById(R.id.txtScore);
        txtQuestionNum = (TextView)findViewById(R.id.txtTotalQuestioin);
        question_text = (TextView)findViewById(R.id.question_text);
        question_image = (ImageView)findViewById(R.id.question_image);

        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        progressBar.getProgressDrawable().setColorFilter(
                Color.WHITE, android.graphics.PorterDuff.Mode.SRC_IN);


        btnA = (Button)findViewById(R.id.btnAnswerA);
        btnB = (Button)findViewById(R.id.btnAnswerB);
        btnC = (Button)findViewById(R.id.btnAnswerC);
        btnD = (Button)findViewById(R.id.btnAnswerD);


        btnA.setOnClickListener(this);
        btnB.setOnClickListener(this);
        btnC.setOnClickListener(this);
        btnD.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {
        mCountDown.cancel();
        //pitanja u listi
        if (index < totalQuestion) {
            Button clickedButton = (Button) v;
            //dohvat pitanja po kategoriji u listu
            if (clickedButton.getText().equals(Common.questionList.get(index).getCorrectAnswer())) {
                score += 10; //na tocan odg dodaje 10
                correctAnwer++;
                showQuestion(++index); //sljed pitanje ako je ono tocno
                //clickedButton.setBackgroundResource(R.color.justGreen);

                //Toast toast = Toast.makeText(Playing.this,"Točno", Toast.LENGTH_SHORT);
                //toast.getView().setBackgroundColor(Color.GREEN);
                //toast.setGravity(Gravity.TOP|Gravity.CENTER, 0, 850);
                //toast.show();

            } else {
                //krivi odgovor
                score -= 10;
                showQuestion(++index); //ajde dalje na pitanja
                //clickedButton.setBackgroundResource(R.color.justRed);


                //Toast toast = Toast.makeText(Playing.this,"Krivo", Toast.LENGTH_SHORT);
                //toast.getView().setBackgroundColor(Color.RED);
                //toast.setGravity(Gravity.TOP|Gravity.CENTER, 0, 850);
                //toast.show();

            }

            txtScore.setText(String.format("%d", score)); //prikazi score tijekom igre

        }
    }
    private void showQuestion(int index) {
        if (index < totalQuestion) {  //izvrti sva pitanja do kraja liste pitanja
            thisQuestion++;
            txtQuestionNum.setText(String.format("%d / %d", thisQuestion, totalQuestion)); //pitanja
            progressBar.setProgress(0);
            progressValue = 0;
            if (Common.questionList.get(index).getIsImageQuestion().equals("true")) {
                //ako je slika pitanje
                Picasso.with(getBaseContext())
                        .load(Common.questionList.get(index).getQuestion())
                        .into(question_image);
                question_image.setVisibility(View.VISIBLE);
                question_text.setVisibility(View.INVISIBLE);

            } else {
                //normalno pitanje tekst
                question_text.setText(Common.questionList.get(index).getQuestion());
                //ako je pitanje text tada ce se slika maknuti odnosno postavit ce se kao invisible
                question_image.setVisibility(View.INVISIBLE);
                question_text.setVisibility(View.VISIBLE);
            }

            btnA.setText(Common.questionList.get(index).getAnswerA());
            btnB.setText(Common.questionList.get(index).getAnswerB());
            btnC.setText(Common.questionList.get(index).getAnswerC());
            btnD.setText(Common.questionList.get(index).getAnswerD());
            mCountDown.start(); //odbrojavanje




        } else {
            //ako je zadnje pitanje, tada prikazi postignute rezult i prebaci na done
            Intent intent = new Intent(this, Done.class);
            Bundle dataSend = new Bundle(); //za prijenos podataka(niz) izmedu aktivnosti
            dataSend.putInt("SCORE", score);
            dataSend.putInt("TOTAL", totalQuestion);
            dataSend.putInt("CORRECT", correctAnwer);
            intent.putExtras(dataSend);
            startActivity(intent);

            Playing.this.finish();

        }
    }

    //neradi mi
    public void onRecieve(Context  context, Intent intent)
    {
        String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
        if ((state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK))){
            Toast.makeText(context,"Received State",Toast.LENGTH_SHORT).show();
            mCountDown.cancel();
        }
    }





    @Override
    protected void onResume() {
        super.onResume();

        totalQuestion = Common.questionList.size();
        mCountDown = new CountDownTimer(TIMEOUT, INTERVAL) {

            @Override
            public void onTick(long millisUntilFinished) {
                // timer
                long sec = (TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));

                progressBar.setProgress(progressValue);
                progressValue += 10.85; //povecava progressbar do kraja


                if(sec == 0)
                {
                    mCountDown.cancel();
                    Context context = getApplicationContext();
                    CharSequence text = "Isteklo vrijeme";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                    Intent intent = new Intent(Playing.this, Done.class);
                    Bundle dataSend = new Bundle(); //mapiraj podatke
                    dataSend.putInt("SCORE", score);
                    dataSend.putInt("TOTAL", totalQuestion);
                    dataSend.putInt("CORRECT", correctAnwer);
                    intent.putExtras(dataSend);
                    startActivity(intent);
                    Playing.this.finish();

                }


            }


            @Override
            public void onFinish() {
                mCountDown.cancel();
                showQuestion(++index);


            }
        };


        showQuestion(index); //trenutno pitanje
    }
    @Override
    public void onStop() {
        super.onStop();
        mCountDown.cancel(); //zaustavi timer
        thisQuestion--; //vrati na zaustavljeno pitanje


    }


}