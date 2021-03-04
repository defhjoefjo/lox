package com.duncan.chen.lox;

class AstPrinter implements Expr.Visitor<String> {
    String print(Expr expr) {
        return expr.accept(this);
    }
    @Override
    public String visitBinaryExpr(Expr.Binary expr) {
        return parenthesize(expr.operator.lexeme, expr.left, expr.right);
    }

    @Override
    public String visitUnaryExpr(Expr.Unary expr) {
        return parenthesize(expr.operator.lexeme, expr.right);
    }

    @Override
    public String visitGroupingExpr(Expr.Grouping expr) {
        return parenthesize("group",expr.expression);
    }

    @Override
    public String visitLiteralExpr(Expr.Literal expr) {
        if (expr.value == null) return "nil";
        return expr.value.toString();
    }


    private String parenthesize(String name, Expr... exprs) {
        StringBuilder build = new StringBuilder();
        build.append("(").append(name);
        for (Expr expr : exprs) {
            build.append(" ");
            build.append(expr.accept(this));
        }
        build.append(")");
        return build.toString();

    }


}
