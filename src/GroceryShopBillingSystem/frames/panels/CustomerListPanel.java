package GroceryShopBillingSystem.frames.panels;

import GroceryShopBillingSystem.CreatePdf;
import GroceryShopBillingSystem.DatabaseCon;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.xml.crypto.Data;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;

import static GroceryShopBillingSystem.Constraint.setPosition;

public class CustomerListPanel extends JPanel implements ActionListener {

    private DefaultTableModel tableModel;
    private JTable table;
    private JScrollPane scrollPane;
    private JLabel messageLabel;
    private JButton exportButton;
    private DatabaseCon db;

    public CustomerListPanel(){
        //Initialising Member Variables
        String columns[] = {"Id","Name","Email","Phone"};
        tableModel = new DefaultTableModel(columns,0);
        table = new JTable(tableModel);
        scrollPane = new JScrollPane(table);
        messageLabel = new JLabel();
        exportButton = new JButton("Export as Pdf");

        fillTable();

        //AddingActionListener
        exportButton.addActionListener(this);

        //Editing panel details
        setBackground(new Color(148,212,66));
        setLayout(new GridBagLayout());

        //Adding Memeber Variables to Panel
        add(scrollPane,setPosition(0,0));
        add(messageLabel,setPosition(0,1));
        add(exportButton,setPosition(0,2));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try{
            CreatePdf.CreateCustomerList();
            messageLabel.setText("Customer List.pdf File Created");
        }catch(Exception excp){
            DatabaseCon.showOptionPane(this,excp);
        }
    }

    private void fillTable(){
        try{
            db = new DatabaseCon();
            ResultSet result = db.executeQuery("SELECT * FROM customer;");

            if( !result.next() ){
                messageLabel.setText("No Data Found");
                return;
            }

            do{
                int id = result.getInt("c_id");
                String name = result.getString("c_name");
                String email = result.getString("email");
                String phone = result.getString("phone");
                tableModel.addRow(new Object[]{id,name,email,phone});
            }while( result.next() );

        }catch(Exception e){
            DatabaseCon.showOptionPane(this,e);
        }finally {
            db.closeConnection();
        }
    }
}
