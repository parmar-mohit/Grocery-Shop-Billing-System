package GroceryShopBillingSystem.frames.panels;

import GroceryShopBillingSystem.DatabaseCon;
import GroceryShopBillingSystem.frames.dialog.UpdateProductCategoryDialog;
import GroceryShopBillingSystem.frames.dialog.UpdateProductInfoDialog;
//import GroceryShopBillingSystem.frames.dialog.UpdateProductInfoDialog;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.ResultSet;

import static GroceryShopBillingSystem.Constraint.setPosition;
import static GroceryShopBillingSystem.frames.panels.NewProductPanel.KGS;
import static GroceryShopBillingSystem.frames.panels.NewProductPanel.LTR;
import static GroceryShopBillingSystem.frames.panels.NewProductPanel.PCS;

public class UpdateProductInfoPanel extends JPanel implements ActionListener,WindowListener {

    private DefaultTableModel tableModel;
    private JTable table;
    private JScrollPane scrollPane;
    private JLabel messageLabel;
    private JButton updateProductInfoButton,updateCategoryButton;
    private DatabaseCon db;


    public UpdateProductInfoPanel(){
        //Initialising Member Variables
        String columns[] = {"Product Name","Product Code","Selling Price","Cost Price","Quantity","Category","Tax Rate"};
        tableModel = new DefaultTableModel(columns,0);
        table = new JTable(tableModel){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        scrollPane = new JScrollPane(table);
        messageLabel = new JLabel();
        updateProductInfoButton = new JButton("Update Product Info");
        updateCategoryButton = new JButton("Update Category");

        //Filling Table
        fillTable();

        //Editing Member Variables
        table.setColumnSelectionAllowed(false);
        scrollPane.setPreferredSize(new Dimension(700, 500));

        //Adding listener to Button
        updateProductInfoButton.addActionListener(this);
        updateCategoryButton.addActionListener(this);

        //Editing panel details
        setBackground(new Color(148,212,66));
        setLayout(new GridBagLayout());

        //adding members to panel
        add(scrollPane,setPosition(0,0,2,1));
        add(messageLabel,setPosition(0,1,2,1));
        add(updateProductInfoButton,setPosition(0,2));
        add(updateCategoryButton,setPosition(1,2));
    }

    private void fillTable(){
        tableModel.getDataVector().removeAllElements();
        revalidate();
        try{
            db = new DatabaseCon();

            ResultSet result = db.executeQuery("SELECT product.*,category_name FROM product JOIN product_category WHERE product.category=product_category.cat_id;");

            if( !result.next()  ){
                messageLabel.setText("No Product Data Found");
                return;
            }

            do{
                int product_code = result.getInt("p_id");
                String product_name = result.getString("p_name");
                float sellingPrice = result.getFloat("s_price");
                float costPrice = result.getFloat("c_price");
                float inventory = result.getFloat("inventory");
                int unit = result.getInt("unit");
                float tax = result.getFloat("tax_rate");
                String category = result.getString("category_name");

                String units;
                if( unit == KGS ){
                    units = "Kgs";
                }else if( unit == PCS ){
                    units = "Pcs";
                }else{
                    units = "Ltr";
                }

                tableModel.addRow(new Object[]{product_name,product_code,sellingPrice,costPrice,inventory+" "+units,category,tax});
            }while(result.next());

            revalidate();
            repaint();
        }catch(Exception e){
            DatabaseCon.showOptionPane(this,e);
        }finally{
            db.closeConnection();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if( e.getSource() == updateProductInfoButton ) {
            int row = table.getSelectedRow();

            if (row == -1) {
                messageLabel.setText("Select a Product");
                return;
            }

            try {
                JDialog dialog = new UpdateProductInfoDialog((JFrame) SwingUtilities.getWindowAncestor(this), (int) tableModel.getValueAt(row, 1));
                dialog.addWindowListener(this);
            } catch (Exception excp) {
                System.out.println(excp);
            }
        }else if( e.getSource() == updateCategoryButton ){
            try {
                JDialog dialog = new UpdateProductCategoryDialog((JFrame) SwingUtilities.getWindowAncestor(this));
                dialog.addWindowListener(this);
            } catch (Exception excp) {
                System.out.println(excp);
            }
        }
    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {

    }

    @Override
    public void windowClosed(WindowEvent e) {
        fillTable();
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
}
