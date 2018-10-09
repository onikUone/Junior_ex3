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

	double temp1;
	double temp2;
	double comp;
	int comp_flg;
	double[][][] ruleFit;	//ルール条件部
	int[][] ruleResult;		//ルール結論部クラス
	double[][][] trust;	//ルール信頼度
	double[][] ruleWeight;	//ルール重み

	int h = 500;	//テストパターン刻み幅
	double[][] test_X;	//テストパターン
	int[] test_Class;	//テストパターン_識別結果
	double[][][] testFit;	//テストパターン_適合度

	double recogRate;	//識別率
	double[][][] recogFit;	//識別率計算用
	int[] recog_Class;		//識別率計算用	学習データ識別クラス
	int ruleNumber;	//ルール数
	int ruleLength;	//総ルール長

	//constructor
	Fuzzy(String inputPath, String outputPath){
		this.inputPath = inputPath;
		this.outputPath = outputPath;

	}

//ファイル読み込みメソッド
	public static double[][] readFile(String path) throws IOException{
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
	public static double memberShip(double x, int K, int k){	//This means the Fuzzy Set
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

//推論メソッド


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
		ruleFit = new double[K+1][K+1][m];
		ruleResult = new int[K+1][K+1];
		trust = new double[K+1][K+1][classNumber];
		ruleWeight = new double[K+1][K+1];
		//適合度計算
		for(int i=0; i < K+1; i++) {
			for(int j=0; j < K+1; j++) {
				for(int p=0; p < m; p++) {
					ruleFit[i][j][p] = 1.0;
					for(int k=0; k < n; k++) {
						if(k==0) {
							ruleFit[i][j][p] *= memberShip(x[p][k], K, i);
						}
						else {
							ruleFit[i][j][p] *= memberShip(x[p][k], K, j);
						}
					}
				}
			}
		}
		//信頼度計算
		for(int i=0; i < K+1; i++) {
			for(int j=0; j < K+1; j++) {
				for(int k=0; k < classNumber; k++) {
					temp1 = 0.0;
					temp2 = 0.0;
					for(int p=0; p < m; p++) {
						if(classof_x[p] == k) {
							temp1 += ruleFit[i][j][p];
						}
						temp2 += ruleFit[i][j][p];
					}
					trust[i][j][k] = temp1/temp2;
				}
			}
		}
		//ルール結論部クラス決定
		for(int i=0; i < K+1; i++) {
			for(int j=0; j < K+1; j++) {
				ruleResult[i][j] = 0;
				comp = trust[i][j][0];
				for(int k=1; k < classNumber; k++) {
					if(comp < trust[i][j][k]) {	//信頼度が一番大きいクラスを結論部として決定する
						comp = trust[i][j][k];
						ruleResult[i][j] = k;
					}
				}
			}
		}
		//ルール重み計算
		for(int i=0; i < K+1; i++) {
			for(int j=0; j < K+1; j++) {
				temp1 = 0.0;
				for(int k=0; k < classNumber; k++) {
					if(ruleResult[i][j] != k) {
						temp1 += trust[i][j][k];
					}
				}
				ruleWeight[i][j] = trust[i][j][ruleResult[i][j]];
				ruleWeight[i][j] -= temp1;
			}
		}
		//ルール数出力
		ruleNumber = 0;
		ruleLength = 0;
		for(int i=0; i < K+1; i++) {
			for(int j=0; j < K+1; j++) {
				if(ruleWeight[i][j] > 0) {
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
		//識別率計算
		recogRate = 0.0;
			//適合度計算
		recogFit = new double[m][K+1][K+1];
		recog_Class = new int[m];
		for(int i=0; i < K+1; i++) {
			for(int j=0; j < K+1; j++) {
				if(ruleWeight[i][j] <= 0) {	//生成不可能ルールでは計算しない
					continue;
				}
				for(int p=0; p < m; p++) {
					recogFit[p][i][j] = 1.0;
					for(int k=0; k < n; k++) {
						if(k==0) {
							recogFit[p][i][j] *= memberShip(x[p][k], K, i);
						}
						else {
							recogFit[p][i][j] *= memberShip(x[p][k], K, j);
						}
					}
					recogFit[p][i][j] *= ruleWeight[i][j];
				}
			}
		}
			//推論クラス決定
		for(int p=0; p < m; p++) {
			comp_flg = 0;
			comp = 0.0;
			for(int i=0; i < K+1; i++) {
				for(int j=0; j < K+1; j++) {
					if(ruleWeight[i][j] <= 0) {	//生成不可能ルールでは計算しない
						continue;
					}
					if(comp_flg == 0) {
						comp_flg = 1;
						comp = recogFit[p][i][j];
						recog_Class[p] = ruleResult[i][j];
					}else if(comp == recogFit[p][i][j]) {
						recog_Class[p] = -1;	//識別不能パターン
					}else if(comp < recogFit[p][i][j]) {
						comp = recogFit[p][i][j];
						recog_Class[p] = ruleResult[i][j];
					}
				}
			}
		}
		for(int p=0; p < m; p++) {
			if(classof_x[p] == recog_Class[p]) {
				recogRate++;
			}
		}
		System.out.println("recogRate: " + recogRate/m *100);
		System.out.println("ruleNumber: " + ruleNumber);
		System.out.println("ruleLength: " + ruleLength);

	//未知パターン推論
		test_X = new double[h*h][n];
		test_Class = new int[h*h];
		testFit = new double[h*h][K+1][K+1];
		//テストパターン生成
		for(int i=0; i<h; i++) {
			for(int j=0; j<h; j++) {//走査方向 : 縦
				test_X[i*h+j][0] = (double)i/h;
				test_X[i*h+j][1] = (double)j/h;
			}
		}
		//適合度計算
		for(int i=0; i < K+1; i++) {
			for(int j=0; j < K+1; j++) {
				if(ruleWeight[i][j] <= 0) {	//生成不可能ルールでは計算しない
					continue;
				}
				for(int p=0; p < h*h; p++) {
					testFit[p][i][j] = 1.0;
					for(int k=0; k < n; k++) {
						if(k==0) {
							testFit[p][i][j] *= memberShip(test_X[p][k], K, i);
						}
						else {
							testFit[p][i][j] *= memberShip(test_X[p][k], K, j);
						}
					}
					testFit[p][i][j] *= ruleWeight[i][j];
				}
			}
		}
		//推論クラス決定
		for(int p=0; p < h*h; p++) {
			comp_flg = 0;
			comp = 0.0;
			for(int i=0; i < K+1; i++) {
				for(int j=0; j < K+1; j++) {
					if(ruleWeight[i][j] <= 0) {	//生成不可能ルールでは計算しない
						continue;
					}
					if(comp_flg == 0) {
						comp_flg = 1;
						comp = testFit[p][i][j];
						test_Class[p] = ruleResult[i][j];
					}else if(comp == testFit[p][i][j]) {
						test_Class[p] = -1;	//識別不能パターン
					}else if(comp < testFit[p][i][j]) {
						comp = testFit[p][i][j];
						test_Class[p] = ruleResult[i][j];
					}
				}
			}
		}

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
