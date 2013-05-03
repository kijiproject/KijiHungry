package com.wibidata.hungry.model;

import java.util.List;

public class Dish {
  private String mId;
  private String mName;
  private String mDescription;
  private String mThumbnail;
  private List<Ingredient> mIngredients;
  private String mCuisine;

  public Dish() {
  }

  public void setId(String id) {
    mId = id;
  }

  public String getId() {
    return mId;
  }

  public void setName(String name) {
    mName = name;
  }

  public String getName() {
    return mName;
  }

  public void setDescription(String description) {
    mDescription = description;
  }

  public String getDescription() {
    return mDescription;
  }

  public void setThumbnail(String thumbnail) {
    mThumbnail = thumbnail;
  }

  public String getThumbnail() {
    return mThumbnail;
  }

  public void setCuisine(String cuisine) {
    mCuisine = cuisine;
  }

  public String getCuisine() {
    return mCuisine;
  }

  public void setIngredients(List<Ingredient> ingredients) {
    mIngredients = ingredients;
  }

  public List<Ingredient> getIngredients() {
    return mIngredients;
  }
}
