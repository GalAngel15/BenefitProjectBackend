package Application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import Application.DataAccess.Location;
import Application._a_Presentation.BoundaryIsNotFoundException;
import Application.business_logic.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.web.client.RestClient;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
class Applicationtests2 {
	private RestClient restClientObj;
	private RestClient restClientUser;
	private RestClient restClientCommand;
	private RestClient restClientAdmin;
    @Value("${spring.application.name}")
	private String superAppName;

	
	@Value("${server.port:8080}")
	public void setPort(int port) {
		this.restClientObj = RestClient.create("http://localhost:" + port + "/superapp/objects");
		this.restClientAdmin = RestClient.create("http://localhost:" + port +"/superapp/admin" );
		this.restClientUser = RestClient.create("http://localhost:" + port +"/superapp/users" );
		this.restClientCommand = RestClient.create("http://localhost:" + port +"/superapp/commands");
	
	}

	@BeforeEach
	public void setup() {
		
		

	}
    @AfterEach
    public void tearDown() {
        try {
            String username = "admUser";
            NewUserBoundary user = new NewUserBoundary();
            user.setUserName(username);
            user.setRole("ADM_USER");
            user.setEmail(username + "@aa.com");
            user.setAvatar("houj");
            
            // Post a app user 
            this.restClientUser.post().body(user).retrieve().body(BoundaryUser.class);
            System.out.println("Posted ADM_USER: " + username);


            // Delete all objects
            this.restClientAdmin.delete().uri("/objects?userSuperapp={userSuperApp}&email={email}",
                this.superAppName, username + "@aa.com").retrieve();
            System.out.println("Deleted all objects");

            // Delete all commands
            this.restClientAdmin.delete().uri("/miniapp?userSuperapp={userSuperApp}&email={email}",
                this.superAppName, username + "@aa.com").retrieve();
            System.out.println("Deleted all commands");
            
            // Delete all users
            this.restClientAdmin.delete().uri("/users?userSuperapp={userSuperApp}&email={email}",
            		this.superAppName, username + "@aa.com").retrieve();
            System.out.println("Deleted all users");
            
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error during tearDown: " + e.getMessage());
        }
    }
	
	
	@Test
	public void contextLoads() {
		
		
	}
	@Test
	public void testGetAllObjectsToUserSuperApp()throws Exception
	{
		// GIVEN the server is up
		// AND the database contains 4 active objects
		// AND the database contains 4 non active objects
		// AND the database contains 1 super app user
		String username = "superUser";
		NewUserBoundary user  = new NewUserBoundary();
		user.setUserName(username);
		user.setRole("SUPERAPP_USER");
		user.setEmail(username+"@aa.com");
		user.setAvatar("houj");
		//post a super app user 
		this.restClientUser.post().body(user).retrieve().body(BoundaryUser.class);
		List<BoundaryObject> all = new ArrayList<>();
		String type = "type_test";
		String alias = "alias_test";
		String createdBy = username+"@aa.com";
		for (int i = 0; i < 4; i++) {
			BoundaryObject obj = new BoundaryObject();
			obj.setActive(true);
			obj.setLocation(new Location(0.2+i , 0.2+i));
			obj.setType(type+" "+i);
			obj.setAlias(alias+" "+i);
			CreatedBy CreatedBy  = new CreatedBy();
			UserId UserId1 = new UserId();
			UserId1.setEmail(createdBy);
			UserId1.setSuperAPP(this.superAppName);
			CreatedBy.setUserId(UserId1);
			obj.setCreatedBy(CreatedBy);
			obj.setObjectDetails(Collections.singletonMap("person", "Jane #" + i));
			// POST Objects to server

			obj = this.restClientObj.post().body(obj).retrieve().body(BoundaryObject.class);
			all.add(obj);
		}
		for (int i = 0; i < 4; i++) {
			BoundaryObject obj = new BoundaryObject();
			obj.setActive(false);
			obj.setLocation(new Location(0.2+i , 0.2+i));
			obj.setType(type+" "+i);
			obj.setAlias(alias+" "+i);
			CreatedBy CreatedBy  = new CreatedBy();
			UserId UserId1 = new UserId();
			UserId1.setEmail(createdBy+i+"non_active");
			UserId1.setSuperAPP(this.superAppName);
			CreatedBy.setUserId(UserId1);
			obj.setCreatedBy(CreatedBy);
			obj.setObjectDetails(Collections.singletonMap("non_active", "non_active #" + i));
			// POST Objects to server

			obj = this.restClientObj.post().body(obj).retrieve().body(BoundaryObject.class);
			all.add(obj);
		}

		// /superapp/objects?userSuperApp ={userSuperApp}&userEmail = {email}&size = {size}&page = {page}
		// WHEN I invoke GET /superapp/objects?userSuperApp ={2024B.gal.angel}&userEmail = {superUser@aa.com}&size = {10}&page = {1}
		BoundaryObject [] actual = this.restClientObj.get().uri("?userSuperapp={userSuperApp}&email={email}&size={size}&page={page}"
				,this.superAppName ,username+"@aa.com" , 10,0 )
				.retrieve().body(BoundaryObject [].class);
		
		// THEN the server responds with the same 8 objects generated above
		assertThat(actual).hasSize(8)
						.usingRecursiveFieldByFieldElementComparator()
						.containsExactlyInAnyOrderElementsOf(all);
		
		
	}
	@Test
	public void testGetAllObjectsToUserMiniApp()throws Exception
	{

		// GIVEN the server is up
		// AND the database contains 4 active objects
		// AND the database contains 4 non active objects
		// AND the database contains 1 mini app user
		String username = "miniAppUser";
		NewUserBoundary user  = new NewUserBoundary();
		user.setUserName(username);
		user.setRole("MINIAPP_USER");
		user.setEmail(username+"@aa.com");
		user.setAvatar("p");
		System.err.println(user.toString());
		//post a super app user 
		this.restClientUser.post().body(user).retrieve().body(BoundaryUser.class);
		List<BoundaryObject> actives = new ArrayList<>();
		String type = "type_test";
		String alias = "alias_test";
		String createdBy = username+"@aa.com";
		for (int i = 0; i < 4; i++) {
			BoundaryObject obj = new BoundaryObject();
			obj.setActive(true);
			obj.setLocation(new Location(0.2+i , 0.2+i));
			obj.setType(type+" "+i);
			obj.setAlias(alias+" "+i);
			CreatedBy CreatedBy  = new CreatedBy();
			UserId UserId1 = new UserId();
			UserId1.setEmail(createdBy);
			UserId1.setSuperAPP("");
			CreatedBy.setUserId(UserId1);
			obj.setCreatedBy(CreatedBy);
			obj.setObjectDetails(Collections.singletonMap("person", "Jane #" + i));
			// POST Objects to server

			obj = this.restClientObj.post().body(obj).retrieve().body(BoundaryObject.class);
			actives.add(obj);
		}
		List<BoundaryObject> non_actives = new ArrayList<>();
		for (int i = 0; i < 4; i++) {
			BoundaryObject obj = new BoundaryObject();
			obj.setActive(false);
			obj.setLocation(new Location(0.2+i , 0.2+i));
			obj.setType(type+" "+i);
			obj.setAlias(alias+" "+i);
			CreatedBy CreatedBy  = new CreatedBy();
			UserId UserId1 = new UserId();
			UserId1.setEmail(createdBy);
			UserId1.setSuperAPP("");
			CreatedBy.setUserId(UserId1);
			obj.setCreatedBy(CreatedBy);
			obj.setObjectDetails(Collections.singletonMap("non_active", "non_active #" + i));
			// POST Objects to server

			obj = this.restClientObj.post().body(obj).retrieve().body(BoundaryObject.class);
			non_actives.add(obj);
		}
		// /superapp/objects?userSuperApp ={userSuperApp}&userEmail = {email}&size = {size}&page = {page}
		// WHEN I invoke GET /superapp/objects?userSuperApp ={2024B.gal.angel}&userEmail = {superUser@aa.com}&size = {10}&page = {1}
		//?userSuperapp=2024B.gal.angel&email=miniAppUser%40aa.com&size=10&page=0
		BoundaryObject [] actual = this.restClientObj.get().uri("?userSuperapp={userSuperApp}&email={email}&size={size}&page={page}"
				,this.superAppName ,username+"@aa.com" , 10,0 )
				.retrieve().body(BoundaryObject [].class);
		
		// THEN the server responds with  4 objects
		assertThat(actual).hasSize(4)
						.usingRecursiveFieldByFieldElementComparator()
						.containsExactlyInAnyOrderElementsOf(actives);
		tearDown();
		
	}
	@Test
	public void testGetSpecificObjectForUserMiniApp()throws Exception
	{

		// GIVEN the server is up
		// AND the database contains 1  non active objects
		// AND the database contains 1 active objects
		// AND the database contains 1 mini app user
		String username = "miniAppUser";
		NewUserBoundary user  = new NewUserBoundary();
		user.setUserName(username);
		user.setRole("MINIAPP_USER");
		
		user.setEmail(username+"@aa.com");
		
		this.restClientUser.post().body(user).retrieve().body(BoundaryUser.class);
		String type = "type_test";
		String alias = "alias_test";
		String createdBy = username+"@aa.com";
		
		BoundaryObject obj = new BoundaryObject();
		obj.setActive(true);
		obj.setLocation(new Location(0.2 , 0.2));
		obj.setType(type+" ");
		obj.setAlias(alias+" ");
		CreatedBy CreatedBy  = new CreatedBy();
		UserId UserId1 = new UserId();
		UserId1.setEmail(createdBy);
		UserId1.setSuperAPP("");
		CreatedBy.setUserId(UserId1);
		obj.setCreatedBy(CreatedBy);
		obj.setObjectDetails(Collections.singletonMap("person", "Jane #"));
		// POST Objects to server
		obj = this.restClientObj.post().body(obj).retrieve().body(BoundaryObject.class);
		
		BoundaryObject obj1 = new BoundaryObject();
		obj1.setActive(false);
		obj1.setLocation(new Location(0.2 , 0.2));
		obj1.setType(type+" ");
		obj1.setAlias(alias+" ");
		CreatedBy CreatedBy1  = new CreatedBy();
		UserId UserId11 = new UserId();
		UserId11.setEmail(createdBy);
		UserId11.setSuperAPP("");
		CreatedBy1.setUserId(UserId11);
		obj1.setCreatedBy(CreatedBy1);
		obj1.setObjectDetails(Collections.singletonMap("person", "Jane #"));
		// POST Objects to server
		obj1 = this.restClientObj.post().body(obj1).retrieve().body(BoundaryObject.class);
		// WHEN I invoke GET on the false active object  /superapp/objects?userSuperApp ={2024B.gal.angel}&userEmail = {superUser@aa.com}
		// THEN I get an BoundaryIsNotFoundException
		BoundaryObject obj_return  = this.restClientObj.get().uri("/{superapp}/{id}?userSuperapp={userSuperApp}&email={email}",
				this.superAppName,obj.getObjectID().getId(),this.superAppName ,username+"@aa.com").retrieve().body(BoundaryObject.class);
		assertThat(obj_return).isNotNull();
		assertThat(obj_return.getObjectID().getId()).isEqualTo(obj.getObjectID().getId());
		tearDown();
	}
	
	@Test
	public void testGetSpecificObjectForUserSuperApp()throws Exception
	{

		// GIVEN the server is up
		// AND the database contains 1  non active objects
		// AND the database contains 1 active objects
		// AND the database contains 1 super app user
		String username = "SuperAppUser";
		NewUserBoundary user  = new NewUserBoundary();
		user.setUserName(username);
		user.setRole("SUPERAPP_USER");
		user.setEmail(username+"@aa.com");
		
		this.restClientUser.post().body(user).retrieve().body(BoundaryUser.class);
		String type = "type_test";
		String alias = "alias_test";
		String createdBy = username+"@aa.com";
		
		BoundaryObject obj = new BoundaryObject();
		obj.setActive(true);
		obj.setLocation(new Location(0.2 , 0.2));
		obj.setType(type+" ");
		obj.setAlias(alias+" ");
		CreatedBy CreatedBy  = new CreatedBy();
		UserId UserId1 = new UserId();
		UserId1.setEmail(createdBy);
		UserId1.setSuperAPP("");
		CreatedBy.setUserId(UserId1);
		obj.setCreatedBy(CreatedBy);
		obj.setObjectDetails(Collections.singletonMap("person", "Jane #"));
		// POST Objects to server
		obj = this.restClientObj.post().body(obj).retrieve().body(BoundaryObject.class);
		
		BoundaryObject obj1 = new BoundaryObject();
		obj1.setActive(false);
		obj1.setLocation(new Location(0.2 , 0.2));
		obj1.setType(type+" ");
		obj1.setAlias(alias+" ");
		CreatedBy CreatedBy1  = new CreatedBy();
		UserId UserId11 = new UserId();
		UserId11.setEmail(createdBy);
		UserId11.setSuperAPP("");
		CreatedBy1.setUserId(UserId11);
		obj1.setCreatedBy(CreatedBy1);
		obj1.setObjectDetails(Collections.singletonMap("person", "Jane #"));
		// POST Objects to server
		obj1 = this.restClientObj.post().body(obj1).retrieve().body(BoundaryObject.class);
		
		// WHEN I invoke GET on the false active object  /superapp/objects?userSuperApp ={2024B.gal.angel}&userEmail = {superUser@aa.com}
		// THEN I get an object

		BoundaryObject obj_return  = this.restClientObj.get().uri("/{superapp}/{id}?userSuperapp={userSuperapp}&email={email}",
				this.superAppName,obj.getObjectID().getId(),this.superAppName ,username+"@aa.com").retrieve().body(BoundaryObject.class);
		assertThat(obj_return).isNotNull();
		assertThat(obj_return.getObjectID().getId()).isEqualTo(obj.getObjectID().getId());
	}
	
//	//no finish yet -yam 
//	public void testAdminGetAllMiniAppCommands()
//	{
//
//		// GIVEN the server is up
//		// AND 5 miniApp commands
//		// AND the database contains 1 admin app user
//		
//		String username = "AdminAppUser";
//		NewUserBoundary user  = new NewUserBoundary();
//		user.setUserName(username);
//		user.setRole("ADM_USER");
//		UserId UserId = new UserId();
//		UserId.setEmail(username+"@aa.com");
//		
//		this.restClientUser.post().body(user).retrieve().body(BoundaryUser.class);
//		for (int i = 0; i < 5; i++) {
//			 BoundaryCommand BoundaryCommand  = new BoundaryCommand();
//		     //Set values for the object's attributes
//		       CommandId ci = new CommandId();
//		       ci.setId("1234567");
//		       BoundaryCommand.setCommandId(ci);
//		       BoundaryCommand.setCommand("Test Command "+i);
//		       BoundaryCommand.setTargetObject(new TargetObject());
//		       String createdBy = "yam_test_@yam.com";
//				CreatedBy CreatedBy  = new CreatedBy();
//				UserId UserId1 = new UserId();
//				UserId1.setEmail(createdBy);
//				UserId1.setSuperAPP("");
//				CreatedBy.setUserId(UserId1);
//		       BoundaryCommand.setCommandAttributes(new HashMap<>());
//		        
//		       BoundaryCommand = this.restClientCommand
//					.post()
//					.body(BoundaryCommand)
//					.retrieve()
//					.body(BoundaryCommand.class);
//		}
//		
//
//		
//		
//		
//	}
	
	
	
	
	
}
