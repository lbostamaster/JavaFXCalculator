/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
public class FXMLstdCalculatorController implements Initializable {
    
    private boolean operatorClicked = false;  //permet de savoir s'il faut ecraser le nombre ou saisir un autre juxtaposé    
    String infixNotation;
    String memoryStore="0";
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
    private void handleCloseAboutButtonAction(ActionEvent event) {
        dialogPane.setVisible(false);
        dialogPane.setDisable(true);
        
    }
    
     //about
    @FXML
    private void aboutAction(ActionEvent event){   
        dialogPane.setVisible(true);
        dialogPane.setDisable(false);    
    }
    
    @FXML
    private void handleButtonAction(ActionEvent event) {
        System.out.println("You clicked me!");
        
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
        
        labelResult.setText("0");
    }
    //boutton clear
    @FXML
    private void handleButtonCAction(ActionEvent event) {
        
        labelResult.setText("0");
        labelEntry.setText("");
    }
    //boutton delete
    @FXML
    private void handleButtonDelAction(ActionEvent event) {
       //delete last inserted caracter
       //if there is only one caracter replace it with 0
       if(operatorClicked ==false){
        if(labelResult.getText().length()==1 )  
           labelResult.setText("0");
        else 
           labelResult.setText(labelResult.getText().substring(0, labelResult.getText().length()-1) ); 
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
            Button btn =(Button) event.getSource();
          labelEntry.setText(labelEntry.getText() + labelResult.getText()+ " "+btn.getText()+" " );
          operatorClicked = true;
          
    }
  
    //boutton =
    @FXML
    private void handleButtonEqualsAction(ActionEvent event) {        
        infixNotation = labelEntry.getText() + labelResult.getText();       
         labelResult.setText( generateResult(infixNotation) );        
         labelEntry.setText("");
         operatorClicked = true;       
    }
    //boutton %
    @FXML
    private void handleButtonPourcentAction(ActionEvent event) {
        if(labelEntry.getText().equals("") )           
            infixNotation = labelResult.getText()+ " / 100";
        else infixNotation = labelEntry.getText() + labelResult.getText()+ " * ( "+ labelEntry.getText().substring(0,labelEntry.getText().length()-3  )+ " ) / 100" ;  
        labelResult.setText( generateResult(infixNotation) );       
        labelEntry.setText("");
         operatorClicked = true;       
        }
    //boutton +/-        
    @FXML
    private void handleButtonSignAction(ActionEvent event) {
        if (labelResult.getText().startsWith("-"))
            labelResult.setText( labelResult.getText().substring(1, labelResult.getText().length()));
        else labelResult.setText("-"+ labelResult.getText());
    }
    //boutton square root  à bosser demain        
    @FXML
    private void handleButtonSquareRootAction(ActionEvent event) {                      
            labelResult.setText("√"+labelResult.getText());                   
    }
    
     //boutton inverse        
    @FXML
    private void handleButtonInverseAction(ActionEvent event) { 
            labelResult.setText("1 / "+ labelResult.getText());                        
            
    } 
     //boutton MC  Memory Clear       
    @FXML
    private void handleButtonMCAction(ActionEvent event) { 
    memoryStore = "0";
    operatorClicked= true;
    labelMemory.setText("");
            
    } 
     //boutton MR       
    @FXML
    private void handleButtonMRAction(ActionEvent event) { 
    labelResult.setText(memoryStore);
    operatorClicked= true;
    } 
     //boutton MS Memory store       
    @FXML
    private void handleButtonMSAction(ActionEvent event) { 
     if(labelResult.getText()!="0"){
        memoryStore = labelResult.getText(); 
        operatorClicked= true;
        labelMemory.setText("M");
     }
    } 
     //boutton M+        
    @FXML
    private void handleButtonMPlusAction(ActionEvent event) { 
        if(labelResult.getText()!="0"){
            double  memoryStoreValue =  Double.valueOf(memoryStore);
            double labelResultValue = Double.valueOf(labelResult.getText());
            memoryStore = String.valueOf(memoryStoreValue + labelResultValue);
            labelMemory.setText("M");
            operatorClicked= true;
        }
            
    } 
     //boutton M-        
    @FXML
    private void handleButtonMMinusAction(ActionEvent event) { 
       if(labelResult.getText()!="0"){
        double  memoryStoreValue =  Double.valueOf(memoryStore);
        double labelResultValue = Double.valueOf(labelResult.getText());
        memoryStore = String.valueOf(memoryStoreValue - labelResultValue);
        labelMemory.setText("M"); 
        operatorClicked= true;
       }
            
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
           infixNotation = labelEntry.getText() + labelResult.getText();         
           labelResult.setText( generateResult(infixNotation) );        
           labelEntry.setText("");
           operatorClicked = true;    
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
        //Stage stage = (Stage) anchorPane.getScene().getWindow(); 
        System.out.println("anchorPane id: "+ anchorPane.getId().toString());    
    }

    
    public  String generateResult(String infixNot){
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.getDefault());
        otherSymbols.setNaN("NaN");
        otherSymbols.setInfinity("Infinity");
        otherSymbols.setDecimalSeparator('.');
        DecimalFormat df = new DecimalFormat("#.##############",otherSymbols);
        
        
      PostfixEvaluator PostfixEvaluator = new PostfixEvaluator();
     String result;   
     String postfixNotation = ShuntingYard.postfix(infixNot);
     double  resultDouble = PostfixEvaluator.evaluatePostfix( postfixNotation );
    // long  resultLong = PostfixEvaluator.evaluatePostfixLongInteger( postfixNotation );
        //si le résultat est un entier alors il faut l'afficher sans virgule        
            //df.setRoundingMode(RoundingMode.CEILING);           
            //result = String.valueOf(resultDouble);
            result = df.format(resultDouble);                 
         return(result);      
      //return (String.valueOf(resultLong) );
    }
    public void printNumberOnLabel(String n){
        //if the label is displaying the default zero text then overwrite it.
        //if the user clicks on + / * - and then clik on a number :=> overwrite labelresult by the last number clicked
       if(labelResult.getText().equals("0") || operatorClicked == true ) {
            labelResult.setText( n );
            operatorClicked=false;
        }            
        else
            labelResult.setText(labelResult.getText()+ n ); 
    }
    public void printOperatorOnLabel(String n){       
            //labelResult.setText(labelResult.getText()+ n );             
          labelEntry.setText(labelEntry.getText() + labelResult.getText()+ " "+n+" " );
          operatorClicked = true;
          
    }
    public void printPointOnLabel(){
        if( operatorClicked==true){
            labelResult.setText("0." );
            operatorClicked=false;}
         else if(labelResult.getText().indexOf('.') == -1 )
            labelResult.setText(labelResult.getText()+ "." );                 
        
    }
    //---------------menu view--------------------------------
    //scientific caculator scene    
    @FXML
    private void handleScientificCalcAction(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("FXMLScientificCalculator.fxml"));         
        Scene scientifiCalculatorScene = new Scene(root);
        //
        //System.out.println("anchorPane: "+anchorPane.toString());
       // System.out.println("get scne: "+ anchorPane.getScene().toString()); 
        //System.out.println("get window : "+ anchorPane.getScene().getWindow().toString());         
        Stage stage = (Stage) anchorPane.getScene().getWindow();
        stage.setScene(scientifiCalculatorScene);
        stage.show();
        stage.setResizable(true);
    }
    
    
        
     //-----------------------------------------------------------------------------------
    
}
