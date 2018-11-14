package me.synology.hsbong.patientphotostorage.view;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.synology.hsbong.patientphotostorage.MainActivity;
import me.synology.hsbong.patientphotostorage.R;
import me.synology.hsbong.patientphotostorage.list.PatientListFragment;
import me.synology.hsbong.patientphotostorage.list.PhotoListFragment;
import me.synology.hsbong.patientphotostorage.util.FileUtil;


/**
 * A simple {@link Fragment} subclass.
 * to handle interaction events.
 * Use the {@link PhotoUploadFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PhotoUploadFragment extends Fragment {

    @BindView(R.id.sendPhotoInfoButton)
    Button _sendPhotoInfoButton;
    @BindView(R.id.buttonUploadPhoto)
    Button _uploadImgButton;
    @BindView(R.id.photoUploadView)
    ImageView _uploadImageView;

    @BindView(R.id.upload_patientId)
    EditText _uploadPatientId;
    @BindView(R.id.upload_patientName)
    EditText _uploadPatientName;
    @BindView(R.id.classification_Spinner)
    Spinner _uploadClassification;
    @BindView(R.id.upload_doctor)
    Spinner _uploadDoctor;

    @BindView(R.id.upload_uploader)
    EditText _uploadUploader;

    @BindView(R.id.qr_Button)
    Button qrButton;

    @BindView(R.id.button_Capture_Photo)
    Button buttonCapturePhoto;

    final static String TITLE = "사진올리기";
    private final int REQ_CODE = 1101;
    private final int PICK_FROM_CAMERA = 1106;

    File imageFile;
    String imageFileName;

    private ArrayAdapter adapter;
    private ArrayAdapter adapter2;


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private static final String TAG = "PhotoUploadFragment";

    private IntentIntegrator qrScan;


    public PhotoUploadFragment() {

        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SignupFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PhotoUploadFragment newInstance(String param1, String param2) {
        PhotoUploadFragment fragment = new PhotoUploadFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        Log.d(TAG, "mParam1: "+mParam1);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imageFileName = String.valueOf(System.currentTimeMillis());
        imageFile = FileUtil.getInstance().getImageFile(getContext(), imageFileName);
    }

    @Deprecated
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_photo_upload, container, false);
        ButterKnife.bind(this, view);

        if(mParam1!=null && mParam1.length()>0){
            Picasso.with(getContext()).load(new File(mParam1)).into(_uploadImageView);
        }

        adapter = ArrayAdapter.createFromResource(getActivity(), R.array.classification, android.R.layout.simple_spinner_dropdown_item);
        _uploadClassification.setAdapter(adapter);
        adapter2 = ArrayAdapter.createFromResource(getActivity(), R.array.doctor, android.R.layout.simple_spinner_dropdown_item);
        _uploadDoctor.setAdapter(adapter2);

        return view;
    }

    @OnClick(R.id.qr_Button)
    public void qrButtonClick() {
        qrScan = new IntentIntegrator(getActivity());
        qrScan.initiateScan();
    }

    @OnClick(R.id.button_Capture_Photo)
    public void capturePhoto() {
        Uri uri;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            uri = FileProvider.getUriForFile(getContext(), "me.synology.hsbong.patientphotostorage.fileprovider", imageFile);
        }
        else{
            uri = Uri.fromFile(imageFile);
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.addFlags(intent.FLAG_GRANT_READ_URI_PERMISSION);
        getActivity().startActivityForResult(intent, PICK_FROM_CAMERA);
    }

    @OnClick(R.id.buttonUploadPhoto)
    public void openImgPick() {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        getActivity().startActivityForResult(i, REQ_CODE);
    }
    public void onSignupFailed(){
        Log.d(TAG, "please fill edit texts");
    }

    @OnClick(R.id.sendPhotoInfoButton)
    public void signup() {

        _sendPhotoInfoButton.setEnabled(false);

        final String signupPage = getString(R.string.server_address) + "/addPhoto.jsp";

        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, signupPage,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // onSignupSuccess();
                        try {
                            Log.d(TAG, response.toString());

                            JSONObject json = new JSONObject(response);
                            int result = json.getInt("result");
                            if (result > 0) {
                                // 성공

                                Toast.makeText(getContext(), "성공", Toast.LENGTH_LONG).show();
                                FragmentTransaction fragmentTransaction = ((MainActivity)getActivity()).getSupportFragmentManager().beginTransaction();
                                fragmentTransaction.replace(R.id.content_main, new PhotoListFragment());
                                fragmentTransaction.commit();
                            } else {
                                // 실패
                                Toast.makeText(getContext(), "실패", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "error listener");
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
               // SharedPreferences pref = getActivity().getSharedPreferences(getString(R.string.sharedpreference_name), Context.MODE_PRIVATE);
               // String unique = pref.getString(getString(R.string.device_uuid), "");
                Map<String, String> param = new HashMap<>();

                param.put("patientId", _uploadPatientId.getText().toString());
                Log.d(TAG, _uploadPatientId.getText().toString());
                param.put("patientName", _uploadPatientName.getText().toString());
                param.put("classification", _uploadClassification.getSelectedItem().toString());
                param.put("doctor", _uploadDoctor.getSelectedItem().toString());
                param.put("uploader", _uploadUploader.getText().toString());
              //  param.put("date", _uploadDate.getText().toString());
                //  param.put("comment", _uploadPatientId.getText().toString());



                if(_uploadImageView.getTag() != null) {
                    param.put("filename", "upload.jpg");
                    param.put("image", _uploadImageView.getTag().toString());
                }

                return param;
            }
        };
        RequestQueue rq = Volley.newRequestQueue(getContext());
        rq.add(jsonObjectRequest);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
       // super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult" + requestCode);

        if (requestCode == IntentIntegrator.REQUEST_CODE) {
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (result.getContents() == null) {
                Toast.makeText(getContext(), "Result Not Found", Toast.LENGTH_LONG).show();
            } else if(result != null){
                //if qr contains data
                try {
                    //converting the data to json
                     JSONObject obj = new JSONObject(result.getContents());
                    //setting values to editText
                    _uploadPatientId.setText(obj.getString("patientId"));

                } catch (JSONException e) {
                    e.printStackTrace();
                    //if control comes here
                    //that means the encoded format not matches
                    //in this case you can display whatever data is available on the qrcode
                    //to a toast
                    Toast.makeText(getContext(), "다른 형식의 코드 입니다 : "+result.getContents(), Toast.LENGTH_LONG).show();
                }
            }
        } else if (requestCode == REQ_CODE && resultCode == Activity.RESULT_OK && data != null) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                byte bb[] = bytes.toByteArray();
                String file = Base64.encodeToString(bb, Base64.DEFAULT);

                _uploadImageView.setImageBitmap(bitmap);
                _uploadImageView.setTag(file);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e){
                e.printStackTrace();
            }
        } else if (requestCode == PICK_FROM_CAMERA && resultCode == Activity.RESULT_OK ){
            Picasso.with(getContext()).load(imageFile).into(_uploadImageView);

        }

    }



    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)getActivity()).getSupportActionBar().setTitle(this.TITLE);
    }
}