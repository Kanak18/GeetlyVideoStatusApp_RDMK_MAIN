package geetly.lyrical.videostatusmaker.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import geetly.lyrical.videostatusmaker.R;
import geetly.lyrical.videostatusmaker.model.VideoviewModel;

import java.util.ArrayList;


public class NvHomeAdapter extends RecyclerView.Adapter<NvHomeAdapter.CustomViewHolder> {

    public Activity context;
    public OnItemClickListener listener;
    public ArrayList<VideoviewModel> dataList;


    public NvHomeAdapter(Activity context, ArrayList<VideoviewModel> dataList, OnItemClickListener listener) {
        this.context = context;
        this.dataList = dataList;
        this.listener = listener;

    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewtype) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_home, null);
        view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.MATCH_PARENT));
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    public void setDataList(ArrayList<VideoviewModel> dataListMe) {
        this.dataList = dataListMe;
        notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder holder, final int i) {
        final VideoviewModel item = dataList.get(i);


        holder.bind(i, item, listener);


    }

    public interface OnItemClickListener {
        void onItemClick(int positon, VideoviewModel item, View view);
    }

    public static class CustomViewHolder extends RecyclerView.ViewHolder {


        private ImageView share, download;

        private Button useNow;

        public CustomViewHolder(View view) {
            super(view);

            share = view.findViewById(R.id.share);
            download = view.findViewById(R.id.download);
            useNow = view.findViewById(R.id.useNow);

        }


        public void bind(final int postion, final VideoviewModel item, final OnItemClickListener listener) {


            download.setOnClickListener(v -> listener.onItemClick(postion, item, v));

            share.setOnClickListener(v -> listener.onItemClick(postion, item, v));

            useNow.setOnClickListener(v -> listener.onItemClick(postion, item, v));

        }

    }
}