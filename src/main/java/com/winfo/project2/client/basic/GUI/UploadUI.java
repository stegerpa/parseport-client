package com.winfo.project2.client.basic.GUI;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import com.winfo.project2.client.basic.connection.ServerConnector;
import com.winfo.project2.client.basic.data.Settings;
import com.winfo.project2.client.basic.helper;
import com.winfo.project2.client.plugins.WebsiteParser;

/**
 * Created by patrick on 13.06.17.
 */

public class UploadUI extends MainUI {
    public static final String VIEW_NAME = "upload";

    public UploadUI() {

    }

    public VerticalLayout getLayout () {

        Label headerLabel = new Label();
        headerLabel.setStyleName("h1");
        VerticalLayout vertical = new VerticalLayout ();

        headerLabel.setValue("Upload");

        Settings settings = helper.getSettings();
        //input form
        FormLayout form = new FormLayout();

        ComboBox<String> fileTypeComboBox= new ComboBox<>("Filetype");
        fileTypeComboBox.setItems("HTML Document","Word Document", "PDF Document");
        fileTypeComboBox.setRequiredIndicatorVisible(true);
        fileTypeComboBox.setIcon(FontAwesome.SITEMAP);
        fileTypeComboBox.setValue("HTML Document");

        TextField path = new TextField("Path");
        path.setIcon(FontAwesome.EXTERNAL_LINK);
        path.setRequiredIndicatorVisible(true);
        Label statusLabel = new Label();


        form.addComponents(fileTypeComboBox, path);

        Button button = new Button("Save me");
        form.addComponent(button);
        //Button click
        button.addClickListener( e -> {
            final int[] status = new int[1];
            new Thread(() -> {
                //create new entity object from parsed website
                WebsiteParser websiteParser = new WebsiteParser(path.getValue());

                //create json out of new object
                String json = helper.objectToJson(websiteParser.parse());

                //post json through serverconnector
                ServerConnector serverConnector = new ServerConnector("http://"+settings.getIpaddress()+":"+settings.getPort());

                status[0] = serverConnector.postJSON("/entities", json);

            }).start();
            try {
                Thread.sleep(3000);

                if(status[0] == 201){
                    statusLabel.setValue("Successfully parsed and saved.");
                }
                else{
                    statusLabel.setValue("Upload Timeout. No Data was saved.");
                }
                path.setValue(" ");
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        });
        form.addComponent(statusLabel);
        vertical.addComponents(headerLabel, form);

//        mainContent.removeAllComponents();
//        mainContent.addComponents(vertical);
        return vertical;
    }
}
