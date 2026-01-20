package geetly.lyrical.videostatusmaker.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class VideoviewModel implements Serializable {
    @SerializedName("id")
    String id;
    @SerializedName("title")
    String title;
    @SerializedName("category")
    String category;
    @SerializedName("video_thumb")
    String videoThumb;
    @SerializedName("video_link")
    String video_link;
    @SerializedName("video_zip")
    String video_zip;
    @SerializedName("is_premium")
    Integer is_premium;


    public Integer getIsPremium() {
        return is_premium;
    }

    public void setIsPremium(Integer is_premium) {
        this.is_premium = is_premium;
    }

    public String getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVideoThumb() {
        return videoThumb;
    }

    public void setVideoThumb(String videoThumb) {
        this.videoThumb = videoThumb;
    }

    public String getVideo_link() {
        return video_link;
    }

    public void setVideo_link(String video_link) {
        this.video_link = video_link;
    }

    public String getVideo_zip() {
        return video_zip;
    }

    public void setVideo_zip(String video_zip) {
        this.video_zip = video_zip;
    }
}