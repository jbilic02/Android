package com.example.mcqapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mcqapp.Common.Common;
import com.example.mcqapp.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    private EditText email,pass;
    private Button loginB;
    private TextView forgotPassB, signupB;
    private FirebaseAuth mAuth;
    private Dialog progressDialog;
    private TextView dialogText;

    //dodano
    FirebaseDatabase database;
    DatabaseReference users;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //inicijalizacija varijabli
        email = findViewById(R.id.email);
        pass = findViewById(R.id.password);
        loginB = findViewById(R.id.loginB);
        forgotPassB = findViewById(R.id.forgot_password);
        signupB = findViewById(R.id.signupB);

        progressDialog = new Dialog(LoginActivity.this);
        progressDialog.setContentView(R.layout.dialog_layout);
        progressDialog.setCancelable(false); //korisnik ne moze cancel dialog, prikaz logina u app (dialog_layout)
        //layout od progressDialog
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        dialogText = progressDialog.findViewById(R.id.dialog_text);
        dialogText.setText("Prijava korisnika...");

        mAuth = FirebaseAuth.getInstance();

        //dodano je ovo ako se radi leaderboard koja ce izvlacit podatke od Users koji su spremljeni u Firebase, nije jos implementirano
        users = FirebaseDatabase.getInstance().getReference("Users");
        database = FirebaseDatabase.getInstance();



        //login button
        loginB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validData())
                {
                    login();
                }
            }
        });

        signupB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

    }

    //vraca true/false o valjanim podatcima za prijavu
    //ako se nista nije unijelo da izbaci error pokraj textbox za unos email i password
    private boolean validData()
    {
        if(email.getText().toString().isEmpty())
        {
            email.setError("Unesi Email");
            return false;
        }
        if(pass.getText().toString().isEmpty())
        {
            pass.setError("Unesi Lozinku");
            return false;
        }
        return true;
    }


    //funkcija za login firebase
    private void login()
    {
        progressDialog.show();
        //trim- uklanja sve razmake iz niza korisničkog unosa, email i sifra
        mAuth.signInWithEmailAndPassword(email.getText().toString().trim(), pass.getText().toString().trim())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Uspješna prijava, ažuriranje korisničkog sučelja s podacima prijavljenog korisnika
                            Toast.makeText(LoginActivity.this,"Prijava uspjesna",Toast.LENGTH_LONG).show();

                            progressDialog.dismiss();

                            //prebacivanje nakon prijave na mainactivity
                            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                            startActivity(intent);
                            finish();

                        } else {
                            progressDialog.dismiss();

                            //u slucaju neuspjesne prijave izbaci error
                            Toast.makeText(LoginActivity.this, task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();


                        }

                    }
                });
    }
}