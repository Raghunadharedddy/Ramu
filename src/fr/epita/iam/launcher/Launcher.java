/**
 * 
 */
package fr.epita.iam.launcher;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import fr.epita.iam.datamodel.Identity;
import fr.epita.iam.exceptions.DaoSaveException;
import fr.epita.iam.exceptions.DaoSearchException;
import fr.epita.iam.services.Authenticator;
import fr.epita.iam.services.FileIdentityDAO;
import fr.epita.iam.services.IdentityDAO;
import fr.epita.iam.services.JDBCIdentityDAO;
import fr.epita.logging.LogConfiguration;
import fr.epita.logging.Logger;

/**
 * @author tbrou
 *
 */
public class Launcher {

	/**
	 * @param args
	 * @throws FileNotFoundException 
	 * @throws SQLException 
	 * @throws DaoSearchException 
	 */
	public static void main(String[] args) throws FileNotFoundException, SQLException, DaoSearchException, DaoSaveException {

		JDBCIdentityDAO dao = new JDBCIdentityDAO();
		
		
		LogConfiguration conf = new LogConfiguration("./tmp/application.log");
		Logger logger = new Logger(conf);
		
	    logger.log("beginning of the program");
		Scanner scanner = new Scanner(System.in);
		
		System.out.println("Enter UID:");
		String userName = scanner.nextLine();
		System.out.println("Password :");
		String password = scanner.nextLine();
		
		if (!Authenticator.authenticate(userName, password)) {
			logger.log("unable to authenticate "  + userName);
			return;
		} else {
			System.out.println("Successfully authenticated");
			// We are authenticated
			String answer = "";
			while (!"3".equals(answer)) {
			
				System.out.println("1. Create UID");
				System.out.println("2. Edit identity :");
				System.out.println("3. Exit");
				System.out.println("your choice : ");
				
				logger.log("User chose the " + answer + " choice");

				answer = scanner.nextLine();

				switch (answer) {
				case "1":
					System.out.println("Identity Creation");
					logger.log("selected the identity creation");
					System.out.println("please input the identity display name :");
					String displayName  = scanner.nextLine();
					System.out.println("identity email :");
					String email = scanner.nextLine();
					System.out.println("birthdate :");
					String birthdate = scanner.nextLine();
					
					Identity identity = new Identity( null,displayName, email, birthdate);
					try {
						dao.save(identity);
						System.out.println("the save operation completed successfully");
					} catch (DaoSaveException e) {
						System.out.println("the save operation is not able to complete, details :" + e.getMessage());
					}
					
					break;
				case "2":

					// Update Identity
					
					System.out.println("Identity Update");
					Identity criteria = new Identity( null,null, null, null);
					List<Identity> identities = dao.search(criteria);
					System.out.println("Availabe Identities");
					for(Identity idens : identities)
					{
						
						System.out.print("Name : "+idens.getDisplayName()+"\t");
						System.out.print("Date of Birth : "+idens.getBirthdate()+"\t");
						System.out.println("Email : "+idens.getEmail());
						
					}
					System.out.println("1.update Identity");
					System.out.println("2.delete Identity");
					System.out.println("Exit.");
					System.out.println("your choice : ");
					Scanner sc = new Scanner(System.in);
					String ch = sc.nextLine();
					switch(ch)
					{
					case "1":
						System.out.println("Input the identity display name of the updatable identity :");
						String name  = scanner.nextLine();
					System.out.println("Input the identity display name :");
					String displayName1  = scanner.nextLine();
					System.out.println("Identity email :");
					String email1 = scanner.nextLine();
					System.out.println("Birthdate ///:");
					String birthdate1 = scanner.nextLine();
					
					Identity identity1 = new Identity( null,displayName1, email1, birthdate1);
						dao.update(identity1,name);
						System.out.println("Update successfully");
						break;
					case "2":System.out.println("Input the identity display name of the updatable identity :");
							String name1  = scanner.nextLine();
							dao.delete(name1);
							
						break;
					case "3" : break;
					}
					break;

				case "3":

					System.out.println("you decided to quit, bye!");
					break;
				default:

					System.out.println("unrecognized option : type 1,2,3 or 4 to quit");
					break;
				}

			}

		}

	}


}
