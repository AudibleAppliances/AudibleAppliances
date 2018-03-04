package uk.ac.cam.groupprojects.bravo.main.threads;

import uk.ac.cam.groupprojects.bravo.config.BikeField;
import uk.ac.cam.groupprojects.bravo.imageProcessing.ScreenBox;
import uk.ac.cam.groupprojects.bravo.model.numbers.ScreenNumber;
import uk.ac.cam.groupprojects.bravo.ocr.SegmentRecogniser;

import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.Set;

import static uk.ac.cam.groupprojects.bravo.main.ApplicationConstants.DEBUG;

/**
 * Created by david on 04/03/2018.
 */
public class SegmentRecogniserThread implements Runnable {

    private ScreenBox[] boxes;
    private Set<ScreenBox> activeSegs;
    private Map<ScreenBox, BufferedImage> imgSegs;
    private Map<BikeField, ScreenNumber> currentFields;

    public SegmentRecogniserThread( ScreenBox[] boxes, Set<ScreenBox> activeSegs, Map<ScreenBox, BufferedImage> imgSegs, Map<BikeField, ScreenNumber> currentFields ){
        this.boxes = boxes;
        this.activeSegs = activeSegs;
        this.imgSegs = imgSegs;
        this.currentFields = currentFields;
    }

    @Override
    public void run() {
        if ( DEBUG ){
            for (ScreenBox box : boxes) {
                if (activeSegs.contains(box)) {
                    for (BikeField field : box.getFields()) {
                        if (field.getTitleBox() == null || activeSegs.contains(field.getTitleBox())) {
                            try {
                                System.out.println("Running OCR for " + field.toString());
                                long startTime = System.currentTimeMillis();
                                currentFields.get(field).setValue(SegmentRecogniser.recogniseInt(imgSegs.get(box)));
                                long elapsedTime = System.currentTimeMillis() - startTime;
                                System.out.println("That took " + elapsedTime);
                            }catch ( Exception e ){
                                //I don't care
                            }

                        }
                    }
                }
            }
        }else {
            for (ScreenBox box : boxes) {
                if (activeSegs.contains(box)) {
                    for (BikeField field : box.getFields()) {
                        if (field.getTitleBox() == null || activeSegs.contains(field.getTitleBox())) {
                            try {
                                currentFields.get(field).setValue(SegmentRecogniser.recogniseInt(imgSegs.get(box)));
                            }catch ( Exception e ){
                                //I don't care
                            }

                        }
                    }
                }
            }
        }


    }
}
