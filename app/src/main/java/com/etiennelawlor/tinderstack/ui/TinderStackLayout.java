package com.etiennelawlor.tinderstack.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;


public class TinderStackLayout extends FrameLayout {

  //Constructors
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

  @Override
  public void onDetachedFromWindow() {
    super.onDetachedFromWindow();
  }

  private void init() {
    setClipChildren(false);
  }

  public void addCard(TinderCardView tinderCardView, int addToPosition) {
    View lastCardAdded = getChildAt(0);
    if (lastCardAdded != null && lastCardAdded.equals(tinderCardView)) {
      return;
    }

    ViewGroup.LayoutParams layoutParams;
    layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    addView(tinderCardView, addToPosition, layoutParams);
  }

  public TinderCardView getTopCardOnStack() {
    return (TinderCardView) getChildAt(getChildCount() - 1);
  }
}
