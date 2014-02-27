import java.awt.*;
import java.util.*;

/* メッセージボックス */
public class MessageWindow extends Window{
    private static final long Char_Interval = 100L;//文字を表示する間隔    

    private String[] contents;//ボックスに表示する内容

    private boolean printing_message;//メッセージ表示中

    private int conversation_index;//現在ボックスに表示されている内容の番号

    private boolean init;

    /* メッセージを流すためのTimerTask */
    private Timer timer;
    private DrawingMessageTask task;
    
    public MessageWindow(String title,int x,int y,int w,int h){
	super(title,x,y,w,h);
	message.set_Color(MessageString.COLOR.WHITE);
	conversation_index = 0;
	task = null;
	timer = new Timer();	
	init = true;
	printing_message = false;
    }

    /* 会話を進める(会話が終了したらfalseを返す) */
    public boolean next(){
	conversation_index++;
	/* 会話終了 */
	if(conversation_index >= contents.length){
	    conversation_index = 0;
	    init = true;
	    task = null;
	    return false;
	}
	printing_message = true;
	task = null;	
	task = new DrawingMessageTask();
	timer.schedule(task, 0L, Char_Interval);
	return true;
    }
    
    /* メッセージボックスの更新 */
    public void update(){
	if(active){
	    if(init){//1回目の呼び出しなら
		task = new DrawingMessageTask();
		timer.schedule(task, 0L, Char_Interval);
		init = false;
		printing_message = true;
	    }
	    super.update();
	    /* 一つのメッセージを表示し終えたら */
	    boolean end = task.get_string_index() >= this.contents[conversation_index].length() - 1;
	    if(end){
		printing_message = false;
		task.cancel();//スレッド停止
	    }
	}
    }
    
    /* メッセージボックスの描画 */
    public void draw(Graphics g){
	super.draw(g);//ウィンドウの描画
	if(active){
	    /* メッセージの描画 */
	    if(task != null){
		String mes_value = this.contents[conversation_index];
	    	 
		String mes = mes_value.substring(0,task.get_string_index() + 1);
		// if(task.get_string_index() < mes_value.length() - 1)
		// 	System.out.print(task.get_string_index()+",");

		int mx = posX + Window.EDGE_SIZE * 3;
		int my = posY + Window.EDGE_SIZE * 5;
		int line_chars = (WIDTH - 4 * Window.EDGE_SIZE) / MessageString.FONT_WIDTH;
		int line_num = mes.length() / line_chars + (mes.length() % line_chars == 0 ? 0 : 1);
		int l = 0; int r = line_chars;
		/* 現在の番号のメッセージを表示する */
		for(int i = 0; i < line_num; i++){
		    message.drawMessage(g,
					mx,
					my + MessageString.FONT_HEIGHT * i,
					mes.substring(l,Math.min(r,mes.length())));
		    l = r;
		    r = l + line_chars;
		}
	    
	    }
	}
    }

    /* conversation_indexのゲッタ(getter) */
    public int get_conversation_index(){
	return conversation_index;
    }

    /* printing_message_indexのゲッタ(getter) */
    public boolean get_printing_message(){
	return printing_message;
    }

    /* contentsのゲッタ(getter) */
    public String[] get_contents(){
	
	return contents;
    }

    /* contentsのセッタ(setter) */
    public void set_contents(String[] contents){
	task = null;
	init = true;
	conversation_index = 0;
	printing_message = false;
	this.contents = contents;
    }
    
    /* メッセージを1文字ずつ順に描画するタスク */
    private class DrawingMessageTask extends TimerTask {
    
	private int string_index;//現在のメッセージの表示インデックス
    
	public DrawingMessageTask(){
	    super();
	    string_index = 0;
	}
    
	public void run() {
	    string_index++;
	}

	public int get_string_index(){
	    return string_index;
	}
    }
    
}

