package geetly.lyrical.videostatusmaker.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Utils_Shareddata {
    public static final String SHARED_NAME = "shared_file";
    public static SharedPreferences instance;

    public static int get(Util_IntShared utilIntShared) {
        return instance.getInt(utilIntShared.getName(), utilIntShared.getDefaultValue());
    }

    public static void init(Context context) {
        if (instance == null) {
            instance = context.getSharedPreferences(SHARED_NAME, 0);
        }
    }

    public static void set(Util_IntShared utilIntShared, int i) {
        Editor edit = instance.edit();
        edit.putInt(utilIntShared.getName(), i);
        edit.apply();
    }

    public static String get(Util_StringShared utilStringShared) {
        return instance.getString(utilStringShared.getName(), utilStringShared.getDefaultValue());
    }

    public static void set(Util_StringShared utilStringShared, String str) {
        Editor edit = instance.edit();
        edit.putString(utilStringShared.getName(), str);
        edit.apply();
    }
}
