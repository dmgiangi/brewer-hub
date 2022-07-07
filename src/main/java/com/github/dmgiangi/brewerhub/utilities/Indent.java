package com.github.dmgiangi.brewerhub.utilities;

public class Indent {
    public static String toIndentedString(java.lang.Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}
