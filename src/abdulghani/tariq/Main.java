package abdulghani.tariq;

import abdulghani.tariq.lexing.Lexer;

import java.util.Arrays;


public class Main {

    public static void main(String[] args) throws Exception {
	// write your code here
//        testOperators();
//        testIndentationRecognition();
//        testNumbers();
        testString();
     }

    private static void  testString() throws Exception {
        Lexer lexer = new Lexer("let xo:= \"hello world\",1.989, y:= \"coco\", z:=false, none , func, proc");
        System.out.println(lexer.lex());
    }
     private static void  testNumbers() throws Exception {
         Lexer lexer = new Lexer("let xo:= 10.99 , 20.45 + 456.3224");
         System.out.println(lexer.lex());
     }
    private static void testOperators() throws Exception {
        Lexer lexer = new Lexer("let x:= y  and or not if: while for else ");
        System.out.println(lexer.lex());
    }
    private static void testIndentationRecognition() throws Exception {
        Lexer lexer = new Lexer("foo:** (:), + zee  - / % [ \n:coco:\n ] ");
        String block = "outer:\n" +
                "    x\n" +
                "    inner:\n" +
                "        x\n" +
                "    y\n";

        String block2 = "foo2():\n" +
                "    pass\n";

        String block3="outer(x,y) number:\n" +
                "    pass\n" +
                "    inner(u,v) number:\n" +
                "        pass\n" +
                "        inner_inner(w,z) number:\n" +
                "            pass\n" +
                "    ret a\n" +
                "ret b\n" ;

//        System.out.println(lexer.lex());
        System.out.println(new Lexer(block3).lex());
    }

}
