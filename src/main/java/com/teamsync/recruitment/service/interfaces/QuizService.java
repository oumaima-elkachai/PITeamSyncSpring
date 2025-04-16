package com.teamsync.recruitment.service.interfaces;

import com.teamsync.recruitment.entity.Quiz;
import java.util.List;

public interface QuizService {
    List<Quiz> getAllQuizzes();
    Quiz getQuizById(String id);
    Quiz createQuiz(Quiz quiz);
}