package com.il.util;

/**
 * Created by ulmasov_im on 07.06.2016.
 */
public class UIFormatter {
    public static String getHighlightMessage(String s){
        return "<div class=\"ui-state-highlight ui-corner-all\" style=\"margin-top: 10px; padding: 0 .7em;\">\n" +
                "\t\t<p><span class=\"ui-icon ui-icon-info\" style=\"float: left; margin-right: .3em;\"></span>\n" +
                "\t\t"+s+"</p>\n" +
                "\t</div>";
    }
    public static String getErrorMessage(String s){

        return "<div class=\"ui-state-error ui-corner-all\" style=\"margin-top: 10px; padding: 0 .7em;\">\n" +
                "\t\t<p><span class=\"ui-icon ui-icon-alert\" style=\"float: left; margin-right: .3em;\"></span>\n" +
                "\t\t"+s+"</p>\n" +
                "\t</div>";
    }

}
