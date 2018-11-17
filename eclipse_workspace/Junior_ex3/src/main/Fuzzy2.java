package main;

import static main.FuzzySetTriangle.*;

public class Fuzzy2 {

	int attribute; //学習データの属性数
	int classes; //学習データのクラス数
	int pattern; //学習データのパターン数
	double[][] x; //学習データ入力パターン
	int[] y; //学習データ教師信号

	String inputPath;

	//Fuzzy rule
	int[] result;
	double[] weight;
	int[] ruleFlg;
	Rule[] rule;

	int ruleNumber;
	int ruleLength;
	double recogRate;

	//ファイル読み込みメソッド
//	public void readFile(String path) throws IOException {
//		List<String[]> list = new ArrayList<String[]>();
//		BufferedReader in = new BufferedReader(new FileReader(path));
//		String line;
//		line = in.readLine();
//		this.pattern = Integer.parseInt(line.split(", ")[0]);
//		this.attribute = Integer.parseInt(line.split(", ")[1]);
//		this.classes = Integer.parseInt(line.split(", ")[2]);
//		x = new double[pattern][attribute];
//		y = new int[pattern];
//		while ((line = in.readLine()) != null) {
//			list.add(line.split(", "));
//		}
//		in.close();
//		for (int i = 0; i < pattern; i++) {
//			for (int j = 0; j < attribute; j++) {
//				x[i][j] = Double.parseDouble(list.get(i)[j]);
//			}
//			y[i] = Integer.parseInt(list.get(i)[attribute]);
//		}
//	}

	//memberShip function
	public static double memberShip(double x, int myFuzzySetNum, int division) {
		double a = (myFuzzySetNum - 1.0) / (division - 1.0);
		double b = 1.0 / (division - 1.0);

		if (myFuzzySetNum == 0) { // don't care
			return 1.0;
		} else {
			return Math.max(1 - Math.abs(a - x) / b, 0.0);
		}

	}

	//適合度計算
	public static double calcFit(int[] rule, double[] x) {
		double fit = 1;
		for (int i = 0; i < x.length; i++) {
			fit *= memberShip(x[i], TriRule[rule[i]][0], TriRule[rule[i]][1]);
		}
		return fit;
	}

	//信頼度計算
	public static double calcTrust(int[] rule, double[][] x, int[] y, int classIs) {
		double temp1 = 0;
		double temp2 = 0;
		for (int p = 0; p < x.length; p++) {
			if (y[p] == classIs) {
				temp1 += calcFit(rule, x[p]);
			}
			temp2 += calcFit(rule, x[p]);
		}
		return temp1 / temp2;
	}

	//未知パターン推論
	public int reasoning(double[] x, int[] _ruleFlg) {
		int flg = 0;
		int maxRuleIndex = -1;
		double max = 0;
		double comp = 0;
		//推論開始
		for (int i = 0; i < rule.length; i++) {
			if (rule[i].trust <= 0.5) {
				flg = -1;
				continue;
			}
			if (_ruleFlg[i] == 0) {
				flg = -1;
				continue;
			}
			if (maxRuleIndex == -1) {
				max = calcFit(rule[i].rule, x) * rule[i].weight;
				maxRuleIndex = i;
				flg = 1;
			} else {
				comp = calcFit(rule[i].rule, x) * rule[i].weight;
				if (comp == max) {
					if (rule[maxRuleIndex].myClass != rule[i].myClass || comp == 0) {
						flg = -1;
						continue;
					}
				}
				if (comp > max) {
					max = comp;
					maxRuleIndex = i;
					flg = 1;
				}
			}
		}
		if (flg == -1) {//識別不能
			return -1;
		} else {
			return rule[maxRuleIndex].myClass;
		}
	}

	//ルール最適化メソッド
	public int[] optimisation() {
		double comp = 0.0;
		int combination = (int) Math.pow(2, (int) Math.pow(n_rule + 1, attribute));
		int optimumIndex = -1;
		double fitness;
		String binary;
		//加重和適応度関数の全探索
		for (int i = 0; i < combination; i++) {
			binary = String.format("%016d", Long.parseLong(Integer.toBinaryString(i)));
			for (int j = 0; j < (int) Math.pow(n_rule + 1, attribute); j++) {
				ruleFlg[j] = Character.getNumericValue(binary.charAt(j));
			}
			//fitnessの計算
			ruleNumber = 0;
			ruleLength = 0;
			recogRate = 0;
			for(int j = 0; j < rule.length; j++) {
				if(ruleFlg[j] == 0) {
					continue;
				}
				else if(rule[j].weight > 0) {
					ruleNumber++;
					for(int k = 0; k < attribute; k++) {
						if(rule[j].rule[k] != 0) {
							ruleLength++;
						}
					}
				}
			}
			for(int p = 0; p < pattern; p++) {
				if(y[p] == reasoning(x[p], ruleFlg)) {
					recogRate++;
				}
			}
			recogRate /= pattern;
			fitness = 10 * (recogRate / pattern * 100) - 1 * ruleNumber - 1 * ruleLength;
			if(i == 0) {
				comp = fitness;
				optimumIndex = i;
			} else {
				if(fitness > comp) {
					comp = fitness;
					optimumIndex = i;
				}
			}
		}
		binary = String.format("%016d", Long.parseLong(Integer.toBinaryString(optimumIndex)));
		for (int i = 0; i < (int) Math.pow(n_rule + 1, attribute); i++) {
			ruleFlg[i] = Character.getNumericValue(binary.charAt(i));
			System.out.print(ruleFlg[i] + " ");
		}
		System.out.println("");
		ruleNumber = 0;
		ruleLength = 0;
		recogRate = 0;
		for(int i = 0; i < rule.length; i++) {
			if(ruleFlg[i] == 0) {
				continue;
			}
			ruleNumber++;
			for(int j = 0; j < attribute; j++) {
				if(rule[i].rule[j] != 0) {
					ruleLength++;
				}
			}
		}
		for(int p = 0; p < pattern; p++) {
			if(y[p] == reasoning(x[p], ruleFlg)) {
				recogRate++;
			}
		}
		System.out.println("RecogRate: " + recogRate / pattern * 100);
		System.out.println("RuleNumber: " + ruleNumber);
		System.out.println("RuleLength: " + ruleLength);
		System.out.println("");
		return ruleFlg;
	}

	//constractor
	Fuzzy2(Sketch _s){
		this.pattern = _s.pattern;
		this.attribute = _s.attribute;
		this.classes = _s.classes;
		this.x = _s.x;
		this.y = _s.y;
		//ルール個体生成 全探索のため全組み合わせの個体生成
//		rule = new Rule[(int) Math.pow(n_rule + 1, attribute)];
//		ruleFlg = new int[rule.length];
//		int[] setRule = new int[attribute];
//		int index = 0;
//		for (int i = 0; i < n_rule + 1; i++) {
//			for (int j = 0; j < n_rule + 1; j++) {
//				setRule[0] = i;
//				setRule[1] = j;
//				rule[index] = new Rule(setRule, this);
//				index++;
//			}
//		}
	}
}
