package main;

import static main.FuzzySetTriangle.*;

public class FuzzyController {
	Fuzzy2 f;
	Sketch s;

	Rule[] rules;
	int[] ruleFlg;

	double recogRate;
	int ruleNumber;
	int ruleLength;

	//メンバシップ関数
	public double memberShip(double _x, int myFuzzySetNum, int division) {
		double a = (myFuzzySetNum - 1.0) / (division - 1.0);
		double b = 1.0 / (division - 1.0);
		if(myFuzzySetNum == 0) {
			return 1.0;
		} else {
			return Math.max(1 - Math.abs(a - _x) / b, 0.0);
		}
	}

	//適合度計算
	public double calcFit(int[] _rule, double[] _x) {
		double fit = 1;
		for(int i = 0; i < f.attribute; i++) {
			fit *= memberShip(_x[i], TriRule[_rule[i]][0], TriRule[_rule[i]][1]);
		}
		return fit;
	}

	//全組み合わせルール生成
	public void generate() {
		rules = new Rule[(int)Math.pow(1 + n_rule, f.attribute)];
		ruleFlg = new int[rules.length];
		int[] setRule = new int[f.attribute];
		int index = 0;
		for(int i = 0; i < n_rule + 1; i++) {
			for(int j = 0; j < n_rule + 1; j++) {
				setRule[0] = i;
				setRule[1] = j;
				rules[index] = new Rule(setRule, f);
				index++;
			}
		}
	}

	public int reasoning(double[] _x, int[] _ruleFlg) {
		int flg = 0;
		int maxRuleIndex = -1;
		double max = 0;
		double comp = 0;

		for(int ruleIndex = 0; ruleIndex < rules.length; ruleIndex++) {
			if(rules[ruleIndex].weight <= 0) {
				if(flg == 0) {
					flg = -1;
				}
				continue;
			}
			if(_ruleFlg[ruleIndex] == 0) {
				continue;
			}
			if(maxRuleIndex == -1) {
				max = Fuzzy2.calcFit(rules[ruleIndex].rule, _x) * rules[ruleIndex].weight;
				maxRuleIndex = ruleIndex;
				flg = 1;
			} else {
				comp = Fuzzy2.calcFit(rules[ruleIndex].rule, _x) * rules[ruleIndex].weight;
				if(comp == max) {
					if(rules[maxRuleIndex].myClass != rules[ruleIndex].myClass || comp == 0) {
						flg = -1;
						continue;
					}
					flg = 1;
				}
				if(comp > max) {
					max = comp;
					maxRuleIndex = ruleIndex;
					flg = 1;
				}
			}
		}
		if(flg == -1) {
			return -1;
		} else {
			return rules[maxRuleIndex].myClass;
		}
	}

	//constractor
	FuzzyController(Fuzzy2 _f, Sketch _s){
		this.f = _f;
		this.s = _s;
		generate();
	}
}
