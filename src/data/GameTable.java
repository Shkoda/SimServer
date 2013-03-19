package data;


import config.GameConfig;
import server.UserWorkerThread;

/**
 * Created with IntelliJ IDEA.
 * User: Nightingale
 * Date: 1/25/13
 * Time: 12:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class GameTable {

    private int[][] transitionMatrix;

    private UserWorkerThread firstPlayerWorker;
    private UserWorkerThread secondPlayerWorker;
    private int turnCounter;
    private int maxTurnNumber;
    private int vertexNumber;


    public GameTable(UserWorkerThread firstPlayerWorker) {
        vertexNumber = GameConfig.GRAPH_VERTEX_NUMBER;
        transitionMatrix = new int[vertexNumber][vertexNumber];
        this.firstPlayerWorker = firstPlayerWorker;
        turnCounter = 0;
        maxTurnNumber = GameConfig.MAX_TURN_NUMBER;

    }

    /**
     * if player creates a triangle he lose.
     *
     * @param playerId
     * @param vertexFrom
     * @param vertexTo
     * @return transitionResult[0] = loser id or -1, if nobody lose, or -2, if draw;
     * transitionResult[1] = connecting vertex (if exists)
     */
    public int[] setTransitionAndCheckLose(int playerId, int vertexFrom, int vertexTo) {

        int[] transitionResult = {-1, -1};

        for (int i = 0; i < vertexNumber; i++)
            if (transitionMatrix[vertexFrom][i] == playerId && transitionMatrix[i][vertexTo] == playerId)  {
                transitionResult[0] = playerId;
                transitionResult[1] = i;
            }

        transitionMatrix[vertexFrom][vertexTo] = playerId;
        transitionMatrix[vertexTo][vertexFrom] = playerId;

        turnCounter++;
        if (turnCounter == maxTurnNumber)
            transitionResult[0] = -2;

        return transitionResult;

    }

    public boolean hasSuchPlayer(UserWorkerThread worker){
        return (firstPlayerWorker.equals(worker) || secondPlayerWorker.equals(worker));
    }


    public void setSecondPlayer(UserWorkerThread workerThread) {
        secondPlayerWorker = workerThread;
    }

    public UserWorkerThread getSecondPlayerWorker() {
        return secondPlayerWorker;
    }

    public void setSecondPlayerWorker(UserWorkerThread secondPlayerWorker) {
        this.secondPlayerWorker = secondPlayerWorker;
    }

    public UserWorkerThread getFirstPlayerWorker() {
        return firstPlayerWorker;
    }

    public void setFirstPlayerWorker(UserWorkerThread firstPlayerWorker) {
        this.firstPlayerWorker = firstPlayerWorker;
    }

    public UserWorkerThread getPartner(UserWorkerThread worker) {
        return (worker.equals(firstPlayerWorker)) ? secondPlayerWorker : firstPlayerWorker;
    }
}
