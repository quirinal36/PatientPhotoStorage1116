package me.synology.hsbong.patientphotostorage.view;

import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.synology.hsbong.patientphotostorage.R;
import me.synology.hsbong.patientphotostorage.list.MyPhotoListRecyclerViewAdapter;
import me.synology.hsbong.patientphotostorage.list.PhotoListFragment;
import me.synology.hsbong.patientphotostorage.model.Photo;
import me.synology.hsbong.patientphotostorage.model.PhotoDetail;


public class PhotoDetailFragment extends Fragment {

    @BindView(R.id.detail_photo)
    ImageView detailPhoto;
    @BindView(R.id.detail_classification)
    TextView detailClassification;
    @BindView(R.id.detail_doctor)
    TextView detailDoctor;
    @BindView(R.id.detail_date)
    TextView detailDate;
    @BindView(R.id.detail_uploader)
    TextView detailUploader;
    @BindView(R.id.detail_comment)
    TextView detailComment;
    @BindView(R.id.detail_patientId)
    TextView detailPatientId;
    @BindView(R.id.detail_patientName)
    TextView detailPatientName;
    @BindView(R.id.detail_patientBirth)
    TextView detailPatientBirth;
    @BindView(R.id.detail_patientSex)
    TextView detailPatientSex;
    @BindView(R.id.detail_patientAddress)
    TextView detailPatientAddress;
    @BindView(R.id.detail_patientPhone)
    TextView detailPatientPhone;
    @BindView(R.id.detail_patientEtc)
    TextView detailPatientEtc;
    @BindView(R.id.detail_patient_button)
    Button detailPatientButton;
    @BindView(R.id.detail_photo_button)
    Button detailPhotoButton;
    @BindView(R.id.detail_comment_button)
    Button detailCommentButton;
    @BindView(R.id.detail_list_button)
    Button detailListButton;
    @BindView(R.id.detail_del_button)
    Button detailDelButton;

    public static final String TAG = PhotoDetailFragment.class.getSimpleName();
    private static final String ARG_PARAM1 = "id";

    private String mParam1;

    public PhotoDetailFragment() {
        // Required empty public constructor
    }


    public static PhotoDetailFragment newInstance(int param1) {
        PhotoDetailFragment fragment = new PhotoDetailFragment();
        Bundle args = new Bundle();
        String param1s = String.valueOf(param1);
        args.putString(ARG_PARAM1, param1s);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        //   loadDetail(mParam1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo_detail, container, false);
        ButterKnife.bind(this, view);

        mParam1 = getArguments().getString(ARG_PARAM1);
        loadDetail(mParam1);

        return view;
    }

    /*
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_photo_detail, container, false);
    }
*/

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void loadDetail(String id){

        RequestQueue rq = Volley.newRequestQueue(getContext());
        StringBuilder url = new StringBuilder();
        url.append("http://www.bacoder.kr/getPhotoById.jsp?id=").append(id);
        Log.d(TAG, "url : "+ url);
        StringRequest sr = new StringRequest(Request.Method.GET, url.toString(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response);
                        try {
                                JSONObject obj = new JSONObject(response);


                            Picasso.with(getContext()).load(obj.getString("photoUrl")).placeholder(R.drawable.avatar).error(R.drawable.avatar).into(detailPhoto);
                            detailClassification.setText(obj.getString("classification"));
                            detailDoctor.setText(obj.getString("doctor"));
                            detailDate.setText(obj.getString("date"));
                            detailUploader.setText(obj.getString("uploader"));
                            detailComment.setText(obj.getString("comment"));
                            detailPatientId.setText(obj.getString("patientId"));
                            detailPatientBirth.setText(obj.getString("patientBirth"));
                            detailPatientName.setText(obj.getString("patientName"));
                            detailPatientSex.setText(obj.getString("patientSex"));
                            detailPatientPhone.setText(obj.getString("patientPhone"));
                            detailPatientAddress.setText(obj.getString("patientAddress"));
                            detailPatientEtc.setText(obj.getString("patientEtc"));


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        rq.add(sr);
    }

}
