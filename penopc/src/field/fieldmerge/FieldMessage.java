package field.fieldmerge;

import peno.htttp.PlayerClient;
import field.Field;

public abstract class FieldMessage implements Fieldable {
	
	
	public Field toField() {
		return FieldConverter.convertToField(this);
	}
	
	public Field mergeWithOther(Fieldable fieldable) {
		return FieldMerger.mergeFields(fieldable.toField(), this.toField());
	}

}
