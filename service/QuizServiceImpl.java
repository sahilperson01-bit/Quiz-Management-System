package com.quiz.service;

import com.quiz.dao.QuizDAO;
import com.quiz.dao.ResultDAO;
import com.quiz.model.Quiz;
import com.quiz.model.User;
import java.util.List;

public class QuizServiceImpl implements QuizService {
    private QuizDAO quizDAO;
    private ResultDAO resultDAO;

    public QuizServiceImpl() {
        this.quizDAO = new QuizDAO();
        this.resultDAO = new ResultDAO();
    }

    @Override
    public void createQuiz(Quiz quiz) throws Exception {
        quizDAO.createQuiz(quiz);
    }

    @Override
    public List<Quiz> getAllQuizzes() throws Exception {
        return quizDAO.getAllQuizzes();
    }

    @Override
    public Quiz getQuiz(int quizId) throws Exception {
        return quizDAO.getQuizById(quizId);
    }

    @Override
    public void submitQuiz(User user, Quiz quiz, int score) throws Exception {
        resultDAO.saveResult(user.getUserId(), quiz.getQuizId(), score);
    }
}
