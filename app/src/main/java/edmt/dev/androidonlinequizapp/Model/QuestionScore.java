package edmt.dev.androidonlinequizapp.Model;

public class QuestionScore {
    private String QuestionScore;
    private String User;
    private String Score;
    private String Bal;

    public QuestionScore() {
    }

    public QuestionScore(String questionScore, String user, String score) {
        QuestionScore = questionScore;
        User = user;
        Score = score;

    }

    public String getQuestionScore() {
        return QuestionScore;
    }

    public void setQuestionScore(String questionScore) {
        QuestionScore = questionScore;
    }

    public String getUser() {
        return User;
    }

    public void setUser(String user) {
        User = user;
    }

    public String getScore() {
        return Score;
    }

    public void setScore(String score) {
        Score = score;
    }

}
