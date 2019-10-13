package com.etiennelawlor.tinderstack.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.FrameLayout;


public class TinderStackLayout extends FrameLayout {


  //Top card
  private TinderCardView topCardOnStack;



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

  // Helper Methods
  private void init() {
    setClipChildren(false);

  }

  public void addCard(TinderCardView tinderCardView) {
    View topCard = getChildAt(0);
    if (topCard != null && topCard.equals(tinderCardView)) {
      return;
    }

    topCardOnStack = tinderCardView;

    ViewGroup.LayoutParams layoutParams;
    layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

    addView(tinderCardView, 0, layoutParams);

    tinderCardView.animate()
        .x(0)
        .setInterpolator(new AnticipateOvershootInterpolator());
  }

  public TinderCardView getTopCardOnStack() {
    return topCardOnStack;
  }
}
