package nosi.webapps.igrp.pages.pesquisarmenu;

import nosi.core.webapp.Model;
import nosi.core.webapp.View;
import nosi.core.gui.components.*;
import nosi.core.gui.fields.*;
import static nosi.core.i18n.Translator.gt;
import nosi.core.webapp.Core;

public class PesquisarMenuView extends View {

	public Field sectionheader_1_text;
	public Field aplicacao;
	public Field id_app;
	public Field t1_menu_principal;
	public Field ativo;
	public Field ativo_check;
	public Field ordem;
	public Field table_titulo;
	public Field pagina;
	public Field checkbox;
	public Field checkbox_check;
	public Field id;
	public IGRPSectionHeader sectionheader_1;
	public IGRPForm form_1;
	public IGRPTable table_1;

	public IGRPToolsBar toolsbar_1;
	public IGRPButton btn_btn_novo;
	public IGRPButton btn_editar;
	public IGRPButton btn_eliminar;

	public PesquisarMenuView(){

		this.setPageTitle("Gestão de Menu");
			
		sectionheader_1 = new IGRPSectionHeader("sectionheader_1","");

		form_1 = new IGRPForm("form_1","");

		table_1 = new IGRPTable("table_1","");

		sectionheader_1_text = new TextField(model,"sectionheader_1_text");
		sectionheader_1_text.setLabel(gt(""));
		sectionheader_1_text.setValue(gt("Gestão Menu"));
		sectionheader_1_text.propertie().add("type","text").add("name","p_sectionheader_1_text").add("maxlength","4000");
		
		aplicacao = new ListField(model,"aplicacao");
		aplicacao.setLabel(gt("Aplicação"));
		aplicacao.propertie().add("remote",Core.getIGRPLink("igrp","PesquisarMenu","index")).add("remote",Core.getIGRPLink("igrp","PesquisarMenu","index")).add("name","p_aplicacao").add("type","select").add("multiple","false").add("maxlength","100").add("required","false").add("disabled","false").add("domain","").add("java-type","").add("tags","false");
		
		id_app = new HiddenField(model,"id_app");
		id_app.setLabel(gt(""));
		id_app.propertie().add("name","p_id_app").add("type","hidden").add("maxlength","250").add("java-type","int").add("tag","id_app");
		
		t1_menu_principal = new PlainTextField(model,"t1_menu_principal");
		t1_menu_principal.setLabel(gt("Menu pai"));
		t1_menu_principal.propertie().add("name","p_t1_menu_principal").add("type","plaintext").add("maxlength","100").add("disable_output_escaping","false").add("html_class","").add("showLabel","true").add("group_in","");
		
		ativo = new CheckBoxField(model,"ativo");
		ativo.setLabel(gt("Ativo"));
		ativo.propertie().add("remote",Core.getIGRPLink("igrp","PesquisarMenu","changeStatus")).add("name","p_ativo").add("type","checkbox").add("maxlength","30").add("switch","true").add("java-type","int").add("showLabel","true").add("group_in","").add("check","true").add("desc","true");
		
		ativo_check = new CheckBoxField(model,"ativo_check");
		ativo_check.propertie().add("name","p_ativo").add("type","checkbox").add("maxlength","30").add("switch","true").add("java-type","int").add("showLabel","true").add("group_in","").add("check","true").add("desc","true");
		
		ordem = new NumberField(model,"ordem");
		ordem.setLabel(gt("Posição"));
		ordem.propertie().add("name","p_ordem").add("type","number").add("min","").add("max","").add("maxlength","30").add("total_footer","false").add("java-type","").add("calculation","false").add("mathcal","").add("numberformat","").add("showLabel","true").add("group_in","");
		
		table_titulo = new PlainTextField(model,"table_titulo");
		table_titulo.setLabel(gt("Título"));
		table_titulo.propertie().add("name","p_table_titulo").add("type","plaintext").add("maxlength","100").add("disable_output_escaping","false").add("html_class","").add("showLabel","true").add("group_in","");
		
		pagina = new PlainTextField(model,"pagina");
		pagina.setLabel(gt("Página"));
		pagina.propertie().add("name","p_pagina").add("type","plaintext").add("maxlength","100").add("disable_output_escaping","false").add("html_class","").add("showLabel","true").add("group_in","");
		
		checkbox = new CheckBoxField(model,"checkbox");
		checkbox.setLabel(gt("Público"));
		checkbox.propertie().add("name","p_checkbox").add("type","checkbox").add("maxlength","30").add("switch","false").add("java-type","int").add("showLabel","true").add("group_in","").add("check","true").add("desc","true");
		
		checkbox_check = new CheckBoxField(model,"checkbox_check");
		checkbox_check.propertie().add("name","p_checkbox").add("type","checkbox").add("maxlength","30").add("switch","false").add("java-type","int").add("showLabel","true").add("group_in","").add("check","true").add("desc","true");
		
		id = new HiddenField(model,"id");
		id.setLabel(gt(""));
		id.propertie().add("name","p_id").add("type","hidden").add("maxlength","30").add("java-type","").add("showLabel","true").add("group_in","").add("tag","id");
		

		toolsbar_1 = new IGRPToolsBar("toolsbar_1");

		btn_btn_novo = new IGRPButton("Novo","igrp","PesquisarMenu","btn_novo","right_panel_submit|refresh","success|fa-plus-square","","");
		btn_btn_novo.propertie.add("type","specific").add("rel","btn_novo").add("refresh_components","");

		btn_editar = new IGRPButton("Editar","igrp","PesquisarMenu","editar","right_panel_submit|refresh","warning|fa-pencil","","");
		btn_editar.propertie.add("id","button_edcd_6e25").add("type","specific").add("class","warning").add("rel","editar").add("refresh_components","");

		btn_eliminar = new IGRPButton("Eliminar","igrp","PesquisarMenu","eliminar","alert_submit","danger|fa-trash","","");
		btn_eliminar.propertie.add("id","button_97b3_231a").add("type","specific").add("class","danger").add("rel","eliminar").add("refresh_components","");

		
	}
		
	@Override
	public void render(){
		
		sectionheader_1.addField(sectionheader_1_text);

		form_1.addField(aplicacao);
		form_1.addField(id_app);


		table_1.addField(t1_menu_principal);
		table_1.addField(ativo);
		table_1.addField(ativo_check);
		table_1.addField(ordem);
		table_1.addField(table_titulo);
		table_1.addField(pagina);
		table_1.addField(checkbox);
		table_1.addField(checkbox_check);
		table_1.addField(id);

		toolsbar_1.addButton(btn_btn_novo);
		table_1.addButton(btn_editar);
		table_1.addButton(btn_eliminar);
		this.addToPage(sectionheader_1);
		this.addToPage(form_1);
		this.addToPage(table_1);
		this.addToPage(toolsbar_1);
	}
		
	@Override
	public void setModel(Model model) {
		
		aplicacao.setValue(model);
		id_app.setValue(model);
		t1_menu_principal.setValue(model);
		ativo.setValue(model);
		ordem.setValue(model);
		table_titulo.setValue(model);
		pagina.setValue(model);
		checkbox.setValue(model);
		id.setValue(model);	

		table_1.loadModel(((PesquisarMenu) model).getTable_1());
		}
}
