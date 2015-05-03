package ie.deri.editor.entity;

import java.util.ArrayList;
import java.util.List;

public class Editor {
	
	private List<Boxe> boxes = new ArrayList<Boxe>();
	
	private List<Arrow> arrows = new ArrayList<Arrow>();

	public List<Boxe> getBoxes() {
		return boxes;
	}

	public void setBoxes(List<Boxe> boxes) {
		this.boxes = boxes;
	}

	public List<Arrow> getArrows() {
		return arrows;
	}

	public void setArrows(List<Arrow> arrows) {
		this.arrows = arrows;
	}
	
	public Boxe getBoxeById(String id){
		for(Boxe boxe:getBoxes()){
			if(boxe.getId().equals(id)){
				return boxe;
			}
		}
		throw new RuntimeException("Boxe not foud for inpputed id [ "+id+" ]");
	}
	
	public Boxe getBoxeByLabel(String label){
		for(Boxe boxe:getBoxes()){
			if(boxe.getLabel().equals(label)){
				return boxe;
			}
		}
		throw new RuntimeException("Boxe not foud for inpputed label [ "+label+" ]");
	}
	
	
	
	public Arrow getArrow(Boxe source,Boxe target){
		for(Arrow arrow:getArrows()){
			if(arrow.getSource().getId().equals(source.getId()) && arrow.getTarget().getId().equals(target.getId())){
				return arrow;
			}			
		}
		throw new RuntimeException("Arrow not foud for inpputed Boxe source id [ "+source.getId()+" ] and Boxe target id [ "+target.getId()+" ]");
	}

}
