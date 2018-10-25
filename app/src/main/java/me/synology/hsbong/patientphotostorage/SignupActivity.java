package me.synology.hsbong.patientphotostorage;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";
    private ArrayAdapter adapter;
    private String phoneId;

    @BindView(R.id.input_name) EditText _nameText;
    @BindView(R.id.input_email) EditText _emailText;
    @BindView(R.id.input_mobile) EditText _mobileText;
    @BindView(R.id.departmentSpinner) Spinner _departmentSpinner;
    @BindView(R.id.btn_signup) Button _signupButton;
    @BindView(R.id.link_login) TextView _loginLink;
    @BindView(R.id.buttonUploadImage) Button _uploadImageButton;
    @BindView(R.id.imageUploadView) ImageView _uploadImageView;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == Activity.RESULT_OK && data != null) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                byte bb[] = bytes.toByteArray();
                String file = Base64.encodeToString(bb, Base64.DEFAULT);

                _uploadImageView.setImageBitmap(bitmap);
                _uploadImageView.setTag(file);
            }
            catch(IOException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        phoneId = EtcLib.getInstance().getPhoneNumber(this);

        TextView textView = (TextView) findViewById(R.id.deviceId);
        textView.setText(phoneId);

//        _departmentSpinner = (Spinner) findViewById(R.id.departmentSpinner);
        adapter = ArrayAdapter.createFromResource(this, R.array.department, android.R.layout.simple_spinner_dropdown_item);
        _departmentSpinner.setAdapter(adapter);


   /*     _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    */

    }

    @OnClick(R.id.buttonUploadImage)
    public void openImgPick() {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, 100);
    }

    @OnClick(R.id.btn_signup)
    public void signup() {
        Log.d(TAG, "Signup");

 //       If (!validate()) {
  //          onSignupFailed();
   //         return;
  //      }

        _signupButton.setEnabled(false);

        final String signupPage = getString(R.string.server_address) + "/signup.jsp";

        StringRequest JsonObjectRequest = new StringRequest(Request.Method.POST, signupPage, new Response.Listener<String>(){

               @Override
               public void onResponse(String response) {
                    Log.d(TAG, response.toString());
                    try {
                        JSONObject json = new JSONObject(response);
                        int result = json.getInt("result");
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
            }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                SharedPreferences pref = getSharedPreferences("bacoder", MODE_PRIVATE);
                String unique = pref.getString("device_uuid", "");
                Map<String, String> param = new HashMap<>();
                String name = _nameText.getText().toString();
                String email = _emailText.getText().toString();
                String mobile = _mobileText.getText().toString();
                String department = _departmentSpinner.getSelectedItem().toString();

                param.put("name", name);
                param.put("email", email);
                param.put("phone", mobile);
                param.put("unique_id", unique);
                param.put("department", department);

                JSONObject json = new JSONObject(param);
                Log.d(TAG, json.toString());

                if (_uploadImageView.getTag() != null) {
                    param.put("filename", "upload.jpg");
                    param.put("image", _uploadImageView.getTag().toString());
                }

                return param;
            }
        };
            RequestQueue rq = Volley.newRequestQueue(this);
            rq.add(JsonObjectRequest);
    }

}
