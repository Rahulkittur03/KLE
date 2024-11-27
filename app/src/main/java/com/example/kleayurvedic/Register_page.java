package com.example.kleayurvedic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.kleayurvedic.utils.AndroidUtility;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register_page extends AppCompatActivity {
    EditText email_edit_text,password_edit_text,confirm_passoword,secret_key;
    TextView login_btn;
    ProgressBar progressBar;
    String email_text,password_text,strconfim_pass;
    Button btn_register;
    String Secret_key_01="10000";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);
        //ID's
        progressBar=findViewById(R.id.progress_bar);
        login_btn=findViewById(R.id.login_Connect);
        email_edit_text=findViewById(R.id.email_Register);
        password_edit_text=findViewById(R.id.register_password);
        confirm_passoword=findViewById(R.id.confim_password_register);
        btn_register=findViewById(R.id.btn_register);
        secret_key=findViewById(R.id.Admin_password);

        progress_bar(false);



        //text



        login_btn.setOnClickListener(v -> {
            Intent intent=new Intent(getApplicationContext(),login_activity.class);
            startActivity(intent);
            finishAffinity();
        });
        btn_register.setOnClickListener(v -> {
            createaccount();
        });

    }
    public void progress_bar(boolean inprogress)
    {
        if(inprogress)
        {
            progressBar.setVisibility(View.VISIBLE);
            btn_register.setVisibility(View.GONE);
        }
        else {
            progressBar.setVisibility(View.GONE);
            btn_register.setVisibility(View.VISIBLE);
        }
    }
    void createaccount()
    {
        email_text=email_edit_text.getText().toString();
        password_text=password_edit_text.getText().toString();
        strconfim_pass=confirm_passoword.getText().toString();
        if(!validation_register()) {
            return;
        }
        createFireBaseAccount(email_text,password_text);
    }
    boolean validation_register()
    {
        if(!Patterns.EMAIL_ADDRESS.matcher(email_text).matches())
        {
            email_edit_text.setError("Invalid E-mail");
            return false;
        }
        if(password_text.length()<8)
        {
            password_edit_text.setError("password length is less then 8");
            return false;
        }
        if((confirm_passoword.getText().toString()).length()<8)
        {
            confirm_passoword.setError("confirm password length is less then 8");
            return false;
        }
        if(!password_text.equals(strconfim_pass))
        {
            confirm_passoword.setError("Doesnt match with the password");
            return false;
        }
        if(!(secret_key.getText().toString()).equals(Secret_key_01))
        {
            secret_key.setError("Key Doesn't match");
            return false;
        }
        return true;
    }

    void createFireBaseAccount(String email_text,String password_text)
    {
        progress_bar(true);
        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email_text,password_text).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progress_bar(false);
                if(task.isSuccessful())
                {
                    AndroidUtility.showToast(getApplicationContext(),"Sucessfully Created our account,Check your Email to Verify");
                    firebaseAuth.getCurrentUser().sendEmailVerification();
                    firebaseAuth.signOut();
                    Intent intent=new Intent(getApplicationContext(),login_activity.class);
                    startActivity(intent);
                }
                else {
                    AndroidUtility.showToast(getApplicationContext(),task.getException().getLocalizedMessage());
                    Log.i("error",task.getException().getLocalizedMessage());
                    return;
                }
            }
        });
    }
}