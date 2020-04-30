/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package konyvtar.database;

/**
 *
 * @author csicsek
 */
public final class AdatbazisKezelo {
    private static AdatbazisKezelo kezelo;
    
    private static final String DB_URL = "jdbc:derby:database/library2;create=true";
    private static Connection conn = null;
    private static Statement stmt = null;

    private AdatbazisKezelo() {
        createConnection();
        setupBookTable();
        setupMemberTable();
        setupKiadTable();
    }
    
    public static AdatbazisKezelo getInstance() {
        if (kezelo == null){
            kezelo = new AdatbazisKezelo();
        }
        return kezelo;
    }
    //Itt hozzuk létre a kapcsolatot.
    void createConnection() {
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
            conn = DriverManager.getConnection(DB_URL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //Itt hozzuk létre a BOOK táblát.
    void setupBookTable() {
        String TABLE_NAME = "BOOK";
        try {
            stmt = conn.createStatement();
            DatabaseMetaData dbm = conn.getMetaData();
            ResultSet tables = dbm.getTables(null,null, TABLE_NAME.toUpperCase(),null);
            if (tables.next()) {
                System.out.println("A " + TABLE_NAME + " már készen áll!");
            } else {
                stmt.execute("CREATE TABLE " + TABLE_NAME + "("
                        + "  id varchar(200) primary key,\n"
                        + "  title varchar(200),\n"
                        + "  author varchar(200),\n"
                        + "  publisher varchar(200),\n"
                        + "  isAvail boolean default true"
                        + " )");
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage() + " .. adatbázis hiba");
        } finally {
        }
    } 
    
    //Itt hozzuk létre a MEMBER táblát.
    void setupMemberTable() {
        String TABLE_NAME = "MEMBER";
        try {
            stmt = conn.createStatement();
            DatabaseMetaData dbm = conn.getMetaData();
            ResultSet tables = dbm.getTables(null,null, TABLE_NAME.toUpperCase(),null);
            if (tables.next()) {
                System.out.println("A " + TABLE_NAME + " asztal már készen áll!");
            } else {
                stmt.execute("CREATE TABLE " + TABLE_NAME + "("
                        + "name varchar(50),\n "
                        + "id varchar(200), \n"
                        + "mobile varchar(25), \n"
                        + "email varchar(100), \n"
                        + "primary key (id))");
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage() + " .. adatbázis hiba");
        } finally {
        }
    }
    
    
    //Itt hozzuk létre a KIAD táblát.
    void setupKiadTable() {
        String TABLE_NAME = "KIAD";
        try {
            stmt = conn.createStatement();
            DatabaseMetaData dbm = conn.getMetaData();
            ResultSet tables = dbm.getTables(null, null, TABLE_NAME.toUpperCase(), null);
            if (tables.next()){
                System.out.println("A " + TABLE_NAME + " asztal már készen áll!");
            } else {
                stmt.execute("CREATE TABLE " + TABLE_NAME + "("
                        + "  bookID varchar(200) primary key,\n"
                        + "  memberID varchar(200),\n"
                        + "  kiadTime timestamp default CURRENT_TIMESTAMP,\n"
                        + "  megujit_count integer default 0,\n"
                        + "  FOREIGN KEY (bookID) REFERENCES BOOK(id),\n"
                        + "  FOREIGN KEY (memberID) REFERENCES MEMBER(id))\n");
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage() + " .. adatbázis hiba");
        } finally {
        }
    }
    
    public ResultSet execQuery(String query) {
        ResultSet result;
        try {
            stmt = conn.createStatement();
            result = stmt.executeQuery(query);
        } catch (SQLException exception) {
            System.out.println("Exception at execQuery:dataHandler" + exception.getLocalizedMessage());
            return null;
        } finally {}
        return result;
    }
    
    public boolean  execAction(String qu) {
        try {
            stmt = conn.createStatement();
            stmt.execute(qu);
            return true;
        } catch (SQLException exception)  {
            JOptionPane.showMessageDialog(null, "Error:" + exception.getMessage(),"Error occured",JOptionPane.ERROR_MESSAGE);
            System.out.println("Exception at execQuery:dataHendler" + exception.getLocalizedMessage());
            return false;
        } finally {
        }
    }
}

