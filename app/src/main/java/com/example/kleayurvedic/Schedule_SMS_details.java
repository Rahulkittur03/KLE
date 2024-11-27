package com.example.kleayurvedic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.kleayurvedic.databinding.ActivityScheduleSmsDetailsBinding;
import com.example.kleayurvedic.databinding.ActivityUserDetailsBinding;
import com.example.kleayurvedic.utils.AndroidUtility;
import com.google.firebase.firestore.DocumentReference;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

public class Schedule_SMS_details extends AppCompatActivity {
    ActivityScheduleSmsDetailsBinding binding;
    String Username,OPDNumeber,IPDNumber,ContactNo,ScheduleDate,ScheduleTime,docID;
    String MessageSent,MessageContactNO;
    boolean IPDbool=false;
    private static final int PERMISSION_REQUEST_SEND_SMS = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityScheduleSmsDetailsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Username=getIntent().getStringExtra("Username");
        OPDNumeber=getIntent().getStringExtra("OPD_number");
        if (getIntent().getStringExtra("IPD_number") != null) {
            IPDNumber = getIntent().getStringExtra("IPD_number");
            IPDbool=true;
        }
        ContactNo=getIntent().getStringExtra("Contact_no");
        ScheduleDate=getIntent().getStringExtra("Schedule_Date");
        ScheduleTime=getIntent().getStringExtra("Schedule_Time");
        docID=getIntent().getStringExtra("docId");


        binding.textViewUsername.setText("Name "+Username);
        binding.textViewOPDNumber.setText("OPD Number "+OPDNumeber);
        binding.textViewContactNo.setText("Contact No "+ContactNo);
        binding.scheduleDate.setText("Appointment Date "+ScheduleDate);
        binding.scheduleTime.setText("Appointment Time "+ScheduleTime );
        if(IPDbool=true)
        {
            //binding.textViewIPDNumber.setText("IPD Number "+IPDNumber);
        }

        binding.buttonBack.setOnClickListener(v -> onBackPressed());
        binding.buttonHome.setOnClickListener(v -> {
            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
        });

        MessageContactNO="+91"+ContactNo;
        MessageSent="KLE Ayurvedic \nHello! "+Username+" Just a quick reminder that our appointment is scheduled for "+ScheduleDate+" at "+ScheduleTime+". Looking forward to seeing you then!";

        binding.sendSms.setOnClickListener(v -> {

            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                // Permission not granted, request it
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        PERMISSION_REQUEST_SEND_SMS);
            } else {
                // Permission granted, continue with sending SMS
                initializeSendSmsButton();
            }
            // Get the default instance of SmsManager
        });
        //What app message!
//        binding.sendSms.setOnClickListener(v -> {
//            if(WhatsAppInstalled()) {
//                try {
//                    String encodedMessage = URLEncoder.encode(MessageSent, "UTF-8");
//                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone="+MessageContactNO+"&text="+encodedMessage));
//                    startActivity(i);
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }
//            } else {
//                AndroidUtility.showToast(getApplicationContext(), "Install WhatsApp");
//            }
//        });


        binding.deleteSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteschedule();
                AndroidUtility.showToast(getApplicationContext(),"deleted Schedule");
            }
        });
    }
//    private boolean WhatsAppInstalled() {
//        PackageManager packageManager = getPackageManager();
//        try {
//            packageManager.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
//            return true; // WhatsApp is installed
//        } catch (PackageManager.NameNotFoundException e) {
//            return false; // WhatsApp is not installed
//        }
//    }
private void initializeSendSmsButton() {
    binding.sendSms.setOnClickListener(v -> {
        // Get the default instance of SmsManager
        SmsManager smsManager = SmsManager.getDefault();

        try {
            // Send the SMS
            smsManager.sendTextMessage(MessageContactNO, null, MessageSent, null, null);
            Toast.makeText(getApplicationContext(), "SMS Sent Successfully", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            // Handle any errors that may occur
            Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    });
}

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_SEND_SMS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, initialize the Send SMS button
                initializeSendSmsButton();
            } else {
                // Permission denied, show a message to the user
                Toast.makeText(this, "Permission denied to send SMS", Toast.LENGTH_SHORT).show();
            }
        }
    }
    void deleteschedule()
    {
        DocumentReference documentReference;
        //error here
        documentReference = AndroidUtility.getCollectionReferenceSchedule().document(docID);
        documentReference.delete().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                AndroidUtility.showToast(getApplicationContext(), "Deleted Successfully");
                Intent intent=new Intent(getApplicationContext(),Schedule_User_Date.class);
                startActivity(intent);
            } else {
                AndroidUtility.showToast(getApplicationContext(), "Failed Deleting");
            }
        });
    }
}