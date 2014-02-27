import java.awt.*;

/* 会話イベント */
public class TalkEvent extends Event{

    private String[] conversations;//会話の内容

    private MessageWindow mes_window;//会話表示ウィンドウ
    
    public TalkEvent(String[] conversations){
	super();
	
	this.conversations = conversations;
	mes_window = Player.mes_window;	
    }

    /* イベント開始 */
    public void start(){
	super.start();
	mes_window.set_contents(this.conversations);//会話データセット
	mes_window.open();
    }

    /* イベント終了 */
    public void end(){
	super.end();
	mes_window.close();
    }

    /* 会話イベントの更新 */
    public void update(){
	if(happen){
	    mes_window.update();
	}
    }

    /* 会話を進める */
    public boolean next(){
	if(!mes_window.get_printing_message()){//メッセージが表示し終わっていたら
	    boolean hasNext = mes_window.next(); //会話を進める
		
	    if(!hasNext){//会話が終わっていたら
		this.end();//イベントは終了
		return false;
	    }
	}	
	return true;
    }
    
    /* 会話イベントにおける描画 */
    public void draw(Graphics g){
	if(happen){
	    mes_window.draw(g);
	}
    }

}
