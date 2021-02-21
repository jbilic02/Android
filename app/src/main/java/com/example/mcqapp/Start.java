package com.example.mcqapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.mcqapp.Common.Common;
import com.example.mcqapp.Model.Question;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Collections;

public class Start extends AppCompatActivity {
    Button btnPlay;
    FirebaseDatabase database;
    DatabaseReference questions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        //učitavanje pitanja iz firebasea, prema odredenoj kategoriji
        database = FirebaseDatabase.getInstance();
        questions = database.getReference("Questions");
        loadQuestion(Common.categoryId);

        btnPlay = findViewById(R.id.btnPlay);
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Start.this, Playing.class);
                startActivity(intent);
                finish();
            }
        });


    }

    private void loadQuestion(String categoryId) {
        // izbrisi listu ako sadrzi stara pitanja
        if (Common.questionList.size() > 0) {
            Common.questionList.clear();
        }
        questions.orderByChild("CategoryId").equalTo(categoryId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) { //dataSnapshot- sadrzi podatke koji su dohvaceni sa firebase
                        //nepromjenjive kopije
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            Question ques = postSnapshot.getValue(Question.class); //
                            Common.questionList.add(ques); //dodavanje pitanja u listu questionList
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        //random lista
        Collections.shuffle(Common.questionList);

    }
}