/*
 * 	This file is part of DicomFlow.
 * 
 * 	DicomFlow is free software: you can redistribute it and/or modify
 * 	it under the terms of the GNU General Public License as published by
 * 	the Free Software Foundation, either version 3 of the License, or
 * 	(at your option) any later version.
 * 
 * 	This program is distributed in the hope that it will be useful,
 * 	but WITHOUT ANY WARRANTY; without even the implied warranty of
 * 	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * 	GNU General Public License for more details.
 * 
 * 	You should have received a copy of the GNU General Public License
 * 	along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package br.ufpb.dicomflow.tests;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.io.IOUtils;
import org.glassfish.jersey.client.ClientConfig;

import br.ufpb.dicomflow.integrationAPI.conf.DicomMessageProperties;

/**
 * 
 * @author Danilo Alexandre
 * @author Juracy Neto
 * 
 */
public class ConcurrentRequest implements Runnable {
	
	
	private WebTarget resourceWebTarget;
	private Integer reqNumber;

	@Override
	public void run() {		
		
		try {
			
			DicomMessageProperties iap = DicomMessageProperties.getInstance();
			iap.load("C:/home/dicomflow/repos/dicomMove2/WebContent/WEB-INF/classes/config.properties");
			
		
			
			String host = iap.getProperty("host");
			String port = iap.getProperty("port");
			String context = iap.getProperty("context");
			String serviceName = iap.getProperty("serviceName");
			String studyId = iap.getProperty("studyId");
			
			String resultPath = iap.getProperty("resultPath");
			
			
			ClientConfig clientConfig = new ClientConfig();
//			clientConfig.register(MyClientResponseFilter.class);
//			clientConfig.register(new AnotherClientFilter());				
			Client client = ClientBuilder.newClient(clientConfig);
//			client.register(ThirdClientFilter.class);
			//http://localhost:8090/DicomMoveServer/rest/DownloadStudy
			WebTarget webTarget = client.target("http://" + host + ":" + port + "/" + context + "/");
//			webTarget.register(FilterForExampleCom.class);
			WebTarget resourceWebTarget = webTarget.path(serviceName + "/" + studyId);
//			WebTarget helloworldWebTarget = resourceWebTarget.path("helloworld");
//			WebTarget helloworldWebTargetWithQueryParam = helloworldWebTarget.queryParam("greeting", "Hi World!");
			
//			Invocation.Builder invocationBuilder = helloworldWebTargetWithQueryParam.request(MediaType.APPLICATION_OCTET_STREAM);				
			
			long inicio = System.currentTimeMillis();
			
			//String authorization = "CDN" + ":" + accessKey.getId() + ":" + signature;
			String authorization = "DICOMFLOW" + ":" + "teste" + ":" + "Teste";
			
			Invocation.Builder invocationBuilder = resourceWebTarget.request(MediaType.APPLICATION_OCTET_STREAM)
					.header(HttpHeaders.AUTHORIZATION, authorization);
			
			Response response = invocationBuilder.get();
			InputStream is = (InputStream)response.getEntity();		
			//response.readEntity(String.class);		

			byte[] SWFByteArray = IOUtils.toByteArray(is);  

			FileOutputStream fos = new FileOutputStream(new File(resultPath + reqNumber + ".zip"));
			fos.write(SWFByteArray);
			fos.flush();
			fos.close();						
			
			long fim = System.currentTimeMillis();			
			SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");						
			System.out.println(reqNumber + " Status: " + response.getStatus() + " - Inicio: " + sdfDate.format(new Date(inicio)) + " Fim: " +  sdfDate.format(new Date(fim)));	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Integer getReqNumber() {
		return reqNumber;
	}

	public void setReqNumber(Integer reqNumber) {
		this.reqNumber = reqNumber;
	}
	
	

}
