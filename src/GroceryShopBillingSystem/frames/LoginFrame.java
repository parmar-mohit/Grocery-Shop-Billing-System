package GroceryShopBillingSystem.frames;

import GroceryShopBillingSystem.DatabaseCon;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.ResultSet;

import static GroceryShopBillingSystem.Constraint.setPosition;

public class LoginFrame extends JFrame implements ActionListener,ItemListener {

    private JTextField usernameTextField;
    private JPasswordField passwordField;
    private JButton loginButton,goBackButton;
    private JLabel avatarImage,backgroundImage,messageLabel;
    private JCheckBox showPasswordCheckbox;
    private DatabaseCon db;

    public LoginFrame() {
        //Initialising members
        backgroundImage = new JLabel(new ImageIcon("Images/Background2.png"));
        usernameTextField = new JTextField("username",15);
        passwordField  = new JPasswordField("password",15);
        loginButton = new JButton("Login");
        goBackButton = new JButton("Go Back");
        avatarImage = new JLabel(new ImageIcon("Images/Avatar2.png"));
        messageLabel = new JLabel("");
        showPasswordCheckbox = new JCheckBox("Show Password");

        //Editing Member variables look
        usernameTextField.setHorizontalAlignment(SwingConstants.CENTER);
        passwordField.setHorizontalAlignment(SwingConstants.CENTER);
        passwordField.setEchoChar('*');
        showPasswordCheckbox.setBackground(new Color(111,138,232));
        loginButton.setBackground(new Color(245,181,71));
        goBackButton.setBackground(new Color(245,181,71));
        loginButton.setPreferredSize(new Dimension(100,25));
        goBackButton.setPreferredSize(new Dimension(100,25));

        //Adding Listener to Buttons
        showPasswordCheckbox.addItemListener(this);
        loginButton.addActionListener(this);
        goBackButton.addActionListener(this);

        //Frame Details
        setTitle("Grocery Shop Billing System");
        getContentPane().setBackground(new Color(111,138,232));
        setIconImage(Toolkit.getDefaultToolkit().getImage("Images/Icon.png"));
        setLayout(new GridBagLayout());
        setSize(960,540);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        //Adding Member to Frame
        add(backgroundImage,setPosition(0,0,1,7));
        add(avatarImage,setPosition(1,0));
        add(usernameTextField,setPosition(1,1));
        add(passwordField,setPosition(1,2));
        add(showPasswordCheckbox,setPosition(1,3));
        add(messageLabel,setPosition(1,4));
        add(loginButton,setPosition(1,5));
        add(goBackButton,setPosition(1,6));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if( e.getSource() == loginButton ){
            String username = usernameTextField.getText();
            if( username.isEmpty() ){
                messageLabel.setText("Enter Username");
                return;
            }

            String password = new String(passwordField.getPassword());
            if( password.isEmpty() ){
                messageLabel.setText("Enter Password");
                return;
            }

            try{
                db = new DatabaseCon();
                if( !db.checkExist("username","user",username)){
                    messageLabel.setText("User does not Exist");
                    return;
                }

                ResultSet result = db.executeQuery("SELECT * FROM user WHERE username=\""+username+"\";");
                result.next();
                if(!password.equals(result.getString("password"))){
                    messageLabel.setText("Invalid Credentials");
                    return;
                }else{
                    messageLabel.setText("Login Successful");
                    new BillingFrame(username);
                    dispose();
                }
            }catch(Exception excp){
                DatabaseCon.showOptionPane(this,excp);
            }finally{
                db.closeConnection();
            }
        }if( e.getSource() == goBackButton ) {
            new BillingSystem();
            dispose();
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if( showPasswordCheckbox.isSelected() ) {
            passwordField.setEchoChar((char)0);
        }else {
            passwordField.setEchoChar('*');
        }
    }
}
