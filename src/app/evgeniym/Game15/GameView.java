package app.evgeniym.Game15;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.SoundPool;
import android.preference.PreferenceManager;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.Toast;

@SuppressLint("ViewConstructor")
public class GameView extends SurfaceView{

   
    private SharedPreferences prefs;
    private Bitmap[][] bmps = new Bitmap[4][4];
    Game15 game;
    private GameThread mainThread;
    private boolean running = false;
    private SurfaceHolder holder;
    private int X, Y;
    Bitmap backgr, last_bmp, picture;
    boolean sound;
    private SoundPool sounds;
    private int gStart, gStop, gMove, gCantMove;
    /** GAME THREAD CLASS*/
    public class GameThread extends Thread{
    	
    	private GameView view;
    	
    	
    	public GameThread(GameView view){
    			this.view = view;
        }
    	public void setRunning(boolean run){
                running = run;
        }
    	
        public void run()
        {
            while (running)
            {
                Canvas canvas = null;
                try
                {
                    // lock the canvas
                    canvas = view.getHolder().lockCanvas();
                    synchronized (view.getHolder())
                    {
                        // draw
                        onDrawed(canvas);
                    }
                }
                catch (Exception e) { }
                finally
                {
                	//unlock the canvas
                    if (canvas != null)
                    {
                            view.getHolder().unlockCanvasAndPost(canvas);
                    }
                }
            }
        }
    }
    /**----------------------------------------------------------------------------
     * 
     * 
     * 
     * Game View Constructor 
     * @param mSelectedImage - if custom image from user's gallery
     * */
    
    
	public GameView(Context context, Bitmap mSelectedImage) {
		super(context);
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
		sound = prefs.getBoolean("sound", false);
		mainThread = new GameThread(this);
		holder = getHolder();
		backgr = scaleBackgroundImage(context);
		picture = mSelectedImage;
		loadSounds(context);
        holder.addCallback(new SurfaceHolder.Callback() 
        {
               public void surfaceDestroyed(SurfaceHolder holder) 
               {
            	   boolean retry = true;
            	   mainThread.setRunning(false);
            	   while (retry)
            	   {
            		   try {
						mainThread.join();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					   retry = false;
            	   }
               }

               @Override
               public void surfaceCreated(SurfaceHolder holder) 
               {
	                mainThread.setRunning(true);
	                //CHECK FOR LIVE STATUS (FOR EXAMPLE RETURN FROM PAUSE CASE)
	              
	                	mainThread.start();
	                	if(sound) sounds.play(gStart, 1.0f, 1.0f, 0, 0, 1.5f);
	                    Toast.makeText(getContext(), "Game started!", Toast.LENGTH_LONG).show();
	               
               }

               @Override
               public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) 
               {  
            	  
               }
          });
        		loadGraphics(context);
	}
/**
 * load all the sounds that will be present in game
 */
private void loadSounds(Context context) {
	sounds = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
	gStart = sounds.load(context, R.raw.start, 1);
	gStop = sounds.load(context, R.raw.win, 1);
	gMove = sounds.load(context, R.raw.move, 1);
	gCantMove =sounds.load(context, R.raw.cantmove, 1);		
	}

/**
 * Function to scale bitmap to use it as background
 * so it will be drawn full screen
 * @return
 */
private Bitmap scaleBackgroundImage(Context ctx) {
		Bitmap background =BitmapFactory.decodeResource(getResources(), R.drawable.bg);
		WindowManager wm = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();	
		Point size = new Point();
		display.getSize(size);
		float scale = (float) background.getHeight() / (float) size.y;
        int newWidth = Math.round(background.getWidth() / scale);
        int newHeight = Math.round(background.getHeight() / scale);
        Bitmap tmp = Bitmap.createScaledBitmap(background, newWidth, newHeight, true);
		return tmp;
	}





private void loadGraphics(Context context) {
	 calculateCoords(context); //Calculate the start points of canvas to place it in center
	 String s = prefs.getString("view", "classic");
     if(s.equalsIgnoreCase("classic")) loadGraphicsClassic();
     	else if(s.equalsIgnoreCase("moscow")) loadGraphicsMoscow();
     	else if(s.equalsIgnoreCase("beauty")) loadGraphicsBeauty();
     	else if(s.equalsIgnoreCase("custom")) loadCustom();
     this.game = new Game15(this, bmps, context, X, Y);		
	}

/**
 * function to calculate the center of the View
 * to place canvas in center of the screen
 */
private void calculateCoords(Context ctx) {
	WindowManager wm = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
	Display display = wm.getDefaultDisplay();	
	Point size = new Point();
	display.getSize(size);
	int width = size.x;
	int height = size.y;
	X = width/2 - 240;
	Y = height/2 - 240;
}


//Draw images on black background
  protected void onDrawed(Canvas canvas) throws InterruptedException 
  {
	  	  canvas.drawBitmap(backgr, 0, 0, null);
		  for(int i=0; i<16; i++){
			  game.squares[i%4][i/4].draw(canvas);
		  }


  }

  public boolean onTouchEvent(MotionEvent e) 
  {      
      if(e.getAction() == MotionEvent.ACTION_DOWN){
      for (int i=0; i<16; i++){
    	  if(game.squares[i%4][i/4].isSelected(e.getX(), e.getY())){
    		  switch (game.canMove(i)){
    		  case 0:
    			  //CANT MOVE
    			  if(sound) sounds.play(gCantMove, 1.0f, 1.0f, 0, 0, 1.5f);
    			  Toast.makeText(getContext(), "This block can't move", Toast.LENGTH_SHORT).show();
    			  break;
    		  case 1:
    			  //UP
    			  if(sound) sounds.play(gMove, 1.0f, 1.0f, 0, 0, 1.5f);
    			  game.swapUp(i);
    			  break;
    		  case 2:
    			  //RIGHT
    			  if(sound) sounds.play(gMove, 1.0f, 1.0f, 0, 0, 1.5f);
        		  game.swapRight(i);
        		  break;
    		  
    		  case 3:
    			  //DOWN
    			  if(sound) sounds.play(gMove, 1.0f, 1.0f, 0, 0, 1.5f);
    			  game.swapDown(i);
    			  break;
    		  case 4:
    			  //LEFT
    			  if(sound) sounds.play(gMove, 1.0f, 1.0f, 0, 0, 1.5f);
    			  game.swapLeft(i);
    			  break;
    		  }

    		  if(game.checkGameOver()){
    			  game.squares[3][3].setBitmap(last_bmp);
    			  if(sound) sounds.play(gStop, 1.0f, 1.0f, 0, 0, 1.5f);
    	          Toast.makeText(getContext(), "You did it, nerd!", Toast.LENGTH_LONG).show();
    		  }
    	 
    		  return true;
    		  
    	  }
      }
      
      }
              return true;
  }
  
  private void loadGraphicsClassic() {
		bmps[0][0]= BitmapFactory.decodeResource(getResources(), R.drawable.a1);
		bmps[1][0]= BitmapFactory.decodeResource(getResources(), R.drawable.a2);
		bmps[2][0]= BitmapFactory.decodeResource(getResources(), R.drawable.a3);
		bmps[3][0]= BitmapFactory.decodeResource(getResources(), R.drawable.a4);
		bmps[0][1]= BitmapFactory.decodeResource(getResources(), R.drawable.a5);
		bmps[1][1]= BitmapFactory.decodeResource(getResources(), R.drawable.a6);
		bmps[2][1]= BitmapFactory.decodeResource(getResources(), R.drawable.a7);
		bmps[3][1]= BitmapFactory.decodeResource(getResources(), R.drawable.a8);
		bmps[0][2]= BitmapFactory.decodeResource(getResources(), R.drawable.a9);
		bmps[1][2]= BitmapFactory.decodeResource(getResources(), R.drawable.a10);
		bmps[2][2]= BitmapFactory.decodeResource(getResources(), R.drawable.a11);
		bmps[3][2]= BitmapFactory.decodeResource(getResources(), R.drawable.a12);
		bmps[0][3]= BitmapFactory.decodeResource(getResources(), R.drawable.a13);
		bmps[1][3]= BitmapFactory.decodeResource(getResources(), R.drawable.a14);
		bmps[2][3]= BitmapFactory.decodeResource(getResources(), R.drawable.a15);
		bmps[3][3]= BitmapFactory.decodeResource(getResources(), R.drawable.a16);
		last_bmp = BitmapFactory.decodeResource(getResources(), R.drawable.a16);
	}
  private void loadGraphicsMoscow() {
		bmps[0][0]= BitmapFactory.decodeResource(getResources(), R.drawable.b1);
		bmps[1][0]= BitmapFactory.decodeResource(getResources(), R.drawable.b2);
		bmps[2][0]= BitmapFactory.decodeResource(getResources(), R.drawable.b3);
		bmps[3][0]= BitmapFactory.decodeResource(getResources(), R.drawable.b4);
		bmps[0][1]= BitmapFactory.decodeResource(getResources(), R.drawable.b5);
		bmps[1][1]= BitmapFactory.decodeResource(getResources(), R.drawable.b6);
		bmps[2][1]= BitmapFactory.decodeResource(getResources(), R.drawable.b7);
		bmps[3][1]= BitmapFactory.decodeResource(getResources(), R.drawable.b8);
		bmps[0][2]= BitmapFactory.decodeResource(getResources(), R.drawable.b9);
		bmps[1][2]= BitmapFactory.decodeResource(getResources(), R.drawable.b10);
		bmps[2][2]= BitmapFactory.decodeResource(getResources(), R.drawable.b11);
		bmps[3][2]= BitmapFactory.decodeResource(getResources(), R.drawable.b12);
		bmps[0][3]= BitmapFactory.decodeResource(getResources(), R.drawable.b13);
		bmps[1][3]= BitmapFactory.decodeResource(getResources(), R.drawable.b14);
		bmps[2][3]= BitmapFactory.decodeResource(getResources(), R.drawable.b15);
		last_bmp =	BitmapFactory.decodeResource(getResources(), R.drawable.b16);
		bmps[3][3]= BitmapFactory.decodeResource(getResources(), R.drawable.a16);
	}
  private void loadGraphicsBeauty() {
		bmps[0][0]= BitmapFactory.decodeResource(getResources(), R.drawable.c1);
		bmps[1][0]= BitmapFactory.decodeResource(getResources(), R.drawable.c2);
		bmps[2][0]= BitmapFactory.decodeResource(getResources(), R.drawable.c3);
		bmps[3][0]= BitmapFactory.decodeResource(getResources(), R.drawable.c4);
		bmps[0][1]= BitmapFactory.decodeResource(getResources(), R.drawable.c5);
		bmps[1][1]= BitmapFactory.decodeResource(getResources(), R.drawable.c6);
		bmps[2][1]= BitmapFactory.decodeResource(getResources(), R.drawable.c7);
		bmps[3][1]= BitmapFactory.decodeResource(getResources(), R.drawable.c8);
		bmps[0][2]= BitmapFactory.decodeResource(getResources(), R.drawable.c9);
		bmps[1][2]= BitmapFactory.decodeResource(getResources(), R.drawable.c10);
		bmps[2][2]= BitmapFactory.decodeResource(getResources(), R.drawable.c11);
		bmps[3][2]= BitmapFactory.decodeResource(getResources(), R.drawable.c12);
		bmps[0][3]= BitmapFactory.decodeResource(getResources(), R.drawable.c13);
		bmps[1][3]= BitmapFactory.decodeResource(getResources(), R.drawable.c14);
		bmps[2][3]= BitmapFactory.decodeResource(getResources(), R.drawable.c15);
		last_bmp =	BitmapFactory.decodeResource(getResources(), R.drawable.c16);
		bmps[3][3]= BitmapFactory.decodeResource(getResources(), R.drawable.a16);
	}
  /**
   * PARSE IMAGE FROM GALLERY TO 120x120 pixels chunks
   * fill array bmps[][] with those bitmaps
   */
  private void loadCustom(){
	    int rows=4,cols=4;
	    //For height and width of the small image chunks 
        int chunkHeight=120,chunkWidth=120;
        int yCoord = 0;
        for(int x=0; x<rows; x++){
            int xCoord = 0;
            for(int y=0; y<cols; y++){
                bmps[x][y]=(Bitmap.createBitmap(picture, xCoord, yCoord, chunkWidth, chunkHeight));
                xCoord += chunkWidth;
            }
            yCoord += chunkHeight;
        }
	last_bmp=bmps[3][3];
	bmps[3][3]= BitmapFactory.decodeResource(getResources(), R.drawable.a16);

	  
  }

}