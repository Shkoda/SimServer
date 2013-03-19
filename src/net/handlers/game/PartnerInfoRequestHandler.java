package net.handlers.game;

import com.google.protobuf.InvalidProtocolBufferException;
import data.Player;
import net.handlers.IncomingMessageHandler;
import net.protobuf.MessagesProtos;
import server.UserWorkerThread;

/**
 * Created with IntelliJ IDEA.
 * User: Nightingale
 * Date: 1/31/13
 * Time: 9:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class PartnerInfoRequestHandler extends IncomingMessageHandler{
    private MessagesProtos.CPartnerInfoRequest packet;

    public PartnerInfoRequestHandler(byte[] data) throws InvalidProtocolBufferException {
        packet = MessagesProtos.CPartnerInfoRequest.parseFrom(data);
    }

    @Override
    public void handle(UserWorkerThread userWorkerThread) {
        Player partner = userWorkerThread.getGameTable().getPartner(userWorkerThread).getPlayer();
        userWorkerThread.write(MessagesProtos.SPartnerInfo.newBuilder()
        .setName(partner.getName())
        .setScore(partner.getScore())
        .build());

    }
}
