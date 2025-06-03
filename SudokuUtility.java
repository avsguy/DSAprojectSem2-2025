package sudokusolver;

import javax.swing.*;
import java.util.*;

public class SudokuUtility {
    
        public static String DIGITS = "123456789";
        public static String COLS = "123456789";
        public static String ROWS = "ABCDEFGHI";
        public static List<String> SQUARES = cross(ROWS, COLS);
        public static Map<String, String> solution = null;
        public static HashMap<String, JFrame> windows = new HashMap<>();
        
        public static void openWindow(String windowName, JFrame windowInstance) {
            JFrame existingWindow = windows.get(windowName);
            if (existingWindow == null || !existingWindow.isVisible()) {
                windows.put(windowName, windowInstance);
                windowInstance.setVisible(true);
            } else {
                existingWindow.toFront();
            }
        }

        public static List<String> cross(String A, String B) {
            List<String> result = new ArrayList<>();
            for (int i = 0; i < A.length(); i++) {
                for (int j = 0; j < B.length(); j++) {
                    result.add("" + A.charAt(i) + B.charAt(j));
                }
            }
            return result;
        }
        
        public static Map<String, String> solve(JTextField[][] cells) {
            StringBuilder gridBuilder = new StringBuilder();
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    String text = cells[i][j].getText().trim();
                    if (text.isEmpty()) {
                        gridBuilder.append("0");
                    } else {
                        if (text.matches("[1-9]")) {
                            gridBuilder.append(text);
                        } else {
                            JOptionPane.showMessageDialog(null, "Invalid input at cell (" + (i + 1)
                                    + ", " + (j + 1) + "). Please enter a digit between 1 and 9.");
                        }   
                    }
                }   
            }
            CSP puzzle = new CSP(gridBuilder.toString());
            Map<String, String> sol = SudokuSolver.backtrackingSearch(puzzle);
            if (sol == null) {
                JOptionPane.showMessageDialog(null, "No solution exists for this Sudoku!");
            }
            return sol;
        }
        
        public static Map<String, String> solve(JTextField text) {
            String grid = text.getText().trim();
            CSP puzzle = new CSP(grid);
            Map<String, String> sol = SudokuSolver.backtrackingSearch(puzzle);
            if (sol == null) {
                JOptionPane.showMessageDialog(null, "No solution exists for this Sudoku!");
            }
            return sol;
        }
        
        public static void update(Map<String, String> solution, JTextField[][] cells) {
            String rows = "ABCDEFGHI";
            String cols = "123456789";
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    String cellName = "" + rows.charAt(i) + cols.charAt(j);
                    cells[i][j].setText(solution.get(cellName));
                }
            }
        }
        
        public static void insertPuzzleString(String puzzleString, JTextField[][] cells) {
            if (puzzleString.length() != 81) {
                JOptionPane.showMessageDialog(null, "Puzzle string must be exactly 81 characters long");
                return;
            }
    
            String rows = "ABCDEFGHI";
            String cols = "123456789";
    
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    String cellName = "" + rows.charAt(i) + cols.charAt(j);
                    int index = rows.indexOf(cellName.charAt(0)) * 9 + cols.indexOf(cellName.charAt(1));
                    char c = puzzleString.charAt(index);
            
                     if (c == '0') {
                        cells[i][j].setText("");
                     } else if (c >= '1' && c <= '9') {
                        cells[i][j].setText(String.valueOf(c));
                    } else {
                        cells[i][j].setText("");
                        JOptionPane.showMessageDialog(null, "Invalid character at position " + index);
                    }
                }
            }
}
        
        private static boolean areRowsValid(String[][] board) {
            for (int i = 0; i < 9; i++) {
                Set<String> seen = new HashSet<>();
                for (int j = 0; j < 9; j++) {
                    String num = board[i][j];
                    if (!num.isEmpty()) {
                        if (seen.contains(num)) {
                            return false;
                        }
                        seen.add(num);
                    }
                }
            }
            return true;
        }
    
        private static boolean areColumnsValid(String[][] board) {
            for (int j = 0; j < 9; j++) {
                Set<String> seen = new HashSet<>();
                for (int i = 0; i < 9; i++) {
                    String num = board[i][j];
                    if (!num.isEmpty()) {
                        if (seen.contains(num)) {
                            return false;
                        }
                        seen.add(num);
                    }
                }
            }
            return true;
        }
    
        private static boolean areBoxesValid(String[][] board) {
            for (int boxRow = 0; boxRow < 3; boxRow++) {
                for (int boxCol = 0; boxCol < 3; boxCol++) {
                    Set<String> seen = new HashSet<>();
                    for (int i = boxRow * 3; i < boxRow * 3 + 3; i++) {
                        for (int j = boxCol * 3; j < boxCol * 3 + 3; j++) {
                            String num = board[i][j];
                            if (!num.isEmpty()) {
                                if (seen.contains(num)) {
                                    return false;
                                }
                                seen.add(num);
                            }
                        }
                    }
                }
            }
            return true;
        }
    
        public static void check(JTextField[][] cells) {
            String[][] board = new String[9][9];
 
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    String text = cells[i][j].getText().trim();
                    if (text.isEmpty()) {
                        board[i][j] = "";
                    } else if (text.matches("[1-9]")) {
                        board[i][j] = text;
                    } else {
                        JOptionPane.showMessageDialog(null, "Invalid input at cell (" + (i + 1)
                                + ", " + (j + 1) + "). Please enter a digit between 1 and 9.");
                        return;
                }
            }
        }
        
            if (!areRowsValid(board)) {
                JOptionPane.showMessageDialog(null, "There is a duplicate in one or more rows!");
                return;
            }
        
            if (!areColumnsValid(board)) {
                JOptionPane.showMessageDialog(null, "There is a duplicate in one or more columns!");
                return;
            }
        
            if (!areBoxesValid(board)) {
                JOptionPane.showMessageDialog(null, "There is a duplicate in one or more 3x3 squares!");
                return;
            }
        
            JOptionPane.showMessageDialog(null, "The board is valid so far.");
    }
    
        public static void clear(JTextField[][] cells) {
            for (int i = 0; i < cells.length; i++) {
                for (int j = 0; j < cells[i].length; j++) {
                    cells[i][j].setText("");
            }
        }
    }
}
