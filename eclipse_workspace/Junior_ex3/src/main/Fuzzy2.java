package main;

public class Fuzzy2 {

	int attribute;
	int n_class;
	int n_pattern;

	double[][] x;	//学習データ入力パターン
	int[] y;		//学習データ教師信号
	int K = 3;

	String inputPath;

	//Fuzzy rule
	int[] result;
	double[] weight;
	int[] ruleFlg;


}
