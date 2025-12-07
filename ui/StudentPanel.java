package com.quiz.ui;

import com.quiz.dao.QuizDAO;
import com.quiz.model.Quiz;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class StudentPanel extends JPanel {
    private MainFrame mainFrame;
    private QuizDAO quizDAO;

    public StudentPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.quizDAO = new QuizDAO();
        setLayout(new BorderLayout());
        setBackground(StyleConstants.getSecondaryColor());

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(StyleConstants.getCardColor());
        headerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));
        
        JLabel titleLabel = StyleConstants.createHeaderLabel("Student Dashboard");
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);

        JButton themeButton = StyleConstants.createStyledButton("Toggle Theme", StyleConstants.getPrimaryColor());
        themeButton.addActionListener(e -> {
            StyleConstants.toggleTheme();
            mainFrame.refreshUI();
        });
        buttonPanel.add(themeButton);

        JButton perfButton = StyleConstants.createStyledButton("My Performance", StyleConstants.getAccentColor());
        perfButton.addActionListener(e -> mainFrame.showPerformancePanel());
        buttonPanel.add(perfButton);

        JButton logoutButton = StyleConstants.createStyledButton("Logout", Color.RED);
        logoutButton.addActionListener(e -> mainFrame.showLogin());
        buttonPanel.add(logoutButton);

        headerPanel.add(buttonPanel, BorderLayout.EAST);
        
        add(headerPanel, BorderLayout.NORTH);

        // Quiz Grid
        JPanel gridPanel = new JPanel(new GridLayout(0, 3, 20, 20)); // 3 columns
        gridPanel.setBackground(StyleConstants.getSecondaryColor());
        gridPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        try {
            List<Quiz> quizzes = quizDAO.getAllQuizzes();
            for (Quiz q : quizzes) {
                gridPanel.add(createQuizCard(q));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        add(new JScrollPane(gridPanel), BorderLayout.CENTER);
    }

    private JPanel createQuizCard(Quiz q) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(StyleConstants.getCardColor());
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel title = new JLabel(q.getTitle());
        title.setFont(StyleConstants.SUBHEADER_FONT);
        title.setForeground(StyleConstants.getPrimaryColor());
        card.add(title, BorderLayout.NORTH);
        
        JTextArea desc = new JTextArea(q.getDescription());
        desc.setWrapStyleWord(true);
        desc.setLineWrap(true);
        desc.setEditable(false);
        desc.setFont(StyleConstants.BODY_FONT);
        desc.setBackground(StyleConstants.getCardColor());
        desc.setForeground(StyleConstants.getTextColor());
        card.add(desc, BorderLayout.CENTER);
        
        JButton startButton = StyleConstants.createStyledButton("Start Quiz", StyleConstants.getAccentColor());
        startButton.addActionListener(e -> mainFrame.showQuizPanel(q.getQuizId()));
        card.add(startButton, BorderLayout.SOUTH);
        
        return card;
    }
}
