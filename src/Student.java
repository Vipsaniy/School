import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Student {
    private String name;
    private int level;
    private Calendar birthday;
    private boolean sex;
    private int ID;

    public Student(String name, int level, int year, int month, int day, boolean sex) {
        this.name = name;
        this.level = level;
        this.birthday = Calendar.getInstance();
        this.birthday.set(year,month,day);
        this.sex = sex;
    }



    public Student(String name, int level, Date date , boolean sex) {
        this.name = name;
        this.level = level;
        this.birthday = Calendar.getInstance();
        this.birthday.setTime(date);
        this.sex = sex;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public Calendar getBirthday() {
        return birthday;
    }

    public boolean isSex() {
        return sex;
    }
}
