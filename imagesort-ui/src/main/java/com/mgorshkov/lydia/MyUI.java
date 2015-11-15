package com.mgorshkov.lydia;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.FileResource;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 *
 */
@Theme("mytheme")
@Widgetset("com.mgorshkov.lydia.MyAppWidgetset")
public class MyUI extends UI {

    final static String[] TYPES = {"Horizontal", "Vertical", "Random"};
    private HorizontalLayout h;
    private ComboBox selector;


    @Override
    protected void init(VaadinRequest vaadinRequest) {
        h = new HorizontalLayout();
        h.setSizeFull();
        h.setSpacing(true);
        h.setMargin(true);

        // Show uploaded file in this placeholder
        final Embedded image = new Embedded("Sorted Image");
        image.setVisible(false);

        class ImageUploader implements Upload.Receiver, Upload.SucceededListener {
            public File file;

            public OutputStream receiveUpload(String filename,
                                              String mimeType) {
                // Create upload stream
                FileOutputStream fos = null; // Stream to write to
                try {
                    // Open the file for writing.
                    file = new File(filename);
                    fos = new FileOutputStream(file);
                } catch (final java.io.FileNotFoundException e) {
                    new Notification("Could not open file<br/>",
                            e.getMessage(),
                            Notification.Type.ERROR_MESSAGE)
                            .show(Page.getCurrent());
                    return null;
                }
                return fos; // Return the output stream to write to
            }

            public void uploadSucceeded(Upload.SucceededEvent event) {
                // Show the uploaded file in the image viewer

                ImageImport imageImport = new ImageImport(file, selector.getValue().toString());
                image.setVisible(true);
                image.setSource(new FileResource(new File("output.png")));
            }
        };
        ImageUploader receiver = new ImageUploader();

        // Create the upload with a caption and set receiver later
        Upload upload = new Upload("Upload Image Here", receiver);
        upload.setButtonCaption("Start Upload and Sort");
        upload.addSucceededListener(receiver);

        selector = new ComboBox();
        selector.addItems(TYPES);
        selector.setNullSelectionAllowed(false);
        selector.setValue(TYPES[0]);

        // Put the components in a panel
        Panel panel = new Panel();
        VerticalLayout panelContent = new VerticalLayout();
        panelContent.setSpacing(true);
        panelContent.addComponents(upload);
        panelContent.addComponent(selector);
        panelContent.addComponent(image);
        panel.setContent(panelContent);

        panelContent.setComponentAlignment(upload, Alignment.MIDDLE_CENTER);
        panelContent.setComponentAlignment(image, Alignment.MIDDLE_CENTER);
        panelContent.setComponentAlignment(selector, Alignment.MIDDLE_CENTER);

        h.addComponent(panel);
//        h.setComponentAlignment(panel, Alignment.MIDDLE_CENTER);
        setContent(h);
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
