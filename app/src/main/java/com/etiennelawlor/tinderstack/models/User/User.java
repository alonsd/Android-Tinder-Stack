package com.etiennelawlor.tinderstack.models.User;


import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "user_table")
public class User {

  @PrimaryKey(autoGenerate = true)
  private int id;

  private String avatarUrl;
  private String displayName;
  private String username;

  @Ignore
  public User() {

  }

  public User(String avatarUrl, String displayName, String username) {
    this.avatarUrl = avatarUrl;
    this.displayName = displayName;
    this.username = username;
  }

  public String getAvatarUrl() {
    return avatarUrl;
  }

  public String getDisplayName() {
    return displayName;
  }

  public String getUsername() {
    return username;
  }

  public int getId() {
    return id;
  }

  public void setAvatarUrl(String avatarUrl) {
    this.avatarUrl = avatarUrl;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public void setId(int id) {
    this.id = id;
  }

  @Override
  public boolean equals(@Nullable Object other) {
    if (other instanceof User) {
      return this.id == ((User) other).getId();
    }
    return false;
  }
}
