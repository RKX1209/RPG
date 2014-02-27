import java.awt.*;

/* イベント用クラス */
public abstract class Event{

    /* イベントが発生する場所 */
    protected int posX;
    protected int posY;
    
    protected boolean happen;//イベントが進行中か否か


    public Event(){
	happen = false;

	/* 会話表示ウィンドウを一度だけ初期化 */

    }

    /* イベント開始 */
    public void start(){
	happen = true;
    }    

    /* イベント終了 */
    public void end(){
	happen = false;	
    }    

    /* イベントの更新 */
    public abstract void update();

    /* イベントにおける描画 */
    public abstract void draw(Graphics g);

    /* happenのゲッタ(getter) */
    public boolean get_happen(){
	return happen;
    }
}
