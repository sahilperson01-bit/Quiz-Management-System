package gui;

import db.QuizDAO;
import model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class LoginWindow extends JFrame {
    private JTextField userField;
    private JPasswordField passField;
    private QuizDAO dao = new QuizDAO();

    public LoginWindow() {
        setTitle("Quiz Login");
        setSize(360, 220);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        JLabel title = new JLabel("Java Quiz Manager", SwingConstants.CENTER);
        add(title, BorderLayout.NORTH);

        JPanel p = new JPanel(new GridLayout(3,2,6,6));
        p.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        p.add(new JLabel("Username:"));
        userField = new JTextField();
        p.add(userField);
        p.add(new JLabel("Password:"));
        passField = new JPasswordField();
        p.add(passField);

        JButton loginBtn = new JButton("Login");
        p.add(loginBtn);

        add(p, BorderLayout.CENTER);

        loginBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String u = userField.getText().trim();
                String p = new String(passField.getPassword()).trim();
                try {
                    User user = dao.authenticate(u, p);
                    if (user != null) {
                        JOptionPane.showMessageDialog(LoginWindow.this, "Welcome " + user.getUsername());
                        dispose();
                        new QuizWindow(user).setVisible(true);
                    } else {
                        JOptionPane.showMessageDialog(LoginWindow.this, "Invalid credentials");
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(LoginWindow.this, "DB error: " + ex.getMessage());
                }
            }
        });
    }
}
