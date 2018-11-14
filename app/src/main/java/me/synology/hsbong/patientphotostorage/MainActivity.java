package me.synology.hsbong.patientphotostorage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.synology.hsbong.patientphotostorage.list.BoardFragment;
import me.synology.hsbong.patientphotostorage.list.PatientListFragment;
import me.synology.hsbong.patientphotostorage.list.PhotoListFragment;
import me.synology.hsbong.patientphotostorage.sign.SignupFragment;
import me.synology.hsbong.patientphotostorage.util.FileUtil;
import me.synology.hsbong.patientphotostorage.view.MyInfoFragment;
import me.synology.hsbong.patientphotostorage.view.PhotoUploadFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private String mCurrentPhotoPath = null;
    private Uri mCapturedImageURI = null;

    private final String TAG = MainActivity.class.getSimpleName();
    private final int MAIN_REQ_CODE = 1108;

    private IntentIntegrator qrScan;


    File imageFile;
    String imageFileName;
    boolean afterCapture = false;

    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
/*
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setIcon(R.drawable.ic_action_search);

        LayoutInflater inflator = (LayoutInflater) this .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.search, null);
        actionBar.setCustomView(v);
*/

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_main);

                imageFileName = String.valueOf(System.currentTimeMillis());
                imageFile = FileUtil.getInstance().getImageFile(context, imageFileName);
                capturePhoto(imageFile);
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

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Fragment fragment = new PatientListFragment();
        fragmentTransaction.replace(R.id.content_main , fragment);
        fragmentTransaction.commit();

    }

    public void capturePhoto(File imageFile) {
        Uri uri;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            uri = FileProvider.getUriForFile(this, "me.synology.hsbong.patientphotostorage.fileprovider", imageFile);
        }
        else{
            uri = Uri.fromFile(imageFile);
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.addFlags(intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(intent, MAIN_REQ_CODE);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Log.d(TAG, "count: " + getSupportFragmentManager().getBackStackEntryCount());
            if(getSupportFragmentManager().getBackStackEntryCount() == 0){
                showFinishDialog();
                return;
            }
            super.onBackPressed();
        }
    }

    private void showFinishDialog(){
        new MaterialDialog.Builder(this)
                .title("종료")
                .content("앱을 종료할까요?")
                .positiveText("확인")
                .negativeText("취소")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        finish();
                    }
                })
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menu.clear();
        getMenuInflater().inflate(R.menu.main, menu);
        setProfile();


        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = new SearchView(this.getSupportActionBar().getThemedContext());
        /*
        MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW |
                MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
        MenuItemCompat.setActionView(item, searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(getApplicationContext(), query, Toast.LENGTH_LONG).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        */
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });



        return true;
    }

    private String searchInput = "";
    private void searchDialog() {

        new MaterialDialog.Builder(this)
                .title("검색")
                .inputType(InputType.TYPE_CLASS_TEXT)
                .input("등록번호", null, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        searchInput = input.toString();
                    }
                })
                .show();
    }


    @BindView(R.id.search_content)
    EditText searchContent;
    @BindView(R.id.search_button)
    ImageButton searchButton;


    private void searchCustomDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.search_dialog, null);
        builder.setView(layout);


        builder.create().show();

        ImageButton searchButton = (ImageButton) layout.findViewById(R.id.search_button);
        ImageButton imageButton = (ImageButton) layout.findViewById(R.id.qr_code_search);

        searchButton.setOnClickListener(this);
        imageButton.setOnClickListener(this);

        searchContent = (EditText) layout.findViewById(R.id.search_content);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.search_button) {
            Toast.makeText(this,"search", Toast.LENGTH_LONG).show();
        }
        if (id == R.id.qr_code_search) {
            Toast.makeText(this,"qrcode", Toast.LENGTH_LONG).show();
            qrScan = new IntentIntegrator(this);
            qrScan.initiateScan();
        }

    }





    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            Log.d(TAG, "searchDialog");
           // searchDialog();
            searchCustomDialog();
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

        if (id == R.id.nav_list) {
            // Handle the camera action
           // Intent intent = new Intent(context, LoginActivity.class);
           // startActivity(intent);
            fragment = new PhotoListFragment();

        } else if (id == R.id.nav_register) {
            fragment = new PhotoUploadFragment();

        } else if (id == R.id.nav_emergency) {
            fragment = new BoardFragment();

        } else if (id == R.id.nav_bookmark) {


        } else if (id == R.id.nav_board) {
            //fragment = new PhotoUploadFragment();
        } else if (id == R.id.nav_setting) {
            SharedPreferences pref = getSharedPreferences("bacoder", MODE_PRIVATE);
            String phone = EtcLib.getInstance().getPhoneNumber(this);

            fragment = new MyInfoFragment();
            Bundle args = new Bundle();
            args.putString("param1", phone);
            args.putString("param2", pref.getString("device_uuid",""));
            fragment.setArguments(args);

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == MAIN_REQ_CODE){
            afterCapture = true;

            Log.d(TAG, "imageFile: "+ imageFileName);

        } else if (requestCode == IntentIntegrator.REQUEST_CODE) {
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else if(result != null){
                //if qr contains data
                try {
                    //converting the data to json
                    JSONObject obj = new JSONObject(result.getContents());
                    //setting values to editText
                    Log.d(TAG, obj.getString("patientId"));
                    searchContent.setText(obj.getString("patientId"));

                } catch (JSONException e) {
                    e.printStackTrace();
                    //if control comes here
                    //that means the encoded format not matches
                    //in this case you can display whatever data is available on the qrcode
                    //to a toast
                    Toast.makeText(this, "다른 형식의 코드 입니다 : "+result.getContents(), Toast.LENGTH_LONG).show();
                }
            }
        }

        else {
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_main);
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }



    @Override
    protected void onResume() {
        super.onResume();
        if(afterCapture){
            try {
                Fragment fragment = PhotoUploadFragment.newInstance(imageFile.getCanonicalPath(), null);
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content_main, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                afterCapture = false;
            }
            catch (IOException e){

            }
        }
    }

    /**
     * Getters and setters.
     */

    public String getCurrentPhotoPath() {
        return mCurrentPhotoPath;
    }

    public void setCurrentPhotoPath(String mCurrentPhotoPath) {
        this.mCurrentPhotoPath = mCurrentPhotoPath;
    }

    public Uri getCapturedImageURI() {
        return mCapturedImageURI;
    }

    public void setCapturedImageURI(Uri mCapturedImageURI) {
        this.mCapturedImageURI = mCapturedImageURI;
    }
}
