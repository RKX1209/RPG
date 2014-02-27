import java.awt.*;
import java.util.*;
import java.io.*;

/* スタッフロール */
public class StaffRoll{

    /* 大きさ */
    public static int WIDTH = MainPanel.WIDTH;
    public static int HEIGHT = MainPanel.HEIGHT;

    /* スタッフロールの早さ */
    public static int SPEED = 10;

    private int CHAR_SIZE = 15;

    /* 左上の座標 */    
    public static int SX = (MainPanel.WIDTH - WIDTH) / 2;
    public static int SY = (MainPanel.HEIGHT - HEIGHT) / 2;

    /* 一番上の座標 */
    private int posX;
    private int posY;
    
    private ArrayList<String> credit;//スタッフロールの内容

    private boolean active;

    private Player player;
    
    public StaffRoll(Player player){
	credit = new ArrayList<String>();
	this.active = false;
	this.posX = SX;
	this.posY = SY + HEIGHT;//下から開始
	loadFile("credit.txt");
	this.player = player;
    }

    /* スタッフロール読み込み */
    public void loadFile(String filename){
	String path = "data/" + filename;
	try {
	    File file = new File(path);
	    BufferedReader in = new BufferedReader(new FileReader(file));
	    String line;
	    while((line = in.readLine()) != null) credit.add(line);	    
	}catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch(IOException e){
	    System.out.println(e);
	}
    }

    /* スタッフロール開始 */
    public void start(){
	this.active = true;
	this.posX = SX;
	this.posY = SY + HEIGHT;//下から開始
	Game.sound_manager.playBGM("ending");
	System.out.println("start");
    }

    public void end(){
	this.active = false;
	System.out.println("end");
    }
    public void update(){
	if(active){
	    player.update();
	    this.posY -= SPEED;
	}
    }
    
    public void draw(Graphics g){
	if(active){
	    g.setColor(Color.WHITE);
	    g.fillRect(0,0,MainPanel.WIDTH,MainPanel.HEIGHT);
	    g.setColor(Color.BLACK);
	    if(this.posY <= -CHAR_SIZE * credit.size()) this.end();
	    Font f = new Font("TimesRoman",Font.ITALIC,CHAR_SIZE);
	    g.setFont(f);
	    for(int i = 0; i < credit.size(); i++)
		g.drawString(credit.get(i),posX,posY + CHAR_SIZE * i);
	    player.set_posX((Map.DISPLAY_ROW - 1) / 4 * 3);
	    player.set_posY(Map.DISPLAY_COL / 2);
	    player.set_now_dir(Object.DIRECTION.LEFT);
	    player.draw(0,0,g);
	}
    }

    public boolean get_active(){
	return active;
    }
}
