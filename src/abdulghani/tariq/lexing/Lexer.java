package abdulghani.tariq.lexing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Lexer {
    private  final String code;

    private int currentIndex =0;
    private int startIndex = 0;
    private int lineNumber = 1;

    private int indentationLevel=0; // or integral count of four spaces
    private int numberOfSpacesPerLevel = 0; // can be reinitialized once at the first block
    private boolean insideBlock = false;

    private List<Token> tokens = new ArrayList<>();

    private char[] noneIdChars = new char[]{
            '.', '-', '+', '#', '&',
            '<', '>', ',', '%', '^',
            '\\', '/', '(', ')', ':'};
    public Lexer(String code) {
        this.code = code;
    }

    public List<Token> lex(){
        while (!isEmpty()){
            startIndex = currentIndex;
            scanToken();
        }
        return tokens;
    }

    // utils
    // is empty? // we cant exceed number of chars given
    private  boolean isEmpty(){
        return currentIndex >= code.length();
    }
    // advance
    private char advance(){
        // what advance should return when we are at the end of the code?
        // what is no char value that doesn't mean empty ?
        // let use null char as end of string mark
        if(!isEmpty()){
            return  code.charAt(currentIndex++);
        }else return '\0';
    }

    private char currentChar(){
        return code.charAt(currentIndex);
    }

    // match
    private boolean match(char c){
        return c == code.charAt(currentIndex);
    }

    // functional methods

    // scan token
    private  void scanToken(){
        char c = advance();
        switch (c){
            case '(': addToken(TokenType.L_PAREN, lineNumber);break;
            case ')': addToken(TokenType.R_PAREN, lineNumber);break;

            case '{': addToken(TokenType.R_BRACKET, lineNumber);break;
            case '}': addToken(TokenType.L_BRACKET, lineNumber);break;

            case '[': addToken(TokenType.SQ_L_BRACKET, lineNumber);break;
            case ']': addToken(TokenType.SQ_R_BRACKET, lineNumber);break;

            case ':': addToken(TokenType.COLON, lineNumber);break; // may be a start of block
            case ',': addToken(TokenType.COMMA, lineNumber);break;

            case '+': addToken(TokenType.PLUS, lineNumber);break;
            case '-': addToken(TokenType.MINUS, lineNumber);break;
            case '/': addToken(TokenType.SLASH, lineNumber);break;
            case '*': addToken(TokenType.TIMES, lineNumber);break;
            case '%': addToken(TokenType.MODULUS, lineNumber);break;

            case ' ': break;
            case '\t': break;

            // if next char is also new line
            // we must close all open blocks by inserting enough 'DEDENT' tokens
            case '\n': lineNumber++; break;

            case '\0': addToken(TokenType.EOI, lineNumber);break;
            default:
                Token t = identifier();
                if(t != null) {
                    addToken(t);
                    break;
                }
                throw new IllegalArgumentException("invalid input token " + c + "  at" + lineNumber);
        }
    }
    // add token
    private void addToken(Token token){
        tokens.add(token);
    }
    private void addToken(TokenType type, int line){
        addToken(type, line, null);
    }
    private void addToken(TokenType type, int line, String text){
        tokens.add(new Token(type, line, text));
    }

    private Token identifier(){
        // not empty and not in special chars and not white space

        while (!isEmpty()
                &&( !Arrays.asList(noneIdChars).contains(currentChar())
                    && !Character.isWhitespace(currentChar()))
        ){
            advance();
        }
        if(!Arrays.asList(noneIdChars).contains(currentChar())){
            return new Token(TokenType.ID, lineNumber, code.substring(startIndex,currentIndex));
        }else return null;
    }
}
