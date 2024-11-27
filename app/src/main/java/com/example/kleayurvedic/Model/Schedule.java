package com.example.kleayurvedic.Model;

import com.google.firebase.Timestamp;

public class Schedule {
    String Username;
    String UserOPD_Number;
    String UserIPD_Number;
    String User_Problem;
    String Description;
    Timestamp Schedule_timestamp;
    String Schedule_Date;
    String Schedule_Status;

    public String getSchedule_Text_time() {
        return Schedule_Text_time;
    }

    public void setSchedule_Text_time(String schedule_Text_time) {
        Schedule_Text_time = schedule_Text_time;
    }

    String Schedule_Text_time;

    public void setSchedule_Date(String schedule_Date) {
        Schedule_Date = schedule_Date;
    }

    public String getUserContactNo() {
        return UserContactNo;
    }

    public void setUserContactNo(String userContactNo) {
        UserContactNo = userContactNo;
    }

    String UserContactNo;

    public Schedule(String userContactNo) {
        UserContactNo = userContactNo;
    }

    public String getSchedule_Date() {
        return Schedule_Date;
    }


    public String getSchedule_Status() {
        return Schedule_Status;
    }

    public void setSchedule_Status(String schedule_Status) {
        Schedule_Status = schedule_Status;
    }

    public Schedule() {
    }

    public Schedule(String username, String userOPD_Number, String userIPD_Number, String user_Problem, String description,String schedule_Text_time,Timestamp schedule_timestamp,String schedule_Date, String schedule_Status,String userContactNo) {
        Username = username;
        UserOPD_Number = userOPD_Number;
        UserIPD_Number = userIPD_Number;
        User_Problem = user_Problem;
        Description = description;
        Schedule_timestamp = schedule_timestamp;
        Schedule_Date = schedule_Date;
        Schedule_Status = schedule_Status;
        UserContactNo = userContactNo;
        Schedule_Text_time = schedule_Text_time;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getUserOPD_Number() {
        return UserOPD_Number;
    }

    public void setUserOPD_Number(String userOPD_Number) {
        UserOPD_Number = userOPD_Number;
    }

    public String getUserIPD_Number() {
        return UserIPD_Number;
    }

    public void setUserIPD_Number(String userIPD_Number) {
        UserIPD_Number = userIPD_Number;
    }

    public String getUser_Problem() {
        return User_Problem;
    }

    public void setUser_Problem(String user_Problem) {
        User_Problem = user_Problem;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public Timestamp getSchedule_timestamp() {
        return Schedule_timestamp;
    }

    public void setSchedule_timestamp(Timestamp schedule_timestamp) {
        Schedule_timestamp = schedule_timestamp;
    }
}
