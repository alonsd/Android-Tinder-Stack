package com.etiennelawlor.tinderstack.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.etiennelawlor.tinderstack.R;
import com.etiennelawlor.tinderstack.bus.events.Completion;
import com.etiennelawlor.tinderstack.models.User;
import com.etiennelawlor.tinderstack.ui.TinderCardView;
import com.etiennelawlor.tinderstack.ui.TinderStackLayout;

public class MainActivity extends AppCompatActivity {

  // region Constants
  private static final int STACK_SIZE = 2;
  // endregion

  // region Views
  private TinderStackLayout tinderStackLayout;
  // endregion

  // region Member Variables
  private String[] displayNamesList, userNameList, avatarUrlList;
  private int index = 1;
  // endregion

  // region Listeners
  private Completion completion;

  // endregion
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    displayNamesList = getResources().getStringArray(R.array.display_names);
    userNameList = getResources().getStringArray(R.array.usernames);
    avatarUrlList = getResources().getStringArray(R.array.avatar_urls);

    tinderStackLayout = findViewById(R.id.tinder_stack_layout);

    completion = new Completion() {
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
    addCards(-1);
  }

  private void addCards(int stackSizeToAdd) {
    TinderCardView tinderCardView;
    for (int i = index; index < i + (STACK_SIZE + stackSizeToAdd); index++) {
      if (index >= displayNamesList.length || index >= userNameList.length || index >= avatarUrlList.length){
        index = 0;
        i = 0;
        addCards(-1);
      }
      tinderCardView = new TinderCardView(this, completion);
      tinderCardView.bind(getUser(index));
      tinderStackLayout.addCard(tinderCardView);
    }
  }

  // region Helper Methods
  private User getUser(int index) {
    User user = new User();
    user.setAvatarUrl(avatarUrlList[index]);
    user.setDisplayName(displayNamesList[index]);
    user.setUsername(userNameList[index]);
    return user;
  }
  // endregion
}
