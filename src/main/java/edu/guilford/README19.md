# Project 19: Letter Matching Game

In this project, we will use what we've learned about files to upgrade a previous game. As you'll recall, in Project 7, we built a game that had the player guess letters in a word. It then revealed the letters they had guessed correctly. Unfortunately, we had to hard-code the list of words, which made it fairly limited.

In this assignment, you will make the game load a dictionary of words - first a short one of around 100 words, then a longer one with around 3000 words. This should be pretty much all the 5-letter words in the English language. The program picks one of these words at random as the chosen word. Additionally, we'll use an app window rather than the command line, which allows us to make a better display. We're limiting ourselves to 5-letter words to simplify some of the work.

## Sample Code

Last class, you completed a project that loaded a dictionary and randomly chose a word to print. I've attached a solution to that assignment here. Review how it works.

## Getting Started

Create a class. I've called mine LetterMatch.java - if you have a better name, use it! But you'll need to keep track of that. Add the usual package and extend JFrame.

## Add the Imports

These are the imports I used. You may discover some unused or need more if you embellish the original.
```
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
```

## Make the Constructor & main

Create the usual constructor for a frame. I set my size to be 400x200, but you may want yours a little bigger or smaller depending on your screen and fonts. Put your name and the project name in the title bar. It should exit when you close it. I would set the layout as a border layout - if you don't, I think it defaults to that, but it's better to be explicit. And, of course, set the visibility as the last line of the constructor.

The main method should just instantiate a new instance of your class. At this point, test it to make sure you didn't forget anything. It should just be a gray app window.

## Adding Global Fields

Add the following before your constructor.
```
    private final String[] words = {"apple", "grape"};

    private String chosenWord;
    private final JButton[] letterButtons;
    private final JTextField inputField;
    private final JLabel attemptsLabel;
    private int attempts;
    Random random = new Random();
```
Your private class for the listener will need access to all of these. Note we have a VERY short list of words to start with. We'll add to this once we have the app working.

## Adding Components

First, let's display a row of buttons, which we'll later use to display one letter at a time. Add this just before the visibility line.
```
letterButtons = new JButton[5];
// Center panel with buttons
JPanel centerPanel = new JPanel();
centerPanel.setLayout(new GridLayout(1, 5));
for (int i = 0; i < 5; i++) {
    letterButtons[i] = new JButton("");
    letterButtons[i].setFont(new Font("Arial", Font.BOLD, 90));
    centerPanel.add(letterButtons[i]);
}
add(centerPanel, BorderLayout.CENTER);
```
Test this to make sure it displays a row of five blank buttons.

Now let's add the input field, attempts label, and reset button. This is rather a lot, but put it directly before the visibility line.
```
inputField = new JTextField(20);
attemptsLabel = new JLabel("Attempts: 0");
attempts = 0;

chosenWord = words[random.nextInt(words.length())];

// South panel with text field, attempts label, and reset button
JPanel southPanel = new JPanel();
southPanel.setLayout(new BorderLayout());

// user hits enter inside textfield to activate
// inputField.addActionListener(new LetterListener());
southPanel.add(inputField, BorderLayout.CENTER);

// label & button are smaller, so pack them separately
JPanel infoPanel = new JPanel();
infoPanel.setLayout(new GridLayout(1, 2));
infoPanel.add(attemptsLabel);

// reset button erases old letters, chooses new word
JButton resetButton = new JButton("Reset");
// resetButton.addActionListener(new ButtonListener());
infoPanel.add(resetButton);

southPanel.add(infoPanel, BorderLayout.EAST);
add(southPanel, BorderLayout.SOUTH);
```
I've commented out the lines that add action listeners, since we haven't written them yet. But note how this works - we've added a text field to the center of southPanel and a panel with the label and button to the east of southPanel. It would also have made sense to use a 3x1 grid layout on southPanel, but it would have made the button look weirdly big.

Also note our routine: make the components that go into a panel, make the panel, set the layout, add the components. We can add the listeners at any point, but this routine is pretty regular.

Test your program. You should get the panel of 5 buttons, a text field, a label that lists the number of attempts, and a button that says reset.

## Adding Functionality

Now that you have an app window working, let's make things work. First, let's do the reset button. Add this class after the constructor but before the main method.
```
class ButtonListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        chosenWord = words[random.nextInt(words.length)];
        for (JButton button : letterButtons) {
            button.setText("");
        }
        attempts = 0;
        attemptsLabel.setText("Attempts: 0");
    }
}
```
Now find the commented line that adds a new ButtonListener and uncomment it. 

Test your code. It should not make a visible difference, but the button should work without causing errors. If you would like to see it better, you could add a line after the attempts label line with System.out.println(chosenWord) to see that it's actually picking a new word when you click the button. But remember to delete this when you're done testing!

Once the button works, we can add functionality to the text field by adding a listener to it, which will be activated when the user hits enter. Add this after the ButtonListener class but before the main method.
```
class LetterListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        String input = inputField.getText().toLowerCase();
        if (input.length() > 0) {
            char firstLetter = input.charAt(0);
            for (int i = 0; i < chosenWord.length(); i++) {
                if (chosenWord.charAt(i) == firstLetter) {
                    letterButtons[i].setText(String.valueOf(firstLetter).toUpperCase());
                }
            }
            attempts++;
            attemptsLabel.setText("Attempts: " + attempts);
        }
        inputField.setText("");
    }
}
```
If you look back to Project 7, this code should be very familiar. Naturally, it's a little more convoluted because of the buttons and labels, but it does the same job. It also keeps track of the number of attempts needed to get all the letters.

Find the commented line that adds the new LetterListener to inputField and uncomment it. Your app should be functional now.

## Expanding the App's Vocabulary

So far, we've just been working with a list of two words: "apple" and "grape". This is advantageous while you're developing the app - you don't want a bunch of different possibilities. But the player should get a larger selection. That's where dictionary.txt and wordlist.txt come in: we're just going to copy code from WordPicker.

Find the line that defines words and change it to this.
```
private List<String> words;
```
Now our variable will be a list rather than an array. We need to initialize it in the constructor. Add the following code immediately preceding the line that defines chosenWord.
```
// Initialize the words list
words = new ArrayList<>();

// Getting the file from the resources folder
InputStream inputStream = LetterMatch.class.getClassLoader().getResourceAsStream("dictionary.txt");

        // Check if the file exists
        if (inputStream == null) {
            JOptionPane.showMessageDialog(null, "alert", "File not found!", JOptionPane.ERROR_MESSAGE);
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
You'll get an error on the lines which set chosenWord now, because the methods to deal with list items are not the same as those to deal with arrays. To fix this, find each occurrence of 
```
chosenWord = words[random.nextInt(words.length)];
```
and replace it with 
```
int index = random.nextInt(words.size());
chosenWord = words.get(index);
```
You could dispense with index by putting the call to random inside the parentheses, but I felt like the line looked too long.

Since the constructor runs before other code, there should be no need to adjust words for the listeners, but the button listener will need its chosenWord adjusted. 

At this point, your app runs with a vocabulary of 100 words. Test this by playing and resetting a few times to see you're getting different words. You could print the word chosen to the command line so you don't have to guess - but this is just for testing purposes! Take that out of the final product.

Finally, switch from dictionary.txt to wordlist.txt to have a list of 3000+ words. Now it's a simple little word game, but realistic.

## Embellishments

Naturally, you'll want to make little stylistic changes. Put your name on the title bar or find a better name for the class. Change colors of the letter buttons with a line like letterButtons[i].setBackground(Color.PINK) in the for loop that instantiates them. You may want to adjust the size or face of the font for the letters. I also noticed that when words with more than one M or W came up, the buttons were too narrow - you could fix that by pushing the frame width up by 50 pixels or so.

A vastly larger change would be this: include a larger dictionary. You can find files like this online - Scrabble players frequently compile these. The problem with that is that it has a uniform probability of choosing each word. So "dog" and "antidisestablishmentarianism" will show up just as often. If you're worried about that, maybe use a line like 
```
if ((line.length() > 3) && (line.length() < 10)) { 
    words.add(line);}
```
Where I just chose 3 & 10 arbitrarily.

This would mean that you need to vary the number of buttons to display the word, though. Maybe consider making a list of buttons instead of an array so you can add and remove when the reset button is pressed. It also means you need to change the width of the frame (probably 80-90 * length), which may be harder to make work.

A final notion would be to use this idea to write a game very much like Wordle. You'd need 6 rows of 5 buttons, and when the user types a word, it should check to see if their word is in the list of 3000+ that's in use and refuse words that aren't in the list. For correct letters in the right position, color the button's background green and for correct letters in the wrong position, color it yellow.