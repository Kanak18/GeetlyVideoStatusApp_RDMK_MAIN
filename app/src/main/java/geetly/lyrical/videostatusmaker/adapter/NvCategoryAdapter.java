package geetly.lyrical.videostatusmaker.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.squareup.picasso.Picasso;

import geetly.lyrical.videostatusmaker.R;
import geetly.lyrical.videostatusmaker.model.ModelCategoryImgage;

import java.util.ArrayList;

public class NvCategoryAdapter extends Adapter<NvCategoryAdapter.MyViewHolder> {
    public int intPos = 0;
    Context context;
    ArrayList<ModelCategoryImgage> modelCategoryImgages;
    MyClickListener mListener;

    public NvCategoryAdapter(Context context, ArrayList<ModelCategoryImgage> arrayList) {
        this.context = context;
        this.modelCategoryImgages = arrayList;
    }

    public void setOnItemClickListener(MyClickListener mListener) {

        this.mListener = mListener;

    }

    public int getItemCount() {
        return this.modelCategoryImgages.size();
    }

    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, @SuppressLint("RecyclerView") final int i) {

        final ModelCategoryImgage modelCategoryImgage = modelCategoryImgages.get(i);

       /* if (i == 0) {
            myViewHolder.imageView.setBackgroundResource(R.drawable.gradient1);
        }else if(i == 1){
            myViewHolder.imageView.setBackgroundResource(R.drawable.gradient2);
        }else if(i == 2){
            myViewHolder.imageView.setBackgroundResource(R.drawable.gradient3);
        }else if(i == 3){
            myViewHolder.imageView.setBackgroundResource(R.drawable.gradient4);
        }else if(i == 4){
            myViewHolder.imageView.setBackgroundResource(R.drawable.gradient5);
        }else if(i == 5){
            myViewHolder.imageView.setBackgroundResource(R.drawable.gradient6);
        }else if(i == 6){
            myViewHolder.imageView.setBackgroundResource(R.drawable.gradient1);
        }else if(i == 7){
            myViewHolder.imageView.setBackgroundResource(R.drawable.gradient2);
        }else if(i == 8){
            myViewHolder.imageView.setBackgroundResource(R.drawable.gradient3);
        }else if(i == 9){
            myViewHolder.imageView.setBackgroundResource(R.drawable.gradient4);
        }else if(i == 10){
            myViewHolder.imageView.setBackgroundResource(R.drawable.gradient5);
        }*/

        myViewHolder.textView.setText(modelCategoryImgage.getCategory());

        if (!TextUtils.isEmpty(modelCategoryImgage.getImage_url())) {
            Picasso.get().load(modelCategoryImgage.getImage_url()).into(myViewHolder.imageView);
        }

        if (this.intPos == i) {
            myViewHolder.view.setVisibility(View.VISIBLE);
        } else {
            myViewHolder.view.setVisibility(View.GONE);
        }

        myViewHolder.itemView.setOnClickListener(view -> {
            intPos = i;
            mListener.onItemClick(intPos, modelCategoryImgages);
        });
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_category, viewGroup, false));
    }

    public interface MyClickListener {

        void onItemClick(int position, ArrayList<ModelCategoryImgage> catData);

    }

    public static class MyViewHolder extends ViewHolder {
        private ImageView imageView;
        private TextView textView;
        private View view;

        public MyViewHolder(View view) {
            super(view);
            this.imageView = (ImageView) view.findViewById(R.id.cat_img);
            this.textView = (TextView) view.findViewById(R.id.cat_title);
            this.view = view.findViewById(R.id.view);
        }
    }
}
