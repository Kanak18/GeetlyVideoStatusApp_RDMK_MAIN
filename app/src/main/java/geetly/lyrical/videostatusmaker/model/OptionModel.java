package geetly.lyrical.videostatusmaker.model;

import com.google.gson.annotations.SerializedName;

public class OptionModel {
    @SerializedName("colorkeyrand")
    public String colorkeyrand;
    @SerializedName("duration")
    public String duration;
    @SerializedName("ff_cmd")
    public String ff_cmd;
    @SerializedName("ff_cmd_user")
    public String ff_cmd_user;
    @SerializedName("ff_cmd_video")
    public String ff_cmd_video;
    @SerializedName("imglist")
    public String imglist;
    @SerializedName("imgratio")
    public String imgratio;
    @SerializedName("total_image")
    public String total_image;
    @SerializedName("video_resolution")
    public String video_resolution;

    public String getColorkeyrand() {
        return this.colorkeyrand;
    }

    public String getDuration() {
        return this.duration;
    }

    public String getFf_cmd() {
        return this.ff_cmd;
    }

    public String getFf_cmd_user() {
        return this.ff_cmd_user;
    }

    public String getFf_cmd_video() {
        return this.ff_cmd_video;
    }

    public String getImglist() {
        return this.imglist;
    }

    public String getImgratio() {
        return this.imgratio;
    }

    public String getTotal_image() {
        return this.total_image;
    }

    public String getVideo_resolution() {
        return this.video_resolution;
    }

    public void setColorkeyrand(String str) {
        this.colorkeyrand = str;
    }

    public void setDuration(String str) {
        this.duration = str;
    }

    public void setFf_cmd(String str) {
        this.ff_cmd = str;
    }

    public void setFf_cmd_user(String str) {
        this.ff_cmd_user = str;
    }

    public void setFf_cmd_video(String str) {
        this.ff_cmd_video = str;
    }

    public void setImglist(String str) {
        this.imglist = str;
    }

    public void setImgratio(String str) {
        this.imgratio = str;
    }

    public void setTotal_image(String str) {
        this.total_image = str;
    }

    public void setVideo_resolution(String str) {
        this.video_resolution = str;
    }
}
