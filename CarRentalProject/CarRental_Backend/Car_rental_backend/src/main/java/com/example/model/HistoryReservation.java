package com.example.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class HistoryReservation {
    @Id
    @Column(name = "history_reservation_id")
    private int history_reservation_id;
    @Column(name = "user_firstname")
    private String user_firstname;
    @Column(name = "user_lastname")
    private String user_lastname;
    @Column(name = "user_phone")
    private String user_phone;
    @Column(name = "car_name")
    private String car_name;
    @Column(name = "car_platenumber")
    private String car_platenumber;
    @Column(name = "car_typemodel")
    private String car_typemodel;
    @Column(name = "image_path")
    private String image_path;
    @Column(name = "start_date")
    private Date start_date;
    @Column(name = "end_date")
    private Date end_date;
    @Column(name = "cost_per_day")
    private double cost_per_day;
    @Column(name = "total_cost")
    private double total_cost;

    public HistoryReservation() {

    }

    public HistoryReservation(int history_reservation_id, String user_firstname, String user_lastname, String user_phone,
            String car_name, String car_platenumber, String car_typemodel, String image_path, Date start_date, Date end_date,
            double total_cost, double cost_per_day) {
        super();
        this.history_reservation_id = history_reservation_id;
        this.user_firstname = user_firstname;
        this.user_lastname = user_lastname;
        this.user_phone = user_phone;
        this.car_name = car_name;
        this.car_platenumber = car_platenumber;
        this.car_typemodel = car_typemodel;
        this.image_path = image_path;
        this.start_date = start_date;
        this.end_date = end_date;
        this.total_cost = total_cost;
        this.cost_per_day = cost_per_day;
    }

    public int getHistoryReservation_id() {
        return history_reservation_id;
    }

    public void setHistoryReservation_id(int history_reservation_id) {
        this.history_reservation_id = history_reservation_id;
    }

    public String getUser_firstname() {
        return user_firstname;
    }

    public void setUser_firstname(String user_firstname) {
        this.user_firstname = user_firstname;
    }

    public String getUser_lastname() {
        return user_lastname;
    }

    public void setUser_lastname(String user_lastname) {
        this.user_lastname = user_lastname;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public String getCar_name() {
        return car_name;
    }

    public void setCar_name(String car_name) {
        this.car_name = car_name;
    }

    public String getCar_platenumber() {
        return car_platenumber;
    }

    public void setCar_platenumber(String car_platenumber) {
        this.car_platenumber = car_platenumber;
    }

    public String getCar_typemodel() {
        return car_typemodel;
    }

    public void setCar_typemodel(String car_typemodel) {
        this.car_typemodel = car_typemodel;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public Date getStart_date() {
        return start_date;
    }

    public void setStart_date(Date start_date) {
        this.start_date = start_date;
    }

    public Date getEnd_date() {
        return end_date;
    }

    public void setEnd_date(Date end_date) {
        this.end_date = end_date;
    }

    public double getcost_per_day() {
        return cost_per_day;
    }

    public void setcost_per_day(double cost_per_day) {
        this.cost_per_day = cost_per_day;
    }

    public double getTotal_cost() {
        return total_cost;
    }

    public void setTotal_cost(double total_cost) {
        this.total_cost = total_cost;
    }
}
