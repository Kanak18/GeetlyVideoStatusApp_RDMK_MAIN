package geetly.lyrical.videostatusmaker.utils;

import android.net.Uri;

import java.io.File;
import java.io.Serializable;



public class Constant implements Serializable {

    private static final long serialVersionUID = 1L;



    public static final String TAG_PORTRAIT = "Portrait";
    public static final String TAG_LANDSCAPE = "Landscape";
    public static final String TAG_SQUARE = "Square";

    public static final String TAG_COLOR_ID = "color_id";
    public static final String TAG_COLOR_NAME = "color_name";
    public static final String TAG_COLOR_CODE = "color_code";

    public static final String TAG_CAT_ID = "cid";
    public static final String TAG_CAT_NAME = "category_name";
    public static final String TAG_CAT_IMAGE = "category_image";
    public static final String TAG_CAT_IMAGE_THUMB = "category_image_thumb";
    public static final String TAG_TOTAL_WALL = "category_total_wall";

    public static final String TAG_WALL_ID = "id";
    public static final String TAG_WALL_IMAGE = "wallpaper_image";
    public static final String TAG_WALL_IMAGE_THUMB = "wallpaper_image_thumb";

    public static final String TAG_GIF_ID = "id";
    public static final String TAG_GIF_IMAGE = "gif_image";
    public static final String TAG_GIF_TAGS = "gif_tags";
    public static final String TAG_GIF_VIEWS = "total_views";
    public static final String TAG_GIF_TOTAL_RATE = "total_rate";
    public static final String TAG_GIF_AVG_RATE = "rate_avg";

    public static final String TAG_WALL_VIEWS = "total_views";
    public static final String TAG_WALL_AVG_RATE = "rate_avg";
    public static final String TAG_WALL_TOTAL_RATE = "total_rate";
    public static final String TAG_WALL_DOWNLOADS = "total_download";
    public static final String TAG_WALL_TAGS = "wall_tags";
    public static final String TAG_WALL_TYPE = "wallpaper_type";
    public static final String TAG_WALL_COLORS = "wall_colors";
    public static final String TAG_IS_FAV = "is_favorite";

    public static final String LOGIN_TYPE_NORMAL = "normal";
    public static final String LOGIN_TYPE_GOOGLE = "google";
    public static final String LOGIN_TYPE_FB = "facebook";

    public static final String DARK_MODE_ON = "on";
    public static final String DARK_MODE_OFF = "off";
    public static final String DARK_MODE_SYSTEM = "system";

    // Number of columns of Grid View
    public static final int NUM_OF_COLUMNS = 2;

    // Gridview image padding
    public static final int GRID_PADDING = 3; // in dp

    public static int columnWidth = 0;
    public static int columnHeight = 0;

    public static Boolean isFav = false;
    public static String packageName = "", search_item = "", giFName = "", gifPath = "";
    public static Uri uri_setwall;
    public static File file;

    public static Boolean isUpdate = false, isLogged = false, isAdmobBannerAd = true, isAdmobInterAd = true, isAdmobNativeAd = false, isFBBannerAd = true,
            isFBInterAd = true, isFBNativeAd = false, isGIFEnabled = true, isPortrait = true, isLandscape = true, isSquare = true, showUpdateDialog = true,
            appUpdateCancel = false;
    public static String ad_publisher_id = "";
    public static String ad_banner_id = "", ad_inter_id = "", ad_native_id = "", fb_ad_banner_id = "", fb_ad_inter_id = "", fb_ad_native_id = "",
            appVersion="1", appUpdateMsg = "", appUpdateURL = "";

    public static int adShow = 5;
    public static int adShowFB = 5;
    public static int adCount = 0;

    public static int admobNativeShow = 12, fbNativeShow = 12;

    public static Boolean isColorOn = true;



}