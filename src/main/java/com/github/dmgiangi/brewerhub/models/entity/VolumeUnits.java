package com.github.dmgiangi.brewerhub.models.entity;

import java.io.IOException;

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
@JsonAdapter(VolumeUnits.Adapter.class)
public enum VolumeUnits {
  LITRES("litres"),
  GALLONS("gallons");

  @Getter
  private final String value;

  public static VolumeUnits fromValue(String input) {
    for (VolumeUnits b : VolumeUnits.values()) {
      if (b.value.equalsIgnoreCase(input)) {
        return b;
      }
    }
    return null;
  }

  public static Float getConvertedValue(Float oldValue, VolumeUnits from, VolumeUnits to) {
    if (from != null && to != null) {
      if(!from.value.equals(to.value)){
        if (from == VolumeUnits.LITRES) {
          return oldValue * 0.2641722f;
        } else if(from == VolumeUnits.GALLONS) {
          return oldValue * 3.78541f;
        }
      }
    }
    return oldValue;
  }

  public static class Adapter extends TypeAdapter<VolumeUnits> {
    @Override
    public void write(final JsonWriter jsonWriter, final VolumeUnits enumeration) throws IOException {
      jsonWriter.value(String.valueOf(enumeration.getValue()));
    }

    @Override
    public VolumeUnits read(final JsonReader jsonReader) throws IOException {
      String value = jsonReader.nextString();
      return VolumeUnits.fromValue(value);
    }
  }
}
