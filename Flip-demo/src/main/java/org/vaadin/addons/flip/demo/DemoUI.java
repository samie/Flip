package org.vaadin.addons.flip.demo;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;
import java.util.Arrays;

import javax.servlet.annotation.WebServlet;

import org.vaadin.addons.flip.Flip;
import org.vaadin.addons.flip.Flip.FlipMode;

import static java.util.stream.Collectors.toList;

@Theme("valo")
@Title("Flip Add-on Demo")
@SuppressWarnings("serial")
public class DemoUI extends UI {

    private Flip flip;
    private Button flipButton;

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
        AbsoluteLayout baseLayout = new AbsoluteLayout();
        baseLayout.setSizeFull();
        demos.addTab(baseLayout, "Flip modes");

        flipButton = new Button("Flip", e -> flip.flip());
        baseLayout.addComponent(flipButton, "top:24px; left:20px;");

        ComboBox<FlipMode> modeSelect = new ComboBox<>("Mode",
                Arrays.stream(FlipMode.values()).filter(flipMode -> !FlipMode.BUTTON.equals(flipMode)).collect(toList()));
        modeSelect.setValue(FlipMode.MANUAL);
        modeSelect.addValueChangeListener(v -> {
            flip.setFlipMode(v.getValue());
            flipButton.setEnabled(!FlipMode.ON_HOVER.equals(flip.getFlipMode()));
        });
        baseLayout.addComponent(modeSelect, "top:24px; right:20px;");

        HorizontalLayout layout = new HorizontalLayout();
        layout.setSizeFull();
        baseLayout.addComponent(layout, "top:20%; bottom: 20%; left: 0; right: 0");

        // Front content
        Image front = new Image(null, new ExternalResource("http://proafile.com/images/channel_images/468/gunboat_g4__small.jpg"));

        // Back content
        Image back = new Image(null, new ExternalResource("http://40.media.tumblr.com/d84b66411606d07649477c844b511211/tumblr_nrllea5ka01rebadho1_400.jpg"));

        flip = new Flip(front, back, FlipMode.MANUAL);
        flip.setHeight("400px");
        flip.setWidth("400px");
        layout.addComponent(flip);
        flip.addFlipListener(f -> {
            Notification.show("Flipped", Notification.Type.TRAY_NOTIFICATION);
        });

        layout.setComponentAlignment(flip, Alignment.MIDDLE_CENTER);
    }
}
