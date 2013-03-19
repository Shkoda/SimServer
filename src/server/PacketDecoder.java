package server;


import net.structure.Envelope;
import net.structure.PacketStructure;
import net.structure.ProtoFactory;
import net.structure.Type;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.handler.codec.replay.ReplayingDecoder;

/**
 * Created with IntelliJ IDEA.
 * User: Nightingale
 * Date: 1/26/13
 * Time: 3:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class PacketDecoder extends ReplayingDecoder<PacketStructure> {

    private Envelope message = new Envelope();


    @Override
    public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e)
            throws Exception {
        ctx.sendUpstream(e);
    }

    public PacketDecoder() {
        this.reset();
    }

    @Override
    public Object decode(ChannelHandlerContext channelHandlerContext,
                            Channel channel, ChannelBuffer channelBuffer,
                            PacketStructure packetStructure) throws Exception {

        switch (packetStructure) {
            case TYPE:
                Type type = Type.fromByte(channelBuffer.readByte());
                this.message.setType(type);
                checkpoint(PacketStructure.LENGTH);

            case LENGTH:
                int size = channelBuffer.readInt();
                if (size < 0) {
                    throw new Exception("Invalid content size");
                }
                byte[] content = new byte[size];
                this.message.setData(content);
                checkpoint(PacketStructure.DATA);

            case DATA:
                channelBuffer.readBytes(this.message.getData(), 0, this.message.getData().length);
                try {
                    return  ProtoFactory.createSpecificHandler(
                            this.message.getData(), this.message.getType());

                } finally {
                    this.reset();
                }
            default:
                throw new Exception("Unknown decoding state: " + packetStructure);
        }
    }

    private void reset() {
        checkpoint(PacketStructure.TYPE);
        this.message = new Envelope();
    }

    @Override
    public void channelDisconnected(ChannelHandlerContext ctx,
                                    ChannelStateEvent e) throws Exception {
        ctx.sendUpstream(e);
    }
}
