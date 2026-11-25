package client;

import client.network.EventHandler;
import controller.MainMenuListener;
import observer.Observable;
import observer.Observer;
import view.MainPanel;


import javax.swing.*;
import java.awt.*;

/**
 *
 * This class represents the main window of the client application.
 */
public class MainFrame extends JFrame implements Runnable, Observer {

    public static final String TITLE = "Bank App - Client";
    private Client client;
    private MainPanel mainPanel;
    private JMenu mStart;
    private JMenuItem miConnect, miDisconnect, miQuit, miConfigure;
    private MainMenuListener mainMenuListener;
    private EventHandler eventHandler;

    public void run() {
        initializeComponents();
        configureMainFrame();
        setVisible(true);
    }

    private void configureMainFrame() {
        //Window configuration
        this.setSize(1000,600);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    }

    private void initializeComponents() {
        //Creation and initialization of the components:
        client = new Client();
        client.addObserver(this);
        mainPanel = new MainPanel(client);
        mainMenuListener = new MainMenuListener(client, this);
        eventHandler = new GuiClientEventHandler(client, mainPanel);

        //Menus:
        mStart = new JMenu("Start");
        miConnect = new JMenuItem("Connect");
        miDisconnect = new JMenuItem("Disconnect");
        miQuit = new JMenuItem("Quit");
        miConfigure = new JMenuItem("Configure server");

        miConnect.setActionCommand("CONNECT");
        miDisconnect.setActionCommand("DISCONNECT");
        miQuit.setActionCommand("QUIT");
        miConfigure.setActionCommand("CONFIGURE");

        miConnect.addActionListener(mainMenuListener);
        miDisconnect.addActionListener(mainMenuListener);
        miQuit.addActionListener(mainMenuListener);
        miConfigure.addActionListener(mainMenuListener);

        mStart.add(miConnect);
        mStart.add(miDisconnect);
        mStart.addSeparator();
        mStart.add(miConfigure);
        mStart.addSeparator();
        mStart.add(miQuit);

        miDisconnect.setEnabled(false);

        JMenuBar mb = new JMenuBar();
        mb.add(mStart);

        this.setContentPane(mainPanel);
        mainPanel.setVisible(false);
        this.setJMenuBar(mb);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater( new MainFrame() );
    }

    @Override
    public void update(Observable observable) {
        if (observable instanceof Client) {
            Client client = (Client)observable;
            if (client.isConnected()) {
                miDisconnect.setEnabled(true);
                miConnect.setEnabled(false);
            } else {
                this.setTitle(TITLE);
                this.mainPanel.setVisible(false);
                mainPanel.hideClientAccountPanel();
                mainPanel.showLoginPanel();
                miDisconnect.setEnabled(false);
                miConnect.setEnabled(true);
            }
        }
    }
}
