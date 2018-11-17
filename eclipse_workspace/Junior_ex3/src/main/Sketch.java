package main;

import processing.core.PApplet;

public class Sketch extends PApplet {
	int size = 700;
	int limit = 500;
	int zero = (size - limit) / 2;

	int pattern;
	int attribute;
	int classes;

	double[][] x;
	int[] y;

	public void read(String _path) {
		String[] line = loadStrings(_path);

		this.pattern = Integer.parseInt(line[0].split(", ")[0]);
		this.attribute = Integer.parseInt(line[0].split(", ")[1]);
		this.classes = Integer.parseInt(line[0].split(", ")[2]);

		x = new double[pattern][attribute];
		y = new int[pattern];

		for (int i = 0; i < pattern; i++) {
			for (int j = 0; j < attribute; j++) {
				x[i][j] = Double.parseDouble(line[i].split(", ")[j]);
			}
			y[i] = Integer.parseInt(line[i].split(", ")[attribute]);
		}

	}

	public void settings() {
		size(size, size);
	}

	public void setup() {
		background(255);
		String inputPath1 = "src/kadai3_pattern1.txt";
		String inputPath2 = "src/kadai3_pattern2.txt";

		read(inputPath1);
		Fuzzy2 f = new Fuzzy2(this);
		FuzzyController fc = new FuzzyController(f, this);
		int[] ruleFlg = new int[16];
		for (int i = 0; i < 16; i++) {
			ruleFlg[i] = 1;
		}
		double recogRate;
		int ruleNumber = 0;
		int ruleLength = 0;
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (ruleFlg[i * 4 + j] == 0) { //選択されたルールでないならば計算しない
					continue;
				}
				if (fc.rules[i*4+j].weight > 0) {
					ruleNumber++;
					if (i != 0) {
						ruleLength++;
					}
					if (j != 0) {
						ruleLength++;
					}
				}
			}
		}
		System.out.println("ルール数： " + ruleNumber);
		System.out.println("総ルール長： " + ruleLength);
		drawBorder(fc, ruleFlg);
		drawAxis();
		drawDataset(f);
	}

	public void draw() {

	}

	public void drawAxis() {
		stroke(0);
		line(zero, zero, zero, limit + zero);
		line(zero, limit + zero, limit + zero, limit + zero);
		line(limit + zero, limit + zero, limit + zero, zero);
		line(limit + zero, zero, zero, zero);

		for (int i = 0; i < 10; i++) {

			if (i % 2 == 0) {
				line(zero + (limit / 10) * i,
						height - zero,
						zero + (limit / 10) * i,
						height - (zero + 15));

				line(zero + (limit / 10) * i,
						height - (zero + limit),
						zero + (limit / 10) * i,
						height - (zero + limit - 15));

				line(zero,
						zero + (limit / 10) * i,
						zero + 15,
						zero + (limit / 10) * i);

				line(zero + limit,
						zero + (limit / 10) * i,
						zero + limit - 15,
						zero + (limit / 10) * i);
			} else {
				line(zero + (limit / 10) * i,
						height - zero,
						zero + (limit / 10) * i,
						height - (zero + 5));

				line(zero + (limit / 10) * i,
						height - (zero + limit),
						zero + (limit / 10) * i,
						height - (zero + limit - 5));

				line(zero,
						zero + (limit / 10) * i,
						zero + 5,
						zero + (limit / 10) * i);

				line(zero + limit,
						zero + (limit / 10) * i,
						zero + limit - 5,
						zero + (limit / 10) * i);
			}
		}
	}

	public void drawDataset(Fuzzy2 _f) {
		drawAxis();
		for (int p = 0; p < pattern; p++) {
			if (_f.y[p] == 0) {
				stroke(0);
				fill(255, 0, 0);
				ellipse((float) _f.x[p][0] * limit + zero,
						height - ((float) _f.x[p][1] * limit + zero),
						10, 10);
			}
			if (_f.y[p] == 1) {
				stroke(0);
				fill(0, 0, 255);
				ellipse((float) _f.x[p][0] * limit + zero,
						height - ((float) _f.x[p][1] * limit + zero),
						10, 10);
			}
			if (_f.y[p] == 2) {
				stroke(0);
				fill(0, 255, 0);
				ellipse((float) _f.x[p][0] * limit + zero,
						height - ((float) _f.x[p][1] * limit + zero),
						10, 10);
			}
		}
	}

	public void drawBorder(FuzzyController _fc, int[] _ruleFlg) {
		//テストパターン生成
		int h = 500;
		double[][] test_X = new double[h * h][attribute];
		int[] test_Y = new int[h * h];
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < h; j++) {
				test_X[i * h + j][0] = (double) i / h;
				test_X[i * h + j][1] = (double) j / h;
			}
		}
		for (int i = 0; i < test_X.length; i++) {
			test_Y[i] = _fc.reasoning(test_X[i], _ruleFlg);
			if (test_Y[i] == 0) {
				stroke(255, 0, 0);
				point((float) (test_X[i][0] * limit + zero), height - (float) (test_X[i][1] * limit + zero));
			}
			if (test_Y[i] == 1) {
				stroke(0, 0, 255);
				point((float) (test_X[i][0] * limit + zero), height - (float) (test_X[i][1] * limit + zero));
			}
			if (test_Y[i] == 2) {
				stroke(0, 255, 0);
				point((float) (test_X[i][0] * limit + zero), height - (float) (test_X[i][1] * limit + zero));
			}
			if (test_Y[i] == -1) {
				stroke(0);
				point((float) (test_X[i][0] * limit + zero), height - (float) (test_X[i][1] * limit + zero));
			}
		}
	}

	public static void main(String[] args) {
		PApplet.main("main.Sketch");
	}

}
