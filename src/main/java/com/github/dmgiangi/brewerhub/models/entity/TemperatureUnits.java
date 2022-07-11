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
@JsonAdapter(TemperatureUnits.Adapter.class)
public enum TemperatureUnits {
  CELSIUS("celsius"),
  FAHRENHEIT("fahrenheit"),
  KELVIN("kelvin");

  @Getter
  private final String value;

  public static TemperatureUnits fromValue(String input) {
    for (TemperatureUnits b : TemperatureUnits.values()) {
      if (b.value.equalsIgnoreCase(input)) {
        return b;
      }
    }
    return null;
  }

  public static Float getConvertedValue(Float value, TemperatureUnits from, TemperatureUnits to) {
    if (from != null && to != null) {
      if(!from.value.equals(to.value)){
        if (from == TemperatureUnits.CELSIUS) {
          if (to == TemperatureUnits.KELVIN) return value + 273.15f;
          else if(to == TemperatureUnits.FAHRENHEIT) return value * 9f / 5f + 32f;
        } else if (from == TemperatureUnits.FAHRENHEIT) {
          if (to == TemperatureUnits.KELVIN) return (value - 32f) * 5f / 9f + 273.15f;
          else if(to == TemperatureUnits.CELSIUS) return (value - 32f) * 5f / 9f;
        } else if (from == TemperatureUnits.KELVIN) {
          if (to == TemperatureUnits.CELSIUS) return value - 273.15f;
          else if(to == TemperatureUnits.FAHRENHEIT) return (value - 273.15f) * 9f / 5f + 32f;
        }
      }
    }
    return value;
  }

  public static class Adapter extends TypeAdapter<TemperatureUnits> {
    @Override
    public void write(final JsonWriter jsonWriter, final TemperatureUnits enumeration) throws IOException {
      jsonWriter.value(String.valueOf(enumeration.getValue()));
    }

    @Override
    public TemperatureUnits read(final JsonReader jsonReader) throws IOException {
      String value = jsonReader.nextString();
      return TemperatureUnits.fromValue(value);
    }
  }
}
