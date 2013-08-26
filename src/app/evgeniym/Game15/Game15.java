package app.evgeniym.Game15;

import java.util.Random;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;

/**
 * this class describes main logic
 * of the 15 puzzle game and holds
 * within all the needed resources
 * @author Evgeniy Mishustin
 * id: 321029191
 *
 */
public class Game15 {
	
	/**INDEXES ARE ALWAYS GO FROM ZERO TO 15
	 * 15 IS THE INDEX OF THE "EMPTY" NODE
	 * THE DIRECTIONS CONSTANTS:
	 * 				1
	 * 			  4 0 2
	 * 				3
	 */
	private static final int INDEX_OF_THE_LAST = 15;
	private static final int CANT_MOVE = 0;
	private static final int UP = 1;
	private static final int RIGHT = 2;
	private static final int DOWN = 3;
	private static final int LEFT = 4;
	Square[][] squares = new Square[4][4];
	private SharedPreferences prefs;

    /**
     * Mixes up the generated field to ensure that we
     * will never get an impossible to solve puzzle
     */
    public Game15(GameView gv, Bitmap[][] bmps, Context context, int x, int y) {
    	prefs = PreferenceManager.getDefaultSharedPreferences(context);
        generateField(gv, bmps, x, y);
        mixUpField();
    }


	private void mixUpField() {
		Random rand = new Random();
		int df;
		//MIXING FIELD DEPENDS ON DIFFICULTY, EASY BY DEFAULT
		String d = prefs.getString("difficulty", "1");
		df=Integer.valueOf(d);
		for(int c=0; c<df*50; c++){
			
			int i = findEmptyNode();
			if(i==-1)	//mistake
				return;
			if(i==0){ 	//[0][0] case, can move down or right
				int r= rand.nextInt(2);
				switch(r){
				case 0:
					//move right
					swapRight(i);
					break;
				case 1:
					swapDown(i);
					break;
				}
			}
			if(i==1 || i==2){ //[0][1] and [0][2] case
				int r=rand.nextInt(3);
				switch(r){
				case 0:
					swapRight(i);
					break;
				case 1:
					swapDown(i);
					break;
				case 2:
					swapLeft(i);
					break;
				}
			}
			if(i==3){ //[0][3] case can move down or left
				int r= rand.nextInt(2);
				switch(r){
				case 0:
					//move right
					swapDown(i);
					break;
				case 1:
					swapLeft(i);
					break;
				}
			}
			if(i==4 || i==8){ //[1][0] and [2][0] case
				int r= rand.nextInt(3);
				switch(r){
				case 0:
					swapUp(i);
					break;
				case 1:
					swapRight(i);
					break;
				case 2:
					swapDown(i);
					break;
				}
			}
			//central square
			if(i==5 || i==6 || i==9 || i==10){
				int r = rand.nextInt(4);
				switch(r){
				case 0:
					swapUp(i);
					break;
				case 1:
					swapRight(i);
					break;
				case 2:
					swapDown(i);
					break;
				case 3:
					swapLeft(i);
					break;
				}
			}
			if(i==7 || i==11){
				int r = rand.nextInt(3);
				switch(r){
				case 0:
					swapUp(i);
					break;
				case 1:
					swapDown(i);
					break;
				case 2:
					swapLeft(i);
					break;					
				}
			}
			if(i==12){
				int r= rand.nextInt(2);
				switch(r){
				case 0:
					swapUp(i);
					break;
				case 1:
					swapRight(i);
					break;
				}
			}
			if(i==13 || i==14){
				int r=rand.nextInt(3);
				switch(r){
				case 0:
					swapUp(i);
					break;
				case 1:
					swapRight(i);
					break;
				case 2:
					swapLeft(i);
					break;					
				}
			}
			if(i==15){
				int r=rand.nextInt(2);
				switch(r){
				case 0:
					swapUp(i);
					break;
				case 1:
					swapLeft(i);
					break;					
				}
			}
		}
	}
	
	/**
	 * function looks up for an empty node. this node should
	 * be swap randomly with one of the nodes around it. 
	 * The problem is that different zones in the game field
	 * have different abilities to swap. For example [0][0] field
	 * may become only [0][1] or [1][0], but the [0][1] field has
	 * three possibilities to move. There are 9 different cases.
	 * This function returns number of empty node.
	 * @return int - number of empty node
	 */
	private int findEmptyNode(){
		for(int i=0;i<16;i++)
			if(this.squares[i%4][i/4].getIndex()==INDEX_OF_THE_LAST)
				return i;
		return -1;
	}
	
	/**
	 * makes the last square unfilled until game over
	 * @param b
	 */
/*	public void setTransparent(boolean b){
		if(b){
			int i = findEmptyNode();
			
		}
		
	}*/
	
	
	private void generateField(GameView gv, Bitmap[][] bmps, int x, int y) {
		  for (int i =0; i<16; i++){
	          squares[i%4][i/4]= new Square(gv, bmps[i%4][i/4], x+(i%4)*bmps[0][0].getHeight(), y+(i/4)*bmps[0][0].getHeight(),bmps[0][0].getHeight(), i);
	        }
	}
	
    public boolean checkGameOver()
    {
      for(int i=0; i<15; i++){
    	  if(this.squares[i%4][i/4].getIndex()!=i)
    		  return false;
      }
      return true;
    }
    
    public void swapUp(int i) {
		// SWITCH COORDINATES FOR REDRAW
    	this.squares[i%4][i/4].moveUp();
    	this.squares[i%4][(i/4)-1].moveDown();
    	//SWITCH SQUARES IN THE MATRIX
    	Square temp = new Square(this.squares[i%4][i/4]);
    	this.squares[i%4][i/4]= this.squares[i%4][(i/4)-1];
    	this.squares[i%4][(i/4)-1]=temp;
	}

    public void swapRight(int i){
    	//SWITCH COORDINATES FOR REDRAW
    	this.squares[i%4][i/4].moveRight();
    	this.squares[(i%4)+1][i/4].moveLeft();
    	//SWITCH SQUARES IN THE MATRIX
    	Square temp = new Square(this.squares[i%4][i/4]);
    	this.squares[i%4][i/4]= this.squares[(i%4)+1][i/4];
    	this.squares[(i%4)+1][i/4]=temp;
    }
    
    public void swapDown(int i) {
		// SWITCH COORDINATES FOR REDRAW
    	this.squares[i%4][i/4].moveDown();
    	this.squares[i%4][(i/4)+1].moveUp();
    	//SWITCH SQUARES IN THE MATRIX
    	Square temp = new Square(this.squares[i%4][i/4]);
    	this.squares[i%4][i/4]= this.squares[i%4][(i/4)+1];
    	this.squares[i%4][(i/4)+1]=temp;
	}
    
    public void swapLeft(int i){
    	//SWITCH COORDINATES FOR REDRAW
    	this.squares[i%4][i/4].moveLeft();
    	this.squares[(i%4)-1][i/4].moveRight();
    	//SWITCH SQUARES IN THE MATRIX
    	Square temp = new Square(this.squares[i%4][i/4]);
    	this.squares[i%4][i/4]= this.squares[(i%4)-1][i/4];
    	this.squares[(i%4)-1][i/4]=temp;
    }
    
    /**
     * Function that decides if current node can move
     * @param i - index of the node within matrix
     * @return true if can move false else
     */
    public int canMove(int i){
    	//UP CASE - CHECK IF NOT UPPER ROW FIRST
    	if((i/4)>0 && this.squares[i%4][(i/4)-1].getIndex()==INDEX_OF_THE_LAST){
    		return UP;
    	}
    	//RIGHT CASE - CHECK IF NOT RIGHT COLUMN
    	if((i%4)<3 && this.squares[(i%4)+1][i/4].getIndex()==INDEX_OF_THE_LAST){
    		return RIGHT;
    	}
    	//DOWN CASE - CHECK IF NOT LOWER ROW
    	if((i/4)<3 && this.squares[i%4][(i/4)+1].getIndex()==INDEX_OF_THE_LAST){
    		return DOWN;
    	}
    	//LEFT CASE - CHECK IF NOT LEFT COLUMN FIRST
    	if((i%4)>0 && this.squares[(i%4)-1][i/4].getIndex()==INDEX_OF_THE_LAST){
    		return LEFT;
    	}
    	return CANT_MOVE;
    }


	
}
