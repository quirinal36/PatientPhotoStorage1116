package me.synology.hsbong.patientphotostorage.list;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import me.synology.hsbong.patientphotostorage.MainActivity;
import me.synology.hsbong.patientphotostorage.R;
import me.synology.hsbong.patientphotostorage.model.Photo;

import java.util.ArrayList;
import java.util.List;


public class PhotoListFragment extends Fragment {

    final static String TITLE = "포토목록";
    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final String ARG_PATIENT_ID = "patient_id";
    private int mColumnCount = 1;
    private String mPatientId = "";

    public static final String TAG = PhotoListFragment.class.getSimpleName();
    MyPhotoListRecyclerViewAdapter adapter;
    RecyclerView recyclerView;

    public PhotoListFragment() {
    }

    public static PhotoListFragment newInstance(int columnCount, String patientId) {
        PhotoListFragment fragment = new PhotoListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        args.putString(ARG_PATIENT_ID, patientId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
            mPatientId = getArguments().getString(ARG_PATIENT_ID);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photolist_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            // recyclerView.setAdapter(new MyPhotoListRecyclerViewAdapter(list, getContext()));
            updateAdapter(mPatientId);
        }
        return view;
    }

    private void updateAdapter(String patientId) {
        final List<Photo> list = new ArrayList<>();
        RequestQueue rq = Volley.newRequestQueue(getContext());
        StringBuilder url = new StringBuilder();
        url.append("http://www.bacoder.kr/getPhoto.jsp").append("?patientId=").append(patientId);
        StringRequest sr = new StringRequest(Request.Method.GET, url.toString(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response);
                        try {
                            JSONObject resultJson = new JSONObject(response);
                            JSONArray array = (JSONArray) resultJson.get("list");
                            //Log.d(TAG, "array leng: " + array.length());

                            for (int i = 0; i < array.length(); i++) {
                                JSONObject obj = new JSONObject(array.getString(i));

                                Photo photo = new Photo();
                                photo.setPatientId(obj.getInt("patientId"));
                                photo.setPhotoId(obj.getInt("id"));
                                photo.setPatientName(obj.getString("patientName"));
                                photo.setPhotoUrl(obj.getString("photoUrl").replaceAll("\\\\", ""));
                                photo.setDate(obj.getString("date"));

                                //Log.d(TAG, photo.getPhotoUrl().toString());
                                //Log.d(TAG, obj.getString("photoUrl").replaceAll("\\\\", ""));
                                list.add(photo);
                            }

                            if (list.size() < 1) {
                                Toast.makeText(getContext(), "검색 결과가 없습니다", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getContext(), "총 " + list.size() + "건의 정보를 찾았습니다.", Toast.LENGTH_LONG).show();
                            }

                            adapter = new MyPhotoListRecyclerViewAdapter(list, getContext());
                            recyclerView.setAdapter(adapter);
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void apaterReroad(String search) {
        Log.d(TAG, "apaterReroad: " + search);
        updateAdapter(search);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(this.TITLE);
    }

}
