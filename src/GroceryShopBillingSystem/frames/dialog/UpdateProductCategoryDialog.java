package GroceryShopBillingSystem.frames.dialog;

import GroceryShopBillingSystem.DatabaseCon;
import GroceryShopBillingSystem.frames.panels.SetToPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;

import static GroceryShopBillingSystem.Constraint.setPosition;

public class UpdateProductCategoryDialog extends JDialog implements ActionListener {
    private JLabel selectCategoryLabel,updateTypeLabel,messageLabel;
    private JComboBox categoryCombobox;
    private JRadioButton setRadioButton, changeByRadioButton;
    private ButtonGroup typeButtonGroup;
    private JPanel optionPanel;
    private DatabaseCon db;
    private JFrame parent;
    private JButton updateButton;

    public UpdateProductCategoryDialog(JFrame parent){
        super(parent);
        this.parent = parent;
        //Initialising Member Variables
        selectCategoryLabel = new JLabel("Select Category");
        categoryCombobox = new JComboBox();
        updateTypeLabel = new JLabel("Update Type");
        setRadioButton = new JRadioButton("Set");
        changeByRadioButton = new JRadioButton("Change By");
        typeButtonGroup = new ButtonGroup();
        typeButtonGroup.add(setRadioButton);
        typeButtonGroup.add(changeByRadioButton);
        messageLabel = new JLabel();
        updateButton = new JButton("Update Category Info");

        //Filling Combobox
        fillCombobox();

        //Editing RadioButton
        setRadioButton.setBackground(new Color(148,212,66));
        changeByRadioButton.setBackground(new Color(148,212,66));
        setRadioButton.addActionListener(this);
        changeByRadioButton.addActionListener(this);

        //Editing panel details
        setTitle("Update Product Category");
        setIconImage(Toolkit.getDefaultToolkit().getImage("Images/Icon.png"));
        getContentPane().setBackground(new Color(148,212,66));
        setLayout(new GridBagLayout());
        setSize(new Dimension(700,500));
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setVisible(true);

        //Adding Member to Dialog
        add(selectCategoryLabel,setPosition(0,0));add(categoryCombobox,setPosition(1,0,2,1));
        add(updateTypeLabel,setPosition(0,1));add(setRadioButton,setPosition(1,1));add(changeByRadioButton,setPosition(2,1));
        add(messageLabel,setPosition(0,3,3,1));
        add(updateButton,setPosition(0,4,3,1));
        revalidate();
        repaint();
    }

    private void fillCombobox(){
        try{
            db = new DatabaseCon();
            ResultSet result = db.executeQuery("SELECT category_name FROM product_category;");
            while( result.next() ){
                categoryCombobox.addItem(result.getString("category_name"));
            }
        }catch(Exception excp){
            DatabaseCon.showOptionPane(parent,excp);
        }finally {
            db.closeConnection();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if( optionPanel !=  null ){
            remove(optionPanel);
        }

        if( e.getSource() == setRadioButton ){
            optionPanel = new SetToPanel();
        }else if( e.getSource() == changeByRadioButton)

        add(optionPanel,setPosition(0,2,3,1));
        optionPanel.setVisible(true);
        revalidate();
        repaint();
    }


}
