package javafxCalculator;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;



public class SciShuntingYard {

    private enum Operator
    {
        ADD(1), SUBTRACT(2), MULTIPLY(3), DIVIDE(4),POWER(5),FUNCTION(6);
        final int precedence;
        Operator(int p) { precedence = p; }
    }

    private static final Map<String, Operator> ops = new HashMap<String, Operator>() {{
        put("+", Operator.ADD);
        put("-", Operator.SUBTRACT);
        put("*", Operator.MULTIPLY);
        put("/", Operator.DIVIDE);
        put("%", Operator.DIVIDE);
        put("^", Operator.POWER);
        put("!", Operator.FUNCTION);
        put("√", Operator.FUNCTION);put("-√", Operator.FUNCTION);
        put("³√", Operator.FUNCTION);put("-³√", Operator.FUNCTION);
        put("log", Operator.FUNCTION);put("-log", Operator.FUNCTION);
        put("ln", Operator.FUNCTION);put("-ln", Operator.FUNCTION);
        put("exp", Operator.FUNCTION);put("exp", Operator.FUNCTION);
        put("cos", Operator.FUNCTION);put("-cos", Operator.FUNCTION);
        put("sin", Operator.FUNCTION);put("-sin", Operator.FUNCTION);
        put("tan", Operator.FUNCTION);put("-tan", Operator.FUNCTION);
        put("sinh", Operator.FUNCTION);put("-sinh", Operator.FUNCTION);
        put("cosh", Operator.FUNCTION);put("-cosh", Operator.FUNCTION);
        put("tanh", Operator.FUNCTION);put("-tanh", Operator.FUNCTION);
        put("cos⁻¹", Operator.FUNCTION);put("-cos⁻¹", Operator.FUNCTION);
        put("sin⁻¹", Operator.FUNCTION);put("-sin⁻¹", Operator.FUNCTION);
        put("tan⁻¹", Operator.FUNCTION);put("-tan⁻¹", Operator.FUNCTION);
    }};

    private static boolean isHigerPrec(String op, String sub)
    {
        return (ops.containsKey(sub) && ops.get(sub).precedence > ops.get(op).precedence);
    }

    public static String postfix(String infix)
    {   
        StringBuilder output = new StringBuilder();
        Deque<String> stack  = new LinkedList<>();
        
        for (String token : infix.split("\\s+")) {
            System.out.println("token: "+token);
            // operator
            if (ops.containsKey(token)) {
                while ( ! stack.isEmpty() && isHigerPrec(token, stack.peek()))
                    output.append(stack.pop()).append(' ');
                stack.push(token);

            // left parenthesis
            } else if (token.equals("(")) { 
                stack.push(token);

            // right parenthesis
            } else if (token.equals(")")) {
                while ( ! stack.peek().equals("("))
                    output.append(stack.pop()).append(' ');
                stack.pop();

            // digit
            } else {
                output.append(token).append(' ');
            }
        }

        while ( ! stack.isEmpty())
            output.append(stack.pop()).append(' ');
        System.out.println("postfixExpression: "+output.toString());
        return output.toString();
    }

}