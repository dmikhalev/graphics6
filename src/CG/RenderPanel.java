package CG;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

class RenderPanel extends JPanel implements ActionListener {
    private ArrayList<Triangle> triangleList;
    private Vertex point1;
    private Vertex point2;

    private JSlider FoVSlider;
    private double fovAngle;

    private JSlider dtSlider;

    private double theta;
    private Image background;

    RenderPanel(ArrayList<Triangle> triangleList, Vertex point1, Vertex point2) {
        this.triangleList = triangleList;
        this.point1 = point1;
        this.point2 = point2;
        FoVSlider = new JSlider(1, 179, 89);
        add(FoVSlider, BorderLayout.NORTH);

        dtSlider = new JSlider(-540, 540, 0);
        add(dtSlider, BorderLayout.NORTH);

        Timer timer = new Timer(5, this);
        timer.start();

        try {
            background = ImageIO.read(new File(("./src/background.jpg")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        //g2.setColor(Color.BLACK);
        //g2.fillRect(0, 0, getWidth(), getHeight());
        g2.drawImage(background, 0, 0, null);

        double denominator = Math.sqrt((point2.x - point1.x) * (point2.x - point1.x) +
                (point2.y - point1.y) * (point2.y - point1.y) +
                (point2.z - point1.z) * (point2.z - point1.z));
        double cx = (point2.x - point1.x) / denominator;
        double cy = (point2.y - point1.y) / denominator;
        double cz = (point2.z - point1.z) / denominator;

        double d = Math.sqrt(cy * cy + cz * cz);

        Matrix4 transfer = new Matrix4(new double[]{
                1, 0, 0, 0,
                0, 1, 0, 0,
                0, 0, 1, 0,
                -point1.x, -point1.y, -point1.z, 1
        });

        // cos = cz / d
        // sin = cy / d
        Matrix4 rotateX = new Matrix4(new double[]{
                1, 0, 0, 0,
                0, cz / d, cy / d, 0,
                0, -cy / d, cz / d, 0,
                0, 0, 0, 1
        });

        // cos = d
        // sin = -cx
        Matrix4 rotateY = new Matrix4(new double[]{
                d, 0, cx, 0,
                0, 1, 0, 0,
                -cx, 0, d, 0,
                0, 0, 0, 1
        });

        Matrix4 transfer2 = new Matrix4(new double[]{
                1, 0, 0, 0,
                0, 1, 0, 0,
                0, 0, 1, 0,
                point1.x, point1.y, point1.z, 1
        });


        Matrix4 rotateX2 = new Matrix4(new double[]{
                1, 0, 0, 0,
                0, (cz * (cz * cz + cy * cy)) / (d * d), (-cy * (cz * cz + cy * cy)) / (d * d), 0,
                0, (cy * (cz * cz + cy * cy)) / (d * d), (cz * (cz * cz + cy * cy)) / (d * d), 0,
                0, 0, 0, 1
        });

        Matrix4 rotateY2 = new Matrix4(new double[]{
                d / (d * d + cx * cx), 0, -cx / (d * d + cx * cx), 0,
                0, 1, 0, 0,
                cx / (d * d + cx * cx), 0, d / (d * d + cx * cx), 0,
                0, 0, 0, 1
        });

        Matrix4 rollTransform = new Matrix4(new double[]{
                Math.cos(Math.toRadians(theta)), -Math.sin(Math.toRadians(theta)), 0, 0,
                Math.sin(Math.toRadians(theta)), Math.cos(Math.toRadians(theta)), 0, 0,
                0, 0, 1, 0,
                0, 0, 0, 1
        });

        Matrix4 panOutTransform = new Matrix4(new double[]{
                1, 0, 0, 0,
                0, 1, 0, 0,
                0, 0, 1, 0,
                0, 0, -300, 1
        });

        double viewportWidth = getWidth();
        double viewportHeight = getHeight();
        double fov = Math.tan(fovAngle / 2) * 170;

        Matrix4 transformMatrix = transfer.multiply(rotateX).multiply(rotateY).multiply(rollTransform)
                .multiply(rotateY2).multiply(rotateX2).multiply(transfer2).multiply(panOutTransform);

        BufferedImage img = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);

        double[] zBuffer = new double[img.getWidth() * img.getHeight()];
        for (int q = 0; q < zBuffer.length; q++) {
            zBuffer[q] = Double.NEGATIVE_INFINITY;
        }

        for (Triangle triangle : triangleList) {
            Vertex v1 = transformMatrix.transform(triangle.v1);
            Vertex v2 = transformMatrix.transform(triangle.v2);
            Vertex v3 = transformMatrix.transform(triangle.v3);

            Vertex ab = new Vertex(v2.x - v1.x, v2.y - v1.y, v2.z - v1.z, v2.w - v1.w);
            Vertex ac = new Vertex(v3.x - v1.x, v3.y - v1.y, v3.z - v1.z, v3.w - v1.w);
            Vertex norm = new Vertex(
                    ab.y * ac.z - ab.z * ac.y,
                    ab.z * ac.x - ab.x * ac.z,
                    ab.x * ac.y - ab.y * ac.x,
                    1
            );
            double normalLength = Math.sqrt(norm.x * norm.x + norm.y * norm.y + norm.z * norm.z);
            norm.x /= normalLength;
            norm.y /= normalLength;
            norm.z /= normalLength;

            double angleCos = Math.abs(norm.z);

            v1.x = v1.x / (-v1.z) * fov;
            v1.y = v1.y / (-v1.z) * fov;
            v2.x = v2.x / (-v2.z) * fov;
            v2.y = v2.y / (-v2.z) * fov;
            v3.x = v3.x / (-v3.z) * fov;
            v3.y = v3.y / (-v3.z) * fov;

            v1.x += viewportWidth / 2;
            v1.y += viewportHeight / 2;
            v2.x += viewportWidth / 2;
            v2.y += viewportHeight / 2;
            v3.x += viewportWidth / 2;
            v3.y += viewportHeight / 2;

            int minX = (int) Math.max(0, Math.ceil(Math.min(v1.x, Math.min(v2.x, v3.x))));
            int maxX = (int) Math.min(img.getWidth() - 1, Math.floor(Math.max(v1.x, Math.max(v2.x, v3.x))));
            int minY = (int) Math.max(0, Math.ceil(Math.min(v1.y, Math.min(v2.y, v3.y))));
            int maxY = (int) Math.min(img.getHeight() - 1, Math.floor(Math.max(v1.y, Math.max(v2.y, v3.y))));
            double triangleArea = (v1.y - v3.y) * (v2.x - v3.x) + (v2.y - v3.y) * (v3.x - v1.x);

            for (int y = minY; y <= maxY; y++) {
                for (int x = minX; x <= maxX; x++) {
                    double b1 = ((y - v3.y) * (v2.x - v3.x) + (v2.y - v3.y) * (v3.x - x)) / triangleArea;
                    double b2 = ((y - v1.y) * (v3.x - v1.x) + (v3.y - v1.y) * (v1.x - x)) / triangleArea;
                    double b3 = ((y - v2.y) * (v1.x - v2.x) + (v1.y - v2.y) * (v2.x - x)) / triangleArea;
                    if (b1 >= 0 && b1 <= 1 && b2 >= 0 && b2 <= 1 && b3 >= 0 && b3 <= 1) {
                        double depth = b1 * v1.z + b2 * v2.z + b3 * v3.z;
                        int zIndex = y * img.getWidth() + x;
                        if (zBuffer[zIndex] < depth) {
                            img.setRGB(x, y, getShade(triangle.color, angleCos).getRGB());
                            zBuffer[zIndex] = depth;
                        }
                    }
                }
            }

        }

        g2.drawImage(img, 0, 0, null);
    }


    private Color getShade(Color color, double shade) {
        double redLinear = Math.pow(color.getRed(), 2.4) * shade;
        double greenLinear = Math.pow(color.getGreen(), 2.4) * shade;
        double blueLinear = Math.pow(color.getBlue(), 2.4) * shade;

        int red = (int) Math.pow(redLinear, 1 / 2.4);
        int green = (int) Math.pow(greenLinear, 1 / 2.4);
        int blue = (int) Math.pow(blueLinear, 1 / 2.4);

        return new Color(red, green, blue);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        theta += Math.toRadians(dtSlider.getValue());
        fovAngle = Math.toRadians(FoVSlider.getValue());
        repaint();
    }

}

