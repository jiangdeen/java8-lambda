package day03;

public interface Parent {

    void message(String body);

    default void welcome() {
        message("parent");
    }

    String getLastMessage();
}
