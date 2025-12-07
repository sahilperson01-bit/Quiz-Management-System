package com.quiz.model;

public class Student extends User {
    public Student(int userId, String username, String password) {
        super(userId, username, password, "STUDENT");
    }

    @Override
    public void displayDashboard() {
        System.out.println("Displaying Student Dashboard for: " + username);
    }
}
