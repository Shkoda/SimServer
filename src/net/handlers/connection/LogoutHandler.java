package net.handlers.connection;

import com.google.protobuf.InvalidProtocolBufferException;
import data.GameTable;
import database.DBHandler;
import net.handlers.IncomingMessageHandler;
import net.protobuf.MessagesProtos;
import server.UserWorkerThread;


/**
 * Created with IntelliJ IDEA.
 * User: Nightingale
 * Date: 1/27/13
 * Time: 4:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class LogoutHandler extends IncomingMessageHandler {
    private MessagesProtos.CLogout packet;

    public LogoutHandler(byte[] data) throws InvalidProtocolBufferException {
        packet = MessagesProtos.CLogout.parseFrom(data);

    }

    @Override
    public void handle(UserWorkerThread userWorkerThread) {

        DBHandler.changeOnlineStatus(userWorkerThread.getPlayer().getName(), false);
        GameTable gameTable = userWorkerThread.getGameTable();
        if (gameTable != null) {
            UserWorkerThread partnerWorker = gameTable.getPartner(userWorkerThread);
            if (partnerWorker != null) {
                partnerWorker.write(MessagesProtos.BInterruptGame.newBuilder()
                        .setOnlyPause(false)
                        .build());
                partnerWorker.setGameTable(null);

            } else {
                userWorkerThread.getGameKeeper().setTableWithOnePlayer(null);
            }

        }

    }
}
