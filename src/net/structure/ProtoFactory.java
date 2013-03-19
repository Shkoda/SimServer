package net.structure;

import com.google.protobuf.InvalidProtocolBufferException;
import net.handlers.IncomingMessageHandler;
import net.handlers.connection.AuthorisationHandler;
import net.handlers.connection.ContinueGameHandler;
import net.handlers.connection.InterruptHandler;
import net.handlers.connection.LogoutHandler;
import net.handlers.game.NewGameRequestHandler;
import net.handlers.game.PartnerInfoRequestHandler;
import net.handlers.game.PlayerTurnHandler;

/**
 * Created with IntelliJ IDEA.
 * User: Nightingale
 * Date: 1/26/13
 * Time: 3:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProtoFactory {

    public static IncomingMessageHandler createSpecificHandler(byte[] data, Type type) {
        try {

            switch (type) {

                case CLOGIN:
                    return new AuthorisationHandler(data);

                case CNEWGAME:
                    return new NewGameRequestHandler(data);

                case CNEWTURN:
                    return new PlayerTurnHandler(data);

                case CINTERRUPT:
                    return new InterruptHandler(data);

                case CCONTINUE:
                    return new ContinueGameHandler(data);

                case CPARTNERINFO:
                    return new PartnerInfoRequestHandler(data);

                case CLOGOUT:
                    return new LogoutHandler(data);


                default:
                    System.out.println("Unable to create instance in factory, type: " + type);
                    return null;
            }
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
            return null;
        }
    }

}
