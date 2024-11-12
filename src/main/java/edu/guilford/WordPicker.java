package edu.guilford;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WordPicker extends JFrame {
    private JTextArea textArea;
    private JButton pickWordButton;
    private List<String> words;

    public WordPicker() {
        // Set up the JFrame
        setTitle("Word Picker");
        setSize(200, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Initialize the words list
        words = new ArrayList<>();

        // Getting the file from the resources folder
        InputStream inputStream = DictionaryReader.class.getClassLoader().getResourceAsStream("dictionary.txt");

        // Check if the file exists
        if (inputStream == null) {
            System.out.println("File not found in the resources folder.");
            return;
        }

        // Read file content and add to words
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                words.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Set up the JTextArea
        textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);

        // Set up the JButton
        pickWordButton = new JButton("Pick a Random Word");
        pickWordButton.addActionListener(new ButtonListener());

        // Add components to the JFrame
        add(scrollPane, BorderLayout.CENTER);
        add(pickWordButton, BorderLayout.SOUTH);

        setVisible(true);
    }

    class ButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (words.isEmpty()) {
                textArea.append("No words available.\n");
                return;
            }
    
            Random random = new Random();
            int index = random.nextInt(words.size());
            String randomWord = words.get(index);
            textArea.append(randomWord + "\n");            
        }
    }

    public static void main(String[] args) {
        new WordPicker();
    }
}
