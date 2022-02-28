package GroceryShopBillingSystem.frames.panels;

import GroceryShopBillingSystem.Constraint;
import GroceryShopBillingSystem.DatabaseCon;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import static GroceryShopBillingSystem.Constraint.setPosition;

public class NewOperatorPanel extends JPanel implements ItemListener,ActionListener {

    private JLabel employeeIdLabel,usernameLabel,passwordLabel,messageLabel;
    private JTextField employeeIdField,usernameField;
    private JPasswordField passwordField;
    private JCheckBox showPassword;
    private JButton addOperatorButton;
    private DatabaseCon db;

    public NewOperatorPanel(){
        //Initialising Member Variables
        employeeIdLabel = new JLabel("Employee ID");
        employeeIdField = new JTextField(20);
        usernameLabel = new JLabel("username");
        usernameField = new JTextField(20);
        passwordLabel= new JLabel("password");
        passwordField = new JPasswordField(20);
        showPassword = new JCheckBox("Show Password");
        messageLabel = new JLabel();
        addOperatorButton = new JButton("Add Operator");

        //Editing Member variables
        passwordField.setEchoChar('*');
        showPassword.setBackground(new Color(148,212,66));

        //Adding Listener ti o Buttons
        showPassword.addItemListener(this);
        addOperatorButton.addActionListener(this);

        //Editing panel details
        setBackground(new Color(148,212,66));
        setLayout(new GridBagLayout());

        //adding member to Panal
        add(employeeIdLabel,setPosition(0,0));
        add(employeeIdField,setPosition(1,0));
        add(usernameLabel,setPosition(0,1));
        add(usernameField,setPosition(1,1));
        add(passwordLabel,setPosition(0,2));
        add(passwordField,setPosition(1,2));
        add(showPassword,setPosition(0,3, Constraint.LEFT));
        add(messageLabel,setPosition(0,4,2,1));
        add(addOperatorButton,setPosition(0,5,2,1));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String employeeId = employeeIdField.getText();
        if( employeeId.isEmpty() )
        {
            messageLabel.setText("Enter Employee ID");
            return;
        }

        String username = usernameField.getText();
        if( username.isEmpty() ){
            messageLabel.setText("Enter Username");
            return;
        }

        String password = new String(passwordField.getPassword());
        if( password.isEmpty() ){
            messageLabel.setText("Enter Password");
            return;
        }

        if( !Constraint.isValidPassword(new String(passwordField.getPassword()) )){
            messageLabel.setText("Password must contain 1 Uppercase letter,1 Lowercase letter and 1 Number");
            return;
        }

        try{
            db = new DatabaseCon();

            if( db.checkExist("emp_id","user",employeeId)) {
                messageLabel.setText("Employee Id Already has an operator user");
                return;
            }

            if( db.checkExist("username","user",username)){
                messageLabel.setText("Username not Available,Select Different username");
                return;
            }

            db.addOperator(username,password,employeeId);
            messageLabel.setText("Operator ID Created");
        }catch(Exception excp){
            DatabaseCon.showOptionPane(this,excp);
        }finally{
            db.closeConnection();
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if(showPassword.isSelected()){
            passwordField.setEchoChar((char)0);
        }else{
            passwordField.setEchoChar('*');
        }
    }
}
