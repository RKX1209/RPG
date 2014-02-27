import java.io.*;
import java.util.*;
import java.awt.*;

/* イベントを管理するクラス */
public class EnemyManager{
    
    private HashMap<String,Enemy> enemies;//敵達

    
    public EnemyManager(){
	enemies = new HashMap<String,Enemy>();
	loadFile("enemy.txt");
    }

    /* イベントのロード */
    public void loadFile(String filename){
	
	String path = "data/" + filename;
	try {
            BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream(path), "Shift_JIS"));
            String line;
            while ((line = br.readLine()) != null) {
		
                // 空行を読み飛ばす		
                if (line.equals("")) continue;
                // コメント行を読み飛ばす
                if (line.startsWith("#")) continue;
		String[] token = line.split(",");
		
                // 敵情報を取得する
                String name = token[0];
		String image_name = token[1];
		State state = new State(name);
		ArrayList<Integer> array = new ArrayList<Integer>();
		for(int i = 2; i < token.length; i++) array.add(Integer.parseInt(token[i]));
		state.SetData(array);
		int width = array.get(array.size() - 3);
		int height = array.get(array.size() - 2);
		int level = array.get(array.size() - 1);
		int x = MainPanel.WIDTH / 2 - width / 2;
		int y = MainPanel.HEIGHT / 2 - height / 2 - 50;
		state.MAX_HP = array.get(1);
		enemies.put(image_name,new Enemy(image_name,1,x,y,state,level));
		
            }
        }catch(FileNotFoundException e){
	    System.out.println(e);	
        } catch (IOException e) {
            e.printStackTrace();
        }	

    }
    
    /* 各イベントの更新 */
    public void update(){
	Iterator it = enemies.keySet().iterator();
	while (it.hasNext()) {
	    java.lang.Object p = it.next();
	    enemies.get(p).update();
	}
    }

    /* 各イベントの描画 */
    public void draw(Graphics g){
	Iterator it = enemies.keySet().iterator();
	while (it.hasNext()) {
	    java.lang.Object p = it.next();
	    enemies.get(p).draw(0,0,g);
	}
    }

    /* 指定された名前の敵が存在するか */
    public Enemy get_enemy(String s){
	if(!(enemies.containsKey(s))) return null;
	return enemies.get(s);
    }    
}
