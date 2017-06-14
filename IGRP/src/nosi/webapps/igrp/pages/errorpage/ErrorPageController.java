/*-------------------------*/

/*Create Controller*/

package nosi.webapps.igrp.pages.errorpage;
import nosi.core.webapp.Controller;
import nosi.core.webapp.Igrp;

import java.io.IOException;


public class ErrorPageController extends Controller {		

	/*public void actionNotFound() throws IOException{
		if(Igrp.getInstance().getUser().isAuthenticated()){
			Igrp.getInstance().getFlashMessage().addMessage("error", "Falha na execu��o da opera��o");
			ErrorPage model = new ErrorPage();
			ErrorPageView view = new ErrorPageView(model);
			this.renderView(view);
		}
		else
			this.redirect("igrp", "login", "login");
	}
	*/
	public void actionException() throws IOException{
		if(Igrp.getInstance().getUser().isAuthenticated()){
			Igrp.getInstance().getFlashMessage().addMessage("error", ""+Igrp.getInstance().getRequest().getAttribute("javax.servlet.error.message"));
			Igrp.getInstance().getFlashMessage().addMessage("info", "Por favor contactar o servi�o de HELPDESK para mais informa��es.(helpdesk@nosi.cv - Tel:2607973)");
			ErrorPage model = new ErrorPage();
			ErrorPageView view = new ErrorPageView(model);
			this.renderView(view);
		}
		else
			this.redirect("igrp", "login", "login");
	}
	
	/*public void actionPermission() throws IOException{
		if(Igrp.getInstance().getUser().isAuthenticated()){
			Igrp.getInstance().getFlashMessage().addMessage("error", "Falha na execu��o da opera��o");
			ErrorPage model = new ErrorPage();
			ErrorPageView view = new ErrorPageView(model);
			this.renderView(view);
		}
		else
			this.redirect("igrp", "login", "login");
	}*/
}
