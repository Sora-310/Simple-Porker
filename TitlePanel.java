import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

import javax.sound.sampled.*;;

class TitlePanel extends JPanel
{
	private JLabel titleName;
	private JButton startButton;
	private JButton endButton;
	private Clip select_sou;

	public TitlePanel()
	{
		setLayout(null);

		// 選択音を用意
		select_sou = SoundManager.createClip(new File("sounds\\select_sou.wav"));

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
				SoundManager.playback(select_sou);

				// プレイ画面に遷移
				JPanel cardPanel = (JPanel)getParent();
				((CardLayout)(cardPanel.getLayout())).show(cardPanel, "Play");	
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
				SoundManager.playback(select_sou);				
				System.exit(0);	// プログラムを終了
			}
		});	
		add(endButton);
	}
}