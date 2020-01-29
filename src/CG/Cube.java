package CG;

import java.awt.*;
import java.util.ArrayList;

public class Cube {
    private int a;
    private ArrayList<Triangle> surface;

    public Cube(int a) {
        this.a = a;
        surface = new ArrayList<>();
        createSurface();
    }

    public ArrayList<Triangle> getSurface() {
        return surface;
    }

    private void createSurface() {
        surface.add(new Triangle(
                new Vertex(-a, a, a,1),
                new Vertex(a, a, a,1),
                new Vertex(-a, -a, a,1),
                Color.WHITE));

        surface.add(new Triangle(
                new Vertex(a, -a, a,1),
                new Vertex(a, a, a,1),
                new Vertex(-a, -a, a,1),
                Color.WHITE));

        surface.add(new Triangle(
                new Vertex(a, a, a,1),
                new Vertex(a, a, -a,1),
                new Vertex(a, -a, -a,1),
                Color.WHITE));

        surface.add(new Triangle(
                new Vertex(a, a, a,1),
                new Vertex(a, -a, a,1),
                new Vertex(a, -a, -a,1),
                Color.WHITE));


        surface.add(new Triangle(
                new Vertex(-a, a, -a,1),
                new Vertex(a, a, -a,1),
                new Vertex(-a, -a, -a,1),
                Color.WHITE));

        surface.add(new Triangle(
                new Vertex(a, -a, -a,1),
                new Vertex(a, a, -a,1),
                new Vertex(-a, -a, -a,1),
                Color.WHITE));

        surface.add(new Triangle(
                new Vertex(-a, a, a,1),
                new Vertex(-a, a, -a,1),
                new Vertex(-a, -a, -a,1),
                Color.WHITE));

        surface.add(new Triangle(
                new Vertex(-a, a, a,1),
                new Vertex(-a, -a, a,1),
                new Vertex(-a, -a, -a,1),
                Color.WHITE));

        surface.add(new Triangle(
                new Vertex(-a, a, -a,1),
                new Vertex(a, a, -a,1),
                new Vertex(a, a, a,1),
                Color.WHITE));

        surface.add(new Triangle(
                new Vertex(-a, a, -a,1),
                new Vertex(-a, a, a,1),
                new Vertex(a, a, a,1),
                Color.WHITE));

        surface.add(new Triangle(
                new Vertex(-a, -a, -a,1),
                new Vertex(a, -a, -a,1),
                new Vertex(a, -a, a,1),
                Color.WHITE));

        surface.add(new Triangle(
                new Vertex(-a, -a, -a,1),
                new Vertex(-a, -a, a,1),
                new Vertex(a, -a, a,1),
                Color.WHITE));

    }


}
