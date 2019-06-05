package snakeGame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class SnakePanel extends JPanel implements Runnable, KeyListener {

//set size of the SnakePanel window
	public static final int WIDTH = 700;
	public static final int HEIGHT = 500;
	
//settings for rendering
	private Graphics2D graphics2d;
	private BufferedImage newimage;
	
//settings for timing in Game
	private Thread thread1;
	private boolean running;
	private long Timming;
	
//SnakeGame details: size of block that makes up the snake, etc
	private final int SIZE = 10; //size of block that makes up the snake
	private Elements header,mouse;
	private ArrayList<Elements> snakeLine;
	private int Scores, Level;
	private boolean GameOver;
	private boolean FinalLevelCompleted;//paused;

//for moving the snake
	private int da, db;
	
//select control keys
	private boolean up, down, left, right, start;//space
	
//Snake Panel setup
	public SnakePanel() {
		setFocusable(true);
		requestFocus();
		addKeyListener(this);
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
	}
	@Override
	public void addNotify() {
		super.addNotify();
		thread1 = new Thread(this);
		thread1.start();
	}
	
	private void setFPS(int fps){
		Timming = 1000/fps;
	}
	
//Set events for control keys: "keyPressed" and "keyReleased"
	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		
		if(key == KeyEvent.VK_UP) up = true;
		if(key == KeyEvent.VK_DOWN) down = true;
		if(key == KeyEvent.VK_LEFT) left = true;
		if(key == KeyEvent.VK_RIGHT) right = true;
		if(key == KeyEvent.VK_ENTER) start = true;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		
		if(key == KeyEvent.VK_UP) up = false;
		if(key == KeyEvent.VK_DOWN) down = false;
		if(key == KeyEvent.VK_LEFT) left = false;
		if(key == KeyEvent.VK_RIGHT) right = false;
		if(key == KeyEvent.VK_ENTER) start = false;
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		
	}
	
//Set counter for the timing of game
	@Override
	public void run() {
		if (running) 
			return;
		init();
		long startingTime;
		long timeSpent;
		long wait;
		while (running){
			startingTime = System.nanoTime();
			
			updating();
			rendering();
			
			timeSpent = System.nanoTime() - startingTime;
			wait = Timming - timeSpent/1000000;
			if(wait > 0) {
				try{
					Thread.sleep(wait);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
	}
	
	private void init() {
		newimage = new BufferedImage(WIDTH, HEIGHT,BufferedImage.TYPE_INT_ARGB);
		graphics2d = newimage.createGraphics();
		running = true;
		setUplevel();
	
	}
	private void setUplevel(){
		snakeLine = new ArrayList<Elements>();
		header = new Elements(SIZE);
		header.setPosition(WIDTH/2, HEIGHT/2);
		snakeLine.add(header);
		
//start with snake length of 3 blocks and Game Level 1; 
		//Increase snake size with each snake mouse collision
		for(int i = 1;i < 3; i++){
			Elements e = new Elements(SIZE);
			e.setPosition(header.getA()+(i*SIZE), header.getB());
			snakeLine.add(e);
		}
		mouse = new Elements(SIZE);
		setMouse();
		Scores = 0;
		GameOver = false;
		Level = 1;
//Increase snake speed as Level increases
		setFPS(Level * 7);
	}
	
//create a new block (mouse) for snake to eat in random positions
	public void setMouse(){
		int a = (int)(Math.random() * (WIDTH - SIZE));
		int b = (int)(Math.random() * (HEIGHT - SIZE));
		a = a - (a % SIZE);
		b = b - (b % SIZE);
		mouse.setPosition(a, b);
	}
	private void rendering() {
		render(graphics2d);
		Graphics graphics = getGraphics();
		graphics.drawImage(newimage, 0, 0,null);
		graphics.dispose();
	}
	
//Generate Update for the game rendering and messages ...
	//...when game is over or final level is completed and stop the game
	private void updating() {
		if(GameOver){
			if(start){
				setUplevel();
			}
			return;
		}
		
		if(FinalLevelCompleted){
			if(start){
				setUplevel();
			}
			return;
		}
		/*if(paused){
			if(start){
				setUplevel();
			}
			return;
		}*/
		if(up && db == 0){
			db = -SIZE;
			da = 0;
		}
		if(down && db == 0){
			db = SIZE;
			da = 0;
		}
		if(left && da == 0){
			da = -SIZE;
			db = 0;
		}
		if(right && da == 0 && db != 0){
			da = SIZE;
			db = 0;
		}
		if(da != 0 || db != 0){
		for(int i = snakeLine.size() - 1;i > 0;i--){
			snakeLine.get(i).setPosition(
					snakeLine.get(i - 1).getA(),snakeLine.get(i - 1).getB());
		}
		header.moveSnake(da, db);
		}
		
//Stop game when snake collide with its body
		for(Elements e : snakeLine){
			if(e.isCollsion(header)){
				GameOver = true;
				break;
			}
		}
//Increase score with snake collision with new block
		if(mouse.isCollsion(header)){
			Scores++;
			setMouse();
		
//Increase game level after a certain score is reached
			Elements e = new Elements(SIZE);
			e.setPosition(-100, -100);
			snakeLine.add(e);
			if(Scores % 5 == 0)
			Level++;
			
//Stop game if Final Level is Completed
			//for(;;){
				while(Level > 3){
			//}
				if(Level > 3){
					FinalLevelCompleted = true;
				}
				break;
				}
			if(Level > 3) Level = 3;
			setFPS(Level * 7);
		}

		if(header.getA() < 0) header.setA(WIDTH);
		if(header.getB() < 0) header.setB(HEIGHT);
		if(header.getA() > WIDTH) header.setA(0);
		if(header.getB() > HEIGHT) header.setB(0);
	}
	
//Set background color of the game
		private void render(Graphics2D graphics2d) {
		graphics2d.setColor(Color.black);
		graphics2d.fillRect(0, 0, WIDTH, HEIGHT);
		//graphics2d.clearRect(0, 0, WIDTH, HEIGHT);
		
//Set snake block color
		graphics2d.setColor(Color.MAGENTA);
		for(Elements e : snakeLine){
			e.render(graphics2d);
		}
		
//Set "Mouse Block" color
		graphics2d.setColor(Color.BLUE);
		mouse.render(graphics2d);
		
//Set "Game Over" message color
		graphics2d.setColor(Color.RED);
		//mouse.render(graphics2d);
		if(GameOver){
			graphics2d.drawString("Game Over!!!", 300, 250);
		}
		
//Set "start game!" message color
		graphics2d.setColor(Color.white);
		graphics2d.drawString("Score: " + Scores + "   "+ "Level: " + Level, 10, 10);
		if(da == 0 && db == 0){
			graphics2d.drawString("start game!", 300, 250);
		}
		
//Set "Final Level Completed" message color
		graphics2d.setColor(Color.green);
		//mouse.render(graphics2d);
		if(FinalLevelCompleted){
			graphics2d.drawString("Final Level Completed: Congratulations!!!", 200, 200);
			//GameOver = true;
	}

}
}
