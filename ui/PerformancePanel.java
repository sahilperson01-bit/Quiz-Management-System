package com.quiz.ui;

import com.quiz.dao.ResultDAO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class PerformancePanel extends JPanel {
    private MainFrame mainFrame;
    private JTable resultTable;
    private DefaultTableModel tableModel;
    private List<Map<String, Object>> results;

    public PerformancePanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        setBackground(StyleConstants.getSecondaryColor());

        if (mainFrame.getCurrentUser() == null) {
            JOptionPane.showMessageDialog(this, "Session Error: No User Found", "Error", JOptionPane.ERROR_MESSAGE);
             // Should redirect to login, but constructor can't easily.
             // We'll handle null in loadData
        }

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(StyleConstants.getCardColor());
        headerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = StyleConstants.createHeaderLabel("My Performance");
        headerPanel.add(titleLabel, BorderLayout.WEST);

        JButton backButton = StyleConstants.createStyledButton("Back", StyleConstants.getAccentColor());
        backButton.addActionListener(e -> mainFrame.showStudentDashboard());
        headerPanel.add(backButton, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // Content Panel (Table + Graph)
        JPanel contentPanel = new JPanel(new GridLayout(2, 1, 20, 20));
        contentPanel.setBackground(StyleConstants.getSecondaryColor());
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Table
        String[] columnNames = {"Quiz Title", "Score", "Date"};
        tableModel = new DefaultTableModel(columnNames, 0);
        resultTable = new JTable(tableModel);
        resultTable.setFont(StyleConstants.BODY_FONT);
        resultTable.setRowHeight(25);
        resultTable.getTableHeader().setFont(StyleConstants.BUTTON_FONT);
        resultTable.getTableHeader().setBackground(StyleConstants.getPrimaryColor());
        resultTable.getTableHeader().setForeground(Color.WHITE);
        
        JScrollPane tableScrollPane = new JScrollPane(resultTable);
        tableScrollPane.setBorder(BorderFactory.createTitledBorder("Recent Results"));
        tableScrollPane.getViewport().setBackground(StyleConstants.getCardColor());
        contentPanel.add(tableScrollPane);

        // Graph Panel
        JPanel graphPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawGraph(g);
            }
        };
        graphPanel.setBackground(StyleConstants.getCardColor());
        graphPanel.setBorder(BorderFactory.createTitledBorder("Performance Graph"));
        contentPanel.add(graphPanel);

        add(contentPanel, BorderLayout.CENTER);

        loadData();
    }

    private void loadData() {
        try {
            if (mainFrame.getCurrentUser() != null) {
                results = new ResultDAO().getResultsByUserId(mainFrame.getCurrentUser().getUserId());
            } else {
                results = new java.util.ArrayList<>();
            }
            tableModel.setRowCount(0);
            for (Map<String, Object> row : results) {
                tableModel.addRow(new Object[]{
                    row.get("quizTitle"),
                    row.get("score"),
                    row.get("date")
                });
            }
            repaint(); // Redraw graph
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading results");
        }
    }

    private void drawGraph(Graphics g) {
        if (results == null || results.isEmpty()) return;

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();
        int padding = 40;
        int barWidth = 40;
        int maxScore = 10; // Assuming max score is 10 for scaling, ideally dynamic

        // Draw axes
        g2d.setColor(StyleConstants.getTextColor());
        g2d.drawLine(padding, height - padding, width - padding, height - padding); // X-axis
        g2d.drawLine(padding, height - padding, padding, padding); // Y-axis

        int x = padding + 20;
        for (Map<String, Object> row : results) {
            int score = (int) row.get("score");
            int barHeight = (int) ((double) score / maxScore * (height - 2 * padding));
            
            g2d.setColor(StyleConstants.getPrimaryColor());
            g2d.fillRect(x, height - padding - barHeight, barWidth, barHeight);
            
            g2d.setColor(StyleConstants.getTextColor());
            g2d.drawString(String.valueOf(score), x + 10, height - padding - barHeight - 5);
            
            String title = (String) row.get("quizTitle");
            if (title.length() > 5) title = title.substring(0, 5) + "..";
            g2d.drawString(title, x, height - padding + 15);

            x += barWidth + 20;
        }
    }
}
