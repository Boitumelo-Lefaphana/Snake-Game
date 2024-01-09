import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class snakeGame extends JPanel implements ActionListener, KeyListener {
    private class Tile {
        int x;
        int y;

        Tile(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    int boardWidth;
    int boardHeight;
    int tileSize = 25;

    // snake
    Tile snakeHead;
    ArrayList<Tile> snakeBody; // Corrected here

    //food
    Tile food;
    Random random;

    //game Logic
    Timer gameLoop;
    int velocityX;
    int velocityY;
    boolean gameOver = false;

    snakeGame(int boardWidth, int boardHeight) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        setPreferredSize(new Dimension(this.boardWidth, this.boardHeight));
        setBackground(Color.black);
        addKeyListener(this);
        setFocusable(true);

        snakeHead = new Tile(5, 5);
        snakeBody = new ArrayList<Tile>(); // Corrected here

        food = new Tile(10, 10);
        random = new Random();
        placeFood();

        gameLoop = new Timer(100, this);
        gameLoop.start();

        velocityX = 0;
        velocityY = 1;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        for (int i = 0; i < boardWidth / tileSize; i++) {
            g.drawLine(i * tileSize, 0, i * tileSize, boardHeight);
            g.drawLine(0, i * tileSize, boardWidth, i * tileSize);
        }

        //food
        g.setColor(Color.red);
        g.fillRect(food.x * tileSize, food.y * tileSize, tileSize, tileSize);
        //snake head
        g.setColor(Color.green);
        g.fillRect(snakeHead.x * tileSize, snakeHead.y * tileSize, tileSize, tileSize);

        //Snake Body
        for (int i = 0; i < snakeBody.size(); i++) {
            Tile snakePart = snakeBody.get(i); // Corrected here
            g.fillRect(snakePart.x * tileSize, snakePart.y * tileSize, tileSize,tileSize); // Corrected here
        }
        // score
        g.setFont(new Font( "Arial", Font.PLAIN, 16));
        if (gameOver) {
            g.setColor((Color.red));
            g.drawString("Game Over:"  + String.valueOf(snakeBody.size()), tileSize - 16, tileSize);
        }
        else {
            g.drawString("Score: " + String.valueOf(snakeBody.size()),tileSize-16, tileSize);
        }

    }

    public void placeFood() {
        food.x = random.nextInt(boardWidth / tileSize);
        food.y = random.nextInt(boardHeight / tileSize);
    }

    public boolean collision (Tile tile1, Tile tile2) { // Corrected here
        return tile1.x == tile2.x && tile1.y == tile2.y;
    }
    public void move() {

        if (collision(snakeHead, food)) { // Corrected here
            snakeBody.add (new Tile(food.x, food.y));
            placeFood();
        }

        //snake Body
        for (int i = snakeBody.size()-1; i >= 0; i--){
            Tile snakePart = snakeBody.get(i); // Corrected here
            if (i == 0) {
                snakePart.x = snakeHead.x;
                snakePart.y = snakeHead.y;
            }
            else {
                Tile prevSnakePart = snakeBody.get(i-1); // Corrected here
                snakePart.x = prevSnakePart.x;
                snakePart.y = prevSnakePart.y;
            }
        }

        snakeHead.x += velocityX;
        snakeHead.y += velocityY;
        //game over conditions
        for (int i = 0; i <snakeBody.size(); i++) {
            Tile snakePart = snakeBody.get(i);
            // collision with the head
            if (collision(snakeHead,snakePart)) {
                gameOver = true;
            }
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if (gameOver) {
            gameLoop.stop();
        }
        if (snakeHead.x*tileSize <0 || snakeHead.x*tileSize > boardWidth ||
            snakeHead.y*tileSize < 0 || snakeHead.y*tileSize > boardHeight) {
            gameOver = true;
        }
    }
    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP && velocityY != 1) {
            velocityX = 0;
            velocityY = -1;
        }
        else if (e.getKeyCode() == KeyEvent.VK_DOWN && velocityY != -1) {
            velocityX = 0;
            velocityY = 1;
        }
        else if (e.getKeyCode() == KeyEvent.VK_LEFT && velocityX != 1) {
            velocityX = -1;
            velocityY = 0;
        }
        else if (e.getKeyCode() == KeyEvent.VK_RIGHT && velocityX != -1) {
            velocityX = 1;
            velocityY = 0;
        }
    }
    @Override
    public void keyTyped(KeyEvent e) {
        // Implementation for keyTyped
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // Implementation for keyPressed
    }

}
