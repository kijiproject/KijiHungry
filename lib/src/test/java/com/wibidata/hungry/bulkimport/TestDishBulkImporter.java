package com.wibidata.hungry.bulkimport;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.junit.Ignore;
import org.junit.Test;

import com.wibidata.core.client.WibiBulkImporter;
import com.wibidata.core.client.WibiRowData;
import com.wibidata.core.client.test.WibiBulkImporterTester;

import com.wibidata.hungry.avro.DishIngredient;
import com.wibidata.hungry.avro.DishIngredients;

/**
 * Tests the functionality of the DishBulkImporter.
 */
public class TestDishBulkImporter {
  @Ignore("Remove this line to enable this test.")
  @Test
  public void testBulkImport() throws IOException {
    // Create the importer we'd like to test.
    final WibiBulkImporter importerToTest = new DishBulkImporter();

    // Creating a testing harness.
    final WibiBulkImporterTester<LongWritable, Text> tester =
        new WibiBulkImporterTester<LongWritable, Text>(importerToTest);

    // Use a sample input record from our file as input to the importer.
    tester.withInput(new LongWritable(0L), new Text(getSampleInputLine()));

    // Run the importer using the test harness.
    // The imported data is returned in a map:
    //   - key: The WibiTable row key (id).
    //   - value: The contents of the row after running the importer.
    final Map<String, WibiRowData> output = tester.run();

    // Verify that the data was imported to the correct row.
    assertTrue(output.containsKey("/en/minestrone"));
    final WibiRowData row = output.get("/en/minestrone");

    assertTrue(row.containsColumn("info", "id"));
    assertEquals("/en/minestrone", row.getStringValue("info", "id").toString());

    assertTrue(row.containsColumn("info", "cuisine"));
    assertEquals("italian", row.getStringValue("info", "cuisine").toString());

    assertTrue(row.containsColumn("info", "ingredients"));
    DishIngredients ingredients = row.getValue("info", "ingredients", DishIngredients.class);
    List<DishIngredient> ingredientList = ingredients.getIngredients();
    assertEquals(4, ingredientList.size());
    assertEquals("tomato", ingredientList.get(0).getName().toString());
    assertEquals("bean", ingredientList.get(1).getName().toString());
    assertEquals("parmigiano reggiano", ingredientList.get(2).getName().toString());
    assertEquals("pasta", ingredientList.get(3).getName().toString());
  }

  private String getSampleInputLine() throws IOException {
    final String resourceName = "com/wibidata/hungry/bulkimport/sampleInputRecord.json";
    return IOUtils.toString(getClass().getClassLoader().getResource(resourceName));
  }
}
