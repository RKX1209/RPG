import java.awt.*;
import java.util.HashMap;

import javax.swing.ImageIcon;

/* メッセージに使う文字列のクラス */
public class MessageString{

    /* ロードを一度で済ませるためにstaticで共有 */
    private static Resource fontImage;//フォント画像
    
    private static HashMap<String,Point> kana2Pos;//カナ->座標への変換

    public static enum COLOR{ WHITE,
			      RED,
			      GREEN,
			      BLUE };

    /* フォントの大きさ */
    public static final int FONT_WIDTH = 16;
    public static final int FONT_HEIGHT = 22;
    
    private COLOR color;//文字の色

    
    public MessageString(COLOR color){
	/* フォントイメージがまだロードされていなかったら */
	if(fontImage == null){
	    // フォントイメージをロード
	    fontImage = new Resource("font.gif");
	}

	/* ハッシュがまだ作成されていなかったら */
	if(kana2Pos == null){
	    // かな→イメージ座標へのハッシュを作成
	    kana2Pos = new HashMap<String,Point>();
	    createHash();
	}
        
	this.color = color;
	
    }

    /* 指定されたメッセージを表示する */
    public void drawMessage(Graphics g,int x,int y,String message){
	for(int i = 0; i < message.length(); i++){
	    char c = message.charAt(i);
	    String c_s = String.valueOf(c);
	    Point pos = (Point)kana2Pos.get(c_s);//文字からフォント内の座標を取得
	    if(pos == null) continue;
	    
	    int color_width = 160;
	    
	    int now_x = x + FONT_WIDTH * i;
	    int px = pos.x * FONT_WIDTH + color_width * color.ordinal();
	    int py = pos.y * FONT_HEIGHT;
	    
	    Image image = fontImage.get_Image();//画像


	    g.drawImage(image,
	    		now_x,y,
	    		now_x + FONT_WIDTH,y + FONT_HEIGHT,
	    		px,py,
	    		px + FONT_WIDTH,py + FONT_HEIGHT,
	    		null);
	    
	}
    }

    /* 色のセッタ(setter) */
    public void set_Color(COLOR color){
	this.color = color;
    }

    /* 色のゲッタ(getter) */
    public COLOR get_Color(){
	return color;
    }
    
    //x:16 y:22
    /* 文字から座標へのハッシュを作成する */
    private void createHash() {
        kana2Pos.put(new String("あ"), new Point(0, 0));
        kana2Pos.put(new String("い"), new Point(1, 0));
        kana2Pos.put(new String("う"), new Point(2, 0));
        kana2Pos.put(new String("え"), new Point(3, 0));
        kana2Pos.put(new String("お"), new Point(4, 0));
        
        kana2Pos.put(new String("か"), new Point(0, 1));
        kana2Pos.put(new String("き"), new Point(1, 1));
        kana2Pos.put(new String("く"), new Point(2, 1));
        kana2Pos.put(new String("け"), new Point(3, 1));
        kana2Pos.put(new String("こ"), new Point(4, 1));
        
        kana2Pos.put(new String("さ"), new Point(0, 2));
        kana2Pos.put(new String("し"), new Point(1, 2));
        kana2Pos.put(new String("す"), new Point(2, 2));
        kana2Pos.put(new String("せ"), new Point(3, 2));
        kana2Pos.put(new String("そ"), new Point(4, 2));
        
        kana2Pos.put(new String("た"), new Point(0, 3));
        kana2Pos.put(new String("ち"), new Point(1, 3));
        kana2Pos.put(new String("つ"), new Point(2, 3));
        kana2Pos.put(new String("て"), new Point(3, 3));
        kana2Pos.put(new String("と"), new Point(4, 3));
        
        kana2Pos.put(new String("な"), new Point(0, 4));
        kana2Pos.put(new String("に"), new Point(1, 4));
        kana2Pos.put(new String("ぬ"), new Point(2, 4));
        kana2Pos.put(new String("ね"), new Point(3, 4));
        kana2Pos.put(new String("の"), new Point(4, 4));
        
        kana2Pos.put(new String("は"), new Point(0, 5));
        kana2Pos.put(new String("ひ"), new Point(1, 5));
        kana2Pos.put(new String("ふ"), new Point(2, 5));
        kana2Pos.put(new String("へ"), new Point(3, 5));
        kana2Pos.put(new String("ほ"), new Point(4, 5));
        
        kana2Pos.put(new String("ま"), new Point(0, 6));
        kana2Pos.put(new String("み"), new Point(1, 6));
        kana2Pos.put(new String("む"), new Point(2, 6));
        kana2Pos.put(new String("め"), new Point(3, 6));
        kana2Pos.put(new String("も"), new Point(4, 6));
        
        kana2Pos.put(new String("や"), new Point(0, 7));
        kana2Pos.put(new String("ゆ"), new Point(1, 7));
        kana2Pos.put(new String("よ"), new Point(2, 7));
        kana2Pos.put(new String("わ"), new Point(3, 7));
        kana2Pos.put(new String("を"), new Point(4, 7));
        
        kana2Pos.put(new String("ら"), new Point(0, 8));
        kana2Pos.put(new String("り"), new Point(1, 8));
        kana2Pos.put(new String("る"), new Point(2, 8));
        kana2Pos.put(new String("れ"), new Point(3, 8));
        kana2Pos.put(new String("ろ"), new Point(4, 8));
        
        kana2Pos.put(new String("ん"), new Point(0, 9));
        kana2Pos.put(new String("ぃ"), new Point(1, 9));
        kana2Pos.put(new String("っ"), new Point(2, 9));
        kana2Pos.put(new String("ぇ"), new Point(3, 9));
        kana2Pos.put(new String("　"), new Point(4, 9));
        
        kana2Pos.put(new String("ゃ"), new Point(0, 10));
        kana2Pos.put(new String("ゅ"), new Point(1, 10));
        kana2Pos.put(new String("ょ"), new Point(2, 10));
        kana2Pos.put(new String("、"), new Point(3, 10));
        kana2Pos.put(new String("。"), new Point(4, 10));
        
        kana2Pos.put(new String("が"), new Point(0, 11));
        kana2Pos.put(new String("ぎ"), new Point(1, 11));
        kana2Pos.put(new String("ぐ"), new Point(2, 11));
        kana2Pos.put(new String("げ"), new Point(3, 11));
        kana2Pos.put(new String("ご"), new Point(4, 11));
        
        kana2Pos.put(new String("ざ"), new Point(0, 12));
        kana2Pos.put(new String("じ"), new Point(1, 12));
        kana2Pos.put(new String("ず"), new Point(2, 12));
        kana2Pos.put(new String("ぜ"), new Point(3, 12));
        kana2Pos.put(new String("ぞ"), new Point(4, 12));
        
        kana2Pos.put(new String("だ"), new Point(0, 13));
        kana2Pos.put(new String("ぢ"), new Point(1, 13));
        kana2Pos.put(new String("づ"), new Point(2, 13));
        kana2Pos.put(new String("で"), new Point(3, 13));
        kana2Pos.put(new String("ど"), new Point(4, 13));
        
        kana2Pos.put(new String("ば"), new Point(0, 14));
        kana2Pos.put(new String("び"), new Point(1, 14));
        kana2Pos.put(new String("ぶ"), new Point(2, 14));
        kana2Pos.put(new String("べ"), new Point(3, 14));
        kana2Pos.put(new String("ぼ"), new Point(4, 14));
        
        kana2Pos.put(new String("ぱ"), new Point(0, 15));
        kana2Pos.put(new String("ぴ"), new Point(1, 15));
        kana2Pos.put(new String("ぷ"), new Point(2, 15));
        kana2Pos.put(new String("ぺ"), new Point(3, 15));
        kana2Pos.put(new String("ぽ"), new Point(4, 15));
        
        kana2Pos.put(new String("ア"), new Point(5, 0));
        kana2Pos.put(new String("イ"), new Point(6, 0));
        kana2Pos.put(new String("ウ"), new Point(7, 0));
        kana2Pos.put(new String("エ"), new Point(8, 0));
        kana2Pos.put(new String("オ"), new Point(9, 0));
        
        kana2Pos.put(new String("カ"), new Point(5, 1));
        kana2Pos.put(new String("キ"), new Point(6, 1));
        kana2Pos.put(new String("ク"), new Point(7, 1));
        kana2Pos.put(new String("ケ"), new Point(8, 1));
        kana2Pos.put(new String("コ"), new Point(9, 1));

        kana2Pos.put(new String("サ"), new Point(5, 2));
        kana2Pos.put(new String("シ"), new Point(6, 2));
        kana2Pos.put(new String("ス"), new Point(7, 2));
        kana2Pos.put(new String("セ"), new Point(8, 2));
        kana2Pos.put(new String("ソ"), new Point(9, 2));
        
        kana2Pos.put(new String("タ"), new Point(5, 3));
        kana2Pos.put(new String("チ"), new Point(6, 3));
        kana2Pos.put(new String("ツ"), new Point(7, 3));
        kana2Pos.put(new String("テ"), new Point(8, 3));
        kana2Pos.put(new String("ト"), new Point(9, 3));
        
        kana2Pos.put(new String("ナ"), new Point(5, 4));
        kana2Pos.put(new String("ニ"), new Point(6, 4));
        kana2Pos.put(new String("ヌ"), new Point(7, 4));
        kana2Pos.put(new String("ネ"), new Point(8, 4));
        kana2Pos.put(new String("ノ"), new Point(9, 4));
        
        kana2Pos.put(new String("ハ"), new Point(5, 5));
        kana2Pos.put(new String("ヒ"), new Point(6, 5));
        kana2Pos.put(new String("フ"), new Point(7, 5));
        kana2Pos.put(new String("ヘ"), new Point(8, 5));
        kana2Pos.put(new String("ホ"), new Point(9, 5));
        
        kana2Pos.put(new String("マ"), new Point(5, 6));
        kana2Pos.put(new String("ミ"), new Point(6, 6));
        kana2Pos.put(new String("ム"), new Point(7, 6));
        kana2Pos.put(new String("メ"), new Point(8, 6));
        kana2Pos.put(new String("モ"), new Point(9, 6));
        
        kana2Pos.put(new String("ヤ"), new Point(5, 7));
        kana2Pos.put(new String("ユ"), new Point(6, 7));
        kana2Pos.put(new String("ヨ"), new Point(7, 7));
        kana2Pos.put(new String("ワ"), new Point(8, 7));
        kana2Pos.put(new String("ヲ"), new Point(9, 7));
        
        kana2Pos.put(new String("ラ"), new Point(5, 8));
        kana2Pos.put(new String("リ"), new Point(6, 8));
        kana2Pos.put(new String("ル"), new Point(7, 8));
        kana2Pos.put(new String("レ"), new Point(8, 8));
        kana2Pos.put(new String("ロ"), new Point(9, 8));
        
        kana2Pos.put(new String("ン"), new Point(5, 9));
        kana2Pos.put(new String("ィ"), new Point(6, 9));
        kana2Pos.put(new String("ッ"), new Point(7, 9));
        kana2Pos.put(new String("ェ"), new Point(8, 9));
        kana2Pos.put(new String("「"), new Point(9, 9));
        
        kana2Pos.put(new String("ャ"), new Point(5, 10));
        kana2Pos.put(new String("ュ"), new Point(6, 10));
        kana2Pos.put(new String("ョ"), new Point(7, 10));
        kana2Pos.put(new String("ー"), new Point(8, 10));
        kana2Pos.put(new String("」"), new Point(9, 10));
        
        kana2Pos.put(new String("ガ"), new Point(5, 11));
        kana2Pos.put(new String("ギ"), new Point(6, 11));
        kana2Pos.put(new String("グ"), new Point(7, 11));
        kana2Pos.put(new String("ゲ"), new Point(8, 11));
        kana2Pos.put(new String("ゴ"), new Point(9, 11));
        
        kana2Pos.put(new String("ザ"), new Point(5, 12));
        kana2Pos.put(new String("ジ"), new Point(6, 12));
        kana2Pos.put(new String("ズ"), new Point(7, 12));
        kana2Pos.put(new String("ゼ"), new Point(8, 12));
        kana2Pos.put(new String("ゾ"), new Point(9, 12));
        
        kana2Pos.put(new String("ダ"), new Point(5, 13));
        kana2Pos.put(new String("ヂ"), new Point(6, 13));
        kana2Pos.put(new String("ヅ"), new Point(7, 13));
        kana2Pos.put(new String("デ"), new Point(8, 13));
        kana2Pos.put(new String("ド"), new Point(9, 13));
        
        kana2Pos.put(new String("バ"), new Point(5, 14));
        kana2Pos.put(new String("ビ"), new Point(6, 14));
        kana2Pos.put(new String("ブ"), new Point(7, 14));
        kana2Pos.put(new String("ベ"), new Point(8, 14));
        kana2Pos.put(new String("ボ"), new Point(9, 14));
        
        kana2Pos.put(new String("パ"), new Point(5, 15));
        kana2Pos.put(new String("ピ"), new Point(6, 15));
        kana2Pos.put(new String("プ"), new Point(7, 15));
        kana2Pos.put(new String("ペ"), new Point(8, 15));
        kana2Pos.put(new String("ポ"), new Point(9, 15));
        
        kana2Pos.put(new String("0"), new Point(0, 16));        
        kana2Pos.put(new String("1"), new Point(1, 16));
        kana2Pos.put(new String("2"), new Point(2, 16));
        kana2Pos.put(new String("3"), new Point(3, 16));
        kana2Pos.put(new String("4"), new Point(4, 16));
        kana2Pos.put(new String("5"), new Point(5, 16));
        kana2Pos.put(new String("6"), new Point(6, 16));
        kana2Pos.put(new String("7"), new Point(7, 16));
        kana2Pos.put(new String("8"), new Point(8, 16));
        kana2Pos.put(new String("9"), new Point(9, 16));
        
        kana2Pos.put(new String("Ａ"), new Point(0, 17));        
        kana2Pos.put(new String("Ｂ"), new Point(1, 17));
        kana2Pos.put(new String("Ｃ"), new Point(2, 17));
        kana2Pos.put(new String("Ｄ"), new Point(3, 17));
        kana2Pos.put(new String("Ｅ"), new Point(4, 17));
        kana2Pos.put(new String("Ｆ"), new Point(5, 17));
        kana2Pos.put(new String("Ｇ"), new Point(6, 17));
        kana2Pos.put(new String("Ｈ"), new Point(7, 17));
        kana2Pos.put(new String("Ｉ"), new Point(8, 17));
        kana2Pos.put(new String("Ｊ"), new Point(9, 17));
        
        kana2Pos.put(new String("Ｋ"), new Point(0, 18));        
        kana2Pos.put(new String("Ｌ"), new Point(1, 18));
        kana2Pos.put(new String("Ｍ"), new Point(2, 18));
        kana2Pos.put(new String("Ｎ"), new Point(3, 18));
        kana2Pos.put(new String("Ｏ"), new Point(4, 18));
        kana2Pos.put(new String("Ｐ"), new Point(5, 18));
        kana2Pos.put(new String("Ｑ"), new Point(6, 18));
        kana2Pos.put(new String("Ｒ"), new Point(7, 18));
        kana2Pos.put(new String("Ｓ"), new Point(8, 18));
        kana2Pos.put(new String("Ｔ"), new Point(9, 18));
        
        kana2Pos.put(new String("Ｕ"), new Point(0, 19));        
        kana2Pos.put(new String("Ｖ"), new Point(1, 19));
        kana2Pos.put(new String("Ｗ"), new Point(2, 19));
        kana2Pos.put(new String("Ｘ"), new Point(3, 19));
        kana2Pos.put(new String("Ｙ"), new Point(4, 19));
        kana2Pos.put(new String("Ｚ"), new Point(5, 19));
        kana2Pos.put(new String("！"), new Point(6, 19));
        kana2Pos.put(new String("？"), new Point(7, 19));
    }
    
}

