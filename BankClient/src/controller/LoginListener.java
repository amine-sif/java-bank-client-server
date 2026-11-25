package controller;

import client.Client;
import view.LoginPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginListener implements ActionListener {
    private Client client;
    private LoginPanel loginPanel;

    public LoginListener(Client client, LoginPanel loginPanel) {
        this.client = client;
        this.loginPanel = loginPanel;
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        Object source = evt.getSource();
        String actionName;
        if (source instanceof JButton) {
            actionName = ((JButton)source).getActionCommand();
            switch (actionName) {
                case "NEW":
                    if (!loginPanel.isAccountNumberValid()) {
                        JOptionPane.showMessageDialog(loginPanel,
                                "The account number must have 6 to 8 characters (uppercase letters and digits only).");
                        return;
                    }
                    if (!loginPanel.isPinValid()) {
                        JOptionPane.showMessageDialog(loginPanel,
                                "The PIN must have 4 to 5 digits.");
                        return;
                    }
                    client.send("NEW "+loginPanel.getClientAccountNumber()+":"+loginPanel.getPin());
                    break;
                case "CONNECT":
                    client.send("CONNECT "+loginPanel.getClientAccountNumber()+":"+loginPanel.getPin());
                    break;
            }
            loginPanel.clear();
        }
    }
}
