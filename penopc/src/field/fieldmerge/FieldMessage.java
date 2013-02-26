package field.fieldmerge;

import field.Field;

public abstract class FieldMessage implements Fieldable {
	
	
	public Field toField() {
		return FieldConverter.convertToField(this);
	}
	
	public Field mergeWithOther(Fieldable fieldable) {
		return FieldMerger.MergeFields(fieldable.toField(), this.toField());
	}

}
