import java.awt.*;

/* ステージ移動イベント */
public class MoveEvent extends Event{
    private String next_stage;//次のステージの名前
    
    public MoveEvent(String next_stage){
	super();
	this.next_stage = next_stage;
    }

    /* イベント開始 */
    public void start(){
	super.start();
    }

    /* イベント終了 */
    public void end(){
	super.end();
    }

    /* ステージ移動イベントの更新 */
    public void update(){
	if(happen){
	}
    }
    
    /* ステージ移動イベントにおける描画 */
    public void draw(Graphics g){
	if(happen){
	}
    }

    /* next_stageのゲッタ(getter)*/
    public String get_next_stage(){
	return next_stage;
    }
}
