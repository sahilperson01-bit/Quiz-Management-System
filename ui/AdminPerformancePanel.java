package com.quiz.ui;

import com.quiz.dao.ResultDAO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class AdminPerformancePanel extends JPanel {
    private MainFrame mainFrame;
    private JTable resultTable;
    private DefaultTableModel tableModel;

    public AdminPerformancePanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        setBackground(StyleConstants.getSecondaryColor());

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(StyleConstants.getCardColor());
        headerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = StyleConstants.createHeaderLabel("All Student Results");
        headerPanel.add(titleLabel, BorderLayout.WEST);

        JButton backButton = StyleConstants.createStyledButton("Back", StyleConstants.getAccentColor());
        backButton.addActionListener(e -> mainFrame.showAdminDashboard());
        headerPanel.add(backButton, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // Content
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(StyleConstants.getSecondaryColor());
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        String[] columnNames = {"Student", "Quiz Title", "Score", "Date"};
        tableModel = new DefaultTableModel(columnNames, 0);
        resultTable = new JTable(tableModel);
        resultTable.setFont(StyleConstants.BODY_FONT);
        resultTable.setRowHeight(25);
        resultTable.getTableHeader().setFont(StyleConstants.BUTTON_FONT);
        resultTable.getTableHeader().setBackground(StyleConstants.getPrimaryColor());
        resultTable.getTableHeader().setForeground(Color.WHITE);

        JScrollPane tableScrollPane = new JScrollPane(resultTable);
        contentPanel.add(tableScrollPane, BorderLayout.CENTER);

        add(contentPanel, BorderLayout.CENTER);

        loadData();
    }

    private void loadData() {
        try {
            List<Map<String, Object>> results = new ResultDAO().getAllResults();
            tableModel.setRowCount(0);
            for (Map<String, Object> row : results) {
                tableModel.addRow(new Object[]{
                    row.get("username"),
                    row.get("quizTitle"),
                    row.get("score"),
                    row.get("date")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading results");
        }
    }
}
