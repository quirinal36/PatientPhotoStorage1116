package me.synology.hsbong.patientphotostorage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SplashActivity extends AppCompatActivity {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private View mControlsView;
    private boolean mVisible;

    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     //   setTheme(R.style.AppTheme_Dark);
        setContentView(R.layout.activity_splash);

        mVisible = true;
        mContentView = findViewById(R.id.fullscreen_content);
        login();

    }

    private void login() {
        final String url = "http://www.bacoder.kr/getPerson.jsp?name=test&password=111111";

        RequestQueue rq = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest JsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>(){

            @Override
            public void onResponse(JSONObject response) {
                // Log.d(TAG, response.toString());
                try {
                    int result = (int) response.get("result");
                    if (result > 0) {
                        // Log.d(TAG, "성공");
                        Toast.makeText(getBaseContext(), "로그인 성공", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getBaseContext(), "로그인 실패", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(SplashActivity.this, SignupActivity.class);
                        startActivity(intent);
                    }

                } catch (JSONException e) {
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