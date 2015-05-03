/*****************************************
 *   Library is under GPL License (GPL)
 *   Copyright (c) 2012 Andreas Herz
 ****************************************/
/**
 * @class draw2d.shape.basic.Label
 * Implements a simple text label.
 * 
 * See the example:
 *
 *     @example preview small frame
 *     
 *     var shape =  new draw2d.shape.basic.Label("This is a simple label");
 *          
 *     canvas.addFigure(shape,40,10);
 *     
 * @author Andreas Herz
 * 
 * @extends draw2d.SetFigure
 */
draw2d.shape.basic.Code= draw2d.SetFigure.extend({

	NAME : "draw2d.shape.basic.Code",      

    /**
     * @constructor
     * Creates a new text element.
     * 
     * @param {String} [text] the text to display
     */
    init : function(text)
    {
        this._super();
        
        if(typeof text === "string"){
    		this.text = text;
    	}
    	else{
    		this.text = "";
    	}
        
        //this.installEditPolicy(new draw2d.policy.figure.AntSelectionFeedbackPolicy());
    },
    

    

    
    /**
     * @method
     * Returns the current text of the label.
     *
     * @returns the current display text of the label
     * @type String
     **/
    getText:function()
    {
      return this.text;
    },
    
    /**
     * @method
     * Set the text for the label. Use \n for multiline text.
     * 
     * @param {String} text The new text for the label.
     **/
    setText:function( text )
    {
      this.text = text;
      return this;
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
         
         memento.code = this.code;

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
         if(typeof memento.code !=="undefined"){
             this.setText(memento.code);
         }

     }

});



