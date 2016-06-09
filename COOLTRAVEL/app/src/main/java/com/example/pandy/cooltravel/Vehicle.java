package com.example.pandy.cooltravel;

/**
 * Created by pandy on 05/03/2016.
 */
public class Vehicle
{
    private String Make , Model, Plate;
    private int vehicle_id;

    public Vehicle(){}
    public Vehicle(int Vehicle_id, String make, String model, String plate)
    {
        this.vehicle_id = Vehicle_id;
        this.Make = make;
        this.Model = model;
        this.Plate = plate;
    }
    public Vehicle( String make, String model, String plate)
    {
        this.Make = make;
        this.Model = model;
        this.Plate = plate;
    }

    public int getVehicle_id() {
        return vehicle_id;
    }

    public void setVehicle_id(int vehicle_id) {
        vehicle_id = vehicle_id;
    }

    public String getMake() {
        return Make;
    }

    public void setMake(String make) {
        Make = make;
    }

    public String getModel() {
        return Model;
    }

    public void setModel(String model) {
        Model = model;
    }

    public String getPlate() {
        return Plate;
    }

    public void setPlate(String plate) {
        Plate = plate;
    }

    @Override
    public String toString() {
        return "Vehicle: "+Make+" "+Model+" ("+Plate+")";
    }
}
