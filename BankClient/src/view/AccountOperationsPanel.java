package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class AccountOperationsPanel extends JPanel {
    private JButton bSavings, bDeposit, bWithdraw, bTransfer, bBill, bHistory;
    private JLabel lblBalance;

    public AccountOperationsPanel() {
        bSavings = new JButton("Create savings account");
        bDeposit = new JButton("Deposit");
        bWithdraw = new JButton("Withdraw");
        bTransfer = new JButton("Transfer");
        bBill = new JButton("Pay bill");
        bHistory = new JButton("Account history");
        lblBalance = new JLabel("Balance : ");

        bSavings.setActionCommand("SAVINGS");
        bDeposit.setActionCommand("DEPOSIT");
        bWithdraw.setActionCommand("WITHDRAW");
        bTransfer.setActionCommand("TRANSFER");
        bBill.setActionCommand("BILL");
        bHistory.setActionCommand("HISTORY");

        //to be completed:
        this.setLayout(new FlowLayout(FlowLayout.RIGHT));
        this.add(lblBalance);
        this.add(bSavings);
        this.add(bDeposit);
        this.add(bWithdraw);
        this.add(bTransfer);
        this.add(bBill);
        this.add(bHistory);
    }
    public void setBalance(String balance) {
        lblBalance.setText("Balance : " + balance + " $");
    }
    public void setListener(ActionListener listener) {
        bSavings.addActionListener(listener);
        bDeposit.addActionListener(listener);
        bWithdraw.addActionListener(listener);
        bTransfer.addActionListener(listener);
        bBill.addActionListener(listener);
        bHistory.addActionListener(listener);
    }
}
