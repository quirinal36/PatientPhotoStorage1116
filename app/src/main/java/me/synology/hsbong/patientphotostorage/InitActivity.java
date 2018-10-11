package me.synology.hsbong.patientphotostorage;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;



public class InitActivity extends Activity {
    final int STORAGE_PERMISSION_CODE = 22;

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }

    /**
     * Called when the activity is first created.
     */
    Thread splashTread;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);

        requestPermission();

       // StartAnimations();
    }

    private void StartAnimations(final String phone) {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
        anim.reset();
        LinearLayout l = (LinearLayout) findViewById(R.id.lin_lay);
        l.clearAnimation();
        l.startAnimation(anim);

        anim = AnimationUtils.loadAnimation(this, R.anim.translate);
        anim.reset();
        ImageView iv = (ImageView) findViewById(R.id.splash);
        iv.clearAnimation();
        iv.startAnimation(anim);

          splashTread = new Thread() {
              @Override
              public void run() {

        final String url = "http://www.bacoder.kr/getPerson.jsp?phone=01026079765&password=12355";

        RequestQueue rq = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest JsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                // Log.d(TAG, response.toString());
                try {
                    int result = response.getInt("id");
                    if (result > 0) {
                        Toast.makeText(getBaseContext(), phone + " 으로 로그인 성공", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(InitActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                        InitActivity.this.finish();
                    } else {
                        Toast.makeText(getBaseContext(), "로그인 실패", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(InitActivity.this, SignupActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                        InitActivity.this.finish();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    InitActivity.this.finish();
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
     };

    Handler mHandler = new Handler();
        mHandler.postDelayed(new

    Runnable() {
        @Override
        public void run () {
            splashTread.start();
           // login();
        }
    },2000);

}

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == STORAGE_PERMISSION_CODE && (grantResults[0]==PackageManager.PERMISSION_GRANTED && grantResults[1]==PackageManager.PERMISSION_GRANTED)){
            String phone = EtcLib.getInstance().getPhoneNumber(this);

            StartAnimations(phone);
        }
        else{
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
            }
        }
    }

    private void requestPermission() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            String phone = EtcLib.getInstance().getPhoneNumber(this);

            StartAnimations(phone);
            return;
        }
    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE},
            STORAGE_PERMISSION_CODE);
}

}