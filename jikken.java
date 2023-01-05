import javax.swing.*;
// import java.awt.*;
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

	private ImageIcon icon;
	private JLabel lb;

	public jikken()
	{
		int num = 000;
		icon = new ImageIcon("0ic_middle\\" + Integer.toString(num) + ".png");
		lb = new JLabel(icon);
		add(lb);
	}
}
