package me.synology.hsbong.patientphotostorage.view;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.synology.hsbong.patientphotostorage.MainActivity;
import me.synology.hsbong.patientphotostorage.R;
import me.synology.hsbong.patientphotostorage.model.Person;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link MyInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyInfoFragment extends Fragment {
    private final static String TITLE = "내정보";

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    @BindView(R.id.imageViewMyProfile)
    ImageView _myProfile;
    @BindView(R.id.text_view_myaddress)
    TextView _myAddress;

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)getActivity()).getSupportActionBar().setTitle(this.TITLE);
    }

    @BindView(R.id.text_view_myemail)
    TextView _myEmail;
    @BindView(R.id.text_view_phone)
    TextView _myPhone;
    @BindView(R.id.text_view_myname)
    TextView _myName;

    private final String TAG = MyInfoFragment.class.getSimpleName();

    public MyInfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyInfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyInfoFragment newInstance(String param1, String param2) {
        MyInfoFragment fragment = new MyInfoFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_info, container, false);
        ButterKnife.bind(this, view);
        setView(mParam1,mParam2);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event



    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
private void setView(final String phone, final String deviceId) {
    RequestQueue rq = Volley.newRequestQueue(getContext());
    StringBuilder url = new StringBuilder();
    url.append("http://www.bacoder.kr/getPerson.jsp");
    url.append("?phone="+phone);
    url.append("&deviceId="+deviceId);

    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url.toString(), null,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d(TAG, response.toString());
                    try{
                        JSONObject myInfo = response;
                        Person person = Person.parsePerson(myInfo);
                        _myAddress.setText(person.getAddress());
                        _myEmail.setText(person.getEmail());
                        _myName.setText(person.getName());
                        _myPhone.setText(person.getPhone());
                        Picasso.with(getContext()).load(person.getPhoto()).placeholder(R.drawable.avatar).error(R.drawable.avatar).into(_myProfile);

                    }
                    catch (JSONException e){
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {

        }
    });
    rq.add(jsonObjectRequest);
}

}
