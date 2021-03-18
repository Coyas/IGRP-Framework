package nosi.webapps.igrp.pages.icons_list;

import nosi.core.webapp.Model;
import nosi.core.webapp.View;
import nosi.core.gui.components.*;
import nosi.core.gui.fields.*;
import static nosi.core.i18n.Translator.gt;

public class Icons_listView extends View {

	public Field fwl_search;
	public Field choose_icon;
	public Field sectionheader_1_text;
	public IGRPForm form_1;
	public IGRPSectionHeader sectionheader_1;


	public Icons_listView(){

		this.setPageTitle("Icons List");
			
		form_1 = new IGRPForm("form_1","");

		sectionheader_1 = new IGRPSectionHeader("sectionheader_1","Lista de ícones");

		fwl_search = new HiddenField(model,"fwl_search");
		fwl_search.setLabel(gt(""));
		fwl_search.propertie().add("name","p_fwl_search").add("type","hidden").add("maxlength","250").add("java-type","").add("tag","fwl_search");
		
		choose_icon = new HiddenField(model,"choose_icon");
		choose_icon.setLabel(gt(""));
		choose_icon.propertie().add("name","p_choose_icon").add("type","hidden").add("maxlength","250").add("java-type","").add("tag","choose_icon");
		
		sectionheader_1_text = new TextField(model,"sectionheader_1_text");
		sectionheader_1_text.setLabel(gt(""));
		sectionheader_1_text.setValue(gt("<p>Lista de &iacute;cones</p>"));
		sectionheader_1_text.propertie().add("type","text").add("name","p_sectionheader_1_text").add("maxlength","4000");
		


		
	}
		
	@Override
	public void render(){
		
		form_1.addField(fwl_search);
		form_1.addField(choose_icon);

		sectionheader_1.addField(sectionheader_1_text);

		this.addToPage(form_1);
		this.addToPage(sectionheader_1);
	}
		
	@Override
	public void setModel(Model model) {
		
		fwl_search.setValue(model);
		choose_icon.setValue(model);	

		}
}
