package com.etiennelawlor.tinderstack.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import androidx.appcompat.widget.AppCompatImageView;
import com.etiennelawlor.tinderstack.R;

public class DynamicHeightImageView extends AppCompatImageView {

    private double mHeightRatio;

    public DynamicHeightImageView(Context context) {
        super(context);
        init(context, null);
    }

    public DynamicHeightImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public DynamicHeightImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mHeightRatio > 0.0) {
            int width = View.MeasureSpec.getSize(widthMeasureSpec);
            int height = (int) (width * mHeightRatio);
            setMeasuredDimension(width, height);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    private void init(Context context, AttributeSet attrs) {
        if (!isInEditMode()) {
            TypedArray attributeArray = context.obtainStyledAttributes(
                    attrs,
                    R.styleable.DynamicHeightImageView);

            try {
                float heightRatio = attributeArray.getFloat(R.styleable.DynamicHeightImageView_heightRatio, 1.0F);
                this.mHeightRatio = heightRatio;
            } finally {
                attributeArray.recycle();
            }
        }
    }

    public void setHeightRatio(double ratio) {
        if (ratio != mHeightRatio) {
            mHeightRatio = ratio;
            requestLayout();
        }
    }

    public double getHeightRatio() {
        return mHeightRatio;
    }
}
