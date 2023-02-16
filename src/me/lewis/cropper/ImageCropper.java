package me.lewis.cropper;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;

public class ImageCropper extends JPanel implements MouseListener, MouseMotionListener {

    private BufferedImage image;
    private boolean maintainAspectRatio;
    private Point startPoint;
    private Point endPoint;
    private Rectangle cropBox;
    private boolean dragging;
    private JButton saveButton;

    public ImageCropper(BufferedImage image, boolean maintainAspectRatio) {
        this.image = image;
        this.maintainAspectRatio = maintainAspectRatio;
        setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
        setOpaque(true);
        setBackground(Color.WHITE);
        addMouseListener(this);
        addMouseMotionListener(this);
        saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveImage();
            }
        });
        saveButton.setFont(new Font("Arial", Font.BOLD,15));
        saveButton.setOpaque(true);
        saveButton.setBackground(Color.WHITE);
        saveButton.setForeground(Color.BLACK);
        saveButton.setFocusPainted(false);
        add(saveButton);
    }

    public void setAspectRatio(int width, int height) {
        int size;
        if (width == 0 || height == 0) {
            return;
        }
        if (maintainAspectRatio) {
            double imageAspectRatio = (double) image.getWidth() / (double) image.getHeight();
            double targetAspectRatio = (double) width / (double) height;
            if (imageAspectRatio > targetAspectRatio) {
                width = (int) (height * imageAspectRatio);
            } else {
                height = (int) (width / imageAspectRatio);
            }
        }
        if (startPoint != null && endPoint != null) {
            int newWidth = width;
            int newHeight = height;
            if (maintainAspectRatio) {
                if (newWidth < newHeight) {
                    newWidth = newHeight;
                } else {
                    newHeight = newWidth;
                }
            }
            endPoint.x = startPoint.x + newWidth;
            endPoint.y = startPoint.y + newHeight;
            repaint();
        }
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

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == saveButton) {
            try {
                String extension = ".png";
                File output = new File("output." + extension);
                BufferedImage cropped = image.getSubimage(cropBox.x, cropBox.y, cropBox.width, cropBox.height);
                ImageIO.write(cropped, extension, output);
                JOptionPane.showMessageDialog(this, "Image saved successfully!");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "An error occurred while saving the image.");
                ex.printStackTrace();
            }
        }
    }
}
