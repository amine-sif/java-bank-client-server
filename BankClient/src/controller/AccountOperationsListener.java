package controller;

import client.Client;
import view.AccountOperationsPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AccountOperationsListener implements ActionListener {
    private Client client;
    private AccountOperationsPanel panel;

    public AccountOperationsListener(Client client, AccountOperationsPanel accountOperationsPanel) {
        this.client = client;
        this.panel = accountOperationsPanel;
    }

    /** Asks the user for a value. Returns null if cancelled or left empty. */
    private String ask(String message) {
        String s = JOptionPane.showInputDialog(panel, message);
        if (s == null) return null;
        s = s.trim();
        return s.isEmpty() ? null : s;
    }

    /** Returns true if the text is a positive number. */
    private boolean isValidAmount(String s) {
        try {
            return Double.parseDouble(s) > 0;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    private String askAmount(String message) {
        String amount = ask(message);
        if (amount == null) return null;
        if (!isValidAmount(amount)) {
            JOptionPane.showMessageDialog(panel, "Please enter a valid amount greater than 0.");
            return null;
        }
        return amount;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        String actionName, amount, account, billNumber, description;
        if (source instanceof JButton) {
            actionName = ((JButton)source).getActionCommand();
            switch (actionName) {
                case "SAVINGS":
                    client.send("SAVINGS");
                    break;
                case "DEPOSIT":
                    amount = askAmount("Amount to deposit :");
                    if (amount != null)
                        client.send("DEPOSIT " + amount);
                    break;
                case "WITHDRAW":
                    amount = askAmount("Amount to withdraw :");
                    if (amount != null)
                        client.send("WITHDRAW " + amount);
                    break;
                case "TRANSFER":
                    amount = askAmount("Amount to transfer :");
                    if (amount == null) break;
                    account = ask("Destination bank account number :");
                    if (account != null)
                        client.send("TRANSFER " + amount + " " + account);
                    break;
                case "BILL":
                    amount = askAmount("Bill amount :");
                    if (amount == null) break;
                    billNumber = ask("Bill number :");
                    if (billNumber == null) break;
                    description = ask("Description :");
                    if (description != null)
                        client.send("BILL " + amount + " " + billNumber + " " + description);
                    break;
                case "HISTORY":
                    client.send("HISTORY");
                    break;
            }
        }
    }
}
