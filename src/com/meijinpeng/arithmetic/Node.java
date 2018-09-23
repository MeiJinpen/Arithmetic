package com.meijinpeng.arithmetic;

import java.util.Objects;

/**
 * 记录每个节点的信息
 */
public class Node {

    private Fraction value;  //如果节点是数值则为本身，如果是符号则是运算后的结果

    private Node left;

    private Node right;

    public Node(Fraction value, Node left, Node right) {
        this.value = value;
        this.left = left;
        this.right = right;
    }

    public Node(Fraction value) {
        this.value = value;
    }

    public Fraction getValue() {
        return value;
    }

    public void setValue(Fraction value) {
        this.value = value;
    }

    public Node getLeft() {
        return left;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public Node getRight() {
        return right;
    }

    public void setRight(Node right) {
        this.right = right;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Node)) return false;
        Node node = (Node) o;
        return Objects.equals(value, node.value) &&
                Objects.equals(left, node.left) &&
                Objects.equals(right, node.right);
    }

    @Override
    public int hashCode() {

        return Objects.hash(value, left, right);
    }
}
