package com.example.kleayurvedic;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.example.kleayurvedic.Model.Schedule;
import com.example.kleayurvedic.Model.Users;
import com.example.kleayurvedic.utils.AndroidUtility;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

public class Schedule_User_Date extends AppCompatActivity {
    RecyclerView recyclerView;
    String p_docid;
    ImageButton back_btn,Home_btn;
    ScheduleAdapter scheduleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_user_date);
        back_btn=findViewById(R.id.button_back);
        Home_btn=findViewById(R.id.button_home);
        recyclerView=findViewById(R.id.recycler_view_schedule);
        p_docid=getIntent().getStringExtra("docId");
        SetupRecyclerView();
        back_btn.setOnClickListener(v -> super.onBackPressed());
        Home_btn.setOnClickListener(v -> {
            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
        });
        Log.i("DocId","this is :   "+p_docid);
    }
    void SetupRecyclerView()
    {
        Query query= AndroidUtility.getCollectionReferenceSchedule().orderBy("schedule_Date", Query.Direction.ASCENDING).whereEqualTo("schedule_Status","1");
        FirestoreRecyclerOptions<Schedule> options=new FirestoreRecyclerOptions.Builder<Schedule>().setQuery(query,Schedule.class).build();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        scheduleAdapter=new ScheduleAdapter(options,getApplicationContext());
        recyclerView.setAdapter(scheduleAdapter);
        AndroidUtility.getCollectionReferenceSchedule().addSnapshotListener((value, error) -> {
            if (value != null) {
                scheduleAdapter.notifyDataSetChanged();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        scheduleAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        scheduleAdapter.stopListening();
    }


}