package com.mtaparenka.pong;

import com.mtaparenka.engine.Window;

public class Game {
    public static void main(String[] args) {
        var window = new Window();
        var scene = new MainMenuScene(window);

        window.setScene(scene);
        window.run();
    }
}
