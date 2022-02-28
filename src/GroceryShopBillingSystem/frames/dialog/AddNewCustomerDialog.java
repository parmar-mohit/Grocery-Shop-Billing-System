package GroceryShopBillingSystem.frames.dialog;

import GroceryShopBillingSystem.DatabaseCon;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;

import static GroceryShopBillingSystem.Constraint.setPosition;

public class AddNewCustomerDialog extends JDialog implements ActionListener {

    private JLabel nameLabel,emailLabel,phoneLabel,messageLabel;
    private JTextField nameField,emailField,phoneField;
    private JButton addCustomerButton;
    private DatabaseCon db;
    private JFrame parent;

    public AddNewCustomerDialog(JFrame parent){
        super(parent);
        this.parent = parent;
        //Initialising Member Varibles
        nameLabel = new JLabel("Customer Name* : ");
        nameField = new JTextField(20);
        emailLabel = new JLabel("Customer Email : ");
        emailField = new JTextField(20);
        phoneLabel = new JLabel("Customer Phone No : ");
        phoneField = new JTextField(20);
        addCustomerButton = new JButton("Add New Customer");
        messageLabel = new JLabel();

        //Adding ActionListener
        addCustomerButton.addActionListener(this);

        //Editing panel details
        setTitle("Add New Customer");
        setIconImage(Toolkit.getDefaultToolkit().getImage("Images/Icon.png"));
        getContentPane().setBackground(new Color(148,212,66));
        setLayout(new GridBagLayout());
        setSize(600,300);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setVisible(true);

        //Adding Members to Dialog
        add(nameLabel,setPosition(0,0));add(nameField,setPosition(1,0));
        add(emailLabel,setPosition(0,1));add(emailField,setPosition(1,1));
        add(phoneLabel,setPosition(0,2));add(phoneField,setPosition(1,2));
        add(messageLabel,setPosition(0,3,2,1));
        add(addCustomerButton,setPosition(0,4,2,1));
        revalidate();
        repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String name = nameField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();

        if( name.isEmpty() ){
            messageLabel.setText("Enter Customer Name");
            return;
        }

        try{
            db = new DatabaseCon();

            if( !email.isEmpty() ) {
                if (db.checkExist("email", "customer", email)) {
                    messageLabel.setText("A Customer with given email id Already Exist");
                    System.out.println("Email Checked");
                    return;
                }
            }

            if( !phone.isEmpty() ) {
                if (db.checkExist("phone", "customer", phone) ) {
                    messageLabel.setText("A Customer with given Phone No Already Exist");
                    return;
                }
            }

            ResultSet result = db.executeQuery("SELECT COUNT(c_id) FROM customer;");
            result.next();
            int c_id = result.getInt(1) + 1;
            db.addCustomer(c_id,name,email,phone);
            messageLabel.setText("New Customer Added");
        }catch(Exception excp){
            DatabaseCon.showOptionPane(parent,excp);
        }finally{
            db.closeConnection();
        }
    }
}
