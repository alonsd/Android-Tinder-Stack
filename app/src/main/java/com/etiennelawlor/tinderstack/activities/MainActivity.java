package com.etiennelawlor.tinderstack.activities;

import android.os.Bundle;
import android.util.Log;
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


  public static final int POSITION_FIRST = 0;
  private TinderStackLayout tinderStackLayout;
  private ArrayList<User> usersList;

  public UserViewModel userViewModel;

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

  private void fetchUserList() {
    userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
    final Observer<List<User>> userObserver = users -> {
      Log.d("inside observe - ", "inside main activity, db list size - " + users.size());
      usersList = (ArrayList<User>) users;
      Log.d("inside observe - ", "inside main activity, array list size - " + usersList.size());
      addCards();
    };
    userViewModel.getAllUsers().observe(this, userObserver);
  }

  public void addCards(){
    tinderStackLayout.removeAllViews();
    TinderCardView tinderCardView;
    for (int i = 0; i < usersList.size(); i++) {
      tinderCardView = new TinderCardView(this);
      tinderCardView.setUserOnCard(usersList.get(i));
      Log.d("inside observe - ", "inside main activity, user value - " + usersList.get(i).getUsername());
      tinderStackLayout.addCard(tinderCardView, POSITION_FIRST);
    }
  }

  @Override
  public void onClick(View view) {
    int buttonTag = Integer.valueOf(String.valueOf(view.getTag()));
    TinderCardView topCardOnStack = tinderStackLayout.getTopCardOnStack();
    if (topCardOnStack == null) {
      addCards();
      return;
    }
    topCardOnStack.handleButtonPressed(buttonTag);
  }

  private void initViewsAndListeners() {
    tinderStackLayout = findViewById(R.id.activity_main_tinder_stack_layout);
    mDeleteButton = findViewById(R.id.activity_main_delete_button);
    mPassButton = findViewById(R.id.activity_main_pass_button);
    mApproveButton = findViewById(R.id.activity_main_approve_button);
    usersList = new ArrayList<>();
    mDeleteButton.setOnClickListener(this);
    mApproveButton.setOnClickListener(this);
    mPassButton.setOnClickListener(this);
  }
}
