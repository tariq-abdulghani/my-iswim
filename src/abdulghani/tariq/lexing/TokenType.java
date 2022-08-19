package abdulghani.tariq.lexing;

public enum TokenType {
    // literals
    NUMBER("number"),
    STRING("string"),
    TRUE("true"),
    FALSE("false"),
    NONE("none"), // ?

//    SET("set"),??
//    STRUCT("struct"),??
//    ARRAY("array"),??

//    MAP("map"), ?? is table? isnt it?
//    TABLE("table"), ?? why not

//---------------------
    LET("let"),
    FUNC("func"),
    ID("ID"),
    NOOP("noop"), // no operation

    L_PAREN("("),
    R_PAREN(")"),
    L_BRACKET("{"),
    R_BRACKET("}"),
    SQ_L_BRACKET("["),
    SQ_R_BRACKET("]"),
    COLON(":"),
    COMMA(","),
//  operators
    PLUS("+"),
    MINUS("-"),
    TIMES("*"),
    SLASH("/"),
    MODULUS("%"),
    DOT("."),
    ASSIGN(":="),

    LOGIC_AND("and"),
    LOGIC_OR("or"),
    LOGIC_NOT("not"),
    IN("in"),

    EQUAL("="),
    LESS_EQUAL("<="),
    LESS("<"),
    LARGE_EQUAL(">="),
    LARGE(">"),
    NOT_EQUAL("!="),

//    control
    IF("if"),
    ELIF("elif"),
    ELSE("else"),
    FOR("for"),
    WHILE("while"),
    TRY("try"),
    CATCH("catch"),
    RETURN("ret"),

//--------------
    EOI("\0"), // why we defined it?
    INDENT("INDENT"),
    DEDENT("DEDENT");

    private String value;

    TokenType(String v){
        this.value = v;
    }

    public String getValue() {
        return value;
    }
}
