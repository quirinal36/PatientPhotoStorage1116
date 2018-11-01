package me.synology.hsbong.patientphotostorage.list;

import android.content.Context;
import android.media.Image;
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

import me.synology.hsbong.patientphotostorage.R;
import me.synology.hsbong.patientphotostorage.model.Photo;

import java.util.List;


public class MyPhotoListRecyclerViewAdapter extends RecyclerView.Adapter<MyPhotoListRecyclerViewAdapter.ViewHolder> {

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
            return new me.synology.hsbong.patientphotostorage.list.MyPhotoListRecyclerViewAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final me.synology.hsbong.patientphotostorage.list.MyPhotoListRecyclerViewAdapter.ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);

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

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, holder.mIdView.getText().toString());
                    Toast.makeText(mContext, holder.mIdView.getText().toString(), Toast.LENGTH_LONG).show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
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