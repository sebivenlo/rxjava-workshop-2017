public class Order {

    private final int mealNR;
    private final int orderNr;
    private int servings;

    Order(int orderNr, int mealNR, int servings) {
        this.orderNr = orderNr;
        this.mealNR = mealNR;
        this.servings = servings;
    }

    int getOrderNr() {
        return orderNr;
    }

    int getMealNR() {
        return mealNR;
    }

    int getServings() {
        return servings;
    }

    @Override
    public String toString() {
        return String.format("Order: %d - Meal Number: %d - Persons: %d", orderNr, mealNR, servings);
    }
}