package restaurant;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * A collection of meals times servings.
 * Supports iteration.
 * @author ode
 */
public class Order implements Iterable<OrderLine>{
    /** The sequence number of the orders. */
    private final int number;
    /** The lines in the order. Any collection implementation will do. */
    private final List<OrderLine> lines = new LinkedList<OrderLine>();

    /**
     * Create an empty order with a sequence number.
     * @param number order number
     */
    Order(int number) {
        super();
        this.number = number;
    }

    /**
     * Add a meal with specified portions or servings.
     * @param mealNR meal number from the menu
     * @param servings
     */
    void addMeal(int mealNR, int servings) {
        lines.add(new OrderLine(number, mealNR, servings));
    }

    /**
     * Change number of servings.
     * @param mealNR
     * @param persons
     */
    void addMealPersons(int mealNR, int persons) {
        // TODO
    }

    void delMealPersons(Meal meal, int persons) {
        // TODO
    }
    /**
     * Get the order number.
     * @return
     */
    int getNumber() {
        return number;
    }

    /**
     * Return an iterator for this collection of order lines.
     * @return
     */
    @Override
    public Iterator<OrderLine> iterator() {
        return lines.iterator();
    }
}
