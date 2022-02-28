package GroceryShopBillingSystem.frames.panels;

import GroceryShopBillingSystem.DatabaseCon;
import GroceryShopBillingSystem.frames.dialog.AddCategoryDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

import static GroceryShopBillingSystem.Constraint.setPosition;

public class NewProductPanel extends JPanel implements KeyListener, ActionListener, WindowListener {

    public static final int KGS = 1;
    public static final int LTR = 3;
    public static final int PCS = 4;

    private JLabel productCodeLabel,productNameLabel,sellingPriceLabel,costPriceLabel,unitLabel,categoryLabel,inventoryLabel,taxLabel,messageLabel;
    private JTextField productCodeField,productNameField,sellingPriceField,costPriceField,inventoryField,taxField;
    private JComboBox unitCombobox,categoryCombobox;
    private JButton addProductButton,addCategoryButton;
    private DatabaseCon db;

    public NewProductPanel(){
        //Initialising Member Variable
        productCodeLabel = new JLabel("Product Code : ");
        productCodeField = new JTextField(30);
        productNameLabel = new JLabel("Product Name : ");
        productNameField = new JTextField(30);
        sellingPriceLabel = new JLabel("Selling Price/Kgs : ");
        sellingPriceField = new JTextField(30);
        costPriceLabel = new JLabel("Cost Price/Kgs : ");
        costPriceField = new JTextField(30);
        unitLabel = new JLabel("Unit : ");
        unitCombobox = new JComboBox(new String[]{"Kgs","Ltr","Pcs"});
        categoryLabel = new JLabel("Category : ");
        categoryCombobox = new JComboBox();
        fillCategoryComboBox();
        inventoryLabel = new JLabel("Inventory : ");
        inventoryField = new JTextField(30);
        taxLabel = new JLabel("Tax Rate : ");
        taxField = new JTextField(30);
        messageLabel = new JLabel();
        addProductButton = new JButton("Add Product");
        addCategoryButton = new JButton("Add New Category");

        //Forcing input field to only take numbers as input
        productCodeField.addKeyListener(this);
        sellingPriceField.addKeyListener(this);
        costPriceField.addKeyListener(this);
        inventoryField.addKeyListener(this);
        taxField.addKeyListener(this);

        //Size Adjustment
        unitCombobox.setPreferredSize(new Dimension(325,25));
        categoryCombobox.setPreferredSize(new Dimension(325,25));

        //Adding listener to Button
        unitCombobox.addActionListener(this);
        addProductButton.addActionListener(this);
        addCategoryButton.addActionListener(this);

        //Editing panel details
        setBackground(new Color(148,212,66));
        setLayout(new GridBagLayout());

        //Adding members to Panel
        add(productCodeLabel,setPosition(0,0));add(productCodeField,setPosition(1,0));
        add(productNameLabel,setPosition(2,0));add(productNameField,setPosition(3,0));
        add(sellingPriceLabel,setPosition(0,1));add(sellingPriceField,setPosition(1,1));
        add(costPriceLabel,setPosition(2,1));add(costPriceField,setPosition(3,1));
        add(unitLabel,setPosition(0,2));add(unitCombobox,setPosition(1,2));
        add(categoryLabel,setPosition(2,2));add(categoryCombobox,setPosition(3,2));
        add(inventoryLabel,setPosition(0,3));add(inventoryField,setPosition(1,3));
        add(taxLabel,setPosition(2,3));add(taxField,setPosition(3,3));
        add(messageLabel,setPosition(0,4,4,1));
        add(addProductButton,setPosition(0,5,2,1));add(addCategoryButton,setPosition(2,5,2,1));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==unitCombobox){
            String unit = (String)unitCombobox.getSelectedItem();
            sellingPriceLabel.setText("Selling Price/"+unit+" : ");
            costPriceLabel.setText("Cost Price/"+unit+" : ");
        }else if( e.getSource() == addProductButton ){
            if( productCodeField.getText().isEmpty() ){
                messageLabel.setText("Enter Product Code");
                return;
            }

            if(productNameField.getText().isEmpty()){
                messageLabel.setText("Enter Product Name");
                return;
            }

            if( sellingPriceField.getText().isEmpty() ){
                messageLabel.setText("Enter Selling Price");
                return;
            }

            if( costPriceField.getText().isEmpty()){
                messageLabel.setText("Enter Cost Price");
                return;
            }

            if(inventoryField.getText().isEmpty()){
                messageLabel.setText("Enter Inventory");
                return;
            }
            if(taxField.getText().isEmpty()){
                messageLabel.setText("Enter Tax Rate");
                return;
            }

            try{
                db = new DatabaseCon();
                if( db.checkExist("p_id","product", Integer.parseInt(productCodeField.getText()))) {
                    messageLabel.setText("Product Code Already Exist in Database");
                    return;
                }

                int productCode = Integer.parseInt(productCodeField.getText());
                String productName = productNameField.getText();
                float sellingPrice,costPrice;
                try{
                    sellingPrice = Float.parseFloat(sellingPriceField.getText());
                    costPrice = Float.parseFloat(costPriceField.getText());
                }catch(Exception excp){
                    messageLabel.setText("Enter Valid Selling Price/Cost Price");
                    return;
                }
                int unit;
                if( unitCombobox.getSelectedItem().equals("Kgs")){
                    unit = KGS;
                }else if( unitCombobox.getSelectedItem().equals("Ltr")){
                    unit = LTR;
                }else{
                    unit = PCS;
                }
                String category = (String)categoryCombobox.getSelectedItem();
                float inventory,tax;
                try{
                    inventory = Float.parseFloat(inventoryField.getText());
                    tax = Float.parseFloat(taxField.getText());
                }catch(Exception excp){
                    messageLabel.setText("Enter a Valid Value for Inventory/Tax Rate");
                    return;
                }


                db.addProduct(productCode,productName,sellingPrice,costPrice,unit,category,inventory,tax);
                messageLabel.setText("Product Added to Database");
            }catch(Exception excp){
                DatabaseCon.showOptionPane(this,excp);
            }finally{
                db.closeConnection();
            }
        }else if(e.getSource() == addCategoryButton ) {
            try{
                JDialog addCategoryDialog = new AddCategoryDialog((JFrame) SwingUtilities.getWindowAncestor(this));
                addCategoryDialog.addWindowListener(this);
            }catch(Exception excp){
                System.out.println(excp);
            }
        }
    }

    private void fillCategoryComboBox() {
        try{
            db = new DatabaseCon();
            categoryCombobox.removeAllItems();
            Vector<String> categoryList = db.getCategoryList();
            int i = 0;
            while( i < categoryList.size()){
                categoryCombobox.addItem(categoryList.elementAt(i));
                i++;
            }
        }catch( Exception e ){
            DatabaseCon.showOptionPane(this,e);
        }finally {
            db.closeConnection();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if(e.getSource() == productCodeField ){

            if( !Character.isDigit(e.getKeyChar()) ){
                e.consume();
            }
        }else{
            if( !Character.isDigit(e.getKeyChar()) && e.getKeyChar() != '.' ){
                e.consume();
            }
        }
    }

    @Override
    public void windowClosed(WindowEvent e) {
        fillCategoryComboBox();
    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
