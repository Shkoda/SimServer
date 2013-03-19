package database;

import config.DBConnectionConfig;
import data.Player;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBHandler {
    private static Connection connection;
    private static String url = DBConnectionConfig.getDbURL();
    private static String dbUser = DBConnectionConfig.getDbUser();
    private static String dbPassword = DBConnectionConfig.getDbPassword();


    private static PreparedStatement insertAuthorisationStatement;
    private static PreparedStatement selectAuthorisationStatement;
    private static PreparedStatement changeScoreStatement;
    private static PreparedStatement getScoreStatement;
    private static PreparedStatement selectTopPlayerStatement;
    private static PreparedStatement selectOnlinePlayersStatement;
    private static PreparedStatement updateOnlineStatusStatement;

    static {
        connectToDB();
        prepareStatements();
    }



    /**
     * connect to local db
     */
    private static void connectToDB() {
        try {
            connection = DriverManager.getConnection(url, dbUser, dbPassword);
        } catch (SQLException e) {
            Logger lgr = Logger.getLogger(DBHandler.class.getName());
            lgr.log(Level.SEVERE, e.getMessage(), e);
        }

    }

    /**
     * create statements for most popular requests
     */
    private static void prepareStatements() {

        try {
          //  String stm = "INSERT INTO players(id, playername, password, score, isonline) VALUES (?, ?, ?, ?, ?)";
            String stm = "INSERT INTO players(playername, password) VALUES (?, ?)";
            insertAuthorisationStatement = connection.prepareStatement(stm);

            stm = "SELECT * FROM players WHERE playername = ?";
            selectAuthorisationStatement = connection.prepareStatement(stm);

            stm = "UPDATE players SET score = ? WHERE playername = ?";
            changeScoreStatement = connection.prepareStatement(stm);

            stm = "SELECT score FROM players WHERE playername = ?";
            getScoreStatement = connection.prepareStatement(stm);

            stm = "SELECT playername, score FROM players ORDER BY score DESC LIMIT ?";
            selectTopPlayerStatement = connection.prepareStatement(stm);

            stm = "SELECT * FROM players WHERE isonline = ?";
            selectOnlinePlayersStatement = connection.prepareStatement(stm);

            stm = "UPDATE players SET isonline = ? WHERE playername = ?";
            updateOnlineStatusStatement = connection.prepareStatement(stm);


        } catch (SQLException e) {
            Logger lgr = Logger.getLogger(DBHandler.class.getName());
            lgr.log(Level.SEVERE, e.getMessage(), e);
        }

    }

    /**
     * @return 0, if player exist but password doesn't match
     *         1, if player exist and password is valid.
     *         2, if new player was created.
     *         3, if player is already online
     */


    public static int connectPlayer(String name, String password) {
        Player player = DBRequestExecutor.findPlayerByName(name, selectAuthorisationStatement);
        if (player != null)
            if (player.getPassword().equals(password)) {
                boolean wasOnline =  player.isOnline();
                if (wasOnline){
                    return 3;
                }
                DBRequestExecutor.setOnlineStatus(name, true, updateOnlineStatusStatement);
                return 1;
            } else
                return 0;

        DBRequestExecutor.createNewPlayer(name, password, insertAuthorisationStatement, selectAuthorisationStatement);
        return 2;
    }

    public static void changeOnlineStatus(String name, boolean online){
        DBRequestExecutor.setOnlineStatus(name, online, updateOnlineStatusStatement);
    }

    public static Player getPlayerInfo(String name){
        return DBRequestExecutor.findPlayerByName(name, selectAuthorisationStatement);
    }

    public static int getScore(String name){
          return DBRequestExecutor.getScore(name, getScoreStatement);
    }

    public static void setScore(String name, int score){
        DBRequestExecutor.setScore(name, score, changeScoreStatement);
    }

    /**
     * temp realisation
     * @param listLength
     */

    public static void topPlayersList(int listLength){
        ResultSet resultSet = DBRequestExecutor.getTopList(listLength, selectTopPlayerStatement) ;
        try {
            while (resultSet.next())
                System.out.println(resultSet.getString(1)+" "+resultSet.getInt(2));
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }




}
