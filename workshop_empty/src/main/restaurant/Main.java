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

    public static void main(String[] args) throws FileNotFoundException, InterruptedException {
        importMeals();

        System.out.println(System.currentTimeMillis());

        observable
                .subscribe(System.out::println, throwable -> System.out.println("Got error: " + throwable.getMessage()), () -> System.out.println(System.currentTimeMillis()));
        Thread.sleep(7000);
    }
}
