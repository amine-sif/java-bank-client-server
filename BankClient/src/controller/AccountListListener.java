package controller;

import client.Client;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Selects the active bank account when the user clicks on it in the list.
 * List entries look like "123456[CHECKING]" or "123457[SAVINGS]".
 */
public class AccountListListener extends MouseAdapter {

    private Client client;
    public AccountListListener(Client client) {
        this.client = client;
    }

    @Override
    public void mouseClicked(MouseEvent evt) {
        Object source = evt.getSource();
        if (!(source instanceof JList)) return;
        Object selected = ((JList<?>) source).getSelectedValue();
        if (selected == null) return;
        String text = selected.toString();
        if (text.contains("[SAVINGS]"))
            client.send("SELECT savings");
        else if (text.contains("[CHECKING]"))
            client.send("SELECT checking");
    }
}
