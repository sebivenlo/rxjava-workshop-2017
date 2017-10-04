public class OrderLine {
    private Order[] orders;
    private int orderNumber;

    public Order[] getOrders() {
        return orders;
    }

    public void setOrders(Order[] orders) {
        this.orders = orders;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public OrderLine(Order[] orders, int orderNumber) {
        this.orders = orders;
        this.orderNumber = orderNumber;
    }
}
