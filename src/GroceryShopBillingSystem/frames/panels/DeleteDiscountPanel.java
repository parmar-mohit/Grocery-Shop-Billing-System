package GroceryShopBillingSystem.frames.panels;

import GroceryShopBillingSystem.DatabaseCon;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;

import static GroceryShopBillingSystem.Constraint.setPosition;

public class DeleteDiscountPanel extends JPanel implements ActionListener {
    private DefaultTableModel tableModel;
    private JTable table;
    private JScrollPane scrollPane;
    private JLabel messageLabel;
    private JButton deleteButton;
    private DatabaseCon db;

    public DeleteDiscountPanel(){
        //Initialising Member Variables
        String columns[] = {"Discount Code","Type","Value","Min Purchase Amt"};
        tableModel = new DefaultTableModel(columns,0);
        table = new JTable(tableModel){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        fillTable();
        scrollPane = new JScrollPane(table);
        messageLabel = new JLabel();
        deleteButton = new JButton("Delete Discount Code");

        //Editing Member Variables
        table.setColumnSelectionAllowed(false);

        //Editing Dimension
        scrollPane.setPreferredSize(new Dimension(500,300));

        //Adding ActionListener
        deleteButton.addActionListener(this);

        //Editing panel details
        setBackground(new Color(148,212,66));
        setLayout(new GridBagLayout());

        //Adding Members to Panel
        add(scrollPane,setPosition(0,0));
        add(messageLabel,setPosition(0,1));
        add(deleteButton,setPosition(0,2));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int row = table.getSelectedRow();
        if( row == - 1 ){
            messageLabel.setText("Select Discount Code");
            return;
        }

        String discountCode = new String(tableModel.getValueAt(row,0)+"");
        try{
            db = new DatabaseCon();
            db.deleteDiscount(discountCode);
            messageLabel.setText("Discount Code Deleted");
            fillTable();
        }catch(Exception excp){
            DatabaseCon.showOptionPane(this,excp);
        }finally {
            db.closeConnection();
        }
    }

    private void fillTable(){
        tableModel.getDataVector().removeAllElements();
        revalidate();
        try{
            db = new DatabaseCon();

            ResultSet result = db.executeQuery("SELECT * FROM discount");

            if( !result.next()  ){
                messageLabel.setText("No Product Data Found");
                return;
            }

            do{
                String discountCode = result.getString("discount_code");
                int type = result.getInt("type");
                String typeString;
                if( type == 1 ){
                    typeString = "Fixed Value";
                }else{
                    typeString = "Percent";
                }
                int value = result.getInt("value");
                int mpa = result.getInt("min_pur_amt");

                tableModel.addRow(new Object[]{discountCode,typeString,value,mpa});
            }while(result.next());

            revalidate();
            repaint();
        }catch(Exception e){
            DatabaseCon.showOptionPane(this,e);
        }finally{
            db.closeConnection();
        }
    }
}
