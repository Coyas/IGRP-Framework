
package nosi.webapps.tutorial.pages.geralapresentacao;
import nosi.core.webapp.View;
import nosi.core.gui.components.*;
import nosi.core.gui.fields.*;
import static nosi.core.i18n.Translator.gt;

public class GeralApresentacaoView extends View {
	
	
	public Field sectionheader_1_text;
	public Field paragraph_1_text;
	public Field videos;
	public Field documentos;
	public Field como_contribuir;
	public Field como_instalar_o_igrp;
	public Field minha_primeira_aplicacao;
	public Field demo_de_um_aplicacao;
	public Field paragraph_6_text;
	public Field paragraph_2_text;
	public Field paragraph_3_text;
	public Field paragraph_4_text;
	public Field paragraph_5_text;
	public Field video_1_text;
	public Field video_3_text;
	public Field video_4_text;
	public Field video_2_text;
	public Field video_5_text;
	public Field documento;
	public Field ver;
	public IGRPSectionHeader sectionheader_1;
	public IGRPParagraph paragraph_1;
	public IGRPTabContent tabcontent_1;
	public IGRPTabContent tabcontent_2;
	public IGRPParagraph paragraph_6;
	public IGRPParagraph paragraph_2;
	public IGRPParagraph paragraph_3;
	public IGRPParagraph paragraph_4;
	public IGRPParagraph paragraph_5;
	public IGRPVideo video_1;
	public IGRPVideo video_3;
	public IGRPVideo video_4;
	public IGRPVideo video_2;
	public IGRPVideo video_5;
	public IGRPTable table_1;

	public IGRPButton btn_download;
	public GeralApresentacaoView(GeralApresentacao model){
		this.setPageTitle(gt("O que fazer dentro do IGRP JAVA..."));
			
		sectionheader_1 = new IGRPSectionHeader("sectionheader_1","");
		paragraph_1 = new IGRPParagraph("paragraph_1",gt("Tutorial IGRP JAVA Framework"));
		tabcontent_1 = new IGRPTabContent("tabcontent_1","");
		tabcontent_2 = new IGRPTabContent("tabcontent_2","");
		paragraph_6 = new IGRPParagraph("paragraph_6",gt("Documentos IGRP JAVA Framework"));
		paragraph_2 = new IGRPParagraph("paragraph_2",gt("Como Contribuir no IGRP JAVA"));
		paragraph_3 = new IGRPParagraph("paragraph_3",gt("Baixar e instalar IGRP JAVA Framework"));
		paragraph_4 = new IGRPParagraph("paragraph_4",gt("Minha Primeira Aplica��o"));
		paragraph_5 = new IGRPParagraph("paragraph_5",gt("Aplica��o Marca��o de Consulta"));
		video_1 = new IGRPVideo("video_1","");
		video_3 = new IGRPVideo("video_3",gt("Como importar o IGRP JAVA do GitHub"));
		video_4 = new IGRPVideo("video_4","");
		video_2 = new IGRPVideo("video_2",gt("Como baixar o IGRP JAVA do NosiCode"));
		video_5 = new IGRPVideo("video_5","");
		table_1 = new IGRPTable("table_1","");
		sectionheader_1_text = new TextField(model,"sectionheader_1_text");
		sectionheader_1_text.setLabel("");
		
		sectionheader_1_text.setValue(gt("Bem-vindo ao seu IGRP Java"));
		sectionheader_1_text.propertie().add("type","text").add("name","p_sectionheader_1_text").add("persist","true").add("maxlength","4000");
		paragraph_1_text = new TextField(model,"paragraph_1_text");
		paragraph_1_text.setLabel("");
		
		paragraph_1_text.setValue(gt("Nesse pequeno Tutorial vamos ver como fazer diversas coisas com o IGRP JAVA, atrav�s de v�deos e documentos. Bom aproveito..."));
		paragraph_1_text.propertie().add("type","text").add("name","p_paragraph_1_text").add("persist","true").add("maxlength","4000");
		videos = new TextField(model,"videos");
		videos.setLabel(gt("V�deos"));
		
		videos.propertie().add("name","p_videos").add("type","button").add("target_fields","").add("closerefresh","false").add("iconColor","#333").add("iconClass","").add("img","fa-video-camera").add("maxlength","50");
		documentos = new TextField(model,"documentos");
		documentos.setLabel(gt("Documentos"));
		
		documentos.propertie().add("name","p_documentos").add("type","button").add("target_fields","").add("closerefresh","false").add("iconColor","#333").add("iconClass","").add("img","fa-file-text").add("maxlength","50");
		como_contribuir = new TextField(model,"como_contribuir");
		como_contribuir.setLabel(gt("Como Contribuir"));
		
		como_contribuir.propertie().add("name","p_como_contribuir").add("type","button").add("target_fields","").add("closerefresh","false").add("iconColor","#333").add("iconClass","").add("img","fa-hand-o-right").add("maxlength","50");
		como_instalar_o_igrp = new TextField(model,"como_instalar_o_igrp");
		como_instalar_o_igrp.setLabel(gt("Como instalar o IGRP"));
		
		como_instalar_o_igrp.propertie().add("name","p_como_instalar_o_igrp").add("type","button").add("target_fields","").add("closerefresh","false").add("iconColor","#333").add("iconClass","").add("img","fa-hand-o-right").add("maxlength","50");
		minha_primeira_aplicacao = new TextField(model,"minha_primeira_aplicacao");
		minha_primeira_aplicacao.setLabel(gt("Minha Primeira Aplica��o"));
		
		minha_primeira_aplicacao.propertie().add("name","p_minha_primeira_aplicacao").add("type","button").add("target_fields","").add("closerefresh","false").add("iconColor","#333").add("iconClass","").add("img","fa-hand-o-right").add("maxlength","50");
		demo_de_um_aplicacao = new TextField(model,"demo_de_um_aplicacao");
		demo_de_um_aplicacao.setLabel(gt("Demo de um Aplica��o"));
		
		demo_de_um_aplicacao.propertie().add("name","p_demo_de_um_aplicacao").add("type","button").add("target_fields","").add("closerefresh","false").add("iconColor","#333").add("iconClass","").add("img","fa-hand-o-right").add("maxlength","50");
		paragraph_6_text = new TextField(model,"paragraph_6_text");
		paragraph_6_text.setLabel("");
		
		paragraph_6_text.setValue(gt("Documentos auxiliares na apredizagem de algumas funcionalidades do IGRP JAVA Framework."));
		paragraph_6_text.propertie().add("type","text").add("name","p_paragraph_6_text").add("persist","true").add("maxlength","4000");
		paragraph_2_text = new TextField(model,"paragraph_2_text");
		paragraph_2_text.setLabel("");
		
		paragraph_2_text.setValue(gt("Nesse v�deo vamos aprender como fazer uma contribui��o nesse framework, desde do import do projecto no reposit�rio GitHub at� como gerar as nossas p�ginas e actualizar o nosso reposit�rio local."));
		paragraph_2_text.propertie().add("type","text").add("name","p_paragraph_2_text").add("persist","true").add("maxlength","4000");
		paragraph_3_text = new TextField(model,"paragraph_3_text");
		paragraph_3_text.setLabel("");
		
		paragraph_3_text.setValue(gt("Nessa sess�o mostra como baixar e instalar o IGRP, para tal temos 2 v�deos. Primeiro mostra como como importar o IGRP JAVA do reposit�rio GitHub directamente do Eclipse IDE e o segundo mostra como baixar o IGRP JAVA do site NosiCode e depois importar no Eclipse IDE"));
		paragraph_3_text.propertie().add("type","text").add("name","p_paragraph_3_text").add("persist","true").add("maxlength","4000");
		paragraph_4_text = new TextField(model,"paragraph_4_text");
		paragraph_4_text.setLabel("");
		
		paragraph_4_text.setValue(gt("Aqui vamos desenvolver uma pequena aplica��o com duas p�ginas com intuito de inserir e ler dados os dados de base de dados (PostgreSql). Ao longo do v�deo tamb�m mostra como baixar e instalar o PostGreSql e configurar a conex�o da aplica��o com o mesmo."));
		paragraph_4_text.propertie().add("type","text").add("name","p_paragraph_4_text").add("persist","true").add("maxlength","4000");
		paragraph_5_text = new TextField(model,"paragraph_5_text");
		paragraph_5_text.setLabel("");
		
		paragraph_5_text.setValue(gt("Um pequeno Demo de como criar uma aplica��o, gerar p�ginas e atribuir permiss�es de acesso a determinados utilizadores."));
		paragraph_5_text.propertie().add("type","text").add("name","p_paragraph_5_text").add("persist","true").add("maxlength","4000");
		video_1_text = new TextField(model,"video_1_text");
		video_1_text.setLabel("");
		
		video_1_text.setValue("https://www.youtube.com/embed/cJTJuEtTRbc");
		video_1_text.propertie().add("type","text").add("name","p_video_1_text").add("persist","true").add("maxlength","4000");
		video_3_text = new TextField(model,"video_3_text");
		video_3_text.setLabel("");
		
		video_3_text.setValue("https://www.youtube.com/embed/Viz9FefXZYg");
		video_3_text.propertie().add("type","text").add("name","p_video_3_text").add("persist","true").add("maxlength","4000");
		video_4_text = new TextField(model,"video_4_text");
		video_4_text.setLabel("");
		
		video_4_text.setValue("https://www.youtube.com/embed/Viz9FefXZYg");
		video_4_text.propertie().add("type","text").add("name","p_video_4_text").add("persist","true").add("maxlength","4000");
		video_2_text = new TextField(model,"video_2_text");
		video_2_text.setLabel("");
		
		video_2_text.setValue("https://www.youtube.com/embed/Viz9FefXZYg");
		video_2_text.propertie().add("type","text").add("name","p_video_2_text").add("persist","true").add("maxlength","4000");
		video_5_text = new TextField(model,"video_5_text");
		video_5_text.setLabel("");
		
		video_5_text.setValue("https://www.youtube.com/embed/Viz9FefXZYg");
		video_5_text.propertie().add("type","text").add("name","p_video_5_text").add("persist","true").add("maxlength","4000");
		documento = new TextField(model,"documento");
		documento.setLabel(gt("Documento"));
		
		documento.propertie().add("name","p_documento").add("type","text").add("maxlength","30").add("align","left").add("lookup_parser","false").add("iskey","false");
		ver = new LinkField(model,"ver");
		ver.setLabel(gt("Ver"));
		
		ver.propertie().add("name","p_ver").add("type","link").add("target","_newtab").add("target_fields","").add("closerefresh","false").add("action","index").add("page","GeralApresentacao").add("app","tutorial").add("class","link").add("btnSize","").add("iconColor","#333").add("iconClass","").add("img","fa-link").add("maxlength","30").add("align","left").add("lookup_parser","false").add("iskey","false").add("desc","true");

		btn_download = new IGRPButton("Download","tutorial","GeralApresentacao","download","submit","primary|fa-download","","");
		btn_download.propertie.add("type","specific").add("code","").add("class","primary").add("rel","download");
		
	}
		
	@Override
	public void render(){
		
		sectionheader_1.addField(sectionheader_1_text);

		paragraph_1.addField(paragraph_1_text);

		tabcontent_1.addField(videos);
		tabcontent_1.addField(documentos);

		tabcontent_2.addField(como_contribuir);
		tabcontent_2.addField(como_instalar_o_igrp);
		tabcontent_2.addField(minha_primeira_aplicacao);
		tabcontent_2.addField(demo_de_um_aplicacao);

		paragraph_6.addField(paragraph_6_text);

		paragraph_2.addField(paragraph_2_text);

		paragraph_3.addField(paragraph_3_text);

		paragraph_4.addField(paragraph_4_text);

		paragraph_5.addField(paragraph_5_text);

		video_1.addField(video_1_text);

		video_3.addField(video_3_text);

		video_4.addField(video_4_text);

		video_2.addField(video_2_text);

		video_5.addField(video_5_text);

		table_1.addField(documento);
		table_1.addField(ver);

		this.addToPage(sectionheader_1);
		this.addToPage(paragraph_1);
		this.addToPage(tabcontent_1);
		this.addToPage(tabcontent_2);
		this.addToPage(paragraph_6);
		this.addToPage(paragraph_2);
		this.addToPage(paragraph_3);
		this.addToPage(paragraph_4);
		this.addToPage(paragraph_5);
		this.addToPage(video_1);
		this.addToPage(video_3);
		this.addToPage(video_4);
		this.addToPage(video_2);
		this.addToPage(video_5);
		this.addToPage(table_1);
	}
}
