package com.mtaparenka.enigne.camera;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class OrthographicCamera {
    public Vector3f position = new Vector3f(0f, 0f, 0f);
    public Vector3f front = new Vector3f(0f, 0f, -1f);
    public Vector3f up = new Vector3f(0f, 1f, 0f);
    public Matrix4f projection = new Matrix4f().identity();
    public Matrix4f view = new Matrix4f().identity();

    private final Vector3f tmp = new Vector3f();

    public float near = 0.0f;
    public float far = 100.0f;

    public OrthographicCamera(float width, float height) {
        projection.ortho(0.0f, width, 0.0f, height, near, far);
        tmp.set(front).add(position);
        view.lookAt(position, tmp, up);
        tmp.zero();
    }

    public void update() {
        view.translate(-position.x, -position.y, -position.z);
    }

    public void translate(float x, float y) {
        position.x += x;
        position.y += y;
    }
}
