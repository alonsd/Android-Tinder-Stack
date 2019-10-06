package com.etiennelawlor.tinderstack;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.etiennelawlor.tinderstack.models.User;
import com.etiennelawlor.tinderstack.models.UserRepository;

import java.util.List;

public class UserViewModel extends AndroidViewModel {

  private UserRepository repository;
  private LiveData<List<User>> allUsers;

  public UserViewModel(@NonNull Application application) {
    super(application);
    repository = new UserRepository(application);
    allUsers = repository.getAllUsers();

  }

  public void insert(User user) {
    repository.insert(user);
  }

  public void update(User user) {
    repository.update(user);
  }

  public void delete(User user) {
    repository.delete(user);
  }

  public void deleteAllUsers(){
    repository.deleteAllUsers();
  }

  public LiveData<List<User>> getAllUsers() {
    return allUsers;
  }
}
