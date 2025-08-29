package com.mtaparenka;

public class Game {
    public static void main(String[] args) {
        var window = new Window();
        var scene = new TestScene(window.window);

        window.setScene(scene);
        window.run();
    }
}
