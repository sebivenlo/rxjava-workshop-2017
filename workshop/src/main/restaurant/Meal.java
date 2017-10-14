public final class Meal {

    private final int number;
    private final String name;
    private final int orderNR;

    Meal(int orderNR, int number, String name) {
        this.orderNR = orderNR;
        this.number = number;
        this.name = name;
    }

    @Override
    public String toString() {
        return String.format("%d: %s (%d)", orderNR, name, number);
    }

    public String getName() {
        return name;
    }
}
