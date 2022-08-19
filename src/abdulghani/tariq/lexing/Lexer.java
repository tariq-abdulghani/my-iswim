package abdulghani.tariq.lexing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Lexer {
    private  final String code;

    private int currentIndex =0;
    private int startIndex = 0;
    private int lineNumber = 1;

    private int indentationLevel=0; // or integral count of four spaces
    private int numberOfSpacesPerLevel = 0; // can be reinitialized once at the first block
    private boolean insideBlock = false;

    private List<Token> tokens = new ArrayList<>();
    private static final Map<String, TokenType > reservedKeyWords = new HashMap<>();

    private final String notInIdentifier = ".-+#&<>,%^\\*/():;=~!";

    // reserved key words
    static {
        reservedKeyWords.put(TokenType.LET.getValue(), TokenType.LET);
        reservedKeyWords.put(TokenType.FUNC.getValue(), TokenType.FUNC);
        reservedKeyWords.put(TokenType.NOOP.getValue(), TokenType.NOOP);
        reservedKeyWords.put(TokenType.LOGIC_AND.getValue(), TokenType.LOGIC_AND);
        reservedKeyWords.put( TokenType.LOGIC_OR.getValue(), TokenType.LOGIC_OR);
        reservedKeyWords.put(TokenType.LOGIC_NOT.getValue(), TokenType.LOGIC_NOT);
        reservedKeyWords.put(TokenType.IN.getValue(), TokenType.IN);

        reservedKeyWords.put(TokenType.IF.getValue(), TokenType.IF);
        reservedKeyWords.put(TokenType.ELIF.getValue(), TokenType.ELIF);
        reservedKeyWords.put(TokenType.ELSE.getValue(), TokenType.ELSE);
        reservedKeyWords.put(TokenType.FOR.getValue(), TokenType.FOR);
        reservedKeyWords.put( TokenType.WHILE.getValue(), TokenType.WHILE);
        reservedKeyWords.put(TokenType.RETURN.getValue(), TokenType.RETURN);

        reservedKeyWords.put(TokenType.TRUE.getValue(), TokenType.TRUE);
        reservedKeyWords.put(TokenType.FALSE.getValue(), TokenType.FALSE);
        reservedKeyWords.put(TokenType.NONE.getValue(), TokenType.NONE);
    }

    public Lexer(String code) {
        this.code = code;
    }

    public List<Token> lex() throws Exception {
        while (!isAtEnd()){
            startIndex = currentIndex;
            scanToken();
        }
        return tokens;
    }

    // utils
    // is empty? // we cant exceed number of chars given
    private  boolean isAtEnd(){
        return currentIndex >= code.length();
    }
    // advance
    private char advance(){
        // what advance should return when we are at the end of the code?
        // what is no char value that doesn't mean empty ?
        // let use null char as end of string mark
        if(!isAtEnd()){
            return  code.charAt(currentIndex++);
        }else return '\0';
    }

    private char peek(){
        if(!isAtEnd()){ // why do we check??
            return  code.charAt(currentIndex);
        }else return '\0';
    }

    // match
    // if match is success should we go next?
    // match is single char lookahead??
    private boolean match(char c){
        if(isAtEnd()) return false;
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
                if(match('\n')) {// may be a start of block
                    insideBlock = true;
                }
                if(match('=')){
                    advance();
                    addToken(TokenType.ASSIGN, lineNumber);
                }else{
                    addToken(TokenType.COLON, lineNumber);
                }
                break;
            case ',': addToken(TokenType.COMMA, lineNumber);break;

            case '+': addToken(TokenType.PLUS, lineNumber);break;
            case '-': addToken(TokenType.MINUS, lineNumber);break;
            case '/': addToken(TokenType.SLASH, lineNumber);break;
            case '*': addToken(TokenType.TIMES, lineNumber);break;
            case '%': addToken(TokenType.MODULUS, lineNumber);break;

            case '=': addToken(TokenType.EQUAL, lineNumber);break;
            case '>': if (match('=')){
                    addToken(TokenType.LARGE_EQUAL, lineNumber);
                    advance();
                }else{
                    addToken(TokenType.LARGE, lineNumber);
                }break;

            case '<': if (match('=')){
                    addToken(TokenType.LESS_EQUAL, lineNumber);
                    advance();
                }else{
                    addToken(TokenType.LESS, lineNumber);
                }break;

            case ' ': break;
//            case '\t': break;

            case '\n':
                lineNumber++;
                // we must close all open blocks by inserting enough 'DEDENT' tokens
                if(!match(' ')){  // if next char is also not space we must close all scopes if we are in scope
                    dedentAfterNewLine();
                }
                if(match(' ')){ // if next is space we need to check for indentation
                    indent();
                }
            break;

            case '\0': addToken(TokenType.EOI, lineNumber);break;

            case '.':
                if (Character.isDigit(peek())) {
                    number(true);
                    break;
                }
            case '"':
                string();break;
            default:
                if(Character.isDigit(c)){
                    number(false);
                    break;
                }

                if(Character.isLetter(c)){
                    identifierOrKeyWord();
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

//    multi character tokens
    private void identifierOrKeyWord(){
        // not empty and not in special chars and not white space
        // parsers responsibility is t check the grammar here we only scan for tokens
        while (!isAtEnd()
                &&( notInIdentifier.indexOf(peek()) == -1
                && !Character.isWhitespace(peek())
        )
        ){
            advance();
        }
        String text =  code.substring(startIndex,currentIndex);
        TokenType type = reservedKeyWords.get(text);
        if(type != null){
            addToken(new Token(type, lineNumber));
        }else
        addToken(new Token(TokenType.ID, lineNumber, text)) ;
    }

    private void number(boolean withOutDot){
        int dotCount = withOutDot? 1: 0;
        while (!isAtEnd() && (Character.isDigit(peek()) || peek() == '.')
        ){
            if(peek() == '.') {
                if(dotCount != 0) throw new NumberFormatException("too many dots " + lineNumber);
                else dotCount ++;
            };

            advance();
        }
        String text =  code.substring(startIndex,currentIndex);
        addToken(new Token(TokenType.NUMBER, lineNumber, text));
    }

    private void string() throws Exception {
        while (!isAtEnd() && peek() != '"'){
            advance();
        }
        if(peek() == '"') {
            addToken(TokenType.STRING, lineNumber, code.substring(startIndex+1 , currentIndex));
            advance();
        }
        else throw new Exception("unterminated string error at " + lineNumber);
    }
}

//    when converting array of primitives to list using Arrays.asList() it has problematic effect
//    to avoid that problems i use string and
//    private char[] noneIdChars = new char[]{
//            '.', '-', '+', '#', '&',
//            '<', '>', ',', '%', '^',
//            '\\', '/', '(', ')', ':'};
