package ie.deri.editor.entity;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class EditorState{
	
	@XmlElement
	private String name;
	
	@XmlElement
	private String id; 
	
	@XmlElement
	private String content;
	
	private String state;
	
	public EditorState(){}
	
	public EditorState(String state){
		this.state=state;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	


}
