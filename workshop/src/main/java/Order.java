public class Order {

    private final int mealNR;

    private int persons;

    public int getOrderNr() {
        return orderNr;
    }

    private final int orderNr;

    public int getMealNR() {
        return mealNR;
    }

    public int getPersons() {
        return persons;
    }

    public Order(int orderNr, int mealNR, int persons) {
        super();
        this.orderNr = orderNr;
        this.mealNR = mealNR;
        this.persons = persons;
    }

    void addPersons(int nr) {
        this.persons += nr;
    }

    void decPersons(int nr) {
        this.persons -= nr;
    }

    @Override
    public String toString() {
        return "Order: " + orderNr + "\tMeal Number: " + mealNR + "\tPersons: " + persons;
    }
}