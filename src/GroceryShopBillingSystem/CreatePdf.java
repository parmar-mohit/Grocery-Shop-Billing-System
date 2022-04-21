package GroceryShopBillingSystem;

import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.*;

import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.stream.Stream;

public class CreatePdf {
     public static void CreateBill(int b_id) throws Exception {
         Document document = new Document();
         PdfWriter.getInstance(document, new FileOutputStream("Bill.pdf"));
         document.open();
         PdfPTable table = new PdfPTable(7);
         addTableHeaderBill(table);
         table.setTotalWidth(1200);
         table.setWidths(new int[]{2,10,3,4,4,4,3});

         float subTotal = 0;
         float tax = 0;
         DatabaseCon db = null;
         try {
             db = new DatabaseCon();
             ResultSet result = db.getBillData(b_id);
             int i = 1;
             while(result.next() ){
                 table.addCell(i+"");
                 table.addCell(result.getString("p_name"));
                 table.addCell(result.getInt("p_id")+"");
                 table.addCell(result.getFloat("quantity")+"");
                 table.addCell(result.getFloat("s_price")+"");
                 table.addCell((result.getFloat("quantity")*result.getFloat("s_price"))+"");
                 table.addCell(result.getFloat("tax_rate")+"");

                 subTotal += result.getFloat("s_price")*result.getFloat("quantity");
                 tax += result.getFloat("tax_rate")/100*(result.getFloat("s_price")*result.getFloat("quantity"));

                 i++;
             }
         } catch (Exception e) {
             System.out.println(e);
         } finally {
             db.closeConnection();
         }

         PdfPCell cell = new PdfPCell(new Phrase("----------------------------------------------"));
         cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
         cell.setColspan(7);
         table.addCell(cell);

         cell = new PdfPCell(new Phrase("Sub Total "));
         cell.setColspan(6);
         cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
         table.addCell(cell);
         cell = new PdfPCell(new Phrase(subTotal + ""));
         cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
         table.addCell(cell);

         cell = new PdfPCell(new Phrase("Tax"));
         cell.setColspan(6);
         cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
         table.addCell(cell);
         cell = new PdfPCell(new Phrase(tax + ""));
         cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
         table.addCell(cell);

         cell = new PdfPCell(new Phrase("Total Amount"));
         cell.setColspan(6);
         cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
         table.addCell(cell);
         cell = new PdfPCell(new Phrase((subTotal + tax) + ""));
         cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
         table.addCell(cell);

         try{
             db = new DatabaseCon();

             if( db.checkExist("b_id","bill_with_discount",b_id)){
                 cell = new PdfPCell(new Phrase("Discount"));
                 cell.setColspan(5);
                 cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                 table.addCell(cell);

                 ResultSet result = db.executeQuery("SELECT * FROM bill_with_discount JOIN discount WHERE bill_with_discount.discount_code = discount.discount_code AND b_id ="+b_id+";");
                 result.next();

                 cell = new PdfPCell(new Phrase(result.getString("discount_code")));
                 cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                 table.addCell(cell);

                 float value;
                 if( result.getInt("type") == 1 ){
                     value = result.getInt("value");
                 }else{
                     value = (subTotal+tax)*result.getInt("value")/100;
                 }

                 cell = new PdfPCell(new Phrase("-"+value));
                 cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                 table.addCell(cell);

                 cell = new PdfPCell(new Phrase("Net Payable Amt"));
                 cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                 cell.setColspan(6);
                 table.addCell(cell);

                 cell = new PdfPCell(new Phrase(String.format("%.02f",(subTotal+tax-value))));
                 cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                 table.addCell(cell);
             }
         }catch(Exception e){
             System.out.println(e);
         }finally{
             db.closeConnection();
         }

         document.add(table);

         try{
             db = new DatabaseCon();

             if( db.checkExist("b_id","receipent",b_id)){
                 ResultSet result = db.executeQuery("SELECT * FROM customer WHERE c_id = (SELECT c_id FROM receipent WHERE b_id="+b_id+");");
                 result.next();
                 document.add(new Paragraph("Thank you "+result.getString("c_name")+" for Shopping with us."));
             }
         }catch(Exception e){
             System.out.println(e);
         }finally {
             db.closeConnection();
         }
         document.close();
     }

    private static void addTableHeaderBill(PdfPTable table) {
        Stream.of("Sr No", "Particulars", "Product Id","Quantity","Rate","Value","Tax(%)")
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(new BaseColor(60,122,37));
                    header.setPhrase(new Phrase(columnTitle));
                    table.addCell(header);
                });
    }

    public static void CreateCustomerList() throws Exception {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream("Customer List.pdf"));
        document.open();
        PdfPTable table = new PdfPTable(4);
        addTableHeaderCustomer(table);

        DatabaseCon db=null;
        try{
            db = new DatabaseCon();
            ResultSet result = db.executeQuery("SELECT * FROM customer;");
            while( result.next() ){
                table.addCell(result.getInt("c_id")+"");
                table.addCell(result.getString("c_name"));
                table.addCell(result.getString("email"));
                table.addCell(result.getString("phone"));
            }
        }catch(Exception e){
            System.out.println(e);
        }finally {
            db.closeConnection();
        }

        document.add(table);
        document.close();
    }

    private static void addTableHeaderCustomer(PdfPTable table) {
        Stream.of("Id","Name","Email","Phone")
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(new BaseColor(60,122,37));
                    header.setPhrase(new Phrase(columnTitle));
                    table.addCell(header);
                });
    }

    public static void CreateSalesReport(Date sDate,Date eDate) throws Exception {
         //Opening File
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream("Sales Report.pdf"));
        document.open();

        //Writing "Sales Report" Title
        Font titleFont = new Font(Font.FontFamily.TIMES_ROMAN,60.0f,Font.BOLD,BaseColor.BLUE);
        Phrase titlePhrase = new Phrase("Sales Report",titleFont);
        Paragraph title = new Paragraph(titlePhrase);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        //Writing Report start and end Date
        document.add(new Paragraph("\n\n"));
        Paragraph about = new Paragraph("This Sales Report has Data From Date : "+new SimpleDateFormat("dd-MM-yyyy").format(sDate)+" to Date : "+new SimpleDateFormat("dd-MM-yyyy").format(eDate));
        about.setAlignment(Element.ALIGN_CENTER);
        document.add(about);

        //Writing Table of Product Quantity Sold
        document.add(new Paragraph("\n"));
        PdfPTable table = new PdfPTable(3);
        addTableHeaderSalesHistory(table);

        DatabaseCon db=null;
        try{
            db = new DatabaseCon();
            ResultSet result = db.getSalesHistory(sDate,eDate);
            while( result.next() ){
                table.addCell(result.getInt(1)+"");
                table.addCell(result.getString(2));
                table.addCell(result.getFloat(3)+"");
            }

            document.add(table);

            String columns[] = {"Date"};
            DefaultTableModel tableModel = new DefaultTableModel(columns,0);
            Date currentDate = new Date(sDate.getTime());
            while( ! currentDate.after(eDate) ) {
                tableModel.addRow(new Object[]{new SimpleDateFormat("dd-MM-yyyy").format(currentDate)});
                //Incrementing Date by one day
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(currentDate);
                calendar.add(Calendar.DATE,1);
                currentDate = calendar.getTime();
            }

            //Generating Graph for Product Sales
            Font subHeadingFont = new Font(Font.FontFamily.TIMES_ROMAN,20.0f,Font.BOLD,BaseColor.RED);
            ResultSet productIdName = db.getIdName(sDate,eDate);

            int i , j =1;
            String pNameArray[] = new String[100];

            while( productIdName.next() ){
                document.newPage();
                //Getting productId and Name
                String productName = productIdName.getString(2);
                int p_id = productIdName.getInt(1);
                tableModel.addColumn(productName);
                pNameArray[j-1] = productName;

                //Writing SubHeading
                Phrase subHeadingPhrase = new Phrase("Sales Graph for "+productName,subHeadingFont);
                document.add(new Paragraph(subHeadingPhrase));
                document.add(new Paragraph("\n"));

                //Creating Arguement String for Python Program
                String argumentString = "\""+productName+"\" ";
                argumentString += new SimpleDateFormat("dd-MM-yyyy").format(sDate)+" ";
                argumentString += new SimpleDateFormat("dd-MM-yyyy").format(eDate)+" ";
                currentDate = new Date(sDate.getTime());
                i = 0;
                while( ! currentDate.after(eDate) ){
                    float quantitySold = db.dailyQuantitySold(p_id,currentDate);
                    argumentString += quantitySold+" ";
                    tableModel.setValueAt(quantitySold,i,j);

                    //Incrementing Date by one day
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(currentDate);
                    calendar.add(Calendar.DATE,1);
                    currentDate = calendar.getTime();
                    i++;
                }

                //Executing Python Program to Create Graph Image
                Process process = Runtime.getRuntime().exec("python CreateGraph.py "+argumentString);
                process.waitFor();

                //Inserting Image into Pdf
                Image img = new Jpeg(Utilities.toURL("Graph.jpg"));
                img.setAlignment(Element.ALIGN_CENTER);
                img.scaleToFit(400,300);
                document.add(img);
                j++;
            }

            document.newPage();
            document.add(new Paragraph("Quantity Sold Breakdown By Date",subHeadingFont));
            document.add(new Paragraph("\n\n"));
            table =  new PdfPTable(j);
            addTableHeaderProductSales(table,pNameArray,j-1);
            int k ,l;
            for( k = 0; k <tableModel.getRowCount(); k++){
                for( l = 0; l < j; l++){
                    table.addCell(tableModel.getValueAt(k,l)+"");
                }
            }

            //Writing Table;
            document.add(table);
        }catch(Exception e){
            System.out.println(e);
        }finally {
            db.closeConnection();
        }

        //Closing Document
        document.close();
    }

    private static void addTableHeaderSalesHistory(PdfPTable table) {
        Stream.of("Product Id","Product Name","Quantity Sold")
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(new BaseColor(60,122,37));
                    header.setPhrase(new Phrase(columnTitle));
                    table.addCell(header);
                });
    }

    public static void addTableHeaderProductSales(PdfPTable table,String pNameArray[],int n) {
         String columns[] = new String[100];
         columns[0] = "Date";
         for( int i = 1; i <= n; i++ ){
             columns[i] = pNameArray[i-1];
         }
        Stream.of(columns)
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(new BaseColor(128,166,237));
                    header.setPhrase(new Phrase(columnTitle));
                    table.addCell(header);
                });
    }
}
