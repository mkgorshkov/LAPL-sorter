package com.mgorshkov.lydia;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.FileResource;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Base64;

/**
 *
 */
@Theme("mytheme")
@Widgetset("com.mgorshkov.lydia.MyAppWidgetset")
public class MyUI extends UI {

    final static String[] TYPES = {"Horizontal", "Vertical", "Random"};
    private HorizontalLayout h;
    private ComboBox selector;
    private Label statusLabel;


    @Override
    protected void init(VaadinRequest vaadinRequest) {
        h = new HorizontalLayout();
        h.setSizeFull();
        h.setSpacing(true);
        h.setMargin(true);

        // Show uploaded file in this placeholder

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

                String output = APICall(new File("output.png"));

                if(output != null){
                    statusLabel.setValue("Sorted completed: <a href=\""+output+"\" target=\"_blank\">"+output+"</a>");
                }else{
                    statusLabel.setValue("Status: Sorting failed. Is it too large?");
                }

                //image.setVisible(true);
                //image.setSource(new FileResource(new File("output.png")));
            }
        };
        ImageUploader receiver = new ImageUploader();

        // Create the upload with a caption and set receiver later
        final Upload upload = new Upload("Upload Image Here. Images >10MB will be rejected.", receiver);
        upload.setButtonCaption("Start Upload and Sort");
        upload.addStartedListener(new Upload.StartedListener() {
            @Override
            public void uploadStarted(Upload.StartedEvent startedEvent) {
                if(startedEvent.getContentLength() >= 10485760){
                    statusLabel.setValue("Status: Interrupted. Filesize was too large.");
                    upload.interruptUpload();
                }
            }
        });
        upload.addSucceededListener(receiver);
        upload.addFailedListener(new Upload.FailedListener() {
            public void uploadFailed(Upload.FailedEvent event) {
                // This method gets called when the upload failed
                statusLabel.setValue("Status: Uploading failed.");
            }
        });

        selector = new ComboBox();
        selector.addItems(TYPES);
        selector.setNullSelectionAllowed(false);
        selector.setValue(TYPES[0]);

        statusLabel = new Label("Status: Ready to upload.", ContentMode.HTML);
        statusLabel.setWidthUndefined();

        // Put the components in a panel
        Panel panel = new Panel();
        VerticalLayout panelContent = new VerticalLayout();
        panelContent.setSpacing(true);
        panelContent.addComponents(upload);
        panelContent.addComponent(selector);
        panelContent.addComponent(statusLabel);
        panel.setContent(panelContent);

        panelContent.setComponentAlignment(upload, Alignment.MIDDLE_CENTER);
        panelContent.setComponentAlignment(statusLabel, Alignment.MIDDLE_CENTER);
        panelContent.setComponentAlignment(selector, Alignment.MIDDLE_CENTER);

        h.addComponent(panel);
//        h.setComponentAlignment(panel, Alignment.MIDDLE_CENTER);
        setContent(h);
    }

    public String APICall(File file){

            final String upload_to = "https://api.imgur.com/3/upload.json";

            HttpClient httpClient = new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext();
            HttpPost httpPost = new HttpPost(upload_to);
            httpPost.setHeader("Authorization", "Client-ID 90656ab2149a92c");


        try {
                final MultipartEntity entity = new MultipartEntity(
                        HttpMultipartMode.BROWSER_COMPATIBLE);

                entity.addPart("image", new FileBody(file));

                httpPost.setEntity(entity);

                final HttpResponse response = httpClient.execute(httpPost,
                        localContext);

                final String response_string = EntityUtils.toString(response
                        .getEntity());

            JSONObject obj = new JSONObject(response_string);
            String pageName = obj.getJSONObject("data").getString("link");

            System.out.println(response_string); //for my own understanding

            System.out.println("URL "+pageName);

            return pageName;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
