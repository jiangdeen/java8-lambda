package day04;

import com.alibaba.fastjson.JSONObject;
import com.jiangdeen.lambda.dto.School;
import com.jiangdeen.lambda.reconfig.StringCollector;
import org.apache.commons.math3.stat.descriptive.summary.Sum;
import org.junit.Test;
import org.testng.Assert;

import java.util.*;
import java.util.stream.Collectors;

public class Test04 {

    public static void method() {
        School school = School.getSchool();
        List<String> teacherNames = school.getClassList().stream()
                .map(School.Class::getTeacher)
                .collect(Collectors.toList());

    }

    /**
     * 测试list 集合流是否有序
     */
    @Test
    public void listSort() {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4);
        List<Integer> sameOrder = numbers.stream().collect(Collectors.toList());
        sameOrder.forEach(System.out::println);
        Assert.assertEquals(numbers, sameOrder);
    }


    /**
     * 测试通过stream的HashSet是否有序
     */
    @Test
    public void hashSetSort() {
        Set<Integer> numbers = new HashSet<>(Arrays.asList(4, 3, 2, 1));
        List<Integer> sameOrder = numbers.stream().collect(Collectors.toList());
        Assert.assertEquals(Arrays.asList(4, 3, 2, 1), sameOrder);
    }

    /**
     * stream排序操作
     */
    @Test
    public void hashSetSortOrdered() {
        Set<Integer> numbers = new HashSet<>(Arrays.asList(4, 3, 2, 1));
        List<Integer> sameOrder = numbers.stream()
                .sorted()
                .collect(Collectors.toList());
        Assert.assertEquals(Arrays.asList(1, 2, 3, 4), sameOrder);
    }


    @Test
    public void converTreeSet() {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4);
        LinkedList sameOrder = numbers.stream()
                .collect(Collectors.toCollection(LinkedList::new));
        Assert.assertEquals(Arrays.asList(1, 2, 3, 4), sameOrder);
    }

    @Test
    public void converValue() {
        School school = School.getSchool();
        Optional<School.Class> opt = school.getClassList().stream().collect(Collectors.maxBy(
                Comparator.comparing(it ->
                        it.getStudents().stream()
                                .collect(Collectors.summingDouble(s -> s.getLanguageFraction() + s.getMathFraction())))
        ));
        System.out.println(opt.get().getTeacher());
        Assert.assertTrue(opt.isPresent());
    }

    @Test
    public void average() {
        School school = School.getSchool();
        Double d = school.getClassList().stream()
                .flatMap(it -> it.getStudents().stream())
                .collect(
                        Collectors.averagingDouble(School.Class.Student::getLanguageFraction)

                );
        System.out.println(d);
        Assert.assertTrue(d > 60);
    }

    @Test
    public void partitioningBy() {
        School school = School.getSchool();
        Map<Boolean, List<School.Class>> d = school.getClassList().stream()
                .collect(
                        Collectors.partitioningBy(it -> it.getAge() > 40)
                );
        System.out.println(JSONObject.toJSON(d));
        Assert.assertTrue(d.size() > 1);
    }

    @Test
    public void groupBy() {
        School school = School.getSchool();
        Map<Integer, List<School.Class.Student>> listMap = school.getClassList().stream()
                .flatMap(it -> it.getStudents().stream())
                .collect(
                        Collectors.groupingBy(School.Class.Student::getAge)
                );
        System.out.println(JSONObject.toJSON(listMap));
        Assert.assertTrue(listMap.size() > 1);
    }

    @Test
    public void custom() {
        School school = School.getSchool();
        String s = school.getClassList().stream()
                .map(School.Class::getTeacher)
                .collect(new StringCollector(", ", "[", "]"));
        System.out.println(s);
        Assert.assertFalse(s.isEmpty());
    }


}
