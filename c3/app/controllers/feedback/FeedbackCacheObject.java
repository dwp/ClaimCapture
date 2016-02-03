package controllers.feedback;

public class FeedbackCacheObject {
    private long datesecs;
    private String origin;
    private int satisfiedScore;
    private String difficulty;
    private String comment;
    private String useragent;

    public FeedbackCacheObject() {
    }

    public long getDatesecs() {
        return datesecs;
    }

    public void setDatesecs(long datesecs) {
        this.datesecs = datesecs;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public int getSatisfiedScore() {
        return satisfiedScore;
    }

    public void setSatisfiedScore(int satisfiedScore) {
        this.satisfiedScore = satisfiedScore;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUseragent() {
        return useragent;
    }

    public void setUseragent(String useragent) {
        this.useragent = useragent;
    }
}
