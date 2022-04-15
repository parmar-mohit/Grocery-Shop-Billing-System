package GroceryShopBillingSystem.frames.panels;

import GroceryShopBillingSystem.CreatePdf;
import GroceryShopBillingSystem.DatabaseCon;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.util.Date;

import static GroceryShopBillingSystem.Constraint.setPosition;

public class SalesHistoryPanel extends JPanel implements ActionListener {
    private JDateChooser startDate,endDate;
    private DefaultTableModel tableModel;
    private JTable table;
    private JScrollPane scrollPane;
    private JLabel messageLabel,startDateLabel,endDateLabel;
    private JButton viewHistoryButton, generateReportButton;
    private DatabaseCon db;

    public SalesHistoryPanel(){
        //Initialising Member Variables
        startDateLabel = new JLabel("Start Date : ");
        startDate = new JDateChooser();
        endDateLabel = new JLabel("End Date : ");
        endDate = new JDateChooser();
        viewHistoryButton = new JButton("View History");
        String columns[] = {"Product Id","Product Name","Quantity Sold"};
        tableModel = new DefaultTableModel(columns,0);
        table = new JTable(tableModel){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        scrollPane = new JScrollPane(table);
        messageLabel = new JLabel();
        generateReportButton = new JButton("Generate Report");

        //Editing Table Details
        table.setColumnSelectionAllowed(false);
        scrollPane.setPreferredSize(new Dimension(550,450));
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.getColumnModel().getColumn(0).setPreferredWidth(100);
        table.getColumnModel().getColumn(1).setPreferredWidth(300);
        table.getColumnModel().getColumn(2).setPreferredWidth(150);

        //Editing DateChooser
        startDate.setPreferredSize(new Dimension(125,30));
        endDate.setPreferredSize(new Dimension(125,30));

        //AddingActionListener
        viewHistoryButton.addActionListener(this);
        generateReportButton.addActionListener(this);

        //Editing panel details
        setBackground(new Color(148,212,66));
        setLayout(new GridBagLayout());

        //Adding Member to Panel
        add(startDateLabel,setPosition(0,0));add(startDate,setPosition(1,0));
        add(endDateLabel,setPosition(2,0));add(endDate,setPosition(3,0));
        add(viewHistoryButton,setPosition(0,1,2,1));add(generateReportButton,setPosition(2,1,2,1));
        add(scrollPane,setPosition(0,2,4,1));
        add(messageLabel,setPosition(0,3,2,1));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Date sDate = startDate.getDate();
        if( sDate == null ){
            messageLabel.setText("Select Proper Start Date");
            return;
        }

        Date eDate = endDate.getDate();
        if( eDate == null ){
            messageLabel.setText("Select Proper End Date");
            return;
        }

        if( eDate.before(sDate) ){
            messageLabel.setText("End Date Should be a Date After Start Date");
            return;
        }

        if( e.getSource() == viewHistoryButton){
            tableModel.getDataVector().removeAllElements();
            revalidate();


            try{
                db = new DatabaseCon();
                ResultSet result = db.getSalesHistory(sDate,eDate);

                if( !result.next() ){
                    messageLabel.setText("No Data Found");
                    return;
                }

                do{
                    tableModel.addRow(new Object[]{result.getInt(1),result.getString(2),result.getFloat(3)});
                }while( result.next() );
            }catch(Exception excp){
                DatabaseCon.showOptionPane(this,excp);
            }finally {
                db.closeConnection();
            }
        }else if( e.getSource() == generateReportButton){
            try{
                CreatePdf.CreateSalesReport(sDate,eDate);
                messageLabel.setText("Sales Report.pdf File Created");
            }catch(Exception excp ){
                DatabaseCon.showOptionPane(this,excp);
            }
        }
    }
}
