package ie.deri.editor.service.rest;


import ie.deri.editor.entity.Arrow;
import ie.deri.editor.entity.Boxe;
import ie.deri.editor.entity.Editor;
import ie.deri.editor.entity.EditorState;
import ie.deri.editor.entity.Tree;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;



@Path("/editor")
public class RedHunterEditorRestService {
	

	private static final String REPLACEMENT_NECESSARY = "@ReplacementNecessary";
	private static final String RELATIONSHIP_DEPENDENCY = "@RelationshipDependency";
	private static final String BAR = "/";
	private static final String EDITOR_REST_SERVICE_CLASS = "RedHunterEditorRestService.class";
	private static final String LINE_SEPARATOR = "line.separator";
	private static final String BAR_BAR = "\\";
	private static final String EDITOR_JS = "editor.js";
	private static final String FUNCTION = "function";
	private static final String JS = "js";
	private static final String DRAW2D_SHAPE_STATE_ONTOLOGY_CLASS = "draw2d.shape.state.OntologyClass";
	private static final String GRAPH_WITHOUT_MAIN_ATTRIBUTE = "Graph without main attribute.";
	private static final String DRAW2D_SHAPE_STATE_ONTOLOGY_MAIN_ATTRIBUTE = "draw2d.shape.state.OntologyMainAttribute";
	private static final String SET = "set";
	private static final String CONNECTION = "Connection";
	private static final String STATE_ONTOLOGY = "state.Ontology";
	private static final String TYPE = "type";
	private static final String CONTENT = "content";
	private static final String USER_DATA_ = "userData=";
	private static final String USER_DATA = "userData";
	private static final String SOURCE_ = "source=";
	private static final String SOURCE = "source";
	private static final String TARGET_ = "target=";
	private static final String TARGET = "target";
	private static final String VAZIO = "";
	private static final String CODE_ = "code=";
	private static final String CODE = "code";
	private static final String UTF_8 = "UTF-8";
	private static final String DATA = "\\data";
	private static final String DATA_JSON = "\\data.json";
	private static final String WEB_INF = "WEB-INF";

	private String datapath;
	
	private String jsonInputAsString;
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/save")
	public String postSaveEditorStateAsJsonFile(EditorState currentState) {
		try{
			System.out.println(currentState.getState());
			File dataDir = new File(getWebInfPath()+DATA);
			dataDir.mkdirs();
			String state = VAZIO;
			if(currentState.getState()!=null && !VAZIO.equals(currentState.getState())){
				state = currentState.getState();	
			}else{
				state = new GsonBuilder().create().toJson(currentState);
			}
			if(getDatapath()==null ||VAZIO.equals(getDatapath())){
				FileUtils.writeStringToFile(new File(getWebInfPath()+DATA+DATA_JSON),state,UTF_8);	
			}else{
				FileUtils.writeStringToFile(new File(getDatapath()),state,UTF_8);
			}
		
			
			//return getChartData(state);
			return "OK";
		}catch(Exception e){
			throw new RuntimeException(e.getMessage(),e);
		}
	}
	
	public Map<String,String> getChartData(String state) throws Exception {
		List<Map<String,String>> mappedState = getMappedState(state);
		Editor editor = getEditor(mappedState);
		Tree<Boxe> processTree = getProcessTree(editor);
		Map<String,String> result = processChart(processTree,editor);
		return result;
	}
	
	public Map<String,String> getChartGrade(String state) throws Exception {
		List<Map<String,String>> mappedState = getMappedState(state);
		Editor editor = getEditor(mappedState);
		Tree<Boxe> processTree = getProcessTree(editor);
		Map<String,String> result = processChart(processTree,editor);
		return result;
	}
	
	
	private Map<String,String> processChart(Tree<Boxe> processTree,Editor editor) throws Exception {
		LinkedHashMap<String,String> result = new java.util.LinkedHashMap<String, String>();
		
		
		//TODO Enable transpaarent way to do it
		Boxe ageBoxe = editor.getBoxeByLabel("age");
		String functionResult = functionResolver(ageBoxe.getCode(),null);
		Double age = new Double(functionResult);
		
		double k;
		for (k=0; k<=age; k ++) {
			File jsFile = createOrUpdateJSFile(processTree,editor,k);
			String value = functionFileResolver(jsFile);  
			result.put(new Double(k).toString(), value);
			System.out.println("x: "+k+" y: "+value);
		}

		return result;
	}

	

	private File createOrUpdateJSFile(Tree<Boxe> processTree,Editor editor, Double k) throws IOException {
		Boxe boxe = null;
		
		//order
		StringBuilder sb = new StringBuilder();
		String[] process = processTree.toString().split(";");
		sb.append(System.getProperty(LINE_SEPARATOR));
		for(int i=process.length-1;i>=0;i--){
			String id = process[i];
			boxe = editor.getBoxeById(id);			
			String code = boxe.getCode().replace("\\n",System.getProperty(LINE_SEPARATOR));
			code = code.replace("\\t","  ");
			if(code.indexOf(FUNCTION)==-1){
				continue;
			}
			String functionNameAssign = code.substring(code.indexOf(FUNCTION),code.indexOf(")")+1).trim(); 
			int indexOfFunctionName = sb.indexOf(functionNameAssign);
			if(indexOfFunctionName!=-1){
				continue;
			}
			
			//TODO refactor this hard code.
			if(functionNameAssign.equals("function age()")){
				code = code.replace("10.0",k.toString());
			}
			
			code = parseCodeMetaData(code,editor);
			if(functionNameAssign.equals("function size(age,temperature,foodLevel,oxygenLevel)")){
				System.out.println(code);
			}
			sb.append(code);
			sb.append(System.getProperty(LINE_SEPARATOR));
		}
		File result = new File(getWebInfPath()+BAR_BAR+JS+BAR_BAR+EDITOR_JS);
		//File result = new File("C:\\ProjetosWS\\DeriConceptualModelWEB3\\ie.deri.editor\\WebContent\\"+EDITOR_JS);
		FileUtils.writeStringToFile(result,sb.toString(),UTF_8);
		return result;
	}



	private String parseCodeMetaData(String code, Editor editor) {
//		@RelationshipDependency(name->|sizeToAge| source->|size| target->|age|)
//		@RelationshipDependency(name->|sizeToTemperature| source->|size| target->|temperature|)
//		@RelationshipDependency(name->|sizeToFoodLevel| source->|size| target->|foodLevel|)
//		@RelationshipDependency(name->|sizeToFoodLevel| source->|size| target->|foodLevel|)
//		@ReplacementNecessary(fromDefault->|size = size-age+temperature+foodLevel| toDefinedVarEval->|size = size ${sizeToAge} age ${sizeToTemperature}temperature ${sizeToFoodLevel} foodLevel|)

		Map<String,String> relationshipDependencyMap = new TreeMap<String,String>();
		Map<String,String> replacementNecessaryMap = new TreeMap<String,String>();
		
		while(code.contains(RELATIONSHIP_DEPENDENCY)){

			String tempCode = code.replace(code.substring(0,code.indexOf("{")+1),"") ;
			tempCode = tempCode.substring(0,tempCode.indexOf(")")+1);
			code = code.replace(tempCode,"");
			tempCode = tempCode.replaceAll(RELATIONSHIP_DEPENDENCY,"").trim();
			tempCode = tempCode.replaceAll("\\(","");
			tempCode = tempCode.replaceAll("\\)","");
			
			String name = tempCode.substring(tempCode.indexOf("name->|"),tempCode.indexOf("source"));
			name = name.replaceAll("name->","");
			name = name.replaceAll("\\|","");

			//@RelationshipDependency(name->|sizeToAge| source->|size| target->|age|)
			String source = tempCode.substring(tempCode.indexOf("source->|"),tempCode.indexOf("target"));
			source = source.replaceAll("source->","");
			source = source.replaceAll("\\|","");
			
			
			String target = tempCode.substring(tempCode.indexOf("target->|"),tempCode.length());
			target = target.replaceAll("target->","");
			target = target.replaceAll("\\|","");
			
			relationshipDependencyMap.put(name.trim(),source.trim()+";"+target.trim());
		}

		
		while(code.contains(REPLACEMENT_NECESSARY)){
			String tempCode = code.replace(code.substring(0,code.indexOf("{")+1),"") ;
			tempCode = tempCode.substring(0,tempCode.indexOf(")")+1);
			code = code.replace(tempCode,"");
			tempCode = tempCode.replaceAll(REPLACEMENT_NECESSARY,"").trim();
			tempCode = tempCode.replaceAll("\\(","");
			tempCode = tempCode.replaceAll("\\)","");
			
			String from = tempCode.substring(tempCode.indexOf("fromDefault->|"),tempCode.indexOf("toDefinedVarEval"));
			from = from.replaceAll("fromDefault->|","");
			from = from.replaceAll("\\|","");
			

			String to = tempCode.substring(tempCode.indexOf("toDefinedVarEval->|"),tempCode.length());
			to = to.replaceAll("toDefinedVarEval->","");
			to = to.replaceAll("\\|","");
			
			replacementNecessaryMap.put(from,to);			
		}

//		@ReplacementNecessary(fromDefault->|size = size-age+temperature+foodLevel| 
//      toDefinedVarEval->|size = size ${sizeToAge} age ${sizeToTemperature}temperature ${sizeToFoodLevel} foodLevel|)
		for(Map.Entry<String,String> entry:replacementNecessaryMap.entrySet()){
			String from = entry.getKey();
			String to = entry.getValue();
			
			while(to.contains("${")){
				String varNameToReplace = to.substring(to.indexOf("${"),to.indexOf("}")+1).trim();
				String varNameToSearch = to.substring(to.indexOf("${")+2,to.indexOf("}")).trim();
				to = to.replace(varNameToReplace,findOperatorByRelationship(varNameToSearch,relationshipDependencyMap,editor));
			}
				code = code.replace(from.trim(),to.trim());
		}
		return code;
	}


	private String findOperatorByRelationship(String varNameToSearch,Map<String, String> relationshipDependencyMap, Editor editor) {
		String[] sourceTarget = relationshipDependencyMap.get(varNameToSearch).split(";");
		Boxe source = editor.getBoxeByLabel(sourceTarget[0]);
		Boxe target = editor.getBoxeByLabel(sourceTarget[1]);
		Arrow arrow = editor.getArrow(source, target);
		
		if("example.shape.IncreaseConnection".equals(arrow.getType())){
			return "+";
		}
		if("example.shape.DecreaseConnection".equals(arrow.getType())){
			return "-";
		}
		
		//TODO Look for exception case
		return "+";
	}

	private String functionFileResolver(File jsFile) throws Exception {
        Reader reader = new FileReader(jsFile);
	    ScriptEngineManager manager = new ScriptEngineManager();
	    ScriptEngine engine = manager.getEngineByName(JS);
	    Object evalresult = engine.eval(reader);
	    if(evalresult==null){
	    	return "";
	    }else{
	    	String eval = evalresult.toString();
	    	return eval;	    	
	    }
	}
	
	private String functionResolver(String code,String... args) throws Exception {
		ScriptEngineManager manager = new ScriptEngineManager();
	    ScriptEngine engine = manager.getEngineByName(JS);
	    Invocable inv = (Invocable) engine;  
		code = code.replace("\\n",System.getProperty(LINE_SEPARATOR));
		code = code.replace("\\t","  ");
		if(code.indexOf(FUNCTION)==-1){
			//TODO look for exception cases
			return "[ERROR - Malformed function]";
		}
	    String functionName = code.substring(code.indexOf(FUNCTION)+8,code.indexOf("(")).trim();
//	    String functionNameAssign = code.substring(code.indexOf(FUNCTION),code.indexOf(")")+1).trim();
//	    String[] argArr = code.substring(code.indexOf("(")+1,code.indexOf(")")).trim().split(",");
//	    for(int i=0;i<argArr.length;i++){
//	    	System.out.println(argArr[i]);
//	    }
//	    System.out.println(functionName);
//        System.out.println();
	    engine.eval(code);
        String result = inv.invokeFunction(functionName,args).toString();
        return result;
	}


	private Tree<Boxe> getProcessTree(Editor editor) {
		Tree<Boxe> processTree = null;
		for(Boxe boxe:editor.getBoxes()){
			if(DRAW2D_SHAPE_STATE_ONTOLOGY_MAIN_ATTRIBUTE.equals(boxe.getType())){
				processTree = new Tree<Boxe>(boxe); 
			}
		}
		if(processTree==null){
			throw new RuntimeException(GRAPH_WITHOUT_MAIN_ATTRIBUTE);
		}
		
		for(Arrow arrow:editor.getArrows()){
			Boxe source = editor.getBoxeById(arrow.getSource().getId());
			Boxe target = editor.getBoxeById(arrow.getTarget().getId());
			if(DRAW2D_SHAPE_STATE_ONTOLOGY_CLASS.equals(source.getType())){
				continue;
			}
			processTree.addLeaf(source,target);
		}
		
		return processTree;
	}

	Editor getEditor(List<Map<String, String>> mappedState) throws Exception {
		Editor editor = new Editor();
		for(Map<String,String> state:mappedState){
			if(state.get(TYPE).contains(STATE_ONTOLOGY)){
				editor.getBoxes().add(getBoxeObj(state));
			}
			if(state.get(TYPE).endsWith(CONNECTION)){
				editor.getArrows().add(getArrowObj(state));
			}
		}
		return editor;
	}

	private Arrow getArrowObj(Map<String, String> state) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Arrow arrow = new Arrow();
		for(Map.Entry<String,String> entry:state.entrySet()){
			String nomeAtributo = entry.getKey();
			if(nomeAtributo==null || VAZIO.equals(nomeAtributo)){
				continue;
			}
			try{
				Method method = null;
				String nomeMetodo = nomeAtributo.substring(0,1).toUpperCase();
				nomeMetodo = nomeMetodo+nomeAtributo.substring(1,nomeAtributo.length());
				nomeMetodo = SET+nomeMetodo;
				if(SOURCE.equals(nomeAtributo) || TARGET.equals(nomeAtributo)){
					String boxeId = getBoxeId(entry.getValue());
					Boxe boxe = new Boxe(boxeId);
					method = Arrow.class.getDeclaredMethod(nomeMetodo,Boxe.class);
					method.invoke(arrow,boxe);
					continue;
				}else{
					method = Arrow.class.getDeclaredMethod(nomeMetodo,String.class);					
					method.invoke(arrow, entry.getValue());	
				}
				
			}catch(NoSuchMethodException nsme){continue;}
		}
		return arrow;
	}

	private String getBoxeId(String value) {
		String id = value;
		id = id.replace("{node=",VAZIO);
		id = id.substring(0,id.lastIndexOf(","));
		return id;
	}

	private Boxe getBoxeObj(Map<String, String> state) throws SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Boxe boxe = new Boxe();
		for(Map.Entry<String,String> entry:state.entrySet()){
			try{
				String nomeAtributo = entry.getKey();
				String nomeMetodo = nomeAtributo.substring(0,1).toUpperCase();
				nomeMetodo = nomeMetodo+nomeAtributo.substring(1,nomeAtributo.length());
				nomeMetodo = SET+nomeMetodo;
				Method metodo = Boxe.class.getDeclaredMethod(nomeMetodo,String.class);
				metodo.invoke(boxe, entry.getValue());				
			}catch(NoSuchMethodException nsme){continue;}
		}
		return boxe;
	}

	public List<Map<String,String>> getMappedState(String state) throws IOException {
		//String json = "{\"name\":\"data\",\"id\":\"data\",\"content\":\"[{\"type\":\"draw2d.shape.state.OntologyClass\",\"id\":\"3f3a4f56-fb37-86f5-5be0-1581067fb51e\",\"x\":14,\"y\":265,\"width\":91,\"height\":86,\"userData\":{},\"cssClass\":\"draw2d_shape_state_OntologyClass\",\"bgColor\":\"#F3F3F3\",\"color\":\"#22B366\",\"stroke\":1,\"alpha\":1,\"radius\":5,\"label\":\"fishPopulation\"},{\"type\":\"draw2d.shape.state.OntologyAttribute\",\"id\":\"d0b98d5e-dcd0-823a-ab8b-b0fa948edd26\",\"x\":199,\"y\":20,\"width\":91,\"height\":86,\"userData\":{},\"cssClass\":\"draw2d_shape_state_OntologyAttribute\",\"bgColor\":\"#F3F3F3\",\"color\":\"#228CB3\",\"stroke\":1,\"alpha\":1,\"radius\":5,\"label\":\"age\",\"code\":\"def age():Double = {return 10.0;}\"},{\"type\":\"draw2d.shape.state.OntologyMainAttribute\",\"id\":\"bb9ae295-61c3-5f10-9052-87934bf07bbd\",\"x\":444,\"y\":252,\"width\":91,\"height\":86,\"userData\":{},\"cssClass\":\"draw2d_shape_state_OntologyMainAttribute\",\"bgColor\":\"#F3F3F3\",\"color\":\"#DB0029\",\"stroke\":1,\"alpha\":1,\"radius\":5,\"label\":\"size\",\"code\":\"   def size(age:Double,temperature:Double,foodeLevel:Double,oxygenLevel:Double):Double = {\\n     var size = 0.0;\\n     size = size-age+temperature+foodLevel;\\n     \\n     if(oxygenLevel<0.5){\\n       size=size/2;\\n     }\\n     if(oxygenLevel>=0.5){\\n       size=size*2;\\n     }\\n     return size;\\n   }\"},{\"type\":\"draw2d.shape.state.OntologyAttribute\",\"id\":\"4e1447e6-1deb-9717-a90e-1a6eb4d1818f\",\"x\":150,\"y\":519,\"width\":106,\"height\":86,\"userData\":{},\"cssClass\":\"draw2d_shape_state_OntologyAttribute\",\"bgColor\":\"#F3F3F3\",\"color\":\"#228CB3\",\"stroke\":1,\"alpha\":0.4,\"radius\":5,\"label\":\"reproductionRate\",\"code\":\"   def reproductionRate(temperature:Double):Double = {\\n     var reproductionRate = 0.5+(temperature/100);\\n     return reproductionRate;\\n   }\"},{\"type\":\"draw2d.shape.state.OntologyAttribute\",\"id\":\"51983843-fd23-aef2-ae46-a34d6bc45ecc\",\"x\":749,\"y\":499,\"width\":91,\"height\":86,\"userData\":{},\"cssClass\":\"draw2d_shape_state_OntologyAttribute\",\"bgColor\":\"#F3F3F3\",\"color\":\"#228CB3\",\"stroke\":1,\"alpha\":1,\"radius\":5,\"label\":\"temperature\",\"code\":\"def temperature():Double = {return 30.0;}\"},{\"type\":\"draw2d.shape.state.OntologyAttribute\",\"id\":\"f8703a6a-cd6f-b058-729f-b86a661f7e2a\",\"x\":776,\"y\":18,\"width\":91,\"height\":86,\"userData\":{},\"cssClass\":\"draw2d_shape_state_OntologyAttribute\",\"bgColor\":\"#F3F3F3\",\"color\":\"#228CB3\",\"stroke\":1,\"alpha\":1,\"radius\":5,\"label\":\"foodLevel\",\"code\":\"def foodLevel():Double = {return 0.3;}\"},{\"type\":\"draw2d.shape.state.OntologyAttribute\",\"id\":\"52ad7c86-6fd3-06ab-12f9-41356d5fdc2d\",\"x\":827,\"y\":236,\"width\":91,\"height\":86,\"userData\":{},\"cssClass\":\"draw2d_shape_state_OntologyAttribute\",\"bgColor\":\"#F3F3F3\",\"color\":\"#228CB3\",\"stroke\":1,\"alpha\":1,\"radius\":5,\"label\":\"oxygenLevel\",\"code\":\"def oxygenLevel():Double = {return 0.5;}\"},{\"type\":\"draw2d.shape.state.OntologyClass\",\"id\":\"f462cb01-7684-bc9e-6ae9-55e0c7af4a40\",\"x\":1120,\"y\":292,\"width\":91,\"height\":86,\"userData\":{},\"cssClass\":\"draw2d_shape_state_OntologyClass\",\"bgColor\":\"#F3F3F3\",\"color\":\"#22B366\",\"stroke\":1,\"alpha\":1,\"radius\":5,\"label\":\"environment\"},{\"type\":\"example.shape.HasConnection\",\"id\":\"277ad0f9-485a-9a5e-a3ca-4094b1bf6460\",\"userData\":{\"condition\":\"IsTrue\",\"variable\":\"activityResult\"},\"cssClass\":\"RegExConnection\",\"stroke\":2,\"color\":\"#1B1B1B\",\"outlineStroke\":0,\"outlineColor\":\"none\",\"policy\":\"draw2d.policy.line.LineSelectionFeedbackPolicy\",\"router\":\"draw2d.layout.connection.FanConnectionRouter\",\"radius\":2,\"source\":{\"node\":\"3f3a4f56-fb37-86f5-5be0-1581067fb51e\",\"port\":\"hybrid0\"},\"target\":{\"node\":\"d0b98d5e-dcd0-823a-ab8b-b0fa948edd26\",\"port\":\"hybrid0\"},\"label\":\"has\"},{\"type\":\"example.shape.HasConnection\",\"id\":\"d20d4804-a015-8f09-de0b-570c8d75249a\",\"userData\":{\"condition\":\"IsTrue\",\"variable\":\"activityResult\"},\"cssClass\":\"RegExConnection\",\"stroke\":2,\"color\":\"#1B1B1B\",\"outlineStroke\":0,\"outlineColor\":\"none\",\"policy\":\"draw2d.policy.line.LineSelectionFeedbackPolicy\",\"router\":\"draw2d.layout.connection.FanConnectionRouter\",\"radius\":2,\"source\":{\"node\":\"3f3a4f56-fb37-86f5-5be0-1581067fb51e\",\"port\":\"hybrid0\"},\"target\":{\"node\":\"bb9ae295-61c3-5f10-9052-87934bf07bbd\",\"port\":\"hybrid0\"},\"label\":\"has\"},{\"type\":\"example.shape.HasConnection\",\"id\":\"029c5674-eeae-ca08-0794-23698be6d605\",\"userData\":{\"condition\":\"IsTrue\",\"variable\":\"activityResult\"},\"cssClass\":\"RegExConnection\",\"stroke\":2,\"color\":\"#1B1B1B\",\"outlineStroke\":0,\"outlineColor\":\"none\",\"policy\":\"draw2d.policy.line.LineSelectionFeedbackPolicy\",\"router\":\"draw2d.layout.connection.FanConnectionRouter\",\"radius\":2,\"source\":{\"node\":\"3f3a4f56-fb37-86f5-5be0-1581067fb51e\",\"port\":\"hybrid0\"},\"target\":{\"node\":\"4e1447e6-1deb-9717-a90e-1a6eb4d1818f\",\"port\":\"hybrid0\"},\"label\":\"has\"},{\"type\":\"example.shape.IncreaseConnection\",\"id\":\"a4d02391-dee5-01af-6a75-c9cd6f5ba6bf\",\"userData\":{\"condition\":\"IsTrue\",\"variable\":\"activityResult\"},\"cssClass\":\"AutoConnection\",\"stroke\":2,\"color\":\"#1B1B1B\",\"outlineStroke\":0,\"outlineColor\":\"none\",\"policy\":\"draw2d.policy.line.LineSelectionFeedbackPolicy\",\"router\":\"draw2d.layout.connection.FanConnectionRouter\",\"radius\":2,\"source\":{\"node\":\"bb9ae295-61c3-5f10-9052-87934bf07bbd\",\"port\":\"hybrid0\"},\"target\":{\"node\":\"51983843-fd23-aef2-ae46-a34d6bc45ecc\",\"port\":\"hybrid0\"},\"label\":\"increases with\"},{\"type\":\"example.shape.IncreaseConnection\",\"id\":\"279014be-9906-ab0e-dcf8-b1a96da5800e\",\"userData\":{\"condition\":\"IsTrue\",\"variable\":\"activityResult\"},\"cssClass\":\"AutoConnection\",\"stroke\":2,\"color\":\"#1B1B1B\",\"outlineStroke\":0,\"outlineColor\":\"none\",\"policy\":\"draw2d.policy.line.LineSelectionFeedbackPolicy\",\"router\":\"draw2d.layout.connection.FanConnectionRouter\",\"radius\":2,\"source\":{\"node\":\"4e1447e6-1deb-9717-a90e-1a6eb4d1818f\",\"port\":\"hybrid0\"},\"target\":{\"node\":\"51983843-fd23-aef2-ae46-a34d6bc45ecc\",\"port\":\"hybrid0\"},\"label\":\"increases with\"},{\"type\":\"example.shape.DecreaseConnection\",\"id\":\"1b6b50d8-917d-e1e7-d217-3ab31d67c565\",\"userData\":{\"condition\":\"IsTrue\",\"variable\":\"activityResult\"},\"cssClass\":\"AutoConnection\",\"stroke\":2,\"color\":\"#1B1B1B\",\"outlineStroke\":0,\"outlineColor\":\"none\",\"policy\":\"draw2d.policy.line.LineSelectionFeedbackPolicy\",\"router\":\"draw2d.layout.connection.FanConnectionRouter\",\"radius\":2,\"source\":{\"node\":\"bb9ae295-61c3-5f10-9052-87934bf07bbd\",\"port\":\"hybrid0\"},\"target\":{\"node\":\"d0b98d5e-dcd0-823a-ab8b-b0fa948edd26\",\"port\":\"hybrid0\"},\"label\":\"decreases with\"},{\"type\":\"example.shape.DependsConnection\",\"id\":\"1fe9dbfd-8ec2-22d8-43a6-b59d6e59b15c\",\"userData\":{\"condition\":\"IsTrue\",\"variable\":\"activityResult\"},\"cssClass\":\"AutoConnection\",\"stroke\":2,\"color\":\"#1B1B1B\",\"outlineStroke\":0,\"outlineColor\":\"none\",\"policy\":\"draw2d.policy.line.LineSelectionFeedbackPolicy\",\"router\":\"draw2d.layout.connection.FanConnectionRouter\",\"radius\":2,\"source\":{\"node\":\"bb9ae295-61c3-5f10-9052-87934bf07bbd\",\"port\":\"hybrid0\"},\"target\":{\"node\":\"52ad7c86-6fd3-06ab-12f9-41356d5fdc2d\",\"port\":\"hybrid0\"},\"label\":\"depends on\"},{\"type\":\"example.shape.IncreaseConnection\",\"id\":\"b38a8838-5747-e8e2-03f7-42865fe1cdcd\",\"userData\":{\"condition\":\"IsTrue\",\"variable\":\"activityResult\"},\"cssClass\":\"AutoConnection\",\"stroke\":2,\"color\":\"#1B1B1B\",\"outlineStroke\":0,\"outlineColor\":\"none\",\"policy\":\"draw2d.policy.line.LineSelectionFeedbackPolicy\",\"router\":\"draw2d.layout.connection.FanConnectionRouter\",\"radius\":2,\"source\":{\"node\":\"bb9ae295-61c3-5f10-9052-87934bf07bbd\",\"port\":\"hybrid0\"},\"target\":{\"node\":\"f8703a6a-cd6f-b058-729f-b86a661f7e2a\",\"port\":\"hybrid0\"},\"label\":\"increases with\"},{\"type\":\"example.shape.HasConnection\",\"id\":\"501ff51f-9015-af4d-ef59-90997bb53ae6\",\"userData\":{\"condition\":\"IsTrue\",\"variable\":\"activityResult\"},\"cssClass\":\"RegExConnection\",\"stroke\":2,\"color\":\"#1B1B1B\",\"outlineStroke\":0,\"outlineColor\":\"none\",\"policy\":\"draw2d.policy.line.LineSelectionFeedbackPolicy\",\"router\":\"draw2d.layout.connection.FanConnectionRouter\",\"radius\":2,\"source\":{\"node\":\"f462cb01-7684-bc9e-6ae9-55e0c7af4a40\",\"port\":\"hybrid0\"},\"target\":{\"node\":\"51983843-fd23-aef2-ae46-a34d6bc45ecc\",\"port\":\"hybrid0\"},\"label\":\"has\"},{\"type\":\"example.shape.HasConnection\",\"id\":\"07a46a58-b579-6f7f-aab8-d6ceb5a7612b\",\"userData\":{\"condition\":\"IsTrue\",\"variable\":\"activityResult\"},\"cssClass\":\"RegExConnection\",\"stroke\":2,\"color\":\"#1B1B1B\",\"outlineStroke\":0,\"outlineColor\":\"none\",\"policy\":\"draw2d.policy.line.LineSelectionFeedbackPolicy\",\"router\":\"draw2d.layout.connection.FanConnectionRouter\",\"radius\":2,\"source\":{\"node\":\"f462cb01-7684-bc9e-6ae9-55e0c7af4a40\",\"port\":\"hybrid0\"},\"target\":{\"node\":\"52ad7c86-6fd3-06ab-12f9-41356d5fdc2d\",\"port\":\"hybrid0\"},\"label\":\"has\"},{\"type\":\"example.shape.HasConnection\",\"id\":\"9cf9b6db-31bf-055b-525a-2b98c38e86f5\",\"userData\":{\"condition\":\"IsTrue\",\"variable\":\"activityResult\"},\"cssClass\":\"RegExConnection\",\"stroke\":2,\"color\":\"#1B1B1B\",\"outlineStroke\":0,\"outlineColor\":\"none\",\"policy\":\"draw2d.policy.line.LineSelectionFeedbackPolicy\",\"router\":\"draw2d.layout.connection.FanConnectionRouter\",\"radius\":2,\"source\":{\"node\":\"f462cb01-7684-bc9e-6ae9-55e0c7af4a40\",\"port\":\"hybrid0\"},\"target\":{\"node\":\"f8703a6a-cd6f-b058-729f-b86a661f7e2a\",\"port\":\"hybrid0\"},\"label\":\"has\"}]\"}";
		Gson gson = new GsonBuilder().create();
		String json = state;
		json = json.replace("\"","'");
		json = json.replace("\\'","'");
		json = json.substring(json.indexOf(CONTENT)+10,json.length());
		json = json.substring(0,json.length()-2);

		System.out.println("=> "+json);
		Type collectionType = new TypeToken<Collection<Object>>(){}.getType();
		Collection<Object> objs = gson.fromJson(json, collectionType);
		List<Map<String,String>> chartDinaObjMapList = new ArrayList<Map<String,String>>(); 
		Iterator it = objs.iterator(); while(it.hasNext()){
			Map<String,String> chartDinaObjMap = new TreeMap<String,String>();
			Object obj = it.next();
			LinkedTreeMap linkedTreeMap = new LinkedTreeMap();
			linkedTreeMap = (LinkedTreeMap) obj;

			for(Object ob:linkedTreeMap.entrySet()){
				String key = VAZIO;
				String value = VAZIO;
				if(ob==null || ob.toString()==null || VAZIO.equals(ob.toString())){
					continue;
				}
				if(ob.toString().startsWith(CODE)){
					key = CODE;
					value = ob.toString().replace(CODE_,VAZIO).trim();
					chartDinaObjMap.put(key, value);
					continue;
				}
				if(ob.toString().startsWith(TARGET)){
					key = TARGET;
					value = ob.toString().replace(TARGET_,VAZIO).trim();
					chartDinaObjMap.put(key, value);
					continue;
				}
				if(ob.toString().startsWith(SOURCE)){
					key = SOURCE;
					value = ob.toString().replace(SOURCE_,VAZIO).trim();
					chartDinaObjMap.put(key, value);
					continue;
				}
				if(ob.toString().startsWith(USER_DATA)){
					key = USER_DATA;
					value = ob.toString().replace(USER_DATA_,VAZIO).trim();
					chartDinaObjMap.put(key, value);
					continue;
				}else{
					String[] objarr = ob.toString().split("=");
					key = objarr[0];
					value = objarr[1];
					chartDinaObjMap.put(key, value);
					continue;
				}
				
				

			}
				chartDinaObjMapList.add(chartDinaObjMap);
		}
		
//		int i = 1;
//		StringBuilder sb = new StringBuilder();
//		for(Map<String,String> dinaMap: chartDinaObjMapList){
//			sb.append("Inicio obj:").append(i).append("\n");
//			for(Map.Entry<String,String> entry:dinaMap.entrySet()){
//				sb.append("key: ").append(entry.getKey()).append(" value:").append(entry.getValue()).append("\n");
//			}
//			sb.append("Fim obj:").append(i).append("\n\n");
//			i++;
//		}
//
//		FileUtils.writeStringToFile(new File(getWebInfPath()+DATA+"\\mappedState.cache"),sb.toString(),UTF_8);

		return chartDinaObjMapList;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/get")
	public String saveEditorStateAsJsonFile() {
		try{
			File dataDir = new File(getWebInfPath()+DATA);
			dataDir.mkdirs();
			String currentState = FileUtils.readFileToString(new File(getWebInfPath()+DATA+DATA_JSON),UTF_8);
			return currentState;
		}catch(Exception e){
			throw new RuntimeException(e.getMessage(),e);
		}
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getRedHunterState")
	public String getRedHunterState() {
		try{
			File dataDir = new File(getWebInfPath()+DATA);
			dataDir.mkdirs();
			String currentState = FileUtils.readFileToString(new File(getWebInfPath()+DATA+DATA_JSON),UTF_8);
			List<Map<String,String>> mappedState = getMappedState(currentState);
			Editor editor = getEditor(mappedState);
			List<Boxe> boxes = new ArrayList<Boxe>();
			int i =1;
			for(Boxe boxe:editor.getBoxes()){
				boxe.setHits(i);
				boxes.add(boxe);
				i++;
			}
			return new GsonBuilder().create().toJson(editor.getBoxes());
		}catch(Exception e){
			throw new RuntimeException(e.getMessage(),e);
		}
	}

	
	public String getWebInfPath() {
	    String filePath = VAZIO;
	    java.net.URL url = RedHunterEditorRestService.class.getResource(EDITOR_REST_SERVICE_CLASS);
	    if (url != null) {
	        String className = url.getFile();
	        filePath = (className.contains(WEB_INF)) ? className.substring(0, className.indexOf(WEB_INF) + WEB_INF.length()) : className;
	        filePath = (className.contains(TARGET)) ? className.substring(0, className.indexOf(TARGET) + TARGET.length()) : className;
	    }
	    if(filePath.startsWith(BAR)){
	    	filePath = filePath.replaceFirst(BAR,VAZIO);
	    }
	    if(filePath.startsWith(BAR)){
	    	filePath = filePath.replaceFirst(BAR_BAR,VAZIO);
	    }
	    return filePath.replace("%20", " ");
	}
	
	
	public static void main(String[] args) throws Exception {
		String json = "{\"name\":\"data\",\"id\":\"data\",\"content\":\"[{\"type\":\"draw2d.shape.state.OntologyClass\",\"id\":\"3f3a4f56-fb37-86f5-5be0-1581067fb51e\",\"x\":14,\"y\":265,\"width\":91,\"height\":86,\"userData\":{},\"cssClass\":\"draw2d_shape_state_OntologyClass\",\"bgColor\":\"#F3F3F3\",\"color\":\"#22B366\",\"stroke\":1,\"alpha\":1,\"radius\":5,\"label\":\"fishPopulation\"},{\"type\":\"draw2d.shape.state.OntologyAttribute\",\"id\":\"d0b98d5e-dcd0-823a-ab8b-b0fa948edd26\",\"x\":222,\"y\":38,\"width\":91,\"height\":86,\"userData\":{},\"cssClass\":\"draw2d_shape_state_OntologyAttribute\",\"bgColor\":\"#F3F3F3\",\"color\":\"#228CB3\",\"stroke\":1,\"alpha\":1,\"radius\":5,\"label\":\"age\",\"code\":\"function age(){\\n\\tvar age = 10.0;\\n\\treturn age;\\n}\"},{\"type\":\"draw2d.shape.state.OntologyMainAttribute\",\"id\":\"bb9ae295-61c3-5f10-9052-87934bf07bbd\",\"x\":343,\"y\":276,\"width\":91,\"height\":86,\"userData\":{},\"cssClass\":\"draw2d_shape_state_OntologyMainAttribute\",\"bgColor\":\"#F3F3F3\",\"color\":\"#DB0029\",\"stroke\":1,\"alpha\":1,\"radius\":5,\"label\":\"size\",\"code\":\"function size(age,temperature,foodLevel,oxygenLevel){\\n@RelationshipDependency(name->|sizeToAge| source->|size| target->|age|)\\n@RelationshipDependency(name->|sizeToTemperature| source->|size| target->|temperature|)\\n@RelationshipDependency(name->|sizeToFoodLevel| source->|size| target->|foodLevel|)\\n@RelationshipDependency(name->|sizeToFoodLevel| source->|size| target->|foodLevel|)\\n@ReplacementNecessary(fromDefault->|size = size-age+temperature+foodLevel| toDefinedVarEval->|size = size ${sizeToAge} age ${sizeToTemperature}temperature ${sizeToFoodLevel} foodLevel|)\\n\\n    var size = 2200;\\n    size = size-age+temperature+foodLevel;\\n    if(oxygenLevel>=0.5){\\n      size = size*2;\\n    }else{\\n      size = size/2;\\n    } \\n    return size;\\n}\\n\\nsize(age(),temperature(),foodLevel(),oxygenLevel());\"},{\"type\":\"draw2d.shape.state.OntologyAttribute\",\"id\":\"4e1447e6-1deb-9717-a90e-1a6eb4d1818f\",\"x\":121,\"y\":521,\"width\":106,\"height\":86,\"userData\":{},\"cssClass\":\"draw2d_shape_state_OntologyAttribute\",\"bgColor\":\"#F3F3F3\",\"color\":\"#228CB3\",\"stroke\":1,\"alpha\":0.4,\"radius\":5,\"label\":\"reproductionRate\",\"code\":\"function reproductionRate(temperature){\\n\\tvar reproductionRate = 0.5;\\n\\treproductionRate = reproductionRate+(temperature/100);\\n\\treturn reproductionRate;\\n}\\n\"},{\"type\":\"draw2d.shape.state.OntologyAttribute\",\"id\":\"51983843-fd23-aef2-ae46-a34d6bc45ecc\",\"x\":762,\"y\":496,\"width\":91,\"height\":86,\"userData\":{},\"cssClass\":\"draw2d_shape_state_OntologyAttribute\",\"bgColor\":\"#F3F3F3\",\"color\":\"#228CB3\",\"stroke\":1,\"alpha\":1,\"radius\":5,\"label\":\"temperature\",\"code\":\"function temperature(){\\n\\tvar temperature = 30.0;\\n\\treturn temperature;\\n}\"},{\"type\":\"draw2d.shape.state.OntologyAttribute\",\"id\":\"f8703a6a-cd6f-b058-729f-b86a661f7e2a\",\"x\":776,\"y\":18,\"width\":91,\"height\":86,\"userData\":{},\"cssClass\":\"draw2d_shape_state_OntologyAttribute\",\"bgColor\":\"#F3F3F3\",\"color\":\"#228CB3\",\"stroke\":1,\"alpha\":1,\"radius\":5,\"label\":\"foodLevel\",\"code\":\"function foodLevel(){\\n\\tvar foodLevel = 0.3; \\n\\treturn foodLevel;\\n}\\n\"},{\"type\":\"draw2d.shape.state.OntologyAttribute\",\"id\":\"52ad7c86-6fd3-06ab-12f9-41356d5fdc2d\",\"x\":825,\"y\":234,\"width\":91,\"height\":86,\"userData\":{},\"cssClass\":\"draw2d_shape_state_OntologyAttribute\",\"bgColor\":\"#F3F3F3\",\"color\":\"#228CB3\",\"stroke\":1,\"alpha\":1,\"radius\":5,\"label\":\"oxygenLevel\",\"code\":\"function oxygenLevel(){\\n\\tvar oxygenLevel = 0.4;\\n\\treturn oxygenLevel;\\n}\"},{\"type\":\"draw2d.shape.state.OntologyClass\",\"id\":\"f462cb01-7684-bc9e-6ae9-55e0c7af4a40\",\"x\":1120,\"y\":291,\"width\":91,\"height\":86,\"userData\":{},\"cssClass\":\"draw2d_shape_state_OntologyClass\",\"bgColor\":\"#F3F3F3\",\"color\":\"#22B366\",\"stroke\":1,\"alpha\":1,\"radius\":5,\"label\":\"environment\"},{\"type\":\"example.shape.HasConnection\",\"id\":\"277ad0f9-485a-9a5e-a3ca-4094b1bf6460\",\"userData\":{\"condition\":\"IsTrue\",\"variable\":\"activityResult\"},\"cssClass\":\"RegExConnection\",\"stroke\":2,\"color\":\"#1B1B1B\",\"outlineStroke\":0,\"outlineColor\":\"none\",\"policy\":\"draw2d.policy.line.LineSelectionFeedbackPolicy\",\"router\":\"draw2d.layout.connection.FanConnectionRouter\",\"radius\":2,\"source\":{\"node\":\"3f3a4f56-fb37-86f5-5be0-1581067fb51e\",\"port\":\"hybrid0\"},\"target\":{\"node\":\"d0b98d5e-dcd0-823a-ab8b-b0fa948edd26\",\"port\":\"hybrid0\"},\"label\":\"has\"},{\"type\":\"example.shape.HasConnection\",\"id\":\"d20d4804-a015-8f09-de0b-570c8d75249a\",\"userData\":{\"condition\":\"IsTrue\",\"variable\":\"activityResult\"},\"cssClass\":\"RegExConnection\",\"stroke\":2,\"color\":\"#1B1B1B\",\"outlineStroke\":0,\"outlineColor\":\"none\",\"policy\":\"draw2d.policy.line.LineSelectionFeedbackPolicy\",\"router\":\"draw2d.layout.connection.FanConnectionRouter\",\"radius\":2,\"source\":{\"node\":\"3f3a4f56-fb37-86f5-5be0-1581067fb51e\",\"port\":\"hybrid0\"},\"target\":{\"node\":\"bb9ae295-61c3-5f10-9052-87934bf07bbd\",\"port\":\"hybrid0\"},\"label\":\"has\"},{\"type\":\"example.shape.HasConnection\",\"id\":\"029c5674-eeae-ca08-0794-23698be6d605\",\"userData\":{\"condition\":\"IsTrue\",\"variable\":\"activityResult\"},\"cssClass\":\"RegExConnection\",\"stroke\":2,\"color\":\"#1B1B1B\",\"outlineStroke\":0,\"outlineColor\":\"none\",\"policy\":\"draw2d.policy.line.LineSelectionFeedbackPolicy\",\"router\":\"draw2d.layout.connection.FanConnectionRouter\",\"radius\":2,\"source\":{\"node\":\"3f3a4f56-fb37-86f5-5be0-1581067fb51e\",\"port\":\"hybrid0\"},\"target\":{\"node\":\"4e1447e6-1deb-9717-a90e-1a6eb4d1818f\",\"port\":\"hybrid0\"},\"label\":\"has\"},{\"type\":\"example.shape.IncreaseConnection\",\"id\":\"a4d02391-dee5-01af-6a75-c9cd6f5ba6bf\",\"userData\":{\"condition\":\"IsTrue\",\"variable\":\"activityResult\"},\"cssClass\":\"AutoConnection\",\"stroke\":2,\"color\":\"#1B1B1B\",\"outlineStroke\":0,\"outlineColor\":\"none\",\"policy\":\"draw2d.policy.line.LineSelectionFeedbackPolicy\",\"router\":\"draw2d.layout.connection.FanConnectionRouter\",\"radius\":2,\"source\":{\"node\":\"bb9ae295-61c3-5f10-9052-87934bf07bbd\",\"port\":\"hybrid0\"},\"target\":{\"node\":\"51983843-fd23-aef2-ae46-a34d6bc45ecc\",\"port\":\"hybrid0\"},\"label\":\"increases with\"},{\"type\":\"example.shape.IncreaseConnection\",\"id\":\"279014be-9906-ab0e-dcf8-b1a96da5800e\",\"userData\":{\"condition\":\"IsTrue\",\"variable\":\"activityResult\"},\"cssClass\":\"AutoConnection\",\"stroke\":2,\"color\":\"#1B1B1B\",\"outlineStroke\":0,\"outlineColor\":\"none\",\"policy\":\"draw2d.policy.line.LineSelectionFeedbackPolicy\",\"router\":\"draw2d.layout.connection.FanConnectionRouter\",\"radius\":2,\"source\":{\"node\":\"4e1447e6-1deb-9717-a90e-1a6eb4d1818f\",\"port\":\"hybrid0\"},\"target\":{\"node\":\"51983843-fd23-aef2-ae46-a34d6bc45ecc\",\"port\":\"hybrid0\"},\"label\":\"increases with\"},{\"type\":\"example.shape.DecreaseConnection\",\"id\":\"1b6b50d8-917d-e1e7-d217-3ab31d67c565\",\"userData\":{\"condition\":\"IsTrue\",\"variable\":\"activityResult\"},\"cssClass\":\"AutoConnection\",\"stroke\":2,\"color\":\"#1B1B1B\",\"outlineStroke\":0,\"outlineColor\":\"none\",\"policy\":\"draw2d.policy.line.LineSelectionFeedbackPolicy\",\"router\":\"draw2d.layout.connection.FanConnectionRouter\",\"radius\":2,\"source\":{\"node\":\"bb9ae295-61c3-5f10-9052-87934bf07bbd\",\"port\":\"hybrid0\"},\"target\":{\"node\":\"d0b98d5e-dcd0-823a-ab8b-b0fa948edd26\",\"port\":\"hybrid0\"},\"label\":\"decreases with\"},{\"type\":\"example.shape.DependsConnection\",\"id\":\"1fe9dbfd-8ec2-22d8-43a6-b59d6e59b15c\",\"userData\":{\"condition\":\"IsTrue\",\"variable\":\"activityResult\"},\"cssClass\":\"AutoConnection\",\"stroke\":2,\"color\":\"#1B1B1B\",\"outlineStroke\":0,\"outlineColor\":\"none\",\"policy\":\"draw2d.policy.line.LineSelectionFeedbackPolicy\",\"router\":\"draw2d.layout.connection.FanConnectionRouter\",\"radius\":2,\"source\":{\"node\":\"bb9ae295-61c3-5f10-9052-87934bf07bbd\",\"port\":\"hybrid0\"},\"target\":{\"node\":\"52ad7c86-6fd3-06ab-12f9-41356d5fdc2d\",\"port\":\"hybrid0\"},\"label\":\"depends on\"},{\"type\":\"example.shape.IncreaseConnection\",\"id\":\"b38a8838-5747-e8e2-03f7-42865fe1cdcd\",\"userData\":{\"condition\":\"IsTrue\",\"variable\":\"activityResult\"},\"cssClass\":\"AutoConnection\",\"stroke\":2,\"color\":\"#1B1B1B\",\"outlineStroke\":0,\"outlineColor\":\"none\",\"policy\":\"draw2d.policy.line.LineSelectionFeedbackPolicy\",\"router\":\"draw2d.layout.connection.FanConnectionRouter\",\"radius\":2,\"source\":{\"node\":\"bb9ae295-61c3-5f10-9052-87934bf07bbd\",\"port\":\"hybrid0\"},\"target\":{\"node\":\"f8703a6a-cd6f-b058-729f-b86a661f7e2a\",\"port\":\"hybrid0\"},\"label\":\"increases with\"},{\"type\":\"example.shape.HasConnection\",\"id\":\"501ff51f-9015-af4d-ef59-90997bb53ae6\",\"userData\":{\"condition\":\"IsTrue\",\"variable\":\"activityResult\"},\"cssClass\":\"RegExConnection\",\"stroke\":2,\"color\":\"#1B1B1B\",\"outlineStroke\":0,\"outlineColor\":\"none\",\"policy\":\"draw2d.policy.line.LineSelectionFeedbackPolicy\",\"router\":\"draw2d.layout.connection.FanConnectionRouter\",\"radius\":2,\"source\":{\"node\":\"f462cb01-7684-bc9e-6ae9-55e0c7af4a40\",\"port\":\"hybrid0\"},\"target\":{\"node\":\"51983843-fd23-aef2-ae46-a34d6bc45ecc\",\"port\":\"hybrid0\"},\"label\":\"has\"},{\"type\":\"example.shape.HasConnection\",\"id\":\"07a46a58-b579-6f7f-aab8-d6ceb5a7612b\",\"userData\":{\"condition\":\"IsTrue\",\"variable\":\"activityResult\"},\"cssClass\":\"RegExConnection\",\"stroke\":2,\"color\":\"#1B1B1B\",\"outlineStroke\":0,\"outlineColor\":\"none\",\"policy\":\"draw2d.policy.line.LineSelectionFeedbackPolicy\",\"router\":\"draw2d.layout.connection.FanConnectionRouter\",\"radius\":2,\"source\":{\"node\":\"f462cb01-7684-bc9e-6ae9-55e0c7af4a40\",\"port\":\"hybrid0\"},\"target\":{\"node\":\"52ad7c86-6fd3-06ab-12f9-41356d5fdc2d\",\"port\":\"hybrid0\"},\"label\":\"has\"},{\"type\":\"example.shape.HasConnection\",\"id\":\"9cf9b6db-31bf-055b-525a-2b98c38e86f5\",\"userData\":{\"condition\":\"IsTrue\",\"variable\":\"activityResult\"},\"cssClass\":\"RegExConnection\",\"stroke\":2,\"color\":\"#1B1B1B\",\"outlineStroke\":0,\"outlineColor\":\"none\",\"policy\":\"draw2d.policy.line.LineSelectionFeedbackPolicy\",\"router\":\"draw2d.layout.connection.FanConnectionRouter\",\"radius\":2,\"source\":{\"node\":\"f462cb01-7684-bc9e-6ae9-55e0c7af4a40\",\"port\":\"hybrid0\"},\"target\":{\"node\":\"f8703a6a-cd6f-b058-729f-b86a661f7e2a\",\"port\":\"hybrid0\"},\"label\":\"has\"}]\"}";
		RedHunterEditorRestService svc = new RedHunterEditorRestService();
		svc.getChartData(json);
		
		ScriptEngineManager manager = new ScriptEngineManager();
	    ScriptEngine engine = manager.getEngineByName(JS);
//	    String myJSCode = "function calcul(a,b,c,d,e)"
//	            + "{"
//	            + "println('ola');"
//	            + "return true;"
//	            + "};" ;
	    
//	    String json = "function age()"
//	    		+ "{"
//	    		+ " var age = 10.0; "
//	    		+ "return age; "
//	    		+ "}";
//	    
//	    System.out.println(engine.eval(json));
//	    
//	    Invocable inv = (Invocable) engine;  
//
//	    String code = json;
//	    String functionName = code.substring(code.indexOf(FUNCTION)+8,code.indexOf("(")).trim();
//	    String functionNameAssign = code.substring(code.indexOf(FUNCTION),code.indexOf(")")+1).trim();
//	    System.out.println("functionNameAssign => "+functionNameAssign);
//	    String[] argArr = code.substring(code.indexOf("(")+1,code.indexOf(")")).trim().split(",");
//	    for(int i=0;i<argArr.length;i++){
//	    	System.out.println(argArr[i]);
//	    }
//	    System.out.println(functionName);
//        System.out.println(inv.invokeFunction(functionName,null).toString());


	}

	public String getDatapath() {
		return datapath;
	}

	public void setDatapath(String datapath) {
		this.datapath = datapath;
	}

	public String getJsonInputAsString() {
		return jsonInputAsString;
	}

	public void setJsonInputAsString(String jsonInputAsString) {
		this.jsonInputAsString = jsonInputAsString;
	}
	

}
