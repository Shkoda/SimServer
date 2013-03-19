package net.handlers;


import server.UserWorkerThread;

public abstract class IncomingMessageHandler {

    private int id = 1;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public abstract void handle(UserWorkerThread userWorkerThread);


    @Override
    public String toString() {
        return this.getClass().getCanonicalName() +
                "{" +
                "Unexpected Server Object" +
                '}';
    }

}