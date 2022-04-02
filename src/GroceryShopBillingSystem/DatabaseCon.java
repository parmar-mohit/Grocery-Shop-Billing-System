package GroceryShopBillingSystem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.Vector;

public class DatabaseCon {

    private static final String URL = "jdbc:mysql://localhost:3306/billing_system";
    private static final String USERNAME = "shop_user";
    private static final String PASSWORD = "shop_pass";
    private Connection db;

    public DatabaseCon() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver"); //Loading SQL Connector Driver
        db = DriverManager.getConnection(URL,USERNAME,PASSWORD); //Creating Connection with database
    }

    public void closeConnection(){
        try {
            db.close();
        }catch(Exception e ) {
            System.out.println(e);
        }
    }

    public ResultSet executeQuery(String query) throws Exception {
        Statement statement = db.createStatement();
        return statement.executeQuery(query);
    }

    public static void showOptionPane(JComponent parent,Exception e){
        System.out.println("Connection to Database Failed");
        System.out.println(e);
        JOptionPane messageBox = new JOptionPane();
        messageBox.showMessageDialog(parent,"We are unable to Connect to database right now.Please try again later","Connection Failed",JOptionPane.ERROR_MESSAGE);
    }

    public static void showOptionPane(JFrame parent,Exception e){
        System.out.println("Connection to Database Failed");
        System.out.println(e);
        JOptionPane messageBox = new JOptionPane();
        messageBox.showMessageDialog(parent,"We are unable to Connect to database right now.Please try again later","Connection Failed",JOptionPane.ERROR_MESSAGE);
    }

    public Vector<String> getCategoryList() throws Exception {
        ResultSet result = executeQuery("SELECT * FROM product_category ORDER BY cat_id;");;
        Vector<String> categoryList = new Vector<String>();
        while(result.next()){
            categoryList.add(result.getString("category_name"));
        }
        return categoryList;
    }

    public void addCategory(int cat_id,String category_name) throws Exception{
        PreparedStatement preparedStatement = db.prepareStatement("INSERT INTO product_category VALUES(?,?);");
        preparedStatement.setInt(1,cat_id);
        preparedStatement.setString(2,category_name);
        preparedStatement.executeUpdate();
    }

    public void updateCategorySet(String category_name,float s_price,float tax) throws Exception {
        PreparedStatement preparedStatement = db.prepareStatement("UPDATE product SET s_price=?,tax_rate=? WHERE category = ( SELECT cat_id FROM product_category WHERE category_name = ? );");
        preparedStatement.setFloat(1,s_price);
        preparedStatement.setFloat(2,tax);
        preparedStatement.setString(3,category_name);
        preparedStatement.executeUpdate();
    }

    public void updateCategoryFixedIncrement(String category_name,float s_price,float tax) throws Exception {
        PreparedStatement preparedStatement = db.prepareStatement("UPDATE product SET s_price = s_price + ?,tax_rate = tax_rate + ? WHERE category = ( SELECT cat_id FROM product_category WHERE category_name = ? );");
        preparedStatement.setFloat(1,s_price);
        preparedStatement.setFloat(2,tax);
        preparedStatement.setString(3,category_name);
        preparedStatement.executeUpdate();
    }

    public void updateCategoryFixedDecrement(String category_name,float s_price,float tax) throws Exception {
        PreparedStatement preparedStatement = db.prepareStatement("UPDATE product SET s_price = s_price - ?,tax_rate = tax_rate - ? WHERE category = ( SELECT cat_id FROM product_category WHERE category_name = ? );");
        preparedStatement.setFloat(1,s_price);
        preparedStatement.setFloat(2,tax);
        preparedStatement.setString(3,category_name);
        preparedStatement.executeUpdate();
    }

    public void updateCategoryPercentIncrement(String category_name,float s_price,float tax) throws Exception {
        PreparedStatement preparedStatement = db.prepareStatement("UPDATE product SET s_price = (s_price + (s_price/100)*?),tax_rate = (tax_rate + (tax_rate/100)*?) WHERE category = ( SELECT cat_id FROM product_category WHERE category_name = ? );");
        preparedStatement.setFloat(1,s_price);
        preparedStatement.setFloat(2,tax);
        preparedStatement.setString(3,category_name);
        preparedStatement.executeUpdate();
    }

    public void updateCategoryPercentDecrement(String category_name,float s_price,float tax) throws Exception {
        PreparedStatement preparedStatement = db.prepareStatement("UPDATE product SET s_price = (s_price - (s_price/100)*?),tax_rate = (tax_rate - (tax_rate/100)*?) WHERE category = ( SELECT cat_id FROM product_category WHERE category_name = ? );");
        preparedStatement.setFloat(1,s_price);
        preparedStatement.setFloat(2,tax);
        preparedStatement.setString(3,category_name);
        preparedStatement.executeUpdate();
    }

    public void addProduct(int productCode,String productName,float sellingPrice,float costPrice,int unit,String category,float inventory,float tax) throws Exception {
        PreparedStatement preparedStatement = db.prepareStatement("INSERT INTO product VALUES(?,?,?,?,?,?,?,(SELECT cat_id FROM product_category WHERE category_name=?));");
        preparedStatement.setInt(1,productCode);
        preparedStatement.setString(2,productName);
        preparedStatement.setFloat(3,sellingPrice);
        preparedStatement.setFloat(4,costPrice);
        preparedStatement.setFloat(5,inventory);
        preparedStatement.setInt(6,unit);
        preparedStatement.setFloat(7,tax);
        preparedStatement.setString(8,category);
        preparedStatement.executeUpdate();
    }

    public void updateProductInfo(int productCode,String productName,float sellingPrice,float costPrice,int unit,String category,float inventory,float tax) throws Exception {
        PreparedStatement preparedStatement = db.prepareStatement("UPDATE product SET p_name=?, s_price=?,c_price=?,inventory=?,unit=?,tax_rate=?,category=(SELECT cat_id FROM product_category WHERE category_name=?) WHERE p_id=?;");
        preparedStatement.setString(1,productName);
        preparedStatement.setFloat(2,sellingPrice);
        preparedStatement.setFloat(3,costPrice);
        preparedStatement.setFloat(4,inventory);
        preparedStatement.setInt(5,unit);
        preparedStatement.setFloat(6,tax);
        preparedStatement.setString(7,category);
        preparedStatement.setInt(8,productCode);
        preparedStatement.executeUpdate();
    }

    public Boolean checkExist(String column,String table,int entry) throws Exception{
        String query = "SELECT EXISTS (SELECT "+column+" FROM "+table+" WHERE "+column+"="+entry+");";
        Statement statement = db.createStatement();
        ResultSet result = statement.executeQuery(query);
        result.next();

        return result.getBoolean(1);
    }

    public Boolean checkExist(String column,String table,String entry) throws Exception{
        PreparedStatement preparedStatement = db.prepareStatement("SELECT EXISTS (SELECT "+column+" FROM "+table+" WHERE "+column+"=?);");
        preparedStatement.setString(1,entry);
        ResultSet result = preparedStatement.executeQuery();
        result.next();

        return result.getBoolean(1);
    }

    public void addOperator(String username,String password,String employee_id) throws Exception {
        PreparedStatement preparedStatement = db.prepareStatement("INSERT INTO user VALUES(?,?,?);");
        preparedStatement.setString(1,username);
        preparedStatement.setString(2,password);
        preparedStatement.setString(3,employee_id);
        preparedStatement.executeUpdate();
    }

    public void addDiscountCode(String discountCode,int type,int value,int mpa) throws Exception{
        PreparedStatement preparedStatement = db.prepareStatement("INSERT INTO discount VALUES(?,?,?,?);");
        preparedStatement.setString(1,discountCode);
        preparedStatement.setInt(2,type);
        preparedStatement.setInt(3,value);
        preparedStatement.setInt(4,mpa);
        preparedStatement.executeUpdate();
    }

    public void addCustomer(int c_id,String name,String email,String phone) throws Exception{
        PreparedStatement preparedStatement = db.prepareStatement("INSERT INTO customer VALUES(?,?,?,?);");
        preparedStatement.setInt(1,c_id);
        preparedStatement.setString(2,name);
        if( email.isEmpty() ){
            System.out.println("enter");
            preparedStatement.setNull(3,Types.VARCHAR);
        }else {
            preparedStatement.setString(3, email);
        }

        if( phone.isEmpty() ){
            preparedStatement.setNull(4,Types.VARCHAR);
        }else {
            preparedStatement.setString(4, phone);
        }
        preparedStatement.executeUpdate();
    }

    public ResultSet getProductInfo(int productId) throws Exception{
        PreparedStatement preparedStatement = db.prepareStatement("SELECT * FROM product WHERE p_id=?;");
        preparedStatement.setInt(1,productId);
        ResultSet result = preparedStatement.executeQuery();
        return result;
    }

    public int getCustomerId(String customerDetail, String customerType) throws Exception {
        if( customerType.equals("Email") ){
            customerType = "email";
        }else{
            customerType = "phone";
        }

        if( !checkExist(customerType,"customer",customerDetail)){
            return -1;
        }else{
            PreparedStatement preparedStatement = db.prepareStatement("SELECT c_id FROM customer WHERE "+customerType+"=?");
            preparedStatement.setString(1,customerDetail);
            ResultSet result = preparedStatement.executeQuery();
            result.next();
            return result.getInt("c_id");
        }
    }

    public int createBill(DefaultTableModel tableModel,String discountCode,int c_id,String username) throws Exception{
        PreparedStatement preparedStatement = db.prepareStatement("SELECT MAX(b_id) FROM bill;");
        ResultSet result = preparedStatement.executeQuery();
        result.next();

        int b_id = (int)result.getInt(1)+1;

        preparedStatement = db.prepareStatement("INSERT INTO bill VALUES(?,?);");
        preparedStatement.setInt(1,b_id);
        preparedStatement.setDate(2,new Date(new java.util.Date().getTime()));
        preparedStatement.executeUpdate();

        for( int i = 0; i < tableModel.getRowCount(); i++){
            int productCode = Integer.parseInt(tableModel.getValueAt(i,2)+"");
            String quantityString = tableModel.getValueAt(i,3)+"";
            String quantityArray[] = quantityString.split(" ",5);
            float inventory = Float.parseFloat(quantityArray[0]);
            if( quantityArray[1].equals("Gms") || quantityArray[1].equals("Milli Ltr") ){
                inventory /= 1000;
            }

            preparedStatement = db.prepareStatement("UPDATE product SET inventory = inventory - ? WHERE p_id=?;");
            preparedStatement.setFloat(1,inventory);
            preparedStatement.setInt(2,productCode);
            preparedStatement.executeUpdate();

            preparedStatement = db.prepareStatement("INSERT INTO bill_contents VALUES(?,?,?);");
            preparedStatement.setInt(1,b_id);
            preparedStatement.setInt(2,productCode);
            preparedStatement.setFloat(3,inventory);
            preparedStatement.executeUpdate();
        }

        if( discountCode != null ){
            preparedStatement = db.prepareStatement("INSERT INTO bill_with_discount VALUES(?,?);");
            preparedStatement.setInt(1,b_id);
            preparedStatement.setString(2,discountCode);
            preparedStatement.executeUpdate();
        }

        if( c_id != 0 ){
            preparedStatement = db.prepareStatement("INSERT INTO receipent VALUES(?,?);");
            preparedStatement.setInt(1,c_id);
            preparedStatement.setInt(2,b_id);
            preparedStatement.executeUpdate();
        }

        preparedStatement = db.prepareStatement("INSERT INTO creates VALUES(?,?);");
        preparedStatement.setString(1,username);
        preparedStatement.setInt(2,b_id);
        preparedStatement.executeUpdate();

        return b_id;
    }

    public ResultSet getBillData(int b_id) throws Exception{
        PreparedStatement preparedStatement = db.prepareStatement("SELECT product.*,bill_contents.quantity FROM product JOIN bill_contents WHERE b_id = ? AND bill_contents.p_id = product.p_id;");
        preparedStatement.setInt(1,b_id);

        return preparedStatement.executeQuery();
    }

    public void deleteDiscount(String discountCode) throws Exception{
        PreparedStatement preparedStatement = db.prepareStatement("DELETE FROM discount WHERE discount_code=?;");
        preparedStatement.setString(1,discountCode);
        preparedStatement.executeUpdate();
    }

    public void change_password(String username,String password) throws Exception {
        PreparedStatement preparedStatement =  db.prepareStatement("UPDATE user SET password=? WHERE username=?");
        preparedStatement.setString(1,password);
        preparedStatement.setString(2,username);
        preparedStatement.executeUpdate();
    }
}
