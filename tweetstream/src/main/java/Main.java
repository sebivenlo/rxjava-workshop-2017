import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import io.reactivex.flowables.ConnectableFlowable;
import io.reactivex.schedulers.Schedulers;
import twitter4j.Status;
import twitter4j.StatusAdapter;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;

import java.lang.management.ManagementFactory;

public class Main {
    private static Flowable<Status> tweetObservable() {
        return Flowable.create(subscriber -> {
            final TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
            twitterStream.addListener(new StatusAdapter() {
                public void onStatus(Status status) {
                    subscriber.onNext(status);
                }

                public void onException(Exception ex) {
                    subscriber.onError(ex);
                }
            });
            twitterStream.sample();
        }, BackpressureStrategy.ERROR);
    }

    public static void main(String[] args) {
        System.out.println(ManagementFactory.getRuntimeMXBean().getName());

        ConnectableFlowable<Status> observable = tweetObservable().publish();
        observable.connect();
        Disposable tweets = observable
                .observeOn(Schedulers.computation())
                // .filter(status -> status.getText().toLowerCase().contains("something"))
                .map(status -> status.getText().replaceAll("[^\\p{ASCII}]", ""))
                .map(status -> {
                    Thread.sleep(1000);
                    return status.toUpperCase();
                })
                .subscribe(System.out::println, System.out::println);
    }
}
