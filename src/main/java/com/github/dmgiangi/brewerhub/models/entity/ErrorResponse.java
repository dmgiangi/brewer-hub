package com.github.dmgiangi.brewerhub.models.entity;

import com.google.gson.annotations.SerializedName;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.sql.Timestamp;

@Getter
@Setter
@Accessors(chain = true)
@EqualsAndHashCode
public class ErrorResponse {
    public ErrorResponse(String message, String statusCode) {
        timestamp = new Timestamp(System.currentTimeMillis()).toString();
        this.message = message;
        this.statusCode = statusCode;
    }

    @SerializedName("timestamp")
    private String timestamp;

    @SerializedName("message")
    private String message;

    @SerializedName("status-code")
    private String statusCode;
}
