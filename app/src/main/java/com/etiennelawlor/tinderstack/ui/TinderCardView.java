package com.etiennelawlor.tinderstack.ui;

import android.animation.Animator;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.etiennelawlor.tinderstack.R;
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
  private static final int DURATION = 700;
  public static final int DELETE_BUTTON_PRESSED = 1;
  public static final int PASS_BUTTON_PRESSED = 2;
  public static final int APPROVE_BUTTON_PRESSED = 3;
  public static final int ALPHA_INVISIBLE = 0;
  public static final int ALPHA_VISIBLE = 1;
  //Views
  private ImageView imageView;
  private TextView displayNameTextView;
  private TextView usernameTextView;
  private TextView mContactTextView;
  private TextView mDeleteTextView;
  private TextView mPassTextView;
  private TinderCardView topCard;
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
  private User cardUser;
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
          // cancel any current animations
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
          updateAlphaOfBadges(posX);
          return true;
        default:
          return super.onTouchEvent(motionEvent);
      }
    }
    return super.onTouchEvent(motionEvent);
  }
  public void handleButtonPressed(int buttonTag) {
    TinderStackLayout tinderStackLayout = ((TinderStackLayout) this.getParent());
    TinderCardView topCard = (TinderCardView) tinderStackLayout.getChildAt(tinderStackLayout.getChildCount() - 1);
    if(isCardAnimating) {
      return;
    }
    switch (buttonTag){
      case DELETE_BUTTON_PRESSED:
        isCardAnimating = true;
        deleteCard(topCard);
        break;
      case PASS_BUTTON_PRESSED:
        isCardAnimating = true;
        passCard(topCard);

        break;
      case APPROVE_BUTTON_PRESSED:
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
    String lawyerName = topCard.displayNameTextView.getText().toString();
    fragment.setApprovedLawyer(lawyerName);
    fragment.show(fragmentManager, "");
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
   * This method is being called after we are above a certain boundary (left or right) and animates the card to go off screen to that side.
   */
  private void dismissCard(final View view, int xPos, boolean shouldRemoveView) {
    view.animate()
        .x(xPos)
        .y(0)
        .setInterpolator(new AccelerateInterpolator())
        .setDuration(DURATION)
        .setListener(new Animator.AnimatorListener() {
          @Override
          public void onAnimationStart(Animator animator) {
            isCardAnimating = true;
          }
          @Override
          public void onAnimationEnd(Animator animator) {
            ViewGroup viewGroup = (ViewGroup) view.getParent();
            //Toast.makeText(getContext(),"parent child count - " + viewGroup.getChildCount(), Toast.LENGTH_SHORT).show();
            if (viewGroup != null) {
              isCardAnimating = false;
              if (shouldRemoveView)
                viewGroup.removeView(view);
              else {
                if (view.getParent() != null)
                  viewGroup.removeView(view);
                TinderCardView tinderCardView = new TinderCardView(getContext());
                tinderCardView.bind(cardUser);
                ((TinderStackLayout)viewGroup).addCard(tinderCardView);
//                ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                viewGroup.addView(view,viewGroup.getChildCount() - 1, layoutParams);
              }
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
   * Updates the alpha of the badges - "like" or "nope"
   */
  private void updateAlphaOfBadges(float posX) {
    float alpha = (posX - padding) / (screenWidth * 0.50f);
    mContactTextView.setAlpha(alpha);
    mDeleteTextView.setAlpha(-alpha);
  }
  /**
   * Binds a cardUser object to a card object
   */
  public void bind(User user) {
    if (user == null)
      return;
    cardUser = user;
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

  @Override
  public boolean equals(@Nullable Object other) {
    if (other instanceof TinderCardView) {
      return this.usernameTextView.getText().equals(((TinderCardView) other).usernameTextView.getText());
    }
    return false;
  }
}
