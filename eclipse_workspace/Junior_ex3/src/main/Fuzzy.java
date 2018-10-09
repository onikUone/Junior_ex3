package main;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Fuzzy {
//初期値パラメータ
	int n;	//学習データの属性数;次元数
	int classNumber;	//学習データのクラス数
	int m;	//学習データのパターン数
	double[][] x;	//学習データの入力パターン
	int[] classof_x;	//学習パターンの教師ラベル
	int K = 3;	//ファジィ集合分割数;
	String inputPath;	//読み込みパス
	String outputPath;	//書き出しパス

		// Fuzzy rule 関係
	int[] result;	// ルール結論部クラス	(result[全ルール数])
	double[] weight;	//ルール重み	(weight[全ルール数])

	int h = 500;	//テストパターン刻み幅
	double[][] test_X;	//テストパターン
	int[] test_Class;	//テストパターン_識別結果
	double[][][] testFit;	//テストパターン_適合度


	//constructor
	Fuzzy(String inputPath, String outputPath){
		this.inputPath = inputPath;
		this.outputPath = outputPath;

	}

//ファイル読み込みメソッド
	public double[][] readFile(String path) throws IOException{
		List<String[]> list = new ArrayList<String[]>();
		BufferedReader in = new BufferedReader(new FileReader(path));
		String line;
		while((line = in.readLine()) != null){
			list.add(line.split(", "));
		}
		in.close();
		double[][] x = new double[list.size()][list.get(0).length];
		for(int i=0; i<list.size(); i++){
			for(int j=0; j<x[i].length; j++) {
				x[i][j] = Double.parseDouble(list.get(i)[j]);
			}
		}
		return x;
	}

//メンバシップ関数
	public double memberShip(double x, int K, int k){	//This means the Fuzzy Set
	//使い方
	//	memberShip(x, ファジィ分割数, 選択するファジィ集合番号)
	//k: 0=don't care, 1=small, 2=medium, 3=large;
	//K: ファジィ集合分割数;
		double a = (k - 1.0)/(K - 1.0);
		double b = 1.0/(K - 1.0);

		if(k == 0) {
			return 1.0;	//don't care
		}else {
			return Math.max( 1 - Math.abs(a - x)/b , 0.0);
		}
	}

//識別器メソッド
/*
 * 引数に渡されたデータセット X[データ数][次元数]の
 * ファジィルールによる推論
 * クラスの結果を result[データ数] の形でreturnする
 *
 * フラグ
 * flg = 0 : ファジィルールの学習
 * 			 return: ファジィルールの結論部
 * flg = 1 : 入力データの推論
 * 			 return: 入力データを推論して得たクラス結果
 * */
	public int[] classifier(double X[][], int flg) {
//		// 計算用変数初期化
		if(flg == 0) {
			weight = new double[(int)Math.pow(K+1, n)];
			result = new int[(int)Math.pow(K+1, n)];
		}
		// ローカル変数
		double[][] fit = new double[X.length][(int)Math.pow(K+1, n)];
		double[][] trust = new double[(int)Math.pow(K+1, n)][classNumber];
		int[] class_ofX = new int[X.length];
		double temp1,temp2,comp;
		int comp_flg;
		int ruleNumber;	//ルール数
		int ruleLength;	//総ルール長

		//適合度計算
		for(int i=0; i < K+1; i++) {
			for(int j=0; j < K+1; j++) {
				if(flg != 0 && weight[i*(K+1) + j] <= 0) {	//識別の際、生成不可能ルールでは計算しない
					continue;
				}
				for(int p=0; p < X.length; p++) {
					fit[p][i*(K+1) + j] = 1.0;
					for(int k=0; k < n; k++) {
						if(k==0) {
							fit[p][i*(K+1) + j] *= memberShip(X[p][k], K, i);
						}
						else {
							fit[p][i*(K+1) + j] *= memberShip(X[p][k], K, j);
						}
					}
				}
			}
		}
		//信頼度計算
		for(int i=0; i < K+1; i++) {
			if(flg != 0) {
				break;
			}
			for(int j=0; j < K+1; j++) {
				for(int k=0; k < classNumber; k++) {
					temp1 = 0.0;
					temp2 = 0.0;
					for(int p=0; p < m; p++) {
						if(classof_x[p] == k) {
							temp1 += fit[p][i*(K+1) + j];
						}
						temp2 += fit[p][i*(K+1) + j];
					}
					trust[i*(K+1) + j][k] = temp1/temp2;
				}
			}
		}
		//ルール結論部クラス決定
		for(int i=0; i < K+1; i++) {
			if(flg != 0) {
				break;
			}
			for(int j=0; j < K+1; j++) {
				result[i*(K+1) + j] = 0;
				comp = trust[i*(K+1) + j][0];
				for(int k=1; k < classNumber; k++) {
					if(comp < trust[i*(K+1) + j][k]) {	//信頼度が一番大きいクラスを結論部として決定する
						comp = trust[i*(K+1) + j][k];
						result[i*(K+1) + j] = k;
					}
				}
			}
		}
		//ルール重み計算
		for(int i=0; i < K+1; i++) {
			if(flg != 0) {
				break;
			}
			for(int j=0; j < K+1; j++) {
				temp1 = 0.0;
				for(int k=0; k < classNumber; k++) {
					if(result[i*(K+1) + j] != k) {
						temp1 += trust[i*(K+1) + j][k];
					}
				}
				weight[i*(K+1) + j] = trust[i*(K+1) + j][result[i*(K+1) + j]];
				weight[i*(K+1) + j] -= temp1;
			}
		}

		if(flg == 0) {	// 学習フラグ
			//ルール数・総ルール長出力
			ruleNumber = 0;
			ruleLength = 0;
			for(int i=0; i < K+1; i++) {
				for(int j=0; j < K+1; j++) {
					if(weight[i*(K+1) + j] > 0) {
						ruleNumber++;
						if(i != 0) {
							ruleLength++;
						}
						if(j != 0) {
							ruleLength++;
						}
					}
				}
			}
			System.out.println("RuleNumber: " + ruleNumber);
			System.out.println("RuleLength: " + ruleLength);
			return result;
		}
		else {	//推論フラグ

			// 適合度に重みを付与
			for(int i=0; i < K+1; i++) {
				for(int j=0; j < K+1; j++) {
					if(weight[i*(K+1) + j] <= 0) {
						continue;
					}
					for(int p=0; p < X.length; p++) {
						fit[p][i*(K+1) + j] *= weight[i*(K+1) + j];

					}
				}
			}
			//推論クラス決定
			for(int p=0; p < X.length; p++) {
				comp_flg = 0;
				comp = 0.0;
				for(int i=0; i < K+1; i++) {
					for(int j=0; j < K+1; j++) {
						if(weight[i*(K+1) + j] <= 0) {	//生成不可能ルールでは計算しない
							continue;
						}
						if(comp_flg == 0) {
							comp_flg = 1;
							comp = fit[p][i*(K+1) + j];
							class_ofX[p] = result[i*(K+1) + j];
						}else if(comp == fit[p][i*(K+1) + j]) {
							class_ofX[p] = -1;	//識別不能パターン
						}else if(comp < fit[p][i*(K+1) + j]) {
							comp = fit[p][i*(K+1) + j];
							class_ofX[p] = result[i*(K+1) + j];
						}
					}
				}
			}
			return class_ofX;

		}
	}

//fuzzyメソッド
/*
 * Fuzzyインスタンスに指定されているPathにしたがって、
 * 		Fuzzyルールの生成    (ruleFit → ruleResult)
 * 		未知パターンの推論
 * 		境界点の書き出し
 * 	を行う
 *
 * */
	public void fuzzy() throws IOException {
	//ファイル読み込み
		double[][] inputFile = readFile(inputPath);
		m = (int)inputFile[0][0];
		n = (int)inputFile[0][1];
		classNumber = (int)inputFile[0][2];

		//学習データセット取得
		x = new double[m][n];	//x[学習パターン数][次元数]
		classof_x = new int[m];	//classof_x[学習パターン数]
		for(int i=0; i< x.length; i++){
			for(int j=0; j<x[i].length; j++) {
				x[i][j] = inputFile[i+1][j];
			}
		}
		for(int i=0; i<classof_x.length; i++) {
			classof_x[i] = (int)inputFile[i+1][n];
		}

	//Fuzzy Rule 生成
		System.out.println("Create Fuzzy Rule.");
		classifier(x,0);	//Fuzzy Rule 生成
		System.out.println("Created Fuzzy Rule.");

		//識別率計算
		double recogRate = 0.0;
		int[] recog_Class = classifier(x, 1);		//識別率計算用	学習データ識別クラス
		for(int p=0; p < m; p++) {
			if(classof_x[p] == recog_Class[p]) {
				recogRate++;
			}
		}

		System.out.println("RecogRate: " + recogRate/m *100);


	//未知パターン推論
		//テストパターン生成
		test_X = new double[h*h][n];
		test_Class = new int[h*h];
		testFit = new double[h*h][K+1][K+1];
		for(int i=0; i<h; i++) {
			for(int j=0; j<h; j++) {//走査方向 : 縦
				test_X[i*h+j][0] = (double)i/h;
				test_X[i*h+j][1] = (double)j/h;
			}
		}
		//テストパターン推論
		test_Class = classifier(test_X, 1);

	//境界点書き出し
		//境界点 書き出し
		PrintWriter outPrint = new PrintWriter(new BufferedWriter(new FileWriter(outputPath)));
		int comClass = -1;
		for(int i=0; i<h; i++) {
			for(int j=0; j<h; j++) {
				if(j == 0) {
					comClass = test_Class[i*h];
				}
				else if(comClass != test_Class[i*h+j]){
					comClass = test_Class[i*h+j];
					for(int k=0; k < n; k++) {
						outPrint.write(String.valueOf(test_X[i*h+j][k]));
						outPrint.write("\t");
					}
					outPrint.println("");
				}
			}
		}
		outPrint.close();
	}

}
