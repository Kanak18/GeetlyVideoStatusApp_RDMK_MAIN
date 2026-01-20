package geetly.lyrical.videostatusmaker.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ModelCategoryResponse {
    @SerializedName("msg")
    public ArrayList<ModelCategoryImgage> msg;
    @SerializedName("code")
    String code;

    public ArrayList<ModelCategoryImgage> getMsg() {
        return msg;
    }

    public void setMsg(ArrayList<ModelCategoryImgage> msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
