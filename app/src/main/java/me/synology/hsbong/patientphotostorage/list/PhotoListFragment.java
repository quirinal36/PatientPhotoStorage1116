package me.synology.hsbong.patientphotostorage.list;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
        private int mColumnCount = 1;
        public static final String TAG = PhotoListFragment.class.getSimpleName();
        MyPhotoListRecyclerViewAdapter adapter;

        public PhotoListFragment() {
        }

        // TODO: Customize parameter initialization
        @SuppressWarnings("unused")
        public static me.synology.hsbong.patientphotostorage.list.PhotoListFragment newInstance(int columnCount) {
            me.synology.hsbong.patientphotostorage.list.PhotoListFragment fragment = new me.synology.hsbong.patientphotostorage.list.PhotoListFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_COLUMN_COUNT, columnCount);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            if (getArguments() != null) {
                mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
            }
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_photolist_list, container, false);

            // Set the adapter
            if (view instanceof RecyclerView) {


                Context context = view.getContext();
                RecyclerView recyclerView = (RecyclerView) view;
                if (mColumnCount <= 1) {
                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
                } else {
                    recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
                }
               // recyclerView.setAdapter(new MyPhotoListRecyclerViewAdapter(list, getContext()));
                updateAdapter(recyclerView);
            }
            return view;
        }

    private List<Photo> getDummyList(){
        List<Photo> list = new ArrayList<Photo>();
        Photo photo1 = new Photo();
        photo1.setPatientId(123123);
        photo1.setPatientName("홍길동");
        photo1.setDate("2018.10.30");
        photo1.setPhotoUrl("https://www.bloter.net/wp-content/uploads/2016/08/13239928_1604199256575494_4289308691415234194_n.jpg");
        photo1.setClassification("수술중");
        photo1.setDoctor("봉황세");
        photo1.setUploader("전담간호사");
        photo1.setComment("수술중 사진입니다.");

        Photo photo2 = new Photo();
        photo2.setPatientId(123456);
        photo2.setPatientName("아수라");
        photo2.setDate("2018.10.30");
        photo2.setPhotoUrl("https://www.bloter.net/wp-content/uploads/2016/08/13239928_1604199256575494_4289308691415234194_n.jpg");
        photo2.setClassification("드레싱");
        photo2.setDoctor("봉황세");
        photo2.setUploader("전담간호사");
        photo2.setComment("병동 드레싱 사진입니다.");

        Photo photo3 = new Photo();
        photo3.setPatientId(123456);
        photo3.setPatientName("김구라");
        photo3.setDate("2018.10.30");
        photo3.setPhotoUrl("https://www.bloter.net/wp-content/uploads/2016/08/13239928_1604199256575494_4289308691415234194_n.jpg");
        photo3.setClassification("드레싱");
        photo3.setDoctor("봉황세");
        photo3.setUploader("전담간호사");
        photo3.setComment("병동 드레싱 사진입니다.");

        list.add(photo1);
        list.add(photo2);
        list.add(photo3);
        list.add(photo2);
        list.add(photo2);
        list.add(photo2);
        list.add(photo2);
        list.add(photo2);
        list.add(photo2);
        list.add(photo2);
        list.add(photo2);
        return list;
    }

    private void updateAdapter(final RecyclerView recyclerView){

        final List<Photo> list = new ArrayList<>();
        RequestQueue rq = Volley.newRequestQueue(getContext());
        StringBuilder url = new StringBuilder();
        url.append("http://www.bacoder.kr/getPhoto.jsp?patientId=1");
        StringRequest sr = new StringRequest(Request.Method.GET, url.toString(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response);
                        try {
                            JSONObject resultJson = new JSONObject(response);
                            JSONArray array = (JSONArray) resultJson.get("list");
                            Log.d(TAG, "array leng: " + array.length());

                            for(int i=0; i<array.length(); i++){
                                JSONObject obj = new JSONObject(array.getString(i));

                                Photo photo = new Photo();
                                photo.setPatientId(obj.getInt("patientId"));
                                photo.setPhotoId(obj.getInt("id"));
                             //   photo.setPatientName(obj.getString("patientName"));
                                photo.setPhotoUrl(obj.getString("photoUrl").replaceAll("\\\\", ""));
                                photo.setDate(obj.getString("date"));

                                //Log.d(TAG, photo.toString());
                                //Log.d(TAG, obj.getString("photoUrl").replaceAll("\\\\", ""));
                                list.add(photo);
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
    public void onResume() {
        super.onResume();
        ((MainActivity)getActivity()).getSupportActionBar().setTitle(this.TITLE);
    }

    }
