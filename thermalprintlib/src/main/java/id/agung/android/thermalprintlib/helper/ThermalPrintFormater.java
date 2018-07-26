package id.agung.android.thermalprintlib.helper;


// kelas ini digunakan untuk formatting pada output ESC / POS / ThermalPrinter

import java.util.ArrayList;
import java.util.List;

/**
 * Created by agung on 26/07/18.
 */

public class ThermalPrintFormater {
    public static String ESC                = "\u001B";
    public static String GS                 = "\u001D";
    public static String INITIALIZE_PRINTER = ESC + "@";

    //<b>
    public static String START_BOLD         = ESC + "E" + "\u0001";
    //</b>
    public static String END_BOLD           = ESC + "E" + "\0";

    //<h>
    public static String START_LARGE        = GS + "!" + "\u0011";
    //</h>
    public static String END_LARGE          = GS + "!" + "\0";

    //<u>
    public static String START_UNDERLINE    = ESC + "-" + "\u0001";
    //</u>
    public static String END_UNDERLINE      = ESC + "-" + "\0";

    //<i>
    public static String START_ITALIC       = ESC + "" + "\u0001";
    //</i>
    public static String END_ITALIC         = ESC + "" + "\0";

    //<br/>
    public static String NEW_LINE           = "\n" + ESC + "a0" + "\0";

    //<e/>
    public static String END_OF_PAGE        = "\n\n\n";

    //<t/>
    public static String TAB                = "\t";

    //<center>
    public static String START_CENTER       = ESC + "a1" + "\u0001";
    //</center>
    public static String END_CENTER         = ESC + "a1" + "\0";

    //<s>
    public static String START_SMALL        = GS + "!" + "\u0001";
    //</s>
    public static String END_SMALL          = GS + "!" + "\0";

    public static String format (String string){
        List<String> regexs         = getRegexChar();
        List<String> replacement    = getRepalacementChar();

        for (int i = 0; i < regexs.size(); i++){
            string = string.replace(regexs.get(i), replacement.get(i));
        }
        return string;
    }

    private static List<String> getRepalacementChar() {
        List<String> regex = new ArrayList<>();

        regex.add(START_BOLD);
        regex.add(END_BOLD);
        regex.add(START_LARGE);
        regex.add(END_LARGE);
        regex.add(START_UNDERLINE);
        regex.add(END_UNDERLINE);
        regex.add(START_ITALIC);
        regex.add(END_ITALIC);
        regex.add(NEW_LINE);
        regex.add(END_OF_PAGE);
        regex.add(TAB);
        regex.add(START_CENTER);
        regex.add(END_CENTER);
        regex.add(START_SMALL);
        regex.add(END_SMALL);

        return regex;
    }

    private static List<String> getRegexChar() {
        List<String> replacement = new ArrayList<>();
        replacement.add("<b>");
        replacement.add("</b>");
        replacement.add("<h>");
        replacement.add("</h>");
        replacement.add("<u>");
        replacement.add("</u>");
        replacement.add("<i>");
        replacement.add("</i>");
        replacement.add("<br/>");
        replacement.add("<e/>");
        replacement.add("<t/>");
        replacement.add("<center>");
        replacement.add("</center>");
        replacement.add("<s>");
        replacement.add("</s>");

        return replacement;
    }
}
