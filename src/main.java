import java.io.*;

    
class Calculator{
    String calcString;

    Calculator(String calcString){
	this.calcString = calcString;
    }

    public boolean isValid(){
	return true;
    }

    public int calculate(){
	return 0;
    }

    public String getCalcString(){
	return new String(calcString);
    }
}

class CalculatorDemo{
    public static void main (String[] args){

	Console con = System.console();
	String demoStr;

	if (con == null){
	    System.out.println("No console available");
	    return;
	}

	demoStr = con.readLine("Enter expression to calculate:\n");

	Calculator calc = new Calculator(new String(demoStr));

	if (calc.isValid()){
	    con.printf("Result is %d\n", calc.calculate());
	} else {
	    System.out.println("Error while parsing");
	}
	
    }
}
