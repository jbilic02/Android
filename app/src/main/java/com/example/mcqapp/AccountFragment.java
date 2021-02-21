package com.example.mcqapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;


public class AccountFragment extends Fragment {
    private Button logoutB;


    public AccountFragment() {
        // Required empty public constructor
        Log.d("ddf","df");
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment, objekt view sadrzi referencu na XML datoteku
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        //inicijalizacija lougoutB
        logoutB = view.findViewById(R.id.logoutB);
        //ukoliko korisnik pritisne botun odjavi se
        logoutB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut(); //logout from firebase

                Intent intent = new Intent(getContext(),LoginActivity.class); //redirekcija/prebaci na LoginActivity
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

                getActivity().finish();
            }
        });

        return view;
    }
}