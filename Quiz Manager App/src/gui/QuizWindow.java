package gui;

import model.MCQQuestion;
import model.Question;
import model.User;
import service.QuizManager;
import service.TimerThread;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

public class QuizWindow extends JFrame {
    private QuizManager manager = new QuizManager();
    private User user;
    private List<Question> questions;
    private int currentIndex = 0;
    private int score = 0;

    // UI
    private JLabel qLabel = new JLabel();
    private JRadioButton ra = new JRadioButton();
    private JRadioButton rb = new JRadioButton();
    private JRadioButton rc = new JRadioButton();
    private JRadioButton rd = new JRadioButton();
    private ButtonGroup bg = new ButtonGroup();
    private JLabel timerLabel = new JLabel("Time: --");

    private TimerThread timer;

    public QuizWindow(User user) {
        this.user = user;
        setTitle("Quiz - " + user.getUsername());
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        initUI();
        loadQuestionsAndStart();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        JPanel top = new JPanel(new BorderLayout());
        top.add(timerLabel, BorderLayout.EAST);
        add(top, BorderLayout.NORTH);

        JPanel center = new JPanel(new GridLayout(5,1));
        center.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        center.add(qLabel);
        bg.add(ra); bg.add(rb); bg.add(rc); bg.add(rd);
        center.add(ra); center.add(rb); center.add(rc); center.add(rd);
        add(center, BorderLayout.CENTER);

        JPanel bottom = new JPanel();
        JButton next = new JButton("Next");
        bottom.add(next);
        add(bottom, BorderLayout.SOUTH);

        next.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                submitAnswerAndNext();
            }
        });
    }

    private void loadQuestionsAndStart() {
        try {
            manager.loadQuestions();
            questions = manager.getShuffledQuestions();
                    if (questions == null || questions.isEmpty()) {
            throw new SQLException("No questions available in database");
        }
            startTimer(300); // 5 minutes for whole quiz
            showQuestion(0);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to load questions: " + e.getMessage());
        }
    }

    private void startTimer(int seconds) {
        timer = new TimerThread(seconds, new TimerThread.TimerListener() {
            @Override
            public void onTick(int secondsLeft) {
                SwingUtilities.invokeLater(() -> timerLabel.setText("Time: " + secondsLeft + "s"));
            }
            @Override
            public void onFinish() {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(QuizWindow.this, "Time's up!");
                    finishQuiz();
                });
            }
        });
        timer.start();
    }

    private void showQuestion(int idx) {
        if (idx < 0 || idx >= questions.size()) return;
        currentIndex = idx;
        Question q = questions.get(idx);
        qLabel.setText((idx+1) + ". " + q.getQuestionText());
        if (q instanceof MCQQuestion) {
            MCQQuestion mq = (MCQQuestion) q;
            ra.setText("A) " + mq.getA());
            rb.setText("B) " + mq.getB());
            rc.setText("C) " + mq.getC());
            rd.setText("D) " + mq.getD());
            bg.clearSelection();
        }
    }

    private void submitAnswerAndNext() {
        Question q = questions.get(currentIndex);
        String selected = null;
        if (ra.isSelected()) selected = "A";
        else if (rb.isSelected()) selected = "B";
        else if (rc.isSelected()) selected = "C";
        else if (rd.isSelected()) selected = "D";

        if (selected != null && q.checkAnswer(selected)) {
            score++;
        }
        if (currentIndex + 1 < questions.size()) {
            showQuestion(currentIndex + 1);
        } else {
            finishQuiz();
        }
    }

    private void finishQuiz() {
        if (timer != null) timer.stopTimer();
        try {
            manager.saveUserScore(user, score, questions.size());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to store score: " + e.getMessage());
        }
        JOptionPane.showMessageDialog(this, "Quiz finished! Score: " + score + "/" + questions.size());
        dispose();
    }
}

