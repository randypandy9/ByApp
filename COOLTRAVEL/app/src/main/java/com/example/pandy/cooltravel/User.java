package com.example.pandy.cooltravel;

/**
 * Created by pandy on 30/06/2015.
 */
public class User
{
    String fname, lname, username, password, vehicleDetails;
    int age;

    public User()
    {
        //empty one
    }
    public User(String fname, String lname, String username, String password, int age)
    {
        this.fname = fname;
        this.lname = lname;
        this.username = username;
        this.password = password;
        this.age = age;
        //all
    }
    public User(String fname, String lname, String username, String password, int age, String vehicleDetails)
    {
        this.fname = fname;
        this.lname = lname;
        this.username = username;
        this.password = password;
        this.age = age;
        this.vehicleDetails = vehicleDetails;
        //all
    }
    public User(String username, String password)
    {
        this.username = username;
        this.password = password;
        //login one


    }

    public String getVehicleDetails() {return vehicleDetails;}

    public void setVehicleDetails(String vehicleDetails) {this.vehicleDetails = vehicleDetails;}

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
