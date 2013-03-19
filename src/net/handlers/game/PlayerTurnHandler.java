package net.handlers.game;

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
 * Time: 4:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class PlayerTurnHandler extends IncomingMessageHandler {
    private MessagesProtos.CNewTurn packet;

    public PlayerTurnHandler(byte[] data) throws InvalidProtocolBufferException {
        packet = MessagesProtos.CNewTurn.parseFrom(data);
    }

    @Override
    public void handle(UserWorkerThread userWorkerThread) {
        int vertexFrom = packet.getVertexNumberFrom();
        int vertexTo = packet.getVertexNumberTo();
        int playerId = userWorkerThread.getPlayer().getId();

        GameTable table = userWorkerThread.getGameTable();
        UserWorkerThread partnerWorker = table.getPartner(userWorkerThread);

        int[] transactionResult = table.setTransitionAndCheckLose(playerId, vertexFrom, vertexTo);
        int loserId = transactionResult[0];

        switch (loserId) {
            case -1:      //continue game
                partnerWorker.write(MessagesProtos.SNewTurn.newBuilder()
                        .setPlayerId(playerId)
                        .setVertexNumberFrom(vertexFrom)
                        .setVertexNumberTo(vertexTo)
                        .setContinueGame(true)
                        .build());
                break;
            case -2:      //draw
                partnerWorker.write(MessagesProtos.SNewTurn.newBuilder()
                        .setPlayerId(playerId)
                        .setVertexNumberFrom(vertexFrom)
                        .setVertexNumberTo(vertexTo)
                        .setContinueGame(false)
                        .build());

                userWorkerThread.write(MessagesProtos.SGameResult.newBuilder()
                        .setGameResult(0)
                        .setVertex1(vertexFrom)
                        .setVertex2(vertexTo)
                        .setVertex3(transactionResult[1])
                        .build());

                partnerWorker.write(MessagesProtos.SGameResult.newBuilder()
                        .setGameResult(0)
                        .setVertex1(vertexFrom)
                        .setVertex2(vertexTo)
                        .setVertex3(transactionResult[1])
                        .build());
                userWorkerThread.setGameTable(null);
                partnerWorker.setGameTable(null);
                break;

            default:
                partnerWorker.write(MessagesProtos.SNewTurn.newBuilder()
                        .setPlayerId(playerId)
                        .setVertexNumberFrom(vertexFrom)
                        .setVertexNumberTo(vertexTo)
                        .setContinueGame(false)
                        .build());

                String playerName = userWorkerThread.getPlayer().getName();
                String partnerName = partnerWorker.getPlayer().getName();

                int partnerScore = DBHandler.getScore(partnerName);
                int playerScore = DBHandler.getScore(playerName);

                partnerScore++;
                playerScore--;

                DBHandler.setScore(playerName, playerScore);
                DBHandler.setScore(partnerName, partnerScore);

                userWorkerThread.write(MessagesProtos.SGameResult.newBuilder()
                        .setGameResult(-1)
                        .setNewScore(playerScore)
                        .setVertex1(vertexFrom)
                        .setVertex2(vertexTo)
                        .setVertex3(transactionResult[1])
                        .build());


                partnerWorker.write(MessagesProtos.SGameResult.newBuilder()
                        .setGameResult(1)
                        .setNewScore(partnerScore)
                        .setVertex1(vertexFrom)
                        .setVertex2(vertexTo)
                        .setVertex3(transactionResult[1])
                        .build());
                userWorkerThread.setGameTable(null);
                partnerWorker.setGameTable(null);
                break;
        }

    }
}
