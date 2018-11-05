package main;

public class Rule {
	//field
	int myClass;
	double weight;
	double trust;
	int fitness;	//自身の評価値
	int[] rule; //rule[n] nは入力パターンの属性数

	//memberFunction

	//自身の結論クラス部決定メソッド
	public void decideMyClass(double[][] x, int[] y, int classes) {
		double max = 0;
		double comp = 0;
		for (int i = 0; i < classes; i++) {
			if (i == 0) {
				max = Fuzzy2.calcTrust(rule, x, y, i);
				myClass = i;
			}
			else {
				comp = Fuzzy2.calcTrust(rule, x, y, i);
				if (comp > max) {
					max = comp;
					myClass = i;
				}
			}
		}
	}

	//自身のルール重み計算メソッド
	public void calcMyWeight(double[][] x, int[] y, int classes) {
		trust = Fuzzy2.calcTrust(rule, x, y, myClass);
		if(trust <= 0.5) {
			weight = 0;
			myClass = -1;
			return;
		}
		weight = trust;
		for (int i = 0; i < classes; i++) {
			if (i != myClass) {
				weight -= Fuzzy2.calcTrust(rule, x, y, i);
			}
		}
	}

	//fitness初期化
	public void clearFitness() {
		this.fitness = 0;
	}

	//constractor
	Rule(int[] rule, Fuzzy2 f) {
		this.rule = new int[f.attribute];
		this.fitness = 0;
		for(int i=0; i<f.attribute; i++) {
			this.rule[i] = rule[i];
		}
		decideMyClass(f.x, f.y, f.classes);
		calcMyWeight(f.x, f.y, f.classes);
	}
}
