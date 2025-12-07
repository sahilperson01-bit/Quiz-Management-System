package com.quiz.ui;

import com.quiz.dao.UserDAO;
import com.quiz.model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;

public class LoginPanel extends JPanel {
    private MainFrame mainFrame;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private UserDAO userDAO;

    public LoginPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.userDAO = new UserDAO();
        setLayout(new GridBagLayout());
        setBackground(StyleConstants.getSecondaryColor());
        
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(StyleConstants.getCardColor());
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(30, 40, 30, 40)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = StyleConstants.createHeaderLabel("Quiz System Login");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        card.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;
        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(StyleConstants.BODY_FONT);
        card.add(userLabel, gbc);
        
        gbc.gridx = 1;
        usernameField = StyleConstants.createStyledTextField();
        card.add(usernameField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(StyleConstants.BODY_FONT);
        card.add(passLabel, gbc);
        
        gbc.gridx = 1;
        passwordField = new JPasswordField(20);
        passwordField.setFont(StyleConstants.BODY_FONT);
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY), 
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        card.add(passwordField, gbc);

        gbc.gridx = 0; gbc.gridy++; gbc.gridwidth = 2;
        JButton loginButton = StyleConstants.createStyledButton("Login", StyleConstants.getPrimaryColor());
        loginButton.addActionListener(this::handleLogin);
        card.add(loginButton, gbc);
        
        gbc.gridy++;
        JButton registerButton = StyleConstants.createStyledButton("Register (Student)", StyleConstants.getAccentColor());
        registerButton.addActionListener(this::handleRegister);
        card.add(registerButton, gbc);
        
        add(card);
    }

    private void handleLogin(ActionEvent e) {
        System.out.println("Login Button Clicked");
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        
        JButton btn = (JButton) e.getSource();
        btn.setEnabled(false); // Prevent double clicks
        btn.setText("Logging in...");

        SwingWorker<User, Void> worker = new SwingWorker<>() {
            @Override
            protected User doInBackground() throws Exception {
                System.out.println("Attempting login for: " + username);
                return userDAO.login(username, password);
            }

            @Override
            protected void done() {
                try {
                    User user = get();
                    System.out.println("Login result: " + user);
                    if (user != null) {
                        mainFrame.setCurrentUser(user);
                        JOptionPane.showMessageDialog(LoginPanel.this, "Login Successful!");
                        if ("ADMIN".equals(user.getRole())) {
                            mainFrame.showAdminDashboard();
                        } else {
                            mainFrame.showStudentDashboard();
                        }
                    } else {
                        JOptionPane.showMessageDialog(LoginPanel.this, "Invalid Credentials", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(LoginPanel.this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                } finally {
                    btn.setEnabled(true);
                    btn.setText("Login");
                }
            }
        };
        worker.execute();
    }

    private void handleRegister(ActionEvent e) {
        System.out.println("Register Button Clicked");
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        
        if (username.isEmpty() || password.isEmpty()) {
             JOptionPane.showMessageDialog(this, "Please enter username and password", "Error", JOptionPane.ERROR_MESSAGE);
             return;
        }

        JButton btn = (JButton) e.getSource();
        btn.setEnabled(false);
        btn.setText("Registering...");

        SwingWorker<Boolean, Void> worker = new SwingWorker<>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                System.out.println("Attempting registration for: " + username);
                return userDAO.registerStudent(username, password);
            }

            @Override
            protected void done() {
                try {
                    boolean success = get();
                    System.out.println("Registration success: " + success);
                    if (success) {
                        JOptionPane.showMessageDialog(LoginPanel.this, "Registration Successful! Please Login.");
                    } else {
                        JOptionPane.showMessageDialog(LoginPanel.this, "Registration Failed", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(LoginPanel.this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                } finally {
                    btn.setEnabled(true);
                    btn.setText("Register (Student)");
                }
            }
        };
        worker.execute();
    }
}
