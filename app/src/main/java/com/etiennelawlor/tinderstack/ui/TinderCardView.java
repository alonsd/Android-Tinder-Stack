package com.etiennelawlor.tinderstack.ui;

import android.animation.Animator;
import android.content.Context;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.etiennelawlor.tinderstack.R;
import com.etiennelawlor.tinderstack.bus.events.OnCardSwipedListener;
import com.etiennelawlor.tinderstack.bus.events.TopCardMovedEvent;
import com.etiennelawlor.tinderstack.models.User.User;
import com.etiennelawlor.tinderstack.utilities.DisplayUtility;
import com.squareup.picasso.Picasso;

/**
 * Created by etiennelawlor on 11/17/16.
 */

public class TinderCardView extends FrameLayout implements View.OnTouchListener {
  private static final String TAG = "TinderCardView";

  //Constants
  private static final float CARD_ROTATION_DEGREES = 40.0f;
  private static final float BADGE_ROTATION_DEGREES = 15.0f;
  private static final int DURATION = 300;

  //Views
  private ImageView imageView;
  private TextView displayNameTextView;
  private TextView usernameTextView;
  private TextView likeTextView;
  private TextView nopeTextView;

  // Member Variables
  private float oldX;
  private float oldY;
  private float newX;
  private float newY;
  private float dX;
  private float dY;
  private float rightBoundary;
  private float leftBoundary;
  private int screenWidth;
  private int padding;
  private OnCardSwipedListener onCardSwipedListener;

  // Constructors
  public TinderCardView(Context context, OnCardSwipedListener onCardSwipedListener) {
    super(context);
    this.onCardSwipedListener = onCardSwipedListener;
    init(context, null);
  }

  public TinderCardView(Context context, AttributeSet attrs, OnCardSwipedListener onCardSwipedListener) {
    super(context, attrs);
    this.onCardSwipedListener = onCardSwipedListener;
    init(context, attrs);
  }

  public TinderCardView(Context context, AttributeSet attrs, int defStyle, OnCardSwipedListener onCardSwipedListener) {
    super(context, attrs, defStyle);
    this.onCardSwipedListener = onCardSwipedListener;
    init(context, attrs);
  }

  // View.OnTouchListener Methods
  @Override
  public boolean onTouch(final View view, MotionEvent motionEvent) {
    TinderStackLayout tinderStackLayout = ((TinderStackLayout) view.getParent());
    TinderCardView topCard = (TinderCardView) tinderStackLayout.getChildAt(tinderStackLayout.getChildCount() - 1);
    if (topCard.equals(view)) {
      switch (motionEvent.getAction()) {
        case MotionEvent.ACTION_DOWN:
          oldX = motionEvent.getX();
          oldY = motionEvent.getY();

          // cancel any current animations
          view.clearAnimation();
          return true;
        case MotionEvent.ACTION_UP:
          if (isCardBeyondLeftBoundary(view)) {
            onCardSwipedListener.send(new TopCardMovedEvent(-(screenWidth)));
            dismissCard(view, -(screenWidth * 2));
          } else if (isCardBeyondRightBoundary(view)) {
            onCardSwipedListener.send(new TopCardMovedEvent(-(screenWidth)));
            dismissCard(view, (screenWidth * 2));
          } else {
            onCardSwipedListener.send(new TopCardMovedEvent(-(screenWidth)));
            resetCard(view);
          }
          return true;
        case MotionEvent.ACTION_MOVE:
          newX = motionEvent.getX();
          newY = motionEvent.getY();

          dX = newX - oldX;
          dY = newY - oldY;

          float posX = view.getX() + dX;

          onCardSwipedListener.send(new TopCardMovedEvent(-(screenWidth)));

          // Set new position
          view.setX(view.getX() + dX);
          view.setY(view.getY() + dY);

          setCardRotation(view, view.getX());

          updateAlphaOfBadges(posX);
          return true;
        default:
          return super.onTouchEvent(motionEvent);
      }
    }
    return super.onTouchEvent(motionEvent);
  }

  public void handleButtonPressed(int choosenButton) {
    TinderStackLayout tinderStackLayout = ((TinderStackLayout) this.getParent());
    TinderCardView topCard = (TinderCardView) tinderStackLayout.getChildAt(tinderStackLayout.getChildCount() - 1);

    switch (choosenButton){
      case 1: //delete button
        updateAlphaOfBadges(topCard.getX());
        dismissCard(topCard, -(screenWidth * 2));
        break;
      case 2: //pass button
        updateAlphaOfBadges(topCard.getX());
        dismissCard(topCard, (screenWidth * 2));
        break;

      case 3: // approve button

        break;
    }

  }


  @Override
  protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    setOnTouchListener(null);
  }

  // Helper Methods
  private void init(Context context, AttributeSet attrs) {
    if (!isInEditMode()) {
      inflate(context, R.layout.tinder_card, this);

      imageView = findViewById(R.id.iv);
      displayNameTextView = findViewById(R.id.display_name_tv);
      usernameTextView = findViewById(R.id.username_tv);
      likeTextView = findViewById(R.id.like_tv);
      nopeTextView = findViewById(R.id.nope_tv);

      likeTextView.setRotation(-(BADGE_ROTATION_DEGREES));
      nopeTextView.setRotation(BADGE_ROTATION_DEGREES);

      screenWidth = DisplayUtility.getScreenWidth(context);
      leftBoundary = screenWidth * (1.0f / 6.0f); // Left 1/6 of screen
      rightBoundary = screenWidth * (5.0f / 6.0f); // Right 1/6 of screen
      padding = DisplayUtility.dp2px(context, 16);

      setOnTouchListener(this);
    }
  }

  // Check if card's middle is beyond the left boundary
  private boolean isCardBeyondLeftBoundary(View view) {
    return (view.getX() + (view.getWidth() / 2) < leftBoundary);
  }

  // Check if card's middle is beyond the right boundary
  private boolean isCardBeyondRightBoundary(View view) {
    return (view.getX() + (view.getWidth() / 2) > rightBoundary);
  }


  /**
   * This method is being called after we are above a certain boundary (left or right) and animates the card to go off screen to that side.
   */
  private void dismissCard(final View view, int xPos) {
    view.animate()
        .x(xPos)
        .y(0)
        .setInterpolator(new AccelerateInterpolator())
        .setDuration(DURATION)
        .setListener(new Animator.AnimatorListener() {
          @Override
          public void onAnimationStart(Animator animator) {

          }

          @Override
          public void onAnimationEnd(Animator animator) {
            ViewGroup viewGroup = (ViewGroup) view.getParent();
            if (viewGroup != null) {
              viewGroup.removeView(view);
            }
          }

          @Override
          public void onAnimationCancel(Animator animator) {

          }

          @Override
          public void onAnimationRepeat(Animator animator) {

          }
        });
  }


  /**
   * This method resets the card back to it's place because we tried to swipe it up or down.
   */
  private void resetCard(View view) {
    view.animate()
        .x(0)
        .y(0)
        .rotation(0)
        .setInterpolator(new OvershootInterpolator())
        .setDuration(DURATION);

    likeTextView.setAlpha(0);
    nopeTextView.setAlpha(0);
  }


  /**
   * Sets card rotation after we swipe to a legal direction (left or right)
   */
  private void setCardRotation(View view, float posX) {
    float rotation = (CARD_ROTATION_DEGREES * (posX)) / screenWidth;
    int halfCardHeight = (view.getHeight() / 2);
    if (oldY < halfCardHeight - (2 * padding)) {
      view.setRotation(rotation);
    } else {
      view.setRotation(-rotation);
    }
  }

  /**
   * Updates the alpha of the badges - "like" or "nope"
   */
  private void updateAlphaOfBadges(float posX) {
    float alpha = (posX - padding) / (screenWidth * 0.50f);
    likeTextView.setAlpha(alpha);
    nopeTextView.setAlpha(-alpha);
  }


  /**
   * Binds a user object to a card object
   */
  public void bind(User user) {
    if (user == null)
      return;

    setUpImage(imageView, user);
    setUpDisplayName(displayNameTextView, user);
    setUpUsername(usernameTextView, user);
  }

  private void setUpImage(ImageView iv, User user) {
    String avatarUrl = user.getAvatarUrl();
    if (!TextUtils.isEmpty(avatarUrl)) {
      Picasso.get()
          .load(avatarUrl)
          .into(iv);
    }
  }

  private void setUpDisplayName(TextView tv, User user) {
    String displayName = user.getDisplayName();
    if (!TextUtils.isEmpty(displayName)) {
      tv.setText(displayName);
    }
  }

  private void setUpUsername(TextView tv, User user) {
    String username = user.getUsername();
    if (!TextUtils.isEmpty(username)) {
      tv.setText(username);
    }
  }


  public OnCardSwipedListener getOnCardSwipedListener() {
    return onCardSwipedListener;
  }
}
