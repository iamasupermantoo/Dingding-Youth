//package com.dorado.gotopage.model;
//
//import java.util.Collections;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//import org.apache.commons.lang3.StringUtils;
//
//import com.fasterxml.jackson.annotation.JsonIgnore;
//import com.fasterxml.jackson.annotation.JsonInclude;
//import com.fasterxml.jackson.annotation.JsonInclude.Include;
//import com.fasterxml.jackson.annotation.JsonProperty;
//import com.google.common.base.Splitter;
//import com.dorado.gotopage.constant.GotoPage;
//
///**
// * @author wangsch
// *
// * @date 2016-09-12
// */
//public class GotoPageModel {
//
//    private int gotoPage;
//
//    private Map<String, Object> params;
//
//    private String i, s;
//
//    public GotoPageModel() {
//        super();
//    }
//
//    public GotoPageModel(GotoPage gotoPage) {
//        this(gotoPage, Collections.emptyMap());
//    }
//
//    /**
//     * @param gotoPage
//     * @param params
//     */
//    public GotoPageModel(GotoPage gotoPage, Map<String, Object> params) {
//        this.gotoPage = gotoPage.getValue();
//        this.params = params == null ? Collections.emptyMap() : params;
//        i = getI();
//        s = getS();
//    }
//    
//    @JsonProperty(value = "t")
//    public int getGotoPage() {
//        return gotoPage;
//    }
//
//    @JsonInclude(value = Include.NON_NULL)
//    public String getI() {
//        return params != null && params.containsKey("i") ? params.get("i").toString() : i;
//    }
//
//    @JsonInclude(value = Include.NON_NULL)
//    public String getS() {
//        return params != null && params.containsKey("s") ? params.get("s").toString() : s;
//    }
//
//    @JsonIgnore
//    public Map<String, Object> getParams() {
//        return params;
//    }
//
//    // setters
//    public void setGotoPage(int gotoPage) {
//        this.gotoPage = gotoPage;
//    }
//
//    public void setParams(Map<String, Object> params) {
//        this.params = params;
//    }
//
//    public void setS(String s) {
//        this.s = s;
//    }
//
//    public void setI(String i) {
//        this.i = i;
//    }
//
//    public static GotoPageModel parse(int gotoPage, String param) {
//        Map<String, Object> params = StringUtils.isEmpty(param)
//                ? Collections.<String, Object> emptyMap()
//                : Splitter.on(',').splitToList(param).stream().map((item) -> item.split("="))
//                .collect(Collectors.toMap((i) -> i[0], (i) -> i[1]));
//        return new GotoPageModel(GotoPage.fromValue(gotoPage), params);
//
//    }
//}
