package com.nowcoder.community;

import org.omg.SendingContext.RunTime;

import java.io.IOException;

public class WKTest {
    public static void main(String[] args) {
        String cmd = "e:/wkhtmltopdf/bin/wkhtmltoimage https://www.nowcoder.com e:/wkhtmltopdf/wk-images/3.png";
        try {
            Runtime.getRuntime().exec(cmd);
            System.out.println("ok.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
