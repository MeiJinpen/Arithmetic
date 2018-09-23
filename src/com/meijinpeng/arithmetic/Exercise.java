package com.meijinpeng.arithmetic;

import java.util.BitSet;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 四则运算一道题目主要实现类
 */
public class Exercise {

    private Operation operation;

    private Node root;

    public Exercise(Operation operation) {
        this.operation = operation;
        ThreadLocalRandom random = ThreadLocalRandom.current();
        int kind = random.nextInt(4);
        if (kind == 0) kind = 1;
        root = build(kind);
        while (root.getValue().isZero()) {
            root = build(kind);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Exercise)) return false;
        Exercise exercise = (Exercise) o;
        return root.equals(exercise.root);
    }

    @Override
    public int hashCode() {

        return Objects.hash(root);
    }

    public Node getRoot() {
        return root;
    }

    public Node build(int num) {
        if (num == 0) {
            return new Node(createFraction(), null, null);
        }
        ThreadLocalRandom random = ThreadLocalRandom.current();
        Node node = new SymbolNode(Constant.SYMBOLS[random.nextInt(4)], null, null);
        int left = random.nextInt(num);   //左子树运算符数量
        int right = num - left - 1;   //右子树运算符数量
        node.setLeft(build(left));
        node.setRight(build(right));
        Fraction value = calculate(((SymbolNode) node).getSymbol(), node.getLeft().getValue(), node.getRight().getValue());
        if (value.isNegative()) {
            swapNode(node);
        }
        value = calculate(((SymbolNode) node).getSymbol(), node.getLeft().getValue(), node.getRight().getValue());
        node.setValue(value);
        return node;
    }

    /**
     * 计算运算总结果
     *
     * @param node
     * @return
     */
    private Fraction getResult(Node node) {
        //如果是叶子节点，则直接返回当前节点的value
        if (node.getLeft() == null || node.getRight() == null) {
            return node.getValue();
        }
        Node left = node.getLeft();
        Node right = node.getRight();
        //分别递归计算出左右子树的运算结果
        Fraction valueLeft = getResult(left);
        Fraction valueRight = getResult(right);
        //如果计算出的数据是负数的话，说明由于是减法导致，需要把左右节点交换
        if (valueLeft.isNegative()) {
            swapNode(left);
        }
        if (valueRight.isNegative()) {
            swapNode(right);
        }
        String symbol = ((SymbolNode) node).getSymbol();
        return calculate(symbol, valueLeft, valueRight);
    }

    /**
     * 打印题目和答案，例如：“( 1 + 2 ) x 3 = 9”
     */
    public String print() {
        return print(root) + " = " + root.getValue();
    }

    /**
     * 获取表达式，例如“( 1 + 2 ) x 3”
     */
    public String print(Node node) {
        if (node == null) {
            return "";
        }
        String mid = node.toString();
        String left = print(node.getLeft());
        if (node.getLeft() instanceof SymbolNode && node instanceof SymbolNode) {
            if (isNeedBrackets(((SymbolNode) node.getLeft()).getSymbol(), ((SymbolNode) node).getSymbol())) {
                left = Constant.LEFT_BRACKETS + " " + left + " " + Constant.RIGHT_BRACKETS;
            }
        }
        String right = print(node.getRight());
        if (node.getRight() instanceof SymbolNode && node instanceof SymbolNode) {
            if (isNeedBrackets(((SymbolNode) node).getSymbol(), ((SymbolNode) node.getRight()).getSymbol())) {
                right = Constant.LEFT_BRACKETS + " " + right + " " + Constant.RIGHT_BRACKETS;
            }
        }
        return left + mid + right;
    }

    //判断是否为加减法
    private boolean isAddOrSub(String symbol) {
        return symbol.equals(Constant.ADDITION) || symbol.equals(Constant.SUBTRACTION);
    }

    //判断是否为乘除法
    private boolean isMulOrDiv(String symbol) {
        return symbol.equals(Constant.MULTIPLICATION) || symbol.equals(Constant.DIVISION);
    }

    /**
     * 比较两个符号中谁优先级最高，由于子树的符号优先级低需要加括号
     */
    private boolean isNeedBrackets(String one, String two) {
        return isAddOrSub(one) && isMulOrDiv(two);
    }

    /**
     * 单步计算
     */
    private Fraction calculate(String symbol, Fraction left, Fraction right) {
        switch (symbol) {
            case Constant.ADDITION:
                return left.add(right);
            case Constant.MULTIPLICATION:
                return left.multiply(right);
            case Constant.SUBTRACTION:
                return left.subtract(right);
            default:
                return left.divide(right);
        }
    }

    /**
     * 交换左右子树
     */
    private void swapNode(Node node) {
        if (node != null) {
            Node t = node.getLeft();
            node.setLeft(node.getRight());
            node.setRight(t);
        }
    }

    /**
     * 随机生成分数
     */
    private Fraction createFraction() {
        if (randomBoolean()) {
            return new Fraction(random(operation.maxNum), 1);
        } else {
            if (randomBoolean()) {
                int y = random(operation.derBound);
                int x = random(y * operation.maxNum);
                return new Fraction(x, y);
            } else {
                int y = random(operation.derBound);
                return new Fraction(random(y), y);
            }
        }
    }

    private int random(int factor) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        int x = random.nextInt(factor);
        if (x == 0) x = 1;
        return x;
    }

    private boolean randomBoolean() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        return random.nextBoolean();
    }

}
