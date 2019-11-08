
package nosi.webapps.igrp.pages.file;

import nosi.core.webapp.Controller;
import java.io.IOException;
import nosi.core.webapp.Core;
import nosi.core.webapp.Response;
/*----#start-code(packages_import)----*/
import nosi.webapps.igrp.dao.CLob;
import nosi.webapps.igrp.dao.TempFile;
import nosi.core.webapp.helpers.TempFileHelper;
import java.util.Properties;
/*----#end-code----*/


public class FileController extends Controller {		

	public Response actionIndex() throws IOException, IllegalArgumentException, IllegalAccessException{
		
		File model = new File();
		model.load();
		FileView view = new FileView();
		/*----#start-code(index)----*/
		
		/*----#end-code----*/
		view.setModel(model);
		return this.renderView(view);	
	}
	
	/*----#start-code(custom_actions)----*/
	public Response actionGetFile() throws Exception {
		String uuid= Core.getParam("uuid");		
		CLob file;
		if(Core.isNotNull(uuid))
			 file = Core.getFileByUuid(uuid);
		else
			 file = Core.getFile(Core.getParamInt("p_id").intValue());
		if(file!=null)
			return this.xSend(file.getC_lob_content(), file.getName(), file.getMime_type(), false);
		throw new Exception("File not found.");
	}
	
	public Response actionGetTempFile() throws Exception {		
		TempFile file = TempFileHelper.getTempFile(Core.getParam("p_uuid"));
		if(file!=null)
			return this.xSend(file.getContent(), file.getName(), file.getMime_type(), false);
		throw new Exception("File not found.");
	}
	
	public Response actionSaveImage()  throws Exception {		
		Properties p = new Properties();
		Integer id = new Integer(-1);
		String uuid = Core.saveFileNGetUuid("p_file_name");
		if(Core.isNull(uuid)) {			
			p.put("msg", Core.gt("Error saving file."));
		}else {
			id= new CLob().find().andWhere("uuid", "=",uuid).one().getId();
		}
		p.put("id", id);
		p.put("uuid", uuid);
		this.format = Response.FORMAT_JSON;
		return this.renderView(Core.toJson(p));
	}
	/*----#end-code----*/
	}
