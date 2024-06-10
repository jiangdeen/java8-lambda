package com.jiangdeen.lambda.twoday;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Test {


    public static void main(String[] args) {
        Test t = new Test();
        // 惰性求值
        t.inertEvaluation();

        // 及早求值
        t.eagerEvaluation();

        // map 转换成大写字母
        t.mapToUpperCase();

        t.testFlatMap();
        // 打印最大/最小的老师年龄
        t.testMaxOrMin();
    }


    /**
     * 惰性求值
     */
    public void inertEvaluation() {
        Stream.of("ab", "ac", "bc")
                .filter(x -> {
                    System.out.println("已经执行了filter方法1");
                    return x.contains("a");
                });
    }


    /**
     * 及早求值
     */
    public void eagerEvaluation() {
        long count = Stream.of("ab", "ac", "bc")
                .filter(x -> {
                    System.out.println("已经执行了filter方法2");
                    return x.contains("a");
                })
                .count();
        System.out.println(count);
    }


    /**
     * collect(toList)
     */
    public void toList() {
        List<String> list = Stream.of("ab", "ac", "bc").collect(Collectors.toList());
        list.forEach(System.out::println);
    }


    /**
     * 把列表中的字母转换成大写
     */
    public void mapToUpperCase() {
        Stream.of("ab", "ac", "bc")
                .map(String::toUpperCase)
                .forEach(System.out::println);
    }

    /**
     * 打印全校学生名称
     */
    public void testFlatMap() {
        // 一年级一班
        School.Class cl1 = new School.Class("王老师", Arrays.asList("小明", "小红"));
        // 一年级二班
        School.Class cl2 = new School.Class("力老师", Arrays.asList("大明", "大红"));
        School scl = new School("王校长", Arrays.asList(cl1, cl2));
        // 全校学生
        List<String> allStudents = scl.getClassList().stream()
                .flatMap(it -> it.getStudents().stream())
                .collect(Collectors.toList());

        allStudents.forEach(System.out::println);

    }


    /**
     * 打印全校学生名称
     */
    public void testMaxOrMin() {
        // 一年级一班
        School.Class cl1 = new School.Class("王老师", 33, Arrays.asList("小明", "小红"));
        // 一年级二班
        School.Class cl2 = new School.Class("力老师", 44, Arrays.asList("大明", "大红"));
        School scl = new School("王校长", Arrays.asList(cl1, cl2));

        // 全校学生
        School.Class cla1 = scl.getClassList().stream()
                .min(Comparator.comparing(School.Class::getAge))
                .get();

        System.out.println("年龄较小的: " + cla1.getTeacher());

        // 全校学生
        School.Class cla2 = scl.getClassList().stream()
                .max(Comparator.comparing(School.Class::getAge))
                .get();

        System.out.println("年龄较大的: " + cla2.getTeacher());

    }

    /**
     * 全校老师的总年龄
     */
    public void testReduce() {
        // 一年级一班
        School.Class cl1 = new School.Class("王老师", 33, Arrays.asList("小明", "小红"));
        // 一年级二班
        School.Class cl2 = new School.Class("力老师", 44, Arrays.asList("大明", "大红"));
        School scl = new School("王校长", Arrays.asList(cl1, cl2));

        // 全校学生
        int sumAge = scl.getClassList().stream()
                .map(School.Class::getAge)
                .reduce(0, Integer::sum);
        System.out.println("全校老师的总年龄: " + sumAge);
    }


    static class School {

        /**
         * 校长
         */

        private String principal;

        /**
         * 班级
         */
        private List<Class> classList;

        public School(String principal, List<Class> classList) {
            this.principal = principal;
            this.classList = classList;
        }

        public String getPrincipal() {
            return principal;
        }

        public void setPrincipal(String principal) {
            this.principal = principal;
        }

        public List<Class> getClassList() {
            return classList;
        }

        public void setClassList(List<Class> classList) {
            this.classList = classList;
        }


        static class Class {
            /**
             * 老师
             */
            private String teacher;

            public Class(String teacher, int age, List<String> students) {
                this.teacher = teacher;
                this.age = age;
                this.students = students;
            }

            public Class(String teacher, List<String> students) {
                this.teacher = teacher;
                this.students = students;
            }


            public int getAge() {
                return age;
            }

            public void setAge(int age) {
                this.age = age;
            }

            private int age;

            /**
             * 学生
             */
            private List<String> students;

            public String getTeacher() {
                return teacher;
            }

            public void setTeacher(String teacher) {
                this.teacher = teacher;
            }

            public List<String> getStudents() {
                return students;
            }

            public void setStudents(List<String> students) {
                this.students = students;
            }


        }
    }

}
