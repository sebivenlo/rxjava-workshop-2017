import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class OrderLine implements Iterable<Order> {

    private List<Order> orders = new ArrayList<>();
    private int orderNumber;

    OrderLine() {
    }

    OrderLine(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    int getOrderNumber() {
        return orderNumber;
    }

    void addOrder(Order order) {
        orders.add(order);
    }

    @Override
    public Iterator<Order> iterator() {
        return orders.iterator();
    }

    @Override
    public String toString() {
        return Arrays.toString(orders.toArray());
    }
}
