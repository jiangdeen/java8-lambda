package com.jiangdeen.lambda.day03;

public interface Carriage {
    default String rock() {
        return "Carriage.rock";
    }
}
