import java.io.*;
import java.util.Scanner;
import java.sql.*;

public class ProjectMenu 
{
	
	public static void main(String[] args) 
	{
	}

	
}
 class MenuBeta
{
	 static Boolean wActivity = false;
	 static Boolean dActivity = false;
	MenuBeta()
	{
	}
	
	public static void display_mainMenu()
	{
		System.out.println("\nMain menu");
		System.out.println("1. Account summary");
		System.out.println("2. Withdrawal");
		System.out.println("3. Deposit");
		System.out.println("4. Current session log");
		System.out.println("5. Log off");
		System.out.println("What would you like to do? Please select an option: ");
	}
	
	public static void display_accountSummary(Statement stmt, ResultSet result) throws SQLException
	{
		//Show user name, ID, PIN, Current session log activity, current balance and any other important information about the user's account
		System.out.println("Coming soon!");
		result.next();
		String ID = "ID", FNAME = "FIRST NAME" , MNAME = "MIDDLE NAME" , LNAME = "LAST NAME" , BALANCE = "BALANCE" , PIN = "PIN";
		System.out.printf("%-20s%-20s%-20s%-20s%-20s%-4s", ID, FNAME, MNAME, LNAME, BALANCE, PIN); 
		System.out.println();
		System.out.printf("%-20s%-20s%-20s%-20s%-20.2f%-4d", result.getString("ID"), result.getString("FirstName"), result.getString("MiddleName"),
				result.getString("LastName"), result.getDouble("CurrentBalance"), result.getInt("PIN") );
	}	
	
	public static void currentSession(Statement stmt, PrintWriter session) throws IOException
	{
		if(wActivity == false && dActivity == false)
		{
			System.out.println("No activity");
		}
		
		else 
		{
			session.close();
			File file = new File ("session.txt"); //read the file
			Scanner s1 = new Scanner(file);
			
			while(s1.hasNextLine())
			{
				System.out.println(s1.nextLine());
				System.out.println("_______________");
				System.out.println(s1.nextDouble());
				s1.nextLine();//consume
				System.out.println("\n");
			}
			s1.close();
		}
	}
	
	public static void withdrawal(Statement stmt, double withdrawAmnt, double balance, String ID, PrintWriter session)throws SQLException 
	{  	//Code to execute withdrawal from account
	
		double newBalance = balance - withdrawAmnt;
		  
		// Create a string utilizing withdraw.
		String sqlStatement = "UPDATE bank SET CurrentBalance = " + "'" + Double.toString(newBalance) + "'" + "WHERE id = " + "'" + ID + "'";
		
		// Send the statement to the DBMS (give the database an SQL command).
		stmt.executeUpdate(sqlStatement);
		  
		session.println("Funds withdrawn");
		session.println(withdrawAmnt);
		wActivity = true;
		session.close();
		
		System.out.println("Balance updated! You have successfully withdrawed " + withdrawAmnt + " dollars!");
	}
	
	
	public static void deposit(Statement stmt, double depoAmnt, double balance, String ID, PrintWriter session) throws SQLException
	{ //Code to execute deposit to account
		
		//System.out.println("Coming soon!");
		
		double newBalance = balance + depoAmnt;
		
		  // Create a string utilizing deposit
		  String sqlStatement = "UPDATE bank " + "SET CurrentBalance = " + "'" + Double.toString(newBalance) + "'" + "WHERE ID = " + "'" + ID + "'";
		
		  // Send the statement to the DBMS (give the database an SQL command).
		  stmt.executeUpdate(sqlStatement);
		  
		  session.println("Funds deposited");
		  session.println(depoAmnt);
		  dActivity = true;
		  session.close();
		  System.out.println("Balance updated! You have successfully deposited " + depoAmnt + " dollars!");
	}

	public static void registration (Statement stmt, String ID, String firstName, String middleName, String lastName, double balance, int PIN) throws SQLException
	{
		String sqlStatement = "INSERT INTO bank VALUES ('" + ID + "'" + "," + "'" + firstName + "'," +  "'" + middleName + "'," + 
	"'" + lastName + "'," + Double.toString(balance) + "," + Integer.toString(PIN) + ")";
		
		stmt.execute(sqlStatement);
		
		try 
		{ //Pause the program a little bit
		    Thread.sleep(1000);                 //1000 milliseconds is one second.
		}
		catch(InterruptedException ex) 
		{
		    Thread.currentThread().interrupt();
		}
		
		System.out.println("Registration complete!");
	}
	
	public static void loginProcessAndMainFunction(Statement stmt)throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, IOException
	{
		//____Log in variables__________________________________________
		String userId, userName, sqlCommand2;
		int answer2 = 0 , userPIN;
		Scanner in2 = new Scanner(System.in);
		ResultSet result, result3;
		//______________________________________________________________
		
		System.out.println("Now in login menu...Please wait.");
		try 
		{ //Pause the program a little bit
		    Thread.sleep(1000);                 //1000 milliseconds is one second.
		}
		catch(InterruptedException ex) 
		{
		    Thread.currentThread().interrupt();
		}
		System.out.print("Please input your ID: ");
		userId = in2.nextLine();
		System.out.print("Now please input your PIN number: ");
		userPIN = in2.nextInt();
		in2.nextLine(); //consume key buffer
		sqlCommand2 = "SELECT ID, PIN FROM bank";
		result3 = stmt.executeQuery(sqlCommand2);
		while(result3.next())
		{
			if (userId.equals(result3.getString("ID")) && userPIN == result3.getInt("PIN"))
			{
				System.out.println("match");
				break;
			}
			else if(result3.isLast())
			{
				String regAnswer;
				System.out.println("Sorry. It seems your information is not found in our system.");
				System.out.println("In case you don't have an account, would you like to register?: ");
				regAnswer = in2.nextLine();
				if (regAnswer.equals("yes") || regAnswer.equals("YES") || regAnswer.equals("Yes"))
				{
					registrationProcess(stmt); //escort user to register
					break;
				}
				else if (regAnswer.equals("no") || regAnswer.equals("No") || regAnswer.equals("NO"))
				{ //let user try again
					System.out.print("Please input your ID: ");
					userId = in2.nextLine();
					System.out.print("Now please input your PIN number: ");
					userPIN = in2.nextInt();
					in2.nextLine(); //consume key buffer
					result3.beforeFirst(); //move the cursor back to the initial row
				}
			}
			
			}
		
		  // Create a string with a SELECT statement.
		  String sqlNStatement = "SELECT FirstName FROM bank WHERE id = " + "'" +  userId + "'";
		  // Send the statement to the DBMS (give the database an SQL command).
		 result = stmt.executeQuery(sqlNStatement);
		 result.next();
		 userName = result.getString("FirstName");
		  System.out.println("Welcome back " + userName + "!");
		  while (answer2 != 5) //loop until user decides to log out
		  {
			  //====While loop variables===================
			  String sqlCommand;
			  double withdrwAmnt, depoAmnt, curBalance;
			  FileWriter fSession = new FileWriter("session.txt", true); //used for keeping track of current activity
			  PrintWriter session = new PrintWriter (fSession); //support for above
			  //===========================================
			  
			  if (answer2 > 5 || answer2 < 1)
			  {
				  System.out.println("Out of bounds selection! Please adjust your input.");
			  }
			  
			  display_mainMenu();
			  answer2 = in2.nextInt(); //let user select an option
			  
			  if (answer2 == 1) //account summary selected 
			  {
				  System.out.println("You've selected account summary.");
				  sqlCommand = "SELECT * FROM bank WHERE id = " + "'" + userId + "'";
				  result = stmt.executeQuery(sqlCommand);
				  display_accountSummary(stmt, result);
			  }
			  
			  else if (answer2 == 2) //withdrawal selected
			  {
				  System.out.println("You've selected withdrawal.");
				  sqlCommand = "SELECT CurrentBalance FROM bank WHERE id = " + "'" + userId + "'";
				  result = stmt.executeQuery(sqlCommand);
				  result.next(); //place cursor at first row (only row)
				  curBalance = result.getDouble(1);
				  System.out.print("Please enter the amount you wish to withdraw: ");
				  withdrwAmnt = in2.nextDouble();
				  in2.nextLine(); //consume key buffer
				  if (withdrwAmnt > curBalance) //check if you have enough funds
				  {
					  System.out.println("I'm sorry. Unfortunately you currently don't have enough funds to make this withdrawal.");
				  }
				  else //you have enough funds, initiate withdrawal
				  {
					  withdrawal(stmt, withdrwAmnt, curBalance, userId, session);
				  }  
			  }
			  
			  else if (answer2 == 3) //deposit selected
			  {
				 System.out.println("You've selected deposit."); 
				  sqlCommand = "SELECT CurrentBalance FROM bank WHERE id = " + "'" + userId + "'";
				  result = stmt.executeQuery(sqlCommand);
				  result.next(); //place cursor at first row (only row)
				  curBalance = result.getDouble(1);
				  System.out.print("Please enter the amount you wish to deposit: ");
				  depoAmnt = in2.nextDouble();
				  in2.nextLine(); //consume key buffer
				  deposit(stmt, depoAmnt, curBalance, userId, session);
			  }
			  
			  else if (answer2 == 4)
			  {
				  System.out.println("You've selected current session log");
				  currentSession(stmt, session);  
			  }
			  
			  //else either 4 or invalid input was selected (4 to log off)
		  } 
	}
	
	public static void registrationProcess(Statement stmt) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, IOException
	{
		//~~~~~Variables~~~~~~~~~~~~~~~~~~~~
		int userPIN;
		double userBalance;
		String ID, fName, mName, lName, SQLCommand;
		Scanner keyboard = new Scanner(System.in);
		ResultSet result2;
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		  
		  //Give SQL command (used for checking if ID the user requests is already in use)
		  SQLCommand = "SELECT id FROM bank";
		  result2 = stmt.executeQuery(SQLCommand);
		  result2.next(); //initialize to first row
		  
		    System.out.println("Now in registration process...");
			System.out.print("Please create an ID (Up to 20 characters only): ");
			ID = keyboard.nextLine();
			while(result2.next()) //loop through every ID to check if the ID user inputed is already in use
			{
				 if(ID.equals(result2.getString("ID")))//check if user ID already exists
				{
					while(ID.equals(result2.getString("ID"))) 
					{
						System.out.println("I'm sorry. That bank ID has already been taken. Please enter another ID...");
						ID = keyboard.nextLine();
						//keyboard.nextLine(); //consume key buffer
						System.out.println("Input received. Now verifying...");
						try 
						{ //Pause the program a little bit
						    Thread.sleep(1000);                 //1000 milliseconds is one second.
						}
						catch(InterruptedException ex) 
						{
						    Thread.currentThread().interrupt();
						}
						result2.first(); //reset cursor to first row in case user decides to input an ID above the ID that was matched 
					}	
				}
			}
			System.out.println("Ok!...Now please enter your first name (Up to 20 characters): ");
			fName = keyboard.nextLine();
			System.out.println("Ok!...Now please enter your middle name (Up to 20 characters. If you");
			System.out.println("don't have a middle name, please enter NA): ");
			mName = keyboard.nextLine();
			System.out.println("Ok!...Now please enter your last name (Up to 20 characters): ");
			lName = keyboard.nextLine();
			System.out.println("Ok!...Now we would like you to enter your initial balance (EX: 1000.00): ");
			userBalance = keyboard.nextDouble();
			System.out.println("Ok!...Finally, please enter your PIN number (four digits only): ");
			userPIN = keyboard.nextInt();
			keyboard.nextLine(); //consume key buffer
			System.out.println("Ok! Input received. Just a moment...");
			
			try 
			{ //Pause the program a little bit
			    Thread.sleep(1000);                 //1000 milliseconds is one second.
			}
			catch(InterruptedException ex) 
			{
			    Thread.currentThread().interrupt();
			}
		 
		//Call registration function
		registration(stmt, ID, fName, mName, lName, userBalance, userPIN);
		
		loginProcessAndMainFunction(stmt);	
	}
	
	public void transferFunds()
	{}
	public void removeUser()
	{}
	
}