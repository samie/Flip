package org.vaadin.addons.flip;

import com.vaadin.annotations.StyleSheet;
import com.vaadin.event.LayoutEvents;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import java.util.ArrayList;
import java.util.List;

/**
 * Vaadin flip component.
 *
 * @author Sami Ekblad
 */
@StyleSheet("flip.css")
public class Flip extends CustomComponent {

    private Component front;
    private Component back;
    private FlipMode flipMode = FlipMode.ON_CLICK;

    private final CssLayout root = new CssLayout();
    private final CssLayout flipper = new CssLayout() {

        @Override
        protected String getCss(Component c) {
            if (c == flipFront) {
                return "position: absolute;right: 10px;top: 10px; z-index: 5;";
            }
            if (c == flipBack) {
                return "position: absolute;left: 10px;top: 10px; z-index: 2;";
            }
            return super.getCss(c);
        }

    };
    private final Button flipFront = new Button(FontAwesome.COG);
    private final Button flipBack = new Button(FontAwesome.COG);

    private final LayoutEvents.LayoutClickListener clickListener = new LayoutEvents.LayoutClickListener() {

        @Override
        public void layoutClick(LayoutEvents.LayoutClickEvent event) {
            flip();
        }
    };

    private List<FlipListener> flipListeners = new ArrayList<>();

    public Flip() {
        root.setStyleName("flip-container");
        root.setSizeFull();
        setCompositionRoot(root);

        flipper.setStyleName("flipper");
        flipper.setSizeFull();
        root.addComponent(flipper);

        flipFront.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                flip();
            }
        });
        flipper.addComponent(flipFront);

        flipBack.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                flip();
            }
        });
        flipper.addComponent(flipBack);

        setFlipMode(FlipMode.MANUAL);
    }

    public Flip(Component frontSide, Component backSide) {
        this();
        setFrontComponent(frontSide);
        setBackComponent(backSide);
    }

    public Flip(Component frontSide, Component backSide, FlipMode flipMode) {
        this();

        setFrontComponent(frontSide);
        setBackComponent(backSide);
        setFlipMode(flipMode);
    }

    public void flip() {

        // If hover flipping is on, do nothing
        if (getFlipMode() == FlipMode.ON_HOVER) {
            return;
        }

        // Add or remove the style name
        if (isBackVisible()) {
            showFront();
        } else {
            showBack();
        }

        // Notify listeners
        for (FlipListener l : new ArrayList<>(flipListeners)) {
            l.flipped(this);
        };

    }

    private void showFront() {
        root.removeStyleName("flipped");
    }

    private void showBack() {
        root.addStyleName("flipped");
    }

    /**
     * Listener interface for user inactivity status changes.
     */
    public interface FlipListener {

        public void flipped(Flip flip);
    }

    /**
     * Flip modes.
     *
     */
    public enum FlipMode {

        ON_CLICK,
        ON_HOVER,
        BUTTON,
        MANUAL
    }

    public void addFlipListener(FlipListener listener) {
        this.flipListeners.add(listener);
    }

    public void removeFlipListener(FlipListener listener) {
        this.flipListeners.remove(listener);
    }

    public Component getFrontComponent() {
        return front;
    }

    public void setFrontComponent(Component frontSide) {
        if (this.front != null) {
            flipper.removeComponent(this.front);
        }
        this.front = frontSide;
        flipper.addComponent(frontSide);
        frontSide.addStyleName("front");
    }

    public Component getBackComponent() {
        return back;
    }

    public void setBackComponent(Component backSide) {
        if (this.back != null) {
            flipper.removeComponent(this.back);
        }
        this.back = backSide;
        flipper.addComponent(backSide, 1);
        backSide.addStyleName("back");
    }

    public Component getVisibleComponent() {
        return isBackVisible() ? getBackComponent() : getFrontComponent();
    }

    public boolean isBackVisible() {
        return root.getStyleName().indexOf("flipped") > 0;
    }

    public FlipMode getFlipMode() {
        return flipMode;
    }

    public void setFlipMode(FlipMode flipMode) {
        this.flipMode = flipMode;
        root.removeStyleName("hoverflip");
        root.removeStyleName("flipped");
        flipFront.setVisible(flipMode == FlipMode.BUTTON);
        flipBack.setVisible(flipMode == FlipMode.BUTTON);
        flipper.removeLayoutClickListener(clickListener);
        if (this.flipMode == FlipMode.ON_HOVER) {
            root.addStyleName("hoverflip");
        } else if (this.flipMode == FlipMode.ON_CLICK) {
            flipper.addLayoutClickListener(clickListener);
        }
    }

}
