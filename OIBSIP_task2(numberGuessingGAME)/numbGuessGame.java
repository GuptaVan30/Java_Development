package numberGuessingGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;



public class numbGuessGame extends JFrame {
	
	private CardLayout cardLayout;
	private JPanel mainPanel;
	private JPanel mainmenuPanel;
	private JPanel gamePanel;
	private JPanel rulesPanel;
	
    private int numberToGuess;
    private int numberOfTries;
    private int maxAttempts = 10;
    private int points;
    private int totalScore;
    private int round;
    private int maxRounds = 3;

    private JTextField guessField;
    private JLabel messageLabel;
    private JLabel scoreLabel;

    public numbGuessGame() {
        // Initialize the frame
        setTitle("Guess the Number Game");
        setSize(450, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);  //This line will center the window on the screen.

        
       // Set up CardLayout
        cardLayout=new CardLayout();
        mainPanel=new JPanel(cardLayout);
        
        
        mainmenuPanel=createMainMenuPanel();
        gamePanel=createGamePanel();
        rulesPanel=createRulesPanel();
        
        mainPanel.add(mainmenuPanel,"Main Menu");
        mainPanel.add(gamePanel,"Game Panel");
        mainPanel.add(rulesPanel,"Rules Panel");
        
        add(mainPanel);
        
        cardLayout.show(mainPanel,"Main Menu");
        
    }   
    
    private JPanel createMainMenuPanel() {
    	
    	JPanel panel=new JPanel();
    	panel.setLayout(new BorderLayout());
    	
    	
   	    JLabel titleLabel = new JLabel("Number Guessing Game \n\n", SwingConstants.CENTER);
    	titleLabel.setFont(new Font("Arial", Font.BOLD, 34));
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.setBackground(Color.GRAY);
        
        JButton startButton=new JButton("Start Game");
        JButton rulesButton=new JButton("Rules");
        JButton exitButton=new JButton("Exit");
        
        startButton.addActionListener(e->cardLayout.show(mainPanel,"Game Panel"));
        rulesButton.addActionListener(e->cardLayout.show(mainPanel,"Rules Panel"));
        exitButton.addActionListener(e->System.exit(0));
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(startButton);
        buttonPanel.add(rulesButton);
        buttonPanel.add(exitButton);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(60, 0, 60, 0));
        panel.add(buttonPanel, BorderLayout.CENTER);

        return panel;
    
    }
    
    
    private JPanel createGamePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 1));

        // Initialize the game variables
        round = 1;
        totalScore = 0;
        startNewRound();

        // Create and add components
        messageLabel = new JLabel("Round " + round + ": Enter your guess (1-100):", SwingConstants.CENTER);
        panel.add(messageLabel);

        guessField = new JTextField();
        panel.add(guessField);

        JButton guessButton = new JButton("Guess");
        panel.add(guessButton);

        scoreLabel = new JLabel("Score: 0", SwingConstants.CENTER);
        panel.add(scoreLabel);

        
        JButton backButton = new JButton("Back to Main Menu");
        panel.add(backButton);

        // Add action listeners
        guessButton.addActionListener(new GuessButtonListener());
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "Main Menu"));
        
        
        return panel;
    }
    
    private JPanel createRulesPanel() {
    	JPanel panel=new JPanel();
    	panel.setLayout(new BorderLayout());
    	panel.setBackground(Color.LIGHT_GRAY);
    	
    	
    	
//    	rulesPanel = new JPanel();
//        rulesPanel.setBackground(Color.PINK);
   	
    	JLabel rulesLabel=new JLabel("Game Rules ",SwingConstants.CENTER);
    	rulesLabel.setFont(new Font("Serif", Font.BOLD, 24));
        panel.add(rulesLabel, BorderLayout.NORTH);
       
       
        
        JTextArea rulesText = new JTextArea(
        
                "Welcome to the Number Guessing Game!\n\n" +
                "1. The computer will randomly select a number between 1 and 100.\n" +
                "2. Your task is to guess the number.\n" +
                "3. Enter your guess and click 'Guess'.\n" +
                "4. The game will tell you if your guess is too high, too low, or correct.\n" +
                "5. You have a maximum of 10 attempts per round.\n" +
                "6. There are 3 rounds in total.\n" +
                "7. Your score will be calculated based on the number of attempts taken.\n" +
                "8. The game ends after 3 rounds or if you choose to exit.\n" +
                "9. For Each wrong guess,there is a deduction of -10 from total points of 100.\n\n"+ 
                "Good luck and have fun!"
            );
        
        rulesText.setEditable(false);
        rulesText.setMargin(new Insets(10, 10, 10, 10));
        panel.add(new JScrollPane(rulesText));
//        panel.add(rulesPanel, BorderLayout.CENTER); 
//       
        
        JButton backButton = new JButton("Back to Main Menu");
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "Main Menu"));
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(backButton);
        
        panel.add(bottomPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void startNewRound() {
        Random rand = new Random();
        numberToGuess = rand.nextInt(100) + 1;
        numberOfTries = 0;
        points = 100;
    }

    private void checkGuess(int guess) {
        numberOfTries++;
        if (guess < numberToGuess) {
            messageLabel.setText("The number you guessed is small. Try again.");
            points -= 10;
        } else if (guess > numberToGuess) {
            messageLabel.setText("The number you guessed is big. Try again.");
            points -= 10;
        } else {
            messageLabel.setText("Correct! You guessed the number in " + numberOfTries + " tries.");
            totalScore += points;
            round++;
            if (round <= maxRounds) {
                startNewRound();
                messageLabel.setText("Round " + round + ": Enter your guess (1-100):");
            } else {
                messageLabel.setText("In total of 3 rounds , Your total score is: " + totalScore);
                guessField.setEditable(false);
            }
        }

        if (numberOfTries == maxAttempts) {
            messageLabel.setText("No more attempts! The correct number was: " + numberToGuess);
            round++;
            if (round <= maxRounds) {
                startNewRound();
                messageLabel.setText("Round " + round + ": Enter your guess (1-100):");
            } else {
                messageLabel.setText("Game Over! Your total score is: " + totalScore);
                guessField.setEditable(false);
            }
        }

        // Update the score label
        scoreLabel.setText("Score: " + totalScore);
    }

    private class GuessButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                int guess = Integer.parseInt(guessField.getText());
                checkGuess(guess);
                guessField.setText("");
            } catch (NumberFormatException ex) {
                messageLabel.setText("Please enter a valid number.");
            }
        }
    }

    private class NewGameButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            round = 1;
            totalScore = 0;
            startNewRound();
            messageLabel.setText("Round " + round + ": Enter your guess (1-100):");
            guessField.setEditable(true);
            scoreLabel.setText("Score: " + totalScore);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new numbGuessGame().setVisible(true);
                
            }
        });
    }
}
