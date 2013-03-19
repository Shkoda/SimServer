package server;


import com.google.protobuf.AbstractMessageLite;
import data.GameKeeper;
import data.GameTable;
import database.DBHandler;
import net.handlers.IncomingMessageHandler;
import net.protobuf.MessagesProtos;
import net.structure.Envelope;
import net.structure.Type;
import org.jboss.netty.channel.Channel;
import data.Player;
import utils.Logger;


/**
 * Created with IntelliJ IDEA.
 * User: Nightingale
 * Date: 1/26/13
 * Time: 3:07 PM
 * To change this template use File | Settings | File Templates.
 */

public class UserWorkerThread {

    private Channel channel;
    private Player player;
    /**
     * global game keeper
     */
    private volatile GameKeeper gameKeeper;
    /**
     * current game table
     */
    private volatile GameTable gameTable;

    public UserWorkerThread(Channel channel, GameKeeper gameKeeper) {
        this.channel = channel;
        this.gameKeeper = gameKeeper;
    }

    public void disconnect() {
        channel.disconnect();
    }

    public void disconnectedFromChannel() {

        if (player != null)  {
            DBHandler.changeOnlineStatus(player.getName(), false);
            gameKeeper.getOnlinePlayers().remove(player.getName());
            if (gameTable != null) {
                UserWorkerThread partnerWorker = gameTable.getPartner(this);

                if (partnerWorker != null){
                    partnerWorker.write(MessagesProtos.BInterruptGame.newBuilder()
                            .setOnlyPause(false)
                            .build());
                    partnerWorker.setGameTable(null);

                } else {
                    gameKeeper.setTableWithOnePlayer(null);
                }

            }
        }
    }

    public void acceptPacket(IncomingMessageHandler message) {
        try {
            Logger.messageReceived(message, player);
            message.handle(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void write(AbstractMessageLite object) {
        Envelope envelope = new Envelope();
        byte[] data = object.toByteArray();
        envelope.setData(data);
        envelope.setLength(data.length);
        envelope.setType(Type.fromClass(object.getClass()));
        channel.write(envelope);
        Logger.messageSend(envelope, player);
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public GameKeeper getGameKeeper() {
        return gameKeeper;
    }

    public void setGameKeeper(GameKeeper gameKeeper) {
        this.gameKeeper = gameKeeper;
    }

    public GameTable getGameTable() {
        return gameTable;
    }

    public void setGameTable(GameTable gameTable) {
        this.gameTable = gameTable;
    }
}
