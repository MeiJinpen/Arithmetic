package com.meijinpeng.arithmetic;

/**
 * 分数属性类（包括整数）
 */
public class Fraction {

    private int s;  //分子

    private int m;  //分母

    /**
     * 数值分解成分数对象
     * @param str 表达式
     */
    public Fraction(String str) {
        int a = str.indexOf("'");
        int b = str.indexOf("/");
        if(a != -1) {
            int z = Integer.valueOf(str.substring(0, a));
            m = Integer.valueOf(str.substring(b + 1));
            s = z * m + Integer.valueOf(str.substring(a + 1, b));
        } else if(b != -1) {
            s = Integer.valueOf(str.split("/")[0]);
            m = Integer.valueOf(str.split("/")[1]);
        } else {
            m = 1;
            s = Integer.valueOf(str);
        }
    }

    /**
     * 通过分子分母组合成分数对象
     * @param s 分子
     * @param m 分母
     */
    public Fraction(int s, int m) {
        this.s = s;
        this.m = m;
//        if(m <= 0) {
//            throw new RuntimeException("分数分母不能为0");
//        }
        handle();
    }

    /**
     * 约分
     */
    private void handle() {
        int mod = 1;
        int max = s > m ? s : m;
        for (int i = 1; i <= max; i++) {
            if(s % i == 0 && m % i == 0) {
                mod = i;
            }
        }
        this.s = s / mod;
        this.m = m / mod;
    }

    public boolean isZero() {
        return m == 0;
    }

    public Fraction add(Fraction fraction) {
        return new Fraction(this.s * fraction.m + this.m * fraction.s, this.m * fraction.m);
    }

    public Fraction multiply(Fraction fraction) {
        return new Fraction(this.s * fraction.s, this.m * fraction.m);
    }

    public Fraction subtract(Fraction fraction) {
        return new Fraction(this.s * fraction.m - this.m * fraction.s, this.m * fraction.m);
    }

    public Fraction divide(Fraction fraction) {
        return new Fraction(this.s * fraction.m, this.m * fraction.s);
    }

    public boolean isNegative() {
        return s < 0;
    }

    @Override
    public String toString() {
        if(m == 1) {
            return String.valueOf(s);
        } else {
            int z = 0;
            if(m != 0 && s > m) {
                z = s / m;
            }
            if(z == 0) {
                return String.valueOf(s) + "/" + String.valueOf(m);
            } else {
                return String.valueOf(z) + "'" + String.valueOf(s % m) + "/" + String.valueOf(m);
            }
        }
    }
}
