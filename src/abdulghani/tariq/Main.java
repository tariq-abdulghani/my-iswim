package abdulghani.tariq;

import abdulghani.tariq.lexing.LexerA;

public class Main {

    public static void main(String[] args) throws Exception {
	// write your code here
        LexerA lexer = new LexerA("FOO (:), + - / % [] SER");

        String block = "outer :\n\tinner :\n\t\tinner_inner :\n\t\t\tx \n\n";
//        System.out.println(lexer.lex());
        System.out.println(new LexerA(block).lex());
    }
}
