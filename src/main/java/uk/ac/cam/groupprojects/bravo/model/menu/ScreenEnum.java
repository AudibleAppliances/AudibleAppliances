package uk.ac.cam.groupprojects.bravo.model.menu;

/**
 * Created by david on 20/02/2018.
 */
public enum ScreenEnum {
    OFF_SCREEN,

    ERROR_SCREEN,
    INITIAL_SCREEN,
    RUNNING_SCREEN,
    PAUSED_SCREEN,

    PROGRAM,
    SELECT_MANUAL,
    SELECT_PROGRAM,
    SELECT_USER_PROGRAM,
    SELECT_HRC,
    SELECT_WATTS;

    public BikeScreen getBikeScreen() {
        switch(this) {
            case OFF_SCREEN: return new OffScreen();
            case ERROR_SCREEN: return new ErrorScreen();
            case INITIAL_SCREEN: return new InitialScreen();
            case RUNNING_SCREEN: return new RunningScreen();
            case PAUSED_SCREEN: return new PausedScreen();
            case PROGRAM: return new ProgramScreen();
            case SELECT_MANUAL: return new SelectManualScreen();
            case SELECT_PROGRAM: return new SelectProgramScreen();
            case SELECT_USER_PROGRAM: return new SelectUserProgramScreen();
            case SELECT_HRC: return new SelectHRCScreen();
            case SELECT_WATTS: return new SelectWattScreen();
            default: return null;
        }
    }
}