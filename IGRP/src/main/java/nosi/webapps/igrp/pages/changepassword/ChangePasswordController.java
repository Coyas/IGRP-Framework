
package nosi.webapps.igrp.pages.changepassword;
import nosi.core.config.Config;
/*----#START-PRESERVED-AREA(PACKAGES_IMPORT)----*/
import nosi.core.ldap.LdapInfo;
import nosi.core.ldap.LdapPerson;
import nosi.core.ldap.NosiLdapAPI;
import nosi.core.webapp.Igrp;
import nosi.core.webapp.Response;
import nosi.core.webapp.mvc.Controller;
import nosi.core.webapp.util.Core;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import javax.xml.ws.handler.MessageContext;

import org.wso2.carbon.um.ws.service.RemoteUserStoreManagerService;
import org.wso2.carbon.um.ws.service.UpdateCredential;
import org.wso2.carbon.um.ws.service.dao.xsd.ClaimDTO;

import static nosi.core.i18n.Translator.gt;

import nosi.webapps.igrp.dao.User;
import service.client.WSO2UserStub;

/*----#END-PRESERVED-AREA----*/

public class ChangePasswordController extends Controller {		


	public Response actionIndex() throws IOException, IllegalArgumentException, IllegalAccessException{
		/*----#START-PRESERVED-AREA(INDEX)----*/  
		ChangePassword model = new ChangePassword();
			model.load();
		
		ChangePasswordView view = new ChangePasswordView(model);
		view.email_1.setValue(Core.getCurrentUser().getEmail().trim());
		return this.renderView(view);
		/*----#END-PRESERVED-AREA----*/
	}


	public Response actionGuardar() throws IOException, IllegalArgumentException, IllegalAccessException{
		/*----#START-PRESERVED-AREA(GUARDAR)----*/
		ChangePassword model = new ChangePassword();
		if(Igrp.getInstance().getRequest().getMethod().equalsIgnoreCase("post")){

			model.load();
		
			if(!model.getPassword_1().equals(model.getConfirm_password())) {
				Core.setMessageError(gt("Password inconsistentes ... Tente de novo !"));
				return this.forward("igrp","ChangePassword","index");
			}
			
			if (model.getPassword_1().equals(model.getOld_password())) {
				Core.setMessageError(gt("A nova senha não pode ser igual a senha anterior ... Tente de novo !"));
				return this.forward("igrp","ChangePassword","index");
			}
			
			switch(this.getConfig().getAutenticationType()) {
				case "ldap": return ldap(model.getOld_password(), model.getPassword_1());
				case "db": return db(model.getOld_password(), model.getPassword_1());
				default:;
			}
			
		}
		return this.redirect("igrp","ChangePassword","index");
		/*----#END-PRESERVED-AREA----*/
	}
	
	/*----#START-PRESERVED-AREA(CUSTOM_ACTIONS)----*/
	
	
	private Response db(String currentPassword, String newPassword) throws IOException {
		User user = Core.getCurrentUser();
		if(!user.getPass_hash().equals(nosi.core.webapp.component.User.encryptToHash(user.getUser_name()+currentPassword, "SHA-256"))) {
			Core.setMessageError(gt("Senha atual inválida. Tente de novo !!! "));
			return this.forward("igrp","ChangePassword","index");
		} 
		user.setPass_hash(nosi.core.webapp.component.User.encryptToHash(user.getUser_name()+ "" + newPassword, "SHA-256"));
		user.setUpdated_at(System.currentTimeMillis());
		user = user.update();
		if(user !=null)
			Core.setMessageSuccess(gt("Senha atualizada com sucesso."));
		else
			return this.forward("igrp","ChangePassword","index");
		
		return this.redirect("igrp","ChangePassword","index");
	
	}
	
	private Response ldap(String currentPassword, String newPassword) throws IOException {
	
		/*User user = Core.getCurrentUser();
		if(!user.getPass_hash().equals(nosi.core.webapp.User.encryptToHash(currentPassword, "MD5"))) {
			Core.setMessageError(gt("Senha atual inválida. Tente de novo !!! "));
			return this.forward("igrp","ChangePassword","index");
		}*/
		
		Properties settings = loadIdentityServerSettings();
		
		if(settings.getProperty("ids.wso2.enabled") != null && settings.getProperty("ids.wso2.enabled").equalsIgnoreCase("true")) {
			if(!useIds(currentPassword, newPassword, settings))
				return forward("igrp","ChangePassword","index");
		}else {
			if(!dontUseIds(currentPassword, newPassword))
				return forward("igrp","ChangePassword","index");
		}
		
		return redirect("igrp","ChangePassword","index");
	}
	
	private boolean useIds(String currentPassword, String newPassword, Object ...obj) {
		boolean flag = false;
		Properties settings = (Properties)obj[0];
		User user = Core.getCurrentUser();
		try {
			URL url =  new URL(settings.getProperty("ids.wso2.RemoteUserStoreManagerService-wsdl-url"));
	        WSO2UserStub.disableSSL();
	        WSO2UserStub stub = new WSO2UserStub(new RemoteUserStoreManagerService(url));
	        stub.applyHttpBasicAuthentication(settings.getProperty("ids.wso2.admin-usn"), settings.getProperty("ids.wso2.admin-pwd"), 2);
	        
	        flag = stub.getOperations().authenticate(user.getUser_name(), currentPassword);
	        
	        if(!flag) { 
	        	Core.setMessageError(gt("Ooops! Email e senha inconsistentes."));
	        	return flag;
	        }
	        
	        flag = false;
	        
	        List<ClaimDTO> result = stub.getOperations().getUserClaimValues(user.getUser_name(), "");
	        if(result != null && result.size() > 0) {
	        	try {
	        		UpdateCredential credential = new UpdateCredential();
			        credential.setUserName(new JAXBElement<String>(new QName(settings.getProperty("ids.wso2.RemoteUserStoreManagerService-wsdl-url"), "userName"), String.class, user.getUser_name()));
			        credential.setNewCredential(new JAXBElement<String>(new QName(settings.getProperty("ids.wso2.RemoteUserStoreManagerService-wsdl-url"), "newCredential"), String.class, newPassword));
			        credential.setOldCredential(new JAXBElement<String>(new QName(settings.getProperty("ids.wso2.RemoteUserStoreManagerService-wsdl-url"), "oldCredential"), String.class, currentPassword));
		
			        stub.getOperations().updateCredential(credential);
			        
			        javax.xml.ws.BindingProvider bp =  (javax.xml.ws.BindingProvider)stub.getOperations();
			        
			        Map<String, Object> m = bp.getResponseContext();
			      
			        
			        int responseCode = -1;
			        
			        try {
			        	responseCode = (Integer)m.get(MessageContext.HTTP_RESPONSE_CODE); // bug due version of jax-ws client TomEE 
			        }catch (NullPointerException e) {
			        	responseCode = 200;
					}
			        if(responseCode == 202 || responseCode == 200) { 
			        	Core.setMessageSuccess(gt("Password alterado com sucesso."));
			        	flag = true;
			        }
	        	}catch(Exception e) {
	        		e.printStackTrace();
	        		Core.setMessageError("Ocorreu um erro. " + e.getMessage());
	        	}
	        }else {
	        	Core.setMessageError(gt("Ocorreu um erro. Utilizador inválido."));
	        }
		}catch(Exception e) {
			Core.setMessageError("Ocorreu um erro. " + e.getMessage());
			e.printStackTrace();
		}
		
		return flag;
	}
	
	private boolean dontUseIds(String currentPassword, String newPassword) {
		boolean flag = true;
		User user = Core.getCurrentUser();
		
		File file = new File(getClass().getClassLoader().getResource(new Config().getBasePathConfig() + File.separator + "ldap" + File.separator + "ldap.xml").getPath());
		
		LdapInfo ldapinfo = JAXB.unmarshal(file, LdapInfo.class);
		NosiLdapAPI ldap = new NosiLdapAPI(ldapinfo.getUrl(), ldapinfo.getUsername(), ldapinfo.getPassword(), ldapinfo.getBase(), ldapinfo.getAuthenticationFilter(), ldapinfo.getEntryDN());
		
		LdapPerson person = ldap.getUserLastInfo(Core.getCurrentUser().getEmail().trim());
		
		String error = ldap.getError();
		if(person != null) {
			person.setPwdLastSet(newPassword);
			person.setUid(user.getUser_name());
			ldap.changePassword(person);
			error = ldap.getError();
			if(error != null) {
				Core.setMessageError("Ocorreu um erro. LDAP error: " + error);
				flag = false;
			}else { 
				user.setPass_hash(nosi.core.webapp.component.User.encryptToHash(newPassword, "SHA-256"));
				user.setUpdated_at(System.currentTimeMillis());
				user = user.update();
				Core.setMessageSuccess(gt("Password alterado com sucesso."));
			}
		}else {
			Core.setMessageError(gt(error != null ? error : "Ocorreu um erro. Email inválido."));
			flag = false;
		}
		return flag;
	}
	
	private Properties loadIdentityServerSettings() {
		String path = new Config().getBasePathConfig() + File.separator  + "common";
		String fileName = "main.xml";
		File file = new File(getClass().getClassLoader().getResource(path + File.separator + fileName).getPath());
		
		Properties props = new Properties();
		try (FileInputStream fis = new FileInputStream(file)) {
			props.loadFromXML(fis);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return props;
	}
		/*----#END-PRESERVED-AREA----*/
}
