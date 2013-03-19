package utils;


import config.ServerConfig;
import data.GameTable;
import net.handlers.IncomingMessageHandler;
import net.structure.Envelope;
import data.Player;


public class Logger {


    private static boolean logEnabled = !ServerConfig.RELEASE_MODE;
    private static boolean sending = true;
    private static boolean receive = true;
    private static boolean sql = false;


    public static void sql(String message) {
        if (logEnabled && sql) writeLog(message);
    }


    public static void messageSend(Envelope envelope, Player player) {
        if (logEnabled && sending) {
            if (player != null) {
                writeLog("<- " + envelope.getType() + " to " + player.getName());
            } else {
                writeLog("<- " + envelope.getType());
            }

        }
    }

    public static void messageSend(Envelope envelope, int id) {
        if (logEnabled && sending) {
            writeLog("<- " + envelope.getType() + " to " + id);

        }
    }

    public static void messageReceived(IncomingMessageHandler message, Player player) {
        if (logEnabled && receive) {
            String name = message.getClass().getSimpleName();
            if (player != null) {
                writeLog("--> " + name.substring(0, name.length() - 7) + " from " + player.getName());
            } else {
                writeLog("--> " + name.substring(0, name.length() - 7));
            }

        }
    }


    public static void error(String message, Throwable e, Player player) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(DateUtils.now());
        buffer.append(" ERROR: ");
        buffer.append(message);

        if (player != null) {
            buffer.append(" [common.entity.AuthorisationInfo ID: ");
            buffer.append(player.getId());
            buffer.append("]");
        }

        writeLog(buffer.toString());
//		writeLog(e.getStackTrace().toString());
        e.printStackTrace();
    }

    public static void info(Player player) {

        StringBuffer buffer = new StringBuffer();
        buffer.append(DateUtils.now());
        buffer.append(" INFO: package ");
        buffer.append(" sended!");

        if (player != null) {
            buffer.append(" [common.entity.AuthorisationInfo ID: ");
            buffer.append(player.getId());
            buffer.append("]");
        }

        buffer.append("\n");
        buffer.append("		");

        writeLog(buffer.toString());
    }

    public static void info(IncomingMessageHandler object, Player player) {

        StringBuffer buffer = new StringBuffer();
        buffer.append(DateUtils.now());
        buffer.append(" INFO: package ");
        buffer.append(object.getClass().getCanonicalName());
        buffer.append(" recived!");

        if (player != null) {
            buffer.append(" [common.entity.AuthorisationInfo ID: ");
            buffer.append(player.getId());
            buffer.append("]");
        }

        buffer.append("\n");
        buffer.append("		");
        buffer.append(object.toString());

        writeLog(buffer.toString());

    }

    public static void info(String message, Player player) {

        StringBuilder buffer = new StringBuilder();
        buffer.append(DateUtils.now());
        buffer.append(" INFO: ");
        buffer.append(message);

        if (player != null) {
            buffer.append(" [common.entity.AuthorisationInfo ID: ");
            buffer.append(player.getId());
            buffer.append("]");
        }

        writeLog(buffer.toString());

    }

    public static void connectionInfo(int dbAnswer, String playerName){

        StringBuilder buffer = new StringBuilder();
        buffer.append(DateUtils.now());
        buffer.append(" AUTHORISATION: [playerNmae = ");
        buffer.append(playerName);
        buffer.append("] ");


        switch (dbAnswer){
            case 0:
                buffer.append("password doesn't match");
                break;
            case 1:
                buffer.append("authorisation OK") ;
                break;
            case 2:
                buffer.append("create new player");
                break;
        }

        writeLog(buffer.toString());
    }

    public static void newGameStartInfo(String playerName1, String playerName2, GameTable table){
        StringBuilder builder = new StringBuilder();
        builder.append(DateUtils.now());
        builder.append("    NEW GAME START: [table = ");
        builder.append(table);
        builder.append("; player1 = ");
        builder.append(playerName1);
        builder.append(", player2 = ");
        builder.append(playerName2);
        builder.append("]");
        writeLog(builder.toString());
    }

    private static void writeLog(String log) {
         if (logEnabled)
            System.out.println(log);
    }


}

