package com.vaadin.example;

import javax.swing.*;

/**
 * Helper to run all the step by step guide applications
 * 
 * @author Vaadin Ltd.
 *
 */
public class AllFramesMain {
    public static void main(String[] a) {
        PersonListFrame frameSwing = new PersonListFrame();
        PersonListFrameVaadinDetails frameVaadinDetails = new PersonListFrameVaadinDetails();
        PersonListFrameVaadinTable frameVaadinTable = new PersonListFrameVaadinTable();
        PersonListFullVaadin frameVaadin = new PersonListFullVaadin();
        frameSwing.setVisible(true);
        frameVaadinDetails.setVisible(true);
        frameVaadinTable.setVisible(true);
        frameVaadin.setVisible(true);
        frameVaadin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
