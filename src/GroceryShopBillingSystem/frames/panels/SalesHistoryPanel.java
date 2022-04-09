package GroceryShopBillingSystem.frames.panels;

import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;

import static GroceryShopBillingSystem.Constraint.setPosition;

public class SalesHistoryPanel extends JPanel {
    private JDateChooser startDate,endDate;
    private DefaultTableModel tableModel;
    private JTable table;
    private JScrollPane scrollPane;
    private JLabel messageLabel,startDateLabel,endDateLabel;
    private JButton viewHistory,exportPdf;

    public SalesHistoryPanel(){
        //Initialising Member Variables
        startDateLabel = new JLabel("Start Date : ");
        startDate = new JDateChooser();
        endDateLabel = new JLabel("End Date : ");
        endDate = new JDateChooser();
        viewHistory = new JButton("View History");
        String columns[] = {"Product Id","Product Name","Quantity Purchased","Revenue"};
        tableModel = new DefaultTableModel(columns,0);
        table = new JTable(tableModel){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        scrollPane = new JScrollPane(table);
        messageLabel = new JLabel();
        exportPdf = new JButton("Export as Pdf");

        //Editing Table Details
        table.setColumnSelectionAllowed(false);
        scrollPane.setPreferredSize(new Dimension(700,450));
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.getColumnModel().getColumn(0).setPreferredWidth(100);
        table.getColumnModel().getColumn(1).setPreferredWidth(250);
        table.getColumnModel().getColumn(2).setPreferredWidth(175);
        table.getColumnModel().getColumn(3).setPreferredWidth(175);

        //Editing DateChooser
        startDate.setPreferredSize(new Dimension(125,30));
        endDate.setPreferredSize(new Dimension(125,30));

        //Editing panel details
        setBackground(new Color(148,212,66));
        setLayout(new GridBagLayout());

        //Adding Member to Panel
        add(startDateLabel,setPosition(0,0));add(startDate,setPosition(1,0));
        add(endDateLabel,setPosition(2,0));add(endDate,setPosition(3,0));
        add(viewHistory,setPosition(0,1,2,1));add(exportPdf,setPosition(2,1,2,1));
        add(scrollPane,setPosition(0,2,4,1));
        add(messageLabel,setPosition(0,3,2,1));
    }
}
