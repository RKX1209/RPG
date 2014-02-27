import java.io.*;
import java.util.*;
import java.awt.*;

/* イベントを管理するクラス */
public class EventManager{
    
    private HashMap<Point,Event> events;//イベント達

    private ArrayList<BattleEvent> battles;//バトルイベント

    public ArrayList<Object> objects;//存在するオブジェクトのリスト

    /* < イベントの状態 >
       通常
       ロード中
     */
    public static enum STATE{ STATE_NORMAL,
			      STATE_STOP};

    private STATE now_state;
    
    public EventManager(){
	events = new HashMap<Point,Event>();
	battles = new ArrayList<BattleEvent>();
	objects = new ArrayList<Object>();
    }

    /* イベントのロード */
    public void loadEvents(String filename){
	System.out.println("Loading... events");
	String path = "event/" + filename;
	try {
	    events.clear();
	    battles.clear();
	    /* "しらべる"失敗もイベントとして扱う */
	    String[] con = {"ここにはなにもないようだ"};
	    events.put(new Point(-1,-1),new TalkEvent(con));

            BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream(path), "Shift_JIS"));
            String line;
            while ((line = br.readLine()) != null) {
		
                // 空行を読み飛ばす		
                if (line.equals("")) continue;
                // コメント行を読み飛ばす
                if (line.startsWith("#")) continue;
                StringTokenizer st = new StringTokenizer(line, ",");
		
                // イベント情報を取得する
                // イベントタイプを取得してイベントごとに処理する
                String eventType = st.nextToken();
		
                if (eventType.equals("TALK")) {  // 会話イベント追加
		    String people = st.nextToken();
		    int x = Integer.parseInt(st.nextToken());
		    int y = Integer.parseInt(st.nextToken());
		    String mes = st.nextToken();		    
		    String[] conv = mes.split("-");
		    events.put(new Point(x,y),new TalkEvent(conv));
		    objects.add(new NormalPeople(people + ".png",3,x,y));
		}
		else if (eventType.equals("MOVE")) {  // 移動イベント追加
		    int x = Integer.parseInt(st.nextToken());
		    int y = Integer.parseInt(st.nextToken());
		    String stage_name = st.nextToken();	
		    events.put(new Point(x,y),new MoveEvent(stage_name));
		}
		else if (eventType.equals("SAVE")) {  // 移動イベント追加
		    int x = Integer.parseInt(st.nextToken());
		    int y = Integer.parseInt(st.nextToken());
		    String mes = st.nextToken();
		    String[] conv = mes.split("-");
		    events.put(new Point(x,y),new SaveEvent(conv));
		}
		else if (eventType.equals("BATTLE")) {  // 戦闘イベント追加
		    int x = Integer.parseInt(st.nextToken());
		    int y = Integer.parseInt(st.nextToken());
		    String enemy_name = st.nextToken();//敵の名前
		    int enemy_level = Integer.parseInt(st.nextToken());//敵のレベル
		    int mode = Integer.parseInt(st.nextToken());//モード
		    Enemy enemy = Game.enemy_manager.get_enemy(enemy_name);//データから敵を作成
		    boolean search = (mode == 1);
		    if(enemy != null){
			if(search)
			    events.put(new Point(x,y),new BattleEvent(enemy,search));
			else
			    battles.add(new BattleEvent(enemy,search));
		    }
		}

		
            }
	    this.now_state = STATE.STATE_NORMAL;
        }catch(FileNotFoundException e){
	    System.out.println(e);	
        } catch (IOException e) {
            e.printStackTrace();
        }	

    }
    
    /* 各イベントの更新 */
    public void update(){
	final STATE state = this.now_state;
	switch(state){
	    
	case STATE_NORMAL:
	
	    Iterator it = events.keySet().iterator();
	    while (it.hasNext()) {
		java.lang.Object p = it.next();
		events.get(p).update();
	    }
	    for(int i = 0; i < battles.size(); i++) battles.get(i).update();
	    break;

	case STATE_STOP:
	    break;
	}
    }

    /* 各イベントの描画 */
    public void draw(Graphics g){
	final STATE state = this.now_state;
	switch(state){
	    
	case STATE_NORMAL:

	    Iterator it = events.keySet().iterator();
	    while (it.hasNext()) {
		java.lang.Object p = it.next();
		events.get(p).draw(g);
	    }
	    for(int i = 0; i < battles.size(); i++) battles.get(i).draw(g);
	    break;

	case STATE_STOP:
	    break;
	}
    }

    /* 指定された位置にイベントが存在するか */
    public Event get_event(Point p){
	if(!(events.containsKey(p))) return null;
	return events.get(p);
    }

    /* ランダムに戦闘イベントを返す */
    public BattleEvent get_battle_event(int walk){
	int prob = (walk <= 5 ? 0 : walk / 25) * 100;//イベント発生確率
	Random rnd = new Random();
        int ran = rnd.nextInt(100);
	if(ran < prob){
	    /* イベント発生 */
	    if(battles.size() <= 0) return null;
	    int index = rnd.nextInt(battles.size());
	    return battles.get(index);
	}
	return null;
    }
    
    /* now_stateのゲッタ(getter) */
    public STATE get_now_state(){
	return now_state;
    }

    /* now_stateのセッタ(setter) */
    public void set_now_state(STATE now_state){
	this.now_state = now_state;
    }
    
}
