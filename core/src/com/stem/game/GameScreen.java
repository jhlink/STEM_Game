package com.stem.game;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.Screen;

public class GameScreen implements Screen {
	
	static class screenObject {
		public Rectangle objPosit;
		public float  x;
		public float y;
		public float width;
		public float height;
		public Texture img;
		
		public screenObject(Rectangle objData) {
			this.objPosit = objData;
			this.x = objData.x;
			this.y = objData.y;
			this.width = objData.getWidth();
			this.height = objData.getHeight();
		}
		
		public void setTexture(Texture imgType) {
			this.img = imgType;
		}
	}
	
	final StemGame game;
	
	public SpriteBatch batch;
	public BitmapFont font;
	private OrthographicCamera camera;
	
//	Stop Signs
	private Texture blankStop;
	
//	Success images
	private	Texture	blackCheck;
	
//	Obstacles images
	private Texture yellDiam;
	private Texture empRect;
	
//	Rectangles used to logically represent objects	
	private Rectangle stopSign;
	private	Rectangle obstacle;
	private Rectangle blackCheckRect;
	
	private Vector3 touchPos;
	
	private Array<screenObject> stopSigns;
	private Array<screenObject> obstacles;
	
	
	//	private long snowFlakeTimes;
	private int	timeElapsing = 30;
	
	public GameScreen(final StemGame gam) {
		this.game = gam;
		
		blankStop = new Texture(Gdx.files.internal("blank_stop.png"));
		yellDiam = new Texture(Gdx.files.internal("yellow_diamond.png"));
		empRect = new Texture(Gdx.files.internal("regrect.png"));
		blackCheck  = new Texture(Gdx.files.internal("lvlcheckmark.png"));
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);
		
		batch = new SpriteBatch();
		
		stopSign = new Rectangle();
		stopSign.x = 5;
		stopSign.y = 5;
		stopSign.width = 50;
		stopSign.height = 50;
		
		obstacle = new Rectangle();
		obstacle.x = 20;
		obstacle.y = 50;
		obstacle.width = 10;
		obstacle.height = 10;
		
		blackCheckRect = new Rectangle();
		blackCheckRect.x = 800 / 2 - 64 / 2;
		blackCheckRect.y = 50;
		blackCheckRect.width = 64;
		blackCheckRect.height = 64;

		stopSigns = new Array<screenObject>();
		obstacles = new Array<screenObject>();
		Random randGen = new Random();
		for (int i = 0; i < randGen.nextInt(10); i++) {
			spawnStopSigns();
			spawnObstacles();
		}
	}

	@Override
	public void render(float delta) {
		
//		Clears screen to make background white
		Gdx.gl.glClearColor(1, 1, 1, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

//		Tells camera to update matrices
		camera.update();
		
		game.batch.setProjectionMatrix(camera.combined);	
		game.batch.begin();
		game.font.draw(game.batch, "Time passed: " + timeElapsing, 0, 480);
		for(screenObject stopSignRect: stopSigns) {
			game.batch.draw(blankStop, stopSignRect.x, stopSignRect.y, stopSignRect.width, stopSignRect.height);
		}
		
		Random randGen = new Random();
		for(screenObject obstacle: obstacles) {
			if (randGen.nextInt(10) > 5) {
				batch.draw(yellDiam, obstacle.x, obstacle.y, obstacle.width, obstacle.height);
			} else {
				batch.draw(empRect, obstacle.x, obstacle.y, obstacle.width, obstacle.height);
			}	
		}
		game.batch.end();
		
		
//		Process user input		
		if(Gdx.input.isTouched()) {
			touchPos = new Vector3();
	      	touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
	     	camera.unproject(touchPos);
	     	
	     	
	     	for(screenObject stopSignRect: stopSigns) {
		     	if (touchPos.x > stopSignRect.x && touchPos.x < stopSignRect.x + stopSignRect.width) {
		     		if (touchPos.y > stopSignRect.y && touchPos.y < stopSignRect.x + stopSignRect.height) {
		     			batch.draw(blackCheck, stopSignRect.x, stopSignRect.y, stopSignRect.width, stopSignRect.height);
		     		}
	            }
	     	}
//	     	stopSign.x = touchPos.x - 64 / 2;
		}			

		
//		Use for snowflake level
//		if(TimeUtils.nanoTime() - lastDropTime > 1000000000)  {
//			spawnRaindrop();
//		}
		
//		Use this to make snowflakes move
//		Iterator<Rectangle> iter = raindrops.iterator();
//		   while(iter.hasNext()) {
//		      Rectangle raindrop = iter.next();
//		      raindrop.y -= 200 * Gdx.graphics.getDeltaTime();
//		      if(raindrop.y + 64 < 0) iter.remove();
//		}
	}
	
	private void spawnStopSigns() {
	      screenObject blank_stop = new screenObject(new Rectangle());
	      blank_stop.setTexture(blankStop);
	      blank_stop.x = MathUtils.random(0, 800-64);
	      blank_stop.y = MathUtils.random(0, 480-64);
	      blank_stop.width = MathUtils.random(40, 60);
	      blank_stop.height = MathUtils.random(40, 60);
	      stopSigns.add(blank_stop);
//	      lastStopSign = TimeUtils.nanoTime();
   }
	
	private void spawnObstacles() {
		screenObject obstacleImg = new screenObject(new Rectangle());
		Random randGen = new Random();
		
		if (randGen.nextInt(6) > 3) {
			obstacleImg.setTexture(yellDiam);
		} else {
			obstacleImg.setTexture(empRect);
		}
		
		obstacleImg.x = MathUtils.random(0, 800-64);
	    obstacleImg.y = MathUtils.random(0, 480-64);
	    obstacleImg.width = MathUtils.random(40, 60);
	    obstacleImg.height = MathUtils.random(40, 60);
	    obstacles.add(obstacleImg);
	    
//	      lastStopSign = TimeUtils.nanoTime();
 }
	
	public void dispose() {
		blankStop.dispose();
		empRect.dispose();
		yellDiam.dispose();
		blackCheck.dispose();
		batch.dispose();
   }
	
	@Override
   	public void resize(int width, int height) {
   	}

	@Override
	public void pause() {
	}
	
	@Override
	public void resume() {
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}
}