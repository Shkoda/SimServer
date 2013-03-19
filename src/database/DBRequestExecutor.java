package database;



import data.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Nightingale
 * Date: 1/25/13
 * Time: 3:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class DBRequestExecutor {


    /**
     * SELECT * FROM players WHERE playername = ?
     *
     * @param name
     * @param selectAuthorisationStatement
     * @return
     */
    protected static Player findPlayerByName(String name, PreparedStatement selectAuthorisationStatement) {
        ResultSet resultSet = null;
        boolean exist = false;
        try {
            selectAuthorisationStatement.setString(1, name);
            resultSet = selectAuthorisationStatement.executeQuery();
            exist = resultSet.next();

        } catch (SQLException e) {
            Logger lgr = Logger.getLogger(DBHandler.class.getName());
            lgr.log(Level.SEVERE, e.getMessage(), e);
        }

        return (!exist) ? null : loadPlayerFromDB(resultSet);
    }

    protected static Player loadPlayerFromDB(ResultSet resultSet) {
        Player player = new Player();
        try {
            player.setName(resultSet.getString(1));
            player.setPassword(resultSet.getString(2));
            player.setScore(resultSet.getInt(3));
            player.setOnline(resultSet.getBoolean(4));
            player.setId(resultSet.getInt(5));
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return player;
    }

    /**
     * INSERT INTO players(id, playername, password, score, isonline) VALUES (?, ?, ?, ?, ?)
     *
     * @param name
     * @param password
     * @param newPlayerStatement
     * @return
     */
    protected static Player createNewPlayer(String name, String password, PreparedStatement newPlayerStatement, PreparedStatement selectAuthorisationStatement) {
        ResultSet resultSet = null;
        try {

            newPlayerStatement.setString(1, name);
            newPlayerStatement.setString(2, password);
            newPlayerStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return findPlayerByName(name, selectAuthorisationStatement);
    }

    /**
     * UPDATE players SET isonline = ? WHERE playername = ?
     *
     * @param name
     * @param onlineStatus
     * @param onlineStatement
     * @return
     */
    protected static void setOnlineStatus(String name, boolean onlineStatus, PreparedStatement onlineStatement) {
        try {
            onlineStatement.setBoolean(1, onlineStatus);
            onlineStatement.setString(2, name);
            onlineStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    /**
     * SELECT score FROM players WHERE playername = ?
     * @param name
     * @param statement
     * @return
     */

    protected static Integer getScore(String name, PreparedStatement statement) {
        ResultSet resultSet = null;
        int score = 0;
        try {
            statement.setString(1, name);
            resultSet = statement.executeQuery();
            resultSet.next();
            score = resultSet.getInt(1);

        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return score;
    }

    /**
     * "UPDATE players SET score = ? WHERE playername = ?
     * @param name
     * @param score
     * @param statement
     */

    protected static void setScore(String name, int score, PreparedStatement statement){
        try {
            statement.setInt(1, score);
            statement.setString(2, name);
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    /**
     * SELECT playername, score FROM players ORDER BY score DESC LIMIT ?
     * @param topListLength
     * @param statement
     * @return
     */
    protected static ResultSet getTopList(int topListLength, PreparedStatement statement){
        ResultSet resultSet = null;
        try {
            statement.setInt(1, topListLength);
            resultSet = statement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
         return resultSet;
    }
}
