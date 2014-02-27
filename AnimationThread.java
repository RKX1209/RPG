import java.awt.*;
import java.util.*;
import java.awt.event.*;
import javax.swing.*;

public class AnimationThread implements Runnable{
    
    public static final double APS = 3.0;//APS(Animation Per Second) 1秒間に何アニメーション
    public static final double Animation_Interval = 1.0 / APS * 1000;//描画間隔[ms]
    
    private Animation animation;//アニメーションクラス

    public AnimationThread(){
	
    }
	    
    /* ゲームのメインループ */
    public void run(){
	while(true){
	    Animation mInstance = animation.getInstance();
	    mInstance.Next();//アニメーションを進める
	    try {
		Thread.sleep((long)Animation_Interval);
	    } catch (InterruptedException e) {
		e.printStackTrace();
	    }

	}
    }

}
