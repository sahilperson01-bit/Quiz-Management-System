package model;

public interface IQuestion {
    void display();
    boolean checkAnswer(String userAns);
    int getId();
    String getQuestionText();
}
