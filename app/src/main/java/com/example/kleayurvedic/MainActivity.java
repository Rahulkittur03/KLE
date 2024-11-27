package com.example.kleayurvedic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import com.example.kleayurvedic.Model.Users;
import com.example.kleayurvedic.utils.AndroidUtility;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.Query;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ImageButton btn_menu;
    UserAdapter userAdapter;
    EditText Search_User;
    FloatingActionButton add_new_patient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView=findViewById(R.id.recycler_view);
        btn_menu=findViewById(R.id.button_menu);
        add_new_patient=findViewById(R.id.add_new_patient);
        Search_User=findViewById(R.id.Search_btn);


        btn_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowMenu();
            }
        });

        setupRecyclerview();

        add_new_patient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),new_user_activity.class);
                startActivity(intent);
            }
        });
//        Search_User.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                String searchText = s.toString().trim();
//                if (!searchText.isEmpty()) {
//                    performSearch(searchText);
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                String searchText = s.toString().trim();
//                if (!searchText.isEmpty()) {
//                    performSearch(searchText);
//                }
//            }
//        });
    }

    void ShowMenu()
    {
        //display and work menu
        PopupMenu popupMenu =new PopupMenu(getApplicationContext(),btn_menu);
        popupMenu.getMenu().add("Scheduled");
        popupMenu.getMenu().add("Logout");
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getTitle()=="Scheduled")
                {
                    //schedule
                    Intent intent=new Intent(getApplicationContext(),Schedule_User_Date.class);
                    startActivity(intent);
                } else if (item.getTitle()=="Logout") {
                    //logout
                    FirebaseAuth.getInstance().signOut();
                    Intent intent=new Intent(getApplicationContext(),login_activity.class);
                    startActivity(intent);
                }
                return false;
            }
        });
    }
    void setupRecyclerview()
    {
        Query query= AndroidUtility.getCollectionReferenceUser().orderBy("timestamp", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Users>options=new FirestoreRecyclerOptions.Builder<Users>().setQuery(query,Users.class).build();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        userAdapter=new UserAdapter(options,getApplicationContext());
        recyclerView.setAdapter(userAdapter);
    }

    void performSearch(String searchText) {
        Query query = AndroidUtility.getCollectionReferenceUser()
                .orderBy("userOPDnumber")
                .startAt(searchText)
                .endAt(searchText + "\uf8ff");

        FirestoreRecyclerOptions<Users> options = new FirestoreRecyclerOptions.Builder<Users>()
                .setQuery(query, Users.class)
                .build();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        userAdapter.updateOptions(options);
        recyclerView.setAdapter(userAdapter);
    }


    @Override
    protected void onStart() {
        super.onStart();
        userAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        userAdapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        userAdapter.notifyDataSetChanged();
    }
}