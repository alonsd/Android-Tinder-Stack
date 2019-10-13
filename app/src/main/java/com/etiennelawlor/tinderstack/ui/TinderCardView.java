package com.etiennelawlor.tinderstack.ui;

import android.animation.Animator;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.etiennelawlor.tinderstack.R;
import com.etiennelawlor.tinderstack.activities.MainActivity;
import com.etiennelawlor.tinderstack.models.User.User;
import com.etiennelawlor.tinderstack.utilities.DisplayUtility;
import com.squareup.picasso.Picasso;


public class TinderCardView extends FrameLayout implements View.OnTouchListener {

  //Constants
  private static final float CARD_ROTATION_DEGREES = 40.0f;
  private static final float BADGE_ROTATION_DEGREES = 15.0f;
  private static final int DURATION = 700;
  public static final int DELETE_BUTTON_TAG = 1;
  public static final int PASS_BUTTON_TAG = 2;
  public static final int APPROVE_BUTTON_TAG = 3;
  public static final int ALPHA_INVISIBLE = 0;
  public static final int ALPHA_VISIBLE = 1;
  //Views
  private ImageView mUserPhotoImageView;
  private TextView mDisplayNameTextView;
  private TextView mUsernameTextView;
  private TextView mContactTextView;
  private TextView mDeleteTextView;
  private TextView mPassTextView;
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
  private boolean isCardAnimating;
  private User userOnCard;


  // Constructors
  public TinderCardView(Context context) {
    super(context);
    init(context, null);
  }

  public TinderCardView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(context, attrs);
  }

  public TinderCardView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    init(context, attrs);
  }

  @Override
  public boolean onTouch(final View view, MotionEvent motionEvent) {
    TinderStackLayout tinderStackLayout = ((TinderStackLayout) view.getParent());
    TinderCardView topCard = (TinderCardView) tinderStackLayout.getChildAt(tinderStackLayout.getChildCount() - 1);
    if (topCard.equals(view) && !topCard.isCardAnimating) {
      switch (motionEvent.getAction()) {
        case MotionEvent.ACTION_DOWN:
          oldX = motionEvent.getX();
          oldY = motionEvent.getY();
          view.clearAnimation();
          return true;
        case MotionEvent.ACTION_UP:
          if (isCardBeyondLeftBoundary(view)) {
            dismissCard(view, -(screenWidth * 2), true);
          } else if (isCardBeyondRightBoundary(view)) {
            showLawyerContactDetailsFragment(topCard);
            resetCard(view);
          } else {
            resetCard(view);
          }
          return true;
        case MotionEvent.ACTION_MOVE:
          newX = motionEvent.getX();
          newY = motionEvent.getY();
          dX = newX - oldX;
          dY = newY - oldY;
          float posX = view.getX() + dX;
          // Set new position
          view.setX(view.getX() + dX);
          view.setY(view.getY() + dY);
          setCardRotation(view, view.getX());
          updateAlphaDuringSwipe(posX);
          return true;
        default:
          return super.onTouchEvent(motionEvent);
      }
    }
    return super.onTouchEvent(motionEvent);
  }

  public void handleCardActions(int cardTag) {
    TinderStackLayout tinderStackLayout = ((TinderStackLayout) this.getParent());
    TinderCardView topCard = tinderStackLayout.getTopCardOnStack();
    if (isCardAnimating || topCard == null) {
      return;
    }
    switch (cardTag) {
      case DELETE_BUTTON_TAG:
        isCardAnimating = true;
        deleteCard(topCard);
        break;
      case PASS_BUTTON_TAG:
        isCardAnimating = true;
        passCard(topCard);
        break;
      case APPROVE_BUTTON_TAG:
        showLawyerContactDetailsFragment(topCard);
        break;
    }
  }

  private void deleteCard(TinderCardView topCard) {
    topCard.mDeleteTextView.setAlpha(ALPHA_VISIBLE);
    dismissCard(topCard, -(screenWidth * 2), true);
  }

  private void passCard(TinderCardView topCard) {
    topCard.mPassTextView.setAlpha(ALPHA_VISIBLE);
    dismissCard(topCard, -(screenWidth * 2), false);
  }

  private void showLawyerContactDetailsFragment(TinderCardView topCard) {
    FragmentManager fragmentManager = ((FragmentActivity) getContext()).getSupportFragmentManager();
    ApproveDialogFragment fragment = new ApproveDialogFragment();
    String lawyerName = topCard.mDisplayNameTextView.getText().toString();
    fragment.setApprovedLawyer(lawyerName);
    fragment.show(fragmentManager, "");
  }

  @Override
  protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    setOnTouchListener(null);
  }

  private void init(Context context, AttributeSet attrs) {
    if (!isInEditMode()) {
      inflate(context, R.layout.tinder_card, this);
      mUserPhotoImageView = findViewById(R.id.iv);
      mDisplayNameTextView = findViewById(R.id.display_name_tv);
      mUsernameTextView = findViewById(R.id.username_tv);
      mContactTextView = findViewById(R.id.tinder_card_contact_textview);
      mDeleteTextView = findViewById(R.id.tinder_card_delete_text_view);
      mPassTextView = findViewById(R.id.tinder_card_pass_text_view);
      mContactTextView.setRotation(-(BADGE_ROTATION_DEGREES));
      mDeleteTextView.setRotation(BADGE_ROTATION_DEGREES);
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
   * This method is being called either after we are above a certain boundary (left or right) or when clicking the dedicated swipe buttons for pass/delete actions.
   * Later this method animates the card to go off screen to the selected side.
   */
  private void dismissCard(final View view, int xPos, boolean shouldDeleteView) {
    view.animate()
        .x(xPos)
        .setInterpolator(new AccelerateInterpolator())
        .setDuration(DURATION)
        .setListener(new Animator.AnimatorListener() {
          @Override
          public void onAnimationStart(Animator animator) {
            isCardAnimating = true;
          }

          @Override
          public void onAnimationEnd(Animator animator) {
            handleViewAfterAnimation(view, shouldDeleteView);
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
   * This method handles passing or deleting the currently visible card view
   * @param view - the view currently being passed/deleted
   * @param shouldDeleteView - determines if we should delete the view as a child or put it back inside the stack
   */
  private void handleViewAfterAnimation(View view, boolean shouldDeleteView) {
    isCardAnimating = false;
    TinderStackLayout tinderStackLayout = (TinderStackLayout) view.getParent();
    if (tinderStackLayout == null)
      return;
    if (shouldDeleteView) {
      //`delete` button pressed
      if (view instanceof TinderCardView) {
        ((MainActivity) getContext()).mUserViewModel.delete(((TinderCardView) view).getUserOnCard());
        tinderStackLayout.removeView(view);
        return;
      }
    }
    //`pass` button pressed
    tinderStackLayout.removeView(view);
    if (tinderStackLayout.getTopCardOnStack() == null) {
      ((MainActivity)getContext()).addCards();
    }
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
    mContactTextView.setAlpha(ALPHA_INVISIBLE);
    mDeleteTextView.setAlpha(ALPHA_INVISIBLE);
    mPassTextView.setAlpha(ALPHA_INVISIBLE);
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
   * Updates the alpha of the badges - "delete" or "pass"
   */
  private void updateAlphaDuringSwipe(float posX) {
    float alpha = (posX - padding) / (screenWidth * 0.50f);
    mContactTextView.setAlpha(alpha);
    mDeleteTextView.setAlpha(-alpha);
  }


  private void setUserImage(ImageView imageView, User user) {
    String avatarUrl = user.getAvatarUrl();
    if (!TextUtils.isEmpty(avatarUrl)) {
      Picasso.get()
          .load(avatarUrl)
          .into(imageView);
    }
  }

  private void setUserDisplayName(TextView tv, User user) {
    String displayName = user.getDisplayName();
    if (!TextUtils.isEmpty(displayName)) {
      tv.setText(displayName);
    }
  }

  private void setUsername(TextView tv, User user) {
    String username = user.getUsername();
    if (!TextUtils.isEmpty(username)) {
      tv.setText(username);
    }
  }

  @Override
  public boolean equals(@Nullable Object other) {
    if (other instanceof TinderCardView) {
      return this.mUsernameTextView.getText().equals(((TinderCardView) other).mUsernameTextView.getText());
    }
    return false;
  }

  public User getUserOnCard() {
    return userOnCard;
  }

  /**
   * Binds a user object to a card object
   */
  public void setUserOnCard(User user) {
    this.userOnCard = user;
    if (user == null)
      return;
    setUserImage(mUserPhotoImageView, user);
    setUserDisplayName(mDisplayNameTextView, user);
    setUsername(mUsernameTextView, user);
  }
}
