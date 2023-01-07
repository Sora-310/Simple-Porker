import javax.swing.*;
import java.awt.*;
// import java.awt.event.*;

class jikken extends JFrame
{
	public static void main(String[] args)
	{
		jikken j = new jikken();
		j.setSize(700, 400);
		j.setTitle("実験");
		j.setDefaultCloseOperation(EXIT_ON_CLOSE);
		j.setLocationRelativeTo(null);
		j.setVisible(true);
	}

	// private ImageIcon icon;
	// private JLabel lb;
	private JButton bt;

	public jikken()
	{
		setLayout(null);

		bt = new JButton(imageResize(101));
		bt.setBounds(100, 100, 90, 120);
		add(bt);
	}

	public ImageIcon imageResize(int num)
	{
		// 画像を取得
		ImageIcon icon = new ImageIcon("0ic_middle\\" + Integer.toString(num) + ".png");

		// Imageクラスを用いてサイズを調整
		Image img = icon.getImage();
		Image newimg = img.getScaledInstance(90, 120, Image.SCALE_SMOOTH);
		icon = new ImageIcon(newimg);

		return icon;
	}
}
