package me.lewis.cropper;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;

public class ImageCropper extends JPanel implements MouseListener, MouseMotionListener {

    private BufferedImage image;
    private Rectangle cropBox;
    private JButton saveButton;
    private JButton rotateButton;

    public ImageCropper(BufferedImage image) {
            this.image = image;
            setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
            setOpaque(true);
            setBackground(Color.WHITE);
            addMouseListener(this);
            addMouseMotionListener(this);
            rotateButton = new JButton("Rotate");
            rotateButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    BufferedImage rotatedImage = rotateImage(image, 90);
                    ImageCropper cropper = new ImageCropper(rotatedImage);

                    Window[] windows = Window.getWindows();
                    for(Window window : windows)
                    {
                        if(window instanceof JFrame)
                        {
                            JFrame frame = (JFrame) window;
                            if(frame.getName().equalsIgnoreCase("cropper"))
                            {
                                frame.setContentPane(cropper);
                                frame.pack();
                                repaint();
                            }
                        }
                    }
                }
            });
            saveButton = new JButton("Save");
            saveButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    saveImage();
                }
            });
            buttonDesign(saveButton);
            buttonDesign(rotateButton);
            add(saveButton);
            add(rotateButton);
        }

        public void saveImage() {
            if (cropBox == null) {
                System.out.println("No crop box selected");
                return;
            }
            try {
                BufferedImage croppedImage = image.getSubimage(cropBox.x, cropBox.y, cropBox.width, cropBox.height);
                String desktop = System.getProperty("user.home") + "\\Desktop";
                ImageIO.write(croppedImage, "png", new File(desktop + "/cropped.png"));
                System.out.println("Image saved: " + desktop + "\\cropped.png");
                System.exit(0);
            } catch (RasterFormatException e) {
                System.out.println("Invalid crop box");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (image != null) {
                g.drawImage(image, 0, 0, this);

                if (cropBox != null) {
                    g.setColor(new Color(0, 0, 0, 150));
                    g.fillRect(0, 0, cropBox.x, getHeight());
                    g.fillRect(cropBox.x + cropBox.width, 0, getWidth() - cropBox.x - cropBox.width, getHeight());
                    g.fillRect(cropBox.x, 0, cropBox.width, cropBox.y);
                    g.fillRect(cropBox.x, cropBox.y + cropBox.height, cropBox.width, getHeight() - cropBox.y - cropBox.height);

                    g.setColor(Color.WHITE);
                    g.drawRect(cropBox.x, cropBox.y, cropBox.width, cropBox.height);
                }
            }
        }

        public void mouseClicked(MouseEvent e) {

        }

        public void mousePressed(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();
            if (cropBox == null) {
                cropBox = new Rectangle(x, y, 0, 0);
            } else {
                cropBox.x = x;
                cropBox.y = y;
            }
            repaint();
        }

        public void mouseReleased(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();
            int width = x - cropBox.x;
            int height = y - cropBox.y;

            // ensure 1:1 aspect ratio
            if (width > height) {
                width = height;
            } else {
                height = width;
            }
            cropBox.setSize(width, height);

            if(cropBox.getWidth() < 0 || cropBox.getHeight() < 0)
            {
                cropBox.x = image.getWidth() / 2;
                cropBox.y = image.getHeight() / 2;
                cropBox.setSize(10, 10);
            }

            if (cropBox.x + cropBox.width > image.getWidth()) {
                cropBox.x = image.getWidth() / 2;
                cropBox.setSize(10, 10);
            }
            if (cropBox.y + cropBox.height > image.getHeight()) {
                cropBox.y = image.getHeight() / 2;
                cropBox.setSize(10, 10);
            }

            saveButton.setVisible(true);
            repaint();
        }

        public void mouseEntered(MouseEvent e) {

        }

        public void mouseExited(MouseEvent e) {

        }

        public void mouseDragged(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();
            int width = x - cropBox.x;
            int height = y - cropBox.y;

            // ensure 1:1 aspect ratio
            if (width > height) {
                width = height;
            } else {
                height = width;
            }
            cropBox.setSize(width, height);

            if(cropBox.getWidth() < 0 || cropBox.getHeight() < 0)
            {
                cropBox.x = image.getWidth() / 2;
                cropBox.y = image.getHeight() / 2;
                cropBox.setSize(10, 10);
            }

            if (cropBox.x + cropBox.width > image.getWidth()) {
                cropBox.x = image.getWidth() / 2;
                cropBox.setSize(10, 10);
            }
            if (cropBox.y + cropBox.height > image.getHeight()) {
                cropBox.y = image.getHeight() / 2;
                cropBox.setSize(10, 10);
            }

            repaint();
        }

        public void mouseMoved(MouseEvent e) {

        }


    private BufferedImage rotateImage(BufferedImage image, double angle) {
        double radians = Math.toRadians(angle);
        double sin = Math.abs(Math.sin(radians));
        double cos = Math.abs(Math.cos(radians));
        int w = image.getWidth();
        int h = image.getHeight();
        int newWidth = (int) Math.floor(w * cos + h * sin);
        int newHeight = (int) Math.floor(h * cos + w * sin);

        BufferedImage rotated = new BufferedImage(newWidth, newHeight, image.getType());
        Graphics2D g2d = rotated.createGraphics();
        AffineTransform at = new AffineTransform();
        at.translate((newWidth - w) / 2, (newHeight - h) / 2);
        at.rotate(radians, w / 2, h / 2);
        g2d.setTransform(at);
        g2d.drawImage(image, 0, 0, null);
        g2d.dispose();

        return rotated;
    }

    public static void buttonDesign(JButton button)
    {
        button.setFont(new Font("Arial", Font.BOLD,15));
        button.setOpaque(true);
        button.setBackground(Color.WHITE);
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
    }
}
