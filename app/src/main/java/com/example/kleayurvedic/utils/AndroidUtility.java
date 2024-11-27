package com.example.kleayurvedic.utils;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;

public class AndroidUtility {
    public static void showToast(Context context,String str){
        Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
    }
    public static CollectionReference getCollectionReferenceUser()
    {

        return FirebaseFirestore.getInstance().collection("Users").document().getParent();
    }
    public static CollectionReference getCollectionReferenceSchedule()
    {
        return FirebaseFirestore.getInstance().collection("Schedule");
    }
   public static String TimestamptoString(Timestamp timestamp)
    {
        return new SimpleDateFormat("MM/dd/yyyy").format(timestamp.toDate());
    }
}
