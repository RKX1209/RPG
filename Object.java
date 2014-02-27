import java.awt.*;

/* オブジェクトのクラス
   マップ上にあるオブジェクトはこのクラスを継承する */
public abstract class Object{

    /* オブジェクトの方向を表す */
    public static enum DIRECTION{ DOWN,
				  LEFT,
				  RIGHT,
				  UP};

    public static final int[][] DirectionVector = { {0,1},
						    {-1,0},
						    {1,0},
    						    {0,-1}};

    protected DIRECTION now_dir;//現在向いている方向
    
    protected Resource resource;//データ

    protected int animation_now;//現在のアニメーション番号
    
    protected int animation_length;//アニメーションの長さ
    
    /* オブジェクトの位置 */
    protected int posX;
    protected int posY;
    
    public Object(String filename,int anime_len,int posX,int posY){
	resource = new Resource(filename);//リソースの初期化
	animation_length = anime_len;
	this.posX = posX;
	this.posY = posY;
	now_dir = DIRECTION.DOWN;
    }

    /* オブジェクトの更新 */
    public abstract void update();

    /* オブジェクトの描画 */
    public abstract void draw(int offsetX,int offsetY,Graphics g);    

    /* now_dirのゲッタ(getter) */
    public DIRECTION get_now_dir(){
	return now_dir;
    }

    /* now_dirのセッタ(setter) */
    public void set_now_dir(DIRECTION now_dir){
	this.now_dir = now_dir;
    }
    
    /* posXのゲッタ(getter) */
    public int get_posX(){
	return posX;
    }

    /* posYのゲッタ(getter) */
    public int get_posY(){
	return posY;
    }

}
