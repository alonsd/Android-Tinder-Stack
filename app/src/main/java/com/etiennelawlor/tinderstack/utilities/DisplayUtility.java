package com.etiennelawlor.tinderstack.utilities;

import android.content.Context;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

//import com.clvb.android.network.models.response.User;

/**
 * Created by etiennelawlor on 12/19/15.
 */
public class DisplayUtility {

    // region Utility Methods
    public static int dp2px(Context context, int dp) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        DisplayMetrics displaymetrics = new DisplayMetrics();
        display.getMetrics(displaymetrics);

        return (int) (dp * displaymetrics.density + 0.5f);
    }

    public static int getScreenWidth(Context context) {
        Point size = new Point();
        ((AppCompatActivity) context).getWindowManager().getDefaultDisplay().getSize(size);
        return size.x;
    }
    // endregion
}
