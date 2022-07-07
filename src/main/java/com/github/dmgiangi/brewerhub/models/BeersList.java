package com.github.dmgiangi.brewerhub.models;

import com.github.dmgiangi.brewerhub.utilities.Indent;

import java.util.ArrayList;

public class BeersList extends ArrayList<Beer> {
  @Override
  public String toString() {
    return "class BeersList: {\n" +
            "    " + Indent.toIndentedString(super.toString()) + "\n" +
            "}";
  }
}
