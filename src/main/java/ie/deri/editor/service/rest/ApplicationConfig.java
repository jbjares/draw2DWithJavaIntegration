package ie.deri.editor.service.rest;



import ie.deri.editor.entity.EditorState;
import ie.deri.editor.entity.Resource;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;


@ApplicationPath("/")
public class ApplicationConfig extends Application {
	   @SuppressWarnings("unchecked")
	    public Set<Class<?>> getClasses() {
	        return new HashSet<Class<?>>(Arrays.asList(Resource.class,EditorState.class,Map.class));
	    }
}
