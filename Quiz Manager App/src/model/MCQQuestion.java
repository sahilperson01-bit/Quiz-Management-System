package model;

public class MCQQuestion extends Question {
    private String a, b, c, d, answer;

    public MCQQuestion(int id, String questionText, String a, String b, String c, String d, String answer) {
        super(id, questionText);
        this.a = a; this.b = b; this.c = c; this.d = d; this.answer = answer;
    }

    @Override
    public void display() {
        System.out.println(questionText);
        System.out.println("A) " + a);
        System.out.println("B) " + b);
        System.out.println("C) " + c);
        System.out.println("D) " + d);
    }

    @Override
    public boolean checkAnswer(String userAns) {
        return answer != null && userAns != null && answer.equalsIgnoreCase(userAns.trim());
    }

    // getters used by DAO
    public String getA() { return a; }
    public String getB() { return b; }
    public String getC() { return c; }
    public String getD() { return d; }
    public String getAnswer() { return answer; }
}
