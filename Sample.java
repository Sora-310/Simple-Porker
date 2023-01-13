import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.applet.*;

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

	class TitlePanel extends JPanel
	{
		private JLabel titleName;
		private JButton startButton;
		private JButton endButton;
		private AudioClip select_sou;

		public TitlePanel()
		{
			setLayout(null);

			// 選択音を用意
			select_sou = Applet.newAudioClip(getClass().getResource("select_sou.wav"));

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
					select_sou.play();
					((CardLayout)(cardPanel.getLayout())).show(cardPanel, "Play");	// プレイ画面に遷移
				}
			});
			add(startButton);

			// 終了ボタン
			endButton = new JButton("終了");
			endButton.setBounds(20, 310, 80, 40);
			endButton.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					select_sou.play();					
					System.exit(0);	// プログラムを終了
				}
			});	
			add(endButton);
		}
	}

	class PlayPanel extends JPanel
	{
		private CardManager cards;
		private JLabel sentence;
		private JLabel arrow[] = new JLabel[5];
		private JButton endButton;
		private JButton judgeButton;
		private JButton allSelectButton;
		private JButton image[] = new JButton[5];
		private ImageIcon arrowIcon;
		private AudioClip select_sou;
		private AudioClip fanfare_sou;
		private int[] cards5 = new int[5];
		private boolean isJudge;
		private boolean isAll;
		private boolean[] choise = new boolean[5];

		public PlayPanel()
		{
			// 下準備

			setLayout(null);

			// トランプのオブジェクトを生成
			cards = new CardManager();
			cards.drowHand(cards5);

			// 判定ボタンの機能切り替えを初期化
			isJudge = true;
			isAll = true;

			// 矢印の画像を用意
			arrowIcon = new ImageIcon("images\\arrow.png");
			Image img = arrowIcon.getImage();
			Image newimg = img.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
			arrowIcon = new ImageIcon(newimg);

			// 選択判断用配列を初期化
			for(int i = 0; i < 5; i++)
			{
				choise[i] = false;
			}

			// 選択音を用意
			select_sou = Applet.newAudioClip(getClass().getResource("select_sou.wav"));
			fanfare_sou = Applet.newAudioClip(getClass().getResource("fanfare.wav"));



			// ここから表示関連

			// 指示文
			sentence = new JLabel("ホールドするカードを選択してください");
			sentence.setFont(new Font("MS ゴシック", Font.PLAIN, 20));
			sentence.setBounds(160, 20, 400, 60);
			add(sentence);

			// 終了ボタン
			endButton = new JButton("終了");
			endButton.setBounds(20, 310, 80, 40);
			endButton.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					select_sou.play();
					System.exit(0);	// プログラムを終了
				}
			});
			add(endButton);

			// 判定ボタン
			judgeButton = new JButton("ＯＫ");
			judgeButton.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					select_sou.play();
					if(isJudge)
					{
						for(int i = 0; i < 5; i++)
						{
							// 選択していない時、カードを交換
							if(!choise[i])
							{
								remove(image[i]);
								repaint();
								
								// カードを交換して表示
								cards5[i] = cards.drowCard(i + 7);
								image[i] = new JButton(imageResize(i));
								image[i].addActionListener(new CardActionListener());
								image[i].setBounds((120 * i) + 60, 105, 90, 120);
								add(image[i]);
							}
							else	// そうでない時、矢印を消す
							{
								choise[i] = false;
								setArrow(i, false);
							}
						}
	
						// 判定を表示
						int judge_num = Porker.judgeHand(cards5);
						if(judge_num > 0 && judge_num < 7)	// ストレート以上の時、音を鳴らす
						{
							fanfare_sou.play();
						}
						sentence.setText(Porker.getStringHand(judge_num));

						// ボタンの文を変更
						judgeButton.setText("再挑戦");

						// ボタンの機能を切り替え
						isJudge = false;
					}
					else
					{
						// 表示されているカードを削除
						for(int i = 0; i < 5; i++)
						{
							cards5[i] = 0;
							remove(image[i]);
						}
						repaint();

						// 選択判断用配列を初期化
						for(int i = 0; i < 5; i++)
						{
							choise[i] = false;
							setArrow(i, false);
						}

						// 文を元に戻す
						sentence.setText("ホールドするカードを選択してください");

						// 引き直したカードを表示
						cards.drowHand(cards5);
						for(int i = 0; i < 5; i++)
						{
							image[i] = new JButton(imageResize(i));
							image[i].addActionListener(new CardActionListener());
							image[i].setBounds((120 * i) + 60, 105, 90, 120);
							add(image[i]);
						}

						// ボタンの文を変更
						judgeButton.setText("ＯＫ");

						// ボタンの機能を切り替え
						isJudge = true;
					}
				}
			});
			judgeButton.setBounds(275, 280, 140, 70);
			add(judgeButton);

			// 全選択ボタン
		allSelectButton = new JButton("全選択：OFF");
		allSelectButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if(isAll)
				{
					for(int i = 0; i < 5; i++)
					{
						if(!choise[i])
						{
							choise[i] = true;
							setArrow(i, choise[i]);
						}
					}

					allSelectButton.setText("全選択：ON");
					isAll = false;
				}
				else
				{
					for(int i = 0; i < 5; i++)
					{
						if(choise[i])
						{
							choise[i] = false;
							setArrow(i, choise[i]);
						}
					}
					
					allSelectButton.setText("全選択：OFF");
					isAll = true;
				}
			}
		});
		allSelectButton.setMargin(new Insets(5, 10, 5, 10));
		allSelectButton.setBounds(450, 300, 100, 50);
		add(allSelectButton);

			// カード5枚
			for(int i = 0; i < 5; i++)
			{
				// 最初に画像のサイズを調整
				image[i] = new JButton(imageResize(i));
				image[i].addActionListener(new CardActionListener());
				image[i].setBounds((120 * i) + 60, 105, 90, 120);
				add(image[i]);
			}

			// 矢印5個（最初は非表示）
			for(int i = 0; i < 5; i++)
			{
				arrow[i] = new JLabel();
				arrow[i].setBounds((120 * i) + 55, 195, 100, 100);
				add(arrow[i]);
			}
		}

		// カードの画像のサイズを調節し、返す
		public ImageIcon imageResize(int num)
		{
			// 画像を取得
			ImageIcon icon = new ImageIcon("images\\" + Integer.toString(cards5[num]) + ".png");
			
			// Imageクラスを用いてサイズを調整
			Image img = icon.getImage();
			Image newimg = img.getScaledInstance(90, 120, Image.SCALE_SMOOTH);
			icon = new ImageIcon(newimg);
			
			return icon;
		}

		// 矢印の表示を更新
		public void setArrow(int n, boolean choise)
		{
			// 表示されている矢印を削除
			remove(arrow[n]);
			repaint();

			// 選択されている時、矢印を表示
			if(choise)
			{
				arrow[n] = new JLabel(arrowIcon);
			}
			else	// そうでなければ、非表示
			{
				arrow[n] = new JLabel();
			}
			arrow[n].setBounds((120 * n) + 55, 195, 100, 100);
			add(arrow[n]);
		}

		// トランプを選択した時の動作
		class CardActionListener implements ActionListener
		{
			private AudioClip select_sou = Applet.newAudioClip(getClass().getResource("select_sou.wav"));
			
			public void actionPerformed(ActionEvent e)
			{
				select_sou.play();

				for(int i = 0; i < 5; i++)
				{
					if(e.getSource() == image[i])
					{
						// ボタンを押す度に切り替わる
						if(choise[i] == false)
						{
							choise[i] = true;
							setArrow(i, choise[i]);
						}
						else
						{
							choise[i] = false;
							setArrow(i, choise[i]);
						}
					}
				}
			}
		}
	}
}

class CardManager
{
	// 山札
	private int[] cards;
	
	/*
		カードの配役
		000 ： ジョーカー
		101～113 ： クローバーの1～13
		201～213 ： ダイヤの1～13
        301～313 ： ハートの1～13
		401～413 ： スペードの1～13
	*/

	public CardManager()
	{
		// 山札の作成
		cards = new int[53];
		initialize();
	}

	// トランプの初期化
    public void initialize()
	{
		// ジョーカーを格納
		cards[0] = 0;

		// 他のカードを格納
		for(int i = 1; i < 14; i++)
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

	// シャッフル
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

	// 山札から1枚引く
	public int drowCard(int num)
	{
		// 1～53でない時、エラー
		if(num < 1 || num > 53)
		{
			return -1;
		}

		// ジョーカーを引いたとき、11枚目を引く
		if(cards[num - 1] == 0)
		{
			num += 12 - num;
		}

		return cards[num - 1];
	}

	// 山札をシャッフルして、5枚引く
	public void drowHand(int[] cards5)
	{
		shuffle();

		int count = 0;
		for(int i = 1; i < 6; i++)
		{
			// カードを取得
			int card = drowCard(i);

			// ジョーカーは手札に入れない
			if(card == 0)
			{
				continue;
			}

			cards5[count] = card;
			count++;

			// 手札が5枚になったらループを終了
			if(count == 5)
			{
				break;
			}
		}
	}
}

class Porker
{
	// エラー
	static final int ERROR = 0;

	// ロイヤルフラッシュ（ロイヤルストレートフラッシュ）
	static final int ROYAL_FLUSH     = 1;
	// ストレートフラッシュ
	static final int STRAIGHT_FLUSH  = 2;
	// フォーカード
	static final int FOUR_OF_A_KIND  = 3;
	// フルハウス
	static final int FULL_HOUSE      = 4;
	// フラッシュ
	static final int FLUSH           = 5;
	// ストレート
	static final int STRAIGHT        = 6;
	// スリーカード
	static final int THREE_OF_A_KIND = 7;
	// ツーペア
	static final int TWO_PAIR        = 8;
	// ワンペア
	static final int ONE_PAIR        = 9;
	// ノーペア
	static final int NO_PAIR         = 10;

	// 手札の役を判定
	public static int judgeHand(int[] cards)
	{
		// 判定前の下準備

		// 配列がnullのときエラー
		if(cards == null)
		{
			return ERROR;
		}

		// 配列の格納数が5でないときエラー
		if(cards.length != 5)
		{
			return ERROR;
		}

		// トランプのマークの個数を格納する配列
		int[] suits = new int[4];
		// 初期化
		for(int i = 0; i < suits.length; i++)
		{
			suits[i] = 0;
		}

		// トランプの数字の個数を格納する配列
		// 配列の0と13は数字のAの個数
		int[] numbers = new int[14];
		// 初期化
		for(int i = 0; i < numbers.length; i++)
		{
			numbers[i] = 0;
		}

		// 5枚のカードのマークと数字をそれぞれ格納
		for(int i = 0; i < cards.length; i++)
		{
			int suit = cards[i] / 100;
			int num = cards[i] % 100;

			if(suit < 1 || suit > 4)
			{
				return ERROR;
			}
			
			if(num < 1 || num > 13)
			{
				return ERROR;
			}
			
			suits[suit - 1]++;
			numbers[num - 1]++;
		}
		numbers[13] = numbers[0];

		// 数字の最大個数を取得
		int num_max = 0;
		for(int i = 0; i < numbers.length - 1; i++)
		{
			if(num_max < numbers[i])
			{
				num_max = numbers[i];
			}
		}

		// マークの最大個数を取得
		int suit_max = 0;
		for(int i = 0; i < suits.length; i++)
		{
			if(suit_max < suits[i])
			{
				suit_max = suits[i];
			}
		}



		// 判定はここから

		// 最大個数が4の時、フォーカード確定
		if(num_max == 4)
		{
			return FOUR_OF_A_KIND;
		}

		// ストレートかどうかの判定
		boolean isStraight = false;
		int continuous = 0;  // 連続して数字が並んだ回数
		int num_first = 0;   // ストレートの最初の数字
		for(int i = 0; i < numbers.length; i++)
		{
			if(numbers[i] != 1)
			{
				continuous = 0;
				num_first = 0;
			}
			else
			{
				continuous++;
				if(continuous == 1)  // 連続した回数が1の時、最初の数字を取得
				{
					num_first = i + 1;
				}

				if(continuous == 5)	 // 連続した回数が5の時、ストレート確定
				{
					isStraight = true;
					break;
				}
			}
		}
		
		// マークが全て同じで、且つストレートの時
		if(suit_max == 5 && isStraight)
		{
			if(num_first == 10)  // 更にストレートが10から始まっている時
			{
				// ロイヤルストレートフラッシュ確定
				return ROYAL_FLUSH;	
			}
			
			// ストレートフラッシュ確定
			return STRAIGHT_FLUSH;	
		}

		// 最大個数が3で、且つペアが1つある時
		if(num_max == 3)
		{
			for(int i = 0; i < numbers.length - 1; i++)
			{
				if(numbers[i] == 2)
				{
					// フルハウス確定
					return FULL_HOUSE;
				}
			}
		}

		// マークが全て同じの時
		if(suit_max == 5)
		{
			// フラッシュ確定
			return FLUSH;
		}

		// ストレートの時
		if(isStraight)
		{
			// ストレート確定
			return STRAIGHT;
		}

		// 最大個数が3の時
		if(num_max == 3)
		{
			return THREE_OF_A_KIND;
		}

		// ペアの個数
		int num_pair = 0;	
		for(int i = 0; i < numbers.length; i++)
		{
			if(numbers[i] == 2)
			{
				num_pair++;
			}
		}

		// ペアが2つの時
		if(num_pair == 2)
		{
			// ツーペア確定
			return TWO_PAIR;
		}

		// ペアが1つの時
		if(num_pair == 1)
		{
			// ワンペア確定
			return ONE_PAIR;
		}

		// どれにも該当しない時、ノーペア確定
		return NO_PAIR;
	}

	static String getStringHand(int num)
	{
		switch(num)
		{
			case ROYAL_FLUSH:
				return "ロイヤルストレートフラッシュ";

			case STRAIGHT_FLUSH:
				return "ストレートフラッシュ";

			case FOUR_OF_A_KIND:
				return "フォーカード";
			
			case FULL_HOUSE:
				return "フルハウス";
		
			case FLUSH:
				return "フラッシュ";
			
			case STRAIGHT:
			    return "ストレート";
			
			case THREE_OF_A_KIND:
				return "スリーカード";
			
			case TWO_PAIR:
				return "ツーペア";
		
			case ONE_PAIR:
				return "ワンペア";			
			
			default:
				return "ノーペア";
		}
	}
}
