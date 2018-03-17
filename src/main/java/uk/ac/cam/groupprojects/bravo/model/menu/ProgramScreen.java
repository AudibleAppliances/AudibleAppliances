package uk.ac.cam.groupprojects.bravo.model.menu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import uk.ac.cam.groupprojects.bravo.config.BikeField;
import uk.ac.cam.groupprojects.bravo.imageProcessing.ScreenBox;
import uk.ac.cam.groupprojects.bravo.main.BikeStateTracker;
import uk.ac.cam.groupprojects.bravo.model.LCDState;
import uk.ac.cam.groupprojects.bravo.model.numbers.Program;

// This is the screen where the user selects which program they want to use
// Specifically, this is the superclass of the 12 possible program screens, given below
public abstract class ProgramScreen extends BikeScreen {
    private final int programNumber;
    private final ScreenEnum screenEnum;

    private static long timeGivenLongSpeech = 0;
    private static final long TIME_BETWEEN_LONG_SPEECHES = 30000;

    protected ProgramScreen(int programNumber, ScreenEnum screenEnum) {
        this.programNumber = programNumber;
        this.screenEnum = screenEnum;
    }

    @Override
    public boolean isActiveScreen(BikeStateTracker state) {
        boolean isProgramScreen = !state.isTimeChanging() &&
               state.getBoxState(ScreenBox.LCD_TEXT_1) == LCDState.SOLID_OFF &&
               state.getBoxState(ScreenBox.LCD_TEXT_3) == LCDState.BLINKING &&
               state.getBoxState(ScreenBox.LCD_TEXT_4) == LCDState.BLINKING &&
               state.getBoxState(ScreenBox.LCD_TEXT_5_TOP) == LCDState.BLINKING &&
               state.getBoxState(ScreenBox.LCD_TEXT_9) == LCDState.SOLID_OFF;

        if (isProgramScreen) {
            Program p  = (Program)state.getFieldValue(BikeField.PROGRAM, true);
            if (p == null)
                return false; // Can't recognise the number

            int value;
            if (p.getValue() > 10) {
                String converted = String.valueOf(p.getValue()).replaceAll("8", "0");
                value = Integer.parseInt(converted);
            }
            else {
                value = p.getValue();
            }

            // If we're in a program screen and the number we can see matches ours
            return value == programNumber;
        }
        else {
            // Not in a program screen
            return false;
        }
    }

    @Override
    public ScreenEnum getEnum() {
        return screenEnum;
    }

    @Override
    public List<String> formatSpeech(BikeStateTracker bikeStateTracker) {
        List<String> dialogs = new ArrayList<>();

        dialogs.add("Program " + programNumber + ".");

        long currentTime = System.currentTimeMillis();
        if (currentTime - TIME_BETWEEN_LONG_SPEECHES > timeGivenLongSpeech) {
            dialogs.add("Rotate to select a program, then press start.");
            timeGivenLongSpeech = currentTime;
        }

        return dialogs;
    }

    @Override
    public int getSpeakDelay() {
        return 4000;
    }

    @Override
    public boolean isSpeakFirst() {
        return true;
    }

    public static class ProgramScreen1 extends ProgramScreen {
        public ProgramScreen1() {
            super(1, ScreenEnum.PROGRAM1);
        }
    }
    public static class ProgramScreen2 extends ProgramScreen {
        public ProgramScreen2() {
            super(2, ScreenEnum.PROGRAM2);
        }
    }
    public static class ProgramScreen3 extends ProgramScreen {
        public ProgramScreen3() {
            super(3, ScreenEnum.PROGRAM3);
        }
    }
    public static class ProgramScreen4 extends ProgramScreen {
        public ProgramScreen4() {
            super(4, ScreenEnum.PROGRAM4);
        }
    }
    public static class ProgramScreen5 extends ProgramScreen {
        public ProgramScreen5() {
            super(5, ScreenEnum.PROGRAM5);
        }
    }
    public static class ProgramScreen6 extends ProgramScreen {
        public ProgramScreen6() {
            super(6, ScreenEnum.PROGRAM6);
        }
    }
    public static class ProgramScreen7 extends ProgramScreen {
        public ProgramScreen7() {
            super(7, ScreenEnum.PROGRAM7);
        }
    }
    public static class ProgramScreen8 extends ProgramScreen {
        public ProgramScreen8() {
            super(8, ScreenEnum.PROGRAM8);
        }
    }
    public static class ProgramScreen9 extends ProgramScreen {
        public ProgramScreen9() {
            super(9, ScreenEnum.PROGRAM9);
        }
    }
    public static class ProgramScreen10 extends ProgramScreen {
        public ProgramScreen10() {
            super(10, ScreenEnum.PROGRAM10);
        }
    }
    public static class ProgramScreen11 extends ProgramScreen {
        public ProgramScreen11() {
            super(11, ScreenEnum.PROGRAM11);
        }
    }
    public static class ProgramScreen12 extends ProgramScreen {
        public ProgramScreen12() {
            super(12, ScreenEnum.PROGRAM12);
        }
    }
}
