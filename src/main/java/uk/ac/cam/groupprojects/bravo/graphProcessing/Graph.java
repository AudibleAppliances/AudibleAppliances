package uk.ac.cam.groupprojects.bravo.graphProcessing;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import uk.ac.cam.groupprojects.bravo.model.screen.LCD;

public class Graph {

    private BufferedImage i;
    private int w, h;
    private double a = 0.2; // proportion of cells to not be checked (padding)

    private static int graphW = 10;
    private static int graphH = 8;
    private boolean[][] g = new boolean[graphW][graphH];

    public Graph(BufferedImage in) {

        i = in;

        w = i.getWidth();
        h = i.getHeight();

        for (int i = 0; i < graphW; i ++) {

            for (int j = 0; j < graphH; j ++) {

                g[i][j] = false;

            }

        }

        read();

    }

    private void read() {

        // for now assumes threshold to make image b/w
        // assumes that the green (lit up parts) appear white, and the background is black
        // so far seems to work best on enlarged images - the original resolution is probably too small to deal with doubles for boundaries

        double cellW = Math.floor(w / graphW);
        double cellH = Math.floor(h / graphH);

        double b = 0.05 * (1 - 2 * a) * (1 - 2 * a) * cellW * cellH;

        for (int col = 0; col < 10; col ++) {

            for (int row = 0; row < 8; row ++) {

                int n = 0;

                for (double x = a * cellW; x < (1 - a) * cellW; x ++) {

                    for (double y = a * cellH; y < (1 - a) * cellH; y ++) {

                        int colour = i.getRGB((int) (col * cellW + x), (int) (row * cellH + y));
                        if (meanRGB(colour) > 0xf0) n += 1;

                    }

                }

                if (n > b) g[col][row] = true;

            }

        }

    }

    public LCD get() {

        return new LCD(g);

    }

    public static void main(String[] args) {
    	
    	// general testing

        File f = new File("test resources/graph/test4.jpg");
        BufferedImage test;
        try {

            test = ImageIO.read(f);

            Graph testGraph = new Graph(test);

            LCD r = testGraph.get();

            for (int i = 0; i < graphH; i ++) {

                for (int j = 0; j < graphW; j ++) {

                    if (r.get(j, i)) System.out.print("-");
                    else System.out.print("X");
                    System.out.print(" ");

                }

                System.out.println("");

            }

        } catch (IOException e) {

            e.printStackTrace();

        }

    }

    private int meanRGB(int c) {

        return (((c & 0x00ff0000) >> 16) + ((c & 0x0000ff00) >> 8) + (c & 0x000000ff)) / 3;

    }

}