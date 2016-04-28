package util;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Pelin on 4/28/2016.
 */

@DatabaseTable(tableName = "user")
public class User {

    @DatabaseField(id = true)
    private String name;
    @DatabaseField
    private int password;

    @DatabaseField
    private long date;

    User() {
        // needed by ormlite
    }

    public User(long millis, String name, int password) {
        this.setDate(millis);
        this.name = name;
        this.password = password;
        /*this.string = (millis % 1000) + "ms";
        this.millis = millis;
        this.even = ((millis % 2) == 0);*/
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }
}