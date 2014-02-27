import java.awt.*;
import java.util.*;

/* 敵クラス */
public class Enemy extends Object{    

    /* ステータス */
    public State state;

    public int level;
    
    public Enemy(String filename,int anime_len,int posX,int posY,State state,int level){
	super(filename,anime_len,posX,posY);
	this.state = state;
	this.level = level;
    }
    
    /* 敵の更新 */
    public void update(){
    }

    /* 敵の描画 */
    public void draw(int offsetX,int offsetY,Graphics g){
	Image image = resource.get_Image();
	g.drawImage(image,posX,posY,null);
    }

}
