package nosi.core.config;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Properties;

import nosi.core.db.migration.api.MigrationIGRPInitConfig;
import nosi.core.webapp.ApplicationManager;
import nosi.core.webapp.Core;
import nosi.core.webapp.Igrp;


/**
 * Emanuel
 * 17 Jul 2018
 */
public final class ConfigApp {

    private String version;
    private String dataInstall;
    private String isInstallation;
    private final Config config;

    private final Properties commonMain;

    private static final ConfigApp CONFIG_APP = new ConfigApp();

    private ConfigApp() {
        this.config = new Config();
        this.commonMain = this.loadConfig("common", "main.xml");
    }

    public static ConfigApp getInstance() {
        return CONFIG_APP;
    }

    public static Properties loadConfig(String fileName) { // xml to properties
        final File file = new File(fileName);
        final Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream(file)) {
            properties.loadFromXML(fis);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }
	public Properties loadProperties(String fileName) throws IOException {
		final Properties properties = new Properties();
		try (InputStream inputStream = getClass().getResourceAsStream(fileName)) {
			if (inputStream != null)
				properties.load(inputStream);
		}
		return properties;
	}


    public Properties loadConfig(String filePath, String fileName) {
        final String path = Config.BASE_PATH_CONFIGURATION + "/" + filePath;
        return loadConfig(Objects.requireNonNull(this.getClass().getClassLoader().getResource(path + "/" + fileName)).getPath().replace("%20", " "));
    }

    public String getBaseConnection() {
        return commonMain.getProperty(ConfigCommonMainConstants.IGRP_DATASOURCE_CONNECTION_NAME.value(), getDefaultBaseConnectionName());
    }

    public String getDefaultBaseConnectionName() {
        return "hibernate-igrp-core";
    }

    public void saveProperties(Properties p, String fileName) {
        try (OutputStream out = Files.newOutputStream(Paths.get(fileName))) {
            p.store(out, "");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public String getExternalUrl(String dad) {
    	String url = ApplicationManager.requestUrl(Igrp.getInstance().getRequest());
    	if(dad != null && !dad.trim().isEmpty()) {
    		String deployedWarName = Core.getDeployedWarName();
    		url = url.replaceFirst("/" + deployedWarName + "/", "/" + dad + "/");
    	}
    	return url;
    }

    public String getWorkspace() {
        return commonMain.getProperty(ConfigCommonMainConstants.IGRP_WORKSPACE.value());
    }

    public String getEnvironment() {
        return commonMain.getProperty(ConfigCommonMainConstants.IGRP_ENV.value());
    }

    public String getAutenticationType() {
        return commonMain.getProperty(ConfigCommonMainConstants.IGRP_AUTHENTICATION_TYPE.value());
    }
    
    public boolean isActiveGlobalACL() {
        return Core.isNotNull(commonMain.getProperty("igrp.plsql.myapps.url"));

    }

    public Properties getMainSettings() {
        return commonMain;
    }

    public Config getConfig() {
        return config;
    }

}
