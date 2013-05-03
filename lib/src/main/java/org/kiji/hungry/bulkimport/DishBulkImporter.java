package com.wibidata.hungry.bulkimport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.io.Text;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import com.wibidata.core.client.EntityId;
import com.wibidata.core.client.lib.bulkimport.BaseTextBulkImporter;
import com.wibidata.hungry.avro.DishIngredient;
import com.wibidata.hungry.avro.DishIngredients;

public class DishBulkImporter extends BaseTextBulkImporter {
  /**
   * This method defines the column or column family that this
   * importer will write to in the target WibiTable.
   *
   * @return The target column this bulk importer writes to.
   */
  @Override
  public String getOutputColumn() throws IOException {
    return "info";
  }

  /**
   * WibiData will call this method once for each line in the input text file.
   * Each line in the input text file is a JSON object that has fields describing the dish,
   * including the wikipedia identifier, human-readable name,
   * description, ingredients, and cuisine.
   *
   * This method should parse the JSON text line to extract relevant
   * information about the dish. These facts about the dish should be
   * written to the columns of a row in the wibi_hungry_dish table.
   *
   * @param line The input text line of JSON (looks like '{cuisine:"chinese", ingredients:[..]}')
   * @param context A helper object used to write to the WibiTable.
   */
  @Override
  public void produce(Text line, Context context) throws IOException, InterruptedException {
    final JsonNode json = parseJson(line.toString());

    // If there aren't any details in the record, then let's just skip it.
    if (null == json.get("details") || null == json.get("details").get("result")) {
      return;
    }

    // Parse the ID of the dish.
    if (null == json.get("details").get("result").get("id")) {
      return;
    }
    final String wikipediaId = json.get("details").get("result").get("id").getTextValue();

    // Use the ID of the dish as the ID of the WibiTable row.
    final EntityId entityId = context.getEntityId(wikipediaId);

    // Write the dish id.
    context.write(entityId, "id", wikipediaId);

    // Write the dish name.
    if (null == json.get("name")) {
      return;
    }
    context.write(entityId, "name", json.get("name").getTextValue());

    // Write the dish ingredients.
    DishIngredients ingredients = getIngredients(json);
    if (null != ingredients) {
      context.write(entityId, "ingredients", ingredients);
    }

    // TODO: Write the dish description.
    // TODO: Write the dish cuisine.
    // TODO: Write the dish thumbnail url.
  }

  /**
   * Parses the JSON line describing a dish.
   *
   * @param jsonInput The JSON string.
   * @return The parsed JSON object.
   */
  private JsonNode parseJson(String jsonInput) throws IOException {
    final ObjectMapper objectMapper = new ObjectMapper();
    return objectMapper.readValue(jsonInput, JsonNode.class);
  }

  /**
   * Gets the ingredients from the json line.
   *
   * @param json The json.
   * @return The parsed DishIngredients as Avro, suitable for writing to a WibiTable cell.
   */
  private DishIngredients getIngredients(JsonNode json) {
    List<DishIngredient> ingredients = new ArrayList<DishIngredient>();
    if (null == json.get("ingredients")) {
      return null;
    }
    for (JsonNode ingredient : json.get("ingredients")) {
      ingredients.add(
          DishIngredient.newBuilder().setName(ingredient.getTextValue().toLowerCase()).build());
    }

    if (ingredients.isEmpty()) {
      return null;
    }

    return DishIngredients.newBuilder()
        .setIngredients(ingredients)
        .build();
  }
}
