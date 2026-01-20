package geetly.lyrical.videostatusmaker.utils;

public enum Util_IntShared {
    ADS(1);
    
    public int mDefaultValue;

    Util_IntShared(int i) {
        this.mDefaultValue = i;
    }

    public int getDefaultValue() {
        return this.mDefaultValue;
    }

    public String getName() {
        return name();
    }
}
