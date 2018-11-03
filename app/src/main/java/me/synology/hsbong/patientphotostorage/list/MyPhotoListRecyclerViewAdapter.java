package me.synology.hsbong.patientphotostorage.list;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import me.synology.hsbong.patientphotostorage.MainActivity;
import me.synology.hsbong.patientphotostorage.R;
import me.synology.hsbong.patientphotostorage.model.Photo;
import me.synology.hsbong.patientphotostorage.view.PhotoDetailFragment;

import java.util.List;


public class MyPhotoListRecyclerViewAdapter extends RecyclerView.Adapter<MyPhotoListRecyclerViewAdapter.ViewHolder> implements View.OnClickListener{

        private final List<Photo> mValues;
        private final String TAG = this.getClass().getSimpleName();
        private Context mContext;

        public MyPhotoListRecyclerViewAdapter(List<Photo> items, Context context) {
            mValues = items;
            mContext = context;
            for(Photo photo : items){
                Log.d(TAG, photo.toString());
            }
        }

        @Override
        public me.synology.hsbong.patientphotostorage.list.MyPhotoListRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_photolist, parent, false);
            view.setOnClickListener(this);
            return new me.synology.hsbong.patientphotostorage.list.MyPhotoListRecyclerViewAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final me.synology.hsbong.patientphotostorage.list.MyPhotoListRecyclerViewAdapter.ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            holder.mView.setTag(mValues.get(position).getPhotoId());
            holder.mIdView.setText(String.valueOf(mValues.get(position).getPatientId()));
            holder.mNameView.setText(mValues.get(position).getPatientName());
            holder.mClassificationView.setText(mValues.get(position).getClassification());
            holder.mUploaderView.setText(mValues.get(position).getUploader());
            holder.mClassificationView.setText(mValues.get(position).getClassification());

            if(mValues.get(position).getPhotoUrl()!=null && mValues.get(position).getPhotoUrl().length()>0) {
                Picasso.with(mContext).load(mValues.get(position).getPhotoUrl()).placeholder(R.drawable.avatar).into(holder.mImageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(mContext, "성공", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError() {
                        Toast.makeText(mContext, "실패", Toast.LENGTH_SHORT).show();
                    }
                });
            }


        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

    @Override
    public void onClick(View view) {
        int id = (int)view.getTag();
        Log.d(TAG, "id : " + id);

                Fragment fragment = PhotoDetailFragment.newInstance(id);
                FragmentTransaction fragmentTransaction = ((MainActivity)mContext).getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content_main, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                /*
                fragment = new PhotoDetailFragment();
        Bundle args = new Bundle();
        int position = (int)view.getTag();
        args.putInt("id", mValues.get(position).getPhotoId());
        Log.d(TAG, "position : " + position);
        Log.d(TAG, "id: " + mValues.get(position).getPhotoId());
        fragment.setArguments(args);
*/
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mIdView;
            public final TextView mNameView;
            public final TextView mClassificationView;
            public final TextView mUploaderView;
            public final ImageView mImageView;
            public Photo mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mIdView = (TextView) view.findViewById(R.id.patientId_photo);
                mNameView = (TextView) view.findViewById(R.id.patientName_photo);
                mImageView = (ImageView) view.findViewById(R.id.photo_url);
                mClassificationView = (TextView) view.findViewById(R.id.classification_photo);
                mUploaderView = (TextView) view.findViewById(R.id.uploader_photo);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mNameView.getText() + "'";
            }
        }
    }