package me.synology.hsbong.patientphotostorage.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Patient {
    private int patientId;
    private String name;
    private String doctor;
    private String birth;
    private String sex;
    private String phone;
    private String address;
    private String memo;
    private String photo;
    private String room;
    private boolean admission;

    public boolean isAdmission() {
        return admission;
    }

    public void setAdmission(boolean admission) {
        this.admission = admission;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }


    public int getPatientId() {
        return patientId;
    }
    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }
    public String getPhoto() {
        return photo;
    }
    public void setPhoto(String photo) {
        this.photo = photo;
    }


    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
    public static Patient parseTo(JSONObject input) throws JSONException {
        Patient result = new Patient();
        if(input.has("id")){
            result.setPatientId(input.getInt("id"));
        }
        if(input.has("photo")){
            result.setPhoto(input.getString("photo"));
        }
        if(input.has("name")){
            result.setName(input.getString("name"));
        }

        return result;
    }

}