package net.structure;


import net.protobuf.MessagesProtos;

import java.util.HashMap;

public enum Type {

    // from Client
    CLOGIN((byte) 0x01),
    CNEWGAME((byte) 0x02),
    CNEWTURN((byte) 0x03),
    CINTERRUPT((byte) 0x04),
    CCONTINUE((byte) 0x05),
    CPARTNERINFO((byte) 0x06),
    CLOGOUT((byte) 0x07),


    // from Server
    SGAMECONFIG((byte) 0x11, MessagesProtos.SGameConfig.class),
    SAUTHORISATION((byte) 0x12, MessagesProtos.SAuthorisation.class),
    SNEWGAMEREQUEST((byte) 0x13, MessagesProtos.SNewGameRequestAnswer.class),
    SPARNERCONNECTED((byte) 0x14, MessagesProtos.SPartnerConnected.class),
    SNEWTURN((byte) 0x15, MessagesProtos.SNewTurn.class),
    SGAMERESULT((byte) 0x16, MessagesProtos.SGameResult.class),
    SINTERRUPT((byte) 0x17, MessagesProtos.BInterruptGame.class),
    SCONTINUE((byte) 0x18, MessagesProtos.BContinueGame.class),
    SPARTNERINFO((byte) 0x19, MessagesProtos.SPartnerInfo.class),
    SSECONDLOGIN((byte) 0x1A, MessagesProtos.SLoginFromAnotherClient.class),


    UNKNOWN((byte) 0x00);

    private  byte b;

    private Class protoClass;

    private final static HashMap<Class, Type> classMap;
    private final static HashMap<Byte, Type> byteMap;

    static {
        classMap = new HashMap<>(64);
        byteMap = new HashMap<>(64);
        for (Type type : values()) {
            classMap.put(type.protoClass, type);
            if (type.protoClass == null) byteMap.put(type.b, type);
        }
    }

    private Type(byte b) {
        this.b = b;
    }

    private Type(byte b, Class protoClass) {
        this.b = b;
        this.protoClass = protoClass;
    }

    public static Type fromByte(byte b) {
        return byteMap.get(b);
    }

    public static Type fromClass(Class c) {
        return classMap.get(c);
    }

    public byte getByteValue() {
        return b;
    }

}
