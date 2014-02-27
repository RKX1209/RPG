import java.awt.*;
import javax.swing.*;

public class MainFrame extends JFrame {
    public MainFrame() {
        setTitle("RPGTest");
	MainPanel main_panel = new MainPanel();
        Container contentPane = getContentPane();
        contentPane.add(main_panel,BorderLayout.CENTER);

        pack();
    }

    public static void main(String[] args) {
        MainFrame frame = new MainFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setResizable(false);//サイズ変更不可
        frame.setVisible(true);
    }
}
