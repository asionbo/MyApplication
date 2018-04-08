package com.example.asionbo.myapplication;

import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
//    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

//    @Test
    public void testBoolean(){
        boolean flag = true;
        int i = 2;
        int a = 0;
        a &= ~i;

        System.err.println(~i);
        System.err.println(a);
        flag &= true;
        System.out.println("true\t&=\ttrue\t==>\t" + flag);
        flag = true;
        flag &= false;
        System.out.println("true\t&=\tfalse\t==>\t" + flag);
        flag = false;
        flag &= true;
        System.out.println("false\t&=\ttrue\t==>\t" + flag);
        flag = false;
        flag &= false;
        System.out.println("false\t&=\tfalse\t==>\t" + flag+"\n");

        flag = true;
        flag |= true;
        System.out.println("true\t|=\ttrue\t==>\t" + flag);
        flag = true;
        flag |= false;
        System.out.println("true\t|=\tfalse\t==>\t" + flag);
        flag = false;
        flag |= true;
        System.out.println("false\t|=\ttrue\t==>\t" + flag);
        flag = false;
        flag |= false;
        System.out.println("false\t|=\tfalse\t==>\t" + flag+"\n");

        System.out.println("^=  相同为假，不同为真");
        flag = true;
        flag ^= true;
        System.out.println("true\t^=\ttrue\t==>\t" + flag);
        flag = true;
        flag ^= false;
        System.out.println("true\t^=\tfalse\t==>\t" + flag);
        flag = false;
        flag ^= true;
        System.out.println("false\t^=\ttrue\t==>\t" + flag);
        flag = false;
        flag ^= false;
        System.out.println("false\t^=\tfalse\t==>\t" + flag);
    }

//    @Test
    public void testDoubleAdd(){
        double a = 90.2;
        double b = 0.01;
        double c = a+b;
        System.out.println(c);
        System.out.println(new BigDecimal(c).doubleValue());
        System.out.println(new BigDecimal(c).setScale(5,BigDecimal.ROUND_HALF_UP).doubleValue());
    }

//    @Test
    public void testYu(){
        int a = 31;
        int b = 36;
        int c = a - b%a;
        int d = a/2;
        String r = "";
        r = Integer.toHexString(23);
        if (r.length() % 2 == 1) {
            r = "0" + r;
        }
        System.out.println((byte)a);
        String s = patchHexString(r, 2);
        String h = "0x"+s;
//        System.out.println(s);
    }

    /**
     * HEX字符串前补0，主要用于长度位数不足。
     *
     * @param str
     *            String 需要补充长度的十六进制字符串
     * @param maxLength
     *            int 补充后十六进制字符串的长度
     * @return 补充结果
     */
    static public String patchHexString(String str, int maxLength) {
        String temp = "";
        for (int i = 0; i < maxLength - str.length(); i++) {
            temp = "0" + temp;
        }
        str = (temp + str).substring(0, maxLength);
        return str;
    }

}