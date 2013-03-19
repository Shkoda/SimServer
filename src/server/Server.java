package server;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;


import config.ServerConfig;
import data.GameKeeper;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.execution.ExecutionHandler;
import org.jboss.netty.handler.execution.OrderedMemoryAwareThreadPoolExecutor;
import data.GameTable;

public class Server {

    private static volatile GameTable tableWithOnePlayer;

    public static void main(String[] args) throws Exception {
        GameKeeper gameKeeper = new GameKeeper();
        runServer(gameKeeper);

    }

    private static void runServer(GameKeeper gameKeeper){
        ExecutorService bossExec = new OrderedMemoryAwareThreadPoolExecutor(1,
                0, 0, 60, TimeUnit.SECONDS);
        ExecutorService ioExec = new OrderedMemoryAwareThreadPoolExecutor(4,
                0, 0, 60, TimeUnit.SECONDS);
        ServerBootstrap networkServer = new ServerBootstrap(
                new NioServerSocketChannelFactory(bossExec, ioExec, 4));

        ExecutionHandler executionHandler = new ExecutionHandler(
                new OrderedMemoryAwareThreadPoolExecutor(4, 1048576, 1048576));

        networkServer.setOption("backlog", 50000);
        networkServer.setOption("connectTimeoutMillis", 10000);
        networkServer.setOption("child.tcpNoDelay", true);
        networkServer.setOption("child.keepAlive", true);
        networkServer.setOption("readWriteFair", true);

        networkServer.setPipelineFactory(new ServerPipelineFactory(executionHandler, gameKeeper));
        networkServer.bind(new InetSocketAddress(
                ServerConfig.DEFAULT_PORT));
    }
}
