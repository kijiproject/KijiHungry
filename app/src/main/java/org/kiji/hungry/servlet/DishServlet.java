package com.wibidata.hungry.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

import com.wibidata.core.client.LocalWibiTableReader;
import com.wibidata.core.client.LocalWibiTableWriter;
import com.wibidata.core.client.Wibi;
import com.wibidata.core.client.WibiDataRequest;
import com.wibidata.core.client.WibiDataRequestException;
import com.wibidata.core.client.WibiRowData;
import com.wibidata.core.client.WibiTable;
import com.wibidata.core.client.WibiTableReader;
import com.wibidata.core.client.WibiTableWriter;
import com.wibidata.hungry.WibiContextListener;
import com.wibidata.hungry.avro.DishIngredient;
import com.wibidata.hungry.avro.DishIngredients;
import com.wibidata.hungry.avro.DishRating;
import com.wibidata.hungry.model.Dish;
import com.wibidata.hungry.model.Ingredient;

/**
 * The dish page servlet (where we show dish details).
 */
public class DishServlet extends HttpServlet {
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {

    request.setCharacterEncoding("UTF-8");

    final String dishId = request.getParameter("id");

    final Wibi wibi = (Wibi) getServletContext().getAttribute(WibiContextListener.WIBI_ATTRIBUTE);

    // TODO: Step 8. Display the dish details.

    // Rate the item if necessary.
    final String rating = request.getParameter("rate");
    final boolean isLoggedIn = null != request.getSession().getAttribute("login");
    if (isLoggedIn) {
      if (null != rating) {
        // TODO: Step 9. Write the rating to the user table.
      }

      // TODO: Step 9 BONUS. If the user has rated this dish before, set the
      // 'currentRating' request attribute so it can be reflected in the view.
    }

    request.getRequestDispatcher("/WEB-INF/view/Dish.jsp").forward(request, response);
  }

  /**
   * Parses an Avro DishIngredients record into a regular list of Ingredient model objects so
   * it can be safely rendered in a JSP page.
   *
   * @param ingredients The dish ingredients avro record from the Wibi table.
   * @return A list of ingredient model objects.
   */
  private List<Ingredient> getIngredients(DishIngredients ingredients) {
    List<Ingredient> ingredientList = new ArrayList<Ingredient>();
    if (null != ingredients.getIngredients()) {
      for (DishIngredient dishIngredient : ingredients.getIngredients()) {
        Ingredient ingredient = new Ingredient();
        ingredient.setName(dishIngredient.getName().toString());
        ingredientList.add(ingredient);
      }
    }
    return ingredientList;
  }
}
