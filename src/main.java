import java.io.*;

//tfi
enum LexemType{
	OPEN_BRACKET,
	CLOSE_BRACKET,
	NUM,
	SUM,
	SUB,
	MUL,
	DIV
}

//atomic lexem class
class Lexem{

    LexemType type;
    String lexem;

    Lexem(LexemType type, String lexem){
	this.type = type;
	this.lexem = lexem;
    }

    public String getLexem(){
	return this.lexem;
    }

    public LexemType getType(){
	return this.type;
    }
}

// class to analyze lexems in input string and to return result lexems
class LexAnalyzer{
    Lexem lexems [];

    LexAnalyzer(){
	this.lexems = null;
    }

    private void appendLexem(Lexem lex){
	Lexem new_ar [];
	if (lexems == null)
	    new_ar = new Lexem[1];
	else
	    new_ar = new Lexem[lexems.length + 1];

	for (int i = 0; i < new_ar.length - 1; ++i){
	    new_ar[i] = lexems[i];
	}

	new_ar[new_ar.length - 1] = lex;
	lexems = new_ar;
    }

    private static boolean isNum(char character){
	if (character <= ((int) '9') && (character >= ((int) '0')))
	    return true;
	else
	    return false;
    }

    public Lexem [] getLexems(){
	return this.lexems;
    }

    public static void outLexems(Lexem lex_ar[]){
	for (int i = 0; i < lex_ar.length; ++i){
	    System.out.println("Lexem " + lex_ar[i].getLexem() + " of type " + lex_ar[i].getType() );
	}
    }

    public void analyzeLexems(String inputStr){

	this.lexems = null;
	int lexCount = 0;
	char prevChar = '\u0000';
	String curLexem = "";
	LexemType lexType = LexemType.NUM;

	for (int i = 0; i < inputStr.length() ; ++i){
	    char currentChar = inputStr.charAt(i);
	    switch (currentChar){
		case '(':
		    lexType = LexemType.OPEN_BRACKET;
		    break;
		case ')':
		    lexType = LexemType.CLOSE_BRACKET;
		    break;
		case '+':
		    lexType = LexemType.SUM;
		    break;
		case '-':
		    lexType = LexemType.SUB;
		    break;
		case '*':
		    lexType = LexemType.MUL;
		    break;
		case '/':
		    lexType = LexemType.DIV;
		    break;
	    }

	    if (isNum(currentChar)){ //if is numeric character, then append it to growing lexem
		curLexem += currentChar;
	    } else { // else (we have only one-character lexems except nums) append to lexems array
		if (isNum(prevChar)){ // or if we have num in previous character then we add it to lexems array at first
		    this.appendLexem(new Lexem(LexemType.NUM, curLexem));
		    curLexem = "";
		}
		this.appendLexem(new Lexem(lexType, "" + currentChar));
	    }

	    prevChar = currentChar;
	}

	if (curLexem != "")
	    this.appendLexem(new Lexem(LexemType.NUM, curLexem));
    }
}

class Parser{
    Lexem lexems[];
    int position;

    Parser (Lexem lexems[]){
	this.lexems = lexems;
	this.position = 0;
    }

    public int parseExpr(){
	return parseAddSub();
    }

    public void resetParser(){
	this.position = 0;
    }

    public void setLexems(Lexem lexems[]){
	this.lexems = lexems;
    }

    private int parseAddSub(){
	int first_expr = parseMulDiv();

	++position;

	if (position > lexems.length)
	    return first_expr;

	if (lexems[position - 1].type == LexemType.SUB)
	    return first_expr - parseAddSub();
	else if (lexems[position - 1].type == LexemType.SUM)
	    return first_expr + parseAddSub();
	else
	    return first_expr;
    }

    private int parseMulDiv(){
	int first_expr = parseBrackets();

	
	++position;

	if (position > lexems.length)
	    return first_expr;

	if (lexems[position - 1].type == LexemType.MUL)
	    return first_expr * parseMulDiv();
	else if (lexems[position - 1].type == LexemType.DIV)
	    return first_expr / parseMulDiv();
	else {
	    --position;
	    return first_expr;
	}

    }

    private int parseBrackets(){
	int expr;
	if (lexems[position].type == LexemType.OPEN_BRACKET){
	    position++;
	    expr = parseAddSub();
	    position++;
	} else
	    expr = parseAtom();
	return expr;
    }

    private int parseAtom(){
	return Integer.parseInt(lexems[position++].lexem);
    }

}
    
class Calculator{
    String calcString;

    Calculator(String calcString){
	this.calcString = calcString;
    }

    public boolean isValid(){
	return true;
    }

    public int calculate(){
	LexAnalyzer lex_an = new LexAnalyzer();
	lex_an.analyzeLexems(calcString);

	LexAnalyzer.outLexems(lex_an.getLexems());

	Parser parser = new Parser(lex_an.getLexems());

	return parser.parseExpr();
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

class LexAnalyzerDemo{
    public static void main (String[] args){

	Console con = System.console();
	Lexem  lex_ar [];
	String demoStr;

	if (con == null){
	    System.out.println("No console available");
	    return;
	}

	demoStr = con.readLine("Enter expression to calculate:\n");

	LexAnalyzer lexAn = new LexAnalyzer();

	lexAn.analyzeLexems(demoStr);

	lex_ar = lexAn.getLexems();

	for (int i = 0; i < lex_ar.length; ++i){
	    System.out.println("Lexem " + lex_ar[i].getLexem() + " of type " + lex_ar[i].getType() );
	}
	
    }
}
