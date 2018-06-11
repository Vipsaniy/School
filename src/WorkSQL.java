import java.io.File;

import java.sql.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


public class WorkSQL {

    private static WorkSQL instance;
    private Connection con;

    public WorkSQL() throws SQLException, ClassNotFoundException {

        String path = new File("database").getAbsolutePath();

        String db_file_name_prefix = path+"\\ExampleDB";

        con = null;

        Class.forName("org.hsqldb.jdbcDriver");

        con = DriverManager.getConnection("jdbc:hsqldb:file:" + db_file_name_prefix, // filenames prefix
                "sa", // user
                "");  // pass
    }

    public static WorkSQL getInstance() throws SQLException, ClassNotFoundException {
        if (instance == null) {
         instance = new WorkSQL();}
        return instance;
    }

    public ArrayList<School> resultRequestSchool() {

        ArrayList<School> schoolInDB = new ArrayList<>();


        try {

            Statement statement = con.createStatement();

            ResultSet rs  = statement.executeQuery("SELECT * FROM  \"Школы\"");


            while (rs.next()) {

                String nameSchool = rs.getString("Название школы");
                int ID = rs.getInt("номер школы");

                School schoolSQL = new School(nameSchool,ID);
                schoolInDB.add(schoolSQL);

            }


            rs.close();


        } catch (SQLException ex) {
            Logger.getLogger(WorkSQL.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
        return schoolInDB;
    }

    public void addSchool(School school) {
        try {
            con.setAutoCommit(true);
            PreparedStatement psql = con.prepareStatement("INSERT INTO \"Школы\"(\"Название школы\",\"номер школы\") VALUES (?,?);");
            psql.setString(1, school.getName());
            psql.setInt(2,school.getID());

            psql.executeUpdate();

            psql.close();
        }
     catch (SQLException ex) {
        Logger.getLogger(WorkSQL.class.getName()).log(Level.SEVERE, null, ex);
        ex.printStackTrace();
    }
    }


    public ArrayList<Student> removeSchool(School school) throws SQLException, ParseException, ClassNotFoundException {


        ArrayList<Student> students = resultRequestStudents(school);

        if (students.size() == 0) {

            con.setAutoCommit(true);
            PreparedStatement psql = con.prepareStatement("DELETE FROM \"Школы\" WHERE \"номер школы\" = ? ");

            psql.setInt(1, school.getID());

            psql.executeUpdate();
        }
        return students;
    }


    public void addStudent(Student student, int numberSchool) {

        try {

            con.setAutoCommit(true);
            PreparedStatement psql = con.prepareStatement("INSERT INTO \"Студенты\"(\"Имя студента\", \"Группа\", \"дата рождения\", \"пол\", \"номер школы\") VALUES (?,?,?,?,?);");
            psql.setString(1, student.getName());
            psql.setInt(2,student.getLevel());
            psql.setDate(3,new java.sql.Date(student.getBirthday().getTimeInMillis()) );
            psql.setString(4,  student.isSex()? "м":"ж");
            psql.setInt(5,numberSchool);

            psql.executeUpdate();

            psql.close();

        } catch (SQLException ex) {
            Logger.getLogger(WorkSQL.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
    }


    public void removeStudent(School school, int ID) throws ParseException, ClassNotFoundException, SQLException {

        con.setAutoCommit(true);
        PreparedStatement psql = con.prepareStatement("DELETE FROM \"Студенты\" WHERE \"id\" = ? ");

        psql.setInt(1,ID);

        psql.executeUpdate();



    }


    public  ArrayList<Student> resultRequestStudents(School school) throws ParseException, ClassNotFoundException {

        ArrayList<Student> listStudents = new ArrayList<>();

        try {


            PreparedStatement psql = con.prepareStatement("SELECT \"Имя студента\", \"Группа\", \"дата рождения\", \"пол\",\"id\" FROM \"Студенты\"  WHERE \"номер школы\" = ? ");

            psql.setInt(1, school.getID());
            ResultSet rs = psql.executeQuery();

            while (rs.next()) {

                String name = rs.getString("Имя студента");
                int level = rs.getInt("Группа");
                Date date = rs.getDate("дата рождения");
                boolean sex = rs.getString("пол").equals("м");
                int ID = rs.getInt("id");

                Student studentSQL = new Student(name, level, date, sex);
                studentSQL.setID(ID);
                listStudents.add(studentSQL);
            }


            rs.close();

        } catch (SQLException ex) {
            Logger.getLogger(WorkSQL.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }

        return listStudents;
    }

    public void closeConnection() throws SQLException {
        con.prepareStatement("shutdown").execute();
        con.close();
    }

}

/*
con.setAutoCommit(true);
        PreparedStatement psql = con.prepareStatement("INSERT INTO \"Студенты\"(\"Имя студента\", \"Группа\", \"дата рождения\", \"пол\", \"номер школы\") VALUES (?,?,?,?,?);");
        psql.setString(1, student.getName());
        psql.setInt(2,student.getLevel());
        psql.setDate(3,new java.sql.Date(student.getBirthday().getTimeInMillis()) );
        psql.setString(4,  student.isSex()? "м":"ж");
        //  psql.setInt(5,statement.getGeneratedKeys().getInt(5));
        psql.setInt(5,2);
        psql.close();*/
