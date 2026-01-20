package geetly.lyrical.videostatusmaker.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.GradientDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;


import java.text.DecimalFormat;

import javax.crypto.SecretKey;

import geetly.lyrical.videostatusmaker.R;

public class Methods {

    private Context context;
    private SecretKey key;

    public Methods(Context context) {
        this.context = context;

    }


    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public int getScreenWidth() {
        int columnWidth;
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        final Point point = new Point();

        point.x = display.getWidth();
        point.y = display.getHeight();

        columnWidth = point.x;
        return columnWidth;
    }

    public int getScreenHeight() {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        final Point point = new Point();
        point.y = display.getHeight();

        return point.y;
    }



    public void setStatusColor(Window window) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    public String getImageThumbSize(String imagePath, String type) {
        if (type.equals(context.getString(R.string.portrait)) || type.equals("")) {
            imagePath = imagePath.replace("&size=300x300", "&size=200x350");
        } else if (type.equals(context.getString(R.string.landscape))) {
            imagePath = imagePath.replace("&size=300x300", "&size=350x200");
        } else if (type.equals(context.getString(R.string.square))) {
            imagePath = imagePath.replace("&size=300x300", "&size=300x300");
        } else if (type.equals(context.getString(R.string.details))) {
            imagePath = imagePath.replace("&size=300x300", "&size=500x500");
        } else if (type.equals(context.getString(R.string.home).concat(Constant.TAG_PORTRAIT))) {
            imagePath = imagePath.replace("&size=300x300", "&size=400x550");
        } else if (type.equals(context.getString(R.string.home).concat(Constant.TAG_LANDSCAPE))) {
            imagePath = imagePath.replace("&size=300x300", "&size=550x400");
        } else if (type.equals(context.getString(R.string.home).concat(Constant.TAG_SQUARE))) {
            imagePath = imagePath.replace("&size=300x300", "&size=500x500");
        } else if (type.equals(context.getString(R.string.categories))) {
            imagePath = imagePath.replace("&size=300x300", "&size=300x250");
        }else{
            imagePath = imagePath.replace("&size=300x300", "&size=200x350");
        }
        return imagePath;
    }

    //check dark mode or not
    public boolean isDarkMode() {
        int currentNightMode = context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (currentNightMode) {
            case Configuration.UI_MODE_NIGHT_NO:
                // Night mode is not active, we're using the light theme
                return false;
            case Configuration.UI_MODE_NIGHT_YES:
                // Night mode is active, we're using dark theme
                return true;
            default:
                return false;
        }
    }


    public GradientDrawable getRoundDrawable(int color) {
        GradientDrawable gd = new GradientDrawable();
        gd.setColor(color);
        gd.mutate();
        gd.setCornerRadius(10);
        return gd;
    }

    public GradientDrawable getRoundDrawableRadis(int color, int radius) {
        GradientDrawable gd = new GradientDrawable();
        gd.setColor(color);
        gd.mutate();
        gd.setCornerRadius(radius);
        return gd;
    }

    public String format(Number number) {
        char[] suffix = {' ', 'k', 'M', 'B', 'T', 'P', 'E'};
        long numValue = number.longValue();
        int value = (int) Math.floor(Math.log10(numValue));
        int base = value / 3;
        if (value >= 3 && base < suffix.length) {
            return new DecimalFormat("#0.0").format(numValue / Math.pow(10, base * 3)) + suffix[base];
        } else {
            return new DecimalFormat("#,##0").format(numValue);
        }
    }





    public int getColumnWidth(int column, int grid_padding) {
        Resources r = context.getResources();
        float padding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, grid_padding, r.getDisplayMetrics());
        return (int) ((getScreenWidth() - ((column + 1) * padding)) / column);
    }


}