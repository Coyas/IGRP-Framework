
package nosi.webapps.igrp.pages.ldapuser;
import nosi.core.config.Config;
import nosi.core.ldap.LdapInfo;
import nosi.core.ldap.LdapPerson;
import nosi.core.ldap.NosiLdapAPI;
import nosi.core.webapp.Igrp;
import nosi.core.webapp.Response;
import nosi.core.webapp.annotation.RParam;
import nosi.core.webapp.mvc.Controller;
import nosi.core.webapp.util.Core;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Properties;

import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import org.wso2.carbon.um.ws.service.AddUser;
import org.wso2.carbon.um.ws.service.RemoteUserStoreManagerService;
import org.wso2.carbon.user.mgt.common.xsd.ClaimValue;

import static nosi.core.i18n.Translator.gt;

import nosi.webapps.igrp.dao.User;
import service.client.WSO2UserStub;

/*----#END-PRESERVED-AREA----*/

public class LdapUserController extends Controller {		


	public Response actionIndex() throws IOException, IllegalArgumentException, IllegalAccessException{
		/*----#START-PRESERVED-AREA(INDEX)----*/
		LdapUser model = new LdapUser();
		if(Igrp.getInstance().getRequest().getMethod().equalsIgnoreCase("post")){
			model.load();
		}
		LdapUserView view = new LdapUserView(model);
		return this.renderView(view);
		/*----#END-PRESERVED-AREA----*/
	}


	public Response actionGravar() throws IOException, IllegalArgumentException, IllegalAccessException{
		/*----#START-PRESERVED-AREA(GRAVAR)----*/
		LdapUser model = new LdapUser();
		if(Igrp.getInstance().getRequest().getMethod().equalsIgnoreCase("post")){
			model.load();
			
			boolean success = false;
			
			Properties settings = loadIdentityServerSettings();
			if(settings.getProperty("ids.wso2.enabled") != null && settings.getProperty("ids.wso2.enabled").equalsIgnoreCase("true")) {
				success = addThroughIds(model, settings);
			}else {
				success = addThroughLdap(model);
			}
			if(!success)
				return this.forward("igrp","LdapUser","index");
			
			/** End **/
		}
		return this.redirect("igrp","LdapUser","index");
		/*----#END-PRESERVED-AREA----*/
	}
	
	/*----#START-PRESERVED-AREA(CUSTOM_ACTIONS)----*/
	
	private boolean addThroughLdap(LdapUser model) {
		boolean flag = false;
		
		File file = new File(getClass().getClassLoader().getResource(new Config().getBasePathConfig() + File.separator + "ldap" + File.separator + "ldap.xml").getPath());
		
		LdapInfo ldapinfo = JAXB.unmarshal(file, LdapInfo.class);
		NosiLdapAPI ldap = new NosiLdapAPI(ldapinfo.getUrl(), ldapinfo.getUsername(), ldapinfo.getPassword(), ldapinfo.getBase(), ldapinfo.getAuthenticationFilter(), ldapinfo.getEntryDN());
		LdapPerson person = new LdapPerson(); 
		person.setCn(model.getCommon_name().trim()); 
		person.setSn(model.getSurname().trim());
		try {
			String aux = model.getEmail_1().trim().split("@")[0];
			person.setUid(aux);
		}catch(Exception e) {
			person.setUid(model.getEmail_1().trim());
		}
		person.setMail(model.getEmail_1().trim());
		person.setDisplayName(model.getCommon_name().trim() + " " + model.getSurname().trim());
		person.setGivenName(model.getCommon_name().trim() + " " + model.getSurname().trim());
		ldap.createUser(person);
		String error = ldap.getError();
		if(error != null)
			Core.setMessageError(error);
		else {
			flag = true;
			Core.setMessageSuccess(gt("Utilizador registado com sucesso."));
		}
		return flag;
	}
	
	private boolean addThroughIds(LdapUser model, Object ...obj) {
		boolean flag = false;
		
		Properties settings = (Properties) obj[0];
		
		try {
			String uri = settings.getProperty("ids.wso2.RemoteUserStoreManagerService-wsdl-url");
			URL url =  new URL(uri);
	        WSO2UserStub.disableSSL();
	        WSO2UserStub stub = new WSO2UserStub(new RemoteUserStoreManagerService(url));
	        stub.applyHttpBasicAuthentication(settings.getProperty("ids.wso2.admin-usn"), settings.getProperty("ids.wso2.admin-pwd"), 2);
	        
	        AddUser addUser = new AddUser();
            
           addUser.setRequirePasswordChange(false);
           
           // aux = model.getEmail_1().trim().split("@")[0];
           addUser.setUserName(new JAXBElement<String>(new QName(uri, "userName"), String.class, model.getEmail_1().trim()));
           
           addUser.setCredential(new JAXBElement<String>(new QName(uri, "credential"), String.class, "Pa$$w0rd"));
           addUser.setProfileName(new JAXBElement<String>(new QName(uri, "profileName"), String.class, "default"));
         
           ClaimValue email = new ClaimValue();
           email.setClaimURI(new JAXBElement<String>(new QName(uri, "claimURI"), String.class, "http://wso2.org/claims/emailaddress"));
           
           email.setValue(new JAXBElement<String>(new QName(uri, "value"), String.class, model.getEmail_1().trim()));
           
           ClaimValue cn = new ClaimValue();
           cn.setClaimURI(new JAXBElement<String>(new QName(uri, "claimURI"), String.class, "http://wso2.org/claims/fullname"));
           cn.setValue(new JAXBElement<String>(new QName(uri, "value"), String.class, model.getCommon_name().trim()));
           
           ClaimValue sn = new ClaimValue();
           sn.setClaimURI(new JAXBElement<String>(new QName(uri, "claimURI"), String.class, "http://wso2.org/claims/lastname"));
           sn.setValue(new JAXBElement<String>(new QName(uri, "value"), String.class, model.getSurname().trim()));
           
           addUser.getClaims().addAll(Arrays.asList(cn, email, sn));
          
          try {
        	  stub.getOperations().addUser(addUser);
          }catch(Exception e) {
        	  e.printStackTrace();
          }
          
         int httpStatusCode = -1;
         try {
        	 httpStatusCode = stub.getHttpResponseStatusCode(); // bug due version of jax-ws client TomEE 
         }catch (NullPointerException e) {
        	 httpStatusCode = 200;
         }
           
          if(httpStatusCode == 202 || httpStatusCode == 200) {
        	  Core.setMessageSuccess(gt("Utilizador registado com sucesso."));
        	  flag = true;
          }else 
        	  Core.setMessageError();
		}catch(Exception e) {
			e.printStackTrace();
			 Core.setMessageError();
		}
		
		return flag;
	}
	
	public Response actionIndex_(@RParam(rParamName = "email") String email) throws IOException, IllegalArgumentException, IllegalAccessException{
	
		LdapUser model = new LdapUser();
		
		if(Igrp.getInstance().getRequest().getMethod().equalsIgnoreCase("post")){
			model.load();
			
			boolean success = updateNUsingIds(model, email);
			
			if(success) return redirect("igrp", "ldap-user", "index_&email=" + email.trim());	
		}
		
		LdapUserView view = new LdapUserView(model);
		view.btn_gravar.setLink("igrp", "ldap-user", "index_&email=" + email);
		
		return this.renderView(view);
	}
	
//	private boolean updateUsingIds() {
//		boolean flag = false;
//		
//		return flag;
//	}
	
	private boolean updateNUsingIds(LdapUser model, String email) {
		boolean flag = false;
		
		File file = new File(getClass().getClassLoader().getResource(new Config().getBasePathConfig() + File.separator + "ldap" + File.separator + "ldap.xml").getPath());
		
		LdapInfo ldapinfo = JAXB.unmarshal(file, LdapInfo.class);
		NosiLdapAPI ldap = new NosiLdapAPI(ldapinfo.getUrl(), ldapinfo.getUsername(), ldapinfo.getPassword(), ldapinfo.getBase(), ldapinfo.getAuthenticationFilter(), ldapinfo.getEntryDN());
		
		LdapPerson person = ldap.getUserLastInfo(email.trim());
		String uid = "";
		
		if(person != null) {
			model.setCommon_name(person.getCn());
			model.setSurname(person.getSn());
			model.setEmail_1(person.getMail());
			uid = person.getUid();
		}else {
			Core.setMessageError(gt("Ocorreu um erro. O utilizador pode não existir."));
			return false;
		}
		
		person.setCn(model.getCommon_name().trim());
		person.setSn(model.getSurname().trim());
		person.setMail(model.getEmail_1().trim());
		person.setDisplayName(person.getCn() + " " + person.getSn());
		person.setGivenName(person.getCn() + " " + person.getSn());
		
		try {
			person.setUid(model.getEmail_1().trim().split("@")[0]);
		}catch(Exception e) {
			person.setUid(model.getEmail_1().trim());
		}
		ldap.updateUser(person, uid);
		String error = ldap.getError();
		if(error != null) {
			Core.setMessageError(gt("Ocorreu um erro. LDAP error: ") + error);
		}else {
			String oldName = ldapinfo.getEntryDN().replaceAll(":_placeholder", uid);
			String newName = ldapinfo.getEntryDN().replaceAll(":_placeholder", person.getUid());
			
			ldap.renameEntry(oldName, newName);
			error = ldap.getError();
			
			if(error != null) {
				Core.setMessageSuccess(gt("Ocorreu um erro. LDAP error: " ) + error);
			}else{
				User u = new User().find().andWhere("email", "=", email).one();
				if(u != null) {
					u.setName(person.getDisplayName());
					u.setEmail(person.getMail());
					u.setUser_name(person.getUid());
					u.setUpdated_at(System.currentTimeMillis());
					u = u.update();
					if(u != null) {
						Core.setMessageSuccess(gt("Utilizador atualizado com sucesso."));
						flag = true;
					}else 
						Core.setMessageSuccess(gt("Ocorreu um erro. Favor contactar o administrador. ")); 
				}else {
					Core.setMessageError(gt("Utilizador inválido. "));
				}
			}
		}
		return flag;
	}
	
	private Properties loadIdentityServerSettings() {
		
		String path = new Config().getBasePathConfig() + File.separator + "common";
		String fileName = "main.xml";
		File file = new File(getClass().getClassLoader().getResource(path + File.separator + fileName).getPath().replaceAll("%20", " "));
		
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
