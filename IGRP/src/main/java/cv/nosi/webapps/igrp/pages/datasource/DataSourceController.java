package cv.nosi.webapps.igrp.pages.datasource;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

import cv.nosi.core.gui.components.IGRPForm;
import cv.nosi.core.gui.components.IGRPTable;
import cv.nosi.core.gui.fields.Field;
import cv.nosi.core.gui.fields.TextField;
import cv.nosi.core.webapp.Igrp;
import cv.nosi.core.webapp.Response;
import cv.nosi.core.webapp.mvc.Controller;
import cv.nosi.core.webapp.util.Core;
import cv.nosi.core.webapp.util.helpers.datasource.DataSourceHelpers;
import cv.nosi.core.webapp.workflow.activit.rest.business.ProcessDefinitionIGRP;
import cv.nosi.core.webapp.workflow.activit.rest.entities.ProcessDefinitionService;
import cv.nosi.core.webapp.workflow.activit.rest.entities.TaskService;
import cv.nosi.core.webapp.workflow.activit.rest.services.ProcessDefinitionServiceRest;
import cv.nosi.core.webapp.workflow.activit.rest.services.TaskServiceRest;
import cv.nosi.core.xml.XMLExtractComponent;
import cv.nosi.core.xml.XMLWritter;
import cv.nosi.webapps.igrp.dao.Action;
import cv.nosi.webapps.igrp.dao.Application;
import cv.nosi.webapps.igrp.dao.Config_env;
import cv.nosi.webapps.igrp.dao.Organization;
import cv.nosi.webapps.igrp.dao.ProfileType;
import cv.nosi.webapps.igrp.dao.RepSource;
import cv.nosi.webapps.igrp.dao.User;
		
public class DataSourceController extends Controller {
	public Response actionIndex() throws IOException, IllegalArgumentException, IllegalAccessException{
		DataSource model = new DataSource();
		model.load();
		DataSourceView view = new DataSourceView();
		/*----#gen-example
		  EXAMPLES COPY/PASTE:
		  INFO: Core.query(null,... change 'null' to your db connection name, added in Application Builder.
		view.data_source.setQuery(Core.query(null,"SELECT 'id' as ID,'name' as NAME "));
		view.tipo.setQuery(Core.query(null,"SELECT 'id' as ID,'name' as NAME "));
		view.processo.setQuery(Core.query(null,"SELECT 'id' as ID,'name' as NAME "));
		view.etapa.setQuery(Core.query(null,"SELECT 'id' as ID,'name' as NAME "));
		  ----#gen-example */
		/*----#start-code(index)----*/	
	
		
		view.tipo.setQuery(Core.query(null,"SELECT 'Task' as ID,'Etapa' as NAME UNION "
				+ "SELECT 'Object' as ID,'Objeto' as NAME UNION "
				+ "SELECT 'Page' as ID,'Pagina' as NAME UNION "
				+ "SELECT 'Query' as ID,'Query' as NAME"),"--- Selecionar Tipo Data Source ---");
		
		view.processo.setVisible(false);
		view.query.setVisible(false);
		view.servico.setVisible(false);
		view.etapa.setVisible(false);
		view.pagina.setVisible(false);
		view.objecto.setVisible(false);
		model.setId_env(Core.isNotNull(model.getId_env())?model.getId_env():Core.getParam("id_env"));
		ProcessDefinitionIGRP processRest =new ProcessDefinitionIGRP();
		if(Core.isNotNull(model.getId_env())) {		
			//			If just one data source exist, will be choosed
			final Map<Object, Object> listDSbyEnv = new Config_env().getListDSbyEnv(Integer.parseInt(model.getId_env()));
			if(listDSbyEnv!=null && listDSbyEnv.size()==2) {				
				model.setData_source(listDSbyEnv.keySet().stream().filter(a->a!=null).findFirst().get().toString());
			}
			String id = Core.getParam("p_datasorce_app");
			String ichange = Core.getParam("ichange");
			if(Core.isNotNull(id)){
				RepSource rep = new RepSource().findOne(Integer.parseInt(id));
				model.setNome(rep.getName());
				model.setObjecto(rep.getType_name().equalsIgnoreCase("object")?rep.getType_query():"");
				model.setQuery(rep.getType_name().equalsIgnoreCase("query")?rep.getType_query():"");
				model.setTipo(rep.getType_name());
				if(rep.getConfig_env()!=null) {
					model.setData_source(""+rep.getConfig_env().getId());
				}
				if(Core.isNull(ichange)) {
					model.setEtapa(rep.getTaskid());
					model.setProcesso(rep.getProcessid());
				}
				if(rep.getType_name().equalsIgnoreCase("page")) {
					Action ac = new Action().findOne(rep.getType_fk());
					model.setPagina(ac.getPage_descr());
					model.setId_pagina(ac.getId());
				}
			}
			Application app = Core.findApplicationById(Core.toInt(model.getId_env()));		
			
			view.data_source.setValue(listDSbyEnv);
			
			//habilita botao de acordo com tipo de objeto
			if(Core.isNotNull(model.getTipo())){
				if(model.getTipo().equalsIgnoreCase("object")){
					view.objecto.setVisible(true);
				}else if(model.getTipo().equalsIgnoreCase("page")){
					view.pagina.setVisible(true);
					view.pagina.setLookup("igrp","LookupListPage","index");
					view.pagina.addParam("p_prm_target","_blank");
					view.pagina.addParam("p_id_pagina", "id");
					view.pagina.addParam("p_pagina", "descricao");
					view.pagina.addParam("p_env_fk", model.getId_env());
				}else if(model.getTipo().equalsIgnoreCase("query")){
					view.query.setVisible(true);
				}else
					if(model.getTipo().equalsIgnoreCase("task")){
					view.etapa.setVisible(true);
					view.processo.setVisible(true);
					if(app!=null) {
						//Get Process Type
						view.processo.setValue(processRest.mapToComboBoxByKey(app.getDad()));
					}
				}
			}
			
			if(Core.isNotNull(id)){
				view.btn_gravar.setLink("gravar&p_datasorce_app="+id);
			}
			view.btn_fechar.setVisible(false);
			if(Core.isNotNull(model.getProcesso()) && app!=null) {
				view.etapa.setValue(processRest.mapToComboBoxByProcessKey(model.getProcesso(),app.getDad()));
			}
		}
		
		view.nome.setVisible(true);
		
		/*----#end-code----*/
		view.setModel(model);
		return this.renderView(view);	
	}
	
	public Response actionGravar() throws IOException, IllegalArgumentException, IllegalAccessException{
		DataSource model = new DataSource();
		model.load();
		/*----#gen-example
		  EXAMPLES COPY/PASTE:
		  INFO: Core.query(null,... change 'null' to your db connection name, added in Application Builder.
		 this.addQueryString("p_id","12"); //to send a query string in the URL
		 return this.forward("igrp","DataSource","index", this.queryString()); //if submit, loads the values
		  ----#gen-example */
		/*----#start-code(gravar)----*/		
		
  		this.addQueryString("p_id_env",model.getId_env());
		if(Core.isNotNull(model.getId_env())){
			Application app = Core.findApplicationById(Core.toInt(model.getId_env()));
			Integer id = Core.getParamInt("p_datasorce_app");
			RepSource rep = new RepSource();
			if(Core.isNotNullOrZero(id))
				rep = rep.findOne(id);
			
			rep.setName(model.getNome());
			rep.setType(model.getTipo());
			rep.setType_name(model.getTipo());
			rep.setType_query(model.getQuery());
			if((model.getTipo().equalsIgnoreCase("object") || model.getTipo().equalsIgnoreCase("query")) && Core.isNull(model.getData_source())) {
				Core.setMessageError("Por favor selecione uma data source");
				return this.forward("igrp","DataSource","index", this.queryString());
			}
			if(Core.isNotNull(model.getData_source()))
				rep.setConfig_env(new Config_env().findOne(Integer.parseInt(model.getData_source())));
			if(model.getTipo().equalsIgnoreCase("object")){
				rep.setType_query(model.getObjecto());
			}
			if(model.getTipo().equalsIgnoreCase("page")){				
				rep.setType_fk(model.getId_pagina());
			}
			if(model.getTipo().equalsIgnoreCase("object") || model.getTipo().equalsIgnoreCase("query")){
				String query = rep.getType_query();
				query = rep.getType().equalsIgnoreCase("object")?"SELECT * FROM "+query:query;
				if(!Core.validateQuery(rep.getConfig_env(),query)){
					Igrp.getInstance().getFlashMessage().addMessage("error","Query Invalido");
					return this.forward("igrp","DataSource","index&id_env="+model.getId_env());
				}
			}
		
			if(model.getTipo().equalsIgnoreCase("task")) {
				ProcessDefinitionService p = new ProcessDefinitionServiceRest().getLatestProcessDefinitionByKey(model.getProcesso(),app.getDad());				
				//if(p.filterAccess(p)) {
					rep.setProcessid(p.getKey());
					rep.setTaskid(model.getEtapa());
					rep.setApplication_source(new Application().findOne(Core.toInt(p.getTenantId())));
					List<TaskService> task = new TaskServiceRest().getTasksByProcessKey(model.getProcesso(),app.getDad()).stream().filter(n->n.getTaskDefinitionKey().equalsIgnoreCase(model.getEtapa())).collect(Collectors.toList());
					rep.setFormkey((task!=null && !task.isEmpty())?task.get(0).getFormKey():"");
				/*}else {
					throw new IOException(Core.NO_PERMITION_MSG);
				}*/
			}
			
			rep.setApplication(app);
			rep.setStatus(1);
			rep.setApplication_source(app);
			Date dt = new Date(System.currentTimeMillis());			
			rep.setDt_updated(dt);
			User user = Core.getCurrentUser();		
			rep.setUser_updated(user);
			
			if(Core.isNotNullOrZero(id)){			
				rep = rep.update();
			}else{
				rep.setDt_created(dt);
				rep.setUser_created(user);
				rep = rep.insert();
			}
			if(rep!=null)
				Core.setMessageSuccess();
		}else{
			Core.setMessageError();
			return this.forward("igrp","DataSource","index", this.queryString());
		}
		/*----#end-code----*/
		return this.redirect("igrp","DataSource","index", this.queryString());	
	}
	
	public Response actionFechar() throws IOException, IllegalArgumentException, IllegalAccessException{
		/*----#start-code(fechar)----*/		
		
		/*----#end-code----*/
		
		return this.redirect("igrp","datasource","index");	
	}
	
/*----#start-code(custom_actions)----*/
	
	//Print data source in xml format
	public Response actionGetDataSource() throws IOException{
		String [] p_id = Igrp.getInstance().getRequest().getParameterValues("p_id");
		String p_template_id = Igrp.getInstance().getRequest().getParameter("p_template_id");
		XMLWritter xml = new XMLWritter();
		xml.startElement("rows");
		if(p_id!=null && p_id.length > 0){
			for(String id:p_id){
				xml.addXml(this.loadDataSource((int)Float.parseFloat(id),(p_template_id!=null && !p_template_id.equals(""))?(int)Float.parseFloat(p_template_id):0));
			}
		}
		xml.endElement();
		return this.renderView(xml.toString());
	}

	//Load data source
	private String loadDataSource(int id,int template_id) {
		DataSourceHelpers dsh = new DataSourceHelpers();
		RepSource rep = new RepSource().findOne(id);
		if(rep!=null){
			Set<Properties> columns = new HashSet<>();
			if(rep.getType().equalsIgnoreCase("object") || rep.getType().equalsIgnoreCase("query")){
				String query = rep.getType_query();
				query = rep.getType().equalsIgnoreCase("object")?"SELECT * FROM "+query:query;
				columns = dsh.getColumns(rep,template_id,query);
				return this.transformToXml(rep,columns);
			}else if(rep.getType().equalsIgnoreCase("page")){
				Action ac = new Action().findOne(rep.getType_fk());
				return this.getDSPageOrTask(rep,ac.getApplication().getDad(), ac.getPage(), "index",ac.getPage_descr());
			}else if(rep.getType().equalsIgnoreCase("task")) {
				return this.getDSPageOrTask(rep,rep.getApplication_source().getDad(), rep.getFormkey(), "index","Task: "+rep.getTaskid());
			}
		}
		return null;
	}
	
	public String getDSPageOrTask(RepSource rep,String app,String page,String action,String title) {
		XMLWritter xml = new XMLWritter();
		xml.startElement("content");
		xml.writeAttribute("uuid", rep.getSource_identify());
		xml.setElement("title", title);
		this.addQueryString("current_app_conn", app);
		String content = this.call(app,page,action,this.queryString()).getContent();
		Core.removeAttribute("current_app_conn");
		content = XMLExtractComponent.extractXML(content);
		List<Field> list = this.getDefaultFields();
		if(rep.getType().equalsIgnoreCase("task")) {
			list = getDefaultFieldsWithProc();
		}
		xml.addXml(this.getDefaultForm(list));
		xml.addXml(content);
		xml.endElement();
		return xml.toString();
	}
	
	public List<Field> getDefaultFieldsWithProc(){
		
		List<Field> list = this.getDefaultFields();
		Field p_prm_definitionid = new TextField(null, "p_prm_definitionid");
		p_prm_definitionid.setLabel(Core.gt("id processo"));
		p_prm_definitionid.setValue(Core.getParam("report_p_prm_definitionid"));
		list.add(p_prm_definitionid);
		return list;
	}
	
	public List<Field> getDefaultFields(){
		User currentUser = Core.getCurrentUser(); 
		Application currentApp = Core.getCurrentApp(); 
		Integer currentOrgId = Core.getCurrentOrganization(); 
		Integer currentProf = Core.getCurrentProfile(); 
		
		List<Field> fields = new ArrayList<>();
		Field data_atual = new TextField(null, "p_data_atual");
		data_atual.setLabel(Core.gt("data atual"));
		data_atual.setValue(Core.getCurrentDate());
		
		Field user_atual = new TextField(null,"p_user_atual");
		user_atual.setLabel(Core.gt("user atual"));
		user_atual.setValue(currentUser != null ? currentUser.getName() : "");
		
		Field email_atual = new TextField(null,"p_email_atual");
		email_atual.setLabel(Core.gt("email atual"));
		email_atual.setValue(currentUser != null ? currentUser.getEmail() : "");
		
		Field application = new TextField(null,"p_application");
		application.setLabel(Core.gt("aplicação atual"));
		application.setValue(currentApp != null ? currentApp.getName() : "");

		Field organization = new TextField(null,"p_organization");
		organization.setLabel(Core.gt("orgânica atual"));
		Organization org = new Organization().findOne(currentOrgId != null ? currentOrgId : -1);
		if(org!=null)
			organization.setValue(org.getName());

		Field profile = new TextField(null,"p_profile");
		profile.setLabel(Core.gt("perfil atual"));
		ProfileType prof = new ProfileType().findOne(currentProf != null ? currentProf : -1); 
		if(prof!=null)
			profile.setValue(prof.getDescr());
		
		fields.add(user_atual);
		fields.add(data_atual);
		fields.add(email_atual);
		fields.add(organization);
		fields.add(profile);
		fields.add(application);
		return fields;
	}
	
	public String getDefaultForm(List<Field> fields) {
		IGRPForm formProcess = new IGRPForm("default_form_report");
		fields.stream().forEach(formProcess::addField);
		return formProcess.toString();
	}
	//Transform columns to xml
	private String transformToXml(RepSource rep,Set<Properties> columns) {
		XMLWritter xml = new XMLWritter();
		xml.startElement("content");
			xml.writeAttribute("uuid", rep.getSource_identify());
			xml.setElement("title", rep.getName());
			xml.setElement("data_source_id", rep.getId());
			IGRPForm form = new IGRPForm("form");
			IGRPTable table = new IGRPTable("table");
			for (Properties p : columns) {
				Field f = new TextField(null,p.getProperty("tag"));
				f.propertie().add("name",p.getProperty("tag"));
				f.propertie().add("key",p.getProperty("key"));
				f.propertie().add("java-type", p.getProperty("type", "String"));
				f.setLabel(p.getProperty("tag"));
				form.addField(f);
				table.addField(f);
			}			
			xml.addXml(this.getDefaultForm(this.getDefaultFields()));
			xml.addXml(form.toString());
			xml.addXml(table.toString());			
		xml.endElement();
		return xml.toString();
	}
	
	/*----#end-code----*/
}
