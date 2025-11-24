package model;

public abstract class Question implements IQuestion {
    protected int id;
    protected String questionText;

    public Question(int id, String questionText) {
        this.id = id;
        this.questionText = questionText;
    }

    public int getId() { return id; }
    public String getQuestionText() { return questionText; }

    public abstract void display();
}
