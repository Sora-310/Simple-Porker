// トランプを操作するクラス
class CardsManager {
	// トランプ配列
	private int[] cards;

	//  配列の値
	//  000 : ジョーカー
	//  101～113 : スペードの1～13
	//  201～213 : ハートの1～13
	//  301～313 : ダイヤの1～13
	//  401～413 : クラブの1～13

	// コンストラクタ
	public CardsManager()
	{
		// 配列を作成
		cards = new int[ 13 * 4 + 1 ];

		// 初期化
		organize();
	}


	// トランプの整列
	public void organize()
	{
		// ジョーカーを格納
		cards[ 0 ] = 0;

		for ( int i = 1; i <= 13; ++ i ) {
			// スペードを格納(添え字:1～13)
			cards[ i ] = 100 + i;

			// ダイヤを格納(添え字:14～26)
			cards[ i + 13 ] = 300 + i;

			// クラブを格納(添え字:27～39)
			cards[ i + 26 ] = 400 + i;

			// ハートを格納(添え字:40～52)
			cards[ i + 39 ] = 200 + i;
		}
	}


	// トランプのシャッフル
	public void shuffle()
	{
		for ( int i = 0; i < cards.length; ++ i ) {
			// 0～(配列aryの個数-1)の乱数を発生
			int rnd = (int)( Math.random() * (double)cards.length );

			// cards[ i ]とcards[ rnd ]を入れ替える
			int w = cards[ i ];
			cards[ i ] = cards[ rnd ];
			cards[ rnd ] = w;
		}
	}


	// トランプの取得
	public int getCard( int num )
	{
		// numが、1～53番目以外の場合、-1を戻す
		if ( ( 1 > num ) || ( 53 < num ) ) return -1;

		// カードを戻す
		return cards[ num - 1 ];
	}
}


// メイン
public class PokerHands {
	// エラー
	static final int ERROR           = 0;

	// ポーカーの役の定数を定義しています。
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


	// カード５枚を配列で渡して役を判定するメソッド
	static int getPockerHand( int[] cards )
	{
		// 配列がnullだったらエラー
		if ( null == cards ) return ERROR;

		// 配列の個数が5でなければエラー
		if ( 5 != cards.length ) return ERROR;


		// トランプのマークの個数を格納する配列
		int[] suit = new int[ 4 ];

		// トランプのマークの個数に0を代入（初期化）
		for ( int i = 0; i < suit.length; i++ )
			suit[ i ] = 0;


		// トランプの番号(1-13)の個数を格納する配列
		// number[ 0 ]とnumber[13]の両方にAの個数を格納
		int[] number = new int[ 14 ];

		// トランプのマークの個数に0を代入（初期化）
		for ( int i = 0; i < number.length; i++ )
			number[ i ] = 0;

		// ５枚のカードのマークと番号の個数を格納
		for ( int i = 0; i < cards.length; i++ ) {
			// マーク
			int mark = cards[ i ] / 100;
			// 番号
			int num = cards[ i ] % 100;

			// markが1から4の範囲外であればエラー
			if ( ( 1 > mark ) || ( 4 < mark ) ) {
				return ERROR;
			}

			// numが1から13の範囲外であればエラー
			if ( ( 1 > num ) || ( 13 < num ) ) {
				return ERROR;
			}

			// マークの個数に１を足す
			++ suit[ mark - 1 ];

			// 番号の個数に１を足す
			++ number[ num - 1 ];
		}
		// number[ 13 ]にAの個数を代入
		number[ 13 ] = number[ 0 ];

		// 番号の個数の最大値を取得
		int number_max = 0;
		for ( int i = 0; i < number.length - 1; i++ ) {
			if ( number_max < number[ i ] )
				number_max = number[ i ];
		}

		// ここから判定処理

		// 個数の最大が４の場合、フォーカード確定
		if ( 4 == number_max )
			return FOUR_OF_A_KIND;

		
		// マークの個数の最大値を取得
		// ５枚のカードが同じマークの場合、suit_max=5となる
		int suit_max = 0;
		for ( int i = 0; i < suit.length; i++ ) {
			if ( suit_max < suit[ i ] )
				suit_max = suit[ i ];
		}

		// ストレートの判定
		boolean isStraight = false;
		int continuous1 = 0;
		int firstnum = 0;
		for ( int i = 0; i < number.length; ++ i ) {
			if ( 1 != number[ i ] ) {
				continuous1 = 0;
				firstnum = 0;
			}
			else {
				++ continuous1;
				// ストレートの最初の番号を格納
				if ( 1 == continuous1 )
					firstnum = i + 1;

				// ５回連続だったら
				if ( 5 == continuous1 ) {
					// ストレートは確定
					isStraight = true;
					break;
				}
			}
		}

		// マークが全て同じで、ストレートだったら
		if ( ( 5 == suit_max ) && isStraight ) {
			// ストレートの先頭の番号が10だったら
			if ( 10 == firstnum ) {
				// ロイヤルフラッシュ確定
				return ROYAL_FLUSH;
			}
			// ストレートフラッシュ確定
			return STRAIGHT_FLUSH;
		}

		// 個数の最大が３で、もう１つペアが存在したら
		if ( 3 == number_max ) {
			for ( int i = 0; i < number.length - 1; ++ i ) {
				if ( 2 == number[ i ] ) {
					// フルハウス確定
					return FULL_HOUSE;
				}
			}
		}


		// マークが全て同じだったら
		if ( 5 == suit_max ) {
			// フラッシュ確定
			return FLUSH;
		}

		// ストレートだったら
		if ( isStraight ) {
			// ストレート確定
			return STRAIGHT;
		}

		// 個数の最大が３の場合、スリーカード確定
		if ( 3 == number_max )
			return THREE_OF_A_KIND;

		// ペアの個数
		int pair_num = 0;
		for ( int i = 0; i < number.length - 1; ++ i ) {
			if ( 2 == number[ i ] )
				++ pair_num;
		}

		// ペアが２つであれば
		if ( 2 == pair_num )
			return TWO_PAIR;

		// ペアが１つであれば
		if ( 1 == pair_num )
			return ONE_PAIR;

		// ノーペア
		return NO_PAIR;
	}


	// 番号を文字に変換
	static String getStringNumber( int num ) {
		switch ( num ) {
			case 1:
				return "A";
			case 2:
				return "2";
			case 3:
				return "3";
			case 4:
				return "4";
			case 5:
				return "5";
			case 6:
				return "6";
			case 7:
				return "7";
			case 8:
				return "8";
			case 9:
				return "9";
			case 10:
				return "10";
			case 11:
				return "J";
			case 12:
				return "Q";
			case 13:
				return "K";
		}
		return "";
	}


	// メイン
	public static void main( String[] args ) {
		// トランプクラスを作成
		CardsManager cards = new CardsManager();

		// 変数を宣言
		int[] cards5 = new int[ 5 ];

		// 200回処理
		int loopcount = 200;
		for ( int j = 0; j < loopcount; ++ j ) {
			// トランプをシャッフル
			cards.shuffle();

			// トランプを上から順番に引いていく
			int card_num = 0;
			for ( int i = 1; i <= 53; ++ i ) {
				// i番目のカードを取得
				int card = cards.getCard( i );

				// ジョーカーだったら配列に入れない
				if ( 0 == card ) continue;

				// カードが５枚になったらループを抜ける
				cards5[ card_num] = card;
				++ card_num;
				if ( 5 == card_num ) break;
			}


			// カードを表示
			for ( int i = 0; i < 5; ++ i ) {
				// マーク
				int mark = cards5[ i ] / 100;
				// 番号
				int num = cards5[ i ] % 100;

				// 番号を文字列に変更
				String strnum = getStringNumber( num );

				//　表示
				switch ( mark ) {
					case 1:
						// スペード
						System.out.print( "S" + strnum );
						break;
					case 2:
						// ハート
						System.out.print( "H" + strnum );
						break;
					case 3:
						// ダイヤ
						System.out.print( "D" + strnum );
						break;
					case 4:
						// クラブ
						System.out.print( "C" + strnum );
						break;
				}
				System.out.print( " " );
			}

			// 役を判定
			int hand = getPockerHand( cards5 );
			switch ( hand ) {
				case ROYAL_FLUSH:
					System.out.println( "ロイヤルフラッシュ" );	
					break;

				case STRAIGHT_FLUSH:
					System.out.println( "ストレートフラッシュ" );	
					break;

				case FOUR_OF_A_KIND:
					System.out.println( "フォーカード" );	
					break;

				case FULL_HOUSE:
					System.out.println( "フルハウス" );	
					break;

				case FLUSH:
					System.out.println( "フラッシュ" );	
					break;

				case STRAIGHT:
					System.out.println( "ストレート" );	
					break;

				case THREE_OF_A_KIND:
					System.out.println( "スリーカード" );	
					break;

				case TWO_PAIR:
					System.out.println( "ツーペア" );	
					break;

				case ONE_PAIR:
					System.out.println( "ワンペア" );
					break;

				default:
					System.out.println( "ノーペア" );
					break;
			}
		}
	}
}