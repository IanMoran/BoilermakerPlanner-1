package project.app.boilermakerplanner;

import java.util.Calendar;

/**
 * Created by tannermcrae on 11/30/13.
 */


public class Task {

	private int id;
    private Calendar cal;
    private String description;
    private boolean recurs;
    private int recurIntervalDays;

    public Task(Calendar cal, String description) {
        this.cal = cal;
        this.description = description;
        this.recurs = false;
        this.recurIntervalDays = 0;
    }

    public Task(Calendar cal, String description, int recurIntervalDays) {
        this.cal = cal;
        this.description = description;
        this.recurs = true;
        this.recurIntervalDays = recurIntervalDays;
    }

    // Accessor Methods
	
	public int getID(){
		return id;
	}
	
	public void setID(int id){
		this.id = id;
	}

    public Calendar getCal() {
        return cal;
    }

    public void setCal(Calendar cal) {
        this.cal = cal;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // No setter method. The variable is set based on the recurrence interval
    public boolean getRecurs() {
        return recurs;
    }

    public int getRecurIntervalDays() {
        return recurIntervalDays;
    }

    public void setRecurIntervalDays(int recurIntervalDays) {
        if (recurIntervalDays == 0)
            this.recurs = false;
        else
            this.recurs = true;
        this.recurIntervalDays = recurIntervalDays;
    }


}
