package com.etiennelawlor.tinderstack.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.FrameLayout;

import com.etiennelawlor.tinderstack.bus.events.OnCardSwipedListener;
import com.etiennelawlor.tinderstack.utilities.DisplayUtility;


public class TinderStackLayout extends FrameLayout {

  // Constants
  private static final int DURATION = 300;

  // Variable members
  private OnCardSwipedListener onCardSwipedListener;
  private int screenWidth;
  private int yMultiplier;

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
  public void addView(View child, int index, ViewGroup.LayoutParams params) {
    super.addView(child, index, params);
    if (onCardSwipedListener != null)
      onCardSwipedListener.onNext(getChildCount());
  }

  @Override
  public void removeView(View view) {
    super.removeView(view);
    if (onCardSwipedListener != null)
      onCardSwipedListener.onNext(getChildCount());
  }

  @Override
  public void onDetachedFromWindow() {
    super.onDetachedFromWindow();
  }

  // Helper Methods
  private void init() {
    setClipChildren(false);

    screenWidth = DisplayUtility.getScreenWidth(getContext());
    yMultiplier = DisplayUtility.dp2px(getContext(), 8);

  }

  public void addCard(TinderCardView tinderCardView) {
    if (onCardSwipedListener == null)
      onCardSwipedListener = tinderCardView.getOnCardSwipedListener();

    View topCard = getChildAt(0);
    if (topCard != null && topCard.equals(tinderCardView)) {
      return;
    }
    topCardOnStack = tinderCardView;

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

  public TinderCardView getTopCardOnStack() {
    return topCardOnStack;
  }
}
