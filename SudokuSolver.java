
package sudokusolver;

import java.util.*;

public class SudokuSolver {

    public static Map<String, String> backtrackingSearch(CSP csp) {
        return recursiveBacktracking(new HashMap<>(), csp);
    }

    public static Map<String, String> recursiveBacktracking(Map<String, String> assignment, CSP csp) {
        if (isComplete(assignment)) {
            return assignment;
        }

        String var = selectUnassignedVariable(assignment, csp);
        String orderedValues = orderDomainValues(var, assignment, csp);
        
        for (int i = 0; i < orderedValues.length(); i++) {
            String value = String.valueOf(orderedValues.charAt(i));
            
            if (isConsistent(var, value, assignment, csp)) { 
                Map<String, String> originalDomain = new HashMap<>(csp.values);
                assignment.put(var, value);

                Map<String, String> inferences = new HashMap<>();
                Map<String, String> inferenceResult = inference(assignment, inferences, csp, var, value);
                
                if (inferenceResult != null) {
                    assignment.putAll(inferenceResult);
                    
                    Map<String, String> result = recursiveBacktracking(assignment, csp);
                    if (result != null) {
                        return result;
                    }
                    
                    for (String infVar : inferenceResult.keySet()) {
                        assignment.remove(infVar);
                    }
                }
                assignment.remove(var);
                csp.values.clear();
                csp.values.putAll(originalDomain);
            }
        }
        return null;
    }

    public static Map<String, String> inference(Map<String, String> assignment,
                                                  Map<String, String> inferences,
                                                  CSP csp, String var, String value) {
        for (String neighbor : csp.peers.get(var)) {
            if (!assignment.containsKey(neighbor) && csp.values.get(neighbor).contains(value)) {
                
                if (csp.values.get(neighbor).length() == 1) {
                    return null; 
                }
                
                String updated = csp.values.get(neighbor).replace(value, "");
                csp.values.put(neighbor, updated);
                
                if (updated.length() == 1) { 
                    inferences.put(neighbor, updated);
                    Map<String, String> result = inference(assignment, inferences, csp, neighbor, updated);
                    if (result == null) {
                        return null;
                    }
                }
            }
        }
        return inferences;
    }

    public static String orderDomainValues(String var, Map<String, String> assignment, CSP csp) {
        return csp.values.get(var);
    }

    public static String selectUnassignedVariable(Map<String, String> assignment, CSP csp) {
        String selected = null;
        int minSize = Integer.MAX_VALUE;
        for (String square : csp.values.keySet()) {
            if (!assignment.containsKey(square)) {
                int len = csp.values.get(square).length();
                if (len < minSize) {
                    minSize = len;
                    selected = square;
                }
            }
        }
        return selected;
    }

    public static boolean isComplete(Map<String, String> assignment) {
        return assignment.keySet().size() == CSP.SQUARES.size();
    }

    public static boolean isConsistent(String var, String value, Map<String, String> assignment, CSP csp) {
        for (String neighbor : csp.peers.get(var)) {
            if (assignment.containsKey(neighbor) && assignment.get(neighbor).equals(value)) {
                return false;
            }
        }
        return true;
    }

}

