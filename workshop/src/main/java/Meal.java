public final class Meal {

    private final int number;

    private final String name;

    private final int orderNR;

    public Meal(int orderNR, int number, String name) {
        this.orderNR = orderNR;
        this.number = number;
        this.name = name;
    }

    public int getNumber() {
        return number;
    }

    @Override
    public String toString() {
        return orderNR + ": \t" + name + " (" + number + ")";
    }

    public String getName() {
        return name;
    }

    public int getOrderNR() {
        return orderNR;
    }
}
