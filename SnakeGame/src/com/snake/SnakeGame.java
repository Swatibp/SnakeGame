package com.snake;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;
import javax.swing.JPanel;
import javax.swing.Timer;

public class SnakeGame extends JPanel implements ActionListener, KeyListener {

    static final int SCREEN_WIDTHX = 600;
    static final int SCRENN_HEIGHTY = 600;
    static final int SIZE = 20;
    static final int DELAY = 150;

    final int[] x = new int[SCRENN_HEIGHTY * SCREEN_WIDTHX];
    final int[] y = new int[SCRENN_HEIGHTY * SCREEN_WIDTHX];
    int score;
    int bodyPart = 2;
    int FoodEatten;
    int FoodX;
    int FoodY;
    int currentDirectionX = 1;
    int currentDirectionY = 0;
    boolean running = false;
    Random random;
    Timer timer;

    public SnakeGame() {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTHX, SCRENN_HEIGHTY));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(this);
        startGame();
    }

    public void startGame() {
        CreateFood();
        running = true;
        x[0] = SCREEN_WIDTHX / 2;
        y[0] = SCRENN_HEIGHTY / 2;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        if (running) {
            g.setColor(Color.YELLOW);
            g.fillOval(FoodX, FoodY, SIZE, SIZE);

            for (int i = 0; i < bodyPart; i++) {
                g.setColor(Color.RED);
                g.fillRect(x[i], y[i], SIZE, SIZE);
            }
        } else {
            GameOver(g);
        }
    }

    public void GameOver(Graphics g) {
        g.setColor(Color.RED);
        g.setFont(new Font("Serif", Font.BOLD, 75));
        FontMetrics met = getFontMetrics(g.getFont());
        g.drawString("Score : " + score, SCRENN_HEIGHTY / 5, SCREEN_WIDTHX / 3);
        g.drawString("Game Over", SCRENN_HEIGHTY / 5, SCREEN_WIDTHX / 2);
    }

    public void move() {
        for (int i = bodyPart; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
        x[0] = x[0] + currentDirectionX * SIZE;
        y[0] = y[0] + currentDirectionY * SIZE;
    }

    public void CreateFood() {
        FoodX = random.nextInt((int) (SCREEN_WIDTHX / SIZE)) * SIZE;
        FoodY = random.nextInt((int) (SCRENN_HEIGHTY / SIZE)) * SIZE;
    }

    public void CheckFood() {
        if (x[0] == FoodX && y[0] == FoodY) {
            score++;
            bodyPart++;
            CreateFood();
        }
    }

    public void CheckCollided() {
        // Check for collision with the snake's body
        for (int i = 1; i < bodyPart; i++) {
            if (x[0] == x[i] && y[0] == y[i]) { // Head collides with body
                running = false;
                break; // Exit loop since collision detected
            }

            if (x[0] < 0) { // left
                running = false;
            }

            if (x[0] >= SCREEN_WIDTHX) { // right
                running = false;
            }

            if (y[0] < 0) { // up
                running = false;
            }

            if (y[0] >= SCRENN_HEIGHTY) { // right
                running = false;
            }
        }

        // Check for collision with the edges of the screen
        if (x[0] < 0 || x[0] >= SCREEN_WIDTHX || y[0] < 0 || y[0] >= SCRENN_HEIGHTY) {
            running = false;
        }

        // Stop the timer if a collision is detected
        if (!running) {
            timer.stop();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_LEFT:
                if (currentDirectionX != 1) { // stop moving right
                    currentDirectionX = -1; // moving left
                    currentDirectionY = 0; // stop up, down move
                }
                break;
            case KeyEvent.VK_RIGHT:
                if (currentDirectionX != -1) { // stop moving left
                    currentDirectionX = 1; // moving right
                    currentDirectionY = 0; // stop up, down
                }
                break;
            case KeyEvent.VK_UP:
                if (currentDirectionY != 1) { // stop moving down
                    currentDirectionY = -1; // moving up
                    currentDirectionX = 0; // stop left and right
                }
                break;
            case KeyEvent.VK_DOWN:
                if (currentDirectionY != -1) { // stop moving up
                    currentDirectionY = 1; // moving down
                    currentDirectionX = 0; // stop left and right
                }
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            CheckFood();
            CheckCollided();
        }
        repaint();
    }
}
