import java.awt.*;
import java.util.*;

/* 選択ボックスクラス */
public class SelectWindow extends Window{

    public static int row_width = 5 * MessageString.FONT_WIDTH;//一選択肢の幅
    
    /* 選択肢間の間隔 */
    public static int INTERVAL_Y = 10;
    public static int INTERVAL_X = 10;

    /* 選択肢周りの間隔 */
    public static int AROUND_Y = 5 * Window.EDGE_SIZE;
    public static int AROUND_X = 7 * Window.EDGE_SIZE;

    private Point select_point;//選択した項目
    
    private String[][] select_list;//選択リスト


    private int row_widths[];//リストの各列の幅

    public SelectWindow(String title,int x,int y,String[][] select_list){
	super(title,x,y,0,0);
	
	WIDTH = select_list[0].length  * (row_width + INTERVAL_X) - INTERVAL_X + 2 * AROUND_X;
	HEIGHT = select_list.length * (MessageString.FONT_HEIGHT + INTERVAL_Y) - INTERVAL_Y + 2 * AROUND_Y;
	
	message.set_Color(MessageString.COLOR.WHITE);
	this.select_list = select_list;
	select_point = new Point(0,0);
    }

    
    /* ウィンドウの更新 */
    public void update(){
	super.update();
	if(active){
	}
    }

    /* ウィンドウの描画 */
    public void draw(Graphics g){
	super.draw(g); //ウィンドウ描画
	
	if(active){
	    /* 選択肢リストの描画 */
	    for(int y = 0; y < select_list.length; y++){
		int my = posY + AROUND_Y + (MessageString.FONT_HEIGHT + INTERVAL_Y) * y;
		for(int x = 0; x < select_list[y].length; x++){
		    int mx = posX + AROUND_X + (row_width + INTERVAL_X) * x;
		    message.drawMessage(g,
					mx,
					my,
					select_list[y][x]);
		}
	    }

	    /* カーソルの描画 */
	    int cx = posX + AROUND_X + (row_width + INTERVAL_X) * select_point.x - MessageString.FONT_WIDTH;
	    int cy = posY + AROUND_Y + (MessageString.FONT_HEIGHT + INTERVAL_Y) * select_point.y + MessageString.FONT_WIDTH / 2;
	    int[] xPoints = {cx,cx + MessageString.FONT_WIDTH / 2,cx};
	    int[] yPoints = {cy,cy + MessageString.FONT_HEIGHT / 4,cy + MessageString.FONT_HEIGHT / 2};
	    g.setColor(Color.WHITE);
	    g.fillPolygon(xPoints, yPoints, 3);
	    
	}
    }

    /* カーソル移動 */
    public void move_cursor(Object.DIRECTION dir){
	int nx = select_point.x + Object.DirectionVector[dir.ordinal()][0];
	int ny = select_point.y + Object.DirectionVector[dir.ordinal()][1];

	if(inner(nx,ny)){
	    select_point.x = nx;
	    select_point.y = ny;
	}
    }
    
    public boolean inner(int x,int y){
	return 0 <= x && x < select_list[0].length && 0 <= y && y < select_list.length;
    }

    /* select_pointのゲッタ(getter) */
    public Point get_select_point(){
	return select_point;
    }

    /* select_pointのセッタ(setter) */
    public void set_select_point(Point select_point){
	this.select_point = select_point;
    }

    /* select_listのゲッタ(getter) */
    public String get_selected_list(Point p){
	return select_list[p.y][p.x];
    }
}
