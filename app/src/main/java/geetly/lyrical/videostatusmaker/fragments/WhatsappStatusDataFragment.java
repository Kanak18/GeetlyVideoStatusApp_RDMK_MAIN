package geetly.lyrical.videostatusmaker.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import geetly.lyrical.videostatusmaker.R;
import geetly.lyrical.videostatusmaker.activity.NV_FullViewDataActivity;
import geetly.lyrical.videostatusmaker.adapter.WhatsappStatusAdapter;
import geetly.lyrical.videostatusmaker.utils.MyAppUtils;

import java.io.File;
import java.util.ArrayList;

import static geetly.lyrical.videostatusmaker.fragments.WhatsappSavedFragment.isStatus;


public class WhatsappStatusDataFragment extends Fragment implements WhatsappStatusAdapter.FileListClickInterface {

    ArrayList<File> fileArrayList;
    private File[] allfiles;
    private WhatsappStatusAdapter whatsappStatusAdapter;
    private TextView tvNoResult;
    private RecyclerView rvFileList;
    private SwipeRefreshLayout swiperefreshview;
    private Activity context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        initView(view);
        initViews();
        context = getActivity();
        return view;
    }


    private void initViews() {

        fileArrayList = new ArrayList<>();
        getData();
        swiperefreshview.setOnRefreshListener(() -> {

            fileArrayList.clear();

            new Handler(Looper.getMainLooper()).postDelayed(this::getData, 10);

            swiperefreshview.setRefreshing(false);
        });
    }

    private void getData() {

        String targetPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsApp/Media/.Statuses";
        File targetDirector = new File(targetPath);
        allfiles = targetDirector.listFiles();

        if (allfiles != null) {
            try {


                if (allfiles.length > 0) {
                    tvNoResult.setVisibility(View.GONE);
                } else {

                    tvNoResult.setVisibility(View.VISIBLE);
                }

                for (int i = 0; i < allfiles.length; i++) {
                    File file = allfiles[i];


                    if (Uri.fromFile(file).toString().endsWith(".png") || Uri.fromFile(file).toString().endsWith(".jpg") || Uri.fromFile(file).toString().endsWith(".mp4")) {


                        fileArrayList.add(file);

                    }


                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        whatsappStatusAdapter = new WhatsappStatusAdapter(getActivity(), fileArrayList, this);
        MyAppUtils.setUpRecycler(rvFileList);
        rvFileList.setAdapter(whatsappStatusAdapter);

    }

    @Override
    public void getPosition(int position, File file) {
        Intent inNext = new Intent(getActivity(), NV_FullViewDataActivity.class);
        inNext.putExtra("ImageDataFile", fileArrayList);
        inNext.putExtra(isStatus, 0);
        inNext.putExtra("Position", position);
        startActivity(inNext);

    }

    private void initView(View view) {
        tvNoResult = (TextView) view.findViewById(R.id.tv_NoResult);
        rvFileList = (RecyclerView) view.findViewById(R.id.rv_fileList);
        swiperefreshview = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
    }
}
