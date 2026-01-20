package geetly.lyrical.videostatusmaker.utils;

import android.content.Context;
import android.util.AttributeSet;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

public class Util_NewView extends AppCompatImageView {
    public Util_NewView(Context context) {
        super(context);
    }

    @Override
    public void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        setMeasuredDimension(getMeasuredHeight(), getMeasuredHeight());
    }

    public Util_NewView(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public Util_NewView(Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }
}
