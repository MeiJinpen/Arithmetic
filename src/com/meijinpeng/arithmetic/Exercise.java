package com.meijinpeng.arithmetic;

import com.sun.org.apache.xalan.internal.xsltc.compiler.util.StringStack;

import java.util.Stack;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 四则运算一道题目主要实现类
 */
public class Exercise {

    private Operation operation;

    private Node root;

    public Exercise(Operation operation, boolean isAuto) {
        this.operation = operation;
        if(isAuto) {
            ThreadLocalRandom random = ThreadLocalRandom.current();
            int kind = random.nextInt(4);
            if (kind == 0) kind = 1;
            root = build(kind);
            while (root.getValue().isZero()) {
                root = build(kind);
            }
        }
    }

    public Fraction getResult() {
        return root.getValue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Exercise)) return false;
        Exercise exercise = (Exercise) o;
        return root.equals(exercise.root);
    }

    /**
     * 随机生成一道四则运算题目
     *
     * @param num 运算符个数
     * @return 二叉树
     */
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
            value = calculate(((SymbolNode) node).getSymbol(), node.getLeft().getValue(), node.getRight().getValue());
        }
        node.setValue(value);
        return node;
    }

    /**
     * 中缀表达式生成树
     * @param exercise 中缀表达式
     * @return 二叉树
     */
    public Node build(String exercise) {
        String[] strs = exercise.trim().split(" ");
        Stack<Node> nodeStack = new Stack<>();
        StringStack symbolStack = new StringStack();
        //中缀表达式转换成前缀表达式，然后再用前序遍历生成数
        for (int i = strs.length - 1; i >= 0; i--) {
            String str = strs[i];
            if (!str.matches("[()+\\u00F7\\-x]")) {
                nodeStack.push(new Node(new Fraction(str)));
            } else {
                while (!symbolStack.empty() && (isMulOrDiv(symbolStack.peekString()) && isAddOrSub(str)
                        || str.equals(Constant.LEFT_BRACKETS))) {
                    String symbol = symbolStack.popString();
                    if(symbol.equals(Constant.RIGHT_BRACKETS)) {
                        break;
                    }
                    push(symbol, nodeStack);
                }
                if(str.equals(Constant.LEFT_BRACKETS)) {
                    continue;
                }
                symbolStack.pushString(str);
            }
        }
        while (!symbolStack.empty()) {
            push(symbolStack.popString(), nodeStack);
        }
        this.root = nodeStack.pop();
        return root;
    }

    /**
     * 将符号压入节点栈且计算结果
     */
    private void push(String symbol, Stack<Node> nodeStack) {
        Node left = nodeStack.pop();
        Node right = nodeStack.pop();
        SymbolNode node = new SymbolNode(symbol, left, right);
        node.setValue(calculate(symbol, left.getValue(), right.getValue()));
        nodeStack.push(node);
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
            if (isNeedBracketsLeft(((SymbolNode) node.getLeft()).getSymbol(), ((SymbolNode) node).getSymbol())) {
                left = Constant.LEFT_BRACKETS + " " + left + " " + Constant.RIGHT_BRACKETS;
            }
        }
        String right = print(node.getRight());
        if (node.getRight() instanceof SymbolNode && node instanceof SymbolNode) {
            if (isNeedBracketsRight(((SymbolNode) node.getRight()).getSymbol(), ((SymbolNode) node).getSymbol())) {
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

    //判断是否为减法
    private boolean isSubtract(String symbol) {
        return symbol.equals(Constant.SUBTRACTION);
    }

    private boolean isDivide(String symbol) {
        return symbol.equals(Constant.DIVISION);
    }

    /**
     * 比较两个符号中谁优先级最高，由于子树的符号优先级低需要加括号
     */
    private boolean isNeedBracketsLeft(String left, String mid) {
        return isAddOrSub(left) && isMulOrDiv(mid);
    }

    /*当右边需要加括号时，可以分为几种情况，第一种中间为除号时，右边所有运算都需要加括号，
    第二种中间为减号时，右边的加减法需要加括号，第三种优先级*/
    private boolean isNeedBracketsRight(String right, String mid) {
        return isAddOrSub(right) && isMulOrDiv(mid) || isDivide(mid) || isSubtract(mid) && isAddOrSub(mid);
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
