/**
 * 
 */
package com.app.chart.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

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
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(value = Include.NON_NULL)
@JsonPropertyOrder({ "fileName", "folderName", "scrollTxt" })
public class CustomerFileBoundary implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6185089189910867241L;

	@JsonProperty("fileName")
	private String fileName;
	@JsonProperty("folderName")
	private String folderName;
	@JsonProperty("scrollTxt")
	private String scrollTxt;
}
