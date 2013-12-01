package project.app.boilermakerplanner;

import java.util.ArrayList;
import java.util.Calendar;
import java.sql.*;
import java.io.*;


/**
 * Created by tannermcrae on 11/30/13.
 * Updated by IanMoran on 11/30/13:
 *	 Added initialize, which initializes connection to the database, and must be called first
 *	 Added initializeDatabase, which is called from constructor, that creates the database if it does not exist
 *	 Added getTasks code to fulfill actual purpose
 *	 Added addTask to accept a task into the table
 */
public class InfoCenter {

	Connection dbConnect;
	String dbName = "PurdueConnectData";
	String createTableStatement = "CREATE TABLE tasks (ID int NOT NULL AUTO_INCREMENT, Year int, Month int, Date int, Hour int, Minute int, Description varchar(255), Recursion int, RecursionDays int)";
	
	//Constructor, initializes Connection object, and calls InitializeDatabase
	public static void initialize()
	{
		Properties props = new Properties();
		FileInputStream in = new fileInputStream("database.properties");
		props.load(in);
		in.close();
		String drivers = props.getProperty("jdbc.drivers");
		if (drivers != null)
			System.setProperty("jdbc.drivers", drivers);
		String url = props.getProperty("jdbc.url");
		String username = props.getProperty("jdbc.username");
		String password = props.getProperty("jdbc.password");
		
		System.out.println("url="+url+" user="+username+" password="+password);

		dbConnect = DriverManager.getConnection( url, username, password);
		
		initializeDatabase();
	}
	
	//Ensures the Database exists, creates it (and the task table) if it doesn't,
	//and sets it to be the primary database just in case
	private static void initializeDatabase()
	{
		ResultSet resultSet = dbConnect.getMetaData().getCatalogs();
		Statment statement = dbConnect.createStatement();
		
		if (resultSet.next())
		{
			statement.execute("USE " + resultSet.getString(1));
			statement.close();
			resultSet.close();
			return; //A database exists already
		}
		resultSet.close();
		//No database exists yet, create it.
		
		statement.execute("CREATE DATABASE " + dbName);
		statement.execute("USE " + dbName);
		statement.execute(createTableStatement);
		statement.close();
		return;
	}
	
	//Returns an ArrayList of type Task containing all tasks in the database/table
    public ArrayList<Task> getTasks(Calendar cal) {
        ArrayList<Task> tasks = new ArrayList<Task>();
		Statement statement = dbConnect.createStatement();
		
		ResultSet results = statement.executeQuery("SELECT * FROM tasks");

		while(results.next())
		{
			int id = result.getInt(1);
			int year = result.getInt(2);
			int month = result.getInt(3);
			int day = result.getInt(4);
			int hour = result.getInt(5);
			int minute = result.getInt(6);
			String descrip = result.getString(7);
			boolean recurs = result.getBoolean(8);
			int recursDay = result.getInt(9);
			
			Calendar cal = new Calendar();
			cal.set(year, month, day, hour, minute);
			
			if(recurs)
			{
				tasks.add(new Task(cal, descrip, recursDay));
			}
			else
			{
				tasks.add(new Task(cal, descrip));
			}
		}
		statement.close();
		results.close();
        return tasks;
    }
	
	//Adds the supplied task to the database/table
	public static void addTask(Task taskToAdd)
	{
		Statement statement = dbConnect.createStatement();
		
		Calendar cal = taskToAdd.getCal();
		int year = cal.get(YEAR);
		int month = cal.get(MONTH);
		int day = cal.get(DAY_OF_MONTH);
		int hour = cal.get(HOUR_OF_DAY);
		int minute = cal.get(MINUTE);
		
		String descrip = taskToAdd.getDescription();
		boolean recur = taskToAdd.getRecurs();
		int recurI;
		if(recur) recurI = 1;
		else recurI = 0;
		int recursDays = taskToAdd.getRecurIntervalDays();
		
		statement.executeUpdate("INSERT INTO tasks (Year, Month, Date, Hour, Minute, Description, Recursion, RecursionDays) VALUES(" +year+", "+month+", "+day+", "+hour+", "+minute+", '"+descrip+"', "+recurI+", "+recursDays+")");
		statement.close();
		return;
}
