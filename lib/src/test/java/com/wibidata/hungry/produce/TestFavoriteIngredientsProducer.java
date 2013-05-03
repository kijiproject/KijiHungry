package com.wibidata.hungry.produce;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;

import com.wibidata.core.client.WibiCell;
import com.wibidata.core.client.WibiRowData;
import com.wibidata.core.client.test.InMemoryKeyValueStore;
import com.wibidata.core.client.test.WibiProducerTester;
import com.wibidata.hungry.avro.DishIngredient;
import com.wibidata.hungry.avro.DishIngredients;
import com.wibidata.hungry.avro.DishRating;
import com.wibidata.hungry.avro.FavoriteIngredients;

public class TestFavoriteIngredientsProducer {
  @Ignore("Remove this line to enable this test.")
  @Test
  public void testProduce() throws IOException {
    // Create a tester.
    WibiProducerTester tester = new WibiProducerTester(new FavoriteIngredientsProducer());
    InMemoryKeyValueStore<String, DishIngredients> ingredientsByDish =
        new InMemoryKeyValueStore<String, DishIngredients>();
    List<DishIngredient> ingredientList = Arrays.asList(
        DishIngredient.newBuilder().setName("chocolate").build(),
        DishIngredient.newBuilder().setName("ice cream").build());
    ingredientsByDish.put("/en/sundae",
        DishIngredients.newBuilder().setIngredients(ingredientList).build());
    tester.bindStore("ingredients-by-dish", ingredientsByDish);

    // Create an input row.
    DishRating rating = DishRating.newBuilder()
        .setDishId("/en/sundae")
        .setDishName("Sundae")
        .setValue(1)
        .build();
    tester.row("a_user")
        .column("rating", "/en/sundae", new WibiCell<DishRating>(DishRating.SCHEMA$, rating));

    // Run my producer.
    Map<String, WibiRowData> output = tester.run();

    // Test that my producer output is as expected.
    // It is a map from row key to your producer-generated output.
    assertTrue(output.containsKey("a_user"));
    WibiRowData row = output.get("a_user");
    assertTrue(row.containsColumn("personalization", "favorite_ingredients"));
    FavoriteIngredients favoriteIngredients =
        row.getValue("personalization", "favorite_ingredients", FavoriteIngredients.class);
    assertNotNull(favoriteIngredients);
    assertNotNull(favoriteIngredients.getIngredients());
    assertFalse(favoriteIngredients.getIngredients().isEmpty());
    assertEquals(2, favoriteIngredients.getIngredients().size());

    assertEquals("chocolate", favoriteIngredients.getIngredients().get(0).getName().toString());
    assertEquals(1.0, favoriteIngredients.getIngredients().get(0).getWeight(), 1e-8);
    assertEquals("ice cream", favoriteIngredients.getIngredients().get(1).getName().toString());
    assertEquals(1.0, favoriteIngredients.getIngredients().get(1).getWeight(), 1e-8);
  }
}
