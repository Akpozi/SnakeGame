package snakeGame;

//import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

//Set new position of snake with each movement
public class Elements {
	private int a,b,sizes;
	public Elements(int sizes){
		this.sizes = sizes;
}
	public int getA(){
		return a;
	}
	public int getB(){
		return b;
	}
	public void setA(int a){
		this.a = a;
		
	}
	public void setB(int b){
		this.b = b;
	}
	public void setPosition(int a,int b){
		this.a = a;
		this.b = b;
	}
	
//Snake movement
public void moveSnake(int da, int db){
	a += da;
	b += db;
}

//Set game boundaries
public Rectangle getBound(){
	return new Rectangle(a, b, sizes, sizes);
}

//check for snake collision
public boolean isCollsion(Elements e){
	if(e == this) return false;
	return getBound().intersects(e.getBound());
}
public void render(Graphics2D graphics2d){
	//graphics2d.setColor(Color.WHITE);
	graphics2d.fillRect(a+1, b+1, sizes-2, sizes-2);
}
}
