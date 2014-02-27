import java.awt.*;

/* グラフィックスのリソースクラス */
public class Resource{
    private Image image;//画像リソース
    
    /* 大きさ(サイズ) */
    private int width;
    private int height;

    private String name;//画像の名前
    public Resource(String filename){
	name = filename;
	LoadFile(filename);
	
    }

    /* ファイルのロード */
    public void LoadFile(String filename){
	String path = "image/" + filename;
	image = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource(path));
	// width = image.getWidth(this);
	// height = image.getHeight(this);
    }

    /* イメージのゲッタ(getter) */
    public Image get_Image(){
	return image;
    }

    /* サイズのゲッタ(getter) */
    public int get_Width(){
	return width;
    }
    public int get_Height(){
	return height;
    }
    
    
}
