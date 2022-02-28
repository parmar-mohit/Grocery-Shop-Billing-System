package GroceryShopBillingSystem.frames;

import GroceryShopBillingSystem.Constraint;
import GroceryShopBillingSystem.DatabaseCon;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;

import static GroceryShopBillingSystem.Constraint.setPosition;

public class AdminLogin extends JFrame implements ActionListener {

    private JLabel avatarImage,adminLoginLabel,passwordLabel,messageLabel;
    private JPasswordField passwordField;
    private JButton loginButton,gobackButton;
    private DatabaseCon db;

    public AdminLogin() {
        //Initialising Member Variables
        avatarImage = new JLabel(new ImageIcon("Images/Avatar1.png"));
        adminLoginLabel = new JLabel("Admin Login");
        passwordLabel = new JLabel("Enter Password");
        passwordField = new JPasswordField(20);
        messageLabel = new JLabel("");
        loginButton = new JButton("Login");
        gobackButton = new JButton("Go Back");

        //Editing Member Variable look
        adminLoginLabel.setFont(new Font("SansSerif",Font.BOLD,24));
        passwordLabel.setFont(new Font("Serif",Font.PLAIN,20));
        passwordLabel.setFont(new Font("Serif",Font.PLAIN,18));
        passwordField.setEchoChar('*');
        messageLabel.setFont(new Font("Serif",Font.PLAIN,16));
        loginButton.setBackground(new Color(245,181,71));
        gobackButton.setBackground(new Color(245,181,71));
        loginButton.setPreferredSize(new Dimension(100,25));
        gobackButton.setPreferredSize(new Dimension(100,25));

        //Adding Action Listener to Buttons
        loginButton.addActionListener(this);
        gobackButton.addActionListener(this);

        //Frame Details
        setTitle("Grocery Shop Billing System");
        getContentPane().setBackground(new Color(111,138,232));
        setIconImage(Toolkit.getDefaultToolkit().getImage("Images/Icon.png"));
        setSize(960,540);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());
        setVisible(true);

        //Adding Member variables to Frame
        add(avatarImage,setPosition(0,0,1,6, Constraint.LEFT));
        add(adminLoginLabel,setPosition(1,0));
        add(passwordLabel,setPosition(1,1,Constraint.LEFT));
        add(passwordField,setPosition(1,2,Constraint.LEFT));
        add(messageLabel,setPosition(1,3,Constraint.LEFT));
        add(loginButton,setPosition(1,4));
        add(gobackButton,setPosition(1,5));

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if( e.getSource() == loginButton ){
            String password="";
            try {
                db = new DatabaseCon();
                ResultSet result = db.executeQuery("SELECT password FROM user WHERE username=\"admin\";");
                result.next();
                password = result.getString("password");
            }catch(Exception excp) {
                DatabaseCon.showOptionPane(this,excp);
            }
            finally {
                db.closeConnection();
            }
            if( password.equals(new String(passwordField.getPassword())) ) {
                new AdminFrame();
                dispose();
            }else{
                messageLabel.setText("Invalid Credentials");
            }
        }else if( e.getSource() == gobackButton ) {
            new BillingSystem();
            dispose();
        }
    }
}

