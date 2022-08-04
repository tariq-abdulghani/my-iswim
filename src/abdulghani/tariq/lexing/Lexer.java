package abdulghani.tariq.lexing;

import java.util.ArrayList;
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

//    when converting array of primitives to list using Arrays.asList() it has problematic effect
//    to avoid that problems i use string and
//    private char[] noneIdChars = new char[]{
//            '.', '-', '+', '#', '&',
//            '<', '>', ',', '%', '^',
//            '\\', '/', '(', ')', ':'};
    private String notInIdentifier = ".-+#&<>,%^\\*/():;";
    public Lexer(String code) {
        this.code = code;
    }

    public List<Token> lex() throws Exception {
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
        if(!isEmpty()){ // why do we check??
            return  code.charAt(currentIndex);
        }else return '\0';
    }

    // match
    private boolean match(char c){
        if(isEmpty()) return false;
        return c == code.charAt(currentIndex);
    }

    // functional methods

    // scan token
    private  void scanToken() throws Exception {
        char c = advance();
        switch (c){
            case '(': addToken(TokenType.L_PAREN, lineNumber);break;
            case ')': addToken(TokenType.R_PAREN, lineNumber);break;

            case '{': addToken(TokenType.R_BRACKET, lineNumber);break;
            case '}': addToken(TokenType.L_BRACKET, lineNumber);break;

            case '[': addToken(TokenType.SQ_L_BRACKET, lineNumber);break;
            case ']': addToken(TokenType.SQ_R_BRACKET, lineNumber);break;

            case ':':
                addToken(TokenType.COLON, lineNumber);
                if(match('\n')) insideBlock = true;
                break; // may be a start of block
            case ',': addToken(TokenType.COMMA, lineNumber);break;

            case '+': addToken(TokenType.PLUS, lineNumber);break;
            case '-': addToken(TokenType.MINUS, lineNumber);break;
            case '/': addToken(TokenType.SLASH, lineNumber);break;
            case '*': addToken(TokenType.TIMES, lineNumber);break;
            case '%': addToken(TokenType.MODULUS, lineNumber);break;

            case ' ': break;
//            case '\t': break;


            // we must close all open blocks by inserting enough 'DEDENT' tokens
            case '\n':
                lineNumber++;
                if(!match(' ')){  // if next char is also not space we must close all scopes if we are in scope
                    dedentAfterNewLine();
                }
                if(match(' ')){ // if next is space we need to check for indentation
                    indent();
                }
            ;break;

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
        // parsers responsibility is t check the grammar here we only scan for tokens
        while (!isEmpty()
                &&( notInIdentifier.indexOf(currentChar()) == -1
                    && !Character.isWhitespace(currentChar())
        )
        ){
            advance();
        }
        return new Token(TokenType.ID, lineNumber, code.substring(startIndex,currentIndex));
    }

    private void indent() throws Exception {
        if(insideBlock ){
            int spaceCount = 0;
            for (int i = currentIndex; code.charAt(i) == ' '; i++) {
                advance();
                spaceCount++;
            }
            // init number of spaces per indentation leve
            if(numberOfSpacesPerLevel == 0) numberOfSpacesPerLevel = spaceCount;

            if(spaceCount % numberOfSpacesPerLevel == 0){
                int indentationLevel = spaceCount/numberOfSpacesPerLevel;
                // if no changes in indentation level
                if(indentationLevel - this.indentationLevel ==0) return;
                // if level increased by 1
                else if(indentationLevel - this.indentationLevel == 1){
                    this.indentationLevel++;
                    addToken(TokenType.INDENT, lineNumber);
                }
                // if level decreased by 1
                else if(indentationLevel - this.indentationLevel == -1){
                    this.indentationLevel--;
                    addToken(TokenType.DEDENT, lineNumber);
                    if(indentationLevel ==0) insideBlock = false;
                }
            }else{
                throw new Exception("invalid indentation level  space count '"+ spaceCount +
                        "' is not  integral multiplier of " + numberOfSpacesPerLevel + " at " + lineNumber);
            }
        }
    }

    private void dedentAfterNewLine(){
        if(insideBlock){
            for (int i = 0; i < indentationLevel; i++) {
                addToken(TokenType.DEDENT, lineNumber);
            }
            indentationLevel = 0;
            insideBlock = false;
        }
    }
}
