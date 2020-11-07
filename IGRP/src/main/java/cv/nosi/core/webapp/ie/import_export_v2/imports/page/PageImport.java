package cv.nosi.core.webapp.ie.import_export_v2.imports.page;

import java.io.File;
import java.io.IOException;
import java.util.List;
import com.google.gson.reflect.TypeToken;

import cv.nosi.core.webapp.ie.import_export_v2.common.Path;
import cv.nosi.core.webapp.ie.import_export_v2.common.serializable.page.PageSerializable;
import cv.nosi.core.webapp.ie.import_export_v2.imports.AbstractImport;
import cv.nosi.core.webapp.ie.import_export_v2.imports.IImport;
import cv.nosi.core.webapp.util.Core;
import cv.nosi.core.webapp.util.helpers.file.FileHelper;
import cv.nosi.webapps.igrp.dao.Action;
import cv.nosi.webapps.igrp.dao.Application;

/**
 * Emanuel
 * 2 Nov 2018
 */
public class PageImport extends AbstractImport implements IImport{
	protected Application application;
	private List<PageSerializable> pages;
	
	
	
	public PageImport(Application application) {
		super();
		this.application = application;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void deserialization(String jsonContent) {
		if(Core.isNotNull(jsonContent)) {
			this.pages = (List<PageSerializable>) Core.fromJsonWithJsonBuilder(jsonContent, new TypeToken<List<PageSerializable>>() {}.getType());
		}
	}

	@Override
	public void execute() {
		if(this.pages!=null){
			this.pages.stream().forEach(page->{
				this.insertPage(page);
			});
		}
	}

	protected void saveFile(PageSerializable page, Action ac) {
		if(page.getClassFiles()!=null && ac!=null) {
			String path = Path.getPath(ac.getApplication())+"pages"+File.separator+ac.getPage().toLowerCase()+File.separator;
			try {
				FileHelper.save(path,ac.getPage()+".java", page.getClassFiles().getXmlOrModel());
				FileHelper.save(path,ac.getPage()+"View.java", page.getClassFiles().getJsonOrView());
				FileHelper.save(path,ac.getPage()+"Controller.java", page.getClassFiles().getXslOrController());
				this.fileName.add(path+ac.getPage()+".java");
				this.fileName.add(path+ac.getPage()+"View.java");
				this.fileName.add(path+ac.getPage()+"Controller.java");
			} catch (IOException e) {
				this.addError(ac.getPage()+" - "+e.getMessage());
			}
		}
		if(page.getPageFiles()!=null) {
			String pathServer = Path.getPathImagesServer(ac);
			String pathWorkSapce = Path.getPathImagesWorkSapce(ac);
			try {
				FileHelper.save(pathServer ,ac.getPage()+".xml", page.getPageFiles().getXmlOrModel());
				FileHelper.save(pathServer,ac.getPage()+".json", page.getPageFiles().getJsonOrView());
				FileHelper.save(pathServer,ac.getPage()+".xsl", page.getPageFiles().getXslOrController());
				if(Core.isNotNull(pathWorkSapce)) {//Save files into your workspace
					FileHelper.save(pathWorkSapce ,ac.getPage()+".xml", page.getPageFiles().getXmlOrModel());
					FileHelper.save(pathWorkSapce,ac.getPage()+".json", page.getPageFiles().getJsonOrView());
					FileHelper.save(pathWorkSapce,ac.getPage()+".xsl", page.getPageFiles().getXslOrController());
				}
			} catch (IOException e) {
				this.addError(e.getMessage());
			}
		}
	}

	protected void insertPage(PageSerializable page) {
		Action ac = new Action().find().andWhere("page", "=",page.getPage()).andWhere("application.dad", "=",page.getDad()).one();
		if(ac==null) {
			if(this.application == null) {
				this.application = new Application().findByDad(page.getDad());
			}
			ac = new Action(page.getPage(), page.getAction(), page.getPackage_name(), page.getXsl_src(), page.getPage_descr(), page.getAction_descr(), page.getVersion(), page.getStatus(), this.application);
			ac.setNomeModulo(page.getNomeModulo());
			ac.setIsComponent(page.getIsComponent());
			ac.setProcessKey(page.getProcessKey());
			ac.setTipo(page.getTipo());
			ac = ac.insert();
			this.addError(ac.hasError()?page.getPage()+" - "+ac.getError().get(0):null);
		}else {
			ac.setNomeModulo(page.getNomeModulo());
			ac.setIsComponent(page.getIsComponent());
			ac.setProcessKey(page.getProcessKey());
			ac.setTipo(page.getTipo());
			ac.setPage_descr(page.getPage_descr());
			ac.setStatus(page.getStatus());			
			ac = ac.update();
			this.addError(ac.hasError()?page.getPage()+" - "+ac.getError().get(0):null);
		}
		if(!ac.hasError()) {
			this.saveFile(page,ac);
		}
	}

}
