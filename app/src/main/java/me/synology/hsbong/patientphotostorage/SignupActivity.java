package me.synology.hsbong.patientphotostorage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";
    private ArrayAdapter adapter;
    private Spinner spinner;
    private String phoneId;

    @BindView(R.id.input_name)
    EditText _nameText;
    @BindView(R.id.input_address) EditText _addressText;
    @BindView(R.id.input_email) EditText _emailText;
    @BindView(R.id.input_mobile) EditText _mobileText;
    @BindView(R.id.input_password) EditText _passwordText;
    @BindView(R.id.input_reEnterPassword) EditText _reEnterPasswordText;
    @BindView(R.id.btn_signup) Button _signupButton;
    @BindView(R.id.link_login) TextView _loginLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        phoneId = EtcLib.getInstance().getPhoneNumber(this);

        TextView textView = (TextView) findViewById(R.id.deviceId);
        textView.setText(phoneId);

        spinner = (Spinner) findViewById(R.id.departmentSpinner);
        adapter = ArrayAdapter.createFromResource(this, R.array.department, android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }

    @OnClick(R.id.btn_signup)
    public void signup() {
        Log.d(TAG, "Signup");

 //       If (!validate()) {
  //          onSignupFailed();
   //         return;
  //      }

        _signupButton.setEnabled(false);

        String name = _nameText.getText().toString();
        String address = _addressText.getText().toString();
        String email = _emailText.getText().toString();
        String mobile = _mobileText.getText().toString();
        String password = _passwordText.getText().toString();
        final String reEnterPassword = _reEnterPasswordText.getText().toString();

        RequestQueue rq = Volley.newRequestQueue(getApplicationContext());
        StringBuilder url = new StringBuilder();
        url.append("http://www.bacoder.kr/addPerson.jsp?");
        url.append("&name="+name);
        url.append("&password="+password);
        url.append("&address="+address);
        url.append("&email="+email);
        url.append("&phone="+mobile);

        Log.d(TAG, url.toString());

        JsonObjectRequest JsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url.toString(), null, new Response.Listener<JSONObject>(){

               @Override
               public void onResponse(JSONObject response) {
                    Log.d(TAG, response.toString());
                    try {
                        int result = (int)response.get("result");
                        if (result >0) {
                            Log.d(TAG, "성공");

                            //성공
                        }
                        else {
                            //실패
                        }

                    }
                    catch(JSONException e) {
                        e.printStackTrace();

                   }

               }
            },
            new Response.ErrorListener(){

                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            rq.add(JsonObjectRequest);
    }

}
