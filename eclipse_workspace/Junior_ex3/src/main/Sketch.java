package main;

import processing.core.PApplet;

public class Sketch extends PApplet{

	public void settings() {
		size(500, 500);
	}

	public void setup(){
		background(255);
		String inputPath1 = "src/kadai3_pattern1.txt";
		String inputPath2 = "src/kadai3_pattern2.txt";

//		try {
//			Fuzzy f_ex1 = new Fuzzy(inputPath1);
//			for(int p = 0; p < f_ex1.x.length; p++) {
//				if(f_ex1.classof_x[p] == 0) {
//					fill(255, 0, 0);
//					ellipse((float)f_ex1.x[p][0], 1 - (float)f_ex1.x[p][1], 3, 3);
//				}
//			}
//			f_ex1.fuzzy();
//
//		} catch (IOException e) {
//			e.printStackTrace();
		}

	public void draw() {

	}

	public static void main(String[] args) {
		PApplet.main("main.Sketch");
	}

}
