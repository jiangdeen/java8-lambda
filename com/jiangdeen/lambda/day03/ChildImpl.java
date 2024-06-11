package com.jiangdeen.lambda.day03;

public class ChildImpl implements Child{
    @Override
    public void message(String body) {
        System.out.println(body);
    }

    @Override
    public String getLastMessage() {
        return null;
    }
}
