package me.synology.hsbong.patientphotostorage.list;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import me.synology.hsbong.patientphotostorage.R;
import me.synology.hsbong.patientphotostorage.model.Board;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Board} and makes a call to the
 * TODO: Replace the implementation with code for your data type.
 */
public class MyBoardRecyclerViewAdapter extends RecyclerView.Adapter<MyBoardRecyclerViewAdapter.ViewHolder> {

    private final List<Board> mValues;
    private final String TAG = this.getClass().getSimpleName();
    private Context mContext;

    public MyBoardRecyclerViewAdapter(List<Board> items, Context context) {
        mValues = items;
        mContext = context;
        for(Board board : items){
            Log.d(TAG, board.toString());
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_board, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(String.valueOf(mValues.get(position).getId()));

        holder.mContentView.setText(mValues.get(position).getTitle());
        holder.mWriter.setText(mValues.get(position).getWriter());

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
        public final TextView mContentView;
        public final TextView mWriter;
        public Board mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.board_number);
            mContentView = (TextView) view.findViewById(R.id.board_title);
            mWriter = (TextView) view.findViewById(R.id.board_writer);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
