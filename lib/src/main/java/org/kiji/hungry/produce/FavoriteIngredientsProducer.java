package com.wibidata.hungry.produce;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;

import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wibidata.core.client.KeyValueStore;
import com.wibidata.core.client.WibiColumnName;
import com.wibidata.core.client.WibiDataRequest;
import com.wibidata.core.client.WibiProducer;
import com.wibidata.core.client.WibiRowData;
import com.wibidata.core.client.lib.kvstore.WibiTableKeyValueStore;
import com.wibidata.hungry.avro.DishIngredient;
import com.wibidata.hungry.avro.DishIngredients;
import com.wibidata.hungry.avro.DishRating;
import com.wibidata.hungry.avro.FavoriteIngredient;
import com.wibidata.hungry.avro.FavoriteIngredients;

public class FavoriteIngredientsProducer extends WibiProducer {
  private static final Logger LOG = LoggerFactory.getLogger(FavoriteIngredientsProducer.class);
  private static final long RECENT_MILLIS = 1000L * 3600L * 24L * 90L;

  @Override
  public WibiDataRequest getDataRequest() {
    return new WibiDataRequest()
        .addColumn(new WibiDataRequest.Column("rating"))
        .withTimeRange(System.currentTimeMillis() - RECENT_MILLIS, HConstants.LATEST_TIMESTAMP);
  }

  @Override
  public String getOutputColumn() {
    return "personalization:favorite_ingredients";
  }

  @Override
  public Map<String, KeyValueStore<?, ?>> getRequiredStores() {
    WibiTableKeyValueStore<DishIngredients> store = new WibiTableKeyValueStore<DishIngredients>(
        "wibi_hungry_dish", new WibiColumnName("info", "ingredients"));
    store.setConf(HBaseConfiguration.addHbaseResources(getConf()));

    return Collections.<String, KeyValueStore<?, ?>>singletonMap(
        "ingredients-by-dish", store);
  }

  @Override
  public void produce(WibiRowData input, Context context)
      throws IOException, InterruptedException {
    if (!input.containsColumn("rating")) {
      LOG.info("User has no ratings.");
      return;
    }

    // Read the user's recent ratings.
    NavigableMap<String, DishRating> dishRatings =
        input.getRecentValues("rating", DishRating.class);
    LOG.info("Found " + dishRatings.size() + " ratings.");

    // TODO: Step 10.
    // Get the ingredients for each positively rated dish.
    // Weight and sort the ingredients by the number of times they appear in positive ratings.
    // Write them to the wibi table in a FavoriteIngredients record by calling context.write().
  }

  private List<String> getIngredients(String dishId, Context context)
      throws IOException, InterruptedException {
    LOG.info("Looking up ingredients for dish " + dishId);
    DishIngredients ingredients =
        context.<String, DishIngredients>getStore("ingredients-by-dish").get(dishId);
    if (null == ingredients) {
      return Collections.emptyList();
    }
    LOG.info("Found " + ingredients.getIngredients().size() + " ingredients.");
    List<String> names = new ArrayList<String>();
    for (DishIngredient dishIngredient : ingredients.getIngredients()) {
      names.add(dishIngredient.getName().toString());
    }
    return names;
  }
}
