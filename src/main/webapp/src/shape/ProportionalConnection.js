
/**
 * @class draw2d.shape.node.Start
 * 
 * A generic Node which has an OutputPort. Mainly used for demo and examples.
 * 
 * See the example:
 *
 *     @example preview small frame
 *     
 *     var figure =  new draw2d.shape.node.Start();
 *     figure.setColor("#3d3d3d");
 *     
 *     canvas.addFigure(figure,50,10);
 *     
 * @extends draw2d.shape.basic.Rectangle
 */
example.shape.ProportionalConnection = draw2d.shape.state.Connection.extend({

    NAME : "example.shape.ProportionalConnection",

	init : function()
    {
        this._super();
        

        this.label = new draw2d.shape.basic.Label("is proportional to");
        this.label.setColor("#0d0d0d");
        this.label.setFontColor("#0d0d0d");
   //     this.label.setBold(true);
        this.label.setBackgroundColor("#f0f0f0");
        
        // add the new decoration to the connection with a position locator.
        //
        this.addFigure(this.label, new draw2d.layout.locator.ManhattanMidpointLocator(this));
        
        this.setUserData( {condition:"IsTrue", variable:"activityResult"});
        
        var arrow = new draw2d.decoration.connection.ArrowDecorator(17,8);
        this.setTargetDecorator(arrow);
            
        this.setCssClass("AutoConnection");
    },
    
    /**
     * @method
     * validate all regular expression from this connection and set a corresponding
     * color for the connection if any errors are in.
     * 
     */
    validate: function(){

        return this;
    },
    
    /**
     * @method
     * Return all variables in the reqular expression group mapping
     * 
     * @return {Array}
     */
    getVariables: function(){
        return draw2d.util.ArrayList.EMPTY_LIST;
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

        this.validate();
    }
    

});
