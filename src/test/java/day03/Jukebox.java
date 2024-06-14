package day03;

public interface Jukebox {
    default String rock() {
        return "Jukebox.rock";
    }
}
