package nosi.core.webapp.import_export;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.Part;
import javax.xml.transform.TransformerConfigurationException;
import nosi.core.config.Config;
import nosi.core.webapp.helpers.ImportExportApp;
import nosi.core.webapp.helpers.XMLTransform;
import nosi.core.webapp.helpers.ImportExportApp.FileOrderCompile;
import nosi.webapps.igrp.dao.Action;
import nosi.webapps.igrp.dao.Application;

/**
 * @author: Emanuel Pereira
 * 5 Nov 2017
 * Importa aplica��es/pag�nas de IGRP PLSQL
 */
public class ImportAppZip extends ImportAppJar{

	private Map<String,FileOrderCompile> filesConfigPagePlsql = new HashMap<>();

	public ImportAppZip(Part fileName){
		super(fileName);
		for(FileOrderCompile file:this.un_jar_files){
			if(file.getConteudo()!=null && !file.getConteudo().equals("")  && file.getNome().startsWith("FTP/IGRP")){
				String part[] = file.getNome().split("/");
				if(!"navigation.xml".equals(part[part.length-1]) && !"slide-menu.xml".equals(part[part.length-1]))
					this.filesConfigPagePlsql.put(part[part.length-1], file);
			}
			if(file.getConteudo()!=null && !file.getConteudo().equals("")  && file.getNome().startsWith("SQL/CONFIG")   && file.getNome().endsWith(".json.xml")){
				String part[] = file.getNome().split("/");
				this.filesConfigPagePlsql.put(part[part.length-1], file);
			}
		}
	}

	
	@Override
	public boolean importApp() {
		boolean result = true;
		for(FileOrderCompile file:this.un_jar_files){
			if(file.getNome().startsWith("SQL/CONFIG")  && file.getNome().endsWith("_ENV.xml")){
				this.app = this.saveApp(file);
				if(this.app==null){
					result = false;
					break;
				}
				FileOrderCompile f = new ImportExportApp().new FileOrderCompile("page/"+this.app.getDad()+"/DefaultPage/index", "", 1);
				result = this.compileFiles(f,this.app);
			}
			if(file.getNome().startsWith("SQL/CONFIG")  && file.getNome().endsWith("_ACTION.xml")){
				List<Action> pages = this.savePagePlsql(file,this.app);
				if(pages.isEmpty()){
					result = false;
					break;
				}else{
					result = this.saveConfigFilesPlsql(pages);
				}
			}
		}
		return result;
	}

	
	private boolean saveConfigFilesPlsql(List<Action> pages) {
		boolean result = false;
		for(Action page:pages)
			result = this.saveConfigFilesPlsql(page.getXsl_src(), page.getApplication(), page);
		return result;
	}

	private boolean saveConfigFilesPlsql(String fileName, Application app, Action page) {
		String part[] = fileName.split("/");
		String xsl = part[part.length-1];
		String xml = xsl.replace(".xsl", ".xml");
		String json = "UI"+page.getId_plsql()+".json.xml";
		boolean result = false;
		if(this.filesConfigPagePlsql.get(xsl)!=null){
			String content = this.filesConfigPagePlsql.get(xsl).getConteudo();
			content = content.replaceAll("../../xsl", "../../../xsl");
			FileOrderCompile file = new ImportExportApp().new FileOrderCompile("configs/"+app.getDad()+"/"+page.getPage()+"/"+page.getAction()+"/"+page.getPage()+".xsl", content, 1);
			result = this.saveFiles(file , app);			
		}
		
		if(this.filesConfigPagePlsql.get(json)!=null){
			String content = this.filesConfigPagePlsql.get(json).getConteudo();
			FileOrderCompile file = new ImportExportApp().new FileOrderCompile("configs/"+app.getDad()+"/"+page.getPage()+"/"+page.getAction()+"/"+page.getPage()+".json", content, 1);
			result = this.saveFiles(file , app);			
		}

		if(this.filesConfigPagePlsql.get(xml)!=null){
			String content = this.filesConfigPagePlsql.get(xml).getConteudo();
			//Verifica se o xml possui package_html que ser� o nome da classe
			//Caso n�o exista, assumi o nome do ficheiro como nome da classe
			content = content.substring(0, content.indexOf("<package_html>")+"<package_html>".length())+ page.getPage()+content.substring(content.indexOf("</package_html>"));
			String package_name = Config.getBasePackage(app.getDad()).contains(".pages")?Config.getBasePackage(app.getDad()):Config.getBasePackage(app.getDad())+".pages";
			content = content.substring(0, content.indexOf("<package_db>")+"<package_db>".length())+package_name +content.substring(content.indexOf("</package_db>"));
			FileOrderCompile file = new ImportExportApp().new FileOrderCompile("configs/"+app.getDad()+"/"+page.getPage()+"/"+page.getAction()+"/"+page.getPage()+".xml", content, 1);
			this.saveFiles(file , app);
			try {
				String path = Config.getBasePathXsl()+Config.getResolvePathXsl(app.getDad(), page.getPage(), page.getVersion())+File.separator+page.getPage()+".xml";
				//Gera codigo MVC a partir de xml, usando gerador xsl
				String modelViewController = XMLTransform.tranform(path, Config.getBasePathXsl()+"images/IGRP/IGRP2.3/core/formgen/util/java/XSL_GENERATOR.xsl");
				String[] partsJavaCode = modelViewController.toString().split(" END ");
				if(partsJavaCode.length > 2){
					String model = partsJavaCode[0]+"*/";
					String view = "/*"+partsJavaCode[1]+"*/";
					String controller = "/*"+partsJavaCode[2];
					FileOrderCompile fileM = new ImportExportApp().new FileOrderCompile("pages/"+app.getDad()+"/"+page.getPage()+"/"+page.getPage()+".java",model,1);
					FileOrderCompile fileV = new ImportExportApp().new FileOrderCompile("pages/"+app.getDad()+"/"+page.getPage()+"/"+page.getPage()+"View.java",view,1);
					FileOrderCompile fileC = new ImportExportApp().new FileOrderCompile("pages/"+app.getDad()+"/"+page.getPage()+"/"+page.getPage()+"Controller.java",controller,1);
					result = this.compileFiles(fileM, app) && this.compileFiles(fileV, app) && this.compileFiles(fileC, app);
				}
			} catch (TransformerConfigurationException e) {
			}
		}
		return result;
	}

}
