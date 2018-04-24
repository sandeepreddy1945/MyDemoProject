/**
 * 
 */
package com.app.chart.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Sandeep Person Details Pojo
 *
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
public class PersonDetails implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = 1174234031239763329L;
	
	private long portalId;
	private String name;
	private String description;
	private String title;
	private String phoneNum;
	private String emailId;
	private String htmlClass;
	private String parent;
	private String image;
	private String imageData;

}
