package uk.ac.cam.groupprojects.bravo.model.menu;

/**
 * Created by david on 20/02/2018.
 */
public enum ScreenEnum {
    OFF_SCREEN,

    INVALID_SCREEN,
    ERROR_SCREEN,
    INITIAL_SCREEN,
    RUNNING_SCREEN,
    PAUSED_SCREEN,

    PROGRAM1,
    PROGRAM2,
    PROGRAM3,
    PROGRAM4,
    PROGRAM5,
    PROGRAM6,
    PROGRAM7,
    PROGRAM8,
    PROGRAM9,
    PROGRAM10,
    PROGRAM11,
    PROGRAM12,
    SELECT_MANUAL,
    SELECT_PROGRAM,
    SELECT_USER_PROGRAM,
    SELECT_HRC,
    SELECT_WATTS;

    public BikeScreen getBikeScreen() {
        switch(this) {
            case OFF_SCREEN: return new OffScreen();
            case INVALID_SCREEN: return new InvalidScreen();
            case ERROR_SCREEN: return new ErrorScreen();
            case INITIAL_SCREEN: return new InitialScreen();
            case RUNNING_SCREEN: return new RunningScreen();
            case PAUSED_SCREEN: return new PausedScreen();
            case PROGRAM1: return new ProgramScreen.ProgramScreen1();
            case PROGRAM2: return new ProgramScreen.ProgramScreen2();
            case PROGRAM3: return new ProgramScreen.ProgramScreen3();
            case PROGRAM4: return new ProgramScreen.ProgramScreen4();
            case PROGRAM5: return new ProgramScreen.ProgramScreen5();
            case PROGRAM6: return new ProgramScreen.ProgramScreen6();
            case PROGRAM7: return new ProgramScreen.ProgramScreen7();
            case PROGRAM8: return new ProgramScreen.ProgramScreen8();
            case PROGRAM9: return new ProgramScreen.ProgramScreen9();
            case PROGRAM10: return new ProgramScreen.ProgramScreen10();
            case PROGRAM11: return new ProgramScreen.ProgramScreen11();
            case PROGRAM12: return new ProgramScreen.ProgramScreen12();
            case SELECT_MANUAL: return new SelectManualScreen();
            case SELECT_PROGRAM: return new SelectProgramScreen();
            case SELECT_USER_PROGRAM: return new SelectUserProgramScreen();
            case SELECT_HRC: return new SelectHRCScreen();
            case SELECT_WATTS: return new SelectWattScreen();
            default: return null;
        }
    }
}