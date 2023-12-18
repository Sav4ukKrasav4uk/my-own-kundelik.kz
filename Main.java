import java.util.*;
import java.io.*;

class User{
	String name;
	String pass;

	public User(String name, String pass){
		this.name = name;
		this.pass = pass;
	}
}

class Student{
	String name;
	String id;
	int score;
	char grade;

	public Student(String name, String id, int score, char grade){
		this.name = name;
		this.id = id;
		this.score = score;
		this.grade = grade;
	}
}

public class Main{
	public static Scanner sc = new Scanner(System.in);
	public static User[] users = new User[100];
	public static int user_counter = 0;
	public static int up = -1; // user position

	public static Student[][] students = new Student[100][1000];
	public static int[] student_counter = new int[100];

	public static double[] average = new double[100];
	public static double[] highest = new double[100];
	public static double[] lowest = new double[100];
	public static boolean[] was_incalc = new boolean[100];

	public static void main(String[] args) throws Exception{
		System.out.print("Note: use \"BACK\" to get back and \"EXIT\" to exit.\n\n");

		if (!log_menu()){
			exit();
		}
	}

	public static boolean returner(String s){
		return s.equals("BACK") ? true : false;
	}
	public static boolean exiter(String s){
		return s.equals("EXIT") ? true : false;
	}

	public static boolean log_menu() throws Exception{
		while (true){
			System.out.println("Log in (1) / Sign up (2)");
			System.out.print("Enter your choice: ");
			String choice_log = sc.next();

			if (returner(choice_log)){
				return false;
			}
			if (exiter(choice_log)){
				exit();
			}

			if (choice_log.charAt(0) == '1' && choice_log.length() == 1){
				if (user_counter == 0){
					System.out.println("Please, register in first.");
					continue;
				}
				if (login()){
					if (!main_menu()){
						continue;
					}
				}
			}
			else if (choice_log.charAt(0) == '2' && choice_log.length() == 1){
				register();
			}
			else {
				System.out.println("Please, choose \"1\" or \"2\".");
			}
		}
	}

	public static boolean login(){
		while (true){
			System.out.print("Enter username: ");
			String username = sc.next();

			if (returner(username)){
				return false;
			}
			if (exiter(username)){
				exit();
			}

			int check = 0, position = -1;
			for (int i = 0; i < user_counter; i++){
				if (username.equals(users[i].name)){
					check = 1;
					position = i;
					break;
				}
			}
			if (check == 0){
				System.out.println("This username doesn't exist!");
				continue;
			}
			
			while (true){
				System.out.print("Enter password: ");
				String password = sc.next();

				if (returner(password)){
					return false;
				}
				if (exiter(password)){
					exit();
				}

				if (password.equals(users[position].pass)){
					System.out.println("You are successfully entered!");
					up = position;
					return true;
				}
				else {
					System.out.println("Incorrect password.");
				}
			}
		}
	}

	public static void register(){
		while (true){
			System.out.print("Enter username: ");
			String username = sc.next();
			
			if (returner(username)){
				return;
			}
			if (exiter(username)){
				exit();
			}

			int once_user = 0, position = -1;
			for (int i = 0; i < user_counter; i++){
				if (username.equals(users[i].name)){
					once_user = 1;
					position = i;
					break;
				}
			}
			if (once_user == 1){
				System.out.println("This username is already engaged!");
				continue;
			}

			System.out.print("Enter password: ");
			String password = sc.next();

			if (returner(password)){
				return;
			}
			if (exiter(password)){
				exit();
			}
			
			users[user_counter++] = new User(username, password);

			System.out.println("You are successfully registered!");
			return;
		}
	}
	
	public static boolean main_menu() throws Exception{
		while (true){
			System.out.println("Main menu [" + users[up].name + "]");
			System.out.println("1. Add Student");
			System.out.println("2. Manage Records");
			System.out.println("3. Calculate Grades");
			System.out.println("4. View Statistics");
			System.out.println("5. Generate Reports");
			System.out.println("6. Delete Student");
			System.out.println("7. Save records to a file");
			System.out.println("8. Log out");
			System.out.println("9. Exit");

			System.out.print("Enter your choice: ");
			String choice_menu = sc.next();

			if (returner(choice_menu)){
				up = -1;
				return false;
			}
			if (exiter(choice_menu)){
				exit();
			}

			if (choice_menu.length() != 1 || choice_menu.charAt(0) < '1' || choice_menu.charAt(0) > '9'){
				System.out.println("Please, choose 1-9.");
				continue;
			}

			int choice = Integer.parseInt(choice_menu);
			switch (choice){
				case 1: add();
					break;
				case 2: manage();
					break;
				case 3: calculate();
					break;
				case 4: statistics();
					break;
				case 5: reports();
					break;
				case 6: delete();
					break;
				case 7: save();
					break;
				case 8: up = -1;
					return false;
				case 9: exit();
					break;
			}
		}
	}

	public static void add(){
		while (true){
			System.out.print("Enter student name: ");
			String name = sc.next();

			if (returner(name)){
				return;
			}
			if (exiter(name)){
				exit();
			}

			if (!checkName(name)){
				continue;
			}

			while (true){
				System.out.print("Enter student ID: ");
				String id = sc.next();

				if (returner(id)){
					return;
				}
				if (exiter(id)){
					exit();
				}

				if (!checkID(id)){
					continue;
				}

				int once_id = 0, position = -1;
				for (int i = 0; i < student_counter[up]; i++){
					if (id.equals(students[up][i].id)){
						once_id = 1;
						position = i;
						break;
					}
				}
				if (once_id == 1){
					System.out.println("Please, enter another id cause this id is also used by student " + students[up][position].name + ".");
					continue;
				}

				while (true){
					System.out.print("Enter test score: ");
					String score = sc.next();

					if (returner(score)){
						return;
					}
					if (exiter(score)){
						exit();
					}

					if (!checkScore(score)){
						continue;
					}

					int test_score = Integer.parseInt(score);
					if (test_score >= 0 && test_score <= 100){
						char grade = calculateGrade(test_score);
						students[up][student_counter[up]++] = new Student(name, id, test_score, grade);

						System.out.println("Student added successfully.");
						was_incalc[up] = false;
						return;
					}
					else {
						System.out.println("The student's test score must be between 0 to 100.");
						continue;
					}
				}
			}
		}
	}

	public static void manage(){
		if (student_counter[up] == 0){
			System.out.println("Please, add at least one student to manage records.");
			return;
		}

		while (true){
			System.out.print("Enter student ID to edit: ");
			String id = sc.next();

			if (returner(id)){
				return;
			}
			if (exiter(id)){
				exit();
			}

			if (!checkID(id)){
				continue;
			}

			int position = existID(id);
			if (position == -1){
				continue;
			}
			
			while (true){
				System.out.print("Enter new test score for student " + students[up][position].name + ": ");
				String new_score = sc.next();

				if (returner(new_score)){
					return;
				}
				if (exiter(new_score)){
					exit();
				}

				if (!checkScore(new_score)){
					continue;
				}

				int test_score = Integer.parseInt(new_score);
				if (test_score >= 0 && test_score <= 100){
					students[up][position].score = test_score;
					students[up][position].grade = calculateGrade(test_score);

					System.out.println("Student's record updated successfully.");
					was_incalc[up] = false;
					return;
				}
				else {
					System.out.println("The student's test score must be between 0 to 100.");
					continue;
				}
			}
		}
	}

	public static void calculate(){
		if (student_counter[up] == 0){
			System.out.println("Please, add at least one student to calculate grades.");
			return;
		}

		was_incalc[up] = true;

		int min = 100, max = 0, sum = 0;
		for (int i = 0; i < student_counter[up]; i++){
			if (students[up][i].score >= max){
				max = students[up][i].score;
			}
			if (students[up][i].score <= min){
				min = students[up][i].score;
			}

			sum += students[up][i].score;
		}

		average[up] = (double)sum / (double)student_counter[up];
		highest[up] = (double)max;
		lowest[up] = (double)min;

		System.out.print("Grades calculated.\nGrade details added.\n");
	}

	public static void statistics(){
		if (student_counter[up] == 0){
			System.out.println("Please, add at least one student to view statistics.");
			return;
		}

		if (!was_incalc[up]){
			System.out.println("You have made changes.");
			System.out.println("Please, calculate grades to view relevant statistics.");
			return;
		}

		int mid = (int)(average[up] * 10.0);
		average[up] = (double)mid / 10.0;


		System.out.println("Statistics: ");
		System.out.println("Total Students: " + student_counter[up]);
		System.out.println("Average Score: " + average[up]);
		System.out.println("Highest Score: " + highest[up]);
		System.out.println("Lowest Score: " + lowest[up]);
	}

	public static void reports(){
		if (student_counter[up] == 0){
			System.out.println("Please, add at least one student to generate reports.");
			return;
		}

		while (true){
			System.out.print("Enter student ID to generate a report: ");
			String id = sc.next();

			if (returner(id)){
				return;
			}
			if (exiter(id)){
				exit();
			}

			if (!checkID(id)){
				continue;
			}

			int position = existID(id);
			if (position == -1){
				continue;
			}

			System.out.println("Generating report for student with ID: " + students[up][position].id);
			System.out.println("Student Report");
			System.out.println("Name: " + students[up][position].name);
			System.out.println("Student ID: " + students[up][position].id);
			System.out.println("Test Score: " + (double)students[up][position].score);
			System.out.println("Final Grade: " + students[up][position].grade);

			return;
		}
	}

	public static void delete(){
		if (student_counter[up] == 0){
			System.out.println("Please, add at least one student to delete student.");
			return;
		}

		while (true){
			System.out.print("Enter student ID to delete: ");
			String id = sc.next();

			if (returner(id)){
				return;
			}
			if (exiter(id)){
				exit();
			}

			if (!checkID(id)){
				continue;
			}

			int position = existID(id);
			if (position == -1){
				continue;
			}

			System.out.println("Student with ID " + students[up][position].id + " deleted successfully.");

			students[up][position].name = "";
			students[up][position].id = "";
			students[up][position].score = 0;
			students[up][position].grade = ' ';

			for (int i = position + 1; i < student_counter[up]; i++){
				students[up][i - 1] = students[up][i];
			}
			student_counter[up]--;

			was_incalc[up] = false;
			return;
		}
	}

	public static void save() throws Exception{
		if (student_counter[up] == 0){
			System.out.println("Please, add at least one student to save records.");
			return;
		}

		FileWriter newFile = new FileWriter("records.txt");

		newFile.write(users[up].name + "'s students records: \n");
		newFile.write("Total Students: " + student_counter[up] + "\n");
		for (int i = 0; i < student_counter[up]; i++){
			newFile.write((i + 1) + ".\n");
			newFile.write("Name: " + students[up][i].name + "\n");
			newFile.write("ID: " + students[up][i].id + "\n");
			newFile.write("Test Score: " + students[up][i].score + "\n");
			newFile.write("Final Grade: " + students[up][i].grade + "\n");
		}
		newFile.close();

		System.out.println("The records saved successfully to \"records.txt\" file.");
	}

	public static boolean checkID(String id){
		int check_id = 0;
		for (int i = 0; i < id.length(); i++){
			if (!Character.isDigit(id.charAt(i))){
				System.out.println("The student's id must consist ONLY of digits!");
				check_id = 1;
				break;
			}
		}
		if (check_id == 1){
			return false;
		}
		return true;
	}

	public static int existID(String id){
		int exist = 0, position = -1;
		for (int i = 0; i < student_counter[up]; i++){
			if (id.equals(students[up][i].id)){
				exist = 1;
				position = i;
				break;
			}
		}

		if (exist == 0){
			System.out.println("The student's not found!");
		}
		return position;
	}

	public static boolean checkName(String name){
		int check_name = 0;
		for (int i = 0; i < name.length(); i++){
			if (!Character.isLetter(name.charAt(i))){
				System.out.println("The student's name must consist ONLY of letters!");
				check_name = 1;
				break;
			}
		}
		if (check_name == 1){
			return false;
		}
		if (name.length() > 15){
			System.out.println("The student's name is too long!");
			return false;
		}

		return true;
	}

	public static boolean checkScore(String score){
		int check_score = 0;
		for (int i = 0; i < score.length(); i++){
			if (!Character.isDigit(score.charAt(i))){
				System.out.println("The student's test score must consist ONLY of digits!");
				check_score = 1;
				break;
			}
		}
		if (check_score == 1){
			return false;
		}
		if (score.length() > 3){
			System.out.println("The student's test score must be between 0 to 100.");
			return false;
		}

		return true;
	}

	public static char calculateGrade(int grade){
		if (grade >= 90){
			return 'A';
		}
		else if (grade >= 80){
			return 'B';
		}
		else if (grade >= 70){
			return 'C';
		}
		else if (grade >= 60){
			return 'D';
		}
		else if (grade >= 40){
			return 'E';
		}
		else {
			return 'F';
		}
	}

	public static void exit(){
		System.out.print("The program has been ended.");
		System.exit(0);
	}
}