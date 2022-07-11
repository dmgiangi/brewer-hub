package com.github.dmgiangi.brewerhub.models.entity;

import com.github.dmgiangi.brewerhub.utilities.Indent;

import java.util.ArrayList;

//TODO Write JAVADOC for this class
public class MaltList  extends ArrayList<Malt> {
  @Override
  public String toString() {
    return "Class MaltList: {\n" +
            "    malts: " + Indent.toIndentedString(super.toString()) + "\n" +
            "}";
  }
}
