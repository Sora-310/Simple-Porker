import javax.swing.*;
import java.awt.*;

class Sample extends JFrame
{
	public static void main(String[] args)
	{
		Sample s = new Sample();
		s.setSize(700, 400);
		s.setTitle("ポーカー");
		s.setDefaultCloseOperation(EXIT_ON_CLOSE);
		s.setLocationRelativeTo(null);
		s.setVisible(true);
	}

	private JPanel cardPanel;

	public Sample()
	{
		cardPanel = new JPanel();
		cardPanel.setLayout(new CardLayout());
		cardPanel.add(new TitlePanel(), "Title");
		cardPanel.add(new PlayPanel(), "Play");		

		// タイトル画面を表示
		((CardLayout)(cardPanel.getLayout())).show(cardPanel, "Title");

		getContentPane().add(cardPanel);
	}
}

