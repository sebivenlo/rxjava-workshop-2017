import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Main {

    private static Map<Integer, Recipe> recipes = new HashMap<>();

    private static Collection<String> getMeals() throws FileNotFoundException {
        List<String> menu = new ArrayList<>();
        Scanner reader = new Scanner(new File("meals.txt"));
        while (reader.hasNext()) menu.add(reader.nextLine());
        return menu;
    }

    private static void importMeals() throws FileNotFoundException {
        Collection<String> mealInfo = getMeals();
        for (String s : mealInfo) {
            String[] data = s.split("#");
            int mealNR = Integer.parseInt(data[0]);
            String menu_name = data[1];
            int prepTime = Integer.parseInt(data[3]);
            recipes.put(mealNR, new Recipe(menu_name, prepTime));
        }
    }

    private static long fibonacci(int n) {
        if (n <= 1) return n;
        else return fibonacci(n - 1) + fibonacci(n - 2);
    }

    private static Meal processMeal(Order o) throws InterruptedException {
        Recipe r = recipes.get(o.getMealNR());
        long fib = fibonacci(40);
        Thread.sleep(r.getPreparationTime());
        return new Meal(o.getOrderNr(), o.getMealNR(), r.getName());
    }

    private static Order parseOrderString(int orderNumber, String s) throws NumberFormatException {
        String[] lineParts = s.split("\\s*,\\s*", 2);
        int mealNR = Integer.parseInt(lineParts[0].trim());
        int servings = Integer.parseInt(lineParts[1].trim());
        System.out.println("Order nr. " + orderNumber + ", ordered: menu nr. " + mealNR + " ," + servings + " servings.");
        return new Order(orderNumber, mealNR, servings);
    }

    public static void main(String[] args) throws FileNotFoundException, InterruptedException {
        importMeals();

        Flowable<List<String>> observable = Flowable.create(emitter -> {
            emitter.onNext(Arrays.asList("1a, 7", "13, 4"));
            emitter.onNext(Arrays.asList("44, 4", "47, 8", " 13, 9", "11, 5"));
            emitter.onNext(Arrays.asList("45, 1", " 47, 2", " 10, 2"));
            emitter.onNext(Arrays.asList("10, 2", " 33, 2", " 19, 2"));
            emitter.onComplete();
        }, BackpressureStrategy.MISSING);

        System.out.println(System.currentTimeMillis());

        observable
                // Make an group of orders from the input string
                // This is necessary to determine which group-order the individual orders belong to
                .scan(new OrderLine(), (prev, current) -> {
                    OrderLine ol = new OrderLine(prev.getOrderNumber() + 1);
                    for (String order : current)
                        try {
                            ol.addOrder(parseOrderString(ol.getOrderNumber(), order));
                        } catch (NumberFormatException ignored) {
                            System.out.println("skipped un-parseable order '" + order + "'");
                        }
                    Thread.sleep(20);
                    return ol;
                })
                // Split the group-order into individual orders
                .flatMap(Flowable::fromIterable)
                // Remove invalid orders (non-existent meals). This is in opposition to the error handling in the regular version
                .filter(order -> recipes.containsKey(order.getMealNR()))
                // Orders contain requests for multiple meals
                // Flatten the individual orders into single-meal orders
                .flatMap(order -> {
                    ArrayList<Order> result = new ArrayList<>();
                    for (int i = 0; i < order.getServings(); i++)
                        result.add(new Order(order.getOrderNr(), order.getMealNR(), 1));
                    return Flowable.fromIterable(result);
                })
                // Enter the parallel part of the flow
                // This will turn every Flowable<Order> emitted by the above statement
                // into a ParallelFlowable<Order>, which has a more limited set of actions
                // RxJava will create "rails" (threads in a thread pool) and will dispatch
                // every incoming object into one of these rails for processing
                // Without a parameter, the level of parallelism will be equal
                // to the amount of cores in the system
                .parallel()
                // parallel() itself only prepares the dispatching into different threads
                // but does not add parallelism in itself
                // runOn() defines which kind of thread pool to run on
                // computation in this case is a bound thread pool for high-cpu-load threads
                // io is an unbound thread pool for threads who wait a lot (e.g. for IO)
                .runOn(Schedulers.computation())
                // Turns every Order into a Meal
                // A computationally expensive operation was added for demonstration purposes
                .map(Main::processMeal)
                // You have a choice of how to go about things:
                // A.) Parallelize every single meal
                // B.) Parallelize every order (handle the meals inside the order sequentially)
                // B is closer to reality (in that a cook would likely prepare the batch of an order and the move on)
                // A is faster because meals can be spread out individually to more threads and as such are done quicker
//                .map(order -> {
//                    ArrayList<Meal> result = new ArrayList<>();
//                    Recipe r = recipes.get(order.getMealNR());
//                    for (int i = 0; i < order.getServings(); i++) {
//                        long fib = fibonacci(40);
//                        result.add(new Meal(order.getOrderNr(), order.getMealNR(), r.getName()));
//                        Thread.sleep(r.getPreparationTime());
//                    }
//                    return result;
//                })
//                .flatMap(Flowable::fromIterable)
                // Flattens the parallel results from the different "rails" into a single stream again
                .sequential()
                // Self explanatory
                .subscribe(System.out::println, throwable -> System.out.println("Got error: " + throwable.getMessage()), () -> System.out.println(System.currentTimeMillis()));
        // You can actually see the number of threads in the output
        // as meals are printed in batches of (in my case) 8
        // You can also see the thread pool in your task manager,
        // where it will create two "java" processes, one without load (the main process)
        // and one with full load (the worker thread pool)
        // This is necessary because the above flow runs on another thread
        // and the main program will exit immediately otherwise
        Thread.sleep(7000);
    }
}
