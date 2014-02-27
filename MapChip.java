import java.awt.*;

/* マップチップ用のクラス */
public class MapChip{
    /* チップの種類 */
    public static enum CHIP_TYPE{ CHIP_TYLE,
				  CHIP_BLOCK,
				  CHIP_DOWN_STAIR,
				  CHIP_UP_STAIR,
				  CHIP_SAND,
				  CHIP_GROVE,
				  CHIP_WATER,
				  CHIP_CASTLE,
				  CHIP_TOWER,
				  CHIP_SAVE,
				  CHIP_NORMAL_TYLE,
				  CHIP_BOSS};

    /* 各チップに対応する画像の名前 */
    public static final String[] CHIP_IMAGE= { "tyle.png",
					       "block.png",
					       "downstair.png",
					       "upstair.png",
					       "sand.png",
					       "grove.png",
					       "water.png",
					       "castle.png",
					       "tower.png",
					       "save.png",
					       "ntyle.png",
					       "bossblock.png"};
    
    public static final int CHIP_SIZE = 32;//1チップの大きさ
    
    private Resource resource;//マップチップの画像

    private CHIP_TYPE chip_type;//マップチップの種類

    private boolean collision;//キャラが衝突するかどうか

    /* チップの位置(チップ単位で) */
    private int posX;
    private int posY;

    public MapChip(CHIP_TYPE type,int posX,int posY,boolean coll){
	this.chip_type = type;
	this.collision = coll;
	this.posX = posX;
	this.posY = posY;
	String image_name = CHIP_IMAGE[type.ordinal()];//読み込む画像の名前
	resource = new Resource(image_name);//画像読み込み	
    }

    public void draw(int offsetX,int offsetY,Graphics g){	
	Image image = resource.get_Image();
	int x = posX + offsetX;
	int y = posY + offsetY;
	
	g.drawImage(image,x,y,null);
    }

    boolean get_collision(){
	return collision;
    }
}
