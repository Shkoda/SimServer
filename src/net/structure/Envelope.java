package net.structure;


public class Envelope {
    protected Type type;
    protected int length;
    protected byte[] data;

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public Envelope() {
    }

    public Envelope(Type type, int length, byte[] data) {
        this.type = type;
        this.length = length;
        this.data = data;
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append(type)
                .append(", data=").append(data == null ? null : data.length + " bytes")
                .toString();
    }

}