import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;
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
import java.util.Properties;

public class Main {
    private static StanfordCoreNLP pipeline;

    private static int sentiment(String input) {
        int mainSentiment = 0;
        if (input != null && input.length() > 0) {
            int longest = 0;
            Annotation annotation = pipeline.process(input);
            for (CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
                Tree tree = sentence.get(SentimentCoreAnnotations.SentimentAnnotatedTree.class);
                int sentiment = RNNCoreAnnotations.getPredictedClass(tree);
                String partText = sentence.toString();
                if (partText.length() > longest) {
                    mainSentiment = sentiment;
                    longest = partText.length();
                }
            }
        }
        return mainSentiment;
    }

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
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, parse, sentiment");
        pipeline = new StanfordCoreNLP(props);

        ConnectableFlowable<Status> observable = tweetObservable().publish();
        observable.connect();
        Disposable tweets = observable
                .observeOn(Schedulers.computation())
                // .filter(status -> status.getText().toLowerCase().contains("something"))
                .map(status -> status.getText().replaceAll("[^\\p{ASCII}]", ""))
                .map(status -> {
                    Thread.sleep(1000);
                    return new Tweet(status, sentiment(status));
                })
                .subscribe(tweet -> System.out.println(tweet.getSentiment() + " : " + tweet.getText()), System.out::println);
    }
}
