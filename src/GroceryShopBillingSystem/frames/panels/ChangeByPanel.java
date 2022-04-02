package GroceryShopBillingSystem.frames.panels;

import GroceryShopBillingSystem.DatabaseCon;

import javax.swing.*;
import javax.xml.crypto.Data;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import static GroceryShopBillingSystem.Constraint.setPosition;

public class ChangeByPanel extends JPanel implements ActionListener,KeyListener {
    private JLabel changeTypeLabel,changeByLabel,s_priceLabel,taxLabel,messageLabel;
    private JTextField s_priceField,taxField;
    private JRadioButton incrementRadioButton,decrementRadioButton,fixedRadioButton,percentRadioButton;
    private ButtonGroup typeButtonGroup,changeByButtonGroup;
    private JButton updateButton;
    private String categoryName;
    private DatabaseCon db;

    public ChangeByPanel(String categoryName){
        this.categoryName = categoryName;
        //Initialising Member
        changeTypeLabel = new JLabel("Change Type");
        incrementRadioButton = new JRadioButton("Increment");
        decrementRadioButton = new JRadioButton("Decrement");
        typeButtonGroup = new ButtonGroup();
        typeButtonGroup.add(incrementRadioButton);
        typeButtonGroup.add(decrementRadioButton);
        changeByLabel = new JLabel("Change By : ");
        fixedRadioButton = new JRadioButton("Fixed Value");
        percentRadioButton = new JRadioButton("Percent");
        changeByButtonGroup = new ButtonGroup();
        changeByButtonGroup.add(fixedRadioButton);
        changeByButtonGroup.add(percentRadioButton);
        s_priceLabel = new JLabel("Selling Price : ");
        s_priceField = new JTextField(15);
        taxLabel = new JLabel("Tax Rate : ");
        taxField = new JTextField(15);
        messageLabel = new JLabel();
        updateButton = new JButton("Update Category Info");

        //Adding ActionListener
        s_priceField.addKeyListener(this);
        taxField.addKeyListener(this);
        updateButton.addActionListener(this);

        //Editing Panel Details
        setLayout(new GridBagLayout());

        //Adding Member to Panel
        add(changeTypeLabel,setPosition(0,0));add(incrementRadioButton,setPosition(1,0));add(decrementRadioButton,setPosition(2,0));
        add(changeByLabel,setPosition(0,1));add(fixedRadioButton,setPosition(1,1));add(percentRadioButton,setPosition(2,1));
        add(s_priceLabel,setPosition(0,2));add(s_priceField,setPosition(1,2));
        add(taxLabel,setPosition(0,3));add(taxField,setPosition(1,3));
        add(messageLabel,setPosition(0,4,3,1));
        add(updateButton,setPosition(0,5,3,1));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if( !(incrementRadioButton.isSelected() || decrementRadioButton.isSelected() )){
            messageLabel.setText("Select Change Type");
            return;
        }

        if( !(fixedRadioButton.isSelected() || percentRadioButton.isSelected()) ){
            messageLabel.setText("Select Change By");
            return;
        }

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
            if( incrementRadioButton.isSelected()  ){
                if( fixedRadioButton.isSelected() ) {
                    db.updateCategoryFixedIncrement(categoryName, s_price, tax);
                }else if( percentRadioButton.isSelected() ){
                    db.updateCategoryPercentIncrement(categoryName,s_price,tax);
                }
            }else if( decrementRadioButton.isSelected() ){
                if( fixedRadioButton.isSelected() ){
                    db.updateCategoryFixedDecrement(categoryName,s_price,tax);
                }else if( percentRadioButton.isSelected() ){
                    db.updateCategoryPercentDecrement(categoryName,s_price,tax);
                }
            }
            messageLabel.setText("Category Info Updated");
        }catch (Exception excp){
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
