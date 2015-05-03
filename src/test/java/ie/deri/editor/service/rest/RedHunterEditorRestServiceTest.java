package ie.deri.editor.service.rest;

import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;

public class RedHunterEditorRestServiceTest {
	
	private static final String HOME_IBMUSER_HACKATHON_WORKSPACE_DATA_DATA_JSON = "/data.json";
	
	private static final String JSON = "{\"name\":\"data\",\"id\":\"data\",\"content\":\"[{\"type\":\"draw2d.shape.state.OntologyClass\",\"id\":\"3f3a4f56-fb37-86f5-5be0-1581067fb51e\",\"x\":14,\"y\":265,\"width\":91,\"height\":86,\"userData\":{},\"cssClass\":\"draw2d_shape_state_OntologyClass\",\"bgColor\":\"#F3F3F3\",\"color\":\"#22B366\",\"stroke\":1,\"alpha\":1,\"radius\":5,\"label\":\"fishPopulation\"},{\"type\":\"draw2d.shape.state.OntologyAttribute\",\"id\":\"d0b98d5e-dcd0-823a-ab8b-b0fa948edd26\",\"x\":222,\"y\":38,\"width\":91,\"height\":86,\"userData\":{},\"cssClass\":\"draw2d_shape_state_OntologyAttribute\",\"bgColor\":\"#F3F3F3\",\"color\":\"#228CB3\",\"stroke\":1,\"alpha\":1,\"radius\":5,\"label\":\"age\",\"code\":\"function age(){\\n\\tvar age = 10.0;\\n\\treturn age;\\n}\"},{\"type\":\"draw2d.shape.state.OntologyMainAttribute\",\"id\":\"bb9ae295-61c3-5f10-9052-87934bf07bbd\",\"x\":343,\"y\":276,\"width\":91,\"height\":86,\"userData\":{},\"cssClass\":\"draw2d_shape_state_OntologyMainAttribute\",\"bgColor\":\"#F3F3F3\",\"color\":\"#DB0029\",\"stroke\":1,\"alpha\":1,\"radius\":5,\"label\":\"size\",\"code\":\"function size(age,temperature,foodLevel,oxygenLevel){\\n@RelationshipDependency(name->|sizeToAge| source->|size| target->|age|)\\n@RelationshipDependency(name->|sizeToTemperature| source->|size| target->|temperature|)\\n@RelationshipDependency(name->|sizeToFoodLevel| source->|size| target->|foodLevel|)\\n@RelationshipDependency(name->|sizeToFoodLevel| source->|size| target->|foodLevel|)\\n@ReplacementNecessary(fromDefault->|size = size-age+temperature+foodLevel| toDefinedVarEval->|size = size ${sizeToAge} age ${sizeToTemperature}temperature ${sizeToFoodLevel} foodLevel|)\\n\\n    var size = 2200;\\n    size = size-age+temperature+foodLevel;\\n    if(oxygenLevel>=0.5){\\n      size = size*2;\\n    }else{\\n      size = size/2;\\n    } \\n    return size;\\n}\\n\\nsize(age(),temperature(),foodLevel(),oxygenLevel());\"},{\"type\":\"draw2d.shape.state.OntologyAttribute\",\"id\":\"4e1447e6-1deb-9717-a90e-1a6eb4d1818f\",\"x\":121,\"y\":521,\"width\":106,\"height\":86,\"userData\":{},\"cssClass\":\"draw2d_shape_state_OntologyAttribute\",\"bgColor\":\"#F3F3F3\",\"color\":\"#228CB3\",\"stroke\":1,\"alpha\":0.4,\"radius\":5,\"label\":\"reproductionRate\",\"code\":\"function reproductionRate(temperature){\\n\\tvar reproductionRate = 0.5;\\n\\treproductionRate = reproductionRate+(temperature/100);\\n\\treturn reproductionRate;\\n}\\n\"},{\"type\":\"draw2d.shape.state.OntologyAttribute\",\"id\":\"51983843-fd23-aef2-ae46-a34d6bc45ecc\",\"x\":762,\"y\":496,\"width\":91,\"height\":86,\"userData\":{},\"cssClass\":\"draw2d_shape_state_OntologyAttribute\",\"bgColor\":\"#F3F3F3\",\"color\":\"#228CB3\",\"stroke\":1,\"alpha\":1,\"radius\":5,\"label\":\"temperature\",\"code\":\"function temperature(){\\n\\tvar temperature = 30.0;\\n\\treturn temperature;\\n}\"},{\"type\":\"draw2d.shape.state.OntologyAttribute\",\"id\":\"f8703a6a-cd6f-b058-729f-b86a661f7e2a\",\"x\":776,\"y\":18,\"width\":91,\"height\":86,\"userData\":{},\"cssClass\":\"draw2d_shape_state_OntologyAttribute\",\"bgColor\":\"#F3F3F3\",\"color\":\"#228CB3\",\"stroke\":1,\"alpha\":1,\"radius\":5,\"label\":\"foodLevel\",\"code\":\"function foodLevel(){\\n\\tvar foodLevel = 0.3; \\n\\treturn foodLevel;\\n}\\n\"},{\"type\":\"draw2d.shape.state.OntologyAttribute\",\"id\":\"52ad7c86-6fd3-06ab-12f9-41356d5fdc2d\",\"x\":825,\"y\":234,\"width\":91,\"height\":86,\"userData\":{},\"cssClass\":\"draw2d_shape_state_OntologyAttribute\",\"bgColor\":\"#F3F3F3\",\"color\":\"#228CB3\",\"stroke\":1,\"alpha\":1,\"radius\":5,\"label\":\"oxygenLevel\",\"code\":\"function oxygenLevel(){\\n\\tvar oxygenLevel = 0.4;\\n\\treturn oxygenLevel;\\n}\"},{\"type\":\"draw2d.shape.state.OntologyClass\",\"id\":\"f462cb01-7684-bc9e-6ae9-55e0c7af4a40\",\"x\":1120,\"y\":291,\"width\":91,\"height\":86,\"userData\":{},\"cssClass\":\"draw2d_shape_state_OntologyClass\",\"bgColor\":\"#F3F3F3\",\"color\":\"#22B366\",\"stroke\":1,\"alpha\":1,\"radius\":5,\"label\":\"environment\"},{\"type\":\"example.shape.HasConnection\",\"id\":\"277ad0f9-485a-9a5e-a3ca-4094b1bf6460\",\"userData\":{\"condition\":\"IsTrue\",\"variable\":\"activityResult\"},\"cssClass\":\"RegExConnection\",\"stroke\":2,\"color\":\"#1B1B1B\",\"outlineStroke\":0,\"outlineColor\":\"none\",\"policy\":\"draw2d.policy.line.LineSelectionFeedbackPolicy\",\"router\":\"draw2d.layout.connection.FanConnectionRouter\",\"radius\":2,\"source\":{\"node\":\"3f3a4f56-fb37-86f5-5be0-1581067fb51e\",\"port\":\"hybrid0\"},\"target\":{\"node\":\"d0b98d5e-dcd0-823a-ab8b-b0fa948edd26\",\"port\":\"hybrid0\"},\"label\":\"has\"},{\"type\":\"example.shape.HasConnection\",\"id\":\"d20d4804-a015-8f09-de0b-570c8d75249a\",\"userData\":{\"condition\":\"IsTrue\",\"variable\":\"activityResult\"},\"cssClass\":\"RegExConnection\",\"stroke\":2,\"color\":\"#1B1B1B\",\"outlineStroke\":0,\"outlineColor\":\"none\",\"policy\":\"draw2d.policy.line.LineSelectionFeedbackPolicy\",\"router\":\"draw2d.layout.connection.FanConnectionRouter\",\"radius\":2,\"source\":{\"node\":\"3f3a4f56-fb37-86f5-5be0-1581067fb51e\",\"port\":\"hybrid0\"},\"target\":{\"node\":\"bb9ae295-61c3-5f10-9052-87934bf07bbd\",\"port\":\"hybrid0\"},\"label\":\"has\"},{\"type\":\"example.shape.HasConnection\",\"id\":\"029c5674-eeae-ca08-0794-23698be6d605\",\"userData\":{\"condition\":\"IsTrue\",\"variable\":\"activityResult\"},\"cssClass\":\"RegExConnection\",\"stroke\":2,\"color\":\"#1B1B1B\",\"outlineStroke\":0,\"outlineColor\":\"none\",\"policy\":\"draw2d.policy.line.LineSelectionFeedbackPolicy\",\"router\":\"draw2d.layout.connection.FanConnectionRouter\",\"radius\":2,\"source\":{\"node\":\"3f3a4f56-fb37-86f5-5be0-1581067fb51e\",\"port\":\"hybrid0\"},\"target\":{\"node\":\"4e1447e6-1deb-9717-a90e-1a6eb4d1818f\",\"port\":\"hybrid0\"},\"label\":\"has\"},{\"type\":\"example.shape.IncreaseConnection\",\"id\":\"a4d02391-dee5-01af-6a75-c9cd6f5ba6bf\",\"userData\":{\"condition\":\"IsTrue\",\"variable\":\"activityResult\"},\"cssClass\":\"AutoConnection\",\"stroke\":2,\"color\":\"#1B1B1B\",\"outlineStroke\":0,\"outlineColor\":\"none\",\"policy\":\"draw2d.policy.line.LineSelectionFeedbackPolicy\",\"router\":\"draw2d.layout.connection.FanConnectionRouter\",\"radius\":2,\"source\":{\"node\":\"bb9ae295-61c3-5f10-9052-87934bf07bbd\",\"port\":\"hybrid0\"},\"target\":{\"node\":\"51983843-fd23-aef2-ae46-a34d6bc45ecc\",\"port\":\"hybrid0\"},\"label\":\"increases with\"},{\"type\":\"example.shape.IncreaseConnection\",\"id\":\"279014be-9906-ab0e-dcf8-b1a96da5800e\",\"userData\":{\"condition\":\"IsTrue\",\"variable\":\"activityResult\"},\"cssClass\":\"AutoConnection\",\"stroke\":2,\"color\":\"#1B1B1B\",\"outlineStroke\":0,\"outlineColor\":\"none\",\"policy\":\"draw2d.policy.line.LineSelectionFeedbackPolicy\",\"router\":\"draw2d.layout.connection.FanConnectionRouter\",\"radius\":2,\"source\":{\"node\":\"4e1447e6-1deb-9717-a90e-1a6eb4d1818f\",\"port\":\"hybrid0\"},\"target\":{\"node\":\"51983843-fd23-aef2-ae46-a34d6bc45ecc\",\"port\":\"hybrid0\"},\"label\":\"increases with\"},{\"type\":\"example.shape.DecreaseConnection\",\"id\":\"1b6b50d8-917d-e1e7-d217-3ab31d67c565\",\"userData\":{\"condition\":\"IsTrue\",\"variable\":\"activityResult\"},\"cssClass\":\"AutoConnection\",\"stroke\":2,\"color\":\"#1B1B1B\",\"outlineStroke\":0,\"outlineColor\":\"none\",\"policy\":\"draw2d.policy.line.LineSelectionFeedbackPolicy\",\"router\":\"draw2d.layout.connection.FanConnectionRouter\",\"radius\":2,\"source\":{\"node\":\"bb9ae295-61c3-5f10-9052-87934bf07bbd\",\"port\":\"hybrid0\"},\"target\":{\"node\":\"d0b98d5e-dcd0-823a-ab8b-b0fa948edd26\",\"port\":\"hybrid0\"},\"label\":\"decreases with\"},{\"type\":\"example.shape.DependsConnection\",\"id\":\"1fe9dbfd-8ec2-22d8-43a6-b59d6e59b15c\",\"userData\":{\"condition\":\"IsTrue\",\"variable\":\"activityResult\"},\"cssClass\":\"AutoConnection\",\"stroke\":2,\"color\":\"#1B1B1B\",\"outlineStroke\":0,\"outlineColor\":\"none\",\"policy\":\"draw2d.policy.line.LineSelectionFeedbackPolicy\",\"router\":\"draw2d.layout.connection.FanConnectionRouter\",\"radius\":2,\"source\":{\"node\":\"bb9ae295-61c3-5f10-9052-87934bf07bbd\",\"port\":\"hybrid0\"},\"target\":{\"node\":\"52ad7c86-6fd3-06ab-12f9-41356d5fdc2d\",\"port\":\"hybrid0\"},\"label\":\"depends on\"},{\"type\":\"example.shape.IncreaseConnection\",\"id\":\"b38a8838-5747-e8e2-03f7-42865fe1cdcd\",\"userData\":{\"condition\":\"IsTrue\",\"variable\":\"activityResult\"},\"cssClass\":\"AutoConnection\",\"stroke\":2,\"color\":\"#1B1B1B\",\"outlineStroke\":0,\"outlineColor\":\"none\",\"policy\":\"draw2d.policy.line.LineSelectionFeedbackPolicy\",\"router\":\"draw2d.layout.connection.FanConnectionRouter\",\"radius\":2,\"source\":{\"node\":\"bb9ae295-61c3-5f10-9052-87934bf07bbd\",\"port\":\"hybrid0\"},\"target\":{\"node\":\"f8703a6a-cd6f-b058-729f-b86a661f7e2a\",\"port\":\"hybrid0\"},\"label\":\"increases with\"},{\"type\":\"example.shape.HasConnection\",\"id\":\"501ff51f-9015-af4d-ef59-90997bb53ae6\",\"userData\":{\"condition\":\"IsTrue\",\"variable\":\"activityResult\"},\"cssClass\":\"RegExConnection\",\"stroke\":2,\"color\":\"#1B1B1B\",\"outlineStroke\":0,\"outlineColor\":\"none\",\"policy\":\"draw2d.policy.line.LineSelectionFeedbackPolicy\",\"router\":\"draw2d.layout.connection.FanConnectionRouter\",\"radius\":2,\"source\":{\"node\":\"f462cb01-7684-bc9e-6ae9-55e0c7af4a40\",\"port\":\"hybrid0\"},\"target\":{\"node\":\"51983843-fd23-aef2-ae46-a34d6bc45ecc\",\"port\":\"hybrid0\"},\"label\":\"has\"},{\"type\":\"example.shape.HasConnection\",\"id\":\"07a46a58-b579-6f7f-aab8-d6ceb5a7612b\",\"userData\":{\"condition\":\"IsTrue\",\"variable\":\"activityResult\"},\"cssClass\":\"RegExConnection\",\"stroke\":2,\"color\":\"#1B1B1B\",\"outlineStroke\":0,\"outlineColor\":\"none\",\"policy\":\"draw2d.policy.line.LineSelectionFeedbackPolicy\",\"router\":\"draw2d.layout.connection.FanConnectionRouter\",\"radius\":2,\"source\":{\"node\":\"f462cb01-7684-bc9e-6ae9-55e0c7af4a40\",\"port\":\"hybrid0\"},\"target\":{\"node\":\"52ad7c86-6fd3-06ab-12f9-41356d5fdc2d\",\"port\":\"hybrid0\"},\"label\":\"has\"},{\"type\":\"example.shape.HasConnection\",\"id\":\"9cf9b6db-31bf-055b-525a-2b98c38e86f5\",\"userData\":{\"condition\":\"IsTrue\",\"variable\":\"activityResult\"},\"cssClass\":\"RegExConnection\",\"stroke\":2,\"color\":\"#1B1B1B\",\"outlineStroke\":0,\"outlineColor\":\"none\",\"policy\":\"draw2d.policy.line.LineSelectionFeedbackPolicy\",\"router\":\"draw2d.layout.connection.FanConnectionRouter\",\"radius\":2,\"source\":{\"node\":\"f462cb01-7684-bc9e-6ae9-55e0c7af4a40\",\"port\":\"hybrid0\"},\"target\":{\"node\":\"f8703a6a-cd6f-b058-729f-b86a661f7e2a\",\"port\":\"hybrid0\"},\"label\":\"has\"}]\"}";

	@Test
	public void saveEditorStateAsJsonFileSucceess() throws Exception{

		RedHunterEditorRestService svc = new RedHunterEditorRestService();
		svc.setJsonInputAsString(JSON);
		svc.setDatapath(HOME_IBMUSER_HACKATHON_WORKSPACE_DATA_DATA_JSON);
		Map<String,String> result = svc.getChartData(JSON);

		for(Map.Entry<String,String> entry:result.entrySet()){
			String key = entry.getKey();
			String value = entry.getValue();
			if("0.0".equals(key)){
				Assert.assertEquals("1115.15",value);				
			}
			if("1.0".equals(key)){
				Assert.assertEquals("1114.65",value);				
			}
			if("2.0".equals(key)){
				Assert.assertEquals("1114.15",value);				
			}
			if("3.0".equals(key)){
				Assert.assertEquals("1113.65",value);				
			}
			if("4.0".equals(key)){
				Assert.assertEquals("1113.15",value);				
			}
			if("5.0".equals(key)){
				Assert.assertEquals("1112.65",value);				
			}
			if("6.0".equals(key)){
				Assert.assertEquals("1112.15",value);				
			}
			if("7.0".equals(key)){
				Assert.assertEquals("1111.65",value);				
			}
			if("8.0".equals(key)){
				Assert.assertEquals("1111.15",value);				
			}
			if("9.0".equals(key)){
				Assert.assertEquals("1110.65",value);				
			}
			if("10.0".equals(key)){
				Assert.assertEquals("1110.15",value);				
			}

		}

	}

	
	@Test
	public void saveEditorStateAsJsonFileFail() throws Exception{

		RedHunterEditorRestService svc = new RedHunterEditorRestService();
		svc.setJsonInputAsString(JSON);
		svc.setDatapath(HOME_IBMUSER_HACKATHON_WORKSPACE_DATA_DATA_JSON);
		Map<String,String> result = svc.getChartData(JSON);

		for(Map.Entry<String,String> entry:result.entrySet()){
			String key = entry.getKey();
			String value = entry.getValue();
			if("0.0".equals(key)){
				Assert.assertNotSame("1115.15",value);				
			}
			if("1.0".equals(key)){
				Assert.assertNotSame("1114.65",value);				
			}
			if("2.0".equals(key)){
				Assert.assertNotSame("1114.15",value);				
			}
			if("3.0".equals(key)){
				Assert.assertNotSame("1113.65",value);				
			}
			if("4.0".equals(key)){
				Assert.assertNotSame("1113.15",value);				
			}
			if("5.0".equals(key)){
				Assert.assertNotSame("1112.65",value);				
			}
			if("6.0".equals(key)){
				Assert.assertNotSame("1112.15",value);				
			}
			if("7.0".equals(key)){
				Assert.assertNotSame("1111.65",value);				
			}
			if("8.0".equals(key)){
				Assert.assertNotSame("1111.15",value);				
			}
			if("9.0".equals(key)){
				Assert.assertNotSame("1110.65",value);				
			}
			if("10.0".equals(key)){
				Assert.assertNotSame("1110.15",value);				
			}
		}
	}


}
