package ie.deri.editor.entity;

import java.io.Serializable;

public class Resource implements Serializable{

	private static final long serialVersionUID = -1992041163030288831L;

	private String id;
	
	private String name;

	public Resource(){}
	
	public Resource(String id, String name) {
		this.id=id;
		this.name=name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


}
