import java.io.File;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.sound.sampled.*;

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
	private Clip select_sou;
	private Clip fanfare_sou;
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

		// 各ボタンの機能切り替えを初期化
		isJudge = true;
		isAll = true;

		// 矢印の画像を用意
		arrowIcon = new ImageIcon("images\\arrow.png");
		Image img = arrowIcon.getImage();
		Image newimg = img.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
		arrowIcon = new ImageIcon(newimg);
		
		// 矢印用のJLabelを5枚用意し、設置
		for(int i = 0; i < 5; i++)
		{
			arrow[i] = new JLabel();
			arrow[i].setBounds((120 * i) + 55, 195, 100, 100);
			add(arrow[i]);
		}

		// 選択判断用配列を初期化
		for(int i = 0; i < 5; i++)
		{
			choise[i] = false;
		}

		// 選択音を用意
		select_sou = SoundManager.createClip(new File("sounds\\select_sou.wav"));
		fanfare_sou = SoundManager.createClip(new File("sounds\\fanfare.wav"));



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
				SoundManager.playback(select_sou);
				System.exit(0);	// プログラムを終了
			}
		});
		add(endButton);

		// 判定ボタン
		judgeButton = new JButton("ＯＫ");
		judgeButton.addActionListener(new JudgeActionListener());
		judgeButton.setBounds(275, 280, 150, 70);
		add(judgeButton);

		// 全選択ボタン
		allSelectButton = new JButton("全選択：OFF");
		allSelectButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				SoundManager.playback(select_sou);
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
			image[i] = makeCard(i);
			add(image[i]);
		}
	}

	// カードを1枚表示
	private JButton makeCard(int num)
	{
		// 画像を取得
		ImageIcon icon = new ImageIcon("images\\" + Integer.toString(cards5[num]) + ".png");
		
		// Imageクラスを用いてサイズを調整
		Image img = icon.getImage();
		Image newimg = img.getScaledInstance(90, 120, Image.SCALE_SMOOTH);
		icon = new ImageIcon(newimg);

		// カードを定義
		JButton button = new JButton(icon);
		button.addActionListener(new CardActionListener());
		button.setBounds((120 * num) + 60, 105, 90, 120);
		return button;
	}

	// 矢印の表示を更新
	private void setArrow(int n, boolean choise)
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
		public void actionPerformed(ActionEvent e)
		{
			SoundManager.playback(select_sou);

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

					break;
				}
			}
		}
	}

	class JudgeActionListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			SoundManager.playback(select_sou);
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
						image[i] = makeCard(i);
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
					SoundManager.playback(fanfare_sou);
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
					image[i] = makeCard(i);
					add(image[i]);
				}

				// ボタンの文を変更
				judgeButton.setText("ＯＫ");

				// ボタンの機能を切り替え
				isJudge = true;
			}
		}
	}
}

