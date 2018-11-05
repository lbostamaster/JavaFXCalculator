/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxCalculator;

import java.util.Scanner;

/**
 *
 * @author h.moumni
 */
public class Utilities {
  
    public static double factorial(double number) {

		int facNumber = 1;
		for (int i = 1; i < number; i++) {
			facNumber = facNumber * (i + 1);
		}
		return facNumber;
	}
    
    public  static double Gamma(double z)
    {
        double tmp1 = Math.sqrt(2*Math.PI/z);
        double tmp2 = z + 1.0/(12 * z - 1.0/(10*z));      
        tmp2 = Math.pow(tmp2/Math.E, z);
        return tmp1 * tmp2;
    }
    public static double st_gamma(double x){
		return Math.sqrt(2*Math.PI/x)*Math.pow((x/Math.E), x);
	}
 
	public static double la_gamma(double x){
		double[] p = {0.99999999999980993, 676.5203681218851, -1259.1392167224028,
			     	  771.32342877765313, -176.61502916214059, 12.507343278686905,
			     	  -0.13857109526572012, 9.9843695780195716e-6, 1.5056327351493116e-7};
		int g = 7;
		if(x < 0.5) return Math.PI / (Math.sin(Math.PI * x)*la_gamma(1-x));
 
		x -= 1;
		double a = p[0];
		double t = x+g+0.5;
		for(int i = 1; i < p.length; i++){
			a += p[i]/(x+i);
		}
 
		return Math.sqrt(2*Math.PI)*Math.pow(t, x+0.5)*Math.exp(-t)*a;
	}   
   
        public static void main(String[] args) {
		
		System.out.println("Gamma \t\tStirling \t\tLanczos");
		for(double i = 1; i <= 20; i += 1){
			System.out.println("" + i/10.0 + "\t\t" + st_gamma(i/10.0) + "\t" + la_gamma(i/10.0)+ "\t" + Gamma(i/10.0)+ "\t" + factorial(i/10.0));
		}
        }        
}
