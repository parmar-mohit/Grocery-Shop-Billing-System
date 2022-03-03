package GroceryShopBillingSystem.frames;

import GroceryShopBillingSystem.frames.panels.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static GroceryShopBillingSystem.Constraint.setPosition;

public class AdminFrame extends JFrame implements ActionListener {

    private JButton newProductButton,newOperatorButton, updateProductInfoButton,salesHistoryButton,customerListButton,discountCodeButton,deleteDiscountButton,changePasswordButton,logoutButton;
    private JPanel optionPanel;

    public AdminFrame(){
        //Initialising member variables
        newProductButton = new JButton("Add New Product");
        newOperatorButton = new JButton("Add New Operator");
        updateProductInfoButton = new JButton("Update Product Info");
        salesHistoryButton = new JButton("View Sales History");
        customerListButton = new JButton("View Customer List");
        discountCodeButton = new JButton("Create Discount Code");
        deleteDiscountButton = new JButton("Delete Discount Code");
        changePasswordButton = new JButton("Change Password");
        logoutButton = new JButton("Logout");

        //Editing Member Variables
        newProductButton.setPreferredSize(new Dimension(175,25));
        newOperatorButton.setPreferredSize(new Dimension(175,25));
        updateProductInfoButton.setPreferredSize(new Dimension(175,25));
        salesHistoryButton.setPreferredSize(new Dimension(175,25));
        customerListButton.setPreferredSize(new Dimension(175,25));
        discountCodeButton.setPreferredSize(new Dimension(175,25));
        deleteDiscountButton.setPreferredSize(new Dimension(175,25));
        changePasswordButton.setPreferredSize(new Dimension(175,25));
        logoutButton.setPreferredSize(new Dimension(175,25));

        colorButton(null);

        //Adding listener to Buttons
        newProductButton.addActionListener(this);
        newOperatorButton.addActionListener(this);
        updateProductInfoButton.addActionListener(this);
        salesHistoryButton.addActionListener(this);
        customerListButton.addActionListener(this);
        discountCodeButton.addActionListener(this);
        deleteDiscountButton.addActionListener(this);
        changePasswordButton.addActionListener(this);
        logoutButton.addActionListener(this);

        //Frame Details
        setTitle("Grocery Shop Billing System");
        getContentPane().setBackground(new Color(111,138,232));
        setIconImage(Toolkit.getDefaultToolkit().getImage("Images/Icon.png"));
        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());
        setVisible(true);

        //adding member to frame
        add(newProductButton,setPosition(0,0));
        add(newOperatorButton,setPosition(0,1));
        add(updateProductInfoButton,setPosition(0,2));
        add(salesHistoryButton,setPosition(0,3));
        add(customerListButton,setPosition(0,4));
        add(discountCodeButton,setPosition(0,5));
        add(deleteDiscountButton,setPosition(0,6));
        add(changePasswordButton,setPosition(0,7));
        add(logoutButton,setPosition(0,8));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if( optionPanel !=  null ){
            remove(optionPanel);
        }

        if(e.getSource() == newProductButton ){
            optionPanel = new NewProductPanel();
        }if(e.getSource() == newOperatorButton ){
            optionPanel = new NewOperatorPanel();
        }else if( e.getSource() == updateProductInfoButton ){
            optionPanel = new UpdateProductInfoPanel();
        }else if( e.getSource() == salesHistoryButton ){
            //optionPanel = new PurchaseHistoryPanel();
        }else if(e.getSource()==customerListButton){
            optionPanel = new CustomerListPanel();
        } else if(e.getSource() == discountCodeButton ) {
            optionPanel = new DiscountCodePanel();
        }else if( e.getSource() == deleteDiscountButton ){
            optionPanel = new DeleteDiscountPanel();
        } else if( e.getSource() == changePasswordButton ){
            optionPanel = new ChangePasswordPanel("admin");
        }else if( e.getSource() == logoutButton ){
            new BillingSystem();
            dispose();
            return;
        }

        add(optionPanel,setPosition(1,0,1,9));
        optionPanel.setVisible(true);
        colorButton((JButton) e.getSource());
        revalidate();
        repaint();
    }

    private void colorButton(JButton selectedButton) {
        newProductButton.setBackground(new Color(245,181,71));
        newOperatorButton.setBackground(new Color(245,181,71));
        updateProductInfoButton.setBackground(new Color(245,181,71));
        salesHistoryButton.setBackground(new Color(245,181,71));
        customerListButton.setBackground(new Color(245,181,71));
        discountCodeButton.setBackground(new Color(245,181,71));
        deleteDiscountButton.setBackground(new Color(245,181,71));
        changePasswordButton.setBackground(new Color(245,181,71));
        logoutButton.setBackground(new Color(245,181,71));

        if( selectedButton != null ){
            selectedButton.setBackground(new Color(255,255,255));
        }
    }
}
