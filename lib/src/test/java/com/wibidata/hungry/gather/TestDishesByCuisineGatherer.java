package com.wibidata.hungry.gather;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.avro.mapred.AvroValue;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mrunit.types.Pair;
import org.junit.Test;

import com.wibidata.core.client.AvroMapReader;
import com.wibidata.core.client.WibiCell;
import com.wibidata.core.client.avro.Node;
import com.wibidata.core.client.test.WibiGathererTester;

import com.wibidata.hungry.avro.DishIngredient;
import com.wibidata.hungry.avro.DishIngredients;

public class TestDishesByCuisineGatherer {
  @Test
  public void testGather() throws IOException {
    // Create a tester.
    WibiGathererTester<Text, AvroValue<Node>> tester
        = new WibiGathererTester<Text, AvroValue<Node>>(new DishesByCuisineGatherer());

    // Create a test input row.
    List<DishIngredient> ingredientList = Arrays.asList(
        DishIngredient.newBuilder().setName("tomato").build(),
        DishIngredient.newBuilder().setName("bean").build());
    DishIngredients ingredients = DishIngredients.newBuilder()
        .setIngredients(ingredientList).build();
    tester.row("/en/minestrone")
        .column("info", "id", "/en/minestrone")
        .column("info", "name", "Minestrone")
        .column("info", "description", "A soup.")
        .column("info", "cuisine", "italian")
        .column("info", "thumbnail", "http://www.google.com/images/minestrone")
        .column("info", "ingredients", new WibiCell(DishIngredients.SCHEMA$, ingredients));

    // Run my gatherer.
    List<Pair<Text, AvroValue<Node>>> output = tester.run();

    // Test that my gatherer output is as expected.
    // It is a list of key/value pairs emitted from your gatherer.
    assertEquals(1, output.size());

    assertEquals("cuisine:italian", output.get(0).getFirst().toString());
    Node graphlet = output.get(0).getSecond().datum();
    assertEquals("cuisine:italian", graphlet.getLabel().toString());
    assertNotNull(graphlet.getEdges());
    assertEquals(1, graphlet.getEdges().size());
    assertEquals("/en/minestrone", graphlet.getEdges().get(0).getTarget().getLabel().toString());
    AvroMapReader<CharSequence> annotations = new AvroMapReader<CharSequence>(
        graphlet.getEdges().get(0).getTarget().getAnnotations());
    assertEquals(3, annotations.size());
    assertTrue(annotations.containsKey("name"));
    assertEquals("Minestrone", annotations.get("name").toString());
    assertTrue(annotations.containsKey("description"));
    assertEquals("A soup.", annotations.get("description").toString());
    assertTrue(annotations.containsKey("thumbnail"));
    assertEquals("http://www.google.com/images/minestrone",
        annotations.get("thumbnail").toString());
  }
}
