/*
 * File: WordleGWindow.java
 * ------------------------
 * This file implements the WordleGWindow class, which manages the
 * graphical display for the Wordle project.
 */

 

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JFrame;
import javax.swing.JComponent;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class implements graphics for the Wordle assignment.
 */

public class WordleGWindow {

/** The number of rows in the Wordle grid. */
    public static final int N_ROWS = 6;

/** The number of columns in the Wordle grid. */
    public static final int N_COLS = 5;

/** The color used for correct letters (a light green). */
    public static final Color CORRECT_COLOR = new Color(0x66BB66);

/** The color used for letters that are present (a brownish yellow). */
    public static final Color PRESENT_COLOR = new Color(0xCCBB66);

/** The color used for letters that are missing (a medium gray). */
    public static final Color MISSING_COLOR = new Color(0x999999);

/**
 * Creates a new WordleGWindow object and displays it on the screen.
 */

    public WordleGWindow() {
        JFrame frame = new JFrame("Wordle");
        frame.setBackground(Color.WHITE);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        canvas = new WordleCanvas();
        frame.add(canvas, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
    }

/**
 * Sets the label from the grid square at the specified row and column.
 *
 * @param row The zero-based row number
 * @param col The zero-based column number
 * @param letter The new contents of the square as a string
 */

    public void setSquareLetter(int row, int col, String letter) {
        canvas.setSquareLetter(row, col, letter);
    }

/**
 * Returns the label from the grid square at the specified row and column.
 *
 * @param row The zero-based row number
 * @param col The zero-based column number
 * @return The contents of the square as a string
 */

    public String getSquareLetter(int row, int col) {
        return canvas.getSquareLetter(row, col);
    }

/**
 * Sets the background color of the grid square at the specified row and
 * column.  Colors are represented as strings in the form "#rrggbb".
 *
 * @param row The zero-based row number
 * @param col The zero-based column number
 * @param color The new color of the grid square
 */

    public void setSquareColor(int row, int col, Color color) {
        canvas.setSquareColor(row, col, color);
    }

/**
 * Returns the background color for the grid square at the specified
 * row and column.
 *
 * @param row The zero-based row number
 * @param col The zero-based column number
 * @return The background color of the square
 */

    public Color getSquareColor(int row, int col) {
        return canvas.getSquareColor(row, col);
    }

/**
 * Sets the background color of the key with the specified label.
 * Colors are represented as strings in the form "#rrggbb".
 *
 * @param label The label on the key
 * @param color The new color of the grid square as a string
 */

    public void setKeyColor(String label, Color color) {
        canvas.setKeyColor(label, color);
    }

/**
 * Returns the background color for the key with the specified label.
 *
 * @param label The label on the key
 * @return The background color of the square
 */

    public Color getKeyColor(String label) {
        return canvas.getKeyColor(label);
    }

/**
 * Sets the current row number and clears all the squares on that row.
 *
 * @param row The new row number
 */

    public void setCurrentRow(int row) {
        canvas.setCurrentRow(row);
    }

/**
 * Gets the current row number.
 *
 * @return The row number
 */

    public int getCurrentRow() {
        return canvas.getCurrentRow();
    }

/**
 * Displays a message under the board grid.
 *
 * @param msg The text of the message
 */

    public void showMessage(String msg) {
        canvas.showMessage(msg);
    }

/**
 * Adds an event listener to the window, which in this application is
 * triggered by hitting the RETURN key or pressing the ENTER button.
 *
 * @param listener The WordleEventListener waiting for this event
 */

    public void addEnterListener(WordleEventListener listener) {
        canvas.addEnterListener(listener);
    }

/* Private instance variables */

    private WordleCanvas canvas;

}

class WordleCanvas extends JComponent implements KeyListener, MouseListener {

    public WordleCanvas() {
        setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
        initWordleGrid();
        initWordleKeys();
        message = "";
        row = 0;
        col = 0;
        listeners = new ArrayList<WordleEventListener>();
        setFocusable(true);
        requestFocusInWindow();
        addKeyListener(this);
        addMouseListener(this);
    }

/**
 * Sets the label from the grid square at the specified row and column.
 *
 * @param row The zero-based row number
 * @param col The zero-based column number
 * @param letter The new contents of the square as a string
 */

    public void setSquareLetter(int row, int col, String letter) {
        grid[row][col].setLetter(letter);
        repaint();
    }

/**
 * Returns the label from the grid square at the specified row and column.
 *
 * @param row The zero-based row number
 * @param col The zero-based column number
 * @return The contents of the square as a string
 */

    public String getSquareLetter(int row, int col) {
        return grid[row][col].getLetter();
    }

/**
 * Sets the background color of the grid square at the specified row and
 * column.  Colors are represented as strings in the form "#rrggbb".
 *
 * @param row The zero-based row number
 * @param col The zero-based column number
 * @param color The new color of the grid square
 */

    public void setSquareColor(int row, int col, Color color) {
        grid[row][col].setColor(color);
        repaint();
    }

/**
 * Returns the background color for the grid square at the specified
 * row and column.
 *
 * @param row The zero-based row number
 * @param col The zero-based column number
 * @return The background color of the square
 */

    public Color getSquareColor(int row, int col) {
        return grid[row][col].getColor();
    }

/**
 * Sets the background color of the key with the specified label.
 * Colors are represented as strings in the form "#rrggbb".
 *
 * @param label The label on the key
 * @param color The new color of the grid square
 */

    public void setKeyColor(String label, Color color) {
        keys.get(label).setColor(color);
        repaint();
    }

/**
 * Returns the background color for the key with the specified label.
 *
 * @param label The label on the key
 * @return The background color of the square
 */

    public Color getKeyColor(String label) {
        return keys.get(label).getColor();
    }

/**
 * Sets the current row number and clears all the squares on that row.
 *
 * @param row The new row number
 */

    public void setCurrentRow(int row) {
        this.row = row;
        this.col = 0;
        for (int col = 0; col < N_COLS; col++) {
            setSquareLetter(row, col, "");
            setSquareColor(row, col, UNKNOWN_COLOR);
        }
        repaint();
    }

/**
 * Gets the current row number.
 *
 * param row The row number
 */

    public int getCurrentRow() {
        return row;
    }

/**
 * Displays a message under the board grid.
 *
 * @param msg The text of the message
 */

    public void showMessage(String msg) {
        message = msg;
        repaint();
    }

/**
 * Adds an event listener to the window, which is triggered by hitting
 * the RETURN key or pressing the ENTER button.
 *
 * @param listener The WordleEnterListener waiting for this event.
 */

    public void addEnterListener(WordleEventListener listener) {
        listeners.add(listener);
    }

/**
 * Repaints the Wordle canvas.  This method is called automatically when
 * the window contents are repainted.
 *
 * @param g The Graphics object on which the painting occurs
 */

    @Override
    public void paintComponent(Graphics g) {
        for (int row = 0; row < N_ROWS; row++) {
            for (int col = 0; col < N_COLS; col++) {
                grid[row][col].paint(g);
            }
        }
        for (String label : keys.keySet()) {
            keys.get(label).paint(g);
        }
        g.setColor(Color.BLACK);
        g.setFont(Font.decode(MESSAGE_FONT));
        FontMetrics fm = g.getFontMetrics();
        int tx = (CANVAS_WIDTH - fm.stringWidth(message)) / 2;
        g.drawString(message, tx, MESSAGE_Y);        
    }

/* KeyListener */

    @Override
    public void keyPressed(KeyEvent e) {
        /* Empty */
    }

    @Override
    public void keyReleased(KeyEvent e) {
        /* Empty */
    }

    @Override
    public void keyTyped(KeyEvent e) {
        keyAction(e.getKeyChar());
    }

/* MouseListener */

    @Override
    public void mouseClicked(MouseEvent e) {
        String key = findKey(e.getX(), e.getY());
        if (key != null) {
            char ch = key.charAt(0);
            if (key.equals("DELETE")) {
                ch = DELETE;
            } else if (key.equals("ENTER")) {
                ch = ENTER;
            }
            keyAction(ch);
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        /* Empty */
    }

    @Override
    public void mouseExited(MouseEvent e) {
        /* Empty */
    }

    @Override
    public void mousePressed(MouseEvent e) {
        /* Empty */
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        /* Empty */
    }

/* Private methods */

/*
 * Initializes the Wordle grid, which is a two-dimensional array of
 * WordleSquare objects.
 */

    private void initWordleGrid() {
        grid = new WordleSquare[N_ROWS][N_COLS];
        int x0 = (CANVAS_WIDTH - BOARD_WIDTH) / 2;
        for (int row = 0; row < N_ROWS; row++) {
            int y = TOP_MARGIN + row * SQUARE_DELTA;
            for (int col = 0; col < N_COLS; col++) {
                int x = x0 + col * SQUARE_DELTA;
                grid[row][col] = new WordleSquare(x, y);
            }
        }
    }

/*
 * Initializes the Wordle keys object, which is a map from key labels to
 * WordleKey objects.
 */

    private void initWordleKeys() {
        keys = new HashMap<String,WordleKey>();
        int nk = KEY_LABELS[0].length;
        int y0 = CANVAS_HEIGHT - BOTTOM_MARGIN - 3 * KEY_HEIGHT - 2 * KEY_YSEP;
        for (int row = 0; row < KEY_LABELS.length; row++) {
            int y = y0 + row * (KEY_HEIGHT + KEY_YSEP);
            int x = (CANVAS_WIDTH - nk * KEY_WIDTH - (nk - 1) * KEY_XSEP) / 2;
            if (row == 1) {
                x += (KEY_WIDTH + KEY_XSEP) / 2;
            }
            for (int col = 0; col < KEY_LABELS[row].length; col++) {
                String label = KEY_LABELS[row][col];
                int w = KEY_WIDTH;
                if (label.length() > 1) {
                    w += (KEY_WIDTH + KEY_XSEP) / 2;
                }
                WordleKey key = new WordleKey(x, y, w, label);
                keys.put(label, key);
                x += w + KEY_XSEP;
            }
        }
    }

/*
 * Implements the action taken by typing a key, either using the keyboard
 * or by clicking on the displayed keys.
 */

    private void keyAction(char letter) {
        letter = Character.toUpperCase(letter);
        if (letter == DELETE) {
            showMessage("");
            if (row < N_ROWS && col > 0) {
                col--;
                grid[row][col].setLetter(" ");
                repaint();
            }
        } else if (letter == ENTER) {
            showMessage("");
            for (WordleEventListener listener : listeners) {
                String s = "";
                for (int col = 0; col < N_COLS; col++) {
                    s += grid[this.row][col].getLetter();
                }
                listener.eventAction(s);
            }
        } else if (Character.isLetter(letter)) {
            showMessage("");
            if (row < N_ROWS && col < N_COLS) {
                grid[row][col].setLetter("" + letter);
                col++;
                repaint();
            }
        }
    }

/*
 * Returns the label from the WordleKey at (x, y), or null if none exists.
 */

    private String findKey(int x, int y) {
        for (String label : keys.keySet()) {
            if (keys.get(label).contains(x, y)) {
                return label;
            }
        }
        return null;
    }
        
/* Private classes */

    class WordleSquare {

        public WordleSquare(int x, int y) {
            this.x = x;
            this.y = y;
            this.letter = "";
            this.color = UNKNOWN_COLOR;
        }

        public void paint(Graphics g) {
            Color fg = Color.WHITE;
            if (color.equals(UNKNOWN_COLOR)) {
                fg = Color.BLACK;
            }
            g.setColor(color);
            g.fillRect(x, y, SQUARE_SIZE, SQUARE_SIZE);
            g.setColor(Color.BLACK);
            g.drawRect(x, y, SQUARE_SIZE, SQUARE_SIZE);
            g.setFont(Font.decode(SQUARE_FONT));
            FontMetrics fm = g.getFontMetrics();
            int tx = x + (SQUARE_SIZE - fm.stringWidth(letter)) / 2;
            int ty = y + SQUARE_SIZE / 2 + SQUARE_LABEL_DY;
            g.setColor(fg);
            g.drawString(letter, tx, ty);
        }

        public void setLetter(String letter) {
            this.letter = letter;
        }

        public String getLetter() {
            return letter;
        }

        public void setColor(Color color) {
            this.color = color;
        }

        public Color getColor() {
            return color;
        }
        
/* Private instance variables */

        private Color color;
        private String letter;
        private int x;
        private int y;

    };

    class WordleKey {

        public WordleKey(int x, int y, int width, String label) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.label = label;
            this.color = KEY_COLOR;
        }

        public void paint(Graphics g) {
            Color fg = Color.WHITE;
            int corner = 2 * KEY_CORNER;
            if (color.equals(KEY_COLOR)) {
                fg = Color.BLACK;
            }
            g.setColor(color);
            g.fillRoundRect(x, y, width, KEY_HEIGHT, corner, corner);
            String key = label;
            String font = KEY_FONT;
            if (key.equals("ENTER")) {
                font = ENTER_FONT;
            } else if (key == "DELETE") {
                key = "\u232B";
            }
            g.setFont(Font.decode(font));
            FontMetrics fm = g.getFontMetrics();
            int tx = x + (width - fm.stringWidth(key)) / 2;
            int ty = y + KEY_HEIGHT / 2 + KEY_LABEL_DY;
            g.setColor(fg);
            g.drawString(key, tx, ty);
        }

        public void setLetter(String label) {
            this.label = label;
        }

        public String getLetter() {
            return label;
        }

        public void setColor(Color color) {
            this.color = color;
        }

        public Color getColor() {
            return color;
        }

        public boolean contains(int x, int y) {
            return x > this.x && x < this.x + width &&
                   y > this.y && y < this.y + KEY_HEIGHT;
        }

/* Private instance variables */

        private Color color;
        private String label;
        private int width;
        private int x;
        private int y;

    }

/* Public constants */

    public static final int N_ROWS = WordleGWindow.N_ROWS;
    public static final int N_COLS = WordleGWindow.N_COLS;

    public static final Color CORRECT_COLOR = WordleGWindow.CORRECT_COLOR;
    public static final Color PRESENT_COLOR = WordleGWindow.PRESENT_COLOR;
    public static final Color MISSING_COLOR = WordleGWindow.MISSING_COLOR;
    public static final Color UNKNOWN_COLOR = Color.WHITE;
    public static final Color KEY_COLOR = new Color(0xDDDDDD);

    public static final int CANVAS_WIDTH = 500;
    public static final int CANVAS_HEIGHT = 700;

    public static final int SQUARE_SIZE = 60;
    public static final int SQUARE_SEP = 5;
    public static final int SQUARE_LABEL_DY = 18;
    public static final int TOP_MARGIN = 30;
    public static final int BOTTOM_MARGIN = 30;
    public static final int MESSAGE_SEP = 24;

    public static final int KEY_WIDTH = 40;
    public static final int KEY_HEIGHT = 60;
    public static final int KEY_CORNER = 9;
    public static final int KEY_LABEL_DY = 7;
    public static final int KEY_XSEP = 5;
    public static final int KEY_YSEP = 7;

    public static final int DELETE = (char) 8;
    public static final int ENTER = (char) 10;

    public static final String SQUARE_FONT = "Helvetica Neue-bold-44";
    public static final String MESSAGE_FONT = "Helvetica Neue-bold-20";
    public static final String KEY_FONT = "Helvetica Neue-18";
    public static final String ENTER_FONT = "Helvetica Neue-14";

    public static final String[][] KEY_LABELS = {
        { "Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P" },
        { "A", "S", "D", "F", "G", "H", "J", "K", "L" },
        { "ENTER", "Z", "X", "C", "V", "B", "N", "M", "DELETE" }
    };

/* Derived constants */

    public static final int SQUARE_DELTA = SQUARE_SIZE + SQUARE_SEP;
    public static final int BOARD_WIDTH =
        N_COLS * SQUARE_SIZE + (N_COLS - 1) * SQUARE_SEP;
    public static final int BOARD_HEIGHT =
        N_ROWS * SQUARE_SIZE + (N_ROWS - 1) * SQUARE_SEP;
    public static final int MESSAGE_X = CANVAS_WIDTH / 2;
    public static final int MESSAGE_Y =
        TOP_MARGIN + BOARD_HEIGHT + MESSAGE_SEP;

/* Private instance variables */

    private ArrayList<WordleEventListener> listeners;
    private HashMap<String,WordleKey> keys;
    private String message;
    private WordleSquare[][] grid;
    private int row;
    private int col;

}

