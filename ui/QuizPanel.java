package com.quiz.ui;

import com.quiz.dao.QuizDAO;
import com.quiz.dao.ResultDAO;
import com.quiz.model.Question;
import com.quiz.model.Quiz;
import com.quiz.util.QuizTimer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuizPanel extends JPanel {
    private MainFrame mainFrame;
    private Quiz quiz;
    private List<Question> questions;
    private int currentQuestionIndex = 0;
    private Map<Integer, Integer> answers = new HashMap<>(); // QuestionID -> SelectedOption
    private QuizTimer quizTimer;
    private Thread timerThread;

    private JLabel questionLabel;
    private JRadioButton[] options;
    private ButtonGroup optionsGroup;
    private JLabel timerLabel;
    private JButton nextButton;
    private JButton submitButton;

    public QuizPanel(MainFrame mainFrame, int quizId) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        setBackground(StyleConstants.getSecondaryColor());
        
        try {
            this.quiz = new QuizDAO().getQuizById(quizId);
            this.questions = quiz.getQuestions();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading quiz");
            return;
        }

        // Top Panel (Timer & Header)
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(StyleConstants.getPrimaryColor());
        topPanel.setBorder(new EmptyBorder(15, 20, 15, 20));
        
        JLabel titleLabel = new JLabel(quiz.getTitle());
        titleLabel.setFont(StyleConstants.HEADER_FONT);
        titleLabel.setForeground(Color.WHITE);
        topPanel.add(titleLabel, BorderLayout.WEST);
        
        JPanel rightTopPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightTopPanel.setOpaque(false);

        timerLabel = new JLabel("Time Left: 10:00");
        timerLabel.setFont(StyleConstants.SUBHEADER_FONT);
        timerLabel.setForeground(Color.WHITE);
        rightTopPanel.add(timerLabel);

        JButton backButton = StyleConstants.createStyledButton("Exit Quiz", Color.RED);
        backButton.addActionListener(e -> confirmExit());
        rightTopPanel.add(backButton);

        topPanel.add(rightTopPanel, BorderLayout.EAST);
        
        add(topPanel, BorderLayout.NORTH);

        // Center Panel (Question Card)
        JPanel centerContainer = new JPanel(new GridBagLayout());
        centerContainer.setBackground(StyleConstants.getSecondaryColor());
        
        JPanel questionCard = new JPanel();
        questionCard.setLayout(new BoxLayout(questionCard, BoxLayout.Y_AXIS));
        questionCard.setBackground(StyleConstants.getCardColor());
        questionCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(30, 40, 30, 40)
        ));
        
        questionLabel = new JLabel();
        questionLabel.setFont(StyleConstants.SUBHEADER_FONT);
        questionLabel.setForeground(StyleConstants.getTextColor());
        questionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        questionCard.add(questionLabel);
        questionCard.add(Box.createVerticalStrut(20));
        
        options = new JRadioButton[4];
        optionsGroup = new ButtonGroup();
        for (int i = 0; i < 4; i++) {
            options[i] = new JRadioButton();
            options[i].setFont(StyleConstants.BODY_FONT);
            options[i].setBackground(StyleConstants.getCardColor());
            options[i].setForeground(StyleConstants.getTextColor());
            options[i].setFocusPainted(false);
            options[i].setAlignmentX(Component.LEFT_ALIGNMENT);
            optionsGroup.add(options[i]);
            questionCard.add(options[i]);
            questionCard.add(Box.createVerticalStrut(10));
        }
        
        centerContainer.add(questionCard);
        add(centerContainer, BorderLayout.CENTER);

        // Bottom Panel (Navigation)
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 20));
        bottomPanel.setBackground(StyleConstants.getCardColor());
        bottomPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));
        
        nextButton = StyleConstants.createStyledButton("Next Question", StyleConstants.getPrimaryColor());
        nextButton.addActionListener(e -> nextQuestion());
        
        submitButton = StyleConstants.createStyledButton("Submit Quiz", StyleConstants.getAccentColor());
        submitButton.addActionListener(e -> submitQuiz());
        submitButton.setEnabled(false);

        bottomPanel.add(nextButton);
        bottomPanel.add(submitButton);
        add(bottomPanel, BorderLayout.SOUTH);

        loadQuestion(0);
        startTimer();
    }

    private void confirmExit() {
        int choice = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to exit? Your progress will be lost.", 
            "Exit Quiz", 
            JOptionPane.YES_NO_OPTION);
            
        if (choice == JOptionPane.YES_OPTION) {
            if (quizTimer != null) quizTimer.stop();
            mainFrame.showStudentDashboard();
        }
    }

    private void startTimer() {
        quizTimer = new QuizTimer(600, timerLabel, this::submitQuiz); // 10 minutes
        timerThread = new Thread(quizTimer);
        timerThread.start();
    }

    private void loadQuestion(int index) {
        if (index < 0 || index >= questions.size()) return;
        
        Question q = questions.get(index);
        questionLabel.setText((index + 1) + ". " + q.getQuestionText());
        options[0].setText(q.getOption1());
        options[1].setText(q.getOption2());
        options[2].setText(q.getOption3());
        options[3].setText(q.getOption4());
        
        optionsGroup.clearSelection();
        if (answers.containsKey(q.getQuestionId())) {
            options[answers.get(q.getQuestionId()) - 1].setSelected(true);
        }

        if (index == questions.size() - 1) {
            nextButton.setEnabled(false);
            submitButton.setEnabled(true);
        } else {
            nextButton.setEnabled(true);
            submitButton.setEnabled(false);
        }
    }

    private void nextQuestion() {
        saveAnswer();
        currentQuestionIndex++;
        loadQuestion(currentQuestionIndex);
    }

    private void saveAnswer() {
        for (int i = 0; i < 4; i++) {
            if (options[i].isSelected()) {
                answers.put(questions.get(currentQuestionIndex).getQuestionId(), i + 1);
                break;
            }
        }
    }

    private void submitQuiz() {
        saveAnswer(); // Save last question
        if (quizTimer != null) quizTimer.stop();
        
        int score = 0;
        for (Question q : questions) {
            if (answers.getOrDefault(q.getQuestionId(), -1) == q.getCorrectOption()) {
                score++;
            }
        }

        try {
            // Assuming user ID 1 for demo if session not fully passed, ideally pass User object
            // In real app, MainFrame should hold current User
            new ResultDAO().saveResult(1, quiz.getQuizId(), score); 
            JOptionPane.showMessageDialog(this, "Quiz Submitted! Score: " + score + "/" + questions.size());
            mainFrame.showStudentDashboard();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving result");
        }
    }
}
