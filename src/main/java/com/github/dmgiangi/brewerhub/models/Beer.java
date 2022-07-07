package com.github.dmgiangi.brewerhub.models;

import com.github.dmgiangi.brewerhub.utilities.Indent;
import com.google.gson.annotations.SerializedName;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@EqualsAndHashCode
public class Beer {
  @SerializedName("id")
  private Integer id = null;

  @SerializedName("name")
  private String name = null;

  @SerializedName("tagline")
  private String tagline = null;

  @SerializedName("first_brewed")
  private String firsBrewed = null;

  @SerializedName("description")
  private String description = null;

  @SerializedName("image_url")
  private String imageUrl = null;

  @SerializedName("abv")
  private Float abv = null;

  @SerializedName("ibu")
  private Float ibu = null;

  @SerializedName("target_fg")
  private Float targetFg = null;

  @SerializedName("target_og")
  private Float targetOg = null;

  @SerializedName("ebc")
  private Float ebc = null;

  @SerializedName("srm")
  private Float srm = null;

  @SerializedName("ph")
  private Float ph = null;

  @SerializedName("attenuation_level")
  private Float attenuationLevel = null;

  @SerializedName("volume")
  private Volume volume = null;

  @SerializedName("boil_volume")
  private Volume boilVolume = null;

  @SerializedName("method")
  private Method method = null;

  @SerializedName("ingredients")
  private Ingredients ingredients = null;

  @SerializedName("brewers_tips")
  private String brewersTips = null;

  @SerializedName("food_pairing")
  private FoodPairings foodPairings = null;

  @SerializedName("contributed_by")
  private String contributor = null;

  @Override
  public String toString() {

    return "Class Beer: {\n" +
            "    id: " + Indent.toIndentedString(id) + "\n" +
            "    name: " + Indent.toIndentedString(name) + "\n" +
            "    tagline: " + Indent.toIndentedString(tagline) + "\n" +
            "    firsBrewed: " + Indent.toIndentedString(firsBrewed) + "\n" +
            "    description: " + Indent.toIndentedString(description) + "\n" +
            "    imageUrl: " + Indent.toIndentedString(imageUrl) + "\n" +
            "    abv: " + Indent.toIndentedString(abv) + "\n" +
            "    ibu: " + Indent.toIndentedString(ibu) + "\n" +
            "    targetFg: " + Indent.toIndentedString(targetFg) + "\n" +
            "    targetOg: " + Indent.toIndentedString(targetOg) + "\n" +
            "    ebc: " + Indent.toIndentedString(ebc) + "\n" +
            "    srm: " + Indent.toIndentedString(srm) + "\n" +
            "    ph: " + Indent.toIndentedString(ph) + "\n" +
            "    attenuationLevel: " + Indent.toIndentedString(attenuationLevel) + "\n" +
            "    contributed_by: " + Indent.toIndentedString(contributor) + "\n" +
            "    volume: " + Indent.toIndentedString(volume) + "\n" +
            "    boilVolume: " + Indent.toIndentedString(boilVolume) + "\n" +
            "    method: " + Indent.toIndentedString(method) + "\n" +
            "    ingredients: " + Indent.toIndentedString(ingredients) + "\n" +
            "    food_pairing: " + Indent.toIndentedString(foodPairings) + "\n" +
            "}";
  }
}
