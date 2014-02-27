import java.awt.*;

/* 人間クラス */
public abstract class Person extends Object{    
    
    public Person(String filename,int anime_len,int posX,int posY){
	super(filename,anime_len,posX,posY);
    }
    
    /* 人間の更新 */
    public abstract void update();

    /* 人間の描画 */
    public abstract void draw(int s_x,int s_y,Graphics g);

}
