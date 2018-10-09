package main;

import java.io.IOException;

public class Main {

	public static void main(String[] args) throws IOException{
		//入力データセット
			//mac開発環境
//		String inputPath_1 = "/Users/Uone/IDrive/OPU/研究フォルダ/Junior_ex3/eclipse_workspace/Junior_ex3/src/kadai3_pattern1.txt";
//		String inputPath_2 = "/Users/Uone/IDrive/OPU/研究フォルダ/Junior_ex3/eclipse_workspace/Junior_ex3/src/kadai3_pattern2.txt";
			//Windows 研究室開発環境
		String inputPath_1 = "C:\\Users\\Yuichi Omozaki\\IDrive\\Junior_ex3\\eclipse_workspace\\Junior_ex3\\src\\kadai3_pattern1.txt";
		String inputPath_2 = "C:\\Users\\Yuichi Omozaki\\IDrive\\Junior_ex3\\eclipse_workspace\\Junior_ex3\\src\\kadai3_pattern2.txt";

		//境界点書き込みPath
			//mac開発環境
//		String borderPath_1 = "/Users/Uone/IDrive/OPU/研究フォルダ/Junior_ex3/eclipse_workspace/Junior_ex3/src/border_pattern1.dat";
//		String borderPath_2 = "/Users/Uone/IDrive/OPU/研究フォルダ/Junior_ex3/eclipse_workspace/Junior_ex3/src/border_pattern2.dat";
			//Windows 研究室開発環境
		String borderPath_1 = "C:\\Users\\Yuichi Omozaki\\IDrive\\Junior_ex3\\eclipse_workspace\\Junior_ex3\\src\\border_pattern1.dat";
		String borderPath_2 = "C:\\Users\\Yuichi Omozaki\\IDrive\\Junior_ex3\\eclipse_workspace\\Junior_ex3\\src\\border_pattern2.dat";


		Fuzzy f_ex1 = new Fuzzy(inputPath_1, borderPath_1);
		Fuzzy f_ex2 = new Fuzzy(inputPath_2, borderPath_2);

		System.out.println("-----例題 1-----");
		f_ex1.fuzzy();
		System.out.println("");
		System.out.println("-----例題 2-----");
		f_ex2.fuzzy();
		System.out.println("");
	}

}
