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

import java.util.Properties;

public class SentimentAnalyzer {
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
        }, BackpressureStrategy.BUFFER);
    }

    public static void main(String[] args) {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, parse, sentiment");
        pipeline = new StanfordCoreNLP(props);

        ConnectableFlowable<Status> observable = tweetObservable().publish();
        observable.connect();
        Disposable trumpTweets = observable
                .filter(status -> status.getText().toLowerCase().contains("trump"))
                .parallel()
                .runOn(Schedulers.computation())
                .map(status -> status.getText().replaceAll("[^\\p{ASCII}]", ""))
                .map(status -> new Tweet(status, sentiment(status)))
                .sequential()
                .subscribe(tweet -> System.out.println(tweet.getSentiment() + " : " + tweet.getText()));
//        Flowable<Tweet> positiveTweets = trumpTweets.filter(tweet -> tweet.getSentiment().equals("positive"));
//        Flowable<Tweet> neutralTweets = trumpTweets.filter(tweet -> tweet.getSentiment().equals("neutral"));
//        Flowable<Tweet> negativeTweets = trumpTweets.filter(tweet -> tweet.getSentiment().equals("negative"));
//        positiveTweets.subscribe(tweet -> System.out.println("positive: " + tweet.getText()));
//        neutralTweets.subscribe(tweet -> System.out.println("neutral: " + tweet.getText()));
//        negativeTweets.subscribe(tweet -> System.out.println("negative:" + tweet.getText()));
    }
}
