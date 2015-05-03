/*****************************************
 *   Library is under GPL License (GPL)
 *   Copyright (c) 2012 Andreas Herz
 ****************************************/
/**
 * @class draw2d.shape.state.State
 * 
 * NOT FOR PRODUCTIVE
 * 
 * 
 * @extends draw2d.shape.layout.VerticalLayout
 */
draw2d.shape.state.OntologyClass = draw2d.shape.layout.VerticalLayout.extend({

	NAME: "draw2d.shape.state.OntologyClass",
	
    init : function()
    {
        this._super();

        this.port = this.createPort("hybrid", new draw2d.layout.locator.BottomLocator(this));
        this.port.setConnectionAnchor(new draw2d.layout.anchor.ChopboxConnectionAnchor(this.port));

        
        this.setBackgroundColor("#f3f3f3");

        // UI representation
        this.setStroke(1);
        this.setColor("#22b366");
        this.setRadius(5);  
        
        // Compose the top row of the shape
        //
        var top = this.createLabel("Class").setStroke(0);        
        this.label = top;
        
        // the middle part of the shape
        // This part contains the ports for the connection
        //
        var center =  new draw2d.shape.basic.Rectangle();  
        center.getHeight= function(){return 1;};
        center.setMinWidth(90);
        center.setColor("#e0e0e0");
        
        // the bottom of the activity shape
        //
        var bottom = new draw2d.shape.basic.Rectangle();
        bottom.setMinHeight(30);
        bottom.setStroke(0);
        bottom.setBackgroundColor(null);

        // finally compose the shape with top/middle/bottom in VerticalLayout
        //
        this.addFigure(top);
        this.addFigure(center);
        this.addFigure(bottom);
        
        this.label = new draw2d.shape.basic.Label("Class Name");
        this.label.setColor("#0d0d0d");
        this.label.setFontColor("#0d0d0d");
   //     this.label.setBold(true);
        this.label.setBackgroundColor("#f0f0f0");
        
        // add the new decoration to the connection with a position locator.
        //
        this.addFigure(this.label, new draw2d.layout.locator.ManhattanMidpointLocator(this));
        
        // Register a label editor with a dialog
        //
        this.label.installEditor(new draw2d.ui.LabelEditor());
        

        
     },
     
     /**
      * @method
      * Set the text to show if the state shape
      * 
      * @param {String} text
      */
     setLabel: function (text)
     {
         this.label.setText(text);
         
         return this;
     },
     
     
     /**
      * @method
      * Return the label of the shape
      * 
      */
     getLabel: function ()
     {
         return this.label.getText();
     },
     
     
     /**
      * @method
      * helper method to create some labels
      * 
      * @param {String} txt the label to display
      * @returns {draw2d.shape.basic.Label}
      * @private
      */
     createLabel: function(txt){
    	 var label =new draw2d.shape.basic.Label(txt);
    	 label.setStroke(1);
    	 label.setColor(this.darkerBgColor);
    	 label.setRadius(0);
    	 label.setBackgroundColor(null);
    	 label.setPadding(5);
    	 label.setColor(this.bgColor.darker(0.2));
    	 label.onDoubleClick=function(angle){/* ignore them for the layout elements*/};
    	    
    	 return label;
     },
     

    /**
     * @method 
     * Return an objects with all important attributes for XML or JSON serialization
     * 
     * @returns {Object}
     */
    getPersistentAttributes : function()
    {
        var memento = this._super();

        memento.label = this.getLabel();
        
        return memento;
    },

    /**
     * @method 
     * Read all attributes from the serialized properties and transfer them into the shape.
     * 
     * @param {Object} memento
     * @returns 
     */
    setPersistentAttributes : function(memento)
    {
        this._super(memento);

        if(typeof memento.label !=="undefined"){
            this.setLabel(memento.label);
        }

    }
});
