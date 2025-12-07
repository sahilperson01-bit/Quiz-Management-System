package com.quiz.service;

import com.quiz.model.Quiz;
import com.quiz.model.User;
import java.util.List;

public interface QuizService {
    void createQuiz(Quiz quiz) throws Exception;
    List<Quiz> getAllQuizzes() throws Exception;
    Quiz getQuiz(int quizId) throws Exception;
    void submitQuiz(User user, Quiz quiz, int score) throws Exception;
}
