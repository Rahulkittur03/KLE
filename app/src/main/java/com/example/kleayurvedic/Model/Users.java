package com.example.kleayurvedic.Model;
import com.google.firebase.Timestamp;


public class Users {
    String username;
    String userage;
    String usergender;
    String userbloodgroup;
    String usercontactNo;
    String userOPDnumber;
    String userIPDnumber;
    String userProblem;
    String userAddress;
    Timestamp timestamp;

    public Users() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserage() {
        return userage;
    }

    public void setUserage(String userage) {
        this.userage = userage;
    }

    public String getUsergender() {
        return usergender;
    }

    public void setUsergender(String usergender) {
        this.usergender = usergender;
    }

    public String getUserbloodgroup() {
        return userbloodgroup;
    }

    public void setUserbloodgroup(String userbloodgroup) {
        this.userbloodgroup = userbloodgroup;
    }

    public String getUsercontactNo() {
        return usercontactNo;
    }

    public void setUsercontactNo(String usercontactNo) {
        this.usercontactNo = usercontactNo;
    }

    public String getUserOPDnumber() {
        return userOPDnumber;
    }

    public void setUserOPDnumber(String userOPDnumber) {
        this.userOPDnumber = userOPDnumber;
    }

    public String getUserIPDnumber() {
        return userIPDnumber;
    }

    public void setUserIPDnumber(String userIPDnumber) {
        this.userIPDnumber = userIPDnumber;
    }

    public String getUserProblem() {
        return userProblem;
    }

    public void setUserProblem(String userProblem) {
        this.userProblem = userProblem;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
