package net.handlers.connection;

import com.google.protobuf.InvalidProtocolBufferException;
import net.handlers.IncomingMessageHandler;
import net.protobuf.MessagesProtos;
import server.UserWorkerThread;

/**
 * Created with IntelliJ IDEA.
 * User: Nightingale
 * Date: 1/30/13
 * Time: 2:52 PM
 * To change this template use File | Settings | File Templates.
 */


public class InterruptHandler extends IncomingMessageHandler {
    private MessagesProtos.BInterruptGame packet;

    public InterruptHandler(byte[] data) throws InvalidProtocolBufferException {
        this.packet = MessagesProtos.BInterruptGame.parseFrom(data);
    }

    @Override
    public void handle(UserWorkerThread userWorkerThread) {
        boolean onlyPause = packet.getOnlyPause();

        UserWorkerThread partner = userWorkerThread.getGameTable().getPartner(userWorkerThread);
        partner.write(MessagesProtos.BInterruptGame.newBuilder().setOnlyPause(onlyPause).build());


    }
}