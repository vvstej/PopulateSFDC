import java.util.Random;

import com.sforce.soap.enterprise.EnterpriseConnection;
import com.sforce.soap.enterprise.LoginResult;
import com.sforce.soap.metadata.CustomTab;
import com.sforce.soap.metadata.Encoding;
import com.sforce.soap.metadata.Metadata;
import com.sforce.soap.metadata.MetadataConnection;
import com.sforce.soap.metadata.SaveResult;
import com.sforce.ws.ConnectionException;
import com.sforce.ws.ConnectorConfig;

/**
 * Login utility.
 */
public class MetadataLoginUtil {

	private static MetadataConnection mdConnection;
    public static void login() throws ConnectionException {
        final String USERNAME = "tej@200.local";
        // This is only a sample. Hard coding passwords in source files is a bad practice.
        final String PASSWORD = "test1234"; 
        final String URL = "http://vvepa-wsm.internal.salesforce.com:6109/services/Soap/c/36.0";//"https://mobile2.t.salesforce.com/services/Soap/c/36.0";
        final LoginResult loginResult = loginToSalesforce(USERNAME, PASSWORD, URL);
        createMetadataConnection(loginResult);
    }

    private static void createMetadataConnection(
            final LoginResult loginResult) throws ConnectionException {
        final ConnectorConfig config = new ConnectorConfig();
        config.setServiceEndpoint(loginResult.getMetadataServerUrl());
        config.setSessionId(loginResult.getSessionId());
        mdConnection =  new MetadataConnection(config);
    }

    private static LoginResult loginToSalesforce(
            final String username,
            final String password,
            final String loginUrl) throws ConnectionException {
        final ConnectorConfig config = new ConnectorConfig();
        config.setAuthEndpoint(loginUrl);
        config.setServiceEndpoint(loginUrl);
        config.setManualLogin(true);
        return (new EnterpriseConnection(config)).login(username, password);
    }
    public static void main(String[] arg) throws Exception{
    	MetadataLoginUtil.login();
    	if(mdConnection!=null){
    		for(int i=0;i<410;i++){
        		createCustomTab("CustomTab_url"+System.currentTimeMillis()+i,mdConnection);
        	}
    	}
    }

	private static void createCustomTab(String uniqueName, MetadataConnection mdConnection) throws Exception{
		CustomTab tab = new CustomTab();
		tab.setFullName(uniqueName);
		tab.setUrl("http://google.com");
		tab.setDescription("samapleDescription");
		tab.setFrameHeight(600);
		tab.setMobileReady(true);
		tab.setMotif("Custom53: Bell");
		tab.setUrlEncodingKey(Encoding.UTF_8);
		tab.setLabel(uniqueName);
		SaveResult[] results = mdConnection.createMetadata(new Metadata[]{tab});
		for(SaveResult r : results){
			if(r.isSuccess()){
				System.out.println("Created Component" + r.getFullName());
			}else{
				System.out.println("Errors"+r.getFullName());
				for(com.sforce.soap.metadata.Error e : r.getErrors()){
					System.out.println("Error message "+e.getMessage());
					System.out.println("Status Code "+e.getStatusCode());
				}
			}
		}
		
	}
}