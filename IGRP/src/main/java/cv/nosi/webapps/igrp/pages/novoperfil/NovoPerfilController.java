package cv.nosi.webapps.igrp.pages.novoperfil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

import cv.nosi.core.config.ConfigApp;
import cv.nosi.core.webapp.Igrp;
import cv.nosi.core.webapp.Response;
import cv.nosi.core.webapp.mvc.Controller;
import cv.nosi.core.webapp.util.Core;
import cv.nosi.core.webapp.util.helpers.database.QueryInterface;
import cv.nosi.core.webapp.util.helpers.database.ResultSet;
import cv.nosi.core.webapp.workflow.activit.rest.entities.GroupService;
import cv.nosi.core.webapp.workflow.activit.rest.services.GroupServiceRest;
import cv.nosi.webapps.igrp.dao.Action;
import cv.nosi.webapps.igrp.dao.Application;
import cv.nosi.webapps.igrp.dao.Menu;
import cv.nosi.webapps.igrp.dao.Organization;
import cv.nosi.webapps.igrp.dao.Profile;
import cv.nosi.webapps.igrp.dao.ProfileType;
		
public class NovoPerfilController extends Controller {
	public Response actionIndex() throws IOException, IllegalArgumentException, IllegalAccessException{
		NovoPerfil model = new NovoPerfil();
		model.load();
		NovoPerfilView view = new NovoPerfilView();
		/*----#gen-example
		  EXAMPLES COPY/PASTE:
		  INFO: Core.query(null,... change 'null' to your db connection name, added in Application Builder.
		view.aplicacao.setQuery(Core.query(null,"SELECT 'id' as ID,'name' as NAME "));
		view.organica.setQuery(Core.query(null,"SELECT 'id' as ID,'name' as NAME "));
		view.perfil_pai.setQuery(Core.query(null,"SELECT 'id' as ID,'name' as NAME "));
		view.primeira_pagina.setQuery(Core.query(null,"SELECT 'id' as ID,'name' as NAME "));
		  ----#gen-example */
		/*----#start-code(index)----*/

		String dad = Core.getCurrentDad();
		if (!"igrp".equalsIgnoreCase(dad) && !"igrp_studio".equalsIgnoreCase(dad)) {
			model.setAplicacao(Core.findApplicationByDad(dad).getId());
			view.aplicacao.propertie().add("disabled", "true");
		}

		model.setActivo(1);
		view.aplicacao.setValue(new Application().getListApps());
		view.organica.setValue(Core.isNotNullOrZero(model.getAplicacao())
				? new Organization().getListOrganizations(model.getAplicacao())
				: null);
		// Perfil pai/Parent profile ocult (not in use)
		//view.perfil_pai.setVisible(false);
		view.btn_gravar.setTitle("Adicionar");
		view.btn_gravar.addParameter("p_aplicacao", model.getAplicacao());

		if (Core.isNotNullOrZero(model.getAplicacao())) {
			view.primeira_pagina.setValue(new Menu().getListActionByOrg(model.getAplicacao(),model.getOrganica()));
			view.perfil_pai.setValue(model.getOrganica() != 0 ? new ProfileType().getListProfiles4Pai(model.getAplicacao(), model.getOrganica()): null);
		}
			

		/*----#end-code----*/
		view.setModel(model);
		return this.renderView(view);	
	}
	
	public Response actionGravar() throws IOException, IllegalArgumentException, IllegalAccessException{
		NovoPerfil model = new NovoPerfil();
		model.load();
		/*----#gen-example
		  EXAMPLES COPY/PASTE:
		  INFO: Core.query(null,... change 'null' to your db connection name, added in Application Builder.
		  this.addQueryString("p_id","12"); //to send a query string in the URL
		  return this.forward("igrp","NovoPerfil","index",this.queryString()); //if submit, loads the values
		  Use model.validate() to validate your model
		  ----#gen-example */
		/*----#start-code(gravar)----*/
		ProfileType pt = new ProfileType();

		pt.setCode(model.getCodigo() + "." + Core.findApplicationById(model.getAplicacao()).getDad());
		pt.setDescr(model.getNome());
		pt.setOrganization(Core.findOrganizationById(model.getOrganica()));

		
		 if(Core.isNotNull(model.getPerfil_pai())){
			 pt.setProfiletype(new ProfileType().findOne(model.getPerfil_pai()));
		 }else {
			 pt.setProfiletype(null);
		 }
		
		pt.setStatus(model.getActivo());
		pt.setApplication(Core.findApplicationById(model.getAplicacao()));
		pt.setFirstPage(new Action().findOne(model.getPrimeira_pagina()));
		if(Core.findProfileByCode(pt.getCode()) !=null) {
			Core.setMessageError("Código perfil duplicado");
			System.out.println("Código perfil duplicado");
			return this.forward("igrp", "NovoPerfil", "index", this.queryString());
		}
		else 
			pt = pt.insert();	
	
		if (pt != null && !pt.hasError()) {
			/*
			 * // Cria grupo e utilizadores no Activiti GroupService group = new
			 * GroupService(); group.setId(pt.getOrganization().getCode() + "." +
			 * pt.getCode()); group.setName(pt.getOrganization().getName() + " - " +
			 * pt.getDescr()); group.setType("assignment"); group.create(group);
			 */
			if (insertProfile(pt)) {
				Core.setMessageSuccess("Perfil criado com sucesso");
			} else {
				Core.setMessageError();
				return this.forward("igrp", "NovoPerfil", "index", this.queryString());
			}

		} else {
			Core.setMessageError();
			return this.forward("igrp", "NovoPerfil", "index", this.queryString());
		}

		return this.redirect("igrp", "NovoPerfil", "index", this.queryString());
		/*----#end-code----*/
			
	}
	
		
		
/*----#start-code(custom_actions)----*/

	private Boolean insertProfile(ProfileType pt) throws IOException {
		Profile prof = new Profile();
		prof.setUser(Core.getCurrentUser());
		prof.setType("PROF");
		prof.setType_fk(pt.getId());
		// ProfileType p = new ProfileType().findOne(Integer.parseInt(id));
		prof.setOrganization(pt.getOrganization());
		prof.setProfileType(pt);
		prof = prof.insert();
		return prof != null && !prof.hasError();
	}

	public Response actionEditar() throws IOException, IllegalArgumentException, IllegalAccessException {
		String idProf = Core.getParam("p_id");
		NovoPerfil model = new NovoPerfil();
		model.load();
		NovoPerfilView view = new NovoPerfilView();		
				
		ProfileType p = new ProfileType().findOne(Integer.parseInt(idProf));
		model.setCodigo(p.getCode());
		model.setNome(p.getDescr());
		model.setAplicacao(p.getApplication().getId());
	
		if (p.getOrganization() != null) {
			model.setOrganica(p.getOrganization().getId());
		}
		model.setActivo(p.getStatus());
		if (Core.isNotNull(p.getProfiletype())) {
			 model.setPerfil_pai(p.getProfiletype().getId());
		}
		if (p.getFirstPage() != null)
			model.setPrimeira_pagina(p.getFirstPage().getId());
		view.sectionheader_1_text.setValue("Gestão de Perfil - Atualizar");
		view.btn_gravar.setTitle("Gravar");
		view.btn_gravar.setLink("GravarEdicao");
		view.btn_gravar.addParameter("p_id", idProf).addParameter("p_aplicacao", model.getAplicacao());
		view.aplicacao.setValue(new Application().getListApps());

		if (Core.isNotNullOrZero(model.getAplicacao())) {
			view.primeira_pagina.setValue(new Menu().getListActionByOrg(model.getAplicacao(),model.getOrganica()));
			view.organica.setValue(new Organization().getListOrganizations(model.getAplicacao()));
			HashMap<String, String> listProfiles4Pai = new ProfileType().getListProfiles4Pai(model.getAplicacao(), model.getOrganica());
			listProfiles4Pai.remove(idProf);
			view.perfil_pai.setValue(model.getOrganica() != 0 ? listProfiles4Pai: null);			
		}	
		view.setModel(model);
		return this.renderView(view);
	}

	public Response actionGravarEdicao() throws IllegalArgumentException, IllegalAccessException, IOException {
		String idProf = Core.getParam("p_id");
		if (Igrp.getInstance().getRequest().getMethod().equals("POST")) {
			NovoPerfil model = new NovoPerfil();
			model.load();
			ProfileType p = Core.findProfileById(Integer.parseInt(idProf));
//			GroupServiceRest groupRest = new GroupServiceRest();
//			GroupService group = new GroupService();
//			groupRest.delete(p.getOrganization().getCode() + "." + p.getCode());
			p.setCode(model.getCodigo());
			p.setDescr(model.getNome());
			p.setOrganization(Core.findOrganizationById(model.getOrganica()));
		
			
			 if(Core.isNotNullOrZero(model.getPerfil_pai())){
				 p.setProfiletype(new ProfileType().findOne(model.getPerfil_pai()));
			}else {
				 p.setProfiletype(null);
			 }
			 
			p.setStatus(model.getActivo());
			p.setApplication(Core.findApplicationById(model.getAplicacao()));
			p.setFirstPage(new Action().findOne(model.getPrimeira_pagina()));
			p = p.update();
			if (p != null) {
//				group.setId(p.getOrganization().getCode() + "." + p.getCode());
//				group.setName(p.getOrganization().getName() + " - " + p.getDescr());
//				group.setType("assignment");
//				groupRest.create(group);
				Core.setMessageSuccess("Perfil atualizado com sucesso.");
			} else
				Core.setMessageError("Erro ao atualizar o perfil.");
			// this.addQueryString("p_id",id);
			return this.forward("igrp", "NovoPerfil", "editar");
		}

		Core.setMessageError("Invalid operation ...");
		return this.forward("igrp", "NovoPerfil", "editar");

	}

	/*----#end-code----*/
}
