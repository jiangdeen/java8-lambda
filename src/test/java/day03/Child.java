package day03;

public interface Child extends Parent {

    @Override
    default void welcome() {
        message("Child");
    }

}
