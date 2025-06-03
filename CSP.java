package sudokusolver;

import java.util.*;

public class CSP extends SudokuUtility {

    public List<String> variables;
    public Map<String, String> domain;
    public Map<String, String> values;
    public List<List<String>> unitList;
    public Map<String, List<List<String>>> units;
    public Map<String, Set<String>> peers;
    public Set<Pair<String, String>> constraints;
    
    public CSP(String grid) {
        this.variables = SQUARES;
        this.domain = getDict(grid);
        this.values = getDict(grid);
        
        unitList = new ArrayList<>();

        for (int i = 0; i < COLS.length(); i++) {
            String colChar = String.valueOf(COLS.charAt(i));
            unitList.add(cross(ROWS, colChar));
        }

        for (int i = 0; i < ROWS.length(); i++) {
            String rowChar = String.valueOf(ROWS.charAt(i));
            unitList.add(cross(rowChar, COLS));
        }

        String[] rowBlocks = {ROWS.substring(0, 3), ROWS.substring(3, 6), ROWS.substring(6, 9)};
        String[] colBlocks = {COLS.substring(0, 3), COLS.substring(3, 6), COLS.substring(6, 9)};
        for (String rb : rowBlocks) {
            for (String cb : colBlocks) {
                unitList.add(cross(rb, cb));
            }
        }

        units = new HashMap<>();
        for (String s : SQUARES) {
            List<List<String>> list = new ArrayList<>();
            for (List<String> unit : unitList) {
                if (unit.contains(s)) {
                    list.add(unit);
                }
            }
            units.put(s, list);
        }

        peers = new HashMap<>();
        for (String s : SQUARES) {
            Set<String> peerSet = new HashSet<>();
            for (List<String> unit : units.get(s)) {
                peerSet.addAll(unit);
            }
            peerSet.remove(s);
            peers.put(s, peerSet);
        }

        constraints = new HashSet<>();
        for (String s : SQUARES) {
            for (String p : peers.get(s)) {
                constraints.add(new Pair<>(s, p));
            }
        }
    }

    public static Map<String, String> getDict(String grid) {
        Map<String, String> result = new HashMap<>();
        
        for (int i = 0; i < SQUARES.size(); i++) {
            char ch = (grid.length() > i) ? grid.charAt(i) : '0';
            String square = SQUARES.get(i);
            if (ch != '0') {
                result.put(square, String.valueOf(ch));
            } else {
                result.put(square, DIGITS);
            }
        }
        return result;
        
    }

    public static class Pair<A, B> {
        public final A first;
        public final B second;

        public Pair(A first, B second) {
            this.first = first;
            this.second = second;
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Pair))
                return false;
            Pair<?, ?> p = (Pair<?, ?>) o;
            return Objects.equals(first, p.first) && Objects.equals(second, p.second);
        }

        @Override
        public int hashCode() {
            return Objects.hash(first, second);
        }

        @Override
        public String toString() {
            return "(" + first + ", " + second + ")";
        }
    }


}
