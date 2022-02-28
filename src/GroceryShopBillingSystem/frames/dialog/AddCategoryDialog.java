package GroceryShopBillingSystem.frames.dialog;

import GroceryShopBillingSystem.DatabaseCon;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import static GroceryShopBillingSystem.Constraint.setPosition;

public class AddCategoryDialog extends JDialog implements ActionListener {
    private JLabel categoryNameLabel,messageLabel;
    private JTextField categoryNameField;
    private JButton addCategoryButton;
    private DatabaseCon db;
    private JFrame parent;

    public AddCategoryDialog(JFrame parent) {
        super(parent);
        this.parent = parent;
        //Initialising Member Variables
        categoryNameLabel = new JLabel("Category Name");
        categoryNameField = new JTextField(15);
        messageLabel = new JLabel();
        addCategoryButton = new JButton("Add Category");

        //Adding Listener
        addCategoryButton.addActionListener(this);

        //Editing dialog details
        setTitle("Add New Category");
        setIconImage(Toolkit.getDefaultToolkit().getImage("Images/Icon.png"));
        getContentPane().setBackground(new Color(148,212,66));
        setLayout(new GridBagLayout());
        setSize(600,300);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setVisible(true);

        //Adding Member Variables to Dialog
        add(categoryNameLabel,setPosition(0,0));add(categoryNameField,setPosition(1,0));
        add(messageLabel,setPosition(0,1,2,1));
        add(addCategoryButton,setPosition(0,2,2,1));
        revalidate();
        repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String category = categoryNameField.getText();
        if( category.isEmpty()) {
            messageLabel.setText("Enter Category Name");
            return;
        }

        try{
            db = new DatabaseCon();
            Vector<String> categoryList = db.getCategoryList();
            int i = 0;
            while( i < categoryList.size() ){
                if( category.equals(categoryList.elementAt(i))){
                    messageLabel.setText("Category Already Exist");
                    return;
                }
                i++;
            }

            db.addCategory(categoryList.size()+1,category);
            messageLabel.setText("Category Added");
        }catch(Exception excp){
            DatabaseCon.showOptionPane(parent,excp);
        }
    }
}
