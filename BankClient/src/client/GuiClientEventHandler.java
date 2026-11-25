package client;

import client.network.Event;
import client.network.EventHandler;
import client.network.Connection;
import view.MainPanel;

import javax.swing.*;

public class GuiClientEventHandler implements EventHandler {
    private Client client;
    private MainPanel mainPanel;

    /**
     * Builds an event handler for a client.
     *
     * @param client Client the client for which this handler handles events
     * @param mainPanel MainPanel the main panel of the graphical interface
     */
    public GuiClientEventHandler(Client client, MainPanel mainPanel) {

        this.client = client;
        this.mainPanel = mainPanel;
        this.client.setEventHandler(this);
    }
    @Override
    public void handle(Event event) {
        Object source = event.getSource();
        String eventType, arg, str;
        String[] t;
        MainFrame frame;

        if (source instanceof Connection) {
            eventType = event.getType();
            switch (eventType) {
                /******************* GENERAL COMMANDS *******************/
                case "END": //The server asks to close the connection
                    client.disconnect(); //Close the connection
                    break;
                /******************* CREATION and LOGIN *******************/
                case "OK":
                    mainPanel.setVisible(true);
                    frame = (MainFrame) mainPanel.getTopLevelAncestor();
                    frame.setTitle(MainFrame.TITLE);
                    break;
                case "NEW":
                    arg = event.getArgument();
                    if (arg.trim().startsWith("NO")) {
                        JOptionPane.showMessageDialog(mainPanel,"New account refused.\n\n"
                                + "The account number must have 6 to 8 characters (uppercase letters and digits only).\n"
                                + "The PIN must have 4 to 5 digits.\n"
                                + "The account number may also already be taken.");
                    }
                    else {
                        mainPanel.hideLoginPanel();
                        mainPanel.showClientAccountPanel();
                        str = arg.substring(arg.indexOf("OK")+2).trim(); // "123456[CHECKING] 0.0"
                        mainPanel.addAccount(str.substring(0, str.indexOf("]")+1));
                        mainPanel.setBalance(str.substring(str.indexOf("]")+1).trim());
                    }
                    break;
                case "CONNECT":
                    arg = event.getArgument();
                    if (arg.trim().startsWith("NO")) {
                        JOptionPane.showMessageDialog(mainPanel,"Login refused");
                    }
                    else {
                        mainPanel.hideLoginPanel();
                        mainPanel.showClientAccountPanel();
                        str = arg.substring(arg.indexOf("OK")+2).trim();
                        t = str.split(":");
                        for (String s:t) {
                            mainPanel.addAccount(s.substring(0,s.indexOf("]")+1));
                        }
                        // The first entry is the checking account, which is selected by default:
                        mainPanel.setBalance(t[0].substring(t[0].indexOf("]")+1).trim());
                    }
                    break;
                /******************* ACCOUNT SELECTION *******************/
                case "SAVINGS" :
                    arg = event.getArgument().trim();
                    if (arg.startsWith("OK")) {
                        mainPanel.addAccount(arg.substring(2).trim() + "[SAVINGS]");
                        JOptionPane.showMessageDialog(mainPanel,"Savings account created");
                    } else {
                        JOptionPane.showMessageDialog(mainPanel,"Could not create a savings account (you may already have one).");
                    }
                    break;
                case "SELECT" :
                    arg = event.getArgument().trim();
                    if (arg.startsWith("OK")) {
                        t = arg.substring(2).trim().split(" "); // [accountNumber, balance]
                        if (t.length >= 2)
                            mainPanel.setBalance(t[1]);
                    } else {
                        JOptionPane.showMessageDialog(mainPanel,"Could not select this account.");
                    }
                    break;

                /******************* BANKING OPERATIONS *******************/
                case "DEPOSIT" :
                case "WITHDRAW" :
                case "BILL" :
                case "TRANSFER" :
                    arg = event.getArgument().trim();
                    if (arg.startsWith("OK")) {
                        str = arg.substring(2).trim();
                        mainPanel.setBalance(str);
                        JOptionPane.showMessageDialog(mainPanel, eventType + " successful.\nNew balance : " + str + " $");
                    } else {
                        JOptionPane.showMessageDialog(mainPanel, eventType + " refused (invalid amount, insufficient funds or unknown account).");
                    }
                    break;
                case "HISTORY" :
                    arg = event.getArgument().trim();
                    if (arg.startsWith("NO")) {
                        JOptionPane.showMessageDialog(mainPanel,"No history available.");
                    } else {
                        JTextArea area = new JTextArea(arg.isEmpty() ? "No operations yet." : arg, 15, 40);
                        area.setEditable(false);
                        JOptionPane.showMessageDialog(mainPanel, new JScrollPane(area), "Account history", JOptionPane.PLAIN_MESSAGE);
                    }
                    break;
                /******************* DEFAULT HANDLING *******************/
                default:
                    System.out.println("RECEIVED : "+event.getType()+" "+event.getArgument());
            }
        }
    }
}
