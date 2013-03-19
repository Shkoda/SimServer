package data;

import server.UserWorkerThread;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created with IntelliJ IDEA.
 * User: Nightingale
 * Date: 1/29/13
 * Time: 4:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class GameKeeper {
    //     List<GameTable> activeGameList;
//     List<UserWorkerThread> playerOnlineList;
//     List<UserWorkerThread> activePlayerList;
    private volatile ConcurrentHashMap<String, UserWorkerThread> onlinePlayers;
    private volatile GameTable tableWithOnePlayer;

    public GameKeeper() {

        tableWithOnePlayer = null;
        onlinePlayers = new ConcurrentHashMap<>();

    }


    public GameTable getTableWithOnePlayer() {
        return tableWithOnePlayer;
    }

    public void setTableWithOnePlayer(GameTable tableWithOnePlayer) {
        this.tableWithOnePlayer = tableWithOnePlayer;
    }

    public ConcurrentHashMap<String, UserWorkerThread> getOnlinePlayers() {
        return onlinePlayers;
    }
}
