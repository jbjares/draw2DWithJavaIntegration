package ie.deri.editor.entity;

public class Arrow {
	
	private String id;
	
	private String type;
	
	private String label;
	
	private Boxe source;
	
	private Boxe target;
	
	public Arrow(String id){
		this.id=id;
	}
	
	public Arrow(){}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Boxe getSource() {
		return source;
	}

	public void setSource(Boxe source) {
		this.source = source;
	}

	public Boxe getTarget() {
		return target;
	}

	public void setTarget(Boxe target) {
		this.target = target;
	}
	
	

}
