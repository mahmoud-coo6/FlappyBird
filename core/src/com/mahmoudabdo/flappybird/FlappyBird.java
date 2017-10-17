package com.mahmoudabdo.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public class FlappyBird extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
    Texture[] birds;
    int flapState=0;
    float birdY=0;
    float veolocity=0;
    int gameState=0;
    float gravity=2;
    Texture topTube,bottomTube;
    float gap=400;
    float maxTubeTopoffSet;
    Random randomGenerator;
    float tubeVelocity=4,disttanceBetwenTubes;
    int numberOfTube=4;
    float tubeX[]=new float[numberOfTube];
    float[] tubeoffSet=new float[numberOfTube];
    Circle birdCircle;
   // ShapeRenderer shapeRenderer;
    Rectangle[] topTubeRectangles;
    Rectangle[] bottomTubeRectangules;
    int score=0;
    int scoringTube=0;
    BitmapFont font;
    Texture gameOver;

	@Override
	public void create () {
		batch = new SpriteBatch();
        background=new Texture("bg.png");
        birds=new Texture[2];
        birds[0]=new Texture("bird.png");
        birds[1]=new Texture("bird2.png");
        topTube=new Texture("toptube.png");
        bottomTube=new Texture("bottomtube.png");
        birdCircle=new Circle();
        font=new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(10);
        gameOver=new Texture("gameovers.png");
       // shapeRenderer=new ShapeRenderer();
        maxTubeTopoffSet=Gdx.graphics.getHeight()/2 - gap/2 -100;
        randomGenerator=new Random();
        disttanceBetwenTubes=Gdx.graphics.getWidth() *3/4;
        topTubeRectangles=new Rectangle[numberOfTube];
        bottomTubeRectangules =new Rectangle[numberOfTube];
         startGame();
    }

    public void startGame(){
        birdY =Gdx.graphics.getHeight()/2 - birds[0].getHeight()/2;

        for (int i=0; i< numberOfTube; i++){
            tubeoffSet[i] =(randomGenerator.nextFloat()- 0.5f)* (Gdx.graphics.getHeight() - gap -200);
            tubeX[i]=Gdx.graphics.getWidth()/2 - topTube.getWidth()/2 + Gdx.graphics.getWidth()+ i*disttanceBetwenTubes;
            topTubeRectangles[i]= new Rectangle();
            bottomTubeRectangules[i]=new Rectangle();

        }
    }

	@Override
	public void render () {
        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        if (gameState == 1) {
            if (tubeX[scoringTube]< Gdx.graphics.getWidth()/2 ){
                score ++;
                Gdx.app.log("Score", String.valueOf(score));
                if (scoringTube < numberOfTube-1){
                    scoringTube ++;
                }else{
                    scoringTube=0;
                }

            }
            if (Gdx.input.justTouched()) {
                veolocity = -30;

            }
            for (int i=0; i< numberOfTube; i++) {
                if (tubeX[i] < -topTube.getWidth()){
                    tubeX[i] += numberOfTube * disttanceBetwenTubes;
                    tubeoffSet[i] =(randomGenerator.nextFloat()- 0.5f)* (Gdx.graphics.getHeight() - gap -200);
                }else{
                    tubeX[i] = tubeX[i] - tubeVelocity;

                }
                batch.draw(topTube, tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeoffSet[i]);
                batch.draw(bottomTube, tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeoffSet[i]);
                topTubeRectangles[i]=new Rectangle( tubeX[i],Gdx.graphics.getHeight() / 2 + gap / 2 + tubeoffSet[i],topTube.getWidth(),topTube.getHeight());
                bottomTubeRectangules[i]=new Rectangle( tubeX[i],Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight()+tubeoffSet[i],bottomTube.getWidth(),bottomTube.getHeight());

            }
            if (birdY > 0 ){
                veolocity = veolocity + gravity;
                birdY -= veolocity;
            }else{
                gameState=2;
            }

    }else if (gameState == 0){
            if (Gdx.input.justTouched()) {
                gameState=1;
            }
        }else if (gameState == 2){
           batch.draw(gameOver , Gdx.graphics.getWidth()/2 - gameOver.getWidth()/2 , Gdx.graphics.getHeight()/2 - gameOver.getHeight()/2);
            if (Gdx.input.justTouched()) {
                gameState=1;
                startGame();
                scoringTube=0;
                score=0;
                veolocity=0;
            }
        }
        if (flapState == 0) {
            flapState = 1;
        } else {
            flapState = 0;
        }


        batch.draw(birds[flapState], Gdx.graphics.getWidth() / 2 - birds[flapState].getWidth() / 2, birdY);
        font.draw(batch,String.valueOf(score),100,200);
        batch.end();
        birdCircle.set(Gdx.graphics.getWidth()/2 , birdY +birds[flapState].getWidth()/2 , birds[flapState].getWidth()/2);
       // shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
      //  shapeRenderer.setColor(Color.RED);
      //  shapeRenderer.circle(birdCircle.x,birdCircle.y,birdCircle.radius);

        for (int i=0; i< numberOfTube; i++){
          //  shapeRenderer.rect(tubeX[i],Gdx.graphics.getHeight() / 2 + gap / 2 + tubeoffSet[i],topTube.getWidth(),topTube.getHeight());
         //   shapeRenderer.rect( tubeX[i],Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight()+tubeoffSet[i],bottomTube.getWidth(),bottomTube.getHeight());
       if (Intersector.overlaps(birdCircle,topTubeRectangles[i]) || Intersector.overlaps(birdCircle,bottomTubeRectangules[i])){
          // Gdx.app.log("Collision","Yes !");
           gameState=2;
       }
        }

       // shapeRenderer.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
        background.dispose();
	}
}
