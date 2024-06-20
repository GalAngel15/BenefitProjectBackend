package Application._a_Presentation;


import org.springframework.http.MediaType;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Application.business_logic.BoundaryCommand;

import Application.business_logic.ServicesCommand;

@RestController
// remove /{MiniAppName}
@RequestMapping(path = { "/superapp/miniapp/{MiniAppName}" })
public class ControllerCommand {
	private ServicesCommand ServicesCommand;
	
	public ControllerCommand(ServicesCommand ServicesCommand) {
		this.ServicesCommand = ServicesCommand;
	}

	@PostMapping(
//path= {"/{MiniAppName}"
		consumes = MediaType.APPLICATION_JSON_VALUE, 
		produces = MediaType.APPLICATION_JSON_VALUE)

	public BoundaryCommand store_command(@RequestBody BoundaryCommand message) {

			return this.ServicesCommand.createMiniAppCommand(message);
	
		
	}


	
	
	

	
	

	
	
	
	
	

}




