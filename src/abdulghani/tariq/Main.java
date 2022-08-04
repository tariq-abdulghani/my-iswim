package abdulghani.tariq;

import abdulghani.tariq.lexing.Lexer;


public class Main {

    public static void main(String[] args) throws Exception {
	// write your code here
        testIndentationRecognition();
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

        System.out.println(lexer.lex());
//        System.out.println(new Lexer(block).lex());
    }
}
