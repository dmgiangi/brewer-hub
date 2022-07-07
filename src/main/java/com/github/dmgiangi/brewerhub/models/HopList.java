package com.github.dmgiangi.brewerhub.models;

import com.github.dmgiangi.brewerhub.utilities.Indent;

import java.util.ArrayList;

//TODO Write JAVADOC for this class
public class HopList extends ArrayList<Hop> {
  @Override
  public String toString() {
    return "Class HopList: {\n" +
            "    " + Indent.toIndentedString(super.toString()) + "\n" +
            "}";
  }
}
