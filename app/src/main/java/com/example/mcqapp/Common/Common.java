package com.example.mcqapp.Common;

import com.example.mcqapp.Model.Question;
import com.example.mcqapp.Model.User;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class Common {
    //globalne varijable
    public static String categoryId;
    public static User currentUser;
    public static List<Question> questionList = new ArrayList<>();

}


//ObjGen: kategorije    http://objgen.com/json/models/OOf
//ObjGen: pitanja     http://objgen.com/json/models/bH0