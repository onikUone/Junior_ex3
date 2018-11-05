package main;

/* FuzzySetTriangle クラス
 *     これは定数クラスとして扱う．
 *     import static main.FuzzyConst.*;
 *     で，TriRuleが使えるようになる．
 *
 */

public class FuzzySetTriangle {
	public static final int n_rule = 3;
	public static final int[][] TriRule = new int[1 + n_rule][2];

	static {
		TriRule[0][0] = 0; TriRule[0][1] = 0;
		TriRule[1][0] = 1; TriRule[1][1] = 3;	//small k = 1, K = 3
		TriRule[2][0] = 2; TriRule[2][1] = 3;	//medium k = 2, K = 3
		TriRule[3][0] = 3; TriRule[3][1] = 3;	//large k = 3, K = 3
	}

	//constractor
	private FuzzySetTriangle() {}
}
