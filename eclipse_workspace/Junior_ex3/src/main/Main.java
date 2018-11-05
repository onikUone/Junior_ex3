package main;

import java.io.IOException;

public class Main {

	public static void main(String[] args) throws IOException{
		//入力データセット
		String inputPath_1 = "src/kadai3_pattern1.txt";
		String inputPath_2 = "src/kadai3_pattern2.txt";

		//境界点書き込みPath
		String borderPath_1 = "src/border_pattern1.dat";
		String borderPath_2 = "src/border_pattern2.dat";

		//最適化境界書き込みPath
		String optimisationPath_1 = "src/optimum_pattern1.dat";
		String optimisationPath_2 = "src/optimum_pattern2.dat";


		Fuzzy f_ex1 = new Fuzzy(inputPath_1, borderPath_1, optimisationPath_1);
		Fuzzy f_ex2 = new Fuzzy(inputPath_2, borderPath_2, optimisationPath_2);

		System.out.println("-----例題 1-----");
		f_ex1.fuzzy();
		System.out.println("");
		System.out.println("-----例題 2-----");
		f_ex2.fuzzy();
		System.out.println("");
	}

}
