package com.quiz.dao;

import com.quiz.util.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ResultDAO {

    public void saveResult(int userId, int quizId, int score) throws SQLException {
        String query = "INSERT INTO Results (user_id, quiz_id, score) VALUES (?, ?, ?)";
        
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, userId);
            stmt.setInt(2, quizId);
            stmt.setInt(3, score);
            
            stmt.executeUpdate();
        }
    }

    public java.util.List<java.util.Map<String, Object>> getResultsByUserId(int userId) throws SQLException {
        java.util.List<java.util.Map<String, Object>> results = new java.util.ArrayList<>();
        String query = "SELECT r.score, r.quiz_date, q.title FROM Results r " +
                       "JOIN Quizzes q ON r.quiz_id = q.quiz_id " +
                       "WHERE r.user_id = ? ORDER BY r.quiz_date DESC";

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, userId);
            try (java.sql.ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    java.util.Map<String, Object> row = new java.util.HashMap<>();
                    row.put("score", rs.getInt("score"));
                    row.put("date", rs.getTimestamp("quiz_date"));
                    row.put("quizTitle", rs.getString("title"));
                    results.add(row);
                }
            }
        }
        return results;
    }
    public java.util.List<java.util.Map<String, Object>> getAllResults() throws SQLException {
        java.util.List<java.util.Map<String, Object>> results = new java.util.ArrayList<>();
        String query = "SELECT r.score, r.quiz_date, q.title, u.username FROM Results r " +
                       "JOIN Quizzes q ON r.quiz_id = q.quiz_id " +
                       "JOIN Users u ON r.user_id = u.user_id " +
                       "ORDER BY r.quiz_date DESC";

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             java.sql.ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                java.util.Map<String, Object> row = new java.util.HashMap<>();
                row.put("score", rs.getInt("score"));
                row.put("date", rs.getTimestamp("quiz_date"));
                row.put("quizTitle", rs.getString("title"));
                row.put("username", rs.getString("username"));
                results.add(row);
            }
        }
        return results;
    }
}
