import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

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

	public JPanel cardPanel;

	public Sample()
	{
		cardPanel = new JPanel();
		cardPanel.setLayout(new CardLayout());
		cardPanel.add(new TitlePanel(), "Title");
		cardPanel.add(new PlayPanel(), "Play");

		((CardLayout)(cardPanel.getLayout())).show(cardPanel, "Title");

		getContentPane().add(cardPanel);
	}

	class TitlePanel extends JPanel
	{
		private JLabel titleName;
		private JButton startButton;
		private JButton endButton;

		public TitlePanel()
		{
			setLayout(null);

			// タイトルの表示
			titleName = new JLabel("Porker");
			titleName.setFont(new Font("Arial", Font.PLAIN, 70));
			titleName.setBounds(240, 100, 400, 70);
			add(titleName);

			// スタートボタン
			startButton = new JButton("はじめる");
			startButton.setBounds(280, 270, 130, 60);
			startButton.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					((CardLayout)(cardPanel.getLayout())).show(cardPanel, "Play");
				}
			});
			add(startButton);

			// 終了ボタン
			endButton = new JButton("終了");
			endButton.setBounds(20, 310, 80, 40);
			endButton.addActionListener(e -> System.exit(0));
			add(endButton);
		}
	}
}

class PlayPanel extends JPanel
{
	// 画像処理・表示はこっち
	// private ImageIcon cardsImage;

	private JLabel titleName;

	public PlayPanel()
	{
		setLayout(null);

		titleName = new JLabel("Porker");
		titleName.setFont(new Font("Arial", Font.PLAIN, 70));
		titleName.setBounds(240, 100, 400, 70);
		add(titleName);
	}
}

class CardsManager
{
	private int[] cards;
	
	/*
		カードの配役
		000 ： ジョーカー
		101～113 ： クローバーの1～13
		201～213 ： ダイヤの1～13
        301～313 ： ハートの1～13
		401～413 ： スペードの1～13
	*/

	public CardsManager()
	{
		cards = new int[53];
		initialize();
	}

    public void initialize()
	{
		cards[0] = 0;

		for(int i = 1; i <= 13; i++)
		{
			// 
			cards[i] = 100 + i;
			cards[i + 13] = 200 + i;
			cards[i + 26] = 300 + i;
			cards[i + 39] = 400 + i;
		}
	}

	public void shuffle()
	{
		for(int i = 0; i < cards.length; i++)
		{

		}
	}
}

class Porker
{
	public void firstHand()
	{
		
	}
}