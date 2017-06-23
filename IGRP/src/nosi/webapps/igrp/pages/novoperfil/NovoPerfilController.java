/*-------------------------*/

/*Create Controller*/

package nosi.webapps.igrp.pages.novoperfil;
import nosi.core.webapp.Controller;
import nosi.core.webapp.FlashMessage;
import nosi.core.webapp.Igrp;
import nosi.core.webapp.RParam;
import nosi.core.webapp.Response;

import java.io.IOException;
import nosi.webapps.igrp.dao.ProfileType;
import nosi.webapps.igrp.dao.Application;
import nosi.webapps.igrp.dao.Organization;


public class NovoPerfilController extends Controller {		

	public Response actionIndex() throws IOException, IllegalArgumentException, IllegalAccessException{
		NovoPerfil model = new NovoPerfil();

		NovoPerfilView view = new NovoPerfilView(model);
			
		if(Igrp.getInstance().getRequest().getMethod().equals("POST")){
			
			model.load();
			
			ProfileType pt = new ProfileType();
		
			pt.setCode(model.getCodigo());
			pt.setDescr(model.getDescricao());
			pt.setOrg_fk(model.getOrganica());
			pt.setSelf_fk(model.getPerfil());
			pt.setStatus(model.getActivo());
			pt.setEnv_fk(model.getAplicacao());
		
			if(pt.insert()){
				Igrp.getInstance().getFlashMessage().addMessage("success","Opera��o efetuada com sucesso");
				return this.redirect("igrp", "novo-perfil", "index");
			}else{
				Igrp.getInstance().getFlashMessage().addMessage("error","Falha ao tentar efetuar esta opera��o");				
			}
			
		}
		view.aplicacao.setValue(new Application().getListApps());
		view.perfil.setValue(new ProfileType().getListProfiles());
		view.organica.setValue(new Organization().getListOrganizations());
		return this.renderView(view);
	}
	
	public Response actionEditar(@RParam(rParamName="p_id")String id) throws IOException, IllegalArgumentException, IllegalAccessException{
		
		NovoPerfil model = new NovoPerfil();
		ProfileType p = new ProfileType();
		p.setId(Integer.parseInt(id));
		p = (ProfileType) p.getOne();
		
		model.setCodigo(p.getCode());
		model.setDescricao(p.getDescr());
		model.setAplicacao(p.getEnv_fk());
		model.setOrganica(p.getOrg_fk());
		model.setActivo(p.getStatus());
		model.setPerfil(p.getSelf_fk());
		
		if(Igrp.getInstance().getRequest().getMethod().equals("POST")){
			model.load();
			
			p.setCode(model.getCodigo());
			p.setDescr(model.getDescricao());
			p.setEnv_fk(model.getAplicacao());
			p.setOrg_fk(model.getOrganica());
			p.setSelf_fk(model.getPerfil());
			p.setStatus(model.getActivo());
			
			if(p.update()){
				Igrp.getInstance().getFlashMessage().addMessage(FlashMessage.SUCCESS, "Perfil atualizado com sucesso.");
				return this.redirect("igrp", "novo-perfil", "editar", new String[]{"p_id"}, new String[]{p.getId() + ""});
			}
			else
				Igrp.getInstance().getFlashMessage().addMessage(FlashMessage.ERROR, "Erro ao atualizar o perfil.");
			
		}
		
		NovoPerfilView view = new NovoPerfilView(model);
		
		view.sectionheader_1_text.setValue("Gest�o de Perfil - Atualizar");
		
		view.btn_gravar.setLink("editar&p_id="+id);
		
		view.aplicacao.setValue(new Application().getListApps());
		view.perfil.setValue(new ProfileType().getListProfiles());
		view.organica.setValue(new Organization().getListOrganizations());
		return this.renderView(view);
	}
	
}
