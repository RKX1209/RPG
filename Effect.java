import java.util.*;
import java.awt.*;

/* エフェクトクラス */
public class Effect extends Object{

    private boolean active;

    private int start;

    /* 1エフェクトの大きさ */
    private int WIDTH;
    private int HEIGHT;

    private int anime;
    
    public Effect(String filename,int anime_len,int posX,int posY,int width,int height){
	super(filename,anime_len,posX,posY);
	this.WIDTH = width;
	this.HEIGHT = height;
	active = false;
    }

    public void start(){
	active = true;
	this.start = Animation.getInstance().getNowCount();
	this.animation_now = 0;
	anime = 0;
    }

    public void stop(){
	this.active = false;
	this.animation_now = 0;
	anime = 0;
    }

    public void update(){
	if(active){
	    // this.animation_now = Animation.getInstance().getNowCount() - start;
	    // if(this.animation_now >= this.animation_length){
	    // 	this.stop();
	    // }
	    
	    anime++;
	    if(anime >= this.animation_length){
	    	this.stop();
	    }
	    
	}
    }
        
    public void draw(int offsetX,int offsetY,Graphics g){
	if(active){
	    Image image = resource.get_Image();
	    //	    int a_now = this.animation_now;
	    int a_now = this.anime;
	    g.drawImage(image,
			posX,posY,
			posX + WIDTH,posY + HEIGHT,
			a_now * WIDTH,0,
			a_now * WIDTH + WIDTH,HEIGHT,
			null);
	}
    }

    public boolean get_active(){
	return active;
    }
}
