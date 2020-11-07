package nosi.core.webapp;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.json.JSONArray;

import nosi.core.webapp.security.EncrypDecrypt;
import nosi.core.webapp.security.PagesScapePermission;
import nosi.core.webapp.security.Permission;
import nosi.core.webapp.security.SecurtyCallPage;
import nosi.webapps.igrp.pages.login.LoginController;
/**
 * @author Marcel Iekiny
 * Apr 18, 2017
 */
public class User implements Component{
	
	private Identity identity;
	private int expire; // for authentication via cookie  
	
	//private static Map<String, HttpSession> users = Collections.synchronizedMap(new HashMap<String, HttpSession>());
	
	public User(){}
	
	public boolean login(Identity identity, int expire){ // Make login and authenticate the user ... using session and cookies(remenberMe implementation purpose) 
		if(identity == null || identity.getAuthenticationKey() == null || identity.getAuthenticationKey().isEmpty())
			return false;
		try {
			this.identity = identity;
			new Permission().changeOrgAndProfile("tutorial");
			// Create the session context
			JSONArray json =  new JSONArray();
			json.put(this.identity.getIdentityId());
			json.put(this.identity.getAuthenticationKey() + "");
			Igrp.getInstance().getRequest().getSession(true).setAttribute("_identity-igrp", json.toString()); 
			if(expire > 0) { 
				this.expire = expire;
				this.sendCookie(Base64.getEncoder().encodeToString(json.toString().getBytes())); //  send a cookie to the end user 
			}
		return true;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	private boolean checkSessionContext(){
		String aux = (String) Igrp.getInstance().getRequest().getSession(true).getAttribute("_identity-igrp");
		if(aux == null || aux.isEmpty()) return false;
		try {
			JSONArray json = new JSONArray(aux);
			int identityId = json.getInt(0);
			String authenticationKey = json.getString(1);
			nosi.webapps.igrp.dao.User user = new nosi.webapps.igrp.dao.User();
			user = user.findIdentityById(identityId);
			
			if(user!=null && authenticationKey.equals(user.getAuth_key())) {
				this.identity = (Identity) user;
			return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	private void checkCookieContext() {
		Cookie aux = null;
		Cookie []allCookies = Igrp.getInstance().getRequest().getCookies();
		if(allCookies != null)
			for(Cookie obj : allCookies)
				if(obj.getName().equals("_identity-igrp"))
					aux = obj;
		if(aux == null || aux.getValue().isEmpty()) return;
		String value = new String(Base64.getDecoder().decode(aux.getValue()));
		try {
			JSONArray json = new JSONArray(value);
			int identityId = json.getInt(0);
			String authenticationKey = json.getString(1);
			nosi.webapps.igrp.dao.User user = new nosi.webapps.igrp.dao.User();
			user = user.findIdentityById(identityId);
			if(user!=null && user.getId()!=0 && authenticationKey.equals(user.getAuth_key())) {
				// create the session context here
				Igrp.getInstance().getRequest().getSession(true).setAttribute("_identity-igrp", json.toString());
				this.identity = (Identity) user;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void sendCookie(String value) { 
		Cookie aux = new Cookie("_identity-igrp", value);
		aux.setMaxAge(this.expire);
		aux.setHttpOnly(true);
		Igrp.getInstance().getResponse().addCookie(aux);
	}
	
	public boolean isAuthenticated(){
		return this.identity != null;
	}
	
	public boolean logout(){ // Reset all login session/cookies information
		try {
			  HttpSession theSession = Igrp.getInstance().getRequest().getSession(true);
			    // print out the session id
			    if( theSession != null ) {
			      synchronized( theSession ) {
			        // invalidating a session destroys it
			        theSession.invalidate();
			      }
			    }
			// destroy the cookie ... make sure this.expire == 0 
			sendCookie("");
			return true;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public Identity getIdentity(){
		return this.identity;
	}

	@Override
	public void init(HttpServletRequest request) {
		String loginUrl = "igrp/login/login";
		String aux = request.getParameter("r") != null ? request.getParameter("r").toString() : loginUrl; 
		
		/* test the login page (TOO_MANY_REQUEST purpose) */ 
		boolean isLoginPage = aux.equals(loginUrl); 
		
		if(SecurtyCallPage.isPublic(aux) && !isLoginPage) return; 
		
		if(aux != null && !isLoginPage && !PagesScapePermission.PAGES_SCAPE_ENCRYPT.contains(aux.toLowerCase())) {
			aux = EncrypDecrypt.decrypt(aux);
		}
		
		if(!this.checkSessionContext() && !isLoginPage){
			try {
				//nosi.core.webapp.helpers.Route.remember(); // remember the url that was requested by the client ... 
				this.checkCookieContext();
				// Anyway, go to login page 
				LoginController controller = new LoginController();
				Response response = controller.actionGoToLogin();
				controller.setResponseWrapper(response);
				Igrp.getInstance().setCurrentController(controller);
				Igrp.getInstance().die();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/*private boolean checkHttpClientRequest() {
		try {
			String customHeader = Igrp.getInstance().getRequest().getHeader("X-IGRP-HTTPCLIENT");
			
			String credentials = Igrp.getInstance().getRequest().getHeader("Authorization");
			// ZGVtbzpkZW1v 
			credentials = credentials.replaceFirst("Basic ", "");
			String decodeString = new String(Base64.getDecoder().decode(credentials));
			
			StringTokenizer token = new StringTokenizer(decodeString, ":");
			String username = token.nextToken();
			String password = token.nextToken();
			
			nosi.webapps.igrp.dao.User user = (nosi.webapps.igrp.dao.User) new nosi.webapps.igrp.dao.User().findIdentityByUsername(username);
			
			if(customHeader == null || !customHeader.equals("1") || user == null || !user.getPass_hash().equals(encryptToHash(password, "MD5"))) { 
				Igrp.getInstance().getResponse().setStatus(401); // 401 status code -> Authentication 
				return false;
			}
			
			this.identity = user;
			
			// Create the session context 
			JSONArray json =  new JSONArray();
			json.put(this.identity.getIdentityId());
			json.put(this.identity.getAuthenticationKey() + "");
			Igrp.getInstance().getRequest().getSession(true).setAttribute("_identity-igrp", json.toString());
			
		}catch(Exception e) {
			return false;
		}
		return true;
	}*/

	@Override
	public void destroy() {
		// not set yet
	}
	
	public static String encryptToHash(String target, String algorithm/* MD5 or SHA1 */){
		String result = "";
		try {
			result = new BigInteger(1, MessageDigest.getInstance(algorithm).digest(target.getBytes())).toString(16/*Hexadecimal codification*/);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static String generateAuthenticationKey() {
		return java.util.UUID.randomUUID().toString().replaceAll("-", "");
	}
	
	public static String generateActivationKey() {
		return Base64.getUrlEncoder().encodeToString(((System.currentTimeMillis() + 1000*7*24*3600) + "").getBytes()); // 7 days 
	}
	
	public static String generatePasswordResetToken() {
		return Base64.getUrlEncoder().encodeToString(((java.util.UUID.randomUUID().toString().replaceAll("-", "") + "_" + (System.currentTimeMillis() + 1000*10*60)) + "").getBytes()); // 10 min.  
	}
	
}
