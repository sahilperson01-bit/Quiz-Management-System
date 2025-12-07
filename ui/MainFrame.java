package com.quiz.ui;

import javax.swing.*;
import java.awt.*;
import com.quiz.model.User;

public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;

    private User currentUser;
    private String currentCard = "Login";

    public MainFrame() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // If Nimbus is not available, fall back to default
        }

        setTitle("Online Quiz Management System");
        setSize(1024, 768);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBackground(StyleConstants.getSecondaryColor());

        // Add Panels
        mainPanel.add(new LoginPanel(this), "Login");
        
        add(mainPanel);
        showLogin();
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void showLogin() {
        currentCard = "Login";
        currentUser = null; // Logout
        mainPanel.add(new LoginPanel(this), "Login"); // Re-add to clear fields
        cardLayout.show(mainPanel, "Login");
    }

    public void showAdminDashboard() {
        currentCard = "Admin";
        mainPanel.add(new AdminPanel(this), "Admin");
        cardLayout.show(mainPanel, "Admin");
    }

    public void showStudentDashboard() {
        currentCard = "Student";
        mainPanel.add(new StudentPanel(this), "Student");
        cardLayout.show(mainPanel, "Student");
    }

    public void showQuizPanel(int quizId) {
        currentCard = "Quiz";
        // Convert to String for card name if we want to be unique, but "Quiz" is fine as we replace it
        mainPanel.add(new QuizPanel(this, quizId), "Quiz");
        cardLayout.show(mainPanel, "Quiz");
    }

    public void showPerformancePanel() {
        currentCard = "Performance";
        mainPanel.add(new PerformancePanel(this), "Performance");
        cardLayout.show(mainPanel, "Performance");
    }

    public void showAdminPerformancePanel() {
        currentCard = "AdminPerformance";
        mainPanel.add(new AdminPerformancePanel(this), "AdminPerformance");
        cardLayout.show(mainPanel, "AdminPerformance");
    }

    public void refreshUI() {
        SwingUtilities.updateComponentTreeUI(this);
        mainPanel.setBackground(StyleConstants.getSecondaryColor());
        
        // Reload current card to apply theme colors to custom painted components
        switch (currentCard) {
            case "Admin": showAdminDashboard(); break;
            case "AdminPerformance": showAdminPerformancePanel(); break;
            case "Student": showStudentDashboard(); break;
            case "Performance": showPerformancePanel(); break;
            case "Login": showLogin(); break;
            // For Quiz, we might lose progress if we reload. ideally we just repaint or warn user.
            // But user clicked the theme button, so they expect a refresh.
            // If they are inside a quiz, maybe we shouldn't allow theme toggle or just repaint.
            // QuizPanel doesn't have a theme toggle in it (usually).
            // If it does, we need to pass quizId back.
        }
        this.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });
    }
}
