package com.mtaparenka.engine.camera;

import org.joml.Matrix4f;
import org.joml.Vector2f;

public class OrthographicCamera {
    public final Vector2f position;
    public final Matrix4f projection; // projection matrix kinda converts NDC coords (-1, 1), to specified, e,g.(800x600)
    public final Matrix4f view;
    public final Matrix4f combined;// camera pov;

    public float zoom = 1f;
    public float near = -1.0f;
    public float far = 1.0f;

    public OrthographicCamera(float left, float right, float bottom, float top) {
        this.position = new Vector2f(0, 0);
        this.projection = new Matrix4f().ortho(left, right, bottom, top, near, far);
        this.view = new Matrix4f();
        this.combined = new Matrix4f();

        update();
    }

    public void update() {
        view.identity()
                .translate(-position.x, -position.y, 0)
                .scale(zoom, zoom, 1.0f);

        // projection * view
        projection.mul(view, combined);
    }

    public void setZoom(float zoom) {
        if (zoom <= 0) {
            this.zoom = 0.1f;
        } else {
            this.zoom = zoom;
        }
    }

    public void translate(float x, float y) {
        position.x += x;
        position.y += y;
    }
}
