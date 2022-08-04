package abdulghani.tariq;

import abdulghani.tariq.lexing.Lexer;
import abdulghani.tariq.lexing.LexerA;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) throws Exception {
	// write your code here
        Lexer lexer = new Lexer("foo:** (:), + zee  - / % [ \n:coco:\n ] ");
//        String block = "outer:\n" +
//                "    x\n" +
//                "    inner:\n" +
//                "        x\n" +
//                "    y";

//        String block = "outer :\n\tinner :\n\t\tinner_inner :\n\t\t\tx \n\n";
        System.out.println(lexer.lex());
//        System.out.println(new Lexer(block).lex());
    }
}
