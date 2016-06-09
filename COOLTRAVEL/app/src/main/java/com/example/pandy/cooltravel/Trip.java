package com.example.pandy.cooltravel;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by pandy on 18/12/2015.
 */
public class Trip
{
    private int trip_id;
    private String Origin;
    private String Destination;
    private int price;
    private int Seats;
    private int SeatsRemaining;
    private String details;
    private String date;


    public int getTrip_id() {
        return trip_id;
    }

    public void setTrip_id(int trip_id) {
        trip_id = trip_id;
    }



    public Trip()
    {

    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Trip(String Origin, String Destination, int price,int Seats, int SeatsRemaining, String details, String date)
    {
        this.Origin = Origin;
        this.Destination = Destination;
        this.price = price;
        this.Seats = Seats;
        this.SeatsRemaining = SeatsRemaining;
        this.details = details;
        this.date = date;

    }

    public Trip (int trip_id, String Origin, String Destination, int price,int Seats, int SeatsRemaining, String details, String date)
    {
        this.trip_id = trip_id;
        this.Origin = Origin;
        this.Destination = Destination;
        this.price = price;
        this.Seats = Seats;
        this.SeatsRemaining = SeatsRemaining;
        this.details = details;
        this.date = date;

    }

    public String getOrigin() {
        return Origin;
    }

    public void setOrigin(String origin) {
        Origin = origin;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDestination() {
        return Destination;
    }

    public void setDestination(String destination) {
        Destination = destination;
    }

    public int getSeats() {
        return Seats;
    }

    public void setSeats(int seats) {
        Seats = seats;
    }

    public int getSeatsRemaining() {
        return SeatsRemaining;
    }

    public void setSeatsRemaining(int seatsRemaining) {
        SeatsRemaining = seatsRemaining;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getDisplayPrice(int price)
    {
        String strPrice = Integer.toString(getPrice());
        String poundshalf = strPrice.substring(0,strPrice.length()-2);
        String pennieshalf = strPrice.substring(strPrice.length()-2,strPrice.length());
        return poundshalf+"."+pennieshalf;
    }

    @Override
    public String toString() {
        return  " Trip Number: "+trip_id+"\n "+ Origin + " to " + Destination + "    "+ date+"\n £"+getDisplayPrice(price)+" per seat \n Seats Remaining: " + SeatsRemaining;
    }
}
