package tk.icudi.durandal.controller;

import java.io.IOException;

import tk.icudi.durandal.core.logic.BoardController;
import tk.icudi.durandal.core.logic.CommandObserver;
import tk.icudi.durandal.core.logic.Message;
import tk.icudi.durandal.core.logic.ShortMessage;
import tk.icudi.durandal.core.logic.player.PlayerHuman;
import tk.icudi.durandal.logger.Log;

public class PlayerBlueToothLocal extends PlayerHuman implements CommandObserver, BTConnectionHandler<ShortMessage> {

    private static final long serialVersionUID = 1L;

    private String name;
    private boolean iAmTheServer;

    private BTConnection<ShortMessage> connection;

    public PlayerBlueToothLocal(String name) {
        this.name = name;
    }

    @Override
    public void setBoardController(BoardController boardController) {
        super.setBoardController(boardController);
        boardController.addObserver(this);
    }

    @Override
    public void tick() {
    }


    @Override
    public void reset() {

    }

    @Override
    public void onLocalCommand(Message message) {
        connection.write(message.getMsg());
    }

    @Override
    public String getName() {
        return name;
    }


    @Override
    public void onConnectionEstablished(BTConnection<ShortMessage> connection, boolean youAreTheServer) {
        this.connection = connection;

    }

    @Override
    public void onConnectionError(IOException e) {
        Log.d(" --- ", "onConnectionError", e);
    }

    @Override
    public void obtainedMessage(ShortMessage msg) {
        // Don't do anything
    }

    public boolean isiAmTheServer() {
        return iAmTheServer;
    }

    public void setiAmTheServer(boolean iAmTheServer) {
        this.iAmTheServer = iAmTheServer;
    }

}
