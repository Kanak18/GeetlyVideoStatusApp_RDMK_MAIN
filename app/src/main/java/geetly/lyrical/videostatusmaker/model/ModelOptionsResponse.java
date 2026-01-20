package geetly.lyrical.videostatusmaker.model;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;

public class ModelOptionsResponse {
    @SerializedName("ReportData")
    public ArrayList<OptionModel> ReportData;

    public ArrayList<OptionModel> getReportData() {
        return this.ReportData;
    }

    public void setReportData(ArrayList<OptionModel> arrayList) {
        this.ReportData = arrayList;
    }
}
