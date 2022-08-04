package abdulghani.tariq.lexing;

import java.util.ArrayList;
import java.util.List;

public class Lexer {
    private  final String code;

    private int current =0;
    private int start = 0;

    private int line = 1;
    private int tabCount=0; // or integral count of four spaces
    private boolean insideBlock = false;

    private List<Token> tokens = new ArrayList<>();

    public Lexer(String code) {
        this.code = code;
    }

    public List<Token> lex(){
        return tokens;
    }

    // utils
    // is empty? // we cant exceed number of chars given
    // advance
    // match

    // functional
    private  void scanToken(){}



}
