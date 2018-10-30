package me.synology.hsbong.patientphotostorage.list;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.synology.hsbong.patientphotostorage.R;
import me.synology.hsbong.patientphotostorage.model.Board;
import me.synology.hsbong.patientphotostorage.model.Patient;

import java.util.ArrayList;
import java.util.List;


public class PhotoListFragment extends Fragment {

        private static final String ARG_COLUMN_COUNT = "column-count";
        private int mColumnCount = 1;


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
                List<Patient> list = new ArrayList<Patient>();
                    Patient patient1 = new Patient();
                    patient1.setId(123123);
                    patient1.setName("홍길동");
                    patient1.setName("2018.10.30");
                    patient1.setPhoto("");

                Patient patient2 = new Patient();
                patient2.setId(123321);
                patient2.setName("이돈석");
                patient2.setName("2018.10.29");
                patient2.setPhoto("");


                list.add(patient1);
                list.add(patient2);

                Context context = view.getContext();
                RecyclerView recyclerView = (RecyclerView) view;
                if (mColumnCount <= 1) {
                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
                } else {
                    recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
                }
                recyclerView.setAdapter(new MyPhotoListRecyclerViewAdapter(list, getContext()));
            }
            return view;
        }






        /**
         * This interface must be implemented by activities that contain this
         * fragment to allow an interaction in this fragment to be communicated
         * to the activity and potentially other fragments contained in that
         * activity.
         * <p/>
         * See the Android Training lesson <a href=
         * "http://developer.android.com/training/basics/fragments/communicating.html"
         * >Communicating with Other Fragments</a> for more information.
         */

    }
