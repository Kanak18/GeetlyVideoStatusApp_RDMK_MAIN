package geetly.lyrical.videostatusmaker.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ModelVideoResponce {
    @SerializedName("msg")
    public ArrayList<VideoviewModel> msg;
    @SerializedName("code")
    String code;

    public ArrayList<VideoviewModel> getMsg() {
        return msg;
    }

    public void setMsg(ArrayList<VideoviewModel> msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
