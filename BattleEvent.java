import java.util.*;
import java.awt.*;

/* 会話イベント */
public class BattleEvent extends Event{

    public static enum STATE{ STATE_PLAYER_TURN,
			      STATE_ENEMY_TURN,			      
			      STATE_ATTACKING,
			      STATE_DAMAGED,
			      STATE_NONE}
    

    private STATE now_state;//現在の状態
    
    private String[] conversations;//会話の内容

    public static MessageWindow mes_window;//会話表示ウィンドウ

    public static StatusWindow status_window;//ステータスウィンドウ

    public static SelectWindow command_window;//コマンドウィンドウ

    private EffectManager effect_manager;//エフェクト管理クラスへの参照

    private Enemy enemy;//戦闘する敵

    private Effect attack_effect;//攻撃エフェクト

    private Player player;//プレイヤーへの参照
    
    private int enemy_damage;

    public boolean boss;
    
    public BattleEvent(Enemy enemy,boolean boss){
	super();
	
	this.conversations = conversations;
	this.mes_window = Player.mes_window;
	this.status_window = Player.status_window;

	/* ステータスボックスの位置 */
	int sx = MainPanel.WIDTH / 8;
	int sy = MainPanel.HEIGHT / 6;
	
	String[][] select_s = {{"たたかう","じゅもん"},
			       {"にげる","どうぐ"} };
	if(command_window == null){
	    this.command_window = new SelectWindow("コマンド",
						   sx + Player.status_window.get_WIDTH() + 80,
						   sy - 30,
						   select_s);
	}

	this.effect_manager = Game.effect_manager;
	this.enemy = enemy;
	this.now_state = STATE.STATE_NONE;
	this.boss = boss;
    }

    /* イベント開始 */
    public void start(){
	super.start();
	String[] conv = {enemy.state.NAME + "があらわれた！"};
	mes_window.set_contents(conv);//会話データセット
	mes_window.open();
	status_window.open();
	command_window.open();
    }

    /* イベント終了 */
    public void end(){
	super.end();
	mes_window.close();
	status_window.close();
	command_window.close();
    }

    /* 会話イベントの更新 */
    public void update(){	
	if(happen){
	    /* ウィンドウ更新 */
	    mes_window.update();
	    command_window.update();

	    /* 敵とエフェクト更新 */	    
	    enemy.update();
	    effect_manager.update();

	    /* 状態遷移 */
	    final STATE st = this.now_state;
	    if(this.mes_window.get_printing_message()) return;//メッセージ表示中は戦闘を進めない
	    
	    switch(st){
		
	    case STATE_ATTACKING://プレイヤーが攻撃中
		if(!attack_effect.get_active()){
		    /* エフェクト終了 */
		    this.enemy.state.HP -= enemy_damage;//被弾
		    String[] conv = {enemy.state.NAME + "に" + String.valueOf(enemy_damage) + "ポイントのダメージをあたえた！"};
		    this.mes_window.set_contents(conv);
		    this.now_state = STATE.STATE_ENEMY_TURN;
		}
		break;

	    case STATE_PLAYER_TURN://プレイヤーのターン
		if(player != null) {
		    player.set_now_state(Player.STATE.STATE_BATTLE_EVENT);
		}
		break;
		
	    case STATE_ENEMY_TURN://敵のターン
		System.out.println(enemy.state.HP);
		if(this.enemy.state.HP <= 0){//勝利
		    String[] conv = {enemy.state.NAME + "をたおした！"};
		    this.enemy.state.HP = enemy.state.MAX_HP;
		    this.mes_window.set_contents(conv);		    
		    this.now_state = STATE.STATE_NONE;
		    this.enemy_damage = 0;
		    player.LEVEL_UP(enemy.level);
		    player.set_now_state(Player.STATE.STATE_BATTLE_WIN);
		}
		else{
		    /* 敵の攻撃 */
		    int player_damage = Math.max(this.enemy.state.ATTACK / 2 - player.status.DEFFENCE / 4,0);
		    String[] conv = {enemy.state.NAME + "のこうげき！" + player.status.NAME + "は" + player_damage + "ポイントのダメージをうけた！"};
		    this.mes_window.set_contents(conv);
		    
		    player.damaged(this.enemy,player_damage);
	
		    if(player.status.HP <= 0){
			this.now_state = STATE.STATE_NONE;
			this.enemy.state.HP = this.enemy.state.MAX_HP;
		    }
		    else this.now_state = STATE.STATE_PLAYER_TURN;
		}
		break;

	    default:
		break;
	    }
	    
	}
    }

    /* こうげき */
    public void Attack(Player player){
	String[] conv = {"ゆうしゃのこうげき！"};
	mes_window.set_contents(conv);//会話データセット

	/* ダメージ計算(http://nyusuke.com/game/dq/dq9damage.html) */
	int basic_damage = player.status.ATTACK / 2 - enemy.state.DEFFENCE / 4;//ダメージ基本値
	int damage_change = basic_damage / 16 + 1;//ダメージ幅
	
	Random rnd = new Random();
        int random_damage = rnd.nextInt(damage_change);
	
	this.enemy_damage = Math.max(basic_damage + random_damage,0);
	
	this.player = player;
	this.now_state = STATE.STATE_ATTACKING;
	this.attack_effect = effect_manager.get_effect("attack");//攻撃エフェクト
	
	attack_effect.start();

    }
    /* 会話イベントにおける描画 */
    public void draw(Graphics g){
	if(happen){
	    /* ウィンドウ描画 */
	    mes_window.draw(g);
	    status_window.draw(g);
	    command_window.draw(g);
	    /* 敵の描画 */
	    enemy.draw(0,0,g);
	    
	    final STATE st = this.now_state;
	    switch(st){
	    case STATE_ATTACKING:
		effect_manager.draw(g);
		break;
	    default:
		break;
	    }
	}
    }

}
