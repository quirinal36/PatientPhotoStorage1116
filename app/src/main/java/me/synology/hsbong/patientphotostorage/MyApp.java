package me.synology.hsbong.patientphotostorage;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import me.synology.hsbong.patientphotostorage.model.Person;


/**
 * Created by bongh on 2018-10-13.
 */

public class MyApp extends Application {
    private Person person;

    @Override
    public void onCreate() {
        super.onCreate();

    }

    public Person getPerson() {
        if (person == null) person = new Person();

        return person;
    }

    public void setPerson(Person item) {

        this.person = item;
    }

    public String getUserInfo(String name) {
        SharedPreferences pref = getSharedPreferences("bacoder", MODE_PRIVATE);
        return pref.getString(name,"");
    }

    public void setUserInfo(String key, String value) {
        SharedPreferences pref = getSharedPreferences("bacoder", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public void setAllUserInfo(){
        String phone = EtcLib.getInstance().getPhoneNumber(this);
        final SharedPreferences pref = getSharedPreferences("bacoder", MODE_PRIVATE);

        StringBuilder url = new StringBuilder();
        url.append(getString(R.string.server_address)).append("/getPerson.jsp")
                .append("?phone=").append(phone)
                .append("&deviceId=").append(pref.getString("device_uuid",""));


        RequestQueue rq = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest JsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url.toString(), null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("setAllUserInfo", response.toString());
                try {
                    String userName = response.getString("name");
                    String userPhone = response.getString("phone");
                    String userEmail = response.getString("email");
                    String userPhoto = response.getString("photo");
                    String userDepartment = response.getString("department");

                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("name", userName);
                    editor.putString("phone", userPhone);
                    editor.putString("email", userEmail);
                    editor.putString("profilePhoto", userPhoto);
                    editor.putString("department", userDepartment);

                    editor.commit();


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
        rq.add(JsonObjectRequest);

    }


}
