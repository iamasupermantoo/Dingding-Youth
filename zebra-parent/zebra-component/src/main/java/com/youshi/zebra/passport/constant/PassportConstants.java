package com.youshi.zebra.passport.constant;

/**
 * @author wangsch
 *
 * @date 2016-09-12
 */
public interface PassportConstants {

    String TICKET_NAME = "z";

    int SALT_LENGTH = 8;

    String NEW_TICKET_SEPARTOR = "$";

    byte[] TICKET_PUBLIC_KEY = { 56, -73, -101, 20, -93, 91, 4, 66, -48, -32, 17, -24, -13, 115,
            119, 17 };

    byte[] SKIP32KEY = { 0x22, 0x12, 0x0B, 0x52, 0x44, 0x19, 0x27, 0x6F, 0x1E, 0x1C };

}
