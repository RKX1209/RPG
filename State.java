import java.awt.*;
import java.util.*;

public class State{
    /* ステータス */
    public String NAME;//名前
    public int LEVEL;//レベル
    public int HP;//現在のHP
    public int MP;//現在のMP
    public int GOLD;//現在のGOLD
    public int E;

    public int POWER;//ちから
    public int SPEED;//すばやさ
    public int MAX_HP;//さいだいHP
    public int MAX_MP;//さいだいMP
    public int ATTACK;//こうげき力
    public int DEFFENCE;//しゅび力
    
    public State(){
	this.NAME = "ゆうしゃ";
    }

    public State(String name){
	this.NAME = name;
    }
    public void SetData(ArrayList<Integer> array){
	this.LEVEL = array.get(0);
	this.HP = array.get(1);
	this.MP = array.get(2);
	this.GOLD = array.get(3);
	this.E = array.get(4);

	this.POWER = array.get(5);
	this.SPEED = array.get(6);
	this.MAX_HP = array.get(7);
	this.MAX_MP = array.get(8);
	this.ATTACK = array.get(9);
	this.DEFFENCE = array.get(10);

    }

    public int[] getData(){
	int[] datas = {this.LEVEL,this.HP,this.MP,
		       this.GOLD,this.E,this.POWER,
		       this.SPEED,this.MAX_HP,this.MAX_MP,
		       this.ATTACK,this.DEFFENCE};
	return datas;
    }

}
