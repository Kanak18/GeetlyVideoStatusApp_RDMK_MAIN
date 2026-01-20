package geetly.lyrical.videostatusmaker.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import java.util.Arrays;

public class WhatsappSavedFragment extends Fragment implements WhatsappStatusAdapter.FileListClickInterface {

    public static String isStatus = "isStatus";
    ArrayList<File> fileArrayList;
    Activity context;
    private File[] allfiles;
    private WhatsappStatusAdapter whatsappStatusAdapter;
    private TextView tvNoResult;
    private SwipeRefreshLayout swiperefreshview;
    private RecyclerView rvFileList;

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

            new Handler(Looper.getMainLooper()).postDelayed(this::getData, 100);

            swiperefreshview.setRefreshing(false);
        });
    }

    private void getData() {
        File targetDirector = new File(MyAppUtils.RootDirectoryWhatsappShow.getAbsolutePath());
        allfiles = targetDirector.listFiles();

        if (allfiles != null)
            try {

                if (allfiles.length > 0) {
                    tvNoResult.setVisibility(View.GONE);
                } else {

                    tvNoResult.setVisibility(View.VISIBLE);
                }

                Arrays.sort(allfiles, (a, b) -> {
                    if (a.lastModified() < b.lastModified())
                        return 1;
                    if (a.lastModified() > b.lastModified())
                        return -1;
                    return 0;
                });

                for (int i = 0; i < allfiles.length; i++) {
                    File file = allfiles[i];

                    if (Uri.fromFile(file).toString().endsWith(".png") || Uri.fromFile(file).toString().endsWith(".jpg") || Uri.fromFile(file).toString().endsWith(".mp4")) {


                        fileArrayList.add(file);

                    }


                }

            } catch (Exception e) {
                e.printStackTrace();
                swiperefreshview.setRefreshing(false);
            }


        whatsappStatusAdapter = new WhatsappStatusAdapter(getActivity(), fileArrayList, this);
        rvFileList.setAdapter(whatsappStatusAdapter);

    }

    @Override
    public void getPosition(int position, File file) {
        Intent inNext = new Intent(getActivity(), NV_FullViewDataActivity.class);
        inNext.putExtra("ImageDataFile", fileArrayList);
        inNext.putExtra(isStatus, 1);
        inNext.putExtra("Position", position);
        startActivity(inNext);

    }

    private void initView(View view) {
        tvNoResult = (TextView) view.findViewById(R.id.tv_NoResult);
        swiperefreshview = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
        rvFileList = (RecyclerView) view.findViewById(R.id.rv_fileList);
    }
}
