package main;

import java.io.IOException;

import processing.core.PApplet;

public class Sketch extends PApplet {
	int x_zero = 50;
	int y_zero = 50;
	int x_limit = 500;
	int y_limit = 500;

	Fuzzy2 f_ex1;

	public void settings() {
		size(600, 600);
	}

	public void setup() {
		background(255);
		String inputPath1 = "src/kadai3_pattern1.txt";
		String inputPath2 = "src/kadai3_pattern2.txt";

		try {
			f_ex1 = new Fuzzy2(inputPath1);
			drawDataset(f_ex1);
			int[] ruleFlg = f_ex1.optimisation();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void draw() {
//		drawDataset(f_ex1);

	}

	public void drawDataset(Fuzzy2 f) {
		stroke(0);
		line(x_zero, y_zero, x_zero, y_limit + y_zero);
		line(x_zero, y_limit + y_zero, x_limit + x_zero, y_limit + y_zero);
		line(x_limit + x_zero, y_limit + y_zero, x_limit + x_zero, y_zero);
		line(x_limit + x_zero, y_zero, x_zero, y_zero);
		for (int p = 0; p < f.x.length; p++) {
			if (f.y[p] == 0) {
				stroke(0);
				fill(255, 0, 0);
				ellipse((float) f.x[p][0] * x_limit + x_zero,
						height - ((float) f.x[p][1] * y_limit + y_zero),
						10, 10);
			}
			if (f.y[p] == 1) {
				stroke(0);
				fill(0, 255, 0);
				ellipse((float) f.x[p][0] * x_limit + x_zero,
						height - ((float) f.x[p][1] * y_limit + y_zero),
						10, 10);
			}
			if (f.y[p] == 2) {
				stroke(0);
				fill(0, 0, 255);
				ellipse((float) f.x[p][0] * x_limit + x_zero,
						height - ((float) f.x[p][1] * y_limit + y_zero),
						10, 10);
			}
		}
	}

	public static void main(String[] args) {
		PApplet.main("main.Sketch");
	}

}
