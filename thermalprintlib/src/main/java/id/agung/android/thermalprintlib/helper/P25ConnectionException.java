package id.agung.android.thermalprintlib.helper;

/**
 * Created by agung on 29/07/18.
 */

public class P25ConnectionException extends Exception {
    private static final long serialVersionUID = 1;
    String error;

    public P25ConnectionException(String msg){
        super(msg);
        this.error = "";
        this.error = msg;
    }

    public String getError(){
        return this.error;
    }
}
