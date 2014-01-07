package cn.com.psh.sunflower.common;


public class ConfigurationManager {

    private static ConfigurationManager _cm;
	private static final String[] NULL_STRING_ARRAY = new String[]{};

	private final static String CONFIG_FILENAME = "configSunflowerType.xml";

	public static ConfigurationManager getInstance() throws Exception {
		if (_cm == null) {
			synchronized (ConfigurationManager.class) {
				if (_cm == null) {
					_cm = new ConfigurationManager();
				}
			}
		}
		return _cm;
	}

	private ConfigurationManager() {	    	    
	}

}
