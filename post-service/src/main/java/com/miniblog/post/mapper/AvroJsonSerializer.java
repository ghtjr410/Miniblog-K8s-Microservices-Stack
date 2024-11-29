package com.miniblog.post.mapper;

import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.io.JsonEncoder;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Component
public class AvroJsonSerializer {
    public <T extends SpecificRecordBase> String serialize(T avroEvent) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            DatumWriter<T> datumWriter = new SpecificDatumWriter<>(avroEvent.getSchema());
            JsonEncoder jsonEncoder = EncoderFactory.get().jsonEncoder(avroEvent.getSchema(), byteArrayOutputStream);
            datumWriter.write(avroEvent, jsonEncoder);
            jsonEncoder.flush();
            byteArrayOutputStream.flush();

            return byteArrayOutputStream.toString();
        } catch (IOException ex) {
            throw new RuntimeException("Failed to serialize Avro event", ex);
        }
    }
}

