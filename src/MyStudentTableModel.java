import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

public class MyStudentTableModel implements TableModel {

    ArrayList<Student> students;
    private Set<TableModelListener> listeners = new HashSet<TableModelListener>();



    public  MyStudentTableModel(ArrayList<Student> students) {
        if (students!= null) {
            this.students = students;
        }
    }

    @Override
    public int getRowCount() {

        return students.size();
    }

    @Override
    public int getColumnCount() {
        return 5;
    }

    @Override
    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return "Имя";
            case 1:
                return "Группа";
            case 2:
                return "Дата";
            case 3:
                return "Пол";
            case 4:
                return "ID";
        }
        return "";
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return String.class;
            case 1:
                return Integer.class;
            case 2:
                return Calendar.class;
            case 3:
                return String.class;
            case 4:
                return Integer.class;
        }
                return String.class;

    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Student student = students.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return student.getName();
            case 1:
                return student.getLevel();
            case 2:
                return new java.sql.Date(student.getBirthday().getTimeInMillis()).toString();
            case 3:
                return student.isSex()? "м":"ж";
            case 4:
                return student.getID();
        }
        return "";
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

    }

    @Override
    public void addTableModelListener(TableModelListener l) {
        listeners.add(l);
    }

    @Override
    public void removeTableModelListener(TableModelListener l) {
        listeners.remove(l);
    }
}
