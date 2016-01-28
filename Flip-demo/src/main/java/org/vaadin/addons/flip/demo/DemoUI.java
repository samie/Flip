package org.vaadin.addons.flip.demo;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;
import java.util.Arrays;

import javax.servlet.annotation.WebServlet;

import org.vaadin.addons.flip.Flip;

@Theme("demo")
@Title("Flip Add-on Demo")
@SuppressWarnings("serial")
public class DemoUI extends UI {
    
    private Flip flip;
    private Button flipButton;
    private ComboBox modeSelect;
    
    @WebServlet(value = "/*", asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = DemoUI.class)
    public static class Servlet extends VaadinServlet {
    }
    
    @Override
    protected void init(VaadinRequest request) {
        
        
        TabSheet demos = new TabSheet();
        demos.setSizeFull();
        setContent(demos);
        
        
        // Demo 1
        AbsoluteLayout layout = new AbsoluteLayout();
        layout.setSizeFull();
        demos.addTab(layout, "Flip modes");
        
        flipButton = new Button("Flip", e -> {
            flip.flip();
        });
        layout.addComponent(flipButton, "top:24px; left:20px;");
        
        modeSelect = new ComboBox("Mode", Arrays.asList(Flip.FlipMode.values()));
        modeSelect.setValue(Flip.FlipMode.MANUAL);
        modeSelect.addValueChangeListener(v -> {
            flip.setFlipMode((Flip.FlipMode) v.getProperty().getValue());
            flipButton.setEnabled(flip.getFlipMode() != Flip.FlipMode.ON_HOVER);
        });
        layout.addComponent(modeSelect, "top:24px; right:20px;");
        // TODO: Should be in Vaadin:
        // modeSelect = new ComboBox<FlipMode>("Mode", Flip.FlipMode.values());
        // modeSelect.addValueChangeListener(v -> {flip.setFlipMode(v.getValue());});

        // Front content
        Image front = new Image(null, new ExternalResource("http://proafile.com/images/channel_images/468/gunboat_g4__small.jpg"));
        //front.setSizeFull();

        // Back content
        Image back = new Image(null, new ExternalResource("http://40.media.tumblr.com/d84b66411606d07649477c844b511211/tumblr_nrllea5ka01rebadho1_400.jpg"));
        //back.setSizeFull();
        
        flip = new Flip(front, back, Flip.FlipMode.MANUAL);
        flip.setHeight("400px");
        layout.addComponent(flip, "top:20%; bottom: 20%; left: 20%; right: 20%");
        flip.addFlipListener(f -> {
            Notification.show("Flipped", Notification.Type.TRAY_NOTIFICATION);
        });
        
                
    }
    
}
