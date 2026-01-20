package geetly.lyrical.videostatusmaker.activity;

import static geetly.lyrical.videostatusmaker.MyApplication.native_show_count;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;

import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import com.airbnb.lottie.LottieAnimationView;


import com.google.android.gms.ads.AdView;

import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.material.navigation.NavigationView;

import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;


import geetly.lyrical.videostatusmaker.BuildConfig;
import geetly.lyrical.videostatusmaker.MyApplication;
import geetly.lyrical.videostatusmaker.R;
import geetly.lyrical.videostatusmaker.Retrofit.APIClientData;
import geetly.lyrical.videostatusmaker.adapter.NvCategoryAdapter;
import geetly.lyrical.videostatusmaker.adapter.NvVideoViewAdapter;
import geetly.lyrical.videostatusmaker.model.ModelCategoryResponse;
import geetly.lyrical.videostatusmaker.model.ModelVideoResponce;
import geetly.lyrical.videostatusmaker.model.VideoviewModel;
import geetly.lyrical.videostatusmaker.utils.MyAppUtils;

import geetly.lyrical.videostatusmaker.utils.Utils_Permission;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NV_LibraryActivity extends AppCompatActivity {


    private Activity context;
    private ArrayList<VideoviewModel> videoviewModel = new ArrayList<>();
    private boolean isBackPressed = false;
    private RecyclerView rv_all_category = null;
    private RecyclerView rv_all_videos = null;
    private LinearLayout ll_no_data_available = null;
    private String selected_Category = "Latest";
    private int page = 1;
    private int pastVisibleItems, visibleItemCount, totalItemCount;
    private boolean loading = false;
    private EditText et_search_bar;
    private AdView adView;
    ActionBarDrawerToggle toggle;
    private LinearLayout ll_progressbar;
    private LottieAnimationView lottieAnimationView;
    private NvVideoViewAdapter nvVideoViewAdapter;
    public static final String TAG_SEARCH_TERM = "tag_search";
    private SwipeRefreshLayout swiperefreshLayout;
    //    private ImageView saveWp;
    private StaggeredGridLayoutManager layoutManager;
    private FrameLayout ll_fbbanner;
    private LottieAnimationView lottiAnimationNodata;

    private static final String TAG_DEBUG = "DEBUG PAGING";

    LinearLayout ll_back;
    ArrayAdapter<Object> nvCategoryAdapter;
    private DrawerLayout drawer_layout;
    private ImageView home;

    Dialog dialog,dialog_exit;
    private NativeAd nativeAd;
    private ProgressBar progressBar;
    private ImageView saveWp;
    private NavigationView navigationView;

    boolean doubleBackToExitPressedOnce = false;



    @SuppressLint("WrongConstant")
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.contain_main);
        context = this;
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        if (MyApplication.app_update_status  > 0 && MyApplication.app_new_version > BuildConfig.VERSION_CODE)
        {
            Log.d("UPDATE_STATUS"," Current Version  : "+BuildConfig.VERSION_CODE);
            showAppDialog(MyApplication.app_update_desc,MyApplication.app_redirect_url,MyApplication.cancel_update_status);
        }




        //String UUID = OneSignal.getPermissionSubscriptionState().getSubscriptionStatus().getUserId();
        //Log.e("AppOpenManager User ID",UUID);


        setupDialog();
        home =  findViewById(R.id.home);
        drawer_layout =  findViewById(R.id.drawer_layout);
        saveWp =  findViewById(R.id.save_wp);
        navigationView =  findViewById(R.id.navigationView);
        et_search_bar =  findViewById(R.id.et_search_bar);
        ll_progressbar =  findViewById(R.id.ll_progressbar);
        swiperefreshLayout = findViewById(R.id.swiperefreshLayout);

        ll_fbbanner =  findViewById(R.id.ll_fbbanner);
        lottiAnimationNodata =  findViewById(R.id.lottiAnimationNodata);
        lottieAnimationView =  findViewById(R.id.animationView);
        rv_all_category =  findViewById(R.id.rv_all_category);
        ll_no_data_available =  findViewById(R.id.ll_no_data_available);
        rv_all_videos =  findViewById(R.id.rv_all_videos);

        et_search_bar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    performSearch();
                    return true;
                }
                return false;
            }
        });

        Intent intent = getIntent();
        if (intent != null) {
            if(intent.hasExtra("video_category")) {
                selected_Category = intent.getStringExtra("video_category");
            }
        }


        home.setOnClickListener(view -> drawer_layout.openDrawer(Gravity.START, true));
        toggle = new ActionBarDrawerToggle(this, drawer_layout, R.string.open, R.string.close);

        navigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.album:
                    startActivity(new Intent(context, NV_AlbumdataActivity.class));
                    return true;
                case R.id.privacy:

                    startActivity(new Intent(Intent.ACTION_VIEW).setDataAndType(Uri.parse(MyAppUtils.PRIVACY_URL), "text/plain"));

                    return true;

                case R.id.rateUs:
                    MyAppUtils.rateApp(context);
                    return true;
                case R.id.share:
                    Intent intent1 = new Intent(Intent.ACTION_SEND);
                    intent1.setType("text/plain");
                    intent1.putExtra(Intent.EXTRA_TEXT, getString(R.string.share) + getPackageName());
                    startActivity(Intent.createChooser(intent1, "Share App..."));

                    return true;
                default:
                    return false;
            }
        });


        lottiAnimationNodata.playAnimation();




        new Utils_Permission(context).checkPermissionsGranted();
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().build());
        copyFile("blankimage.jpg");

        saveWp.setOnClickListener(v -> {
            MyAppUtils.showInterstitialAds(context);

            startActivity(new Intent(getApplicationContext(), NV_WhatsappActivity.class));


        });


        swiperefreshLayout.setOnRefreshListener(() -> {

            page = 1;
            videoviewModel.clear();
            nvVideoViewAdapter.notifyDataSetChanged();

            getVideos(selected_Category);

            swiperefreshLayout.setRefreshing(false);

        });


        RecyclerViewsetup();
        MyAppUtils.showBannerAds(this,ll_fbbanner);

        MyApplication.page_from = "MainPage";


    }




    public void performSearch()
    {
        et_search_bar.clearFocus();
        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(et_search_bar.getWindowToken(), 0);
        //Toast.makeText(this,"Searching now"+et_search_bar.getText(),Toast.LENGTH_LONG).show();


        String text = et_search_bar.getText().toString();

        et_search_bar.setText("");

        Intent myIntent = new Intent(NV_LibraryActivity.this,NV_SearchVideo.class);
        myIntent.putExtra("search_text",text);
        startActivity(myIntent);

    }

    public void setupDialog() {
        dialog_exit = new Dialog(context);
        dialog_exit.setCancelable(false);
        dialog_exit.requestWindowFeature(1);
        dialog_exit.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog_exit.setContentView(R.layout.dialog_exit_app);
        dialog_exit.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog_exit.setCanceledOnTouchOutside(false);


        if(MyApplication.show_native > 0) {
            MyAppUtils.loadAdmobNativeAd((FrameLayout) dialog_exit.findViewById(R.id.fl_adplaceholder),context);
        }

        Button btnNo = dialog_exit.findViewById(R.id.btnNo);
        btnNo.setOnClickListener(view -> {
            //Toast.makeText(context, "No Pressed found", Toast.LENGTH_SHORT).show();
            if (dialog_exit.isShowing()) dialog_exit.dismiss();
        });

        Button btnYes = dialog_exit.findViewById(R.id.btnYes);
        btnYes.setOnClickListener(view -> {
            //Toast.makeText(context, "Yes Pressed found", Toast.LENGTH_SHORT).show();
            dialog_exit.dismiss();
            finish();
            System.exit(0);
        });


        Window window = dialog_exit.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

    }


    private void showAppDialog(String description, String link, boolean isCancel) {

        Dialog dialog = new Dialog(NV_LibraryActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_update_app);
        dialog.setCancelable(false);

        dialog.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setLayout(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);

        TextView textViewDes = dialog.findViewById(R.id.textView_description_dialog_update);
        TextView buttonUpdate = dialog.findViewById(R.id.button_update_dialog_update);
        TextView buttonCancel = dialog.findViewById(R.id.button_cancel_dialog_update);

        if (isCancel) {
            buttonCancel.setVisibility(View.VISIBLE);
        } else {
            buttonCancel.setVisibility(View.GONE);
        }
        textViewDes.setText(description);

        buttonUpdate.setOnClickListener(v -> {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(link)));
            dialog.dismiss();
        });

        buttonCancel.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private void copyFile(String str) {
        try {
            InputStream open = getAssets().open(str);
            StringBuilder sb = new StringBuilder();
            sb.append(getCacheDir());
            sb.append("/");
            sb.append(str);
            FileOutputStream fos = new FileOutputStream(sb.toString());
            byte[] bArr = new byte[1024];
            while (true) {
                int read = open.read(bArr);
                if (read != -1) {
                    fos.write(bArr, 0, read);
                } else {
                    open.close();
                    fos.flush();
                    fos.close();
                    return;
                }
            }
        } catch (Exception e) {
            e.getMessage();
        }
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

                            loadMoreData(selected_Category);

                            ll_progressbar.setVisibility(View.GONE);

                        }, 100);

                    }
                }

            }
        });

        if (MyAppUtils.isConnectingToInternet(context)) {
            lottieAnimationView.setVisibility(View.GONE);
            lottieAnimationView.pauseAnimation();
            getCategory();
            //getVideos("Latest");
            if(selected_Category != "")
            {
                getVideos(selected_Category);
                Log.e("Category Selected from Push","Done : "+selected_Category);
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

    private void loadMoreData(String str) {
        //Log.e(TAG_DEBUG,"Load More Data 424");
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("app", getApplicationContext().getPackageName());
        jsonObject.addProperty("cat", str);
        jsonObject.addProperty("page", page);
        jsonObject.addProperty("vidtype",100);
        try {
            APIClientData.getInterface().getCatVideo(jsonObject).enqueue(new Callback<ModelVideoResponce>() {
                @Override
                public void onFailure(@NotNull Call<ModelVideoResponce> call, @NotNull Throwable th) {


                }

                @Override
                public void onResponse(@NotNull Call<ModelVideoResponce> call, @NotNull Response<ModelVideoResponce> response) {

                    if (response.isSuccessful() && response.body() != null) {
                        ModelVideoResponce modelVideoResponce = response.body();
                        int i = 0;

                        ll_no_data_available.setVisibility(View.GONE);
                        ll_progressbar.setVisibility(View.GONE);
                        rv_all_videos.setVisibility(View.VISIBLE);

                        while (i < modelVideoResponce.getMsg().size()) {
                            if (i == 0 && MyApplication.show_native > 0) {
                                videoviewModel.add(null);
                            }
                            videoviewModel.add(modelVideoResponce.getMsg().get(i));
                            i++;
                        }

                        nvVideoViewAdapter.setDataList(videoviewModel);
                        nvVideoViewAdapter.notifyDataSetChanged();
                        loading = false;


                    } else {
                        Toast.makeText(context, "No video found", Toast.LENGTH_SHORT).show();
                    }

                }
            });
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    private void getCategory() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("app", getApplicationContext().getPackageName());
        jsonObject.addProperty("cat", "Latest");

        APIClientData.getInterface().getAllCategory(jsonObject).enqueue(new Callback<ModelCategoryResponse>() {
            public void onFailure(@NotNull Call<ModelCategoryResponse> call, @NotNull Throwable th) {

            }

            public void onResponse(@NotNull Call<ModelCategoryResponse> call, @NotNull Response<ModelCategoryResponse> response) {
                ModelCategoryResponse modelCategoryResponse = response.body();

                if (modelCategoryResponse == null || modelCategoryResponse.getMsg() == null || modelCategoryResponse.getMsg().isEmpty()) {
                    ll_no_data_available.setVisibility(View.VISIBLE);
                    return;
                }


                ll_no_data_available.setVisibility(View.GONE);
                NvCategoryAdapter nvCategoryAdapter = new NvCategoryAdapter(context, modelCategoryResponse.getMsg());
                rv_all_category.setLayoutManager(new GridLayoutManager(context, 1, RecyclerView.HORIZONTAL, false));
                rv_all_category.setAdapter(nvCategoryAdapter);

                nvCategoryAdapter.setOnItemClickListener((position, catData) -> {

                    page = 1;
                    selected_Category = catData.get(position).getCategory();
                    Log.e("sssss", "onResponse: " + selected_Category);
                    loading = false;


                    MoveToCateogry(selected_Category);

                    // startActivity(new Intent(NV_LibraryActivity.this.context, NV_CategoryListActivity.class)
                    //         .putExtra("video_category", selected_Category));

                    //getVideos(selected_Category);
                    //nvCategoryAdapter.notifyDataSetChanged();

                });

            }
        });
    }
    public void MoveToCateogry(String categoryName) {
        Log.e("CategoryList",categoryName);
        Intent  intent = new Intent(context, NV_CategoryListActivity.class);
        intent.putExtra("video_category",categoryName);
        startActivity(intent);
    }

    public void getVideos(String str) {
        String f = "1";
        Log.e("sssss", "onResponse: " + str + page);
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

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        //Log.e("BACKPRESS => ", String.valueOf(doubleBackToExitPressedOnce));
        dialog_exit.show();
    }

    @Override
    public void onPause() {
        if (adView != null) {
            adView.pause();
        }
        super.onPause();
    }

    /** Called when returning to the activity */
    @Override
    public void onResume() {
        super.onResume();
        if (adView != null) {
            adView.resume();
        }
    }

    /** Called before the activity is destroyed */
    @Override
    public void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }



}




