/**
 * 
 */
package com.app.chart.model;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRootName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Sandeep
 *
 */
@Getter
@Setter
@ToString(includeFieldNames = true)
@NoArgsConstructor
@JsonInclude(value = Include.NON_NULL)
@JsonPropertyOrder({ "rootName", "subBoundaries" })
@JsonRootName("SunburstBoundary")
public class SunburstBoundary implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2790625990401646842L;
	@JsonProperty("rootName")
	private String rootName;
	@JsonProperty("subBoundaries")
	private List<ReleaseBoundary> subBoundaries;

	@Getter
	@Setter
	@ToString(includeFieldNames = true)
	@NoArgsConstructor
	@JsonInclude(value = Include.NON_NULL)
	@AllArgsConstructor
	@JsonPropertyOrder({ "fieldName", "scores", "color", "attrBoundaries" })
	@JsonRootName("ReleaseBoundary")
	public static class ReleaseBoundary implements Serializable{

		/**
		 * 
		 */
		private static final long serialVersionUID = 4676147787739669059L;
		@JsonProperty("fieldName")
		private String fieldName;
		@JsonProperty("scores")
		private double scores;
		@JsonProperty("color")
		private CustomColor color;
		@JsonProperty("attrBoundaries")
		private List<ReleaseAttrBoundary> attrBoundaries;

		@Getter
		@Setter
		@ToString(includeFieldNames = true)
		@NoArgsConstructor
		@JsonInclude(value = Include.NON_NULL)
		@JsonPropertyOrder({ "fieldName", "scores", "color" })
		@AllArgsConstructor
		@JsonRootName("ReleaseAttrBoundary")
		public static class ReleaseAttrBoundary implements Serializable{

			/**
			 * 
			 */
			private static final long serialVersionUID = -8904329717562890925L;
			@JsonProperty("fieldName")
			private String fieldName;
			@JsonProperty("scores")
			private double scores;
			@JsonProperty("color")
			private CustomColor color;
		}
	}

}
