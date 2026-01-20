package geetly.lyrical.videostatusmaker.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import geetly.lyrical.videostatusmaker.BuildConfig;
import geetly.lyrical.videostatusmaker.R;
import geetly.lyrical.videostatusmaker.activity.NV_FullViewDataActivity;
import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;

import static geetly.lyrical.videostatusmaker.fragments.WhatsappSavedFragment.isStatus;


public class WhatsappStatusAdapter extends RecyclerView.Adapter<WhatsappStatusAdapter.ViewHolder> {

    private Context context;
    private ArrayList<File> fileArrayList;
    private LayoutInflater layoutInflater;
    private FileListClickInterface fileListClickInterface;
    private ImageView ivImage;
    private ImageView ivPlay;


    public WhatsappStatusAdapter(Context context, ArrayList<File> files, FileListClickInterface fileListClickInterface) {
        this.context = context;
        this.fileListClickInterface = fileListClickInterface;
        this.fileArrayList = files;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View inflatedView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout
                .items_whatsapp_view, viewGroup, false);
        return new ViewHolder(inflatedView);

//        View view = layoutInflater.inflate(R.layout.items_whatsapp_view, viewGroup, false);
//        return new ViewHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        File fileItem = fileArrayList.get(i);

        if (fileItem.getAbsolutePath().endsWith(".mp4")) {
            ivPlay.setVisibility(View.VISIBLE);
        } else {
            ivPlay.setVisibility(View.GONE);
        }
        Glide.with(context)
                .load(fileItem.getAbsolutePath())
                .into(ivImage);

        ivPlay.setOnClickListener(view -> {

            Uri uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", fileItem.getAbsoluteFile());
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(uri, "video/*");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            context.startActivity(intent);

        });
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                fileListClickInterface.getPosition(i, fileItem);

                Intent inNext = new Intent(context, NV_FullViewDataActivity.class);
                inNext.putExtra("ImageDataFile", fileArrayList);
                inNext.putExtra(isStatus, 0);
                inNext.putExtra("Position", i);
                context.startActivity(inNext);
            }
        });


    }

    @Override
    public int getItemCount() {
        return fileArrayList.size();
    }

    private void initView(View view) {
        ivImage = (ImageView) view.findViewById(R.id.iv_image);
        ivPlay = (ImageView) view.findViewById(R.id.iv_play);
    }

    public interface FileListClickInterface {
        void getPosition(int position, File file);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            initView(itemView);

        }
    }
}