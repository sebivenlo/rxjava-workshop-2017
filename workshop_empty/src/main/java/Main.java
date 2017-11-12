import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.Observable;

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

    private static Order parseOrderString(int orderNumber, String s) {
        String[] lineParts = s.split("\\s*,\\s*", 2);
        int mealNR = Integer.parseInt(lineParts[0].trim());
        int servings = Integer.parseInt(lineParts[1].trim());
        System.out.println("Order nr. " + orderNumber + ", ordered: menu nr. " + mealNR + " ," + servings + " servings.");
        return new Order(orderNumber, mealNR, servings);
    }

    public static void main(String[] args) throws FileNotFoundException {
        importMeals();

        Observable<List<String>> o = Observable.create(emitter -> emitter.onNext(Arrays.asList("", "", "")));

//        observable
//                .subscribe(System.out::println);
    }
}
