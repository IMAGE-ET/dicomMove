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

package br.ufpb.dicomflow.bean;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="series")
public interface SeriesIF {


	public String getSeriesIuid();

	public void setSeriesIuid(String seriesIuid);

	public String getSeriesNumber();

	public void setSeriesNumber(String seriesNumber);

	public String getModality();

	public void setModality(String modality);

	public String getBodyPartExamined();

	public void setBodyPartExamined(String bodyPartExamined);
	
	public String getSeriesDescription();
	
	public void setSeriesDescription(String seriesDescription);
	
	public String getStationName();
	
	public void setStationName(String stationName);
	
	public String getInstitution();
	
	public void setInstitution(String institution);

	
	
}