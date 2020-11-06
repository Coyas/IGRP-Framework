package nosi.webapps.igrp.dao;

import nosi.base.ActiveRecord.BaseActiveRecord;
import nosi.core.config.ConfigDBIGRP;
import nosi.core.webapp.util.Core;

public class IGRPBaseActiveRecord<T> extends BaseActiveRecord<T> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void setConnectionName(String connectionName) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public T findOne(Object value) {
		if(value!=null)
			return super.findOne(Core.toInt(value.toString()));
		throw new IllegalArgumentException();
	}
	
	@Override
	public String getConnectionName() {
		return ConfigDBIGRP.FILE_NAME_HIBERNATE_IGRP_CONFIG;
    }
}
