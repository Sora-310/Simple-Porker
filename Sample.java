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

		// タイトル画面を表示
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
					// プレイ画面に遷移
					((CardLayout)(cardPanel.getLayout())).show(cardPanel, "Play");
				}
			});
			add(startButton);

			// 終了ボタン
			endButton = new JButton("終了");
			endButton.setBounds(20, 310, 80, 40);
			endButton.addActionListener(e -> System.exit(0));	// プログラムを終了
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
		// カードの束の作成
		cards = new int[53];
		initialize();
	}

    public void initialize()
	{
		// ジョーカーを格納
		cards[0] = 0;

		// 他のカードを格納
		for(int i = 1; i <= 13; i++)
		{
			// クローバー
			cards[i] = 100 + i;

			// ダイヤ
			cards[i + 13] = 200 + i;

			// ハート
			cards[i + 26] = 300 + i;

			// スペード
			cards[i + 39] = 400 + i;
		}
	}

	public void shuffle()
	{
		for(int i = 0; i < cards.length; i++)
		{
			int rum = (int)(Math.random() * (double)(cards.length));

			int swap = cards[i];
			cards[i] = cards[rum];
			cards[rum] = swap;
		}
	}

	public int getCard(int num)
	{
		if(num < 1 || num > 53)
		{
			return -1;
		}

		return cards[num - 1];
	}
}

class Porker
{
	// エラー
	static final int ERROR = 0;

	// ロイヤルストレートフラッシュ
	static final int ROYAL_FLUSH = 1;
	// ストレートフラッシュ
	static final int STRAIGHT_FLUSH = 2;
	// フォーカード
	static final int FOUR_OF_A_KIND = 3;
	// フルハウス
	static final int FULL_HOUSE = 4;
	// フラッシュ
	static final int FLUSH = 5;
	// ストレート
	static final int STRAIGHT = 6;
	// スリーカード
	static final int THREE_OF_A_KIND = 7;
	// ツーペア
	static final int TWO_PAIR = 8;
	// ワンペア
	static final int ONE_PAIR = 9;
	// ノーペア
	static final int NO_PAIR = 10;

	public int judgeHand(int[] cards)
	{
		// 配列がnullのときエラー
		if(cards == null)
			return ERROR;

		// 配列の格納数が5でないときエラー
		if(cards.length != 5)
			return ERROR;

		
		return 0;
	}
}