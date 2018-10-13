package me.synology.hsbong.patientphotostorage.model;

import android.app.Application;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by bongh on 2018-10-11.
 */

public class Person {
    private int id;
    private String name;
    private int age;

    private String email;
    private String phone;
    private String password;
    private String uniqueId;
    private String photo;
    private String department;



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public static Person parsePerson(JSONObject json) throws JSONException {
        Person person = new Person();

        person.setId(json.getInt("id"));
        person.setName(json.getString("name"));
        person.setDepartment(json.getString("department"));
        person.setEmail(json.getString("email"));
        person.setPhone(json.getString("phone"));
        person.setPhoto(json.getString("photo"));

        return person;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
