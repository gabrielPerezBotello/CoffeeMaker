package edu.ncsu.csc.CoffeeMaker.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller class for the URL mappings for CoffeeMaker. The controller returns
 * the approprate HTML page in the /src/main/resources/templates folder. For a
 * larger application, this should be split across multiple controllers.
 *
 * @author Kai Presler-Marshall
 */
@Controller
public class MappingController {

    /**
     * On a GET request to /index, the IndexController will return
     * /src/main/resources/templates/index.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/index", "/" } )
    public String index ( final Model model ) {
        return "index";
    }

    /**
     * On a GET request to /recipe, the RecipeController will return
     * /src/main/resources/templates/recipe.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/recipe", "/recipe.html" } )
    public String addRecipePage ( final Model model ) {
        return "recipe";
    }

    /**
     * On a GET request to /deleterecipe, the DeleteRecipeController will return
     * /src/main/resources/templates/deleterecipe.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/deleterecipe", "/deleterecipe.html" } )
    public String deleteRecipeForm ( final Model model ) {
        return "deleterecipe";
    }

    /**
     * On a GET request to /editrecipe, the EditRecipeController will return
     * /src/main/resources/templates/editrecipe.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/editrecipe", "/editrecipe.html" } )
    public String editRecipeForm ( final Model model ) {
        return "editrecipe";
    }

    /**
     * Handles a GET request for inventory. The GET request provides a view to
     * the client that includes the list of the current ingredients in the
     * inventory and a form where the client can enter more ingredients to add
     * to the inventory.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/inventory", "/inventory.html" } )
    public String inventoryForm ( final Model model ) {
        return "inventory";
    }

    /**
     * On a GET request to /makecoffee, the MakeCoffeeController will return
     * /src/main/resources/templates/makecoffee.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/makecoffee", "/makecoffee.html" } )
    public String makeCoffeeForm ( final Model model ) {
        return "makecoffee";
    }

    /**
     * On a GET request to /ingredient, the IngredientController will return
     * /src/main/resources/templates/ingredient.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/ingredient", "/ingredient.html" } )
    public String addIngredientPage ( final Model model ) {
        return "ingredient";
    }

    /**
     * On a GET request to /order, the HomeController will return
     * /src/main/resources/templates/home.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/home", "/home.html" } )
    public String homePage ( final Model model ) {
        return "home";
    }

    /**
     * On a GET request to /order, the OrderController will return
     * /src/main/resources/templates/order.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/vieworders", "/vieworders.html" } )
    public String viewOrdersPage ( final Model model ) {
        return "viewOrders";
    }

    /**
     * On a GET request to /order, the OrderController will return
     * /src/main/resources/templates/order.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/order", "/order.html" } )
    public String orderPage ( final Model model ) {
        return "order";
    }

    /**
     * On a GET request to /index, the IndexController will return
     * /src/main/resources/templates/index.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/pickup", "/pickup.html" } )
    public String pickupPage ( final Model model ) {
        return "pickup";
    }

    /**
     * On a GET request to /addbarista, the addBaristaController will return
     * /src/main/resources/templates/addbarista.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/addbarista", "/addbarista.html" } )
    public String addBaristaPage ( final Model model ) {
        return "addBarista";
    }

    /**
     * On a GET request to /removebarista, the removeBaristaController will
     * return /src/main/resources/templates/removebarista.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/removebarista", "/removebarista.html" } )
    public String removeBaristaPage ( final Model model ) {
        return "removeBarista";
    }

    /**
     * On a GET request to /history, the HistoryController will return
     * /src/main/resources/templates/history.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/history", "/history.html" } )
    public String historyPage ( final Model model ) {
        return "history";
    }
}
