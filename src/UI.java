import com.sun.deploy.panel.NumberDocument;


import javax.swing.*;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import javax.swing.text.MaskFormatter;

import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.text.ParseException;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UI extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JComboBox comboBox1;
    private JTable table1;
    private JButton buttonAddStudent;
    private JButton buttonRemoveStudent;
    private JFormattedTextField nameFormAddStudent;
    private JFormattedTextField birthdayFormAddStudent;
    private JFormattedTextField levelFormAddStudent;
    private JSpinner sexSpinner;
    private JButton addSchoolButton;
    private JButton removeSchoolButton;
    private JFormattedTextField formattedSchool;
    private JLabel errorLabel;


    private int removeHardButton = 0;
    private ArrayList<School> schools;
    private WorkSQL workSQL;
    private int IDforRemove;

    public UI() throws SQLException, ClassNotFoundException, ParseException {

        setContentPane(contentPane);


        setPreferredSize(new Dimension(1000,500));

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int sizeWidth = 800;
        int sizeHeight = 600;
        int locationX = (screenSize.width - sizeWidth) / 2;
        int locationY = (screenSize.height - sizeHeight) / 2;

        setBounds(locationX, locationY, sizeWidth, sizeHeight);


        levelFormAddStudent.setDocument(new NumberDocument());


        setModal(true);
        getRootPane().setDefaultButton(buttonOK);


        errorLabel.setVisible(false);
        errorLabel.setForeground(Color.RED);

        String [] sexArray = {"м","ж"};
        SpinnerModel spinnerSex = new SpinnerListModel(sexArray);
        sexSpinner.setModel(spinnerSex);


        comboBox1.addItem("Выберите школу...");
        workSQL = WorkSQL.getInstance();
        schools = workSQL.resultRequestSchool();
        for (School s : schools) {
            comboBox1.addItem(s);
        }

        MaskFormatter mf = new MaskFormatter("####.##.##");

        mf.install(birthdayFormAddStudent);




        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    onOK();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    onCancel();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });

        ListSelectionModel selModel = table1.getSelectionModel();

        selModel.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                String result = "";
                int[] selectedRows = table1.getSelectedRows();
                for(int i = 0; i < selectedRows.length; i++) {
                    int selIndex = selectedRows[i];
                    TableModel model = table1.getModel();
                    Object value = model.getValueAt(selIndex, 4);
                    result = result + value;
                    if(i != selectedRows.length - 1) {
                        result += ", ";
                    }
                }
                System.out.println(result);
               try { IDforRemove = Integer.parseInt(result);}
               catch (NumberFormatException ex) {
                   System.out.println(ex);
               }

            }
        });




        buttonRemoveStudent.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                    try {
                    removeStudent();
                } catch (ParseException e1) {
                    e1.printStackTrace();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                } catch (ClassNotFoundException e1) {
                    e1.printStackTrace();
                }
            }
        });


        addSchoolButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!formattedSchool.getText().equals("")) {


                try {
                    addSchool();
                } catch (ParseException e1) {
                    e1.printStackTrace();
                } catch (ClassNotFoundException e1) {
                    e1.printStackTrace();
                }
            }}
        });




        removeSchoolButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    removeSchool();
                } catch (ParseException e1) {
                    e1.printStackTrace();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                } catch (ClassNotFoundException e1) {
                    e1.printStackTrace();
                }
            }
        });

        buttonAddStudent.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent Ae) {
                try {
                    addStudent();
                } catch (ParseException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                try {
                    onCancel();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    onCancel();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }


        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        comboBox1.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {

                if (e.getStateChange() == ItemEvent.SELECTED) {

                    if (removeHardButton ==0) {
                        comboBox1.removeItemAt(0);
                        removeHardButton++;
                    }

                    try {

                            for (int i = 0; i<schools.size();i++) {
                                if (comboBox1.getSelectedIndex() == i) {

                                    table1.setModel(new MyStudentTableModel(workSQL.resultRequestStudents((School) comboBox1.getItemAt(i))));
                                    TableColumnModel tcm = table1.getColumnModel();
                                    tcm.removeColumn(table1.getColumn("ID"));
                                    return;
                                }
                            }

                        table1.setModel(new DefaultTableModel());

                    }
                    catch (ClassNotFoundException | ParseException ex) {
                        Logger.getLogger(UI.class.getName()).log(Level.SEVERE, null, ex);
                        ex.printStackTrace();
                    }
                }
             }
        });



    }





    private void addStudent() throws ParseException, ClassNotFoundException {
        if (nameFormAddStudent.getText() != null && !nameFormAddStudent.getText().equalsIgnoreCase( "") &&
                birthdayFormAddStudent.getText() != null && !birthdayFormAddStudent.getText().equals("") &&
                levelFormAddStudent.getText() != null && !levelFormAddStudent.getText().equals("")) {

            String name = nameFormAddStudent.getText();
            int level = Integer.parseInt(levelFormAddStudent.getText());
            String[] arrayBirthday =  birthdayFormAddStudent.getText().split("\\.");

            int year = Integer.parseInt(arrayBirthday[0]);
            int month = Integer.parseInt(arrayBirthday[1])-1;
            arrayBirthday[2] = arrayBirthday[2].trim();
            int day = Integer.parseInt(arrayBirthday[2]);


            boolean sex = sexSpinner.getValue().toString().equals("м");

            Student student = new Student(name, level, year, month, day, sex);
            workSQL.addStudent(student, ((School)comboBox1.getItemAt(comboBox1.getSelectedIndex())).getID());

        }
        else {errorLabel.setVisible(true);}

        table1.setModel(new MyStudentTableModel(workSQL.resultRequestStudents((School) comboBox1.getItemAt(comboBox1.getSelectedIndex()))));
        TableColumnModel tcm = table1.getColumnModel();
        tcm.removeColumn(table1.getColumn("ID"));


    }


    private void removeStudent() throws ParseException, SQLException, ClassNotFoundException {

        workSQL.removeStudent((School) comboBox1.getItemAt(comboBox1.getSelectedIndex()),IDforRemove);
        table1.setModel(new MyStudentTableModel(workSQL.resultRequestStudents((School) comboBox1.getItemAt(comboBox1.getSelectedIndex()))));
        TableColumnModel tcm = table1.getColumnModel();
        tcm.removeColumn(table1.getColumn("ID"));
    }

    private void addSchool() throws ParseException, ClassNotFoundException {


            String nameAddSchool = formattedSchool.getText();
            ArrayList<School> list = workSQL.resultRequestSchool();
            School school = new School(nameAddSchool,list.get(list.size()-1).getID()+1);
            workSQL.addSchool(school);

            schools = workSQL.resultRequestSchool();

            comboBox1.addItem(schools.get(schools.size()-1));



    }

    private void removeSchool() throws ParseException, SQLException, ClassNotFoundException {
        workSQL.removeSchool((School)comboBox1.getItemAt(comboBox1.getSelectedIndex()));

        if (workSQL.removeSchool((School)comboBox1.getItemAt(comboBox1.getSelectedIndex())).size() == 0) {
            comboBox1.removeItemAt(comboBox1.getSelectedIndex());
        }
        else errorLabel.setVisible(true);
    }



    private void onOK() throws SQLException {
        // add your code here
        workSQL.closeConnection();
        dispose();
    }

    private void onCancel() throws SQLException {
        // add your code here if necessary
        workSQL.closeConnection();
        dispose();
    }

    public static void main(String[] args) throws SQLException, ClassNotFoundException, ParseException {
        UI dialog = new UI();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }




}
