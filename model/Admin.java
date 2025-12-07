package com.quiz.model;

public class Admin extends User {
    public Admin(int userId, String username, String password) {
        super(userId, username, password, "ADMIN");
    }

    @Override
    public void displayDashboard() {
        System.out.println("Displaying Admin Dashboard for: " + username);
    }
}
