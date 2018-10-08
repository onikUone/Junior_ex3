package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

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

	public static double memberShip(double x, int k){	//This means the Fuzzy Set
		//k: 0=don't care, 1=small, 2=medium, 3=large
		int K = 3;	//ファジィ分割数
		double a = (k - 1.0)/(K - 1.0);
		double b = 1.0/(K - 1.0);
		System.out.println("a: " + a);
		System.out.println("b: " + b);

		if(k == 0) {
			return 1.0;	//don't care
		}else {
			return Math.max( 1 - Math.abs(a - x)/b , 0.0);
		}
	}

	public static void main(String[] args) throws IOException {
		//初期パラメータ
		int n;	//学習データの属性数;次元数
		int classNumber;	//学習データのクラス数
		int m;	//学習データのパターン数
		//mac開発環境の入力データへのパス
		String inputPath_1 = "/Users/Uone/IDrive/OPU/研究フォルダ/Junior_ex3/eclipse_workspace/Junior_ex3/src/kadai3_pattern1.txt";
		String inputPath_2 = "/Users/Uone/IDrive/OPU/研究フォルダ/Junior_ex3/eclipse_workspace/Junior_ex3/src/kadai3_pattern2.txt";
		double[][] x;
		int[] classof_x;

		//ファイル読み込み
		double[][] inputFile_1 = readFile(inputPath_1);
		double[][] inputFile_2 = readFile(inputPath_2);

		//例題1処理
		m = (int)inputFile_1[0][0];
		n = (int)inputFile_1[0][1];
		classNumber = (int)inputFile_1[0][2];

		//学習データセット取得
		x = new double[m][n];	//x[学習パターン数][次元数]
		classof_x = new int[m];	//classof_x[学習パターン数]
		for(int i=0; i< x.length; i++){
			for(int j=0; j<x[i].length; j++) {
				x[i][j] = inputFile_1[i+1][j];
			}
		}
		for(int i=0; i<classof_x.length; i++) {
			classof_x[i] = (int)inputFile_1[i+1][n];
		}

		System.out.println(memberShip(0.2, 3));


	}

}
