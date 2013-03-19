package net.handlers.game;

import com.google.protobuf.InvalidProtocolBufferException;
import data.GameKeeper;
import data.GameTable;
import net.handlers.IncomingMessageHandler;
import net.protobuf.MessagesProtos;
import server.UserWorkerThread;
import utils.Logger;


/**
 * Created with IntelliJ IDEA.
 * User: Nightingale
 * Date: 1/27/13
 * Time: 4:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class NewGameRequestHandler extends IncomingMessageHandler {
    private MessagesProtos.CRequestNewGame packet;

    public NewGameRequestHandler(byte[] data) throws InvalidProtocolBufferException {
        packet = MessagesProtos.CRequestNewGame.parseFrom(data);
    }

    @Override
    public void handle(UserWorkerThread userWorkerThread) {


        GameKeeper gameKeeper = userWorkerThread.getGameKeeper();
        synchronized (gameKeeper) {
            GameTable table = gameKeeper.getTableWithOnePlayer();
            if (table == null) {                                 //if nobody waits for partner
                table = new GameTable(userWorkerThread);        //create new game and wait for partner
                userWorkerThread.setGameTable(table);
                gameKeeper.setTableWithOnePlayer(table);
                userWorkerThread.write(MessagesProtos.SNewGameRequestAnswer.newBuilder()
                        .setStartGame(false)
                        .setFirstTurnAccess(true)
                        .build());


            } else {         //if somebody waits for game

                UserWorkerThread partnerWorker = table.getFirstPlayerWorker();      //partner worker

                if (!partnerWorker.equals(userWorkerThread)) {
                    table.setSecondPlayer(userWorkerThread);                            //occupy table
                    userWorkerThread.setGameTable(table);
                    gameKeeper.setTableWithOnePlayer(null);                             //release buffer table
                    partnerWorker.write(MessagesProtos.SPartnerConnected.newBuilder().setNothing(true).build());  //send start game message to partner
                    userWorkerThread.write(MessagesProtos.SNewGameRequestAnswer.newBuilder()
                            .setStartGame(true)
                            .setFirstTurnAccess(false)
                            .build());


                    Logger.newGameStartInfo(partnerWorker.getPlayer().getName(),
                            userWorkerThread.getPlayer().getName(),
                            table);
                }
            }
        }
    }


}
