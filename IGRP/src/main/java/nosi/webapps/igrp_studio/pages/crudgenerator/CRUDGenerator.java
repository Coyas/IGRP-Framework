package nosi.webapps.igrp_studio.pages.crudgenerator;

import nosi.core.gui.components.IGRPTable;
import nosi.core.webapp.Model;
import nosi.core.webapp.RParam;
import nosi.core.webapp.databse.helpers.BaseQueryInterface;
import java.util.ArrayList;
import java.util.List;

public class CRUDGenerator extends Model{		

	@RParam(rParamName = "p_aplicacao")
	private String aplicacao;

	@RParam(rParamName = "p_data_source")
	private String data_source;

	@RParam(rParamName = "p_schema")
	private String schema;

	@RParam(rParamName = "p_table_type")
	private String table_type;
	
	private List<Table_1> table_1 = new ArrayList<>();	
	public void setTable_1(List<Table_1> table_1){
		this.table_1 = table_1;
	}
	public List<Table_1> getTable_1(){
		return this.table_1;
	}

	
	public void setAplicacao(String aplicacao){
		this.aplicacao = aplicacao;
	}
	public String getAplicacao(){
		return this.aplicacao;
	}
	
	public void setData_source(String data_source){
		this.data_source = data_source;
	}
	public String getData_source(){
		return this.data_source;
	}
	
	public void setSchema(String schema){
		this.schema = schema;
	}
	public String getSchema(){
		return this.schema;
	}
	
	public void setTable_type(String table_type){
		this.table_type = table_type;
	}
	public String getTable_type(){
		return this.table_type;
	}


	public static class Table_1 extends IGRPTable.Table{
		private int check_table;
		private int check_table_check;
		private String table_name;
		public void setCheck_table(int check_table){
			this.check_table = check_table;
		}
		public int getCheck_table(){
			return this.check_table;
		}
		public void setCheck_table_check(int check_table_check){
			this.check_table_check = check_table_check;
		}
		public int getCheck_table_check(){
			return this.check_table_check;
		}

		public void setTable_name(String table_name){
			this.table_name = table_name;
		}
		public String getTable_name(){
			return this.table_name;
		}

	}

	public void loadTable_1(BaseQueryInterface query) {
		this.setTable_1(this.loadTable(query,Table_1.class));
	}

}
