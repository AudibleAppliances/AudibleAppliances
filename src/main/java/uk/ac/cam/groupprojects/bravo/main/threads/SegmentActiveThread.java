package uk.ac.cam.groupprojects.bravo.main.threads;

import uk.ac.cam.groupprojects.bravo.imageProcessing.ScreenBox;
import uk.ac.cam.groupprojects.bravo.ocr.SegmentActive;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

import static uk.ac.cam.groupprojects.bravo.main.ApplicationConstants.DEBUG;

/**
 * Created by david on 04/03/2018.
 */
public class SegmentActiveThread implements Runnable {

    private ScreenBox[] boxes;
    private Set<ScreenBox> activeSegs;
    private Map<ScreenBox, BufferedImage> imgSegs;

    public SegmentActiveThread( ScreenBox[] boxes, Set<ScreenBox> activeSegs, Map<ScreenBox, BufferedImage> imgSegs ){
        this.boxes = boxes;
        this.activeSegs = activeSegs;
        this.imgSegs = imgSegs;
    }

    @Override
    public void run() {
        if ( DEBUG ){
            for ( ScreenBox box: boxes ){
                try {
                    System.out.println("Running segmentActive for " + box.name() );
                    long startTime = System.currentTimeMillis();
                    if (SegmentActive.segmentActive(imgSegs.get(box))) {
                        synchronized ( activeSegs ){
                            activeSegs.add(box);
                        }

                    }
                    long elapsedTime = System.currentTimeMillis() - startTime;
                    System.out.println("segmentActive took " + elapsedTime);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }else {
            for ( ScreenBox box: boxes ){
                try {
                    if (SegmentActive.segmentActive(imgSegs.get(box))) {
                        synchronized ( activeSegs ){
                            activeSegs.add(box);
                        }
                    }
                } catch (IOException e) {
                    //Do nothing lol
                }
            }
        }

    }

}
