class CardManager {
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
