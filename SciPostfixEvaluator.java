package javafxCalculator;
import java.util.Stack;
/**
 * Class to evaluate infix and postfix expressions.
 * 
 * @author Paul E. Davis (feedback@willcode4beer.com)
 */
public class SciPostfixEvaluator {
   
        public static double evaluatePostfix(String postfixExpr, String angleUnit) {
            
                //char[] chars = postfixExpr.toCharArray();
                String[] items   = postfixExpr.split("\\s");
                Stack<Double> stack = new Stack<>();
                for (String item : items) {
                    
        try {            
            stack.push(Double.valueOf(item));
        } catch (NumberFormatException e) {
            double itemToPush=1;
            if (item.startsWith("-"))
                itemToPush= -1;
            
             if (item.contains("π"))
                 itemToPush= itemToPush * Math.PI;
                          
            else if (item.contains("e"))
                itemToPush= itemToPush * Math.E;    
                          
            else if (item.endsWith("!"))
                itemToPush= Utilities.factorial(Double.valueOf( item.substring(0, item.length()- 1 ) ));
              
            else if (item.endsWith("log")){
                Double value1 = stack.pop();
                itemToPush = itemToPush * Math.log10(value1);
               
            }
            else if (item.endsWith("³√")){
                Double value1 = stack.pop();
                itemToPush = itemToPush * Math.cbrt(value1);
               
            }
            else if (item.endsWith("√")){
                Double value1 = stack.pop();
                itemToPush = itemToPush * Math.sqrt(value1);
               
            }
            
            else if (item.equals("ln")){
                Double value1 = stack.pop();
                itemToPush = itemToPush * Math.log(value1);
            }
            else if (item.equals("exp")){
                Double value1 = stack.pop();
                itemToPush = itemToPush * Math.exp(value1);
            }
            else if (item.equals("cos")){
                Double value1 = stack.pop();
                if(angleUnit.equals("DEGREE"))
                    value1= Math.toRadians(value1);
                  
                itemToPush = itemToPush * Math.cos(value1);    
            }
            else if (item.equals("sin")){
                Double value1 = stack.pop();
                if(angleUnit.equals("DEGREE"))
                    value1= Math.toRadians(value1);
                itemToPush = itemToPush * Math.sin(value1);
            }
            else if (item.equals("tan")){
                Double value1 = stack.pop();
                if(angleUnit.equals("DEGREE"))
                    value1= Math.toRadians(value1);
                itemToPush = itemToPush * Math.tan(value1);
            }
            
            else if (item.equals("sin⁻¹")){
                Double value1 = stack.pop();
                if(angleUnit.equals("DEGREE"))
                    value1= Math.toRadians(value1);
                itemToPush = itemToPush * Math.asin(value1);
            }
            else if (item.equals("cos⁻¹")){
                Double value1 = stack.pop();
                if(angleUnit.equals("DEGREE"))
                    value1= Math.toRadians(value1);
                itemToPush = itemToPush * Math.acos(value1);
            }
            else if (item.equals("tan⁻¹")){
                Double value1 = stack.pop();
                if(angleUnit.equals("DEGREE"))
                    value1= Math.toRadians(value1);
                itemToPush = itemToPush * Math.atan(value1);
            }
            else if (item.equals("sinh")){
                Double value1 = stack.pop();
                if(angleUnit.equals("DEGREE"))
                    value1= Math.toRadians(value1);
                itemToPush = itemToPush * Math.sinh(value1);
            }
            else if (item.equals("tanh")){
                Double value1 = stack.pop();
                if(angleUnit.equals("DEGREE"))
                    value1= Math.toRadians(value1);
                itemToPush = itemToPush * Math.tanh(value1);
            }
            else if (item.equals("cosh")){
                Double value1 = stack.pop();
                if(angleUnit.equals("DEGREE"))
                    value1= Math.toRadians(value1);
                itemToPush = itemToPush * Math.cosh(value1);
                
            }
            else{
            //cas opérateurs + - * / ^ %          
            Double value1 = stack.pop();
            Double value2 = stack.pop();

            switch (item) {
                case "+":
                    itemToPush = value2 + value1;
                    
                    break;
                case "-":
                    itemToPush = value2 - value1;
                    
                    break;
                case "*":
                    itemToPush = value2 * value1;
                    
                    break;
                case "/":
                    itemToPush = value2 / value1;
                                                       
                    break;
                case "^":
                    itemToPush = Math.pow(value2, value1);
                                     
                    break;   
                case "%":
                    itemToPush = value2 % value1;
                                     
                    break;      
            }//end switch
            }//end else
            stack.push(itemToPush  );  
        }
    }

                if (stack.isEmpty()== false)
                    return stack.pop();
                else return 0;
        }
        //possible loss of precision if number contains 17 digits or more
                       
}