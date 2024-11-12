# Project 18: File Reader

For today's project, we introduce one of the most vital parts of computer programming: reading data from files. While our use will be relatively small, data scientists commonly read in files with gigabytes of collected data in order to do analyses on populations or to train large language models.

To accomplish this, we need to know about some new constructs.

- try / catch routines
- InputStream
- BufferedReader
- Lists
- text area
- scroll pane

## Sample Code

You'll find a program titled DictionaryReader.java. Open it and run it. Scroll through the output in the terminal window. Now go to src/main/resources and find dictionary.txt. Open it and you'll see that the program read this file and printed it. 

Recall that `DictionaryReader.class.getClassLoader()` will pull the path to the resource directory. Then `.getResourceAsStream("dictionary.txt")` will connect the InputStream to that file, if it exists. If it doesn't, you'll get `null` (a reserve word that stands for not having a file) as the InputStream. The if statement that follows will kick the program out to the command line if inputStream turns out to be null.

Next, we look at the line `try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) `. As we've seen before, the keyword try will skip the block that follows in favor of the catch block if an error occurs. If the line is successful, a BufferedReader named reader is created and connected to the inputStream. 

If you guessed what reader does, you'd probably guess right. It has several methods, but we'll be most interested in reader.read() and reader.readLine(). You would use reader.read() to read the next character in a file, and reader.readLine() to read the next full line. Each line is treated as ending in \n - which we've used before to start a new line in a string.

The working part of the program is this block.
```
String line;
while ((line = reader.readLine()) != null) {
    System.out.println(line);
}
```
Of course, line is being declared as a string. Then the part of the next line (line = reader.readLine()) will read a line of text and store it in line. But once it's assigned, the != null tests to see whether the reader got anything when it read the next line. If it didn't, then line will be null and line != null will be false, so the while loop stops. If, however, line is anything else, the block of the while loop runs, and it prints out line.

## Experiment

Look in the resources folder. You should see another text file. Try modifying this code so that it prints out the other file.

## Making a Word Picker

In an earlier assignment, we had a list of about 100 words that were five letters long. That's a pretty short list compared to how many five-letter words are out there - although I wouldn't want to type it by hand. It would be handy to have a list of all five-letter English words but I sure don't want to type that, so I did a quick Google search. It turned up a text file from a GitHub user called shmookey, which I dropped in here. He left it on a public repository, so I'm pretty sure he's OK with us using it.

We're going to write a program that reads in all these words, then randomly picks one and prints it. If you recall what we did for the previous word guessing game, you can see how this would improve it. Integrating these two ideas is going to be the next project after this one.

## Create a Class

Make a class called WordPicker.java. The usual guidelines apply. Add the package line, make it extend JFrame, and make a constructor and main method.

## Do the imports

These are the imports I used. Check back at the end to make sure there aren't any that are unused.
```
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
```

## Add the Global Variables

Add the following before the constructor. These are objects that need to be accessed in the private class that will appear later.
```
    private JTextArea textArea;
    private JButton pickWordButton;
    private List<String> words;
```
The text area is like a larger version of a text field. The real new structure is a List - a list is like an array, but it can have things added or removed from it. If you need a list of integers, you would declare it as List<int> variable. But here we need a list of strings that will hold the words we read in later.

## Set up the Frame

Insert the usual starters for the app constructor.
```
        // Set up the JFrame
        setTitle("Word Picker");
        setSize(200, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setVisible(true);
```
As usual, you'll be inserting a lot of code before the line that says setVisible(true) - which I call the visibility line.

## Set up main

Insert the following as your main method.
```
public static void main(String[] args) {
    new WordPicker();
}
```
At this point you should test your program. If you don't... well, good luck.

## Set up the Interface

Insert the following before the visibility line.
```
        // Set up the JTextArea
        textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);

        // Set up the JButton
        pickWordButton = new JButton("Pick a Random Word");
        // pickWordButton.addActionListener(new ButtonListener());

        // Add components to the JFrame
        add(scrollPane, BorderLayout.CENTER);
        add(pickWordButton, BorderLayout.SOUTH);
```
The text area & scroll pane just sets up a display for text that's bigger than a label or text field. The scroll pane just allows it to scroll when you have filled up a page.

We've commented out the line that adds the button listener because we haven't written it yet. But at this point, you should be able to test your code and get a look at the interface. If it doesn't work at this point DO NOT CONTINUE UNTIL FIXING IT.

## Initialize the List of Words

Add the following after the line with setLocationRelativeTo(null).
```
// Initialize the words list
words = new ArrayList<>();
```
This creates a blank list. I've seen this done differently with ArrayList<>(String[0]) to let the array list know what type the list will be, but this seems to work. After you've got everything else working, you might try this modification and see if it works as well.

## Read the File

Add the following after words has been initialized but before the text area is added.
```
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
```
This code is mostly duplicating the DictionaryReader code. The only difference is replacing the print line with words.add(line) - this will add line to words as it reads each line. After this, you'll have a list called words that contains 100 or so 5-letter words.

Test your code. It should still work unless there have been any typos. VS Code will complain because you haven't done anything with words, but that shouldn't stop it from executing.

## Make the Button Work

Add the following class after the end of the constructor but before the main method starts.
```
class ButtonListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        if (words.isEmpty()) {
            textArea.append("No words available.\n");
            return;
        }

        Random random = new Random();
        int index = random.nextInt(words.size());
        textArea.append(words.get(index); + "\n");            
    }
}
```
As we've seen in past assignments, "implements" is like "extends", but for listeners. We only override the actionPerformed method. Notice the data validation - to avoid errors that might crop up when we try to get info from an empty list, the if statement kicks us out of the method if words is an empty list.

Now uncomment the line that adds the action listener. Your program should now randomly choose words from the list and add them to the text area.

## Larger List

Change dictionary.txt for wordlist.txt. Notice that, since we used words.size() in our randomizer, the larger list doesn't require us to hardcode a size.

Test your code. Because wordlist has about 3000 entries, you'll probably go a long time between repeats.

## Embellishments

Adjust the size and colors of components. In particular, make your text area a different color from the panels. Put your name on the title bar.

You might try adding a line like words.remove(index). This will deplete the list of words, randomly transferring them to the text area. Notice that this should give those of you working on a blackjack project a neat idea of how to accomplish a shuffle.