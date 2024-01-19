import java.sql.Connection;
import java.sql.*;
import java.util.Scanner;

public class Main {
    static String username;
    static String password;
    static String email;
    static String requestBook;
    static String requestUser;
    static String returnBook;
    static String returnUser;


    private static Scanner scanner = new Scanner(System.in);
    static String input;


    private static void LoanHistory(Connection connection, Scanner scanner) throws SQLException {
        Statement statement = connection.createStatement();
        String MyBooks = "SELECT book from BooksInLibrary where Book in(SELECT BookNames FROM BorrowedLibraryBooks WHERE UserNames = '" + username + "')";
        ResultSet MB = statement.executeQuery(MyBooks);
        System.out.println("Books Borrowed by " + username);
        while (MB.next()) {
            if (MB.getString("book") != null) ;
            {
                System.out.println(MB.getString("Book"));
            }
        }
        System.out.println("-------------");
        System.out.println(username + " loan history at library of smallville:");
        String MyBooks2 = "SELECT BookNames,DateOfBorrwing,ReturnDate,Daysleft FROM BorrowedLibraryBooks where UserNames = '" + username + "'";
        ResultSet MB2 = statement.executeQuery(MyBooks2);
        while (MB2.next()) {
            if (MB2.getString("ReturnDate") != null & MB2.getString("Booknames") != null) {
                System.out.println("Borrowed Book Title: " + MB2.getString("BookNames") + "\n" +
                        "Date Of Borrowing:" + MB2.getString("DateOfBorrwing") + "\n" + "Return Date:" + MB2.getString("Daysleft") + "\n" +
                        "It was Returned: " + MB2.getString("ReturnDate"));
            } else if (MB2.getString("ReturnDate") == null & MB2.getString("Booknames") != null) {
                System.out.println("Borrowed Book Title: " + MB2.getString("BookNames") + "\n" +
                        "Date Of Borrowing:" + MB2.getString("DateOfBorrwing") + "\n" + "Return Date:" + MB2.getString("Daysleft") + "\n" +
                        "It was Returned: not yet returned");
            }
        }
        System.out.println("-------------");
        System.out.println("Loans of Games as rented by " + username);
        String MyGames = "SELECT VideoGames,DateOfBorrwing,ReturnDate,Daysleft FROM BorrowedLibraryBooks where UserNames = '" + username + "'";
        ResultSet MGames = statement.executeQuery(MyGames);
        while (MGames.next()) {
            if (MGames.getString("ReturnDate") != null & MGames.getString("VideoGames") != null) {
                System.out.println("Borrowed Game Title: " + MGames.getString("VideoGames") + "\n" +
                        "Date Of Borrowing:" + MGames.getString("DateOfBorrwing") + "\n" + "Return Date:" + MGames.getString("Daysleft") + "\n" +
                        "It was Returned: " + MGames.getString("ReturnDate"));
            } else if (MGames.getString("ReturnDate") == null & MGames.getString("VideoGames") != null) {
                System.out.println("Borrowed Game Title: " + MGames.getString("VideoGames") + "\n" +
                        "Date Of Borrowing:" + MGames.getString("DateOfBorrwing") + "\n" + "Return Date:" + MGames.getString("Daysleft") + "\n" +
                        "It was Returned: not yet returned");
            }
        }
        System.out.println("-------------");
    }

    private static void UpdateProfile(Connection connection, Scanner scanner) throws SQLException {
        Statement statement = connection.createStatement();
        System.out.println("If you want to update your username type 1. If you want to update your password type 2");
        String valProfile = scanner.nextLine();
        if (valProfile.equals("1")) {
            System.out.println("please enter your new username first and then your old username");
            System.out.print("new username:");
            String newProfile = scanner.nextLine();
            System.out.print("old username:");
            String oldprofile = scanner.nextLine();
            String updateprofile = "UPDATE UserLibrary SET username ='" + newProfile + "' WHERE username ='" + oldprofile + "'";
            int fixusername = statement.executeUpdate(updateprofile);
            System.out.println("your new username is now " + newProfile);
        } else if (valProfile.equals("2")) {
            System.out.println("please enter your new password");
            String newPassword = scanner.nextLine();
            System.out.println("please enter your old password");
            String oldPassword = scanner.nextLine();
            String updateprofile2 = "Update UserLibrary SET password ='" + newPassword + "' where password ='" + oldPassword + "'";
            int fixpassword = statement.executeUpdate(updateprofile2);
            System.out.println("your new password is now " + newPassword);
        } else {
            System.out.println("sorry thats incorrect press enter to try again");
            String tryprofileagain = scanner.nextLine();
        }

    }

    private static void BorrowBooks(Connection connection, Scanner scanner) {
        try {
            Statement statement = connection.createStatement();

            System.out.print("please write the name of the book you want to borrow: ");
            requestBook = scanner.nextLine();
            String CheckingBook = "SELECT Book from BooksInLibrary WHERE Borrowed = 0 AND book ='" + requestBook + "'";
            // String ISbook = " Select book From BooksInLibrary where Book = "
            ResultSet BookCheck = statement.executeQuery(CheckingBook);
            // String reborrow = "Select Book"
            if (BookCheck.next()) {
                String borrow = "INSERT INTO BorrowedLibraryBooks ( BookNames,UserNames ) VALUES ('" + requestBook + "','" + username + "')";
                int BookRecord = statement.executeUpdate(borrow);
                if (BookRecord > 0) {
                    String showmeBooks = "Update BooksInLibrary SET Borrowed = 1 where Book = '" + requestBook + "'";
                    int rs = statement.executeUpdate(showmeBooks);
                    System.out.println("thank you for borrowing see you next time!");
                }
            } else {
                System.out.println("sorry that name is incorrect. please try again by pressing enter");
                String retry = scanner.nextLine();
            }
        } catch (SQLException ex) {
            Database.PrintSQLException(ex);
            System.out.println("sorry that book is taken");
        }
    }

    private static void ReturnBooks(Connection connection, Scanner scanner) {
        try {
            Statement statement = connection.createStatement();

            System.out.print("please write the name of the book you want to return: ");
            returnBook = scanner.nextLine();
            String ReturnCheckingBook = "SELECT Book from BooksInLibrary WHERE Borrowed = 1 AND book = '" + returnBook + "'";
            ResultSet ReturnBookCheck = statement.executeQuery(ReturnCheckingBook);
            if (ReturnBookCheck.next()) {
            /*System.out.println("please enter your Username: ");
             returnUser = scanner.nextLine();*/
                String Giveback = "UPDATE BorrowedLibraryBooks SET ReturnDate = NOW() where BookNames = '" + returnBook + "'";
                // String borrow = "INSERT INTO BorrowedLibraryBooks ( BookNames,UserNames ) VALUES ('" +returnBook + "','" +returnUser+ "')";
                int BookReturn = statement.executeUpdate(Giveback);
                if (BookReturn > 0) {
                    String Giveback2 = "UPDATE BooksInLibrary SET Borrowed = FALSE where Book = '" + returnBook + "'";
                    int BookReturn2 = statement.executeUpdate(Giveback2);
                    System.out.println("thank you for returning see you next time!");
                }
            } else {
                System.out.println("sorry that name is incorrect. please try again by pressing enter");
                String retry = scanner.nextLine();
            }
        } catch (SQLException ex) {
            Database.PrintSQLException(ex);

        }

    }

    private static void BorrowGames(Connection connection, Scanner scanner) {
        try {
            Statement statement = connection.createStatement();

            System.out.print("please write the name of the game you want to borrow: ");
            String requestGames = scanner.nextLine();
            String Checking = "Select Games From GamesInLibrary where Games ='" + requestGames + "'";
            ResultSet GameCheck = statement.executeQuery(Checking);
            if (GameCheck.next()) {
                String borrowGames = "INSERT INTO BorrowedLibraryBooks ( VideoGames,UserNames,Daysleft ) VALUES ('" + requestGames + "','" + username + "',DATE_ADD(Current_date,INTERVAL 10 Day))";
                int BookRecord = statement.executeUpdate(borrowGames);
                if (BookRecord > 0) {
                    String showmeGames = "Update GamesInLibrary SET Borrowed = 1 where Games = '" + requestGames + "'";
                    int rs = statement.executeUpdate(showmeGames);
                    System.out.println("thank you for borrowing see you next time!");
                }
            } else {
                System.out.println("sorry that name is incorrect. please try again by pressing enter");
                String retry = scanner.nextLine();
            }
        } catch (SQLException ex) {
            Database.PrintSQLException(ex);
            System.out.println("sorry that game is taken");
            throw new RuntimeException(ex);
        }
    }

    private static void ReturnGames(Connection connection, Scanner scanner) {
        try {
            Statement statement = connection.createStatement();

            System.out.print("please write the name of the Game you want to return: ");
            String returnGame = scanner.nextLine();
            String ReturnCheckingBook = "Select Games From GamesInLibrary where Games ='" + returnGame + "'";
            ResultSet ReturnBookCheck = statement.executeQuery(ReturnCheckingBook);
            if (ReturnBookCheck.next()) {
            /*System.out.println("please enter your Username: ");
             returnUser = scanner.nextLine();*/
                String Giveback = "UPDATE BorrowedLibraryBooks SET ReturnDate = NOW() where VideoGames = '" + returnGame + "'";
                // String borrow = "INSERT INTO BorrowedLibraryBooks ( BookNames,UserNames ) VALUES ('" +returnBook + "','" +returnUser+ "')";
                int BookReturn = statement.executeUpdate(Giveback);
                if (BookReturn > 0) {
                    String Giveback2 = "UPDATE GamesInLibrary SET Borrowed = FALSE where Games = '" + returnGame + "'";
                    int BookReturn2 = statement.executeUpdate(Giveback2);
                    System.out.println("thank you for returning see you next time!");
                }
            } else {
                System.out.println("sorry that name is incorrect. please try again by pressing enter");
                String retry = scanner.nextLine();
            }
        } catch (SQLException ex) {
            Database.PrintSQLException(ex);

        }
    }

    private static void SearchFilter(Connection connection, Scanner scanner) throws SQLException {
        Statement statement = connection.createStatement();
        System.out.println("please choose to filter ethier games or books. for books type 1 and For games type 2");
        String chosenow = scanner.nextLine();
        if (chosenow.equals("1")) {
            System.out.print("Book name:");
            String filterone = scanner.nextLine();
            System.out.println("author name:");
            String filtertwo = scanner.nextLine();
            String filterCenter = "Select Book from BooksInLibrary where Book ='" + filterone + "' OR author =' Written by" + filtertwo + "'";
            ResultSet Centerfilter = statement.executeQuery(filterCenter);

            if (Centerfilter.next()) {
                System.out.println(Centerfilter.getString("Book"));
            }
            String filterCenteralt = "Select Book from BooksInLibrary where author='" + filtertwo + "'";
            ResultSet CenterFilteralt = statement.executeQuery(filterCenteralt);
            if (CenterFilteralt.next()) {
                System.out.println(CenterFilteralt.getString("Book"));
            }
        } else if (chosenow.equals("2")) {
            String filterthree = scanner.nextLine();
            String filterfour = scanner.nextLine();
            String filterCenter2 = "Select Games,Rating from GamesInLibrary where Games ='" + filterthree + "' OR Rating ='" + filterfour + "'";
            ResultSet Centerfilter2 = statement.executeQuery(filterCenter2);
            if (Centerfilter2.next()) {
                System.out.println(Centerfilter2.getString("Games") + "\n" + Centerfilter2.getString("Rating"));
            }
        } else {
            System.out.println("sorry thats wrong try again by pressing enter");
            String SearchAgain = scanner.nextLine();
        }

    }

    private static void LibraryHub(Connection connection, Scanner scanner) {
        // System.out.println("this shit works baby");
        try {
            Statement statement = connection.createStatement();
            String input2;
            do {
                System.out.println("Welcome to the library of smallville or as the kids call it Library of small!\n" +
                        "Now please chose between the options below:\n" + //done!
                        "1: Shows you the Books&Games currently available to borrow\n" + // done!
                        "2: Shows your loan history of both books and games\n" + //Done! for real this time
                        "3: Update your profile settings\n" + // finally done!
                        "4: Lets you Borrow a book of your choice\n" + // domne! for real this time
                        "5: Lets you Return books\n" + // Done! for real this time
                        "6: Lets you borrow a game of your choice\n" + //done!
                        "7: Lets you Return Games\n" + //done!
                        "8: Lets you search with filters\n" + //done! finally beg to gods
                        "9: Log out\n"); //done my homie
                System.out.print("Please enter a Number from the List above here:");
                input2 = scanner.nextLine();
                if (input2.equals("1")) {
                    System.out.println("Book Section");
                    String showbooks = "Select book,author,PublishingCompany from BooksInLibrary where Borrowed = false";
                    ResultSet shower = statement.executeQuery(showbooks);
                    while (shower.next()) {
                        System.out.println("Book title:" + shower.getString("book") + "\n" + shower.getString("author") + "\nPublihsed by: " + shower.getString("PublishingCompany") + "\n");
                    }
                    System.out.println("--------------");
                    System.out.println("Game section");
                    String showGames = "Select Games,genre,Rating From GamesInLibrary where Borrowed = false";
                    ResultSet allgames = statement.executeQuery(showGames);
                    while (allgames.next()) {
                        System.out.println("Game Title:" + allgames.getString("Games") + "\nGenre:" + allgames.getString("genre") + "\n Rating:" + allgames.getString("Rating"));
                    }

                } else if (input2.equals("2")) {
                    LoanHistory(connection, scanner);
                } else if (input2.equals("3")) {
                    UpdateProfile(connection, scanner);
                } else if (input2.equals("4")) {
                    BorrowBooks(connection, scanner);
                } else if (input2.equals("5")) {
                    ReturnBooks(connection, scanner);
                } else if (input2.equals("6")) {
                    BorrowGames(connection, scanner);
                } else if (input2.equals("7")) {
                    ReturnGames(connection, scanner);
                } else if (input2.equals("8")) {
                    SearchFilter(connection, scanner);
                } else if (input2.equals("9")) {
                    System.out.println("goodbye and we hope to see you again soon");
                }
            } while (!input2.equals("9"));
            {
                //input = String.valueOf(4);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    // En kommentar
    private static void LoginUser(Connection connection, Scanner scanner) {
        System.out.print("username:");
        username = scanner.nextLine();
        System.out.print("password:");
        password = scanner.nextLine();
        try {
            Statement statement = connection.createStatement();
            //library assignment
            String sql = "SELECT username,password from UserLibrary WHERE username ='" + username + "' AND password ='" + password + "'";
            // '360sniper69','myGoldenminecraftserver69','Snipergod@gmail.com',CURRENT_TIMESTAMP
            ResultSet newuser2 = statement.executeQuery(sql);
            if (newuser2.next()) {
                LibraryHub(connection, scanner);
            } else {
                System.out.println("sorry that was wrong try again");
            }
            //end of library asssignment
        } catch (SQLException ex) {
            Database.PrintSQLException(ex);
        }
    }

    private static void RegisterUser(Connection connection, Scanner scanner) {

        System.out.println("username:");
        username = scanner.nextLine();
        System.out.println("password:");
        password = scanner.nextLine();
        System.out.println("email:"); // only put in your email up to before the @ like this but without the marks 'bently'@gmail.com so here you would only type in bently
        email = scanner.nextLine();
        try {
            Statement statement = connection.createStatement();
            //library assignment
            String sql = "INSERT INTO UserLibrary (username,password,email,lastlogin) VALUES('" + username + "','" + password + "','" + email + "@gmail.com',CURRENT_TIMESTAMP )";
            // '360sniper69','myGoldenminecraftserver69','Snipergod@gmail.com',CURRENT_TIMESTAMP
            int newuser = statement.executeUpdate(sql);
            if (newuser > 0) {
                System.out.println(sql);
                LibraryHub(connection, scanner);
            } else {
                System.out.println("Kunde inte hitta en användare");
            }

            //end of library asssignment
        } catch (SQLException ex) {
            System.out.println("the username and password");
            Database.PrintSQLException(ex);
        }

    }

    public static void main(String[] args) {
        Database.username = "root";
        Database.password = "Underfell5958";
        //inte ändra port
        Database.database = "LIBRARYOFSMALL";
        Scanner scanner = new Scanner(System.in);
        String input;
        Connection connection = Database.getConnection();

        if (connection == null) {
            System.out.println("kunde inte ansluta till databs soryy");
            System.exit(-1);
            return;
        }
        do {
            System.out.println("please login or register to view our collection: \n" +
                    "1: Login with existing account\n" +
                    "2: Register a new account if you don't have one\n" +
                    "3: Quit");
            System.out.print("Please enter a Number from the List abover here:");
            input = scanner.nextLine();
            switch (input) {
                case "1":
                    LoginUser(connection, scanner);
                    break;
                case "2":
                    RegisterUser(connection, scanner);
                    break;
                case "3":

            }

        } while (!input.equals("3"));
        {
            System.out.println("thank you and goodbye");
        }

    }
}

