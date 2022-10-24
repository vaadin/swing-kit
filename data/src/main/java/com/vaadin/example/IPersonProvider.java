package com.vaadin.example;

import com.vaadin.swingkit.core.SwingVaadinCallable;

/**
 * SwingVaadinCallable interface that has to be implemented by any Vaadin View
 * that want to be callable from the Swing side.
 * 
 * @author Vaadin Ltd.
 *
 */
public interface IPersonProvider extends SwingVaadinCallable {
	/**
	 * Consumer of the event produced when a Person is selected on the table/grid view. 
	 * 
	 * @param id unique Identifier of the user.
	 */
	void show(Long id);
}
