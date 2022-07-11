package com.github.dmgiangi.brewerhub.models.entity;

import com.github.dmgiangi.brewerhub.utilities.Indent;

import java.util.ArrayList;

//TOTO Write JAVADOC for this class
public class FoodPairings extends ArrayList<String> {
    @Override
    public String toString() {
        return "Class FoodPairings: {\n" +
                "    " + Indent.toIndentedString(super.toString()) + "\n" +
                "}";
    }
}
