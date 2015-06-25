import java.awt.*;
import java.awt.event.*;

public class Game implements KeyListener{

    /* < ゲームの状態 >
       ゲーム画面
       ロード画面
       ゲームクリアー画面
       ゲームオーバー画面(画面自体は無い)
       スタッフロール中
    */
    public static enum STATE{ STATE_GAME,
			      STATE_NOW_LOADING,
			      STATE_GAME_CLEAR,
			      STATE_GAME_OVER,
			      STATE_STAFF_ROLL};


    public static SoundManager sound_manager;
    
    private STATE now_state;//現在の状態
    
    private Map map;//現在のステージ

    private EventManager event_manager;//現在のステージのイベントを管理

    public static EnemyManager enemy_manager;//敵データを管理
    
    public static EffectManager effect_manager;//エフェクトデータを管理

    private Player player;//プレイヤーへの参照

    private boolean bootup;//初回起動時

    private StaffRoll staff_roll;//スタッフロール

    private MyServer server;
    
    public Game(){
	bootup = true;//起動
	sound_manager = new SoundManager();
	now_state = STATE.STATE_NOW_LOADING;
	
	event_manager = new EventManager();//イベントデータ管理クラス初期化
	enemy_manager = new EnemyManager();//敵データ管理クラス初期化
	effect_manager = new EffectManager();//エフェクトデータ管理クラス初期化
	
	player = new Player(event_manager,"Player.png",3,0,0);//プレイヤー初期化
	player.load();//セーブデータをロードする
	

	map = new Map(event_manager,player);//ステージ初期化

	Load(player.get_now_stage());
	bootup = false;

	staff_roll = new StaffRoll(this.player);

	server = new MyServer();
	server.start();
	System.out.println("Init Sever");
    }

    /* 各データをロード */
    public void Load(String stage_name){
	String map_name = stage_name + ".txt";
	String event_name = "event_" + stage_name + ".txt";
	map.loadFile(map_name,bootup);
	event_manager.loadEvents(event_name);
	now_state = STATE.STATE_GAME;
    }
    
    public void update(){
	final STATE state = this.now_state;
	switch(state){
	    
	case STATE_GAME:
	    //System.out.println(map.get_now_state() == Map.STATE.STATE_STOP);
	    if(map.get_now_state() == Map.STATE.STATE_STOP){

		this.now_state = STATE.STATE_NOW_LOADING;
	    }
	    else if(map.get_now_state() == Map.STATE.STATE_GAME_OVER){

		this.now_state = STATE.STATE_GAME_OVER;
	    }
	    else if(map.get_now_state() == Map.STATE.STATE_GAME_CLEAR){

		this.now_state = STATE.STATE_GAME_CLEAR;
	    }	    
	    
	    else{
		map.update();
		event_manager.update();
	    }
	    break;

	case STATE_NOW_LOADING:
	    Load(this.player.get_now_stage());//ロードする
	    map.set_now_state(Map.STATE.STATE_COMPLETE_LOADING);//ロード完了をMapクラスに伝える
	    break;

	case STATE_GAME_CLEAR://ゲームクリア
	    SaveEvent sa = new SaveEvent(null);
	    sa.save(this.player);
	    System.out.println("GameClear");
	    staff_roll.start();
	    this.now_state = STATE.STATE_STAFF_ROLL;
	    break;

	case STATE_STAFF_ROLL://スタッフロール
	    staff_roll.update();
	    if(!staff_roll.get_active()) this.now_state = STATE.STATE_GAME_OVER;
	    break;
	    
	case STATE_GAME_OVER://ゲームオーバー
	    bootup = true;
	    this.player.load();
	    Load(this.player.get_now_stage());//復帰する
	    // player.status.HP = player.status.MAX_HP;
	    // player.status.MP = player.status.MAX_MP;
	    map.set_now_state(Map.STATE.STATE_COMPLETE_LOADING);//ロード完了をMapクラスに伝える
	    bootup = false;
	    break;
	    
	}
    }
    
    public void draw(Graphics g){
	final STATE state = this.now_state;
	switch(state){
	    
	case STATE_GAME:
	
	    map.draw(g);//マップ(マップチップ,オブジェクト)の描画
	    event_manager.draw(g);
	    
	    break;

	case STATE_GAME_CLEAR://ゲームクリア
	    break;

	case STATE_STAFF_ROLL://スタッフロール
	    staff_roll.draw(g);
	    break;
	    
	case STATE_NOW_LOADING://ロード中	    
	    /* ロード画面は真っ暗 */
	    g.setColor(Color.BLACK);
	    g.fillRect(0,0,MainPanel.WIDTH,MainPanel.HEIGHT);
	    break;
	    
	default:
	    break;
	    
	}

    }
    
    /* キーボードのイベント処理 */
    public void keyPressed(KeyEvent e) {
	int keyCode = e.getKeyCode();
        //System.out.println("Press: " + e.getKeyText(e.getKeyCode()));
	
	KeyBoard mInstance = KeyBoard.getInstance();        
        switch (keyCode) {
	case KeyEvent.VK_UP :
	    mInstance.Press(KeyBoard.KEY_CODE.KEY_UP);
	    break;
	case KeyEvent.VK_DOWN :
	    mInstance.Press(KeyBoard.KEY_CODE.KEY_DOWN);
	    break;
	case KeyEvent.VK_RIGHT :
	    mInstance.Press(KeyBoard.KEY_CODE.KEY_RIGHT);
	    break;
	case KeyEvent.VK_LEFT :
	    mInstance.Press(KeyBoard.KEY_CODE.KEY_LEFT);
	    break;
	case KeyEvent.VK_Z :
	    mInstance.Press(KeyBoard.KEY_CODE.KEY_Z);
	    break;
	case KeyEvent.VK_X :
	    mInstance.Press(KeyBoard.KEY_CODE.KEY_X);
	    break;
	case KeyEvent.VK_C :
	    mInstance.Press(KeyBoard.KEY_CODE.KEY_C);
	    break;
	case KeyEvent.VK_SPACE :
	    mInstance.Press(KeyBoard.KEY_CODE.KEY_SPACE);
	    break;
	default:
	    break;
	}	    
    }
    
    public void keyReleased(KeyEvent e) {
	int keyCode = e.getKeyCode();
        //System.out.println("Release: " + e.getKeyText(e.getKeyCode()));
	KeyBoard mInstance = KeyBoard.getInstance();
	
        switch (keyCode) {
	case KeyEvent.VK_UP :
	    mInstance.Release(KeyBoard.KEY_CODE.KEY_UP);
	    break;
	case KeyEvent.VK_DOWN :
	    mInstance.Release(KeyBoard.KEY_CODE.KEY_DOWN);
	    break;
	case KeyEvent.VK_RIGHT :
	    mInstance.Release(KeyBoard.KEY_CODE.KEY_RIGHT);
	    break;
	case KeyEvent.VK_LEFT :
	    mInstance.Release(KeyBoard.KEY_CODE.KEY_LEFT);
	    break;
	case KeyEvent.VK_Z :
	    mInstance.Release(KeyBoard.KEY_CODE.KEY_Z);
	    break;
	case KeyEvent.VK_X :
	    mInstance.Release(KeyBoard.KEY_CODE.KEY_X);
	    break;
	case KeyEvent.VK_C :
	    mInstance.Release(KeyBoard.KEY_CODE.KEY_C);
	    break;
	case KeyEvent.VK_SPACE :
	    mInstance.Release(KeyBoard.KEY_CODE.KEY_SPACE);
	    break;
	default:
	    break;
	}	    
	
    }
    
    public void keyTyped(KeyEvent e) {
	//System.out.println("Type: " + e.getKeyText(e.getKeyCode()));
    }

}
