import java.awt.*;

public class NormalPeople extends Person{
    public NormalPeople(String filename,int anime_len,int posX,int posY){
	super(filename,anime_len,posX,posY);
    }
    public void update(){
	/* 現在のアニメーション番号取得 */
	this.animation_now = Animation.getInstance().getNowCount();
	this.animation_now = this.animation_now % this.animation_length;	
    }

    public void draw(int s_x,int s_y,Graphics g){
	Image image = resource.get_Image();
	
	int chip_size = MapChip.CHIP_SIZE;//1チップの大きさ
	
		

	int draw_x = posX * chip_size  + s_x;
	int draw_y = posY * chip_size  + s_y;
	
	g.drawImage(image,
		    draw_x,
		    draw_y,
		    draw_x + chip_size,
		    draw_y + chip_size,
		    animation_now * chip_size,
		    now_dir.ordinal() * chip_size,
		    animation_now * chip_size + chip_size,
		    now_dir.ordinal() * chip_size + chip_size,
		    null);

    }
}
