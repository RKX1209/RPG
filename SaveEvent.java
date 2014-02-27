import java.awt.*;
import java.io.*;
import java.util.*;

/* 会話イベント */
public class SaveEvent extends Event{

    private String[] conversations;//会話の内容

    private MessageWindow mes_window;//会話表示ウィンドウ

    private SelectWindow decide_window;//はい、いいえウィンドウ
    public SaveEvent(String[] conversations){
	super();
	
	this.conversations = conversations;
	this.mes_window = Player.mes_window;
	this.decide_window = Player.decide_window;
    }

    /* イベント開始 */
    public void start(){
	super.start();
	System.out.println(this.mes_window);
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

    /* データをセーブする */
    public void save(Player player){
	try{
	    File file = new File("data/personal.txt");

	    if (checkBeforeWritefile(file)){
		PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));

		int[] datas = {player.status.LEVEL,player.status.HP,player.status.MP,
			       player.status.GOLD,player.status.E,player.status.POWER,
			       player.status.SPEED,player.status.MAX_HP,player.status.MAX_MP,
			       player.status.ATTACK,player.status.DEFFENCE,
			       player.get_posX(),player.get_posY()};
    
		for(int i = 0; i < datas.length; i++) pw.println(datas[i]);
		pw.println(player.get_now_stage());
		pw.close();
		System.out.println("Save: complete" + new Point(player.get_posX(),player.get_posY()));
	    }else{
		System.out.println("セーブに失敗しました");
	    }
	}catch(IOException e){
	    System.out.println(e);
	}
    }

    private static boolean checkBeforeWritefile(File file){
	if (file.exists()){
	    if (file.isFile() && file.canWrite()){
		return true;
	    }
	}

	return false;
    }    
}
