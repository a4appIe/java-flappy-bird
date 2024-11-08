import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class FlappyBird extends JPanel implements ActionListener, KeyListener {
    int boardWidth = 360;
    int boardHeight = 640;
    Image bgImg;
    Image birdImg;
    Image topPipeImg;
    Image btmPipeImg;

    int birdX = boardWidth / 8;
    int birdY = boardHeight / 2;
    int birdWidth = 34;
    int birdHeight = 24;

    class Bird {
        int x = birdX;
        int y = birdY;
        int width = birdWidth;
        int height = birdHeight;
        Image img;

        Bird(Image img) {
            this.img = img;
        }
    }

    // PIPES
    int pipeX = boardWidth;
    int pipeY = 0;
    int pipeWidth = 64;
    int pipeHeight = 512;
    class Pipe{
        int x = pipeX;
        int y = pipeY;
        int width = pipeWidth;
        int height = pipeHeight;
        Image img;
        boolean passed;
        Pipe(Image img){
            this.img = img;
        }
    }

    // GAME
    Bird bird;
    int velocityX = -4;
    int velocityY = 0;
    int gravity = 1;

    ArrayList<Pipe> pipes;
    Random random = new Random();

    // TIMER
    Timer gameLoop;
    Timer placePipes;

    boolean gameOver = false;
    double score = 0;

    FlappyBird() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setFocusable(true);
        addKeyListener(this);
        bgImg = new ImageIcon(getClass().getResource("./assets/flappybirdbg.png")).getImage();
        birdImg = new ImageIcon(getClass().getResource("./assets/flappybird.png")).getImage();
        topPipeImg = new ImageIcon(getClass().getResource("./assets/toppipe.png")).getImage();
        btmPipeImg = new ImageIcon(getClass().getResource("./assets/bottompipe.png")).getImage();

        // BIRD
        bird = new Bird(birdImg);
        pipes = new ArrayList<Pipe>();

        // PLACE PIPE TIMER
        placePipes = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                placePipes();
            }
        });
        placePipes.start();

        // GAME TIMER
        gameLoop = new Timer(1000 / 60, this);
        gameLoop.start();

    }

    public void placePipes(){
        int randomPipeY = (int) (pipeY - pipeHeight/4 - Math.random() * (pipeHeight/2));
        int openingSpace = boardHeight/4;


        Pipe topPipe = new Pipe(topPipeImg);
        topPipe.y = randomPipeY;
        pipes.add(topPipe);

        Pipe btmPipe = new Pipe(btmPipeImg);
        btmPipe.y = topPipe.y + pipeHeight + openingSpace;
        pipes.add(btmPipe);

    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        g.drawImage(bgImg, 0, 0, boardWidth, boardHeight, null);
        g.drawImage(bird.img, bird.x, bird.y, bird.width, bird.height, null);
        for(int i = 0;i<pipes.size(); ++i){
            Pipe pipe = pipes.get(i);
            g.drawImage(pipe.img, pipe.x, pipe.y, pipe.width, pipe.height, null);
        }

        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.PLAIN, 32));

        if(gameOver){
            g.drawString("Game over: " + String.valueOf((int)score), 10, 35);
        } else {
            g.drawString("Score: " + String.valueOf((int)score), 10, 35);
        }
    }

    public void move() {
        bird.y += velocityY;
        velocityY += gravity;
        bird.y = Math.max(bird.y, 0);
        
        // PIPE
        for(int i=0; i<pipes.size(); ++i){
            Pipe pipe = pipes.get(i);
            pipe.x += velocityX;

            if(!pipe.passed && bird.x > pipe.x + pipe.width){
                pipe.passed = true;
                score += 0.5;
            }

            if(collision(bird, pipe)){
                gameOver = true;
            }
        }

        if(bird.y > boardHeight){
            gameOver = true;
        }

    }

    public boolean collision(Bird bird, Pipe pipe){
        return bird.x < pipe.x + pipe.width && bird.x + bird.width > pipe.x && bird.y < pipe.y + pipe.height && bird.y + bird.height > pipe.y;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if(gameOver){
            placePipes.stop();
            gameLoop.stop();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            velocityY = -9;
            if(gameOver){
                bird.y = birdY;
                velocityY = 0;
                pipes.clear();
                score = 0;
                gameOver = false;
                gameLoop.start();
                placePipes.start();
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
