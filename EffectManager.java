import java.util.*;
import java.awt.*;
import java.io.*;

public class EffectManager{
    
    private HashMap<String,Effect> effects;//エフェクト達
    
    public EffectManager(){
	effects = new HashMap<String,Effect>();
	loadFile("effect.txt");
    }

    /* エフェクトのロード */
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
		
                // エフェクト情報を取得する
                String name = token[0];
		String image_name = token[1];
		int animation_len = Integer.parseInt(token[2]);
		int width = Integer.parseInt(token[3]);
		int height = Integer.parseInt(token[4]);
		int x = MainPanel.WIDTH / 2 - width / 2;
		int y = MainPanel.HEIGHT / 2 - height / 2 - 50;

		effects.put(name,new Effect(image_name,animation_len,x,y,width,height));
				
            }
        }catch(FileNotFoundException e){
	    System.out.println(e);	
        } catch (IOException e) {
            e.printStackTrace();
        }	

    }

    /* 各エフェクトの更新 */
    public void update(){
	Iterator it = effects.keySet().iterator();
	while (it.hasNext()) {
	    java.lang.Object p = it.next();
	    effects.get(p).update();
	}
    }

    /* 各エフェクトの描画 */
    public void draw(Graphics g){
	Iterator it = effects.keySet().iterator();
	while (it.hasNext()) {
	    java.lang.Object p = it.next();
	    effects.get(p).draw(0,0,g);
	}
    }
	
    /* 指定された名前のエフェクトが存在するか */
    public Effect get_effect(String s){
	if(!(effects.containsKey(s))) return null;
	return effects.get(s);
    }    

}
