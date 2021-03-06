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
package br.ufpb.dicomflow.ws;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import br.ufpb.dicomflow.bean.InstanceIF;
import br.ufpb.dicomflow.bean.SeriesIF;
import br.ufpb.dicomflow.bean.StudyIF;
import br.ufpb.dicomflow.service.PacsPersistentServiceIF;
import br.ufpb.dicomflow.service.ServiceException;
import br.ufpb.dicomflow.service.ServiceLocator;
import br.ufpb.dicomflow.util.Util;

@Path("/DownloadStudy/{studyIUID}")
public class DownloadStudy extends GenericWebService {
	
	static List<InstanceIF> instances;
	
	@GET
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response getStudy(@PathParam("studyIUID") String studyIUID) {
		
		//TODO - Modificar
		if (authenticate()) {
			System.out.println("OK");
		}
				
		//TODO implementando a autenticacao no download
		//AccessKeyID = host
		//AccessKey = credential		
		//No solicitante - pegar o host do applicationcontext - beans URLGenerator e mailHeadBuilder
		//No que responde o download - pegar da tabela access 
		
		
		Date initialTime = new Date();
		SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss.SSS");
		Util.getLogger(this).debug("INICIANDO DOWNLOAD - " + format.format(initialTime) );
		PacsPersistentServiceIF persistentService = ServiceLocator.singleton().getPacsPersistentService();
		StudyIF study = (StudyIF) persistentService.selectStudy(studyIUID);	
		
		if (study != null ) {
			//TODO melhorar o resgate dos arquivos objetivando melhor desempenho
			List<SeriesIF> series = persistentService.selectAllSeries(study);
			instances =  persistentService.selectAllFiles(series);	

	        StreamingOutput stream = new StreamingOutput() {
				@Override
				public void write(OutputStream os) throws IOException, WebApplicationException {				
					try {
						ServiceLocator.singleton().getFileService().createZipFile(instances, os);
					} catch (ServiceException e) {				
						e.printStackTrace();
					}                                				
				}
	        };       
	        String responseHeader =   "attachment; filename=\"" + studyIUID + ".zip\"";
	        Date finalTime = new Date();
	        Util.getLogger(this).debug("DONE - inicio " + format.format(initialTime) +" - fim " + format.format(finalTime));
	        return Response.ok(stream).header("Content-Disposition",responseHeader).build();
	        
		} else {
			return notFound();
		}
	}

}
