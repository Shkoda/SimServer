package server;


import net.structure.Envelope;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;

/**
 * Created with IntelliJ IDEA.
 * User: Nightingale
 * Date: 1/26/13
 * Time: 3:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class PacketEncoder extends OneToOneEncoder {

    public static PacketEncoder getInstance() {
        return InstanceHolder.INSTANCE;
    }

    public static ChannelBuffer encodeMessage(Envelope message)
            throws IllegalArgumentException {

        // type(1b) + payload length(4b) + payload(nb)
        int size = 5 + message.getData().length;

        ChannelBuffer buffer = ChannelBuffers.buffer(size);
        buffer.writeByte(message.getType().getByteValue());
        buffer.writeInt(message.getData().length);
        buffer.writeBytes(message.getData());
        return buffer;
    }


    @Override
    public Object encode(ChannelHandlerContext channelHandlerContext,
                            Channel channel, Object msg) throws Exception {
        if (msg instanceof Envelope) {
            return encodeMessage((Envelope) msg);
        } else {
            return msg;
        }
    }

    private static final class InstanceHolder {
        private static final PacketEncoder INSTANCE = new PacketEncoder();
    }
}
