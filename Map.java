import java.io.*;
import java.util.*;
import java.awt.*;

/* ステージのマップ用クラス */
public class Map{
    
    public static final int Frame_Of_Move = Player.Frame_Of_Move;//プレイヤーの移動に何フレーム使うか

    /* < マップの状態 >
       通常
       ロード中
       ロード完了
       ゲームクリア
       ゲームオーバー
     */
    public static enum STATE{ STATE_NORMAL,
			      STATE_STOP,
			      STATE_COMPLETE_LOADING,
			      STATE_GAME_CLEAR,
			      STATE_GAME_OVER};
    
    
    /* 表示するマップの範囲 */
    public static final int DISPLAY_COL = 21;//行数
    public static final int DISPLAY_ROW = 21;//列数
    
    /* マップの大きさ(チップ数) */
    private static int COL;//列数
    private static int ROW;//行数
    
    
    private static MapChip[][] map_chips;//マップチップ
    
    private static ArrayList<Object> objects;//存在するオブジェクトのリスト

    private Player player;//プレイヤークラスの参照

    private EventManager event_manager;//イベントマネージャーへの参照

    private STATE now_state;//現在の状態
    
    public Map(EventManager em,Player player){
	this.event_manager = em;
	objects = new ArrayList<Object>();
	this.player = player;
	this.now_state = STATE.STATE_NORMAL;
	this.objects = em.objects;
    }

    public void loadFile(String filename,boolean init){
	String path = "map/" + filename;
	try {
	    objects.clear();
	    File file = new File(path);
	    BufferedReader in = new BufferedReader(new FileReader(file));
            
            // 行数・列数を読み込む
            this.ROW = Integer.parseInt(in.readLine());
            this.COL = Integer.parseInt(in.readLine());
	    int pX = Integer.parseInt(in.readLine());
	    int pY = Integer.parseInt(in.readLine());
	    /* プレイヤー(主人公) */
	    if(!init){
		this.player.set_posX(pX);
		this.player.set_posY(pY);
	    }
			
	    objects.add(this.player);
	    
	    System.out.println(ROW + "," + COL);
            // マップを読み込む
            map_chips = new MapChip[ROW][COL];
	    
            for (int i = 0; i < ROW; i++) {
		String line = in.readLine();
                for (int j = 0; j < COL; j++) {
		    final int chip = line.charAt(j);
		    System.out.print((char)chip);
		    int y = MapChip.CHIP_SIZE * i;
		    int x = MapChip.CHIP_SIZE * j;
		    
		    switch(chip){
			/* ブロック */
		    case '#':
			map_chips[i][j] = new MapChip(MapChip.CHIP_TYPE.CHIP_BLOCK,x,y,true);
			break;
			
			/* タイル */
		    case '.':
			map_chips[i][j] = new MapChip(MapChip.CHIP_TYPE.CHIP_NORMAL_TYLE,x,y,false);
			break;
			
			/* タイル */
		    case '-':
			map_chips[i][j] = new MapChip(MapChip.CHIP_TYPE.CHIP_TYLE,x,y,false);
			break;
			
			/* 砂場 */
		    case 's':
			map_chips[i][j] = new MapChip(MapChip.CHIP_TYPE.CHIP_SAND,x,y,false);
			break;

			/* 水場 */
		    case 'w':
			map_chips[i][j] = new MapChip(MapChip.CHIP_TYPE.CHIP_WATER,x,y,true);
			break;
			
			/* 林 */
		    case 't':
			map_chips[i][j] = new MapChip(MapChip.CHIP_TYPE.CHIP_GROVE,x,y,true);
			break;
			
			/* 上り階段 */
		    case 'u':
			map_chips[i][j] = new MapChip(MapChip.CHIP_TYPE.CHIP_UP_STAIR,x,y,false);
			break;

			
			/* 下り階段 */
		    case 'd':
			map_chips[i][j] = new MapChip(MapChip.CHIP_TYPE.CHIP_DOWN_STAIR,x,y,false);
			break;

			/* 城 */
		    case 'C':
			map_chips[i][j] = new MapChip(MapChip.CHIP_TYPE.CHIP_CASTLE,x,y,false);
			break;
			
			/* タワー */
		    case 'T':
			map_chips[i][j] = new MapChip(MapChip.CHIP_TYPE.CHIP_TOWER,x,y,false);
			break;

			/* セーブポイント */
		    case 'S':
			map_chips[i][j] = new MapChip(MapChip.CHIP_TYPE.CHIP_SAVE,x,y,true);
			break;

			/* ボスチップ */
		    case 'B':
			map_chips[i][j] = new MapChip(MapChip.CHIP_TYPE.CHIP_BOSS,x,y,true);
			break;
			
			/* タイル */
		    default:
			map_chips[i][j] = new MapChip(MapChip.CHIP_TYPE.CHIP_TYLE,x,y,false);
			break;
			
		    }
                }
		System.out.println("");
            }
            in.close();//ファイルを閉じる

	    this.now_state = STATE.STATE_NORMAL;
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch(IOException e){
	    System.out.println(e);
	}
    }
    
    /* マップの更新 */
    public void update(){
	final STATE state = this.now_state;
	switch(state){
	    
	case STATE_NORMAL:
	    /* プレイヤーが移動要求をしてきたら*/
	    if(player.get_now_state() == Player.STATE.STATE_MOVE_STAGE_EVENT){
		/* マップとイベントを停止する */
		this.event_manager.set_now_state(EventManager.STATE.STATE_STOP);
		this.now_state = STATE.STATE_STOP;
	    }
	    else if(player.get_now_state() == Player.STATE.STATE_BATTLE_LOSE){
		this.event_manager.set_now_state(EventManager.STATE.STATE_STOP);
		this.now_state = STATE.STATE_GAME_OVER;
	    }
	    else if(player.get_now_state() == Player.STATE.STATE_CLEAR){
		this.event_manager.set_now_state(EventManager.STATE.STATE_STOP);
		this.now_state = STATE.STATE_GAME_CLEAR;
	    }
	    
	    else{
		/* オブジェクトの更新 */
		for(int i = 0; i < objects.size(); i++){
		    objects.get(i).update();
		}
	    }
	    break;

	case STATE_STOP://ロード中
	    break;

	case STATE_GAME_CLEAR://ゲームクリア
	    break;
	    
	case STATE_GAME_OVER://ゲームオーバー
	    break;
	    
	case STATE_COMPLETE_LOADING://ローディング完了
	    player.set_now_state(Player.STATE.STATE_MOVE);
	    player.set_walking(0);
	    this.now_state = STATE.STATE_NORMAL;
	    break;

	default:
	    break;
	    
	}
	
    }
    
    /* マップチップとオブジェクトの描画 */
    public void draw(Graphics g){
	final STATE state = this.now_state;
	switch(state){
	    
	case STATE_NORMAL:
	
	    int frame_count = player.get_frame_count();
	
	    int chip_size = MapChip.CHIP_SIZE;//1チップの大きさ
	
	    /* 移動アニメーションのためにオフセットを計算する */
	    double one_frame = (double)chip_size / (double)Frame_Of_Move;
	    double frame_offset = 0;
	
	    if(frame_count < 0) frame_offset = 0;
	    else frame_offset = one_frame * ((double)Frame_Of_Move - (double)frame_count - 1);

	    int dx = Object.DirectionVector[player.get_now_dir().ordinal()][0];
	    int dy = Object.DirectionVector[player.get_now_dir().ordinal()][1];

	    /* 描画する位置は、通常座標から1フレームごとに近づけていく(アニメーション) */
	    int pX = player.get_posX() * chip_size - (int)frame_offset * dx;
	    int pY = player.get_posY() * chip_size - (int)frame_offset * dy;
	

	    /* 左上(原点)とする座標を(offsetX,offsetY)を決定する */
	    int offsetX = Math.min(player.get_start_x() * chip_size - pX,0);
	    offsetX = Math.max(offsetX, MainPanel.WIDTH - ROW * chip_size);
	
	    int offsetY = Math.min(player.get_start_y() * chip_size - pY,0);
	    offsetY = Math.max(offsetY, MainPanel.HEIGHT - COL * chip_size);

	    /* 左上をタイルに変換 */
	    int sx = -offsetX / chip_size;
	    int sy = -offsetY / chip_size;
	    //System.out.println(new Point(sx,sy));
	
	    //画面の範囲内にあるマップチップを描画
	    for(int y = sy; y <= Math.min(sy + DISPLAY_COL,COL - 1); y++){
	    	for(int x = sx; x <= Math.min(sx + DISPLAY_ROW,ROW - 1); x++){
	    	    map_chips[y][x].draw(offsetX,offsetY,g);
	    	}
	    }

	
	    /* 画面の範囲内にあるオブジェクトを描画 */
	    for(int i = 0; i < objects.size(); i++){
		objects.get(i).draw(offsetX,offsetY,g);
	    }

	    break;

	case STATE_STOP://ロード中
	    break;

	}

    }

    /* プレイヤークラスのゲッタ(getter) */
    public Player get_Player(){
	return player;
    }

    /* now_stateのゲッタ(getter) */
    public STATE get_now_state(){
	return now_state;
    }

    /* now_stateのセッタ(setter) */
    public void set_now_state(STATE now_state){
	this.now_state = now_state;
    }
    
    /* 座標がマップの中か否か */
    public static boolean isInMap(int x,int y){
	return 0 <= x && x < COL && 0 <= y && y < ROW;
    }

    /* 衝突判定のあるマップチップか否か */
    public static boolean isCollision(int x,int y){
	boolean exist = false;
	for(int i = 0; i < objects.size(); i++){
	    if(objects.get(i).get_posX() == x && objects.get(i).get_posY() == y){
		exist = true;
		break;
	    }
	}
	return map_chips[y][x].get_collision() || exist;
    }
}
