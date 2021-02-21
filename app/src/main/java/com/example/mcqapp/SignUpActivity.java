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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mcqapp.Common.Common;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DataSnapshot;

import com.example.mcqapp.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignUpActivity extends AppCompatActivity {

    private EditText name,email,pass,confirmPass;
    private Button signUpB;
    private ImageView backB;

    private FirebaseAuth mAuth;
    private String emailStr, passStr, confirmPassStr, nameStr;
    private Dialog progressDialog;
    private TextView dialogText;
    DatabaseReference users;
    //FirebaseDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        name = findViewById(R.id.username);
        email = findViewById(R.id.email);
        pass = findViewById(R.id.password);
        confirmPass = findViewById(R.id.confirm_pass);
        signUpB = findViewById(R.id.signupB);
        backB = findViewById(R.id.backB);

        //database
        //database = FirebaseDatabase.getInstance();
        //users = database.getReference("Users");
        users = FirebaseDatabase.getInstance().getReference("Users");

        //prikaz ucitavanja u app
        progressDialog = new Dialog(SignUpActivity.this);
        progressDialog.setContentView(R.layout.dialog_layout);
        progressDialog.setCancelable(false); //korisnik ne moze cancel dialog, prikaz logina u app (dialog_layout)
        //layout od progressDialog
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        dialogText = progressDialog.findViewById(R.id.dialog_text);
        dialogText.setText("Registracija korisnika..."); //text dialoga

        mAuth = FirebaseAuth.getInstance();


        //back tipka za vracanje na login activity
        backB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        signUpB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()) { //provjera ispravih unosa ako je true pozovi signupNewUser funkciju
                    signupNewUser();
                }

                    //spremi podatke registracije u database pod Users
                    User user = new User(nameStr,passStr,emailStr);
                    users.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.child(user.getUserName()).exists()) { //provjera postojanja korisnika
                                User login = snapshot.child(nameStr).getValue(User.class);
                                Toast.makeText(SignUpActivity.this,"exist user",Toast.LENGTH_SHORT).show();

                                Common.currentUser = login;
                            }
                            else
                            {
                                users.child(user.getUserName()).setValue(user); //tada verificiraj korisnika(ime,sifra,email) u bazu path Users
                                Toast.makeText(SignUpActivity.this,"success",Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                //}
            }
        });

    }

    //provjera ispravnog unosa podataka
    private boolean validate()
    {
        nameStr = name.getText().toString().trim(); //trim-brise razmake
        passStr = pass.getText().toString().trim();
        emailStr = email.getText().toString().trim();
        confirmPassStr = confirmPass.getText().toString().trim();

        if(nameStr.isEmpty())
        {
            name.setError("Unesi Ime");
            return false;
        }
        if(emailStr.isEmpty())
        {
            email.setError("Unesi Email");
            return false;
        }
        if(passStr.isEmpty())
        {
            pass.setError("Unesi Lozinku");
            return false;
        }
        if(confirmPassStr.isEmpty())
        {
            confirmPass.setError("Unesi ponovo Lozinku");
            return false;
        }
        if(passStr.compareTo(confirmPassStr) != 0) //ako nisu iste
        {
            Toast.makeText(SignUpActivity.this,"Lozinke bi trebale biti iste",Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void signupNewUser() //dodavanje novog korisnika
    {
        progressDialog.show(); //prikaz dialoga korisniku

        mAuth.createUserWithEmailAndPassword(emailStr, passStr)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //ukoliko je uspjesna registracija
                            Toast.makeText(SignUpActivity.this, "Uspjesna registracija", Toast.LENGTH_LONG).show();

                            progressDialog.dismiss();
                            //nakon uspjesne registracije pribacujemo se na mainactivity dok se signupActivity zavrsava
                            Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                            SignUpActivity.this.startActivity(intent);
                            SignUpActivity.this.finish();

                        } else {
                            //ako je registracija pogresna tada prikazi poruku korisniku
                            progressDialog.dismiss();
                            Toast.makeText(SignUpActivity.this, "Greska kod registracije.",
                                    Toast.LENGTH_LONG).show();
                        }

                    }
                });
    }
}