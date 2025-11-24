package db;

import model.MCQQuestion;
import model.Question;
import model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuizDAO {

    // Fetch all questions
    public List<Question> getAllQuestions() throws SQLException {
        List<Question> list = new ArrayList<>();
        String sql = "SELECT id, question, optionA, optionB, optionC, optionD, answer FROM questions";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                MCQQuestion q = new MCQQuestion(
                        rs.getInt("id"),
                        rs.getString("question"),
                        rs.getString("optionA"),
                        rs.getString("optionB"),
                        rs.getString("optionC"),
                        rs.getString("optionD"),
                        rs.getString("answer")
                );
                list.add(q);
            }
        }
        return list;
    }

    // Insert question (admin)
    public void insertQuestion(Question q) throws SQLException {
        String sql = "INSERT INTO questions (id, question, optionA, optionB, optionC, optionD, answer) VALUES (question_seq.NEXTVAL, ?,?,?,?,?,?,?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, q.getQuestionText());
            ps.setString(2, ((MCQQuestion) q).getA());
            ps.setString(3, ((MCQQuestion) q).getB());
            ps.setString(4, ((MCQQuestion) q).getC());
            ps.setString(5, ((MCQQuestion) q).getD());
            ps.setString(6, ((MCQQuestion) q).getAnswer());
            ps.executeUpdate();
        }
    }

    // Save score (synchronized to avoid race)
    public void saveScore(int userId, int score, int total) throws SQLException {
        String sql = "INSERT INTO scores (id, user_id, score, total, attempt_date) VALUES (score_seq.NEXTVAL, ?, ?, ?, SYSDATE)";
        synchronized (this) {
            try (Connection c = DBConnection.getConnection();
                 PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setInt(1, userId);
                ps.setInt(2, score);
                ps.setInt(3, total);
                ps.executeUpdate();
            }
        }
    }

    // Simple user auth (returns User if exists)
    public User authenticate(String username, String password) throws SQLException {
        String sql = "SELECT id, username, role FROM users WHERE username = ? AND password = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new User(rs.getInt("id"), rs.getString("username"), rs.getString("role"));
                }
            }
        }
        return null;
    }
}
