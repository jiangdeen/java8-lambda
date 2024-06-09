package com.jiangdeen.lambda.oneday;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @link{Predicate.class}
 */
public class PredicateTest {

    public static void main(String[] args) {
        PredicateTest test = new PredicateTest();
        // 找出所有用户中年龄大于60的人
        List<User> users = test.getAgeThanSixty();
        System.out.println(users.toString());
    }


    /**
     * 找出所有用户中年龄大于60的人
     */
    public List<User> getAgeThanSixty() {
        User xiaoming = new User("小明", 12, null);
        User xiaohong = new User("小红", 13, null);
        User daming = new User("大明", 61, null);
        return Stream.of(xiaoming, xiaohong, daming)
                .filter(x -> x.getAge() > 60)
                .collect(Collectors.toList());
    }


    /**
     * 登陆逻辑,函数
     *
     * @param username
     * @param password
     * @return
     */
    public boolean checkUser(String username, String password) {
        Supplier<Boolean> predicate = () -> "小明".equals(username) && "123456".equals(password);
        return predicate.get();
    }


    static class User {
        @Override
        public String toString() {
            return "User{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    ", password='" + password + '\'' +
                    '}';
        }

        public User(String name, int age, String password) {
            this.name = name;
            this.age = age;
            this.password = password;
        }

        private String name;

        private int age;

        private String password;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }


}
