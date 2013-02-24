package field;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ObjectMap<S,T> implements Iterable<T> {

	public ObjectMap() {
		
	}
	
	private Map<S, T> objectMap = new HashMap<S, T>();
	
	public void addObject(S id, T object)
		throws IllegalArgumentException {
		if (!canHaveAsId(id)) {
			throw new IllegalArgumentException("Given Id is invalid!");
		}
		overWrite(id, object);
	}
	
	public boolean isValidId(S id) {
		return id != null;
	}
	
	public boolean canHaveAsId(S id) {
		return isValidId(id) && !hasId(id);
	}
	
	public boolean hasId(S id) {
		return objectMap.containsKey(id);
	}
	
	public boolean canHaveAsObject(T object) {
		return object != null;
	}
	
	public Collection<T> getObjectCollection() {
		return objectMap.values();
	}
	
	public T getObjectAtId(S id){
		if (!hasId(id)) {
			throw new IllegalArgumentException(""+id + " does not exist");
		}
		return objectMap.get(id);
	}
	
	public void removeObjectAtId(S id) {
		if (!hasId(id)) {
			throw new IllegalArgumentException(""+id + " does not exist");
		}
		objectMap.remove(id);
	}
	
	public void overWrite(S id, T object)
			throws IllegalArgumentException {
		if (!canHaveAsObject(object)) {
			throw new IllegalArgumentException("Given Object is invalid!");
		}
		objectMap.put(id, object);
	}

	@Override
	public Iterator<T> iterator() {
		return objectMap.values().iterator();
	}

}
