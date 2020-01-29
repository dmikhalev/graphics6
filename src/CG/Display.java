package CG;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

class Display extends JFrame {

    private JTextField getX1 = new JTextField();
    private JTextField getY1 = new JTextField();
    private JTextField getZ1 = new JTextField();
    private JTextField getX2 = new JTextField();
    private JTextField getY2 = new JTextField();
    private JTextField getZ2 = new JTextField();


    Display() {
        setLayout(null);
        setTitle("CG6.5");
        setSize(220, 280);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel x1Label = new JLabel();
        x1Label.setText("X1: ");
        x1Label.setBounds(20, 80, 50, 30);
        add(x1Label);
        getX1.setBounds(45, 88, 30, 20);
        getX1.setEditable(true);
        add(getX1);
        JLabel y1Label = new JLabel();
        y1Label.setText("Y1: ");
        y1Label.setBounds(20, 110, 50, 30);
        add(y1Label);
        getY1.setBounds(45, 118, 30, 20);
        getY1.setEditable(true);
        add(getY1);
        JLabel z1Label = new JLabel();
        z1Label.setText("Z1: ");
        z1Label.setBounds(20, 140, 50, 30);
        add(z1Label);
        getZ1.setBounds(45, 148, 30, 20);
        getZ1.setEditable(true);
        add(getZ1);
        JLabel x2Label = new JLabel();
        x2Label.setText("X2: ");
        x2Label.setBounds(120, 80, 50, 30);
        add(x2Label);
        getX2.setBounds(145, 88, 30, 20);
        getX2.setEditable(true);
        add(getX2);
        JLabel y2Label = new JLabel();
        y2Label.setText("Y2: ");
        y2Label.setBounds(120, 110, 50, 30);
        add(y2Label);
        getY2.setBounds(145, 118, 30, 20);
        getY2.setEditable(true);
        add(getY2);
        JLabel z2Label = new JLabel();
        z2Label.setText("Z2: ");
        z2Label.setBounds(120, 140, 50, 30);
        add(z2Label);
        getZ2.setBounds(145, 148, 30, 20);
        getZ2.setEditable(true);
        add(getZ2);


        JButton draw = new JButton("Draw");
        draw.setBounds(20, 20, 165, 40);
        add(draw);
        draw.addActionListener(e -> {

            JFrame frame = new JFrame();
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null);
            frame.setResizable(false);
            frame.setVisible(true);

            Cube cube = new Cube(100);

            Vertex vertex1 = new Vertex(Integer.parseInt(getX1.getText()), Integer.parseInt(getY1.getText()),
                    Integer.parseInt(getZ1.getText()), 1);

            Vertex vertex2 = new Vertex(Integer.parseInt(getX2.getText()), Integer.parseInt(getY2.getText()),
                    Integer.parseInt(getZ2.getText()), 1);

            draw(cube.getSurface(), vertex1, vertex2, frame);
        });
    }

    private void draw(ArrayList<Triangle> triangleList, Vertex vertex1, Vertex vertex2, JFrame frame) {
        Container pane = frame.getContentPane();
        pane.setLayout(new BorderLayout());
        RenderPanel renderPanel = new RenderPanel(triangleList, vertex1, vertex2);
        pane.add(renderPanel, BorderLayout.CENTER);
    }

}
