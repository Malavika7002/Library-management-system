package com.library.student;

import com.library.student.ui.StudentDashboardFrame;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StudentDashboardFrame());
    }
}
