package com.jiangdeen.lambda.dto;

import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class School {

    /**
     * 校长
     */

    private String principal;

    /**
     * 班级
     */
    private List<Class> classList;

    public static School getSchool() {
        String sch = "{" +
                "\"classList\": [{" +
                "\"age\": 50," +
                "\"students\": [{" +
                "\"age\": 12," +
                "\"languageFraction\": 71.5," +
                "\"mathFraction\": 80.5," +
                "\"name\": \"小明\"" +
                "}, {" +
                "\"age\": 13," +
                "\"languageFraction\": 90.0," +
                "\"mathFraction\": 75.0," +
                "\"name\": \"小红\"" +
                "}]," +
                "\"teacher\": \"王老师\"" +
                "}, {" +
                "\"age\": 40," +
                "\"students\": [{" +
                "\"age\": 15," +
                "\"languageFraction\": 90.0," +
                "\"mathFraction\": 100.0," +
                "\"name\": \"小明1\"" +
                "}, {" +
                "\"age\": 16," +
                "\"languageFraction\": 600.0," +
                "\"mathFraction\": 95.0," +
                "\"name\": \"小红1\"" +
                "}]," +
                "\"teacher\": \"力老师\"" +
                "}]," +
                "\"principal\": \"王校长\"" +
                "}";
        return JSONObject.parseObject(sch, School.class);
    }


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Class {
        /**
         * 班主任
         */
        private String teacher;

        /**
         * 年龄
         */
        private int age;

        /**
         * 学生
         */
        private List<Student> students;





        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Student {
            /**
             * 姓名
             */
            private String name;
            /**
             * 年龄
             */
            private int age;

            /**
             * 语文分数
             */
            private double languageFraction;

            /**
             * 数学分数
             */
            private double mathFraction;

        }

    }
}