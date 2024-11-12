package edu.guilford;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DictionaryReader {
    public static void main(String[] args) {
        // Getting the file from the resources folder
        InputStream inputStream = DictionaryReader.class.getClassLoader().getResourceAsStream("dictionary.txt");

        // Check if the file exists
        if (inputStream == null) {
            System.out.println("File not found in the resources folder.");
            return;
        }

        // Read and print the file content
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}