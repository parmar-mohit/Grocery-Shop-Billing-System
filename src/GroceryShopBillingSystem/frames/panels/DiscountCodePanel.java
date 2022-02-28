package GroceryShopBillingSystem.frames.panels;

import GroceryShopBillingSystem.DatabaseCon;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import static GroceryShopBillingSystem.Constraint.setPosition;

public class DiscountCodePanel extends JPanel implements KeyListener,ActionListener {
    public static final int FIXED_VALUE = 1;
    public static final int PERCENT = 2;
    private JLabel discountCodeLabel,typeLabel,valueLabel,minimumPurchaseAmountLabel,messageLabel;
    private JTextField discountCodeField,valueField,minimumPurchaseAmountField;
    private JRadioButton percentType,fixedValueType;
    private JButton createDiscountCodeButton;
    private ButtonGroup typeButtonGroup;
    private DatabaseCon db;

    public DiscountCodePanel(){
        //Initialising Member Variables
        discountCodeLabel = new JLabel("Discount Code : ");
        discountCodeField = new JTextField(20);
        typeLabel = new JLabel("Type : ");
        typeButtonGroup = new ButtonGroup();
        percentType = new JRadioButton("Percent");
        fixedValueType = new JRadioButton("Fixed Value");
        typeButtonGroup.add(percentType);
        typeButtonGroup.add(fixedValueType);
        valueLabel = new JLabel("Discount Value : ");
        valueField = new JTextField(20);
        minimumPurchaseAmountLabel = new JLabel("Minimum Purchase Value : ");
        minimumPurchaseAmountField = new JTextField(20);
        messageLabel = new JLabel("");
        createDiscountCodeButton = new JButton("Create Discount Code");

        //Changing Color
        percentType.setBackground(new Color(148,212,66));
        fixedValueType.setBackground(new Color(148,212,66));

        //Adding Listener to Member Variables
        valueField.addKeyListener(this);
        minimumPurchaseAmountField.addKeyListener(this);
        createDiscountCodeButton.addActionListener(this);

        //Editing panel details
        setBackground(new Color(148,212,66));
        setLayout(new GridBagLayout());

        //Adding Member Variable to Panel
        add(discountCodeLabel,setPosition(0,0));
        add(discountCodeField,setPosition(1,0,2,1));
        add(typeLabel,setPosition(0,1));
        add(percentType,setPosition(1,1));
        add(fixedValueType,setPosition(2,1));
        add(valueLabel,setPosition(0,2));
        add(valueField,setPosition(1,2,2,1));
        add(minimumPurchaseAmountLabel,setPosition(0,3));
        add(minimumPurchaseAmountField,setPosition(1,3,2,1));
        add(messageLabel,setPosition(0,4,3,1));
        add(createDiscountCodeButton,setPosition(0,5,3,1));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String discountCode = discountCodeField.getText();
        if( discountCode.isEmpty() ) {
            messageLabel.setText("Enter Discount Code");
            return;
        }

        int type = 0;
        if( percentType.isSelected() ){
            type = PERCENT;
        }else if( fixedValueType.isSelected()){
            type = FIXED_VALUE;
        }else{
            messageLabel.setText("Select Type");
            return;
        }

        String value = valueField.getText();
        if( value.isEmpty()){
            messageLabel.setText("Enter Value");
            return;
        }

        String mpa = minimumPurchaseAmountField.getText();
        if( mpa.isEmpty()){
            messageLabel.setText("Enter Minimum Purchase Value");
            return;
        }

        try{
            db = new DatabaseCon();

            if( db.checkExist("discount_code","discount",discountCode)){
                messageLabel.setText("Discount Code Already Exist");
                return;
            }

            db.addDiscountCode(discountCode,type,Integer.parseInt(value),Integer.parseInt(mpa));
            messageLabel.setText("Discount Code Created");
        }catch( Exception excp){
            DatabaseCon.showOptionPane(this,excp);
        }finally {
            db.closeConnection();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if( !Character.isDigit(e.getKeyChar()) ){
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