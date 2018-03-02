package uk.ac.cam.groupprojects.bravo.graphProcessing;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;

//import uk.ac.cam.groupprojects.bravo.model.screen.LCD;

public class Graph {

	private int w, h;
	private double a = 0.4; // proportion of cell width to not be checked (padding)
	private double b = 0.3; // proportion of cell height ''

	public static int graphW = 8;
	public static int graphH = 8;
	private int[] g = new int[graphW];
	private int[] og = new int[graphW];
	private static boolean[] d = new boolean[graphW];
	private int flashing = -1;

	public Graph(BufferedImage in) {

		w = in.getWidth();
		h = in.getHeight();

		for (int i = 0; i < graphW; i ++) g[i] = 0;

		read(in);

	}

	public void read(BufferedImage i) {
		
		//og = g.clone();
		
		for (int j = 0; j < graphW; j ++) og[j] = g[j];

		// for now assumes threshold to make image b/w
		// assumes that the green (lit up parts) appear white, and the background is black
		// so far seems to work best on enlarged images - the original resolution is probably too small to deal with doubles for boundaries

		double cellW = Math.floor(w / graphW);
		double cellH = Math.floor(h / graphH);

		double p = 0.05 * (1 - 2 * a) * (1 - 2 * b) * cellW * cellH;

		for (int col = 0; col < graphW; col ++) {
			
			g[col] = 0;

			for (int row = 0; row < graphH; row ++) {

				int n = 0;

				for (double x = a * cellW; x < (1 - a) * cellW; x ++) {

					for (double y = b * cellH; y < (1 - b) * cellH; y ++) {

						int colour = i.getRGB((int) (col * cellW + x), (int) (row * cellH + y));
						if (testColour(colour)) n += 1;

					}

				}

				if (n > p) g[col] += (1 << row);

			}

		}
		
		//System.out.println(Arrays.toString(g));
		//System.out.println(Arrays.toString(og));
		
		updateD();

	}
	
	private void updateD() {
		
		flashing = -1;
		
		for (int col = 0; col < graphW; col ++) {
			
			d[col] = false;
			
			if (g[col] != og[col]) {
				
				d[col] = true;
				if (g[col] == 0 || og[col] == 0) flashing = col;
				
			}
			
		}
		
	}

	public int[] get() {
		
		// return copy of most recent graph

		return g.clone();

	}
	
	public int[] getOld() {
		
		// return copy of penultimate graph
		
		return og.clone();
		
	}
	
	public boolean cell(int x, int y) {
		
		// true if cell at column x, row y is lit
		
		return (((g[x] >> y) & 1) != 0);
		
	}
	
	public int barHeight(int x) {
		
		// height of the bar at column x
		
		int r = 0;
		
		for (int i = 0; i < graphH; i ++) r += ((g[x] >> i) & 1);
		
		return r;
		
	}
	
	public boolean[] delta() {
		
		// index of array is whether that column changed in value or not
		
		return d;
		
	}
	
	public int flashing() {
		
		// returns the column that is flashing, -1 if no column is
		
		return flashing;
		
	}

	public static void main(String[] args) {
		
		// general testing

		Graph testGraph;
		File fa = new File("src/test/resources/graph/test4.jpg");
		BufferedImage testa;
		try {

			testa = ImageIO.read(fa);
			testGraph = new Graph(testa);

			for (int i = 0; i < graphH; i ++) {

				for (int j = 0; j < graphW; j ++) {

					if (testGraph.cell(j, i)) System.out.print("-");
					else System.out.print("X");
					System.out.print(" ");

				}

				System.out.println("");

			}
			
			System.out.println(Arrays.toString(d));
			
			System.out.println("");
			
			File fb = new File("src/test/resources/graph/test5.jpg");
			BufferedImage testb;
			try {

				testb = ImageIO.read(fb);
				testGraph.read(testb);

				for (int i = 0; i < graphH; i ++) {

					for (int j = 0; j < graphW; j ++) {

						if (testGraph.cell(j, i)) System.out.print("-");
						else System.out.print("X");
						System.out.print(" ");

					}

					System.out.println("");

				}
				
				System.out.println(Arrays.toString(d));

			} catch (IOException e) {

				e.printStackTrace();

			}

		} catch (IOException e) {

			e.printStackTrace();

		}

	}

	private boolean testColour(int c) {
		
		int mean = (((c & 0x00ff0000) >> 16) + ((c & 0x0000ff00) >> 8) + (c & 0x000000ff)) / 3;
		int green = (c & 0x0000ff00) >> 8;
		
		//System.out.print(mean);
		//System.out.print(" ");
		//System.out.println((c & 0x0000ff00) >> 8);

		return (mean > 30) && (green > 50);

	}

}