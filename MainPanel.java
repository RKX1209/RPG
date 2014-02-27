import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.awt.event.*;



public class MainPanel extends JPanel implements Runnable{

    /* ウィンドウの大きさ */
    public static final int WIDTH = MapChip.CHIP_SIZE * Map.DISPLAY_ROW;
    public static final int HEIGHT = MapChip.CHIP_SIZE * Map.DISPLAY_COL;
    
    public static final double FPS = 40;//画面のFPS
    public static final double Repaint_Interval = 1.0 / FPS * 1000;//描画間隔[ms]
    
    private Game game;//ゲームのメインクラス

    /* スレッド実装クラス */
    private AnimationThread animation_thread;

    /* 実際のスレッド */
    private Thread game_loop;//ゲームループ用のスレッド
    private Thread animation_loop;//アニメーション用のスレッド

    public MainPanel() { 
        setPreferredSize(new Dimension(WIDTH, HEIGHT));//ウィンドウの大きさ設定
	setFocusable(true);
	
	/* 初期化 */
	game = new Game();//ゲームクラス
		
	addKeyListener(game);//キーボードイベント処理はGameクラスに投げる

	/* スレッド初期化 & 開始 */
	animation_thread = new AnimationThread();
	
	/* メインループ用スレッド */
	game_loop = new Thread(this);
	animation_loop = new Thread(animation_thread);

	/* スレッドスタート */
	game_loop.start();
	animation_loop.start();
	
    }

    /* ゲームのメインループ */
    public void run(){
	while(true){
	    
	    game.update();
	    
	    repaint();
	    try {
		Thread.sleep((long)Repaint_Interval);
	    } catch (InterruptedException e) {
		e.printStackTrace();
	    }

	}
    }

    /* 再描画時に呼ばれる */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
	game.draw(g);
    }

}
