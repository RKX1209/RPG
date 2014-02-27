import java.awt.*;

/* ステータスボックスクラス */
public class StatusWindow extends Window{

    public static int row_width = 5 * MessageString.FONT_WIDTH;//一選択肢の幅
    
    /* 選択肢間の間隔 */
    public static int INTERVAL_Y = 10;
    public static int INTERVAL_X = 5;

    /* 選択肢周りの間隔 */
    public static int AROUND_Y = 5 * Window.EDGE_SIZE;
    public static int AROUND_X = 3 * Window.EDGE_SIZE;

    private String[][] status_list;//ステータスリスト

    private State status;

    public StatusWindow(String title,int x,int y,String[][] status_s){
	super(title,x,y,0,0);
	this.status_list = status_s;
	WIDTH = status_list[0].length  * (row_width + INTERVAL_X) + AROUND_X - INTERVAL_X;
	HEIGHT = status_list.length * (MessageString.FONT_HEIGHT + INTERVAL_Y) + 2 * AROUND_Y - INTERVAL_Y;

	message.set_Color(MessageString.COLOR.WHITE);

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
	    int mx = posX + AROUND_X;	
	    /* ステータスの描画 */
	    for(int y = 0; y < status_list.length; y++){
		int my = posY + AROUND_Y + (MessageString.FONT_HEIGHT + INTERVAL_Y) * y;

	    
		message.drawMessage(g,
				    mx,
				    my,
				    status_list[y][0]);

		int sy = my;
		int sx = posX + this.WIDTH - status_list[y][1].length() * MessageString.FONT_WIDTH - 2 * Window.EDGE_SIZE;
		message.drawMessage(g,
				    sx,
				    sy,
				    status_list[y][1]);
	    }
	}
    }

    public void set_status_list(String[][] status_list){
	this.status_list = status_list;
    }
}
