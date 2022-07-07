package com.github.dmgiangi.brewerhub.models;

import java.io.IOException;
import java.util.Map;

import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

// TODO write javadoc for thi class
@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonAdapter(WeightUnits.Adapter.class)
public enum WeightUnits {
  GRAMS("grams"),
  KILOGRAMS("kilograms"),
  OUNCE("ounce"),
  POUNDS("pounds");

  @Getter
  private final String value;

  public static WeightUnits fromValue(String input) {
    for (WeightUnits b : WeightUnits.values()) {
      if (b.value.equalsIgnoreCase(input)) {
        return b;
      }
    }
    return null;
  }

  //TODO set the correct conversion values
  public static float getConvertedValue(Float oldValue, WeightUnits from, WeightUnits to){
    if (from != null && to != null) {
      if(!from.value.equals(to.value)){
         Map<String, Float> convertTable = Map.ofEntries(
                 Map.entry(WeightUnits.GRAMS.getValue() + WeightUnits.KILOGRAMS.getValue(), 0.001f),
                 Map.entry(WeightUnits.GRAMS.getValue() + WeightUnits.OUNCE.getValue(), 0.001f),
                 Map.entry(WeightUnits.GRAMS.getValue() + WeightUnits.POUNDS.getValue(), 0.001f),
                 Map.entry(WeightUnits.KILOGRAMS.getValue() + WeightUnits.GRAMS.getValue(), 0.001f),
                 Map.entry(WeightUnits.KILOGRAMS.getValue() + WeightUnits.OUNCE.getValue(), 0.001f),
                 Map.entry(WeightUnits.KILOGRAMS.getValue() + WeightUnits.POUNDS.getValue(), 0.001f),
                 Map.entry(WeightUnits.OUNCE.getValue() + WeightUnits.GRAMS.getValue(), 0.001f),
                 Map.entry(WeightUnits.OUNCE.getValue() + WeightUnits.KILOGRAMS.getValue(), 0.001f),
                 Map.entry(WeightUnits.OUNCE.getValue() + WeightUnits.POUNDS.getValue(), 0.001f),
                 Map.entry(WeightUnits.POUNDS.getValue() + WeightUnits.GRAMS.getValue(), 0.001f),
                 Map.entry(WeightUnits.POUNDS.getValue() + WeightUnits.OUNCE.getValue(), 0.001f)
         );
         return oldValue * convertTable.get(from.value + to.value);
      }
    }
    return oldValue;
  }

  public static class Adapter extends TypeAdapter<WeightUnits> {
    @Override
    public void write(final JsonWriter jsonWriter, final WeightUnits enumeration) throws IOException {
      jsonWriter.value(String.valueOf(enumeration.getValue()));
    }

    @Override
    public WeightUnits read(final JsonReader jsonReader) throws IOException {
      String value = jsonReader.nextString();
      return WeightUnits.fromValue(value);
    }
  }
}
