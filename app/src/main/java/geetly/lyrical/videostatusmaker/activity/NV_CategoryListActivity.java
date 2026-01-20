package geetly.lyrical.videostatusmaker.activity;

import static geetly.lyrical.videostatusmaker.MyApplication.native_show_count;

import androidx.activity.ComponentActivity;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import geetly.lyrical.videostatusmaker.MyApplication;
import geetly.lyrical.videostatusmaker.R;
import geetly.lyrical.videostatusmaker.Retrofit.APIClientData;
import geetly.lyrical.videostatusmaker.adapter.NvVideoViewAdapter;
import geetly.lyrical.videostatusmaker.model.ModelVideoResponce;
import geetly.lyrical.videostatusmaker.model.VideoviewModel;
import geetly.lyrical.videostatusmaker.utils.MyAppUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NV_CategoryListActivity extends AppCompatActivity {

    private Activity context;
    private ArrayList<VideoviewModel> videoviewModel = new ArrayList<>();
    private RecyclerView rv_all_videos = null;
    private LinearLayout ll_progressbar;
    private LinearLayout ll_no_data_available = null;
    private int page = 1;
    private NvVideoViewAdapter nvVideoViewAdapter;
    private StaggeredGridLayoutManager layoutManager;
    private int pastVisibleItems, visibleItemCount, totalItemCount;
    private boolean loading = false;
    private LottieAnimationView lottieAnimationView;
    private LottieAnimationView lottiAnimationNodata;
    private String passed_category;
    private FrameLayout ll_fbbanner;

    private ImageView llBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);
        context = this;

        //passed_category = getIntent().getStringExtra("video_category").toString(); // Now, message has Drawer title
        passed_category = getIntent().getStringExtra("video_category");

        if (passed_category != null) {
            // The value is not null, so you can safely call toString() on it.
            passed_category = passed_category.toString();
        } else {
            // Handle the case where "video_category" is null (optional).
            // You can provide a default value or an error message here.
            // For example: passed_category = "Default Category";
            passed_category = "Latest";
        }

        TextView ct = findViewById(R.id.cat_title);
        ct.setText(passed_category);

       Log.e("CategoryList","Pssed " + passed_category);


        ll_progressbar =  findViewById(R.id.ll_progressbar);
        lottiAnimationNodata =  findViewById(R.id.lottiAnimationNodata);
        lottieAnimationView =  findViewById(R.id.animationView);
        ll_no_data_available =  findViewById(R.id.ll_no_data_available);
        rv_all_videos =  findViewById(R.id.rv_all_videos);
        ll_fbbanner =  findViewById(R.id.ll_fbbanner);

        llBack = findViewById(R.id.iv_back);

        llBack.setOnClickListener(v -> onBackPressed());

        MyApplication.page_from = "CategoryPage";

        RecyclerViewsetup();
        MyAppUtils.showBannerAds(this,ll_fbbanner);
    }

    private void RecyclerViewsetup() {

        layoutManager = new StaggeredGridLayoutManager(2, 1);
        rv_all_videos.setLayoutManager(layoutManager);
        rv_all_videos.setHasFixedSize(true);


        rv_all_videos.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NotNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }

            @Override
            public void onScrolled(@NotNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //Log.e("hhhhh", "onScrolled: " + "hhhh2");
                visibleItemCount = layoutManager.getChildCount();
                totalItemCount = layoutManager.getItemCount();
                int[] firstVisibleItems = null;
                firstVisibleItems = layoutManager.findFirstVisibleItemPositions(firstVisibleItems);
                if (firstVisibleItems != null && firstVisibleItems.length > 0) {
                    pastVisibleItems = firstVisibleItems[0];
                    //Log.e("hhhhh", "onScrolled: " + "hhhh1");
                }
                //Log.e(TAG_DEBUG,"Load More Data 377  LOADING VARIABLE " +loading);
                if (!loading) {
                    //Log.e("hhhhh", "onScrolled: " + "hhhh0");
                    //Log.e(TAG_DEBUG,"Load More Data 380  ARG1 " + (visibleItemCount + pastVisibleItems) + " ARG 2 " + totalItemCount);
                    if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                        loading = true;
                        //  Log.e("hhhhh", "onScrolled: " + "hhhh");
                        page = page + 1;

                        ll_progressbar.setVisibility(View.VISIBLE);

                        new Handler(getMainLooper()).postDelayed(() -> {

                           // loadMoreData(selected_Category);

                            ll_progressbar.setVisibility(View.GONE);

                        }, 100);

                    }
                }

            }
        });

        if (MyAppUtils.isConnectingToInternet(context)) {
            lottieAnimationView.setVisibility(View.GONE);
            lottieAnimationView.pauseAnimation();

            if(passed_category != "")
            {
                getVideos(passed_category);
                //Log.e("Category Selected from Push","Done : "+passed_category);
            }
            else
            {
                getVideos("Latest");
            }

        } else {
            lottieAnimationView.setVisibility(View.VISIBLE);
            lottieAnimationView.playAnimation();
        }


    }

    public void getVideos(String str) {
        String f = "1";
       // Log.e("sssss", "onResponse: " + str + page);
        ll_progressbar.setVisibility(View.VISIBLE);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("app", getApplicationContext().getPackageName());
        jsonObject.addProperty("cat", str);
        jsonObject.addProperty("page", page);
        jsonObject.addProperty("vidtype",100);
        try {
            APIClientData.getInterface().getCatVideo(jsonObject).enqueue(new Callback<ModelVideoResponce>() {
                public void onFailure(Call<ModelVideoResponce> call, Throwable th) {
                    ll_progressbar.setVisibility(View.GONE);
                    ll_no_data_available.setVisibility(View.VISIBLE);
                    rv_all_videos.setVisibility(View.GONE);

                }

                public void onResponse(@NotNull Call<ModelVideoResponce> call, @NotNull Response<ModelVideoResponce> response) {

                    try {
                        ModelVideoResponce modelVideoResponce = response.body();
                        int i = 0;
                        if (modelVideoResponce.getCode() == null || !modelVideoResponce.getCode().equals("200")) {
                            ll_no_data_available.setVisibility(View.VISIBLE);
                            rv_all_videos.setVisibility(View.GONE);
                            ll_progressbar.setVisibility(View.GONE);
                            return;
                        }
                        ll_no_data_available.setVisibility(View.GONE);
                        ll_progressbar.setVisibility(View.GONE);
                        rv_all_videos.setVisibility(View.VISIBLE);
                        videoviewModel = new ArrayList();
                        while (i < modelVideoResponce.getMsg().size()) {

                            if (i % native_show_count == 0 && i != 0 && MyApplication.show_native > 0 ) {
                                videoviewModel.add(null);
                            }
                            videoviewModel.add(modelVideoResponce.getMsg().get(i));
                            i++;
                        }
                        nvVideoViewAdapter = new NvVideoViewAdapter(context, videoviewModel);
                        layoutManager = new StaggeredGridLayoutManager(2, 1);
                        rv_all_videos.setLayoutManager(layoutManager);
                        rv_all_videos.setAdapter(nvVideoViewAdapter);

                    } catch (Exception e) {
                        ll_progressbar.setVisibility(View.GONE);
                        ll_no_data_available.setVisibility(View.VISIBLE);
                        rv_all_videos.setVisibility(View.GONE);
                    }

                }
            });
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Intent i;
                i = new Intent(this, NV_LibraryActivity.class);
                startActivity(i);
                finishAffinity();
                finish();
                super.onBackPressed();
        }

    }
}