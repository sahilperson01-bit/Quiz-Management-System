package service;

import db.QuizDAO;
import model.Question;
import model.User;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class QuizManager {
    private QuizDAO dao;
    private List<Question> questionList;
    private Map<Integer, Integer> userScores; // userId -> latest score (thread-safe)

    public QuizManager() {
        this.dao = new QuizDAO();
        this.userScores = new ConcurrentHashMap<>();
    }

    // Load questions from DB (uses generics List<Question>)
    public void loadQuestions() throws SQLException {
        this.questionList = dao.getAllQuestions();
    }

    public List<Question> getShuffledQuestions() {
        Collections.shuffle(questionList);
        return questionList;
    }

    // store score using DAO (synchronized at DAO layer)
    public void saveUserScore(User user, int score, int total) throws SQLException {
        dao.saveScore(user.getId(), score, total);
        userScores.put(user.getId(), score);
    }

    public int getLastScore(int userId) {
        return userScores.getOrDefault(userId, 0);
    }
}
