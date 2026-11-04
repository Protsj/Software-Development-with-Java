package com.university;

public class Student {
    private double totalGrades = 0;
    private int count = 0;

    public void addGrade(double grade) {
        totalGrades += grade;
        count++;
    }

    public double calculateGPA() {
        return count == 0 ? 0 : totalGrades / count;
    }
}