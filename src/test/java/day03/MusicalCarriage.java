package day03;

public class MusicalCarriage implements Carriage,Jukebox{
    @Override
    public String rock() {
        return Carriage.super.rock();
    }
}
