package GroceryShopBillingSystem.frames.panels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import static GroceryShopBillingSystem.Constraint.setPosition;

public class SetToPanel extends JPanel implements KeyListener {
    private JLabel s_priceLabel,taxLabel;
    private JTextField s_priceField,taxField;

    public SetToPanel(){
        //Initialising Member Variables
        s_priceLabel = new JLabel("Selling Price : ");
        s_priceField = new JTextField(15);
        taxLabel = new JLabel("Tax Rate : ");
        taxField = new JTextField(15);

        //Adding KeyListener
        s_priceField.addKeyListener(this);
        taxField.addKeyListener(this);

        //Editing Panel Details
        setLayout(new GridBagLayout());

        //Adding Members to Panel
        add(s_priceLabel,setPosition(0,0));add(s_priceField,setPosition(1,0));
        add(taxLabel,setPosition(0,1));add(taxField,setPosition(1,1));
    }

    public String getSellingPrice(){
        return s_priceField.getText();
    }

    public String getTax(){
        return taxField.getText();
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
