package com.vaadin.example;

import com.vaadin.swingkit.core.SwingVaadinCallable;

public interface IPersonProvider extends SwingVaadinCallable {
    void show(Long id);
}
