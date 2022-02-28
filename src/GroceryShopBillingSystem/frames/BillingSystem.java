package GroceryShopBillingSystem.frames;

import GroceryShopBillingSystem.Constraint;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static GroceryShopBillingSystem.Constraint.setPosition;

public class BillingSystem extends JFrame implements ActionListener {

    private JButton operatorLoginButton,adminLoginButton;
    private JLabel label;

    public BillingSystem(){
        //Initialising Member Variables
        label = new JLabel("Grocery Shop Billing System");
        operatorLoginButton = new JButton("Operator Login");
        adminLoginButton = new JButton("Admin Login");

        //Editing Member Variables
        label.setFont(new Font(Font.SANS_SERIF,Font.BOLD,30));
        operatorLoginButton.setBackground(new Color(245,181,71));
        adminLoginButton.setBackground(new Color(245,181,71));

        //Adding Listener to Button
        operatorLoginButton.addActionListener(this);
        adminLoginButton.addActionListener(this);

        //Frame Details
        setTitle("Grocery Shop Billing System");
        setIconImage(Toolkit.getDefaultToolkit().getImage("Images/Icon.png"));
        setImageBackground();
        setSize(960,540);
        setLayout(new GridBagLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setResizable(false);

        //Adding Buttons to Frame
        add(label,setPosition(0,0,2,1, Constraint.LEFT));
        add(operatorLoginButton,setPosition(0,1));
        add(adminLoginButton,setPosition(1,1));
    }

    private void setImageBackground(){
        getContentPane().setBackground(new Color(111,138,232));
        setContentPane(new JPanel(){
            final Image img = Toolkit.getDefaultToolkit().getImage("Images/Background1.png");

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(img,0,0,null);
            }
        });
        pack();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if( e.getSource() == operatorLoginButton ){
            new LoginFrame();
            dispose();
        }else if( e.getSource() == adminLoginButton ){
            new AdminLogin();
            dispose();
        }
    }
}
