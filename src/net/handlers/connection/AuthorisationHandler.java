package net.handlers.connection;

import com.google.protobuf.InvalidProtocolBufferException;
import data.GameKeeper;
import data.GameTable;
import database.DBHandler;
import net.handlers.IncomingMessageHandler;
import net.protobuf.MessagesProtos;
import server.UserWorkerThread;
import data.Player;
import utils.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Nightingale
 * Date: 1/27/13
 * Time: 4:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class AuthorisationHandler extends IncomingMessageHandler {
    private MessagesProtos.CAuthRequest packet;

    public AuthorisationHandler(byte[] data) throws InvalidProtocolBufferException {
        this.packet = MessagesProtos.CAuthRequest.parseFrom(data);
    }


    @Override
    public void handle(UserWorkerThread userWorkerThread) {

        String name = packet.getName();
        String password = packet.getPassword();

        /*  connectionResult =
         * 0, if  password doesn't match
         * 1, if  password is valid.
         * 2, if new player was created.
         * 3, if was online
         */
        int connectionResult = DBHandler.connectPlayer(name, password);
        Logger.connectionInfo(connectionResult, name);


        if (connectionResult != 0) {

            Player player = DBHandler.getPlayerInfo(name);
            userWorkerThread.setPlayer(player);
            userWorkerThread.write(MessagesProtos.SAuthorisation.newBuilder()
                    .setIsSuccessful(true)
                    .setPlayerId(player.getId())
                    .setScore(player.getScore())
                    .build());

            if (connectionResult == 3) {     //player was already online
                GameKeeper gameKeeper = userWorkerThread.getGameKeeper();
                UserWorkerThread alterEgo = gameKeeper.getOnlinePlayers().get(player.getName());
                alterEgo.write(MessagesProtos.SLoginFromAnotherClient.newBuilder().setNothing(true).build());

                GameTable alterEgoTable = alterEgo.getGameTable();
                if (alterEgoTable != null) {

                    UserWorkerThread partner = alterEgoTable.getPartner(alterEgo);
                    if (partner != null) {      //if was in game
                        partner.write(MessagesProtos.BInterruptGame.newBuilder().setOnlyPause(false).build());
                        partner.setGameTable(null);
                        alterEgo.setGameTable(null);
                    } else {                  //if was waiting for game
                        synchronized (gameKeeper) {
                            gameKeeper.setTableWithOnePlayer(null);
                        }
                    }
                }
            }

            userWorkerThread.getGameKeeper().getOnlinePlayers().put(player.getName(), userWorkerThread);
            return;
        }

        userWorkerThread.write(MessagesProtos.SAuthorisation.newBuilder()
                .setIsSuccessful(false)
                .setPlayerId(0)
                .setScore(0)
                .build());

    }

    /*

     */
}
