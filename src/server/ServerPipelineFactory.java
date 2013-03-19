package server;

import data.GameKeeper;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.execution.ExecutionHandler;


public class ServerPipelineFactory implements ChannelPipelineFactory {

    private final ExecutionHandler executionHandler;
    private volatile GameKeeper gameKeeper;

    public ServerPipelineFactory(ExecutionHandler executionHandler, GameKeeper gameKeeper) {
        this.executionHandler = executionHandler;
        this.gameKeeper = gameKeeper;
    }

    @Override
    public ChannelPipeline getPipeline() throws Exception {

        PacketDecoder decoder = new PacketDecoder();
        PacketEncoder encoder = new PacketEncoder();

        ChannelPipeline pipeline = Channels.pipeline();

        pipeline.addLast("decoder", decoder);
        pipeline.addLast("encoder", encoder);
        //pipeline.addLast("threadPool", executionHandler);
        pipeline.addLast("handler", new ChannelHandler(gameKeeper));

        return pipeline;
    }
}