import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;

import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Flowable<Integer> observable = Flowable.create((FlowableEmitter<Integer> emitter) -> {
            for (int i = 0; i < 100; i++) {
                Thread.sleep((int) (Math.random() * 10));
                emitter.onNext(i);
            }
            emitter.onComplete();
        }, BackpressureStrategy.BUFFER).debounce(8, TimeUnit.MILLISECONDS);

        observable
                .filter(n -> n % 3 == 0)
                .map(s -> s + "d")
                .map(s -> s.substring(0, s.length() - 1))
                .map(Integer::valueOf)
                .subscribe(
                    (s) -> System.out.println("Incoming: " + s),
                    (e) -> System.out.println("Something went wrong: " + e.getMessage()),
                    () -> System.out.println("This observable is finished")
        );

        Thread.sleep(1000);
    }
}
