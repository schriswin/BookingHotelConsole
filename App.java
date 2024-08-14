package com.main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Scanner;

public class App 
{
	private static final String URL="jdbc:mysql://127.0.0.1:3306/hoteldb";
	private static final String USERNAME="root";
	private static final String PASSWORD="SAMchris1*";
	
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        // Load Driver
//        try{
//        	Class.forName("com.mysql.jdbc.Driver");
//        	System.out.println("Driver loaded successfully");
//        }catch(ClassNotFoundException e){
//        	System.out.println("Failed to load the driver "+e.getMessage());
//        }
        
        // Connect to DB
        try {
			Connection connection = DriverManager.getConnection(URL,USERNAME,PASSWORD);
			System.out.println("Connected to DB");
			System.out.println();
			/*
			    MENU:
			 1. Book a room            - insert data of customer - save it into DB
			 2. View the room          - Show details of that customer who is staying in the room 
			 3. Show all bookings      - Show all customers data
			 4. Update booking details - Update data
			 5. Delete data            - Delete customer data
			 6. Exit
			 */
			boolean flag = true;
			while(flag){
				// Let's show these options to the user  
				
				System.out.println("Please select from below: ");
				System.out.println("1. Book a room");
				System.out.println("2. View the room");
				System.out.println("3. Show all bookings");
				System.out.println("4. Update booking details");
				System.out.println("5. Delete data");
				System.out.println("6. Exit");
				
				Scanner scanner = new Scanner(System.in);
				System.out.println("ENTER OPTION: ");
				int option = scanner.nextInt();
				System.out.println("Option selected by user: "+option);
				System.out.println();
				
				
				
				switch(option){
				case 1:
					bookARoom(connection,scanner);
					break;
				case 2:
					viewBookedRoom(connection,scanner);
					break;
				case 3:
					showAllBookings(connection);
					break;
				case 4:
					updateDetails(connection,scanner);
					break;
				case 5:
					deleteBooking(connection,scanner);
					break;
				case 6:
					System.out.println("Thank you! Program is terminated");
					flag=false;
				    break;
				default:
					break;
				}				
				
				connection.close();
				System.out.println("Connection is closed..");
			}
			
			} catch (SQLException e) {
				System.out.println("Failed to connect to DB "+e.getMessage());
			}
	    }
						
    
    // Method to book a room
    private static void bookARoom(Connection connection,Scanner scanner){
    	// Columns - id,name,room_no,phone,booking_time
    	String sql = "INSERT INTO booking_table(name,room_no,phone)VALUES(?,?,?)";
    	
    	try {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			
			System.out.println("Enter name: ");
			String name = scanner.next();
			System.out.println("Enter Room no: ");
			int roomNo = scanner.nextInt();
			System.out.println("Enter phone no: ");
			String phoneNo = scanner.next();
			
			if(name!=null || phoneNo!=null || roomNo!=0){
				
				preparedStatement.setString(1,name);
				preparedStatement.setInt(2,roomNo);
				preparedStatement.setString(3,phoneNo);
				int rowsAffected = preparedStatement.executeUpdate();
				
				if(rowsAffected > 0){
					System.out.println("Booked Successfully..!");
					System.out.println();
				}
				else{
					System.out.println("Failed to book...Please try again...");
				}
			}
			
			else{
				System.out.println("--Please enter details");
			}
			
			
			preparedStatement.close();
			
		} catch (SQLException e) {
			System.out.println("Failed to book a room"+e.getMessage());
		}
    }

    //Method to view the room
    private static void viewBookedRoom(Connection connection, Scanner scanner){
    	
    	String sql = "SELECT * FROM booking_table WHERE id=? and name=?";
    	
    	try {
    		
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			
    		System.out.print("ENTER ID: ");
    		int userInputId = scanner.nextInt();
    		System.out.print("ENTER NAME: ");
    		String userInputName = scanner.next();
    		System.out.println();
    		
    		if(userInputId!=0 && userInputName!=null){
    			
    			preparedStatement.setInt(1,userInputId);
    			preparedStatement.setString(2,userInputName);
    			ResultSet resultSet = preparedStatement.executeQuery();
    			
    			System.out.println("+----------+--------------+---------------+------------------+----------------------------+");
    			System.out.println("|    ID    |     NAME     |    ROOM NO    |     PHONE NO     |            TIME            |");			
    			System.out.println("+----------+--------------+---------------+------------------+----------------------------+");
    			
    			while(resultSet.next()){
    				
    				int id = resultSet.getInt("id");
    				String name = resultSet.getString("name");
    				int roomNo = resultSet.getInt("room_no");
    				String phoneNo = resultSet.getString("phone");
    				Timestamp timestamp = resultSet.getTimestamp("booking_time");
    				
    				System.out.println("|    "+id+"     |     "+name+"     |    "+roomNo+"         |    "+phoneNo+"    |    "+timestamp+"   |");
    			}
    			System.out.println("+----------+--------------+---------------+------------------+----------------------------+");
    			resultSet.close();
    			preparedStatement.close();
    		}
    		else{
    			System.out.println("Please enter the ID and Name.");
    		}
			
		} catch (SQLException e) {
			System.out.println("Failed to get single data from the table."+e.getMessage());
		}
    	
    }

    //Method to show all the bookings
    private static void showAllBookings(Connection connection){
    	
    	String sql = "SELECT * FROM booking_table";
    	
    	try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sql);
			
			System.out.println("+----------+--------------+---------------+------------------+----------------------------+");
			System.out.println("|    ID    |     NAME     |    ROOM NO    |     PHONE NO     |            TIME            |");			
			System.out.println("+----------+--------------+---------------+------------------+----------------------------+");
			
			while(resultSet.next()){
				
				int id = resultSet.getInt("id");
				String name = resultSet.getString("name");
				int roomNo = resultSet.getInt("room_no");
				String phoneNo = resultSet.getString("phone");
				Timestamp timestamp = resultSet.getTimestamp("booking_time");
				
				System.out.println("|    "+id+"     |     "+name+"     |    "+roomNo+"         |    "+phoneNo+"    |    "+timestamp+"   |");
			}
			System.out.println("+----------+--------------+---------------+------------------+----------------------------+");
			System.out.println();
			resultSet.close();
			statement.close();
			
		} catch (SQLException e) {
			System.out.println("Failed to show all bookings"+e.getMessage());
		}
    	
    }

    //Method to update booking details
    private static void updateDetails(Connection connection, Scanner scanner){
    	String sql = "UPDATE booking_table SET phone=? WHERE id=?";
    	try {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			
			System.out.println("ENTER ID: ");
			int inputID = scanner.nextInt();
			if(!bookingIDExist(connection,inputID)){
				System.out.println("Booking not available with this ID");
				return;
			}
			System.out.println("ENTER NEW PHONE NUMBER: ");
			String inputPhoneNo = scanner.next();
			
			if(inputID!=0 && inputPhoneNo!=null){
				
				preparedStatement.setString(1,inputPhoneNo);
				preparedStatement.setInt(2,inputID);
				int rowsAffected = preparedStatement.executeUpdate();
				if(rowsAffected>0){
					System.out.println("Updated the details");
				}
				else{
					System.out.println("Not updated");
				}
				preparedStatement.close();
			}
			else{
				System.out.println("Please try again");
			}
						
		} catch (SQLException e) {
			System.out.println("Failed to update the booking details"+e.getMessage());
		}
    }

    //Method to delete booking details
    private static void deleteBooking(Connection connection, Scanner scanner){
    	String sql = "DELETE FROM booking_table WHERE id=?";
    	
    	try {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);

			System.out.println("ENTER THE ID TO BE DELETED: ");
			int id = scanner.nextInt();
			
			if(id!=0){
				
				if(!bookingIDExist(connection,id)){
					System.out.println("Booking not available with this ID");
					return;
				}
				
				preparedStatement.setInt(1,id);
				int rowsAffected = preparedStatement.executeUpdate();
				if(rowsAffected>0){
					System.out.println("Deleted the details");
				}
				else{
					System.out.println("Not deleted the details");
				}
				preparedStatement.close();
			}
			else{
				System.out.println("Enter valid ID");
			}			
			
		} catch (SQLException e) {
			System.out.println("Failed to delete"+e.getMessage());
		}
    }
    
    //Method to check whether the booking ID already exists
    private static boolean bookingIDExist(Connection connection, int id){
    	String sql = "SELECT * FROM booking_table WHERE id=?";
    	
    	try{
    	 PreparedStatement preparedStatement = connection.prepareStatement(sql);
    	 preparedStatement.setInt(1,id);
    	 ResultSet resultSet = preparedStatement.executeQuery();
    	 if(resultSet.next()){
    		 return true;
    	 }
    	 resultSet.close();
    	 preparedStatement.close();
    	}catch (SQLException e) {
			System.out.println("Message: "+e.getMessage());
		}
    	return false;
    }
}
