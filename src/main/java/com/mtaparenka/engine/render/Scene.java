package com.mtaparenka.engine.render;

public abstract class Scene {
    public abstract void updatePhysics(double dt);
    public abstract void update(double dt);
    public abstract void dispose();
}
