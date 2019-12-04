package com.etiennelawlor.tinderstack.models.User;

import android.app.AlertDialog;
import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.etiennelawlor.tinderstack.utilities.UserDatabase;

import java.util.List;

public class UserRepository {

  private UserDao userDao;
  private LiveData<List<User>> allUsers;

  public UserRepository(Application application) {
    UserDatabase database = UserDatabase.getInstance(application);
    userDao = database.userDao();
    allUsers = userDao.getAllUsers();
  }

  public void insert(User user) {
    new InsertUserAsyncTask(userDao).execute(user);
  }

  public void update(User user) {
    new UpdateUserAsyncTask(userDao).execute(user);
  }

  public void delete(User user) {
    new DeleteUserAsyncTask(userDao).execute(user);
  }

  public void deleteAllUsers() {
    new DeleteAllUsersAsyncTask(userDao);
  }

  public LiveData<List<User>> getAllUsers() {
    return allUsers;
  }

  private static class InsertUserAsyncTask extends AsyncTask<User, Void, Void> {

    private UserDao userDao;

    private InsertUserAsyncTask(UserDao userDao) {
      this.userDao = userDao;
    }

    @Override
    protected Void doInBackground(User... users) {
      userDao.insert(users[0]);
      return null;
    }
  }

  private static class UpdateUserAsyncTask extends AsyncTask<User, Void, Void> {

    private UserDao userDao;

    private UpdateUserAsyncTask(UserDao userDao) {
      this.userDao = userDao;
    }

    @Override
    protected Void doInBackground(User... users) {
      userDao.update(users[0]);
      return null;
    }
  }

  private static class DeleteUserAsyncTask extends AsyncTask<User, Void, Void> {

    private UserDao userDao;

    private DeleteUserAsyncTask(UserDao userDao) {
      this.userDao = userDao;
    }

    @Override
    protected Void doInBackground(User... users) {
      userDao.delete(users[0]);
      return null;
    }
  }

  private static class DeleteAllUsersAsyncTask extends AsyncTask<Void, Void, Void> {

    private UserDao userDao;

    private DeleteAllUsersAsyncTask(UserDao userDao) {
      this.userDao = userDao;
    }

    @Override
    protected Void doInBackground(Void... voids) {
      userDao.deleteAllUsers();
      return null;
    }
  }





}
