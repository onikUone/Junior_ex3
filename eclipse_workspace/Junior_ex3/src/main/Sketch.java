package main;

import java.io.IOException;

import processing.core.PApplet;

public class Sketch extends PApplet {
	int size = 700;
	int limit = 500;
	int zero = (size - limit) / 2;

	Fuzzy f_ex1;
	Fuzzy2 f2_ex1;

	public void settings() {
		size(size, size);
	}

	public void setup() {
		background(255);
		String inputPath1 = "src/kadai3_pattern1.txt";
		String inputPath2 = "src/kadai3_pattern2.txt";

		try {
			f_ex1 = new Fuzzy(inputPath1);
			f_ex1.readFile(inputPath1);
			drawDataset(f_ex1);

			//			f2_ex1 = new Fuzzy2(inputPath1);
			//			drawDataset(f2_ex1);
			//			int[] ruleFlg = f2_ex1.optimisation();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void draw() {
		//		drawDataset(f_ex1);

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
			}
			else {
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

	public void drawDataset(Fuzzy f) {
		drawAxis();
		for (int p = 0; p < f.x.length; p++) {
			if (f.classof_x[p] == 0) {
				stroke(0);
				fill(255, 0, 0);
				ellipse((float) f.x[p][0] * limit + zero,
						height - ((float) f.x[p][1] * limit + zero),
						10, 10);
			}
			if (f.classof_x[p] == 1) {
				stroke(0);
				fill(0, 255, 0);
				ellipse((float) f.x[p][0] * limit + zero,
						height - ((float) f.x[p][1] * limit + zero),
						10, 10);
			}
			if (f.classof_x[p] == 2) {
				stroke(0);
				fill(0, 0, 255);
				ellipse((float) f.x[p][0] * limit + zero,
						height - ((float) f.x[p][1] * limit + zero),
						10, 10);
			}
		}
	}

	public static void main(String[] args) {
		PApplet.main("main.Sketch");
	}

}
