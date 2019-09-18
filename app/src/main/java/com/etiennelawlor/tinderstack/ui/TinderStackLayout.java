package com.etiennelawlor.tinderstack.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.FrameLayout;

import com.etiennelawlor.tinderstack.bus.events.Completion;
import com.etiennelawlor.tinderstack.utilities.DisplayUtility;

/**
 * Created by etiennelawlor on 11/18/16.
 */

public class TinderStackLayout extends FrameLayout {

  // region Constants
  private static final int DURATION = 300;
  // endregion
  private Completion completion;
  private int screenWidth;
  private int yMultiplier;
  // endregion

  // region Constructors
  public TinderStackLayout(Context context) {
    super(context);
    init();
  }

  public TinderStackLayout(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public TinderStackLayout(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    init();
  }
  // endregion

  @Override
  public void addView(View child, int index, ViewGroup.LayoutParams params) {
    super.addView(child, index, params);
    if (completion != null)
      completion.onNext(getChildCount());
  }

  @Override
  public void removeView(View view) {
    super.removeView(view);
    if (completion != null)
      completion.onNext(getChildCount());
  }

  @Override
  public void onDetachedFromWindow() {
    super.onDetachedFromWindow();
  }

  // region Helper Methods
  private void init() {
    setClipChildren(false);

    screenWidth = DisplayUtility.getScreenWidth(getContext());
    yMultiplier = DisplayUtility.dp2px(getContext(), 8);

    //completion = setupCompletion();

  }

  public void addCard(TinderCardView tinderCardView) {
    if (completion == null)
      completion = tinderCardView.getCompletion();

    ViewGroup.LayoutParams layoutParams;
    layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

    int childCount = getChildCount();
    addView(tinderCardView, 0, layoutParams);

    float scaleValue = 1 - (childCount / 50.0f);

    tinderCardView.animate()
        .x(0)
        .y(childCount * yMultiplier)
        .scaleX(scaleValue)
        .setInterpolator(new AnticipateOvershootInterpolator())
        .setDuration(DURATION);
  }
  // endregion
}
