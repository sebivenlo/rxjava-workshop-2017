class Tweet {
    private String text;
    private int sentiment;

    Tweet(String text, int sentiment) {
        this.text = text;
        this.sentiment = sentiment;
    }

    public String getSentiment() {
        switch (sentiment) {
            case 0:
                return "very negative";
            case 1:
                return "negative";
            case 2:
                return "neutral";
            case 3:
                return "positive";
            case 4:
                return "very positive";
            default:
                return "undetermined";
        }
    }

    public String getText() {
        return text;
    }
}
