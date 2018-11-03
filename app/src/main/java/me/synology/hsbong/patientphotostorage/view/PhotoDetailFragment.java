package me.synology.hsbong.patientphotostorage.view;

import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import me.synology.hsbong.patientphotostorage.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PhotoDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PhotoDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */


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


    private static final String ARG_PARAM1 = "id";

    private String mParam1;

    public PhotoDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment PhotoDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PhotoDetailFragment newInstance(int param1) {
        PhotoDetailFragment fragment = new PhotoDetailFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_photo_detail, container, false);
    }



    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
