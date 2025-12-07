package com.quiz.ui;

import com.quiz.dao.QuizDAO;
import com.quiz.model.Question;
import com.quiz.model.Quiz;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;

public class AdminPanel extends JPanel {
    private MainFrame mainFrame;
    private QuizDAO quizDAO;

    private JList<String> quizList;
    private DefaultListModel<String> listModel;
    private JPanel editorPanel;
    private CardLayout editorLayout;
    
    // Editor Components
    private JTextField titleField;
    private JTextArea descArea;
    private JTextField qField, op1Field, op2Field, op3Field, op4Field;
    private JComboBox<String> correctOptionCombo;
    private Quiz currentQuiz;

    public AdminPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.quizDAO = new QuizDAO();
        setLayout(new BorderLayout());
        setBackground(StyleConstants.getSecondaryColor());

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(StyleConstants.getCardColor());
        headerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));
        
        JLabel titleLabel = StyleConstants.createHeaderLabel("Admin Dashboard");
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        JButton resultsButton = StyleConstants.createStyledButton("Student Results", StyleConstants.getAccentColor());
        resultsButton.addActionListener(e -> mainFrame.showAdminPerformancePanel());
        headerPanel.add(resultsButton, BorderLayout.CENTER);
        
        JButton logoutButton = StyleConstants.createStyledButton("Logout", Color.RED);
        logoutButton.addActionListener(e -> mainFrame.showLogin());
        headerPanel.add(logoutButton, BorderLayout.EAST);
        
        add(headerPanel, BorderLayout.NORTH);

        // Split Pane
        JSplitPane splitPane = new JSplitPane();
        splitPane.setDividerLocation(250);
        splitPane.setBorder(null);

        // Left: Quiz List
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(StyleConstants.getSecondaryColor());
        
        listModel = new DefaultListModel<>();
        quizList = new JList<>(listModel);
        quizList.setFont(StyleConstants.BODY_FONT);
        quizList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        quizList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) loadSelectedQuiz();
        });
        
        JScrollPane listScroll = new JScrollPane(quizList);
        listScroll.setBorder(BorderFactory.createEmptyBorder());
        leftPanel.add(listScroll, BorderLayout.CENTER);
        
        JButton createButton = StyleConstants.createStyledButton("+ New Quiz", StyleConstants.getPrimaryColor());
        createButton.addActionListener(e -> showCreateQuizForm());
        leftPanel.add(createButton, BorderLayout.SOUTH);
        
        splitPane.setLeftComponent(leftPanel);

        // Right: Editor Area
        editorLayout = new CardLayout();
        editorPanel = new JPanel(editorLayout);
        editorPanel.setBackground(StyleConstants.getCardColor());
        
        editorPanel.add(new JLabel("Select or Create a Quiz", SwingConstants.CENTER), "Empty");
        editorPanel.add(createQuizFormPanel(), "QuizForm");
        editorPanel.add(createQuestionFormPanel(), "QuestionForm");
        editorPanel.add(createBulkImportPanel(), "BulkImport");
        
        splitPane.setRightComponent(editorPanel);
        add(splitPane, BorderLayout.CENTER);
        
        refreshQuizList();
    }

    private void refreshQuizList() {
        listModel.clear();
        try {
            java.util.List<Quiz> quizzes = quizDAO.getAllQuizzes();
            for (Quiz q : quizzes) {
                listModel.addElement(q.getQuizId() + ": " + q.getTitle());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showCreateQuizForm() {
        titleField.setText("");
        descArea.setText("");
        currentQuiz = null;
        editorLayout.show(editorPanel, "QuizForm");
    }

    private void loadSelectedQuiz() {
        String selected = quizList.getSelectedValue();
        if (selected != null) {
            int quizId = Integer.parseInt(selected.split(":")[0]);
            try {
                currentQuiz = quizDAO.getQuizById(quizId);
                // For now, just show question form for the selected quiz
                // Ideally we show a details view first
                clearQuestionForm();
                editorLayout.show(editorPanel, "QuestionForm");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private JPanel createQuizFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(StyleConstants.getCardColor());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Quiz Title:"), gbc);
        gbc.gridx = 1;
        titleField = StyleConstants.createStyledTextField();
        panel.add(titleField, gbc);
        
        gbc.gridx = 0; gbc.gridy++;
        panel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1;
        descArea = new JTextArea(5, 20); // 5 rows, 20 cols
        descArea.setFont(StyleConstants.BODY_FONT);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        descArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY), 
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        JScrollPane scrollPane = new JScrollPane(descArea);
        panel.add(scrollPane, gbc);
        
        gbc.gridx = 1; gbc.gridy++;
        JButton saveButton = StyleConstants.createStyledButton("Save & Add Questions", StyleConstants.getPrimaryColor());
        saveButton.addActionListener(e -> saveQuiz());
        panel.add(saveButton, gbc);
        
        return panel;
    }

    private void saveQuiz() {
        String title = titleField.getText();
        String desc = descArea.getText();
        if (title.isEmpty()) return;
        
        currentQuiz = new Quiz(0, title, desc);
        // We don't save to DB yet, we wait for questions or save empty?
        // Let's save empty first to get ID
        try {
            quizDAO.createQuiz(currentQuiz); // This needs to return ID or update object
            // Re-fetch to get ID is safest if DAO doesn't update
            refreshQuizList();
            // Select the last one (hacky) or find by title
            // For now, just switch to question form assuming user wants to add questions
            editorLayout.show(editorPanel, "QuestionForm");
            JOptionPane.showMessageDialog(this, "Quiz Created! Now add questions.");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private JPanel createQuestionFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(StyleConstants.getCardColor());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(StyleConstants.createHeaderLabel("Add Question"), gbc);
        
        gbc.gridwidth = 1; gbc.gridy++;
        panel.add(new JLabel("Question Text:"), gbc);
        gbc.gridx = 1; qField = StyleConstants.createStyledTextField(); panel.add(qField, gbc);
        
        gbc.gridx = 0; gbc.gridy++; panel.add(new JLabel("Option 1:"), gbc);
        gbc.gridx = 1; op1Field = StyleConstants.createStyledTextField(); panel.add(op1Field, gbc);
        
        gbc.gridx = 0; gbc.gridy++; panel.add(new JLabel("Option 2:"), gbc);
        gbc.gridx = 1; op2Field = StyleConstants.createStyledTextField(); panel.add(op2Field, gbc);
        
        gbc.gridx = 0; gbc.gridy++; panel.add(new JLabel("Option 3:"), gbc);
        gbc.gridx = 1; op3Field = StyleConstants.createStyledTextField(); panel.add(op3Field, gbc);
        
        gbc.gridx = 0; gbc.gridy++; panel.add(new JLabel("Option 4:"), gbc);
        gbc.gridx = 1; op4Field = StyleConstants.createStyledTextField(); panel.add(op4Field, gbc);
        
        gbc.gridx = 0; gbc.gridy++; panel.add(new JLabel("Correct Option:"), gbc);
        gbc.gridx = 1; 
        String[] options = {"Option 1", "Option 2", "Option 3", "Option 4"};
        correctOptionCombo = new JComboBox<>(options);
        correctOptionCombo.setFont(StyleConstants.BODY_FONT);
        correctOptionCombo.setBackground(StyleConstants.getCardColor());
        panel.add(correctOptionCombo, gbc);
        
        gbc.gridx = 1; gbc.gridy++;
        JButton addButton = StyleConstants.createStyledButton("Add Question", StyleConstants.getAccentColor());
        addButton.addActionListener(e -> addQuestion());
        panel.add(addButton, gbc);
        
        gbc.gridx = 0; gbc.gridy++; gbc.gridwidth = 2; // New row
        JButton bulkButton = StyleConstants.createStyledButton("Bulk Import", StyleConstants.getPrimaryColor());
        bulkButton.addActionListener(e -> editorLayout.show(editorPanel, "BulkImport"));
        panel.add(bulkButton, gbc);
        
        return panel;
    }

    private void addQuestion() {
        if (currentQuiz == null) return;
        try {
            // DAO needs a method to add single question or we update the quiz object and re-save?
            // Existing DAO createQuiz saves everything. We need addQuestion method in DAO.
            // For now, let's just use the object and re-save? No, that duplicates.
            // We need to add a method to QuizDAO: addQuestionToQuiz(int quizId, Question q)
            // Or we just use SQL directly here for speed, but better to update DAO.
            // Let's assume we update DAO next.
            
            Question q = new Question(0, qField.getText(), op1Field.getText(), op2Field.getText(), 
                                    op3Field.getText(), op4Field.getText(), correctOptionCombo.getSelectedIndex() + 1);
            
            quizDAO.addQuestion(currentQuiz.getQuizId(), q);
            JOptionPane.showMessageDialog(this, "Question Added!");
            clearQuestionForm();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private JPanel createBulkImportPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(StyleConstants.getCardColor());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        panel.add(StyleConstants.createHeaderLabel("Bulk Import Questions"), BorderLayout.NORTH);

        JTextArea infoArea = new JTextArea("Paste questions in the following format (one per line):\n" +
                                         "Question Text | Option 1 | Option 2 | Option 3 | Option 4 | Correct Option Number (1-4)\n\n" +
                                         "Example:\n" +
                                         "What is Java? | A Car | A Language | A Food | A Planet | 2");
        infoArea.setEditable(false);
        infoArea.setBackground(StyleConstants.getCardColor());
        infoArea.setFont(StyleConstants.BODY_FONT);
        panel.add(infoArea, BorderLayout.CENTER);

        JTextArea inputArea = new JTextArea();
        inputArea.setFont(StyleConstants.BODY_FONT);
        JScrollPane scrollPane = new JScrollPane(inputArea);
        scrollPane.setPreferredSize(new Dimension(400, 300));
        panel.add(scrollPane, BorderLayout.WEST); // Or Center? Center overrides info

        // Re-layout slightly
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(infoArea, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        panel.add(centerPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);
        
        JButton backButton = StyleConstants.createStyledButton("Back", Color.GRAY);
        backButton.addActionListener(e -> editorLayout.show(editorPanel, "QuestionForm"));
        buttonPanel.add(backButton);
        
        JButton importButton = StyleConstants.createStyledButton("Import", StyleConstants.getPrimaryColor());
        importButton.addActionListener(e -> processBulkImport(inputArea.getText()));
        buttonPanel.add(importButton);
        
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void processBulkImport(String text) {
        if (currentQuiz == null) return;
        
        String[] lines = text.split("\\n");
        int successCount = 0;
        int failCount = 0;
        
        for (String line : lines) {
            if (line.trim().isEmpty()) continue;
            
            try {
                String[] parts = line.split("\\|");
                if (parts.length != 6) throw new Exception("Invalid format");
                
                String qText = parts[0].trim();
                String op1 = parts[1].trim();
                String op2 = parts[2].trim();
                String op3 = parts[3].trim();
                String op4 = parts[4].trim();
                int correct = Integer.parseInt(parts[5].trim());
                
                Question q = new Question(0, qText, op1, op2, op3, op4, correct);
                quizDAO.addQuestion(currentQuiz.getQuizId(), q);
                successCount++;
            } catch (Exception e) {
                failCount++;
                System.out.println("Failed to import line: " + line);
            }
        }
        
        JOptionPane.showMessageDialog(this, "Import Complete!\nSuccess: " + successCount + "\nFailed: " + failCount);
        if (successCount > 0) {
            // clear input?
            editorLayout.show(editorPanel, "QuestionForm");
        }
    }

    private void clearQuestionForm() {
        qField.setText(""); op1Field.setText(""); op2Field.setText(""); 
        op3Field.setText(""); op4Field.setText(""); correctOptionCombo.setSelectedIndex(0);
    }
}
