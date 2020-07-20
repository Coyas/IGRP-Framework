package nosi.webapps.igrp.pages.etapaaccess;

import nosi.core.config.ConfigDBIGRP;
import nosi.core.webapp.Controller;
import java.io.IOException;
import nosi.core.webapp.Core;
import nosi.core.webapp.Response;
/*----#start-code(packages_import)----*/
import nosi.core.webapp.activit.rest.business.ProcessDefinitionIGRP;
import nosi.core.webapp.activit.rest.entities.ProcessDefinitionService;
import nosi.core.webapp.activit.rest.entities.TaskService;
import nosi.core.webapp.activit.rest.services.ResourceServiceRest;
import nosi.core.webapp.activit.rest.services.TaskServiceRest;
import nosi.core.webapp.helpers.CheckBoxHelper;
import nosi.webapps.igrp.dao.Organization;
import nosi.webapps.igrp.dao.ProfileType;
import nosi.webapps.igrp.dao.TaskAccess;
import nosi.webapps.igrp.dao.User;
import nosi.webapps.igrp.pages.etapaaccess.Etapaaccess.Table_1;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
/*----#end-code----*/
		
public class EtapaaccessController extends Controller { 
	
	public Response actionIndex() throws IOException, IllegalArgumentException, IllegalAccessException{
		Etapaaccess model = new Etapaaccess();
		model.load();
		EtapaaccessView view = new EtapaaccessView();
		/*----#gen-example
		  EXAMPLES COPY/PASTE:
		  INFO: Core.query(null,... change 'null' to your db connection name, added in Application Builder.
		model.loadTable_1(Core.query(null,"SELECT '1' as id,'Lorem mollit sed omnis iste' as descricao,'' as processid,'hidden-4256_fb3d' as task_description "));
		  ----#gen-example */
		/*----#start-code(index)----*/
		String type = Core.getParam("type");
		Integer orgProfId = Core.getParamInt("p_id");
		Integer orgId=null, profId= null;
		String userEmail = Core.getParam("userEmail");		
		if(type.compareTo("org")==0) {
			model.setTable_1(this.getOrganizationTasks(orgProfId));
			orgId = orgProfId;
		}
		if(type.compareTo("user")==0) {
			ProfileType prof = new ProfileType().findOne(orgProfId);
			model.setTable_1(this.getUserTasks(prof,new User().findIdentityByEmail(userEmail)));
			orgId = prof.getOrganization().getId();
			profId = prof.getId();
		}
		if(type.compareTo("prof")==0) {
			ProfileType prof = new ProfileType().findOne(orgProfId);
			model.setTable_1(this.getProfileTasks(prof));	
			orgId = prof.getOrganization().getId();		
		}
		view.btn_gravar.setLink("gravar&type="+type+"&orgProfId="+orgProfId+"&orgId="+orgId+"&profId="+profId+"&userEmail="+userEmail);
		/*----#end-code----*/
		view.setModel(model);
		return this.renderView(view);	
	}
	
	public Response actionGravar() throws IOException, IllegalArgumentException, IllegalAccessException{
		Etapaaccess model = new Etapaaccess();
		model.load();
		/*----#gen-example
		  EXAMPLES COPY/PASTE:
		  INFO: Core.query(null,... change 'null' to your db connection name, added in Application Builder.
		 this.addQueryString("p_id","12"); //to send a query string in the URL
		 return this.forward("igrp","etapaaccess","index", model, this.queryString()); //if submit, loads the values  ----#gen-example */
		/*----#start-code(gravar)----*/
		String type = Core.getParam("type");
		Integer orgProfId = Core.getParamInt("orgProfId");
		String userEmail = Core.getParam("userEmail");
		User user = null;
		String[] p_id = Core.getParamArray("p_id");
		String[] p_id_check = Core.getParamArray("p_id_check");
		CheckBoxHelper cb = Core.extractCheckBox(p_id, p_id_check);
		
		if(type.compareTo("user")==0) {
			user = new User().find().andWhere("email", "=",userEmail).one();
			this.removeOldInserts(type,user.getId(),cb.getUncheckedIds());	
		}else {
			this.removeOldInserts(type,orgProfId,cb.getUncheckedIds());	
		}
		boolean r = this.insertNew(cb.getChekedIds(),type,orgProfId,user);		
		if(r) {
			Core.setMessageSuccess();
		}else {
			Core.setMessageError();
		}
		this.addQueryString("type", type)
			.addQueryString("p_id", orgProfId)
			.addQueryString("userEmail", userEmail);
		return this.forward("igrp","Etapaaccess","index", this.queryString());	
		/*----#end-code----*/
		
			
	}


/*----#start-code(custom_actions)----*/

	private boolean insertNew(List<String> chekedIds, String type, Integer orgProfId,User user) {
		boolean r = true;
		Integer orgId = Core.getParamInt("orgId");
		if(chekedIds!=null) {
			Organization org = new Organization();
			if(Core.isNotNull(orgId) && orgId!=0) {
				org = org.findOne(orgId);
			}else {
				org = org.findOne(orgProfId);
			}			
			ProfileType prof = null;
			if("prof".compareTo(type)==0 || "user".compareTo(type)==0) {
				prof = new ProfileType().findOne(orgProfId);
			}
			for(String id:chekedIds) {
				String[] taskProcess = id.split(separator);
				if(taskProcess.length > 1) {
					TaskAccess task = new TaskAccess();
					task.setProcessName(taskProcess[1]);
					task.setTaskName(taskProcess[0]);
					task.setTaskDescription(taskProcess[2]);
					if("org".compareTo(type)==0) {
						task.setOrganization(org);
						r = task.insert()!=null;
						//Chain the all profile child
						if(r) {
							org.getProfilesType().forEach(proftype->{
								TaskAccess taskP = new TaskAccess();
								taskP.setProcessName(taskProcess[1]);
								taskP.setTaskName(taskProcess[0]);
								taskP.setTaskDescription(taskProcess[2]);
								taskP.setOrganization(proftype.getOrganization());
								taskP.setProfileType(proftype);
								taskP.insert();
							});
						}
					}
					if("prof".compareTo(type)==0) {
						task.setOrganization(prof.getOrganization());
						task.setProfileType(prof);
						r = task.insert()!=null;	
					}
					if("user".compareTo(type)==0) {
						task = task.find().andWhere("processName", "=",task.getProcessName())
								   .andWhere("taskName","=",task.getTaskName())
								   .andWhere("profileType", "=",prof.getId())
								   .andWhere("organization", "=",org.getId())
								   .one();
						if(task!=null) {
							task.setUser_fk(user.getId());	
							r = task.update()!=null;	
						}
					}	
				}
			}
		}
		return r;
	}

	/*
	 * Remove all old associates tasks
	 */
	private void removeOldInserts(String type,Integer orgProfUserId,List<String> uncheckedIds) {
		Integer orgId = Core.getParamInt("orgId");
		if(uncheckedIds!=null  && orgId!=0) {
			if("org".compareTo(type)==0) {				
				Core.delete(ConfigDBIGRP.FILE_NAME_HIBERNATE_IGRP_CONFIG,"tbl_task_access").where("org_fk=:org_fk")
									   .addInt("org_fk",orgProfUserId)
									   .execute();
			}
			if("prof".compareTo(type)==0) {
				Core.delete(ConfigDBIGRP.FILE_NAME_HIBERNATE_IGRP_CONFIG,"tbl_task_access").where("prof_fk=:prof_fk")
				   .addInt("prof_fk",orgProfUserId)
				   .execute();
			}
			if("user".compareTo(type)==0) {
				Core.executeQuery(ConfigDBIGRP.FILE_NAME_HIBERNATE_IGRP_CONFIG,"UPDATE tbl_task_access SET user_fk=null WHERE user_fk="+orgProfUserId+" AND prof_fk="+Core.getParamInt("profId"));
			}
		}
	}

	/*
	 * List all task associate to application with references to organization
	 */
	private List<Table_1> getOrganizationTasks(Integer orgProfId) {
		Organization org = new Organization().findOne(orgProfId);
		List<Table_1> table = new ArrayList<>();
		if(org!=null) {
			List<TaskService> list = new ArrayList<>();
			List<TaskAccess> listExist = new ArrayList<>();
			ProcessDefinitionIGRP processRest = new ProcessDefinitionIGRP();
			TaskServiceRest taskRest = new TaskServiceRest();
			for(ProcessDefinitionService process:processRest.getProcessDefinitionsForCreated(org.getApplication().getDad())){
				String link = process.getResource().replace("/resources/", "/resourcedata/");
				String resource = new ResourceServiceRest().getResourceData(link); 
				list.addAll(taskRest.extractTasks(resource,true));
				listExist.addAll(this.getTaskOrgExists(org.getId(), process.getKey()));
			}
			 
			list.stream().forEach(task->{
				Table_1 t = new Table_1();
				t.setId(task.getTaskDefinitionKey()+separator+task.getProcessDefinitionId()+separator+task.getProcessDefinitionId() + " - " + task.getName());
				if(listExist!=null) {
					if(!listExist.stream().filter(c->c.getProcessName().compareTo(task.getProcessDefinitionId())==0).filter(c->c.getTaskName().compareTo(task.getTaskDefinitionKey())==0).collect(Collectors.toList()).isEmpty()) {
						t.setId_check(t.getId());
					}
				}
				t.setDescricao(task.getProcessDefinitionId() + " - " + task.getName());
				t.setProcessid(task.getProcessDefinitionId());
				table.add(t);
			});
		}
		return table;
	}
	
	
	/*
	 * List all task associate to organization with references to profile
	 */
	private List<Table_1> getProfileTasks(ProfileType prof) {
		List<Table_1> table = new ArrayList<>();
		if(prof!=null) {
			List<TaskAccess> list = new TaskAccess().find()
													.andWhere("organization", "=",prof.getOrganization().getId())
													.andWhere("profileType", "isnull")
													.all();
			list.stream().forEach(task->{
				Table_1 t = new Table_1();
				t.setId(task.getTaskName()+separator+task.getProcessName()+separator+task.getTaskDescription());
				t.setProcessid(task.getProcessName());
				t.setDescricao(task.getTaskDescription());
				if(this.getTaskProfExists(prof.getOrganization().getId(), prof.getId(), task.getProcessName(), task.getTaskName())!=null)
					t.setId_check(t.getId());
				table.add(t);
			});
		}
		return table;
	}

	
	/*
	 * List all task associate to application with references to user
	 */
	private List<Table_1> getUserTasks(ProfileType prof, User user) {
		List<Table_1> table = new ArrayList<>();
		if(prof!=null) {
			List<TaskAccess> list = new TaskAccess().find()
													.andWhere("organization", "=",prof.getOrganization().getId())
													.andWhere("profileType","=", prof.getId())
													.andWhere("profileType", "isnotnull")
													.all();
			list.stream().forEach(task->{
				Table_1 t = new Table_1();
				t.setId(task.getTaskName()+separator+task.getProcessName()+separator+task.getTaskDescription());
				t.setProcessid(task.getProcessName());
				t.setDescricao(task.getTaskDescription());
				if(this.getTaskUserExists(user,task.getProcessName(), task.getTaskName())!=null)
					t.setId_check(t.getId());
				table.add(t);
			});
		}
		return table;
	}
	
	/*
	 *  Get existing task associate to the organization
	 */
	private List<TaskAccess> getTaskOrgExists(Integer id,String proccessName){
		return new TaskAccess().find()
				.andWhere("organization", "=",id)
				.andWhere("processName", "=",proccessName)
				.andWhere("profileType", "isnull")
				.all() ;
	}
	
	/*
	 * Get existing task associate to the profile
	 */
	private TaskAccess getTaskProfExists(Integer idOrg,Integer idProf,String proccessName,String taskName){
		return new TaskAccess().find()
				.andWhere("organization", "=",idOrg)
				.andWhere("processName", "=",proccessName)
				.andWhere("taskName", "=",taskName)
				.andWhere("profileType", "=",idProf)
				.one();
	}
	
	/*
	 * Get existing task associate to the user
	 */
	private TaskAccess getTaskUserExists(User user,String proccessName,String taskName){
		TaskAccess t= new TaskAccess().find()
				.andWhere("processName", "=",proccessName)
				.andWhere("taskName", "=",taskName)
				.andWhere("user_fk", "=",user.getId())		
				.one();
		return t;
	}
	private final String separator = "---IGRP---";
	/*----#end-code----*/
}
