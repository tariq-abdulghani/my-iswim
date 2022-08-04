package abdulghani.tariq.lexing;

public class Token {
    TokenType type;
    int line;
    String text;

    public Token(TokenType type, int line) {
        this.type = type;
        this.line = line;
        this.text = null;
    }

    public Token(TokenType type, int line, String text) {
        this.type = type;
        this.line = line;
        this.text = text;
    }

    @Override
    public String toString() {
        return type + (text!= null? ": " +text : "");
    }
}
