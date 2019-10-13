package com.etiennelawlor.tinderstack.utilities;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.etiennelawlor.tinderstack.models.User.User;
import com.etiennelawlor.tinderstack.models.User.UserDao;

import java.util.ArrayList;
import java.util.List;


@Database(entities = {User.class}, version = 1, exportSchema = false)
public abstract class UserDatabase extends RoomDatabase {

  private static UserDatabase instance;

  public abstract UserDao userDao();

  public static synchronized UserDatabase getInstance(Context context) {

    Log.d("inside observe - ", "inside database");

    if (instance == null) {
      instance = Room.databaseBuilder(context.getApplicationContext(), UserDatabase.class, "user_database").fallbackToDestructiveMigration().addCallback(roomUserCallback).build();
    }
    return instance;
  }

  private static RoomDatabase.Callback roomUserCallback = new RoomDatabase.Callback() {

    @Override
    public void onCreate(@NonNull SupportSQLiteDatabase db) {
      super.onCreate(db);
      new PopulateDbAsyncTask(instance).execute();
    }
  };

  //TODO - delete this in the future. This is just for populating.
  private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {

    static final String URL = "https://www.shortlist.com/media/images/2019/05/40-favourite-songs-of-famous-people-28-1556672663-9rFo-column-width-inline.jpg";
    static final String URL2 = "https://img-s-msn-com.akamaized.net/tenant/amp/entityid/BBR9VUw.img?h=416&amp;w=624&amp;m=6&amp;q=60&amp;u=t&amp;o=f&amp;l=f&amp;x=2232&amp;y=979";
    static final String URL3 = "https://dz9yg0snnohlc.cloudfront.net/new-what-famous-people-with-depression-have-said-about-the-condition-1.jpg";
    private UserDao userDao;

    private PopulateDbAsyncTask(UserDatabase db) {
      userDao = db.userDao();
    }

    @Override
    protected Void doInBackground(Void... voids) {
      List<User> users = new ArrayList<>();
      users.add(new User(URL, "Barak Obama1", "/@BarakObama1"));
      users.add(new User(URL2, "Barak Obama2", "/@BarakObama2"));
      users.add(new User(URL3, "Barak Obama3", "/@BarakObama3"));
      userDao.insertListOfUsers(users);
      return null;
    }
  }
}
