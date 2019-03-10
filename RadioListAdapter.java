package sound.chill.com.mychilloutplayer;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class RadioListAdapter extends   RecyclerView.Adapter<RadioListAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    private ArrayList<DataModel> dataSet;
    private CustomItemClickListener listener;

    RadioListAdapter(Context context, ArrayList<DataModel> data, CustomItemClickListener customItemClickListener) {
        mInflater = LayoutInflater.from(context);
        this.dataSet = data;
        this.listener = customItemClickListener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        final View mItemView = mInflater.inflate(R.layout.list_of_radio_stations, parent, false);
        return new ViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.wordItemView.setText(dataSet.get(position).name);
        holder.imageView.setImageResource(dataSet.get(position).getImage());

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public final ImageView imageView;
        final TextView wordItemView;
        final RadioListAdapter mAdapter;

        ViewHolder(View itemView, RadioListAdapter adapter) {
            super(itemView);
            wordItemView = itemView.findViewById(R.id.listTextView);
            imageView = itemView.findViewById(R.id.listImageView);
            itemView.setOnClickListener(this);
            this.mAdapter = adapter;
        }

        @Override
        public void onClick(View view) {
        listener.onItemClick(view, getAdapterPosition());
        }
    }

    public interface CustomItemClickListener {
        void onItemClick(View v, int position);
    }
}

