package com.example.kleayurvedic;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.kleayurvedic.MainActivity;
import com.example.kleayurvedic.Model.Schedule;
import com.example.kleayurvedic.MyReceiver;
import com.example.kleayurvedic.databinding.ActivityUserDetailsBinding;
import com.example.kleayurvedic.utils.AndroidUtility;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class UserDetails extends AppCompatActivity {
    ActivityUserDetailsBinding binding;
    Boolean ipdbool =false;
    String p_username, p_OPDNumber, p_IPDNumber, p_contactno, p_problem, p_description, p_day, p_docId;
    String Schedule_Date,Schedule_Time, Schedule_Status;
    String strTime, strDate;

    Calendar calendar;
    MyReceiver myReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserDetailsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Schedule_Status = "1";

        p_username = getIntent().getStringExtra("username");
        p_OPDNumber = getIntent().getStringExtra("userOPDnumber");
        try {
            if (getIntent().getStringExtra("userIPDnumber") != null) {
                p_IPDNumber = getIntent().getStringExtra("userIPDnumber");
                binding.textViewIPDNumber.setText("IDP No: "+p_IPDNumber);
                ipdbool=true;
            }
        } catch (Exception e) {
            Log.i("ipdNOt availale", e.getLocalizedMessage());
        }

        p_contactno = getIntent().getStringExtra("usercontactNo");
        p_problem = getIntent().getStringExtra("userProblem");
        p_docId = getIntent().getStringExtra("docId");
        Log.i("documentId", p_docId);

        binding.textViewUsername.setText("Name: "+p_username);
        binding.textViewOPDNumber.setText("OPD No: "+p_OPDNumber);
        binding.textViewProblem.setText("Problem: "+p_problem);


        binding.scheduleBtn.setOnClickListener(v -> {
            p_description = binding.EditTextDesc.getText().toString();
            p_day = binding.editTextDay.getText().toString();
            Schedule_Date = binding.editTextDate.getText().toString();
            Schedule_Time=binding.editTextTime.getText().toString();
            if(p_day =="DAY - ")
            { AndroidUtility.showToast(getApplicationContext(),"Enter the DAY");
                return;
            }
            if (Schedule_Date.isEmpty() | Schedule_Time.isEmpty() ) {
                AndroidUtility.showToast(getApplicationContext(),"Enter the Data and Time");
                return;
            }
            save_details_schedule();
        });

        binding.buttonBack.setOnClickListener(v -> onBackPressed());
        binding.buttonHome.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });

        // Date and time picker
        calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);


        binding.editTextDate.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePickerDialog view1, int year1, int monthOfYear, int dayOfMonth) {
                    calendar.set(year1, monthOfYear, dayOfMonth);
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    strDate = sdf.format(calendar.getTime());
                    binding.editTextDate.setText(strDate);
                }
            }, year, month, day);
            datePickerDialog.show(getSupportFragmentManager(), "DatePickerDialog");
        });

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        binding.editTextTime.setOnClickListener(v -> {
            TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePickerDialog view12, int hourOfDay, int minute1, int second) {
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    calendar.set(Calendar.MINUTE, minute1);

                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
                    strTime = sdf.format(calendar.getTime());
                    binding.editTextTime.setText(strTime);
                }
            }, hour, minute, true);
            timePickerDialog.show(getSupportFragmentManager(), "TimePickerDialog");
        });

        // Register BroadcastReceiver dynamically
        myReceiver = new MyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.example.kleayurvedic.MY_ACTION");
        registerReceiver(myReceiver, filter);

        // ** Notification **

       createNotificationChannal();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Unregister the BroadcastReceiver when the activity is destroyed
        unregisterReceiver(myReceiver);
    }

    void save_details_schedule() {
        Schedule schedule = new Schedule();
        schedule.setUsername(p_username);
        schedule.setUserOPD_Number(p_OPDNumber);
        schedule.setUser_Problem(p_problem);
        schedule.setDescription(p_description);
        schedule.setSchedule_timestamp(Timestamp.now());
        schedule.setSchedule_Date(Schedule_Date);
        schedule.setSchedule_Text_time(Schedule_Time);
        schedule.setSchedule_Status(Schedule_Status);
        schedule.setUserContactNo(p_contactno);
        if(ipdbool=true)
        {
            schedule.setUserIPD_Number(p_IPDNumber);
        }
        saveToFireDatabase(schedule);
    }

    void saveToFireDatabase(Schedule schedule) {
        DocumentReference documentReference;
        documentReference = AndroidUtility.getCollectionReferenceSchedule().document();
        documentReference.set(schedule).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String docId = documentReference.getId();
                AndroidUtility.showToast(getApplicationContext(), "Success");
                try {
                    Alarm_Manager(docId,p_username,p_OPDNumber);
                } catch (Exception e) {
                    Log.i("error", e.getLocalizedMessage());
                }
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            } else {
                AndroidUtility.showToast(getApplicationContext(), "Failed");
            }
        });
    }
//docid_change
    void Alarm_Manager(String docId,String p_username,String p_OPDNumber) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        long alarmTime = calendar.getTimeInMillis();

        Intent intent = new Intent(getApplicationContext(), MyReceiver.class);
        intent.putExtra("docId", docId);
        intent.putExtra("p_docId", p_docId);
        intent.putExtra("username", p_username);
        intent.putExtra("OPDNumber", p_OPDNumber);
        Log.i("Time", "aaaa" + alarmTime);
        PendingIntent pi;
        final int ALARM_REQ =(int)System.currentTimeMillis();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            pi = PendingIntent.getBroadcast(getApplicationContext(), ALARM_REQ, intent,
                    PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);
        } else {
            pi = PendingIntent.getBroadcast(getApplicationContext(), ALARM_REQ, intent,
                    PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);
        }

        alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTime, pi);
    }
    // Notification Version Check if not then  create channal
    private void createNotificationChannal() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            CharSequence name="Patient Notification";
            String description="channal alaram";
            int important= NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel=new NotificationChannel("Alarm_Manager",name,important);
            channel.setDescription(description);

            NotificationManager notificationManager=getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
            Log.i("alaram","Manager_01");
        }
        else {
            Log.i("alaram","Manager_lower_then_8");
        }
    }
}
