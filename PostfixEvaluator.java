package javafxCalculator;
import java.util.Stack;
/**
 * Class to evaluate infix and postfix expressions.
 * 
 * @author Paul E. Davis (feedback@willcode4beer.com)
 */
public class PostfixEvaluator {

        public double evaluatePostfix(String postfixExpr) {
                //char[] chars = postfixExpr.toCharArray();
                String[] items   = postfixExpr.split("\\s");
                Stack<Double> stack = new Stack<>();
                for (String item : items) {
                    
        try {            
            stack.push(Double.valueOf(item));
        } catch (NumberFormatException e) {
            if( item.startsWith("√"))
                stack.push(Math.sqrt( Double.valueOf(item.substring( 1,item.length() ) )) ) ;
            else if (item.startsWith("π"))
                stack.push(Math.PI);
            else if (item.equals("-π"))
                stack.push(-Math.PI);
            else if (item.equals("e"))
                stack.push(Math.E);
            else if (item.equals("-e"))
                stack.push(-Math.E);
            else{
            //cas opérateurs + - * /           
            Double value1 = stack.pop();
            Double value2 = stack.pop();

            switch (item) {
                case "+":
                    stack.push(value2 + value1);
                    break;
                case "-":
                    stack.push(value2 - value1);
                    break;
                case "*":
                    stack.push(value2 * value1);
                    break;
                case "/":
                    
                    stack.push(value2 / value1);                                   
                    break;
            }//end switch
            }//end else
        }
    }

                if (stack.isEmpty()== false)
                    return stack.pop();
                else return 0;
        }
        //possible loss of precision if number contains 17 digits or more
                       
}