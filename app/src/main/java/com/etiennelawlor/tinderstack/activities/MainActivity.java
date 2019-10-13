package com.etiennelawlor.tinderstack.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.etiennelawlor.tinderstack.R;
import com.etiennelawlor.tinderstack.models.User.User;
import com.etiennelawlor.tinderstack.models.ViewModel.UserViewModel;
import com.etiennelawlor.tinderstack.ui.TinderCardView;
import com.etiennelawlor.tinderstack.ui.TinderStackLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{


  private TinderStackLayout mStackLayout;
  private ArrayList<User> mUsersList;

  public UserViewModel mUserViewModel;

  @BindView(R.id.activity_main_delete_button)
  Button mDeleteButton;

  @BindView(R.id.activity_main_pass_button)
  Button mPassButton;

  @BindView(R.id.activity_main_approve_button)
  Button mApproveButton;



  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    initViewsAndListeners();
    fetchUserList();
  }


  /**
   * This method is observing the database and adds child views if there are any changes to the data.
   */
  private void fetchUserList() {
    mUserViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
    final Observer<List<User>> userObserver = users -> {
      mUsersList = (ArrayList<User>) users;
      addCards();
    };
    mUserViewModel.getAllUsers().observe(this, userObserver);
  }

  public void addCards(){
    mStackLayout.removeAllViews();
    TinderCardView tinderCardView;
    for (int i = 0; i < mUsersList.size(); i++) {
      tinderCardView = new TinderCardView(this);
      tinderCardView.setUserOnCard(mUsersList.get(i));
      mStackLayout.addCard(tinderCardView, 0);
    }
  }

  @Override
  public void onClick(View view) {
    int buttonTag = Integer.valueOf(String.valueOf(view.getTag()));
    TinderCardView topCardOnStack = mStackLayout.getTopCardOnStack();
    if (topCardOnStack == null) {
      addCards();
      return;
    }
    topCardOnStack.handleCardActions(buttonTag);
  }

  private void initViewsAndListeners() {
    mStackLayout = findViewById(R.id.activity_main_tinder_stack_layout);
    mDeleteButton = findViewById(R.id.activity_main_delete_button);
    mPassButton = findViewById(R.id.activity_main_pass_button);
    mApproveButton = findViewById(R.id.activity_main_approve_button);
    mUsersList = new ArrayList<>();
    mDeleteButton.setOnClickListener(this);
    mApproveButton.setOnClickListener(this);
    mPassButton.setOnClickListener(this);
  }
}
