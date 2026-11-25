package server;

import bank.Bank;
import bank.BankIO;
import server.network.Connection;

/**
 * This class extends the Server class and adds what is needed for the
 * server to be a bank server.
 */
public class BankServer extends Server {
    public static final int INACTIVITY_DELAY = 30000;
    //Reference to the bank managed by this server:
    private Bank bank;
    //Thread that removes inactive connections:
    private InactiveConnectionsThread inactiveConnectionsThread;
    /**
     * Creates a server that will listen on the specified port.
     *
     * @param port int listening port of the server
     */
    public BankServer(int port) {
        super(port);
    }

    /**
     * Returns the bank managed by this server.
     * @return the bank managed by this server
     */
    public Bank getBank() {
        return bank;
    }
    /**
     * Starts the bank server, if it has not already been started. Starts the thread that listens for the arrival
     * of clients, the thread that listens for the arrival of text and the thread that removes inactive connections.
     * Sets up the event handler of the server.
     *
     * @return boolean true if the server was started correctly, false if the server was already started or if
     * there is a connection problem
     */
    @Override
    public boolean start() {
        if (super.start()) {
            this.bank = BankIO.load();
            if (this.bank==null)
                this.bank = new Bank("Bank");
            inactiveConnectionsThread = new InactiveConnectionsThread(this);
            inactiveConnectionsThread.start();
            return true;
        }
        return false;
    }
    /**
     * Stops the server by stopping the threads that listen for the arrival of clients and of text and the thread
     * that removes inactive connections, then saves the bank. Does nothing if the server is not running
     * (so that a server that failed to start never overwrites the saved bank).
     */
    @Override
    public void stop() {
        if (!isStarted())
            return;
        super.stop();
        if (inactiveConnectionsThread!=null)
            inactiveConnectionsThread.interrupt();
        BankIO.save(bank);
    }

    /**
     * Provides the list of the connected client account numbers, separated by the symbol :.
     * @return the list of the connected client account numbers
     */
    public String list() {
        String s = "";
        for (Connection cnx:connected) {
            s += ((BankConnection)cnx).getClientAccountNumber() + ":";
        }
        return s;
    }
    /**
     * Removes all inactive connections (those whose inactivity delay exceeds INACTIVITY_DELAY - see the
     * lab statement).
     */
    public synchronized void removeInactive() {
        //Question 1.2 :
        BankConnection cnx;
        for (int i=this.connected.size()-1;i>=0;i--) {
            cnx = (BankConnection)this.connected.get(i);
            if (cnx.isInactiveFor(INACTIVITY_DELAY)) {
                cnx.send("END");
                cnx.close();
                this.connected.remove(i);
            }
        }
    }
}
