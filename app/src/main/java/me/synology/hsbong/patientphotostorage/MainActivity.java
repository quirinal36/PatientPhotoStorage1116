package me.synology.hsbong.patientphotostorage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.synology.hsbong.patientphotostorage.sign.SignupFragment;
import me.synology.hsbong.patientphotostorage.view.MyInfoFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

   // @BindView(R.id.userName) TextView _userName;




    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        context = getApplicationContext();

    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        setProfile();


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Fragment fragment = null;
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
           // Intent intent = new Intent(context, LoginActivity.class);
           // startActivity(intent);

        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {
            SharedPreferences pref = getSharedPreferences("bacoder", MODE_PRIVATE);
            String phone = EtcLib.getInstance().getPhoneNumber(this);

            fragment = new MyInfoFragment();
            Bundle args = new Bundle();
            args.putString("param1", phone);
            args.putString("param2", pref.getString("device_uuid",""));
            fragment.setArguments(args);

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {
         //   fragment = new SignupFragment();

        }

        if(fragment != null) {
            fragmentTransaction.replace(R.id.content_main, fragment);
            fragmentTransaction.commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);


        return true;
    }

    public void setProfile() {
        TextView textView = (TextView) findViewById(R.id.userName);
        textView.setText(((MyApp) getApplicationContext()).getUserInfo("name"));
        TextView textView2 = (TextView) findViewById(R.id.department);
        textView2.setText(((MyApp) getApplicationContext()).getUserInfo("phone"));

        ImageView imageView = (ImageView) findViewById(R.id.profileIcon);
        ((MyApp) getApplicationContext()).getUserInfo("profilePhoto");

        Picasso.with(getApplicationContext()).load(((MyApp) getApplicationContext()).getUserInfo("profilePhoto")).placeholder(R.drawable.avatar).error(R.drawable.avatar).into(imageView);
        imageView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                SharedPreferences pref = getSharedPreferences("bacoder", MODE_PRIVATE);
                String phone = EtcLib.getInstance().getPhoneNumber(getApplicationContext());

                Fragment fragment = null;
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragment = new MyInfoFragment();
                Bundle args = new Bundle();
                args.putString("param1", phone);
                args.putString("param2", pref.getString("device_uuid",""));
                fragment.setArguments(args);
                fragmentTransaction.replace(R.id.content_main, fragment);
                fragmentTransaction.commit();

              DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
              drawer.closeDrawer(GravityCompat.START);
            }
        });
    }
}
