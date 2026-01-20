package geetly.lyrical.videostatusmaker.model;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

public class MultipleResource {

    @SerializedName("show_open_ads_admob")
    public Integer show_open_ads_admob;
    @SerializedName("show_banner")
    public Integer show_banner;
    @SerializedName("show_inter")
    public Integer show_inter;
    @SerializedName("show_native")
    public Integer show_native;
    @SerializedName("show_reward")
    public Integer show_reward;

    @SerializedName("force_update")
    public Integer force_update;

    @SerializedName("banner_ad_id")
    public String banner_ad_id;

    @SerializedName("native_ad_id")
    public String native_ad_id;

    @SerializedName("interstitial_ad_id")
    public String interstitial_ad_id;

    @SerializedName("reward_ad_id")
    public String reward_ad_id;

    @SerializedName("admob_open_ads")
    public String admob_open_ads;

    @SerializedName("interstitial_show_count")
    public Integer interstitial_show_count;

    @SerializedName("native_show_count")
    public Integer native_show_count;


    @SerializedName("ad_network")
    public String ad_network;

    @SerializedName("admob_publisher_id")
    public String admob_publisher_id;

    @SerializedName("unity_game_id")
    public String unity_game_id;

    @SerializedName("startapp_app_id")
    public String startapp_app_id;


    @SerializedName("app_update_status")
    public Integer app_update_status;


    @SerializedName("app_new_version")
    public Integer app_new_version;

    @SerializedName("app_update_desc")
    public String app_update_desc;
    @SerializedName("app_redirect_url")
    public String app_redirect_url;
    @SerializedName("cancel_update_status")
    public Boolean cancel_update_status;

    @SerializedName("status")
    public Integer status;

}

