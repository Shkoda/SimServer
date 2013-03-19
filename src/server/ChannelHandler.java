package server;

import config.GameConfig;
import data.GameKeeper;
import net.handlers.IncomingMessageHandler;
import net.protobuf.MessagesProtos;
import org.jboss.netty.channel.*;

import java.util.logging.Level;

/**
 * Created with IntelliJ IDEA.
 * User: Nightingale
 * Date: 1/26/13
 * Time: 3:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class ChannelHandler  extends SimpleChannelHandler {

    private UserWorkerThread worker;
    private volatile GameKeeper gameKeeper;


    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(
            ChannelHandler.class.getName());

    public ChannelHandler(GameKeeper gameKeeper) {
        this.gameKeeper = gameKeeper;
    }

    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e)
            throws Exception {
        worker = new UserWorkerThread(e.getChannel(), gameKeeper);
        logger.log(Level.WARNING, "channel connected");



        worker.write(MessagesProtos.SGameConfig.newBuilder().setVertexNumber(GameConfig.GRAPH_VERTEX_NUMBER).build());
    }

    @Override
    public void channelDisconnected(ChannelHandlerContext ctx,
                                    ChannelStateEvent e) throws Exception {
        worker.disconnectedFromChannel();
        logger.log(Level.WARNING, "channel disconnected");

    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {

        if (e.getChannel().isOpen()){
            worker.acceptPacket( (IncomingMessageHandler) e.getMessage());
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
        //	Logger.error("Exception from downstream", e.getCause(), null);
        ctx.getChannel().close();
    }
}
