package com.quiz.dao;

import com.quiz.model.Question;
import com.quiz.model.Quiz;
import com.quiz.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuizDAO {

    public void createQuiz(Quiz quiz) throws SQLException {
        String quizQuery = "INSERT INTO Quizzes (title, description) VALUES (?, ?)";
        String questionQuery = "INSERT INTO Questions (quiz_id, question_text, option1, option2, option3, option4, correct_option) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        Connection conn = DBConnection.getInstance().getConnection();
        try {
            conn.setAutoCommit(false); // Transaction management
            
            // Insert Quiz
            int quizId = -1;
            try (PreparedStatement stmt = conn.prepareStatement(quizQuery, new String[]{"quiz_id"})) {
                stmt.setString(1, quiz.getTitle());
                stmt.setString(2, quiz.getDescription());
                stmt.executeUpdate();
                
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        quizId = rs.getInt(1);
                    }
                }
            }
            
            if (quizId != -1) {
                // Insert Questions
                try (PreparedStatement stmt = conn.prepareStatement(questionQuery)) {
                    for (Question q : quiz.getQuestions()) {
                        stmt.setInt(1, quizId);
                        stmt.setString(2, q.getQuestionText());
                        stmt.setString(3, q.getOption1());
                        stmt.setString(4, q.getOption2());
                        stmt.setString(5, q.getOption3());
                        stmt.setString(6, q.getOption4());
                        stmt.setInt(7, q.getCorrectOption());
                        stmt.addBatch();
                    }
                    stmt.executeBatch();
                }
            }
            
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }

    public List<Quiz> getAllQuizzes() throws SQLException {
        List<Quiz> quizzes = new ArrayList<>();
        String query = "SELECT * FROM Quizzes";
        
        try (Connection conn = DBConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                Quiz quiz = new Quiz(
                    rs.getInt("quiz_id"),
                    rs.getString("title"),
                    rs.getString("description")
                );
                quizzes.add(quiz);
            }
        }
        return quizzes;
    }

    public Quiz getQuizById(int quizId) throws SQLException {
        Quiz quiz = null;
        String quizQuery = "SELECT * FROM Quizzes WHERE quiz_id = ?";
        String questionQuery = "SELECT * FROM Questions WHERE quiz_id = ?";
        
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(quizQuery)) {
            
            stmt.setInt(1, quizId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    quiz = new Quiz(
                        rs.getInt("quiz_id"),
                        rs.getString("title"),
                        rs.getString("description")
                    );
                }
            }
        }
        
        if (quiz != null) {
            try (Connection conn = DBConnection.getInstance().getConnection();
                 PreparedStatement stmt = conn.prepareStatement(questionQuery)) {
                
                stmt.setInt(1, quizId);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        quiz.addQuestion(new Question(
                            rs.getInt("question_id"),
                            rs.getString("question_text"),
                            rs.getString("option1"),
                            rs.getString("option2"),
                            rs.getString("option3"),
                            rs.getString("option4"),
                            rs.getInt("correct_option")
                        ));
                    }
                }
            }
        }
        return quiz;
    }
    public void addQuestion(int quizId, Question q) throws SQLException {
        String query = "INSERT INTO Questions (quiz_id, question_text, option1, option2, option3, option4, correct_option) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, quizId);
            stmt.setString(2, q.getQuestionText());
            stmt.setString(3, q.getOption1());
            stmt.setString(4, q.getOption2());
            stmt.setString(5, q.getOption3());
            stmt.setString(6, q.getOption4());
            stmt.setInt(7, q.getCorrectOption());
            stmt.executeUpdate();
        }
    }
}
