package GroceryShopBillingSystem.frames.panels;

import GroceryShopBillingSystem.DatabaseCon;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import static GroceryShopBillingSystem.Constraint.setPosition;

public class SetToPanel extends JPanel implements ActionListener, KeyListener {
    private JLabel s_priceLabel,taxLabel,messageLabel;
    private JTextField s_priceField,taxField;
    private JButton updateButton;
    private String categoryName;
    private DatabaseCon db;

    public SetToPanel(String categoryName){
        this.categoryName = categoryName;
        //Initialising Member Variables
        s_priceLabel = new JLabel("Selling Price : ");
        s_priceField = new JTextField(15);
        taxLabel = new JLabel("Tax Rate : ");
        taxField = new JTextField(15);
        messageLabel = new JLabel();
        updateButton = new JButton("Update Category Info");

        //Adding Listener
        s_priceField.addKeyListener(this);
        taxField.addKeyListener(this);
        updateButton.addActionListener(this);

        //Editing Panel Details
        setLayout(new GridBagLayout());

        //Adding Members to Panel
        add(s_priceLabel,setPosition(0,0));add(s_priceField,setPosition(1,0));
        add(taxLabel,setPosition(0,1));add(taxField,setPosition(1,1));
        add(messageLabel,setPosition(0,2,2,1));
        add(updateButton,setPosition(0,3,2,1));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if( s_priceField.getText().isEmpty() ){
            messageLabel.setText("Enter Selling Price");
            return;
        }

        if( taxField.getText().isEmpty() ){
            messageLabel.setText("Enter Tax Rate");
            return;
        }

        float s_price,tax;
        try{
            s_price = Float.parseFloat(s_priceField.getText());
            tax = Float.parseFloat(taxField.getText());
        }catch(Exception excp){
            messageLabel.setText("Enter a Valid Value for Inventory/Tax Rate");
            return;
        }

        try{
            db = new DatabaseCon();
            db.updateCategorySet(categoryName,s_price,tax);
            messageLabel.setText("Category Info Updated");
        }catch(Exception excp){
            DatabaseCon.showOptionPane(this,excp);
        }finally {
            db.closeConnection();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if( !Character.isDigit(e.getKeyChar()) && e.getKeyChar() != '.' ){
            e.consume();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
