package GroceryShopBillingSystem;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.sql.ResultSet;
import java.util.stream.Stream;

public class CreatePdf {
     public static void CreateBill(int b_id) throws Exception {
         Document document = new Document();
         PdfWriter.getInstance(document, new FileOutputStream("Bill.pdf"));
         document.open();
         PdfPTable table = new PdfPTable(7);
         addTableHeader(table);
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

    private static void addTableHeader(PdfPTable table) {
        Stream.of("Sr No", "Particulars", "Product Id","Quantity","Rate","Value","Tax(%)")
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(new BaseColor(60,122,37));
                    header.setPhrase(new Phrase(columnTitle));
                    table.addCell(header);
                });
    }
}
