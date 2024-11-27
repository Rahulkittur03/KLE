package com.example.kleayurvedic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.kleayurvedic.Model.Users;
import com.example.kleayurvedic.databinding.ActivityNewUserBinding;
import com.example.kleayurvedic.utils.AndroidUtility;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

public class new_user_activity extends AppCompatActivity {
    ActivityNewUserBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityNewUserBinding.inflate(getLayoutInflater());
        View view=binding.getRoot();
        setContentView(view);



        binding.buttonBack.setOnClickListener(v -> onBackPressed());
        binding.buttonHome.setOnClickListener(v -> {
            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
        });

        binding.userAddButton.setOnClickListener(v -> {
            save_User_details();
        });
    }
    void save_User_details()
    {
        String username=binding.formName.getText().toString();
        String userage=binding.formAge.getText().toString();
        String usergender=binding.formGender.getSelectedItem().toString();
        String userbloodgroup=binding.formBloodGroup.getSelectedItem().toString();
        String usercontactNo=binding.formContactNo.getText().toString();
        String userOPDnumber=binding.formOpdNo.getText().toString();
        String userIPDnumber=binding.formIpdNo.getText().toString();
        String userProblem=binding.formUserProblem.getText().toString();
        String userAddress=binding.formAddress.getText().toString();
        if(!user_data_validation(username,userage,usergender, userbloodgroup,usercontactNo,userOPDnumber, userIPDnumber, userProblem, userAddress))
        {
            return;
        }
        else {
            //mainfun
            Users users=new Users();
            users.setUsername(username);
            users.setUserage(userage);
            users.setUsergender(usergender);
            users.setUserbloodgroup(userbloodgroup);
            users.setUsercontactNo(usercontactNo);
            users.setUserOPDnumber(userOPDnumber);
            users.setUserIPDnumber(userIPDnumber);
            users.setUserProblem(userProblem);
            users.setUserAddress(userAddress);
            users.setTimestamp(Timestamp.now());

            save_userstoFirebase(users);
        }
    }
    boolean user_data_validation(String username, String userAge, String userGender, String userBloodGroup,String usercontactNo,
                                 String userOPDNumber, String userIPDNumber, String userProblem, String userAddress)
    {
        //blood grp and gender not workings
        String spinner_gender="Select Your Gender";
        String spinner_blood="Select Your bloodgroup";
       if(username==null || username.length()<3|| username.isEmpty())
       {
            binding.formName.setError("Required UserName");
            return false;
       }
        if(userAge==null|| userAge.isEmpty())
        {
            binding.formAge.setError("Required Age");
            return false;
        }
        if(userBloodGroup.equals(spinner_gender))
        {
            AndroidUtility.showToast(getApplicationContext(),"Select Your Gender");
            return false;
        }
        if(userBloodGroup.equals(spinner_blood))
        {
            AndroidUtility.showToast(getApplicationContext(),"Select your blood group");
            return false;
        }
        if(usercontactNo==null||usercontactNo.isEmpty())
        {
            binding.formContactNo.setError("Required Contact Number");
            return false;
        }
        if(userOPDNumber==null||userOPDNumber.isEmpty())
        {
            binding.formOpdNo.setError("Required OPD Number");
            return false;
        }
        if(userProblem==null||userProblem.isEmpty())
        {
            binding.formUserProblem.setError("Required problem");
            return false;
        }
        if(userAddress==null||userAddress.isEmpty())
        {
            binding.formAddress.setError("Required address");
            return false;
        }
        return true;
    }
    void save_userstoFirebase(Users users)
    {
        DocumentReference documentReference;
        documentReference=AndroidUtility.getCollectionReferenceUser().document();

        documentReference.set(users).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    AndroidUtility.showToast(getApplicationContext(),"User Added SuccesFully");
                    Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                }
                else {
                    AndroidUtility.showToast(getApplicationContext(),task.getException().getLocalizedMessage());
                }
            }
        });
    }

}