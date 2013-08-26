package app.evgeniym.Game15;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Each block of the image mixed is present
 * as element of Square class (squared Sprite)
 * @author Evgeniy Mishustin
 *
 */
public class Square{
	
	private float x;
	private float y;
	private int size;
	private int index;
	private Bitmap bmp;
	
	
	public GameView gameView;
	
	public Square(GameView gw, Bitmap bm, float x, float y, int size, int ind){
		this.gameView=gw;
		this.bmp=bm;
		this.size=size;
		this.x=x;
		this.y=y;
		this.index=ind;		
	}
	
	public Square(Square s){
		this.gameView=s.gameView;
		this.bmp=s.bmp;
		this.size=s.size;
		this.x=s.x;
		this.y=s.y;
		this.index=s.index;
	}
	
	public float getX(){
		return this.x;		
	}
	public float getY(){
		return this.y;
	}
	public void setX(float x){
		this.x=x;
	}
	public void setY(float y){
		this.y=y;
	}
	public int getIndex(){
		return this.index;
	}
	public void setIndex(int i){
		this.index=i;
	}
	public void setBitmap(Bitmap bmp){
		this.bmp = bmp;
	}
	
	public void draw(Canvas c){
		c.drawBitmap(bmp, x, y, null);
	}



	public void moveRight() {
		setX(getX()+this.size);
	}
	public void moveLeft(){
		setX(getX()-this.size);
	}
	public void moveUp(){
		setY(getY()-this.size);
	}
	public void moveDown(){
		setY(getY()+this.size);
	}
	
	public void switchPlaces(Square s){
		float tempX, tempY;
		tempX=this.getX();
		tempY=this.getY();
		this.setX(s.getX());
		this.setY(s.getY());
		s.setX(tempX);
		s.setY(tempY);
	}
	
    public boolean isSelected(float x, float y) {
        if (x > this.x && x < (this.x + this.size) && y > this.y
                && y < (this.y + this.size)) {
            return true;
        }
        return false;
    }
 
	
}
