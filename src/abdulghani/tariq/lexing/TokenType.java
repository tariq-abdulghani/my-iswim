package abdulghani.tariq.lexing;

public enum TokenType {

    INT("int"),
    REAL("real"),
    NUMBER("number"),

    ID("ID"),

    L_PAREN("("),
    R_PAREN(")"),
    L_BRACKET("{"),
    R_BRACKET("}"),
    SQ_L_BRACKET("["),
    SQ_R_BRACKET("]"),
    COLON(":"),
    COMMA(","),
    PLUS("+"),
    MINUS("-"),
    TIMES("*"),
    SLASH("/"),
    MODULUS("%"),
    EOI("\0"),
    INDENT("INDENT"),
    DEDENT("DEDENT");

    private String val;

    TokenType(String v){
        this.val = v;
    }
}
