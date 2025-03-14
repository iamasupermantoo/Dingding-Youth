package com.dorado.gotopage.utils;

import java.io.IOException;
import java.util.Map.Entry;

//import com.dorado.core.web.view.model.GotoPageView;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * @author wangsch
 *
 * @date 2016-09-12
 */
//public class GotoPageViewSerializer extends JsonSerializer<GotoPageView> {
//
//    @Override
//    public void serialize(GotoPageView value, JsonGenerator jgen, SerializerProvider provider)
//            throws IOException {
//        jgen.writeStartObject();
//        jgen.writeNumberField("t", value.getGotoPageModel().getGotoPage());
//        for (Entry<String, Object> entry : value.getGotoPageModel().getParams().entrySet()) {
//            if (entry.getValue() instanceof Number) {
//                jgen.writeNumberField(entry.getKey(), ((Number) entry.getValue()).intValue());
//            } else {
//                jgen.writeStringField(entry.getKey(), entry.getValue().toString());
//            }
//        }
//        jgen.writeEndObject();
//    }
//
//}
