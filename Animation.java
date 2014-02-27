
/* グラフィックスのアニメーションクラス
   他のクラスと共有するため、シングルトンで実装 */

public class Animation{
    private static Animation mInstance = new Animation();//唯一のインスタンス

    private static final int Animation_Length_Limit = 999999;//アニメーション数の限界
    
    private int now_count;//現在のアニメーション番号
    
    private Animation(){
	now_count = 0;
    }

    /* アニメーションを進める */
    public void Next(){
	now_count++;//アニメーションを進める
	now_count %= Animation_Length_Limit;//オーバーフロー防止
	//System.out.println(now_count);
    }

    
    /* 現在のアニメーション番号を得る */
    public int getNowCount(){
	return now_count;
    }
    
    /* インスタンスを得る */
    public static Animation getInstance(){
	return mInstance;
    }

}
