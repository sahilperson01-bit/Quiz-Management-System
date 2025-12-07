package com.quiz.dao;

import com.quiz.model.Admin;
import com.quiz.model.Student;
import com.quiz.model.User;
import com.quiz.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    public User login(String username, String password) throws SQLException {
        String query = "SELECT * FROM Users WHERE username = ? AND password = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, username);
            stmt.setString(2, password);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("user_id");
                    String role = rs.getString("role");
                    
                    if ("ADMIN".equalsIgnoreCase(role)) {
                        return new Admin(id, username, password);
                    } else {
                        return new Student(id, username, password);
                    }
                }
            }
        }
        return null;
    }

    public boolean registerStudent(String username, String password) throws SQLException {
        String query = "INSERT INTO Users (username, password, role) VALUES (?, ?, 'STUDENT')";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, username);
            stmt.setString(2, password);
            
            int rows = stmt.executeUpdate();
            return rows > 0;
        }
    }
}
