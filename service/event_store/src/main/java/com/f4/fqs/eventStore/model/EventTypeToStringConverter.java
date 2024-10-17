package com.f4.fqs.eventStore.model;

import com.f4.fqs.commons.kafka_common.message.EventType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

@ReadingConverter
public class EventTypeToStringConverter implements Converter<EventType, String> {

    @Override
    public String convert(EventType type) {
        return type.name();
    }

}
