package GroceryShopBillingSystem.frames.dialog;

import GroceryShopBillingSystem.Constraint;
import GroceryShopBillingSystem.DatabaseCon;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.ResultSet;

import static GroceryShopBillingSystem.Constraint.setPosition;

public class ChangePasswordDialog extends JDialog implements ItemListener, ActionListener {

    private JLabel oldPasswordLabel,newPasswordLabel,confirmPasswordLabel,messageLabel;
    private JPasswordField oldPasswordField,newPasswordField,confirmPasswordField;
    private JCheckBox showPasswordCheckbox;
    private JButton changePasswordButton;
    private DatabaseCon db;
    private String username;
    private JFrame parent;

    public ChangePasswordDialog(JFrame parent, String uname) {
        super(parent);
        this.parent = parent;
        username = uname;
        //Initialising Member Variables
        oldPasswordLabel = new JLabel("Enter Old Password : ");
        oldPasswordField = new JPasswordField(20);
        newPasswordLabel = new JLabel("Enter New Password : ");
        newPasswordField = new JPasswordField(20);
        confirmPasswordLabel = new JLabel("Confirm Password : ");
        confirmPasswordField = new JPasswordField(20);
        showPasswordCheckbox = new JCheckBox("Show Password");
        messageLabel = new JLabel("");
        changePasswordButton = new JButton("Change Password");

        //Editing member variables attributes
        oldPasswordField.setEchoChar('*');
        newPasswordField.setEchoChar('*');
        confirmPasswordField.setEchoChar('*');
        showPasswordCheckbox.setBackground(new Color(148,212,66));

        //Adding Listener to member variables
        showPasswordCheckbox.addItemListener(this);
        changePasswordButton.addActionListener(this);

        //Editing panel details
        setTitle("Change Password");
        setIconImage(Toolkit.getDefaultToolkit().getImage("Images/Icon.png"));
        getContentPane().setBackground(new Color(148,212,66));
        setLayout(new GridBagLayout());
        setSize(600,300);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setVisible(true);

        //adding member to panel
        add(oldPasswordLabel,setPosition(0,0, Constraint.RIGHT));
        add(oldPasswordField,setPosition(1,0,Constraint.LEFT));
        add(newPasswordLabel,setPosition(0,1,Constraint.RIGHT));
        add(newPasswordField,setPosition(1,1,Constraint.LEFT));
        add(confirmPasswordLabel,setPosition(0,2,Constraint.RIGHT));
        add(confirmPasswordField,setPosition(1,2,Constraint.LEFT));
        add(showPasswordCheckbox,setPosition(0,3,Constraint.LEFT));
        add(messageLabel,setPosition(0,4,2,1));
        add(changePasswordButton,setPosition(0,5,2,1));

        revalidate();
        repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String old = new String(oldPasswordField.getPassword());
        String newp = new String(newPasswordField.getPassword());
        String confirm = new String(confirmPasswordField.getPassword());

        if (!newp.equals(confirm)) {
            messageLabel.setText("Passwords Do Not Match");
            return;
        }

        if( newp.isEmpty() ){
            messageLabel.setText("Enter Password");
            return;
        }

        try {
            db = new DatabaseCon();
            ResultSet result = db.executeQuery("SELECT password from user WHERE username=\""+username+"\";");
            result.next();
            if (!old.equals(result.getString("password"))) {
                messageLabel.setText("Incorrect Password");
            } else {
                db.change_password(username, newp);
                messageLabel.setText("Password Changed");
            }
        } catch (Exception excp) {
            DatabaseCon.showOptionPane(parent, excp);
        } finally {
            db.closeConnection();
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if( showPasswordCheckbox.isSelected() ) {
            oldPasswordField.setEchoChar((char)0);
            newPasswordField.setEchoChar((char)0);
            confirmPasswordField.setEchoChar((char)0);
        }else{
            oldPasswordField.setEchoChar('*');
            newPasswordField.setEchoChar('*');
            confirmPasswordField.setEchoChar('*');
        }
    }
}
