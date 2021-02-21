package com.example.mcqapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class LeaderBoardFragment extends Fragment {
    View myFragment;

    FirebaseDatabase database;
    DatabaseReference questionScore,rankingTbl;
    int sum = 0;
    RecyclerView rankingList;
    LinearLayoutManager layoutManager;



    public static LeaderBoardFragment newInstance()
    {
        LeaderBoardFragment leaderBoardFragment = new LeaderBoardFragment();
        return leaderBoardFragment;
    }

    public LeaderBoardFragment() {
        //prazni konstruktor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myFragment = inflater.inflate(R.layout.fragment_leader_board,container,false);



        return myFragment;
    }


}