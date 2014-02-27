
/* キーボードの状態を保持するクラス
   他のクラスから共有するため、シングルトンで実装 */

public class KeyBoard{
    private static KeyBoard mInstance = new KeyBoard();//唯一のインスタンス

    /* キー */
    public static enum KEY_CODE{ KEY_UP,
				 KEY_DOWN,
				 KEY_RIGHT,
				 KEY_LEFT,
				 KEY_Z,
				 KEY_X,
				 KEY_C,
				 KEY_SPACE,
				 KEY_NONE };

    /* 押された方のモード(トグルは切り替え方式) */
    public static enum MODE{ MODE_NORMAL,
			     MODE_TOGGLE };
    
    private static final int Key_Kind = 9;//キーボードの種類

    //private MODE mode;//モード
    
    private boolean[] Key_Code;//押されたボタン

    private boolean[] first_push;//押しっぱなしではなく始めて押された

    private boolean[] before;
    
    private KeyBoard(){
	Key_Code = new boolean[Key_Kind];
	first_push = new boolean[Key_Kind];
	//mode = MODE_NORMAL;
	before = new boolean[Key_Kind];
    }

    /* キーコードのセッタ(Setter) */
    public void Press(KEY_CODE p){
	//System.out.println("Press");
	this.Key_Code[p.ordinal()] = true;
    }

    /* キーコードのセッタ(Setter) */
    public void Release(KEY_CODE p){
	//System.out.println("Release");
	this.Key_Code[p.ordinal()] = false;
    }

    /* キーが押されているか否か */
    public boolean IsPressed(KEY_CODE p,MODE mode){
	if(mode == MODE.MODE_TOGGLE){	
	    //	System.out.println(before);
	    if(!before[p.ordinal()] && this.Key_Code[p.ordinal()]){
		//	    System.out.println("change");
		first_push[p.ordinal()] = true;
	    }else{
		first_push[p.ordinal()] = false;
	    }
	    before[p.ordinal()] = this.Key_Code[p.ordinal()];

	    boolean res = (this.Key_Code[p.ordinal()] && this.first_push[p.ordinal()]) == true;
	    // if(res)
	    // 	System.out.println("#####toggle#####");
	    return res;
	}	
	return this.Key_Code[p.ordinal()] == true;
    }

    /* キーが放されているか否か */
    public boolean IsReleased(KEY_CODE p){
	return this.Key_Code[p.ordinal()] == false;
    }
    
    /* インスタンスを得る */
    public static KeyBoard getInstance(){
	return mInstance;
    }
}
