package uk.ac.cam.groupprojects.bravo.model.screen;

/**
 * Created by david on 12/02/2018.
 */
public enum LCDFunction {
    MANUAL(
            0,
            "manual",
            "10000000 10000000 10000000 10000000 10000000 10000000 10000000 10000000"
    ),

    NOT_DEF(
            -1,
            "not defined"
    )
    ;


    private String audibleName;
    private int programNo;
    private String[] values;

    LCDFunction( int programNo, String audibleName, String... values ){
        this.audibleName = audibleName;
        this.programNo = programNo;
        this.values = values;
    }


    public String[] getValues() {
        return values;
    }

    public String getAudibleName(){
        return audibleName;
    }

    public int getProgramNo(){
        return programNo;
    }
}
