package com.vojtechruzicka.javafxweaverexample.entyti;

import javax.persistence.*;
@Entity
public class JournalEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String dob;
    private String phone;
    private String surFirstSecondName;

    public JournalEntity(String dob, String phone, String surFirstSecondName) {
        this.dob = dob;
        this.phone = phone;
        this.surFirstSecondName = surFirstSecondName;
    }

    public JournalEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSurFirstSecondName() {
        return surFirstSecondName;
    }

    public void setSurFirstSecondName(String surFirstSecondName) {
        this.surFirstSecondName = surFirstSecondName;
    }
}
