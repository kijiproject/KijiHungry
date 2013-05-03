package com.wibidata.hungry.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.wibidata.core.client.AvroMapReader;
import com.wibidata.core.client.avro.Edge;
import com.wibidata.core.client.avro.Node;

public class Cuisine {
  private String mName;
  private List<Dish> mDishes;

  public Cuisine() {
    mDishes = new ArrayList<Dish>();
  }

  public static Cuisine fromDishes(Node node) {
    Cuisine cuisine = new Cuisine();
    cuisine.setName(StringUtils.removeStart(node.getLabel().toString(), "cuisine:"));
    for (Edge edge : node.getEdges()) {
      Dish dish = new Dish();
      dish.setId(edge.getTarget().getLabel().toString());
      AvroMapReader<CharSequence> annotations =
          new AvroMapReader(edge.getTarget().getAnnotations());
      dish.setName(annotations.get("name").toString());
      dish.setDescription(annotations.get("description").toString());
      dish.setThumbnail(annotations.get("thumbnail").toString());
      cuisine.addDish(dish);
    }
    return cuisine;
  }

  public void setName(String name) {
    mName = name;
  }

  public String getName() {
    return mName;
  }

  public List<Dish> getDishes() {
    return mDishes;
  }

  public void addDish(Dish dish) {
    mDishes.add(dish);
  }
}
