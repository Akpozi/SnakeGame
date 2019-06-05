package snakeGame;

import java.awt.Dimension;

import javax.swing.JFrame;

public class MainClass {

	//settings for the JFrame
	public static void main(String[] args) {
		JFrame snakeFrame = new JFrame("Snake Game");
		snakeFrame.setContentPane(new SnakePanel());
		snakeFrame.pack();
		snakeFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		snakeFrame.setPreferredSize(new Dimension(SnakePanel.WIDTH, SnakePanel.HEIGHT));
		snakeFrame.setVisible(true);
		snakeFrame.setResizable(false);
		snakeFrame.setLocationRelativeTo(null);
	}

}
