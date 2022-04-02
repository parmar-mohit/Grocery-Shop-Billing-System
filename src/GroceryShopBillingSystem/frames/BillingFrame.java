package GroceryShopBillingSystem.frames;

import GroceryShopBillingSystem.CreatePdf;
import GroceryShopBillingSystem.DatabaseCon;
import GroceryShopBillingSystem.frames.dialog.AddNewCustomerDialog;
import GroceryShopBillingSystem.frames.dialog.ChangePasswordDialog;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

import static GroceryShopBillingSystem.Constraint.*;

public class BillingFrame extends JFrame implements ActionListener, KeyListener {

    private JLabel operatorLabel,dateLabel,subTotalLabel,taxLabel,totalAmountLabel,discountStatusLabel,customerStatusLabel,quantityLabel,discountLabel,messageLabel;
    private JButton changePasswordButton,logoutButton,addItemButton,addCustomerIdButton,addNewCustomerButton,applyDiscountButton,removeItemButton,clearBillButton,printButton;
    private JTextField quantityField, customerDetailField,discountField;
    private DefaultTableModel tableModel;
    private JTable table;
    private JScrollPane scrollPane;
    private JComboBox productIdCombobox,productNameCombobox,unitCombobox,customerDetailCombobox;
    private int c_id;
    private String username,discountCode;
    private DatabaseCon db;
    private float subTotal,tax;


    public BillingFrame(String username){
        //Initialising Member Variables
        c_id = 0;
        subTotal = 0;
        tax = 0;
        discountCode = null;
        this.username = username;
        operatorLabel = new JLabel("Operator : "+username);
        dateLabel = new JLabel("Date : "+new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
        changePasswordButton = new JButton("Change Password");
        logoutButton = new JButton("Logout");
        String columns[] = {"Sr No","Particulars","Product Id","Quantity","Rate","Value","Tax (%) "};
        tableModel = new DefaultTableModel(columns,0);
        table = new JTable(tableModel);
        scrollPane = new JScrollPane(table);
        subTotalLabel = new JLabel("Sub Total : 0");
        taxLabel = new JLabel("Tax : 0");
        totalAmountLabel = new JLabel("Total Amount : 0");
        discountStatusLabel = new JLabel("Discount Status : N/A");
        customerStatusLabel = new JLabel("Customer Id : N/A");
        productIdCombobox = new JComboBox();
        productNameCombobox = new JComboBox();
        unitCombobox = new JComboBox();
        try{
            db = new DatabaseCon();
            ResultSet result = db.executeQuery("SELECT p_id,p_name FROM product");
            while(result.next()){
                productIdCombobox.addItem(result.getInt("p_id")+"");
                productNameCombobox.addItem(result.getString("p_name"));
            }
        }catch(Exception e){
            DatabaseCon.showOptionPane(this,e);
        }finally {
            db.closeConnection();
        }
        fillUnitCombobox();
        quantityLabel = new JLabel("Quantity : ");
        quantityField = new JTextField(20);
        addItemButton = new JButton("Add Item");
        customerDetailField = new JTextField(20);
        customerDetailCombobox = new JComboBox(new String[]{"Email","Phone"});
        addCustomerIdButton =  new JButton("Add Customer Id");
        addNewCustomerButton = new JButton("Add New Customer");
        discountLabel = new JLabel("Discount Code : ");
        discountField = new JTextField(20);
        applyDiscountButton = new JButton("Apply Discount");
        removeItemButton = new JButton("Remove Item");
        clearBillButton = new JButton("Clear Bill");
        printButton = new JButton("Print Bill");
        messageLabel = new JLabel();

        //Adding color to Button
        changePasswordButton.setBackground(new Color(245,181,71));
        logoutButton.setBackground(new Color(245,181,71));
        addItemButton.setBackground(new Color(245,181,71));
        addCustomerIdButton.setBackground(new Color(245,181,71));
        addNewCustomerButton.setBackground(new Color(245,181,71));
        applyDiscountButton.setBackground(new Color(245,181,71));
        removeItemButton.setBackground(new Color(255,60,60));
        clearBillButton.setBackground(new Color(255,60,60));
        printButton.setBackground(new Color(00,255,00));

        //Editing Dimension
        operatorLabel.setPreferredSize(new Dimension(300,25));
        dateLabel.setPreferredSize(new Dimension(300,25));
        changePasswordButton.setPreferredSize(new Dimension(300,25));
        logoutButton.setPreferredSize(new Dimension(300,25));
        scrollPane.setPreferredSize(new Dimension(1300,250));
        subTotalLabel.setPreferredSize(new Dimension(300,25));
        taxLabel.setPreferredSize(new Dimension(300,25));
        totalAmountLabel.setPreferredSize(new Dimension(600,25));
        discountStatusLabel.setPreferredSize(new Dimension(600,25));
        customerStatusLabel.setPreferredSize(new Dimension(600,25));
        productIdCombobox.setPreferredSize(new Dimension(300,25));
        productNameCombobox.setPreferredSize(new Dimension(600,25));
        quantityField.setPreferredSize(new Dimension(300,25));
        unitCombobox.setPreferredSize(new Dimension(300,25));
        addItemButton.setPreferredSize(new Dimension(300,50));
        customerDetailField.setPreferredSize(new Dimension(300,25));
        customerDetailCombobox.setPreferredSize(new Dimension(300,25));
        addCustomerIdButton.setPreferredSize(new Dimension(300,25));
        addNewCustomerButton.setPreferredSize(new Dimension(300,25));
        discountField.setPreferredSize(new Dimension(300,25));
        applyDiscountButton.setPreferredSize(new Dimension(600,25));
        removeItemButton.setPreferredSize(new Dimension(300,25));
        clearBillButton.setPreferredSize(new Dimension(300,25));
        printButton.setPreferredSize(new Dimension(600,25));

        //Editing Font
        operatorLabel.setFont(new Font(Font.SERIF,Font.PLAIN,18));
        dateLabel.setFont(new Font(Font.SERIF,Font.PLAIN,18));
        subTotalLabel.setFont(new Font(Font.SERIF,Font.PLAIN,20));
        taxLabel.setFont(new Font(Font.SERIF,Font.PLAIN,20));
        totalAmountLabel.setFont(new Font(Font.SERIF,Font.BOLD,20));
        discountStatusLabel.setFont(new Font(Font.SERIF,Font.PLAIN,18));
        customerStatusLabel.setFont(new Font(Font.SERIF,Font.PLAIN,16));
        quantityLabel.setFont(new Font(Font.SERIF,Font.PLAIN,18));
        discountLabel.setFont(new Font(Font.SERIF,Font.PLAIN,18));
        messageLabel.setFont(new Font(Font.SERIF,Font.BOLD,18));
        messageLabel.setForeground(new Color(175,00,00));

        //Editing Border
        subTotalLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        taxLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        totalAmountLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        messageLabel.setBorder(BorderFactory.createRaisedBevelBorder());

        //Editing Table
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        table.getColumnModel().getColumn(1).setPreferredWidth(650);
        table.getColumnModel().getColumn(2).setPreferredWidth(120);
        table.getColumnModel().getColumn(3).setPreferredWidth(120);
        table.getColumnModel().getColumn(4).setPreferredWidth(120);
        table.getColumnModel().getColumn(5).setPreferredWidth(120);
        table.getColumnModel().getColumn(6).setPreferredWidth(120);

        //Adding ActionListener
        changePasswordButton.addActionListener(this);
        logoutButton.addActionListener(this);
        productIdCombobox.addActionListener(this);
        productNameCombobox.addActionListener(this);
        addNewCustomerButton.addActionListener(this);
        addItemButton.addActionListener(this);
        removeItemButton.addActionListener(this);
        clearBillButton.addActionListener(this);
        applyDiscountButton.addActionListener(this);
        addCustomerIdButton.addActionListener(this);
        printButton.addActionListener(this);

        //AddingKeyListener
        quantityField.addKeyListener(this);

        //Frame Details
        setTitle("Grocery Shop Billing System");
        getContentPane().setBackground(new Color(111,138,232));
        setIconImage(Toolkit.getDefaultToolkit().getImage("Images/Icon.png"));
        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());
        setVisible(true);

        //Adding Member to Frame
        add(operatorLabel,setPosition(0,0));add(dateLabel,setPosition(1,0));
        add(changePasswordButton,setPosition(2,0));add(logoutButton,setPosition(3,0));
        add(scrollPane,setPosition(0,1,4,1));
        add(messageLabel,setPosition(0,2,4,1));
        add(subTotalLabel,setPosition(0,3));add(taxLabel,setPosition(1,3));add(totalAmountLabel,setPosition(2,3,2,1));
        add(discountStatusLabel,setPosition(0,4,2,1));add(customerStatusLabel,setPosition(2,4,2,1));
        add(productIdCombobox,setPosition(0,5));add(productNameCombobox,setPosition(1,5,2,1));
        add(quantityLabel,setPosition(0,6,RIGHT));add(quantityField,setPosition(1,6,LEFT));add(unitCombobox,setPosition(2,6));
        add(addItemButton,setPosition(3,5,1,2));
        add(customerDetailField,setPosition(0,7));add(customerDetailCombobox,setPosition(1,7));
        add(addCustomerIdButton,setPosition(2,7));add(addNewCustomerButton,setPosition(3,7));
        add(discountLabel,setPosition(0,8,RIGHT));add(discountField,setPosition(1,8,LEFT));
        add(applyDiscountButton,setPosition(0,9,2,1));
        add(removeItemButton,setPosition(2,8));add(clearBillButton,setPosition(3,8));add(printButton,setPosition(2,9,2,1));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        messageLabel.setText("");

        if( e.getSource() == changePasswordButton ){
            new ChangePasswordDialog(this,username);
        }else if( e.getSource() == productIdCombobox ){
            productNameCombobox.setSelectedIndex(productIdCombobox.getSelectedIndex());
            fillUnitCombobox();
        }else if( e.getSource() == productNameCombobox ){
            productIdCombobox.setSelectedIndex(productNameCombobox.getSelectedIndex());
            fillUnitCombobox();
        } else if( e.getSource() == logoutButton ){
            new BillingSystem();
            dispose();
        }else if( e.getSource() == addNewCustomerButton ){
            JDialog dialog = new AddNewCustomerDialog(this);
        }else if( e.getSource() == addItemButton ){
            String quantityString = quantityField.getText();
            if( quantityString.isEmpty()){
                messageLabel.setText("Enter Quantity");
                return;
            }
            float quantity = Float.parseFloat(quantityString);

            int productId = Integer.parseInt(productIdCombobox.getSelectedItem()+"");

            String unit = unitCombobox.getSelectedItem()+"";
            if( unit.equals("Gms") || unit.equals("Milli Ltr") ){
                quantity /= 1000;
            }

            int row = checkExist(productId);
            if( row == -1 ) {

                try {
                    db = new DatabaseCon();
                    ResultSet result = db.getProductInfo(productId);
                    result.next();
                    tableModel.addRow(new Object[]{tableModel.getRowCount() + 1, result.getString("p_name"), result.getInt("p_id"),quantityString + " " + unit, String.format("%.2f", result.getFloat("s_price")), String.format("%.2f", quantity * result.getFloat("s_price")), result.getFloat("tax_rate")});
                    quantityField.setText("");
                    refreshLabel();
                } catch (Exception excp) {
                    DatabaseCon.showOptionPane(this, excp);
                } finally {
                    db.closeConnection();
                }
            }else{
                String quantityStr = tableModel.getValueAt(row,3)+"";
                String quantityArray[] = quantityStr.split(" ",100);
                Float oldQuantity = Float.parseFloat(quantityArray[0]);
                Float newQuantity = Float.parseFloat(quantityField.getText());
                float rate = Float.parseFloat(tableModel.getValueAt(row,4)+"");

                String quantityUnitString;
                float value=0;
                if( quantityArray[1].equals(unitCombobox.getSelectedItem()+"") ){
                    quantityUnitString = (oldQuantity+newQuantity) + " " + quantityArray[1];
                    if( unitCombobox.getSelectedItem().equals("Kgs") || unitCombobox.getSelectedItem().equals("Ltr") ){
                        value = (oldQuantity+newQuantity)*rate;
                    }else if( unitCombobox.getSelectedItem().equals("Gms") || unitCombobox.getSelectedItem().equals("Milli Ltr") ){
                        value = (oldQuantity+newQuantity)/1000*rate;
                    }else{
                        value = (oldQuantity+newQuantity)*rate;
                    }
                }else{
                    if( quantityArray[1].equals("Kgs") || quantityArray[1].equals("Ltr") ){
                        if( unitCombobox.getSelectedItem().equals("Kgs") || unitCombobox.equals("Ltr") ){
                            quantityUnitString = (oldQuantity+newQuantity) + " " + quantityArray[1];
                            value = (oldQuantity+newQuantity)*rate;
                        }else{
                            quantityUnitString = (oldQuantity+(newQuantity/1000)) + " " + quantityArray[1];
                            value = (oldQuantity+(newQuantity/1000))*rate;
                        }
                    }else{
                        if( unitCombobox.getSelectedItem().equals("Gms") || unitCombobox.getSelectedItem().equals("Milli Ltr") ){
                            quantityUnitString = (oldQuantity+newQuantity) + " " + quantityArray[1];
                            value = (oldQuantity+newQuantity)/1000*rate;
                        }else{
                            quantityUnitString = ((oldQuantity/1000)+newQuantity) + " " + quantityArray[1];
                            value = ((oldQuantity/1000)+newQuantity)*rate;
                        }
                    }
                }
                tableModel.setValueAt(quantityUnitString,row,3);
                tableModel.setValueAt(value,row,5);
                quantityField.setText("");
                refreshLabel();
            }
        }else if( e.getSource() == removeItemButton ){
            int row = table.getSelectedRow();
            if( row == -1 ){
                messageLabel.setText("Select an Item to Remove");
                return;
            }
            tableModel.removeRow(row);

            while( row < tableModel.getRowCount() ){
                tableModel.setValueAt(row+1,row,0);
                row++;
            }
            refreshLabel();
        }else if(e.getSource() == clearBillButton ){
            while( tableModel.getRowCount() > 0 ){
                tableModel.removeRow(0);
            }
            discountCode = null;
            c_id = 0;
            refreshLabel();
        }else if( e.getSource() == applyDiscountButton ){
            String discountCode = discountField.getText();

            if( discountCode.isEmpty() ){
                messageLabel.setText("Enter Discount Code");
                return;
            }

            try{
                db = new DatabaseCon();

                if( !db.checkExist("discount_code","discount",discountCode) ){
                    messageLabel.setText("Entered Discount Code Does not Exist");
                    return;
                }

                ResultSet result = db.executeQuery("SELECT min_pur_amt FROM discount WHERE discount_code=\""+discountCode+"\";");
                result.next();

                if( result.getInt("min_pur_amt") > subTotal+tax ){
                    messageLabel.setText("Minimum "+result.getInt("min_pur_amt")+"Rs Order Value is Required to Apply Entered Discount Code");
                    return;
                }

                this.discountCode = discountCode;
                refreshLabel();
            }catch(Exception excp){
                DatabaseCon.showOptionPane(this,excp);
            }finally{
                db.closeConnection();
            }
        }else if( e.getSource() == addCustomerIdButton ){
            String customerDetail = customerDetailField.getText();
            if( customerDetail.isEmpty() ){
                messageLabel.setText("Enter Customer Detail");
                return;
            }

            String customerType = customerDetailCombobox.getSelectedItem()+"";

            try{
                db = new DatabaseCon();
                int id = db.getCustomerId(customerDetail,customerType);
                if( id == -1 ){
                    messageLabel.setText("No Customer with Given Details Found");
                    return;
                }else{
                    c_id = id;
                    refreshLabel();
                }
            }catch(Exception excp){
                DatabaseCon.showOptionPane(this,excp);
            }finally {
                db.closeConnection();
            }

        }else if(e.getSource()==printButton){
            if( tableModel.getRowCount() == 0 ){
                messageLabel.setText("Add Items to Bill");
                return;
            }

            for( int i = 0; i < tableModel.getRowCount(); i++ ){
                int productCode = Integer.parseInt(tableModel.getValueAt(i,2)+"");
                String quantityString = tableModel.getValueAt(i,3)+"";
                String quantityArray[] = quantityString.split(" ",5);
                float inventory = Float.parseFloat(quantityArray[0]);
                if( quantityArray[1].equals("Gms") || quantityArray[1].equals("Milli Ltr") ){
                    inventory /= 1000;
                }

                try{
                    db = new DatabaseCon();
                    ResultSet result = db.executeQuery("SELECT inventory FROM product WHERE p_id="+productCode+";");
                    result.next();
                    if( result.getFloat(1) < inventory ){
                        messageLabel.setText("Specified Inventory Does not Exist for Product with Product Id : "+productCode);
                        return;
                    }

                    int b_id = db.createBill(tableModel,discountCode,c_id,username);
                    CreatePdf.CreateBill(b_id);
                    while( tableModel.getRowCount() > 0 ){
                        tableModel.removeRow(0);
                    }
                    discountCode = null;
                    c_id = 0;
                    refreshLabel();
                    messageLabel.setText("Bill Created");
                }catch(Exception excp){
                    DatabaseCon.showOptionPane(this,excp);
                }finally {
                    db.closeConnection();
                }
            }
        }
    }

    private void fillUnitCombobox(){
        int p_id = Integer.parseInt(productIdCombobox.getSelectedItem()+"");
        unitCombobox.removeAllItems();
        try{
            db = new DatabaseCon();
            ResultSet result = db.executeQuery("SELECT unit FROM product WHERE p_id="+p_id+";");
            result.next();
            if( result.getInt("unit") == 1 ){
                unitCombobox.addItem("Gms");
                unitCombobox.addItem("Kgs");
            }else if( result.getInt("unit")== 3 ){
                unitCombobox.addItem("Milli Ltr");
                unitCombobox.addItem("Ltr");
            }else{
                unitCombobox.addItem("Pcs");
            }
        }catch(Exception e){
            DatabaseCon.showOptionPane(this,e);
        }finally {
            db.closeConnection();
        }
    }

    private int checkExist(int productCode){
        for( int i = 0; i < tableModel.getRowCount(); i++){
            int a = Integer.parseInt(tableModel.getValueAt(i,2)+"");
            if( a == productCode ){
                return i;
            }
        }

        return -1;
    }

    private void refreshLabel(){
        int row = tableModel.getRowCount();
        subTotal = 0;
        tax = 0;
        for( int i = 0; i < row; i++){
            subTotal += Float.parseFloat(tableModel.getValueAt(i,5)+"");
            tax += Float.parseFloat(tableModel.getValueAt(i,5)+"")/100*Float.parseFloat(tableModel.getValueAt(i,6)+"");
        }

        subTotalLabel.setText(String.format("Sub Total : %.02f Rs",subTotal));
        taxLabel.setText(String.format("Tax : %.02f Rs",tax));
        float total = subTotal +tax;
        if( discountCode != null ){
            try{
                db = new DatabaseCon();
                ResultSet result = db.executeQuery("SELECT * FROM discount WHERE discount_code=\""+discountCode+"\";");
                result.next();
                if( result.getInt("type") == 1 ){
                    total -= (float)result.getInt("value");
                }else{
                    total -= total*(float)result.getInt("value")/100;
                }
            }catch(Exception excp){
                DatabaseCon.showOptionPane(this,excp);
            }finally {
                db.closeConnection();
            }
        }
        totalAmountLabel.setText(String.format("Total Amount : %.02f Rs", total));

        if( c_id ==  0 ){
            customerStatusLabel.setText("Cutomer Id : N/A");
        }else{
            customerStatusLabel.setText("Customer Id : "+c_id);
        }

        if( discountCode != null ){
            discountStatusLabel.setText("Discount Status : "+discountCode);
        }else{
            discountStatusLabel.setText("Discount Status : N/A");
        }
    }


    @Override
    public void keyTyped(KeyEvent e) {
        if( !Character.isDigit(e.getKeyChar()) ){
            e.consume();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
