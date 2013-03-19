package net.handlers.connection;

import com.google.protobuf.InvalidProtocolBufferException;
import net.handlers.IncomingMessageHandler;
import net.protobuf.MessagesProtos;
import server.UserWorkerThread;

/**
 * Created with IntelliJ IDEA.
 * User: Nightingale
 * Date: 1/30/13
 * Time: 2:53 PM
 * To change this template use File | Settings | File Templates.
 */


public class ContinueGameHandler extends IncomingMessageHandler {
    private MessagesProtos.BContinueGame packet;

    public ContinueGameHandler(byte[] data) throws InvalidProtocolBufferException {
        this.packet = MessagesProtos.BContinueGame.parseFrom(data);
    }

    @Override
    public void handle(UserWorkerThread userWorkerThread) {
        UserWorkerThread partnerWorker = userWorkerThread.getGameTable().getPartner(userWorkerThread);
        partnerWorker.write(MessagesProtos.BContinueGame.newBuilder().setNothing(true).build());
    }
}
