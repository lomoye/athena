package com.lomoye.common.serialization;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.text.SimpleDateFormat;

/**
 * Created by tommy on 2015/6/23.
 */
public class JacksonObjectMapper extends ObjectMapper {
    public JacksonObjectMapper() {
        super();
        //new Date("yyyy-MM-dd HH:mm:ss") firefox不支持
        setDateFormat(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"));

        setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }
}
