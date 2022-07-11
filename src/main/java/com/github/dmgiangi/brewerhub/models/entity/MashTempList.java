package com.github.dmgiangi.brewerhub.models.entity;

import com.github.dmgiangi.brewerhub.utilities.Indent;

import java.util.ArrayList;

//TOTO Write JAVADOC for this class
public class MashTempList extends ArrayList<MashTemp> {
    @Override
    public String toString() {
        return "Class MashTempList: {\n" +
                "    " + Indent.toIndentedString(super.toString()) + "\n" +
                "}";
    }
}
