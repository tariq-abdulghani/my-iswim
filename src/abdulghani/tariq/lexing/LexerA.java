package abdulghani.tariq.lexing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LexerA {
    private  final String code;

    private int current =0; //?
    private int start = 0; // ?

    private int line = 1;
    private int tabCount=0;

    public LexerA(String code) {
        this.code = code;
    }

    public List<Token> lex() throws Exception {
        List<Token> tokens = new ArrayList<>();
        while (!isEmpty()){
            char c = advance();
            while ( c == '\n' || c == ' '){
                if(c == '\n')   line++;
                c = advance();
            }
            start =  current;
            Token token = matchToken(c);
            tokens.add(token);
        }
        return tokens;
    }
    private Token matchToken(char c) throws Exception {
        switch (c){
//            case ' ': ;
//            case '\n': line++;
            case '\t':
                Token t = indent();
                if(t != null){
                    return t;
                }
            case '(': return new Token(TokenType.L_PAREN,  line );
            case ')': return new Token(TokenType.R_PAREN, line);

            case '{': return new Token(TokenType.R_BRACKET, line);
            case '}': return new Token(TokenType.L_BRACKET, line);

            case '[': return new Token(TokenType.SQ_L_BRACKET, line);
            case ']': return new Token(TokenType.SQ_R_BRACKET, line);

            case ':': return new Token(TokenType.COLON, line);
            case ',': return new Token(TokenType.COMMA,  line);

            case '+': return new Token(TokenType.PLUS, line);
            case '-': return new Token(TokenType.MINUS, line);
            case '/': return new Token(TokenType.SLASH, line);
            case '*': return new Token(TokenType.TIMES, line);
            case '%': return new Token(TokenType.MODULUS, line);

            case '\0': return new Token(TokenType.EOI, line);
            default:
                if(Character.isLetter(c)){
                    Token token = identifier();
                    if (token != null) return token;
                }
                throw new Exception("invalid token " + c + "at " + line);
        }
    }

    boolean isEmpty(){
        return  current >= code.length();
    }

    char advance(){
        return  isEmpty()? '\0': code.charAt(current++);
    }

    // single lookahead
    boolean matches(char c){
        return  code.charAt(current) == c;
    }

    Token identifier(){
        char[] specialChars = new char[]{
                '.', '-', '+', '#', '&',
                '<', '>', ',', '%', '^',
                '\\', '/', '(', ')', ':'};
        char currentChar = code.charAt(current);
        while(!isEmpty() && (currentChar != ' ' && currentChar != '\n')){
            if(Arrays.asList(specialChars).contains(currentChar)) return null;
            currentChar = advance();
        }
        Token token = new Token(TokenType.ID, line, code.substring(start-1, current));
        return token;
    }

    Token indent(){
        int count = 1;
        while (code.charAt(current) == '\t'){
            advance();
            count++;
        }
        if(count > tabCount) {
            tabCount = count;
            return new Token(TokenType.INDENT, line);
        } else if(count < tabCount) {
            tabCount = count;
            return new Token(TokenType.DEDENT, line);
        } else {
            return null;
        }
    }
}
