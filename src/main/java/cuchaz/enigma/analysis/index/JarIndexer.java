package cuchaz.enigma.analysis.index;

import cuchaz.enigma.translation.mapping.EntryResolver;
import cuchaz.enigma.translation.representation.entry.*;

public interface JarIndexer {
	default void indexClass(ClassDefEntry classEntry) {
	}

	default void indexField(FieldDefEntry fieldEntry) {
	}

	default void indexMethod(MethodDefEntry methodEntry) {
	}

	default void indexMethodReference(MethodDefEntry callerEntry, MethodEntry referencedEntry) {
	}

	default void indexFieldReference(MethodDefEntry callerEntry, FieldEntry referencedEntry) {
	}

	default void processIndex(EntryResolver resolver) {
	}
}
