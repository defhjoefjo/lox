package com.duncan.chen.lox;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.duncan.chen.lox.TOKEN_TYPE.*;

public class Scanner {
    private final String source;
    private final List<Token> tokens = new ArrayList<>();
    private int start = 0;
    private int current = 0;
    private int line = 1;
    private static final Map<String, TOKEN_TYPE> keywords;
    // Directly taken from the book
    static {
        keywords = new HashMap<>();
        keywords.put("and",    AND);
        keywords.put("class",  CLASS);
        keywords.put("else",   ELSE);
        keywords.put("false",  FALSE);
        keywords.put("for",    FOR);
        keywords.put("fun",    FUN);
        keywords.put("if",     IF);
        keywords.put("nil",    NIL);
        keywords.put("or",     OR);
        keywords.put("print",  PRINT);
        keywords.put("return", RETURN);
        keywords.put("super",  SUPER);
        keywords.put("this",   THIS);
        keywords.put("true",   TRUE);
        keywords.put("var",    VAR);
        keywords.put("while",  WHILE);
    }

    public Scanner(String source) {
        this.source = source;
    }
    List<Token> Scan(){
        while (!atEnd()) {
            start = current;
            Scan_Token();
        }
        tokens.add(new Token(EOF, "", null, line));
        return tokens;
    }

    // check if it is at the end
    boolean atEnd() {
       return current >= source.length();
    }

    // return the chat at current position and move forward
    private char advance() {
        return source.charAt(current++);
    }

    // add a token to the token array
    private void add_token(TOKEN_TYPE type) {
        add_token(type,null);
    }

    private void add_token(TOKEN_TYPE type, Object literal){
        String lexeme = source.substring(start, current);
        tokens.add(new Token(type, lexeme, literal, line));
    }

    // To check if the next char is the expected when differentiating ==\=, !=\!, <=\<, >=\>
    private boolean match(char expected) {
        if (atEnd()) return false;
        if (source.charAt(current) != expected) return false;
        current++;
        return true;
    }
    
    private char peek() {
        if(atEnd()) return '\0';
        return source.charAt(current);
    }

    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private void number(){
        while(isDigit(peek())) advance();
        if (isDigit(peekNext()) && peek() == '.') {
            advance();
            while (isDigit(peek())) advance();
        }
        add_token(NUMBER,Double.parseDouble(source.substring(start,current)));
    }

    private char peekNext() {
        if (current + 1 >= source.length()) return '\0';
        return source.charAt(current + 1);
    }
    private void string() {
        while (peek() != '"' && !atEnd()) {
            if (peek() == '\n') line++;
            advance();
        }
        if (atEnd()) {
            System.out.println("Unfinished String");
            return;
        }
        advance();

        // Trim the surrounding quotes.
        String value = source.substring(start + 1, current - 1);
        add_token(STRING, value);
    }
     private boolean isAlpha(char c) {
        return  (c == '_' || (c <= 'z' && c >= 'a') || (c <= 'Z' && c >= 'A'));
     }
    private boolean isAlphaNumeric(char c) {
        return isAlpha(c) || isDigit(c);
    }
     private void identifier() {
        while (isAlphaNumeric(peek())) advance();
        String text = source.substring(start, current);
        TOKEN_TYPE type = keywords.get(text);
        if (type == null) type = IDENTIFIER;
        add_token(type);
     }
    // Identify tokens and add tokens to token array
    private void Scan_Token() {
        char c = advance();
        switch (c){
            case '(': add_token(LEFT_PAREN); break;
            case ')': add_token(RIGHT_PAREN); break;
            case '{': add_token(LEFT_BRACE); break;
            case '}': add_token(RIGHT_BRACE); break;
            case ',': add_token(COMMA); break;
            case '.': add_token(DOT); break;
            case '-': add_token(MINUS); break;
            case '+': add_token(PLUS); break;
            case ';': add_token(SEMICOLON); break;
            case '*': add_token(STAR); break;
            case '!':
                add_token((match('='))? NEG : NEG_EUQAL);
                break;
            case '=':
                add_token((match('='))? EQUAL_EQUAL : EQUAL);
                break;
            case '<':
                add_token(match('=') ? LESS_EQUAL : LESS);
                break;
            case '>':
                add_token(match('=') ? GREATER_EQUAL : GREATER);
                break;
            case '/':
                // the token is comment
                if (match('/')) {
                    while (peek() != '\n' && !atEnd()) {
                        advance();
                    }
                } else {
                    add_token(SLASH);
                }
                break;
            case ' ':
            case '\r':
            case '\t':
                // Ignore whitespace.
                break;
            case '\n':
                line++;
                break;
            case '"': string(); break;
            default:
                if (isDigit(c)){
                    number();
                } else if (isAlpha(c)) {
                    identifier();
                }
                else {
                    Lox.error(line, "Unexpected charcter");
                }
                break;
        }
    }

}
