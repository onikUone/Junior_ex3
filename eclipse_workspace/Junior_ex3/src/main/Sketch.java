package main;

import java.io.IOException;

import processing.core.PApplet;

public class Sketch extends PApplet {

	public void settings() {
		size(500, 500);
	}

	public void setup() {
		background(255);
		String inputPath1 = "src/kadai3_pattern1.txt";
		String inputPath2 = "src/kadai3_pattern2.txt";

		try {
			Fuzzy2 f_ex1 = new Fuzzy2(inputPath1);
			drawDataset(f_ex1);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void draw() {

	}

	public void drawDataset(Fuzzy2 f) {
		for (int p = 0; p < f.x.length; p++) {
			if (f.y[p] == 0) {
				stroke(0);
				fill(255, 0, 0);
				ellipse((float) f.x[p][0] * width, height - (float) f.x[p][1] * height, 10, 10);
			}
			if (f.y[p] == 1) {
				stroke(0);
				fill(0, 255, 0);
				ellipse((float) f.x[p][0] * width, height - (float) f.x[p][1] * height, 10, 10);
			}
			if (f.y[p] == 2) {
				stroke(0);
				fill(0, 0, 255);
				ellipse((float) f.x[p][0] * width, height - (float) f.x[p][1] * height, 10, 10);
			}
		}
	}

	public static void main(String[] args) {
		PApplet.main("main.Sketch");
	}

}
