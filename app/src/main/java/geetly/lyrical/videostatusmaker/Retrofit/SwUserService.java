package geetly.lyrical.videostatusmaker.Retrofit;

import geetly.lyrical.videostatusmaker.model.ModelCategoryResponse;
import geetly.lyrical.videostatusmaker.model.ModelOptionsResponse;
import geetly.lyrical.videostatusmaker.model.ModelVideoResponce;
import com.google.gson.JsonObject;
import geetly.lyrical.videostatusmaker.model.MultipleResource;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface SwUserService {

    //categoery wise data api call
    //@POST("getdatacategorywise1.php")
    //@POST("getdatacategorywise1_fix.php")
    @POST("getdatacategorywise1.php")
    @Headers({"Accept: application/json", "Content-Type: application/json"})
    Call<ModelVideoResponce> getCatVideo(@Body JsonObject jsonObject);

    @POST("ooption.php")
    @Headers({"Accept: application/json", "Content-Type: application/json"})
    Call<ModelOptionsResponse> getCat_Video_ooptions(@Body JsonObject jsonObject);

    //get categoery api call
    @POST("getallcategory.php")
    @Headers({"Accept: application/json", "Content-Type: application/json"})
    Call<ModelCategoryResponse> getAllCategory(@Body JsonObject jsonObject);

    //download file api
    @POST("download.php")
    Call<JsonObject> updateDownloads(@Body JsonObject jsonObject);


    //@POST("getsettings_geetly_demo.php")
    @POST("getsettings_geetly_ver44.php")
    @Headers({"Accept: application/json", "Content-Type: application/json"})
    Call<MultipleResource> getAppSettings(@Body JsonObject jsonObject);

    //categoery wise data api call
    @POST("getsearchvideo.php")
    @Headers({"Accept: application/json", "Content-Type: application/json"})
    Call<ModelVideoResponce> getSearchVideo(@Body JsonObject jsonObject);
}
