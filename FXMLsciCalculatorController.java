/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/*  This java file is not used the scientific calculator works with the standard controller!!!      */
package javafxCalculator;

//import java.math.BigDecimal;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;

import javafx.scene.control.Label;
import javafx.scene.control.ChoiceBox;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
//import java.lang.Object;

/**
 *
 * @author h.moumni
 */
public class FXMLsciCalculatorController implements Initializable {
    private boolean signClicked=false;
    private boolean operatorClicked = false;   
    private boolean functionClicked = false;
    private boolean decimalPointClicked=false;// permet d'eviter de saisir la virgule décimal plusieurs fois sur le meme nombre
    private boolean constantClicked = false;
    private boolean resultDisplayed  = false;
    String infixNotation;
    String memoryStore="0";
    short leftParentheses = 0, rightParentheses = 0;
    private static String ans = "0";
    //final Clipboard clipboard = Clipboard.getSystemClipboard();
    final ClipboardContent content = new ClipboardContent();
    
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private DialogPane dialogPane;
    @FXML
    private Label labelResult;    
    @FXML
    private Label labelEntry;
    @FXML
    private Label labelMemory;
    
    @FXML
    private   ChoiceBox<String> choicebox;
    
    ObservableList angle = FXCollections.observableArrayList();
    
    private void loadAngleUnits(){
        angle.removeAll(angle);
        String radian ="RADIAN";
        String degree = "DEGREE";
        angle.addAll(radian,degree);        
        choicebox.getItems().addAll(angle);
        choicebox.setValue(radian);        
    }
    
    @FXML
    private void handleCloseAboutButtonAction(ActionEvent event) {
        dialogPane.setVisible(false);
        dialogPane.setDisable(true);
        
    }
    

    @FXML
    private void aboutAction(ActionEvent event){   
        dialogPane.setVisible(true);
        dialogPane.setDisable(false);    
    }
                      
    //boutton Number
    @FXML
    private void handleNumberAction(ActionEvent event) {         
        Button btn=(Button) event.getSource(); 
        String number = btn.getText();
        printNumberOnLabel(number);
              
    }
      
    //boutton clear entry
    @FXML
    private void handleButtonCEAction(ActionEvent event) {
        
        labelEntry.setText("");
        operatorClicked=false;
        constantClicked=false;
        decimalPointClicked=false;       
        signClicked =false;
         
    }
     //boutton clear 
    @FXML
    private void handleButtonCAction(ActionEvent event) {        
        labelResult.setText("0");
        
    }
    
    //boutton delete
    @FXML
    private void handleButtonDelAction(ActionEvent event) {
      if(labelEntry.getText().endsWith(" ( ")){
          labelEntry.setText(labelEntry.getText().substring(0, labelEntry.getText().length()-3) );
          leftParentheses--;
      }
      else if(labelEntry.getText().endsWith(" ) ")){
            labelEntry.setText(labelEntry.getText().substring(0, labelEntry.getText().length()-3) );
            rightParentheses--;
      }
     
      else if(labelEntry.getText().endsWith(" ") || labelEntry.getText().endsWith("ANS")){
          labelEntry.setText(labelEntry.getText().substring(0, labelEntry.getText().length()-3) );          
      }        
      else if(!labelEntry.getText().isEmpty())
           labelEntry.setText(labelEntry.getText().substring(0, labelEntry.getText().length()-1) );  
      //après suppression 
      //if endsWith operand constant number minusSign
      if(labelEntry.getText().endsWith("-"))
          signClicked = true;
      if(labelEntry.getText().endsWith("ANS") || labelEntry.getText().endsWith("π")){
          operatorClicked=false;
          constantClicked= true;
      } 
      if(labelEntry.getText().endsWith(" * ") || labelEntry.getText().endsWith( " / ") || labelEntry.getText().endsWith(" + ") || labelEntry.getText().endsWith(" - ")){   
          operatorClicked = true;
          decimalPointClicked=false;
          signClicked =false;
      } else if(labelEntry.getText().endsWith(".")){
            operatorClicked=false;
            decimalPointClicked=true;
      }
          
    }
    //boutton decimal point
    @FXML
    private void handleButtonPointAction(ActionEvent event) {
        printPointOnLabel();                          
    }   
   //operator button
    @FXML
    private void handleOperatorAction(ActionEvent event) {
        if(operatorClicked==true || functionClicked==true ||  labelEntry.getText().endsWith("( ")) return;
        Button btn =(Button) event.getSource();      
        if(!labelEntry.getText().isEmpty()){         
          labelEntry.setText(labelEntry.getText() + " "+btn.getText()+" " );
          operatorClicked = true;
          decimalPointClicked=false;
          signClicked =false;
          constantClicked=false;
          functionClicked = false;
        } 
        else {labelEntry.setText(  ans+" "+btn.getText()+" " );
          operatorClicked = true;
          decimalPointClicked = false;
          signClicked =false;
          constantClicked=false;
          functionClicked = false;
        }  
    }
    //sign minus button
    @FXML
    private void handleMinusSignAction(ActionEvent event) {
          if(signClicked == false && operatorClicked==true || labelEntry.getText().isEmpty() || labelEntry.getText().endsWith("( ")){  
          labelEntry.setText(labelEntry.getText() + "-" );
          signClicked = true;
          }
    }
  
    //boutton =
    @FXML
    private void handleButtonEqualsAction(ActionEvent event) {        
        infixNotation = labelEntry.getText().trim();
        int diffParentheses = leftParentheses - rightParentheses;
        while(leftParentheses > rightParentheses){
            infixNotation = infixNotation+" ) ";           
            rightParentheses++;
        }
        ans = generateResult(infixNotation);
         labelResult.setText( labelEntry.getText()+" = " + ans ); 
         labelEntry.setText("");
         decimalPointClicked=false;
         operatorClicked= false;
         constantClicked=false;
         functionClicked = false;
    }  
     
              
    
    @FXML
    private void exitProgram(ActionEvent event) {
        System.exit(0);
       
    }
    //copy
    @FXML
    private void handleCopyAction(ActionEvent event){           
        content.putString(labelResult.getText());
	Clipboard.getSystemClipboard().setContent(content);      
    }
     //paste not used in simple calculator
    @FXML
    private void handlePasteAction(ActionEvent event){                 
        Pattern p = Pattern.compile("^((\\d+)[\\+\\-\\*\\./]?)*$"); //use numbers /*-+ only
        Matcher m = p.matcher( Clipboard.getSystemClipboard().getContent(DataFormat.PLAIN_TEXT).toString()  );
        if(m.matches())
            
            labelEntry.setText( Clipboard.getSystemClipboard().getContent(DataFormat.PLAIN_TEXT).toString()); 
        else System.out.println("not matching");
    }
           
     //key typed
    @FXML
    private void handleOnKeyTyped(KeyEvent event){                   
        
     //  if(Character.isDigit(event.getCharacter().charAt(0)))
     //       printNumberOnLabel(event.getCharacter());
     //  else if(event.getCharacter.equals(""))  [0-9]
     Pattern numberPattern = Pattern.compile("[\\d]");   
     Matcher matcherNumber = numberPattern.matcher(event.getCharacter());
     Pattern operatorPattern = Pattern.compile("[\\+\\-\\*/]");
     Matcher matcherOperator = operatorPattern.matcher(event.getCharacter());
     if(matcherNumber.matches())
        printNumberOnLabel(event.getCharacter());
     else if(matcherOperator.matches())
             printOperatorOnLabel(event.getCharacter());
     else if(event.getCharacter().equals("."))
            printPointOnLabel();
     else if(event.getCharacter().equals("\r") || event.getCharacter().equals("\n") || event.getCharacter().equals("=") ){           
           infixNotation = labelEntry.getText().trim();         
           labelResult.setText( generateResult(infixNotation) );                     
            }
     
    }
       
     //key released
    @FXML
    private void handleOnKeyReleased(KeyEvent event){
        
        if(event.getCode().toString().equals("C") && event.isControlDown()== true){
            content.putString(labelResult.getText());
            Clipboard.getSystemClipboard().setContent(content);            
        }
                  
          
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {       
        loadAngleUnits();             
    }

    
    public  String generateResult(String infixNot){
     try{
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.getDefault());
        otherSymbols.setNaN("NaN");
        otherSymbols.setInfinity("Infinity");
        otherSymbols.setDecimalSeparator('.');
        DecimalFormat df = new DecimalFormat("#.##############",otherSymbols);             
      SciPostfixEvaluator SciPostfixEvaluator = new SciPostfixEvaluator();
        //REPLACE ANS BY RESULT
        System.out.println("infoxNot: "+infixNot);        
        String infixNotANS = infixNot.replace("ANS", ans);
        System.out.println("infixNotANS: "+infixNotANS);
     String postfixNotation = SciShuntingYard.postfix(infixNotANS);
     double  resultDouble = SciPostfixEvaluator.evaluatePostfix( postfixNotation, choicebox.getValue() );
    // long  resultLong = SciPostfixEvaluator.evaluatePostfixLongInteger( postfixNotation );
        //si le résultat est un entier alors il faut l'afficher sans virgule        
            //df.setRoundingMode(RoundingMode.CEILING);           
            //String result = String.valueOf(resultDouble);            
         String   result = df.format(resultDouble);                 
         return(result);
     } catch(Exception e ) {
          e.printStackTrace();
         return "ERROR";}
      //return (String.valueOf(resultLong) );
    }
    public void printNumberOnLabel(String n){
        if(constantClicked==true)
            labelEntry.setText(labelEntry.getText()+ " * " );
        labelEntry.setText(labelEntry.getText()+ n ); 
        operatorClicked=false;
        constantClicked= false;
        functionClicked = false;
    }
    public void printOperatorOnLabel(String n){ 
            
          labelEntry.setText(labelEntry.getText()+ " "+n+" " );
          operatorClicked = true;
          decimalPointClicked = false;
          constantClicked=false;
          
    }
    public void printPointOnLabel(){
        if( operatorClicked==true || labelEntry.getText().isEmpty()){
            labelEntry.setText(labelEntry.getText()+"0." );
            operatorClicked=false;
            decimalPointClicked=true;
        }
                   
        else if (decimalPointClicked==false && constantClicked==false && !labelEntry.getText().endsWith(" ")) {
            labelEntry.setText(labelEntry.getText()+ "." );
            operatorClicked=false;
            decimalPointClicked=true;
        }
        
    }
        
     @FXML
    private void handleStdCalcAction(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("FXMLStandardCalculator.fxml"));         
        Scene standardCalculatorScene = new Scene(root);
        Stage stage2 = (Stage) anchorPane.getScene().getWindow();
        stage2.setScene(standardCalculatorScene);              
        stage2.show();
        stage2.setResizable(true);
    }
    //-------scientific buttons--------------------------------------------------------
    @FXML
     private void hCSTAction(ActionEvent event) {                 
        if(decimalPointClicked) return ;
         Button btn=(Button) event.getSource(); 
              
        if(labelEntry.getText().endsWith(") ") || !labelEntry.getText().endsWith(" ") && !labelEntry.getText().endsWith("-") && !labelEntry.getText().isEmpty())
            labelEntry.setText(labelEntry.getText()+" * ");
        labelEntry.setText(labelEntry.getText()+ btn.getText() );
        operatorClicked=false;
        constantClicked = true;
        functionClicked = false;
                     
    } 
    
    @FXML
     private void handleRandomAction(ActionEvent event) {   
        if(decimalPointClicked) return ; 
        double rnd= Math.random();
         String random = Double.toString(rnd);
        if(labelEntry.getText().endsWith(") ") || !labelEntry.getText().endsWith(" ") && !labelEntry.getText().endsWith("-") && !labelEntry.getText().isEmpty())
            labelEntry.setText(labelEntry.getText()+" * ");
        labelEntry.setText(labelEntry.getText()+ random );
        constantClicked = true;
        functionClicked = false;
                     
    } 
    @FXML
     private void hFactorialAction(ActionEvent event) {                 
       if( labelEntry.getText().endsWith(") ") || constantClicked==false && functionClicked==false && operatorClicked==false && decimalPointClicked==false && signClicked==false && !labelEntry.getText().endsWith(" ") && !labelEntry.getText().endsWith("-") && !labelEntry.getText().isEmpty()){
         labelEntry.setText(labelEntry.getText()+"!"); 
          constantClicked= true;
       }
    } 
    @FXML
     private void hFunctionAction(ActionEvent event) {
       if(labelEntry.getText().endsWith(".")) return; 
       Button btn =(Button) event.getSource();  
       if(   constantClicked==true  || labelEntry.getText().endsWith("!") || labelEntry.getText().endsWith(") ")  ) 
             labelEntry.setText(labelEntry.getText()+" * ");
        labelEntry.setText(labelEntry.getText()+btn.getText()+" "); 
        functionClicked = true;
        constantClicked=false;
        operatorClicked = false;
       
    }    
    
     @FXML
     private void hExpAction(ActionEvent event) {
        if(labelEntry.getText().endsWith(".")) return;          
       if(   constantClicked==true  || labelEntry.getText().endsWith("!") || labelEntry.getText().endsWith(") ")  ) 
             labelEntry.setText(labelEntry.getText()+" * ");
        labelEntry.setText(labelEntry.getText()+"exp "); 
        functionClicked = true;
        constantClicked=false;
        operatorClicked = false;
    }  
     
    @FXML
     private void hModulusAction(ActionEvent event) {
        if(operatorClicked==true || functionClicked==true ||  labelEntry.getText().endsWith("( ")) return;          
        if(!labelEntry.getText().isEmpty()){         
          labelEntry.setText(labelEntry.getText() + " % " );
          operatorClicked = true;
          decimalPointClicked=false;
          signClicked =false;
          constantClicked=false;
          functionClicked = false;
        } 
        else {labelEntry.setText(  ans+" % " );
          operatorClicked = true;
          decimalPointClicked = false;
          signClicked =false;
          constantClicked=false;
          functionClicked = false;
        }  
    } 
        
    @FXML
     private void handleLeftPAction(ActionEvent event) {  
        if(operatorClicked==false && functionClicked == false && !labelEntry.getText().isEmpty() && !labelEntry.getText().endsWith("( ")) labelEntry.setText(labelEntry.getText()+" * ");
        labelEntry.setText(labelEntry.getText()+" ( ");
        leftParentheses++;
         
    } 
    @FXML
     private void handleRightPAction(ActionEvent event) {                              
        if( rightParentheses != leftParentheses && !labelEntry.getText().endsWith("( ") ){
            labelEntry.setText(labelEntry.getText()+" ) ");
            rightParentheses++;
        } 
    }     
      
    
}
