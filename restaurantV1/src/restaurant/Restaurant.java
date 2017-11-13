package restaurant;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Naive restaurant for concurrency lab.
 *
 * @author ode
 * @author hom
 */

public class Restaurant {
    /**
     * Name of the joint.
     */
    private String name;
    /**
     * Last order number to keep track of the orders.
     */
    private int orderCount;
    /**
     * The cookbook a.k.a. preparation rules.
     */
    private Map<Integer, Recipe> recipes;
    /**
     * What can be ordered here
     */
    private ArrayList<String> menu;
    /**
     * Queue of orders.
     */
    private Queue<Order> orderQueue;
    /**
     * Queue of meals.
     */
    private Queue<Meal> mealsReadyQueue;
    /**
     * Format for our price list and bills
     */
    private final DecimalFormat df = new DecimalFormat("##.00");

    /**
     * Helper method for output formatting.
     */
    public static void printSeparator() {
        System.out
                .println("===================================="
                        + "==================================");
    }

    /**
     * Construct a named restaurant.
     *
     * @param name
     */
    public Restaurant(String name) {
        this.name = name;
        orderCount = 0;
        orderQueue = new Queue<Order>();
        mealsReadyQueue = new Queue<Meal>();
        recipes = new HashMap<Integer, Recipe>();
        menu = new ArrayList<String>();
        importMeals();
    }

    /**
     * Import the meals.
     * Putting meals in a separate text file avoid hard coding of meal properties
     * into this source code. It would acquire a bad smell over time ;-)).
     */
    final void importMeals() {
        Collection<String> mealInfo = Input.getMeals("meals.txt");
        for (String s : mealInfo) {
            String[] ss = s.split("#");
            int mealNR = Integer.parseInt(ss[0]);
            String mname = ss[1];
            float price = Float.parseFloat(ss[2]);
            int prepTime = Integer.parseInt(ss[3]);
            recipes.put(mealNR, new Recipe(mname, prepTime));
            menu.add(mealNR + ":\t" + mname + "\tprice:\t" + df.format(price));
        }
    }

    /**
     * Place order.
     * An order is a list of strings, each string interpreted
     * as an integer pair, separated by a comma and possibly white space.
     * The first integer is the meal number from the menu,
     * the second value is the number of servings.
     *
     * @param ordered array of strings
     * @return the next order number
     * @throws RestaurantException
     */
    public int submitOrder(String... ordered) throws RestaurantException {
        orderCount++;
        Order order = new Order(orderCount);
        for (int i = 0; i < ordered.length; i++) {
            String[] lineParts = ordered[i].split("\\s*,\\s*", 2);
            int mealNR = 0;
            int servings = 0;
            try {
                mealNR = Integer.parseInt(lineParts[0].trim());
                servings = Integer.parseInt(lineParts[1].trim());
            } catch (NumberFormatException nfe) {
                throw new RestaurantException(nfe);
            }
            if (!recipes.containsKey(mealNR)) {
                throw new RestaurantException("Order nr. " + orderCount +
                        ": a non existing meal (nr.=" + mealNR + ") ordered!");
            }
            order.addMeal(mealNR, servings);
            System.out.println("Order nr. " + orderCount
                    + ", ordered: menu nr. " + mealNR + " ,"
                    + servings + " servings.");
        }
        orderQueue.put(order);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return orderCount;
    }

    /**
     * Are there orders in work?
     *
     * @return
     */
    public boolean hasOrders() {
        return !orderQueue.empty();
    }

    /**
     * Removes one order from the order list.
     *
     * @return info for the next order
     */
    public String getNextOrder() {
        return orderQueue.get().toString();
    }

    /**
     * Process the orders in FIFO order.
     */
    public void processOrders() {
        while (!orderQueue.empty()) {
            Order order = orderQueue.get();
            int orderNR = order.getNumber();
            for (OrderLine ol : order) { //while (order.hasOrderLines()) {
                //OrderLine ol = order.getOrderLine();
                int mealNR = ol.getMealNR();
                int persons = ol.getPersons();
                for (int p = 0; p < persons; p++) {
                    this.mealsReadyQueue.put(this.prepareMeal(orderNR, mealNR));
                }
            }
        }
    }

    /**
     * Prepares a meal according to recipe (preparation time).
     *
     * @param orderNR
     * @param mealNR
     * @return the prepared meal.
     */
    private Meal prepareMeal(int orderNR, int mealNR) {
        Recipe recipe = recipes.get(mealNR);
        String mealName = recipe.getName();
        int procTime = recipe.getPreparationTime();
        try {
            Thread.sleep(procTime);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return new Meal(orderNR, mealNR, mealName);
    }

    /**
     * Is there anything to serve?
     *
     * @return true if there are meals that can be obtained via the getMeals
     * method
     */
    public boolean hasMeals() {
        return !mealsReadyQueue.empty();
    }

    /**
     * Get the next meal to serve.
     *
     * @return information of the next meal belonging to a certain order
     */
    public String getNextMeal() {
        try {
            Thread.sleep(150);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Meal meal = mealsReadyQueue.get();
        return meal.toString();
    }

    /**
     * Print the menu of the restaurant.
     * Each meal has a number and this number
     * should be used to order a meal.
     */
    public void printMenu() {
        System.out.println("Menu of restaurant " + name);
        printSeparator();
        Set<Integer> mealNRs = recipes.keySet();
        for (String line : menu) {
            System.out.println(line);
        }
    }

    /**
     * String representation of the restaurant.
     *
     * @return a string.
     */
    @Override
    public String toString() {
        return "Restaurant " + name;
    }
}