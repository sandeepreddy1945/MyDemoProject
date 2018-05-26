/**
 * 
 */
package com.app.chart.model;

import java.io.Serializable;
import java.util.List;

import eu.hansolo.tilesfx.chart.ChartData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Sandeep Reddy Battula
 *
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ReleaseBoardBoundary implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4216088667961733680L;
	private List<SunburstBoundary> sunburstBoundaries;
	private List<ChartData> radialBoundaries;
	private List<ChartData> donutBoundaries;
}
