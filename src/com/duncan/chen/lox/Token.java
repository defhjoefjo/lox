package com.duncan.chen.lox;

public class Token {
    final TOKEN_TYPE type;
    final String lexeme;
    final int line;
    final Object literal;


    public Token(TOKEN_TYPE type, String lexeme, Object literal, int line) {
        this.type = type;
        this.lexeme = lexeme;
        this.literal = literal;
        this.line = line;
    }

    @Override
    public String toString() {
        return "Token{" +
                "type=" + type +
                ", lexeme='" + lexeme + '\'' +
                ", literal=" + literal +
                '}';
    }
}
