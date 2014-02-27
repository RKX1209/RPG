import java.awt.*;
import java.util.*;
import java.io.*;

/* プレイヤー(主人公)クラス */
public class Player extends Person{

    public static final int Frame_Of_Move = 7;//移動に何フレーム使うか
    
    /* < プレイヤーの状態 >
       移動中
       待機状態(操作不能)....相手の攻撃中などに待機するため
       ステータスウィンドウ表示中
       つよさウィンドウ表示中
       じゅもんウィンドウ表示中
       どうぐウィンドウ表示中
       会話イベント発生中
       戦闘イベント発生中
       戦闘勝利
       戦闘敗北
       ステージ移動イベント発生中
       セーブイベント発生中
       ゲームクリア
     */
    public static enum STATE{ STATE_MOVE,
			      STATE_WAITING,
			      STATE_STATUS_WINDOW_OPEN,
			      STATE_STRENGTH_WINDOW_OPEN,
			      STATE_MAGIC_WINDOW_OPEN,
			      STATE_ITEM_WINDOW_OPEN,
			      STATE_TALK_EVENT,
			      STATE_BATTLE_EVENT,
			      STATE_BATTLE_WIN,
			      STATE_BATTLE_LOSE,
			      STATE_MOVE_STAGE_EVENT,
			      STATE_SAVE_EVENT,
			      STATE_CLEAR};

    /* ステータス */
    public State status;

    /* ウィンドウ */
    public static StatusWindow status_window;//ステータスウィンドウ
    public static SelectWindow select_window;//コマンドウィンドウ
    public static StatusWindow strength_window;//つよさウィンドウ
    public static MessageWindow mes_window;//メッセージウィンドウ
    public static SelectWindow decide_window;//はい、いいえウィンドウ

    
    private STATE now_state;//現在の状態

    private String now_stage;//現在のステージ名
    
    private int frame_count;//フレームカウント用変数

    private EventManager event_manager;//イベントを管理するクラス

    private Event now_event;//現在発生しているイベント

    private TalkEvent now_talk_event;//現在発生している会話イベント

    private BattleEvent now_battle_event;//現在発生している戦闘イベント
    
    private MoveEvent now_move_event;//現在発生しているステージ移動イベント
    
    private SaveEvent now_save_event;//現在発生しているセーブイベント

    private int walking;//歩数

    /* 最初の位置 */
    private int start_x;
    private int start_y;
    
    public Player(EventManager em,String filename,int anime_len,int posX,int posY){
	super(filename,anime_len,posX,posY);
	status = new State();
	this.start_x = (Map.DISPLAY_ROW - 1) / 2;
	this.start_y = (Map.DISPLAY_COL - 1) / 2;
	this.event_manager = em;
	frame_count = -1;
	
	now_state = STATE.STATE_MOVE;
	walking = 0;
    }

    /* プレイヤーの移動 */
    public void move(final DIRECTION dir){
		
	this.now_dir = dir;
	int dx = DirectionVector[dir.ordinal()][0];
	int dy = DirectionVector[dir.ordinal()][1];
	int nx = posX + dx;
	int ny = posY + dy;
	if(!Map.isInMap(nx,ny)) return;
	if(Map.isCollision(nx,ny)) return;	
	frame_count = 0;
	posX = nx;
	posY = ny;
	walking++;
	System.out.println(new Point(posX,posY));

    }
    
    /* プレイヤーの更新 */
    public void update(){
	
	/* キーボード処理 */
	KeyBoard mInstance = KeyBoard.getInstance();
	boolean up = mInstance.IsPressed(KeyBoard.KEY_CODE.KEY_UP,
					 KeyBoard.MODE.MODE_NORMAL);
	boolean down = mInstance.IsPressed(KeyBoard.KEY_CODE.KEY_DOWN,
					   KeyBoard.MODE.MODE_NORMAL);
	boolean right = mInstance.IsPressed(KeyBoard.KEY_CODE.KEY_RIGHT,
					    KeyBoard.MODE.MODE_NORMAL);
	boolean left = mInstance.IsPressed(KeyBoard.KEY_CODE.KEY_LEFT,
					   KeyBoard.MODE.MODE_NORMAL);

	/* トグル */
	boolean up_t = mInstance.IsPressed(KeyBoard.KEY_CODE.KEY_UP,
					   KeyBoard.MODE.MODE_TOGGLE);
	boolean down_t = mInstance.IsPressed(KeyBoard.KEY_CODE.KEY_DOWN,
					     KeyBoard.MODE.MODE_TOGGLE);
	boolean right_t = mInstance.IsPressed(KeyBoard.KEY_CODE.KEY_RIGHT,
					      KeyBoard.MODE.MODE_TOGGLE);
	boolean left_t = mInstance.IsPressed(KeyBoard.KEY_CODE.KEY_LEFT,
					     KeyBoard.MODE.MODE_TOGGLE);
	
	boolean z_t = mInstance.IsPressed(KeyBoard.KEY_CODE.KEY_Z,
					  KeyBoard.MODE.MODE_TOGGLE);
	boolean x_t = mInstance.IsPressed(KeyBoard.KEY_CODE.KEY_X,
					  KeyBoard.MODE.MODE_TOGGLE);
	
	boolean space_t = mInstance.IsPressed(KeyBoard.KEY_CODE.KEY_SPACE,
					      KeyBoard.MODE.MODE_TOGGLE);


	/* 現在のアニメーション番号取得 */
	this.animation_now = Animation.getInstance().getNowCount();
	this.animation_now = this.animation_now % this.animation_length;

	
	/* ウィンドウの更新 */
	update_windows();
	status_window.update();
	select_window.update();
	strength_window.update();

	
	
	/* 現在の状態によって遷移 */
	final STATE state = this.now_state;
	
	switch(state){
	    
	case STATE_MOVE://歩行中
	    Game.sound_manager.playBGM("normal");
	    if(0 <= frame_count && frame_count < Frame_Of_Move){//移動中なら
		frame_count++;
		return; 
	    }
	    frame_count = -1;

	    /* 戦闘イベント */
	    now_battle_event = event_manager.get_battle_event(this.walking);
	    
	    /* イベント */
	    if(z_t){
		boolean res = search();//しらべる
	    }
	    else if(up){
		move(DIRECTION.UP);//上に移動
	    }
	    else if(down){
		move(DIRECTION.DOWN);//下に移動
	    }
	    else if(left){
		move(DIRECTION.LEFT);//左に移動
	    }
	    else if(right){
		move(DIRECTION.RIGHT);//右に移動
	    }
	    else if(space_t){
	    /* ステータス画面表示 */
		open_window();
		this.now_state = STATE.STATE_STATUS_WINDOW_OPEN;//遷移
	    }

	    /* 自動で遭遇するイベント */
	    Event event_tmp = event_manager.get_event(new Point(posX,posY));	    
	    if(event_tmp != null){
		/* ステージ移動イベント */
		if(event_tmp instanceof MoveEvent){
		    /* 次のステージに移動 */
		    now_move_event = (MoveEvent)event_tmp;
		    String next_stage = now_move_event.get_next_stage();
		    this.move_stage(next_stage);
		}
	    }
	    
	    if(now_battle_event != null){
	    	/* 戦闘イベント開始 */
	    	this.walking = 0;
	    	now_battle_event.start();
	    	this.now_state = STATE.STATE_BATTLE_EVENT;
	    }
	    
	    break;
	    
	case STATE_STATUS_WINDOW_OPEN://ステータスバーを開いている
	    if(z_t){
		Point sp = select_window.get_select_point();//選択する
		final String sel_s = select_window.get_selected_list(sp);
		if(sel_s == "つよさ"){
		    strength_window.open();//つよさウィンドウをひらく
		    now_state = STATE.STATE_STRENGTH_WINDOW_OPEN;
		}
		else if(sel_s == "しらべる"){
		    boolean res = search();//しらべる
		    close_window();
		}		
	    }
	    else if(up_t){
		select_window.move_cursor(DIRECTION.UP);//カーソルを上に移動
	    }
	    else if(down_t){
		select_window.move_cursor(DIRECTION.DOWN);//カーソルを下に移動
	    }
	    else if(left_t){
		select_window.move_cursor(DIRECTION.LEFT);//カーソルを左に移動
	    }
	    else if(right_t){
		select_window.move_cursor(DIRECTION.RIGHT);//カーソルを右に移動
	    }
	    else if(x_t){//もどる
		close_window();
		now_state = STATE.STATE_MOVE;
	    }
	    break;

	case STATE_STRENGTH_WINDOW_OPEN://つよさウィンドウ表示中
	    if(x_t){
		strength_window.close();//とじる
		now_state = STATE.STATE_STATUS_WINDOW_OPEN;
		System.out.println("close");
	    }
	    break;
	    
	case STATE_TALK_EVENT://イベント中
	    /* イベントを進める */
	    if(z_t){
		
		boolean hasNext = (now_talk_event).next();
		if(!hasNext){//イベント終了
		    this.now_state = STATE.STATE_MOVE;
		}		
	    }
	    break;
	    
	case STATE_BATTLE_EVENT://戦闘イベント発生中
	    if(now_battle_event.boss)
		Game.sound_manager.playBGM("boss");
	    else
		Game.sound_manager.playBGM("battle");
	    
	    SelectWindow command_window = BattleEvent.command_window;
	    if(mes_window.get_printing_message()) break;
	    
	    if(z_t){
		Point sp = command_window.get_select_point();//コマンドを選択する
		final String sel_s = command_window.get_selected_list(sp);
		if(sel_s == "たたかう"){
		    this.now_battle_event.Attack(this);//攻撃
		    this.now_state = STATE.STATE_WAITING;
		    //相手の攻撃が終わるまでプレイヤーを待機させる
		}		
	    }
	    else if(up_t){
		command_window.move_cursor(DIRECTION.UP);//カーソルを上に移動
	    }
	    else if(down_t){
		command_window.move_cursor(DIRECTION.DOWN);//カーソルを下に移動
	    }
	    else if(left_t){
		command_window.move_cursor(DIRECTION.LEFT);//カーソルを左に移動
	    }
	    else if(right_t){
		command_window.move_cursor(DIRECTION.RIGHT);//カーソルを右に移動
	    }
	    
	    break;
	    
	case STATE_SAVE_EVENT://セーブイベント発生中
	    /* イベントを進める */
	    if(z_t){
		boolean hasNext = (now_save_event).next();
		if(!hasNext){//イベント終了
		    now_save_event.save(this);//セーブする
		    this.now_state = STATE.STATE_MOVE;
		}		
	    }
	    
	    break;

	case STATE_BATTLE_WIN://バトル勝利
	    if(now_battle_event.mes_window.get_printing_message()) break;
	    now_battle_event.end();
	    if(now_battle_event.boss){
		this.now_state = STATE.STATE_CLEAR;
	    }
	    else{
		this.now_state = STATE.STATE_MOVE;
	    }
	    break;

	case STATE_CLEAR://ゲームクリア
	    break;
	    
	case STATE_BATTLE_LOSE:// 敗北(ゲームオーバー)
	    break;
	    
	case STATE_MOVE_STAGE_EVENT://ステージ移動中
	    /* ステージ移動中は操作禁止 */
	    break;

	case STATE_WAITING:
	    /* 他クラスから指示があるまで待機 */
	    break;
	default:
	    break;
	}

	
	
    }

    /* プレイヤーの描画 */
    public void draw(int s_x,int s_y,Graphics g){
	if(this.now_state != STATE.STATE_BATTLE_EVENT &&
	   this.now_state != STATE.STATE_WAITING){
	    Image image = resource.get_Image();
	
	    int chip_size = MapChip.CHIP_SIZE;//1チップの大きさ
	
	    /* 移動アニメーションのためにオフセットを計算する */
	    double one_frame = (double)chip_size / (double)Frame_Of_Move;
	    double frame_offset = 0;
	
	    if(frame_count < 0) frame_offset = 0;
	    else frame_offset = one_frame * ((double)Frame_Of_Move - (double)frame_count - 1);
	
	    int dx = DirectionVector[now_dir.ordinal()][0];
	    int dy = DirectionVector[now_dir.ordinal()][1];


	    /* 描画する位置は、通常座標から1フレームごとに近づけていく(アニメーション) */
	    int draw_x = posX * chip_size - (int)frame_offset * dx + s_x;
	    int draw_y = posY * chip_size - (int)frame_offset * dy + s_y;
	
	    g.drawImage(image,
			draw_x,
			draw_y,
			draw_x + chip_size,
			draw_y + chip_size,
			animation_now * chip_size,
			now_dir.ordinal() * chip_size,
			animation_now * chip_size + chip_size,
			now_dir.ordinal() * chip_size + chip_size,
			null);
	
	/* ウィンドウの描画 */
	status_window.draw(g);
	select_window.draw(g);
	strength_window.draw(g);

	}
    }

    /* 「しらべる」 */
    public boolean search(){
	//Game.sound_manager.playSE("decide_beep");
	/* プレイヤーの向いている先にイベントがあるかしらべる */
	Point target = new Point(posX,posY);
	target.x += Object.DirectionVector[now_dir.ordinal()][0];
	target.y += Object.DirectionVector[now_dir.ordinal()][1];
	
	Event event = event_manager.get_event(target);

	/* イベントが見つかった */
	if(event != null){
	    
	    /* イベントの種類によってキャスト(静的型付けだからね。仕方ないね) */	    
	    if(event instanceof TalkEvent){//会話イベント
		now_talk_event = (TalkEvent)event;
		now_talk_event.start();//イベントスタート
		now_state =  STATE.STATE_TALK_EVENT;
	    }
	    else if(event instanceof SaveEvent){//セーブイベント
		now_save_event = (SaveEvent)event;
		now_save_event.start();//イベントスタート
		now_state =  STATE.STATE_SAVE_EVENT;
	    }

	    else if(event instanceof BattleEvent){//セーブイベント
		this.now_battle_event = (BattleEvent)event;
		this.now_battle_event.start();//イベントスタート		
		this.now_state =  STATE.STATE_BATTLE_EVENT;
	    }
	    
	    return true;
	}
	else{
	    /* イベントが見つからなかった場合は"失敗イベント"を発生させる */
	    now_talk_event = (TalkEvent)(event_manager.get_event(new Point(-1,-1)));
	    now_talk_event.start();//イベントスタート
	    now_state =  STATE.STATE_TALK_EVENT;
	    return false;
	}

    }

    /* 次のステージに移動する */
    public void move_stage(String next_stage){
	this.now_stage = next_stage;
	this.now_state = STATE.STATE_MOVE_STAGE_EVENT;
    }
    
    /* ステータスウィンドウを開く */
    public void open_window(){
	status_window.open();
	select_window.open();	
    }

    /* ステータスウィンドウを閉じる */
    public void close_window(){
	status_window.close();
	select_window.close();	
    }

    public void load(){
	try {
            BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream("data/personal.txt"), "Shift_JIS"));
	    
	    ArrayList<String> array = new ArrayList<String>();
	    ArrayList<Integer> array_i = new ArrayList<Integer>();
	    String line;
	    
	    while((line = br.readLine()) != null) array.add(line);
	    for(int i = 0; i < array.size(); i++) System.out.println(array.get(i));
	    for(int i = 0; i < array.size() - 1; i++) array_i.add(Integer.parseInt(array.get(i)));
	    
	    status.SetData(array_i);//読み込んだ値をセットする
	    this.posX = array_i.get(array.size() - 3);
	    this.posY = array_i.get(array.size() - 2);
	    System.out.println("Load" + new Point(posX,posY));
	    this.now_stage = array.get(array.size() - 1);

	    init_windows();//ウィンドウ初期化
	    this.now_state = STATE.STATE_MOVE;
	    this.walking = 0;
	    this.now_dir = DIRECTION.DOWN;
	    frame_count = -1;
	    Game.sound_manager.playBGM("normal");
        }catch(FileNotFoundException e){
	    System.out.println(e);	
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void init_windows(){
	String[][] status_s = {{"レベル",String.valueOf(status.LEVEL)},
			       {"ＨＰ",String.valueOf(status.HP)},
			       {"ＭＰ",String.valueOf(status.MP)},
			       {"Ｇ",String.valueOf(status.GOLD)},
			       {"Ｅ",String.valueOf(status.E)} };

	String[][] strength_s = {{"レベル",String.valueOf(status.LEVEL)},
				 {"ちから",String.valueOf(status.POWER)},
				 {"すばやさ",String.valueOf(status.SPEED)},
				 {"さいだいＨＰ",String.valueOf(status.MAX_HP)},
				 {"さいだいＭＰ",String.valueOf(status.MAX_MP)},
				 {"こうげきカ",String.valueOf(status.POWER)},
				 {"しゅびカ",String.valueOf(status.DEFFENCE)}};

	String[][] select_s = {{"はなす","じゅもん"},
			       {"つよさ","どうぐ"},
			       {"とびら","しらべる"}};

	String[][] decide_s = {{"はい"},
			       {"いいえ"}};
	    
	/* ステータスボックスの位置 */
	int sx = MainPanel.WIDTH / 10;
	int sy = MainPanel.HEIGHT / 6;
	
	status_window = new StatusWindow(status.NAME,
					 sx,
					 sy,
					 status_s);

	strength_window = new StatusWindow("つよさ",
					   sx + status_window.get_WIDTH() + 5,
					   sy + 30,
					   strength_s);


	select_window = new SelectWindow("コマンド",
					 sx + status_window.get_WIDTH() + 100,
					 sy - 30,
					 select_s);

	mes_window = new MessageWindow("",
				       (int)(MainPanel.WIDTH / 5.3),
				       (int)(MainPanel.HEIGHT / 1.8),
				       MainPanel.WIDTH * 4 / 6,
				       150);

	
	decide_window = new SelectWindow("はなす",
					 (int)(MainPanel.WIDTH / 5.3) + 150 / 5,
					 (int)(MainPanel.HEIGHT / 1.8) - 30,
					 decide_s);
	
	
    }

    public void update_windows(){
	
	String[][] status_s = {{"レベル",String.valueOf(status.LEVEL)},
			       {"ＨＰ",String.valueOf(status.HP)},
			       {"ＭＰ",String.valueOf(status.MP)},
			       {"Ｇ",String.valueOf(status.GOLD)},
			       {"Ｅ",String.valueOf(status.E)} };

	String[][] strength_s = {{"レベル",String.valueOf(status.LEVEL)},
				 {"ちから",String.valueOf(status.POWER)},
				 {"すばやさ",String.valueOf(status.SPEED)},
				 {"さいだいＨＰ",String.valueOf(status.MAX_HP)},
				 {"さいだいＭＰ",String.valueOf(status.MAX_MP)},
				 {"こうげきカ",String.valueOf(status.POWER)},
				 {"しゅびカ",String.valueOf(status.DEFFENCE)}};
	
	/* ステータスボックスの位置 */
	int sx = MainPanel.WIDTH / 10;
	int sy = MainPanel.HEIGHT / 6;
	
	this.status_window.set_status_list(status_s);
	this.strength_window.set_status_list(strength_s);
    }
    
    /* ダメージを受ける */
    public void damaged(Enemy enemy,int damage){
	
	this.status.HP = Math.max(this.status.HP - damage,0);

	
	//update_windows();
	
	if(this.status.HP <= 0){
	    now_battle_event.end();
	    this.now_state = STATE.STATE_BATTLE_LOSE;
	}

	
    }

    public void LEVEL_UP(int level){
	this.status.LEVEL += level;
	this.status.POWER += 5 * level;
	this.status.SPEED += 3 * level;
	this.status.MAX_HP += 3 * level;
	this.status.MAX_MP += 4 * level;
	this.status.ATTACK += 3 * level;
	this.status.DEFFENCE += 2 * level;
	this.status.HP = this.status.MAX_HP;
	this.status.MP = this.status.MAX_MP;
	
    }
    /* LEVELのゲッタ(getter) */
    public int get_LEVEL(){
	return status.LEVEL;
    }

    /* LEVELのセッタ(setter) */
    public void set_LEVEL(int LEVEL){
	this.status.LEVEL = LEVEL;
    }

    /* HPのゲッタ(getter) */
    public int get_HP(){
	return status.HP;
    }

    /* HPのセッタ(setter) */
    public void set_HP(int HP){
	this.status.HP = HP;
    }

    /* MPのゲッタ(getter) */
    public int get_MP(){
	return status.MP;
    }

    /* MPのセッタ(setter) */
    public void set_MP(int MP){
	this.status.MP = MP;
    }

    /* GOLDのゲッタ(getter) */
    public int get_GOLD(){
	return status.GOLD;
    }

    /* GOLDのセッタ(setter) */
    public void set_GOLD(int GOLD){
	this.status.GOLD = GOLD;
    }

    /* start_xのゲッタ(getter) */
    public int get_start_x(){
	return start_x;
    }

    /* start_yのゲッタ(getter) */
    public int get_start_y(){
	return start_y;
    }
    
    /* frame_countのゲッタ(getter) */
    public int get_frame_count(){
	return frame_count;
    }

    /* frame_countのセッタ(setter) */
    public void set_frame_count(int frame_count){
	this.frame_count = frame_count;
    }

    /* now_stateのゲッタ(getter) */
    public STATE get_now_state(){
	return now_state;
    }

    /* now_stateのセッタ(setter) */
    public void set_now_state(STATE now_state){
	this.now_state = now_state;
    }
    
    /* now_stageのゲッタ(getter) */
    public String get_now_stage(){
	return now_stage;
    }
    
    /* posXのセッタ(setter) */
    public void set_posX(int posX){
	this.posX = posX;
    }

    /* posYのセッタ(setter) */
    public void set_posY(int posY){
	this.posY = posY;
    }

    /* start_xのセッタ(setter) */
    public void set_start_x(int start_x){
	this.start_x = start_x;
    }

    /* start_yのセッタ(setter) */
    public void set_start_y(int start_y){
	this.start_y = start_y;
    }

    /* walkingのセッタ(setter) */
    public void set_walking(int walking){
	this.walking = walking;
    }
    
}
