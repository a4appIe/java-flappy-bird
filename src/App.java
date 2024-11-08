import javax.swing.*;

public class App {
    public static void main(String[] args) throws Exception {
        int boardWidth = 360;
        int boardHeight = 640;
        JFrame frame = new JFrame("Flappy Game");
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        FlappyBird flappy_bird = new FlappyBird();
        frame.add(flappy_bird);
        frame.pack();
        flappy_bird.requestFocus();
        frame.setVisible(true);
    }
}
