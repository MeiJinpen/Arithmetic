package com.meijinpeng.arithmetic;

import java.util.Objects;

/**
 * 记录符号的节点
 */
public class SymbolNode extends Node {

    private String symbol;

    public SymbolNode(String symbol, Node left, Node right) {
        super(null, left, right);
        this.symbol = symbol;
    }

    public SymbolNode(String symbol) {
        super(null);
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return " " + symbol + " ";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SymbolNode)) return false;
        SymbolNode that = (SymbolNode) o;

        boolean flag = this.symbol != null && symbol.equals(that.symbol);
        if(!flag) return false;

        boolean left = this.getLeft() != null && getLeft().equals(that.getLeft());
        boolean right = this.getRight() != null && getRight().equals(that.getRight());
        //左右子树相同
        if(left && right) {
            return true;
        }
        if(left ^ right) {
            return false;
        }
        //如果是加法或乘法由于满足交换律所以要判断
        if(this.symbol.equals(Constant.ADDITION) || this.symbol.equals(Constant.MULTIPLICATION)) {
            left = this.getLeft() != null && getLeft().equals(that.getRight());
            right = this.getRight() != null && getRight().equals(that.getLeft());
        }
        return left && right;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), symbol);
    }
}
