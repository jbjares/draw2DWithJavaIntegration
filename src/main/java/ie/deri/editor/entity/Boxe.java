package ie.deri.editor.entity;


public class Boxe implements Comparable<Boxe>{
	
	private String id;
	
	private String type;
	
	private String code;
	
	private String label;
	
	private Boolean isLeaf;
	
	private Integer hits;
	
	public Boxe(String id){
		this.id=id;
	}
	
	public Boxe(){}
	

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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Boolean getIsLeaf() {
		return isLeaf;
	}

	public void setIsLeaf(Boolean isLeaf) {
		this.isLeaf = isLeaf;
	}

	@Override
	public int compareTo(Boxe boxe) {
		if(this.getId().equals(boxe.getId())){
			return 0;
		}
		return 1;
	}

	@Override
	public boolean equals(Object obj) {
		return this.getId().equals(((Boxe)obj).getId()) && this.getType().equals(((Boxe)obj).getType());
	}

	@Override
	public int hashCode() {
		String str = this.getId();
		str = str.replaceAll("[^\\d.]", "");
		str = str.substring(0,5);
		return new Integer(str);
	}

	public Integer getHits() {
		return hits;
	}

	public void setHits(Integer hits) {
		this.hits = hits;
	}

	

}
