import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        Observable<String[]> observable = Observable.create(observableEmitter -> {
            observableEmitter.onNext(new String[]{"11, 7", "13, 4", "10, 500"});
            observableEmitter.onNext(new String[]{"44, 4 ", "47, 8", " 13, 9 ", "11, 5"});
            observableEmitter.onComplete();
        });
        observable.scan(new Indexed<>(0, new String[]{"43, 1"}), (prev, v) -> new Indexed<>(prev.index + 1, v))
                .flatMap(indexed -> {
                    ArrayList<Indexed<String>> aggregate = new ArrayList<>();
                    for (String item : indexed.item) aggregate.add(new Indexed<>(indexed.index, item));
                    return Observable.fromIterable(aggregate);
                })
                .map(s -> {
                    String[] lineParts = s.item.split("\\s*,\\s*", 2);
                    int mealNR;
                    int servings;
                    mealNR = Integer.parseInt(lineParts[0].trim());
                    servings = Integer.parseInt(lineParts[1].trim());
                    Thread.sleep((long) (Math.random() * 1000));
                    System.out.println("Order nr. " + s.index + ", ordered: menu nr. " + mealNR + " ," + servings + " servings.");
                    return new Order(s.index, mealNR, servings);
                })
                .map(s -> {
                    Thread.sleep(100);
                    return new Meal(s.getOrderNr(), s.getMealNR(), "TBD");
                })
                .subscribe(System.out::println);
    }
}
