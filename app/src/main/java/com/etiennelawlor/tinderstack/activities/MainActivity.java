package com.etiennelawlor.tinderstack.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;


import com.etiennelawlor.tinderstack.R;
import com.etiennelawlor.tinderstack.bus.events.OnCardSwipedListener;
import com.etiennelawlor.tinderstack.models.User.User;
import com.etiennelawlor.tinderstack.models.ViewModel.UserViewModel;
import com.etiennelawlor.tinderstack.ui.TinderCardView;
import com.etiennelawlor.tinderstack.ui.TinderStackLayout;
import java.util.ArrayList;
import butterknife.BindView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

  private static final int STACK_SIZE = 2;

  private TinderStackLayout tinderStackLayout;
  private OnCardSwipedListener listener;
  private UserViewModel userViewModel;
  private ArrayList<User> usersList;
  private int index = 1;

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

    observeLiveData();

    initViewsAndListeners();
  }

  private void initViewsAndListeners() {
    tinderStackLayout = findViewById(R.id.activity_main_tinder_stack_layout);
    mDeleteButton = findViewById(R.id.activity_main_delete_button);
    mPassButton = findViewById(R.id.activity_main_pass_button);
    mApproveButton = findViewById(R.id.activity_main_approve_button);
    mDeleteButton.setOnClickListener(this);
    mApproveButton.setOnClickListener(this);
    mPassButton.setOnClickListener(this);
    listener = new OnCardSwipedListener() {
      @Override
      public void send(Object object) {

      }

      @Override
      public void onNext(Integer integer) {


        if (integer == 1) {
          addCards(1);
        }

      }
    };
  }

  private void observeLiveData() {
    userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
    userViewModel.getAllUsers().observe(this, users -> {
      //update a new list of users
      usersList = (ArrayList) users;
      addCards(-1);
    });
  }

  private void addCards(int stackSizeToAdd) {
    TinderCardView tinderCardView;
    for (int i = index; index < i + (STACK_SIZE + stackSizeToAdd); index++) {
      if (index >= usersList.size()) {
        index = 0;
        i = 0;
        addCards(-1);
      }
      tinderCardView = new TinderCardView(this, listener);
      tinderCardView.bind(usersList.get(index));
      tinderStackLayout.addCard(tinderCardView);
    }
  }

  @Override
  public void onClick(View view) {
    TinderCardView topCardOnStack = tinderStackLayout.getTopCardOnStack();
    switch (view.getId()) {
      case R.id.activity_main_approve_button:
        //show alert dialog - open a dialog with contact information but do nothing with the stack

        break;
      case R.id.activity_main_pass_button:
        //next card should appear - this is not a neutral button
        topCardOnStack.handleButtonPressed(Integer.valueOf(String.valueOf(view.getTag())));
        break;
      case R.id.activity_main_delete_button:
        //next card should appear - this is a 'dislike' button
        topCardOnStack.handleButtonPressed(Integer.valueOf(String.valueOf(view.getTag())));
        break;

    }
  }
}
