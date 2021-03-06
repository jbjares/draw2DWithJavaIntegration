

example.PropertyPane = Class.extend({
	
	init:function(elementId, view){
	    this.selectedFigure = null;
        this.html = $("#"+elementId);
        this.view = view;
        this.pane = null;
        this.view.addSelectionListener(this);
        
        // register as listener to update the property pane if anything has been changed in the model
        //
        view.getCommandStack().addEventListener($.proxy(function(event){
            if(event.isPostChangeEvent()){
                this.onSelectionChanged(this.selectedFigure);
            }
        },this));
	},
	
    /**
     * @method
     * Called if the selection in the canvas has been changed. You must register this
     * class on the canvas to receive this event.
     * 
     * @param {draw2d.Figure} figure
     */
    onSelectionChanged : function(figure){
        this.selectedFigure = figure;
        
        if(this.pane!==null){
            this.pane.onHide();
        }
        
        this.html.html("");

        if(figure===null){
            return;
        }
        this.pane = null;
        //console.log("Nome da figura: "+figure.NAME);
        switch(figure.NAME)
        {
//            case "draw2d.shape.state.OntologyClass":
//                this.pane = new example.propertypane.PropertyPaneState(figure);
//                break;
            case "draw2d.shape.state.OntologyAttribute":
                this.pane = new example.propertypane.PropertyPaneAttribute(figure);
                break;
            case "draw2d.shape.state.OntologyMainAttribute":
                this.pane = new example.propertypane.PropertyPaneAttribute(figure);
                break;
//            case "example.shape.Start":
//                this.pane = new example.propertypane.PropertyPaneStart(figure);
//                break;
//            case "draw2d.shape.state.End":
//                this.pane = new example.propertypane.PropertyPaneEnd(figure);
//                break;
//            case "example.shape.Activity":
//                this.pane = new example.propertypane.PropertyPaneActivity(figure);
//                break;
//            case "example.shape.RegExConnection":
//                this.pane = new example.propertypane.PropertyPaneRegExConnection(figure);
//                break;
//            case "example.shape.AutoConnection":
//                this.pane = new example.propertypane.PropertyPaneAutoConnection(figure);
//                break;
            default:
                break;
        }
        
        if(this.pane!==null){
            this.pane.injectPropertyView(this.html);
        }
    },
    
    onResize: function()
    {
        if(this.pane!==null){
            this.pane.onResize();
        }
    }
    
});

