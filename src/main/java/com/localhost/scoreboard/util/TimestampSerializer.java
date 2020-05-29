package com.localhost.scoreboard.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class TimestampSerializer extends StdSerializer<Timestamp> {

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy MMM dd HH:mm:ss");

    public TimestampSerializer(){
        this(null);
    }

    public TimestampSerializer(Class<Timestamp> t) {
        super(t);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+3"));
    }

    @Override
    public void serialize(Timestamp timestamp, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
            throws IOException {
        jsonGenerator.writeString(TimestampSerializer.format(timestamp));
    }

    public static String format(Timestamp timestamp) {
        Date date = new Date();
        date.setTime(timestamp.getTime());
        return simpleDateFormat.format(date);
    }
}
