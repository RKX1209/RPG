import java.awt.*;

/* メッセージボックスなどのウィンドウクラス */
public class Window{

    public static final int EDGE_SIZE = 4;//ウィンドウの外枠と内枠の間の長さ

    protected String title;//ウィンドウのタイトル

    protected static MessageString message;//メッセージを表示するためのクラス

    protected boolean active;//ウィンドウが表示されているか否か
    
    /* ウィンドウの大きさ */
    protected int WIDTH;
    protected int HEIGHT;

    /* ウィンドウの位置 */
    protected int posX;
    protected int posY;

    
    public Window(String title,int x,int y,int w,int h){
	this.title = title;
	this.posX = x;
	this.posY = y;
	this.WIDTH = w;
	this.HEIGHT = h;
	if(message == null)
	    message = new MessageString(MessageString.COLOR.WHITE);//メッセージ文字列
	active = false;
    }
    
    /* ウィンドウの更新 */
    public void update(){
	if(active){
	}
    }

    /* ウィンドウの描画 */
    public void draw(Graphics g){
	if(active){
	    /* ウィンドウ背景の描画 */
	    int in_X = posX + EDGE_SIZE;
	    int in_Y = posY + EDGE_SIZE;
	
	    int in_WIDTH = WIDTH - 2 * EDGE_SIZE;
	    int in_HEIGHT = HEIGHT - 2 * EDGE_SIZE;

	    /* 外枠の描画 */
	    g.setColor(Color.WHITE);
	    g.fillRect(posX,posY,WIDTH,HEIGHT);

	    /* 内枠の描画 */
	    g.setColor(Color.BLACK);
	    g.fillRect(in_X,in_Y,in_WIDTH,in_HEIGHT);

	    /* タイトルの描画 */
	    if(title != ""){
		int title_width = title.length() * MessageString.FONT_WIDTH;
		int title_height = MessageString.FONT_HEIGHT;
		int sx = posX + WIDTH / 2 - title_width / 2;
		int sy = posY;
		g.setColor(Color.BLACK);
		g.fillRect(sx,sy,title_width,title_height);//タイトルの裏は塗りつぶし
		message.drawMessage(g,
				    sx,
				    sy - 6,
				    title);
	    }
	}
	
    }

    /* WIDTHのゲッタ(getter) */
    public int get_WIDTH(){
	return WIDTH;
    }

    /* WIDTHのセッタ(setter) */
    public void set_WIDTH(int WIDTH){
	this.WIDTH = WIDTH;
    }

    /* HEIGHTのゲッタ(getter) */
    public int get_HEIGHT(){
	return HEIGHT;
    }

    /* HEIGHTのセッタ(setter) */
    public void set_HEIGHT(int HEIGHT){
	this.HEIGHT = HEIGHT;
    }

    /* ウィンドウを開く */
    public void open(){
	active = true;
    }

    /* ウィンドウを閉じる */
    public void close(){
	active = false;
    }

    /* ウィンドウを開閉する */
    public void toggle(){
	active = !active;
    }
}
