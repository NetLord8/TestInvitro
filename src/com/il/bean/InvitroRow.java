package com.il.bean;

import java.time.LocalDate;

/**
 * Created by ulmasov_im on 07.06.2016.
 */
public class InvitroRow {
    private LocalDate analyzDate;
    private String nss;
    private double inz;
    private String patient;
    private String code;
    private String analyz;
    private double aCount;
    private double price;

    public InvitroRow() {
    }

    public LocalDate getAnalyzDate() {
        return analyzDate;
    }

    public void setAnalyzDate(LocalDate analyzDate) {
        this.analyzDate = analyzDate;
    }

    public String getNss() {
        return nss;
    }

    public void setNss(String nss) {
        this.nss = nss;
    }

    public double getInz() {
        return inz;
    }

    public void setInz(double inz) {
        this.inz = inz;
    }

    public String getPatient() {
        return patient;
    }

    public void setPatient(String patient) {
        this.patient = patient;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAnalyz() {
        return analyz;
    }

    public void setAnalyz(String analyz) {
        this.analyz = analyz;
    }

    public double getaCount() {
        return aCount;
    }

    public void setaCount(double aCount) {
        this.aCount = aCount;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
