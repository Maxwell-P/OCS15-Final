/*
 * File: Wordle.java
 * -----------------
 * This file implements the Wordle class, which manages the
 * logic for the Wordle project.
 */

import java.awt.*;
import java.util.*;
import java.util.List;

public class Wordle {

    /* Private instance variables */
    private WordleGWindow gw;
    private String word;
    private static final int WORD_LENGTH = 5;
    private Random rand = new Random();
    private boolean exist = true;
    private Set<String> dictionarySet;
    private Map<String, String> guessesToClues = new HashMap<>();

    public void victoryAnimation() {
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < WORD_LENGTH; col++) {
                gw.setSquareColor(row, col, WordleGWindow.CORRECT_COLOR);
            }
        }
    }
    // Other methods and variables

    /**
     * Generates a list of possible words from the dictionary that match the given guesses and clues.
     *
     * @param guessToClue a Map of guesses (so far) with their associated clues
     * @param dictionary  the dictionary used for this Wordle
     * @return a List of all the possible words (given the guesses and clues so far)
     */
    public static List<String> listOfPossibleWords(Map<String, String> guessToClue, String[] dictionary) {
        List<String> possibleWords = new ArrayList<>();

        for (String word : dictionary) {
            boolean matchesAll = true;
            for (String guess : guessToClue.keySet()) {
                String expectedClue = guessToClue.get(guess);
                String actualClue = getHint(guess, word);
                if (!actualClue.equals(expectedClue)) {
                    matchesAll = false;
                    break;
                }
            }
            if (matchesAll) {
                possibleWords.add(word);
            }
        }

        return possibleWords;
    }

    /**
     * Checks if a word matches all the clues from previous guesses.
     *
     * @param word        the word to check
     * @param guessToClue a map of previous guesses and their clues
     * @return true if the word matches all clues, false otherwise
     */
    private static boolean matchesAllClues(String word, Map<String, String> guessToClue) {
        for (Map.Entry<String, String> entry : guessToClue.entrySet()) {
            String guess = entry.getKey().toUpperCase();
            String clue = entry.getValue();

            if (!matchesClue(word.toUpperCase(), guess, clue)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if a word matches a specific clue from a guess.
     *
     * @param word  the word to check
     * @param guess the guess
     * @param clue  the clue associated with the guess
     * @return true if the word matches the clue, false otherwise
     */
    private static boolean matchesClue(String word, String guess, String clue) {
        char[] wordChars = word.toCharArray();
        boolean[] usedChars = new boolean[word.length()];

        for (int i = 0; i < clue.length(); i++) {
            if (Character.isUpperCase(clue.charAt(i))) {
                if (wordChars[i] != clue.charAt(i)) {
                    return false;
                }
                usedChars[i] = true;
            }
        }

        for (int i = 0; i < clue.length(); i++) {
            if (Character.isLowerCase(clue.charAt(i))) {
                boolean found = false;
                for (int j = 0; j < word.length(); j++) {
                    if (wordChars[j] == Character.toUpperCase(clue.charAt(i)) && !usedChars[j] && j != i) {
                        found = true;
                        usedChars[j] = true;
                        break;
                    }
                }
                if (!found) return false;
            }
        }

        for (int i = 0; i < clue.length(); i++) {
            if (clue.charAt(i) == '*') {
                for (int j = 0; j < word.length(); j++) {
                    if (wordChars[j] == guess.charAt(i) && !usedChars[j]) {
                        return false;
                    }
                }
            }
        }

        return true;
    }
    public void run() {
        dictionarySet = new HashSet<>(Arrays.asList(WordleDictionary.FIVE_LETTER_WORDS));
        word = chooseWord(); // choose random word
        gw = new WordleGWindow();
        gw.addEnterListener((s) -> enterAction(s));
    }

    /**
     * Returns a random word from WordleDictionary. Has a 1/3 chance to skip any word that ends with an "S".
     */
    public String chooseWord(){
        while (true) {
            int index = rand.nextInt(WordleDictionary.FIVE_LETTER_WORDS.length);
            String option = WordleDictionary.FIVE_LETTER_WORDS[index];
            if (option.endsWith("s")) {
                if (rand.nextDouble() > (double) 1/3) {
                    continue;
                }
            }
            return option;
        }
    }

/*
 * Called when the user hits the RETURN key or clicks the ENTER button,
 * passing in the string of characters on the current row.
 */

    public void enterAction(String s) {
        int currentRow = gw.getCurrentRow();
        if (s.length() != WORD_LENGTH) {
            if (s.isEmpty()) {
                List<String> possibleWords = listOfPossibleWords(guessesToClues, WordleDictionary.FIVE_LETTER_WORDS);
                System.out.println("Possible words: " + possibleWords);
                gw.showMessage("Listing possible words...");
            } else {
                gw.showMessage("Enter a " + WORD_LENGTH + "-letter word.");
            }
        }
        else if (!dictionarySet.contains(s.toLowerCase())) {
            gw.showMessage("Word doesn't exist.");
            gw.setCurrentRow(currentRow);
        }
        else {
            String hint = getHint(s, word);
            guessesToClues.put(s.toUpperCase(), hint);
            for (int i = 0; i < s.length(); i++) {
                char guessChar = s.charAt(i);
                Color color;
                if (hint.charAt(i) == guessChar) {
                    color = WordleGWindow.CORRECT_COLOR;
                } else if (hint.charAt(i) == Character.toLowerCase(guessChar)) {
                    color = WordleGWindow.PRESENT_COLOR;
                } else {
                    color = WordleGWindow.MISSING_COLOR;
                }
                gw.setSquareLetter(currentRow, i, "" + guessChar);
                gw.setSquareColor(currentRow, i, color);
                Color keyColor = gw.getKeyColor("" + guessChar);
                if (keyColor != WordleGWindow.CORRECT_COLOR) {
                    gw.setKeyColor("" + guessChar, color);
                }
            }

            if (s.equalsIgnoreCase(word)) {
                victoryAnimation();
                gw.showMessage("Congratulations! You've guessed the word!");
            }
            else if (currentRow == 5){
                gw.showMessage("The word was: '" + word + "'! Better luck next time!");
            }
            else {
                gw.setCurrentRow(currentRow + 1);
            }
        }
    }


    /**
     * @param guess the user's guess
     * @param word the secret word to be guessed
     * @return a String version of the hint where a capital letter
     * represents a correct guess at the correct location, a lower
     * case letter represents a correct guess at the wrong location,
     * and a '*' represents an incorrect letter (neither in the
     * correct place nor a correct letter anywhere in the word)
     *
     * You will use this helper method when coloring the squares.
     * It's also the crucial method that is tested in codePost.
     *
     * Examples:
     * word        = "CLASS"
     * guess       = "SASSY"
     * returns:      "sa*S*"
     *
     * word        = "FLUFF"
     * guess       = "OFFER"
     * returns:      "*ff**"
     *
     * word        = "STACK"
     * guess       = "TASTE"
     * returns:      "tas**"
     *
     * word        = "MYTHS"
     * guess       = "HITCH"
     * returns:      "h*T**"
     *
     */
    public static String getHint(String guess, String word){
        StringBuilder hint = new StringBuilder("*****");
        char[] guessChars = guess.toUpperCase().toCharArray();
        char[] wordChars = word.toUpperCase().toCharArray();

        for (int i = 0; i < WORD_LENGTH; i++) {
            if (guessChars[i] == wordChars[i]) {
                hint.setCharAt(i, Character.toUpperCase(guessChars[i]));
                wordChars[i] = '*';
                guessChars[i] = '+';
            }
        }

        for (int i = 0; i < WORD_LENGTH; i++) {
            for (int j = 0; j < WORD_LENGTH; j++) {
                if (guessChars[i] == wordChars[j] && guessChars[i] != '+') {
                    hint.setCharAt(i, Character.toLowerCase(guessChars[i]));
                    wordChars[j] = '*';
                    break;
                }
            }
        }

        return hint.toString();
    }

/* Startup code */

    public static void main(String[] args) {
        new Wordle().run();
    }

}
