package org.vaadin.addons.flip;

import com.vaadin.ui.Image;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.vaadin.addons.flip.Flip.FlipMode;

public class FlipTest {

	@Test
	public void testSetFlipMode_should_be_manual_with_no_arg_constructor() {
		Flip flip = new Flip();
		assertEquals(FlipMode.MANUAL, flip.getFlipMode());
	}

	@Test
	public void testSetFlipMode_on_click() {
		Flip flip = new Flip();
		flip.setFlipMode(FlipMode.ON_CLICK);
		assertEquals(FlipMode.ON_CLICK, flip.getFlipMode());
	}

	@Test
	public void testSetFrontAndBack() {
		Flip flip = new Flip(new Image("Front Image"), new Image("Back Image"), FlipMode.MANUAL);

		assertEquals("Front Image", flip.getFrontComponent().getCaption());
		assertEquals("Back Image", flip.getBackComponent().getCaption());
		assertEquals("front", flip.getFrontComponent().getStyleName());
		assertEquals("back", flip.getBackComponent().getStyleName());
	}

	@Test
	public void testFlip() {
		Flip flip = new Flip(new Image("Front Image"), new Image("Back Image"), FlipMode.MANUAL);
		assertFalse(flip.isBackVisible());
		flip.flip();
		assertTrue(flip.isBackVisible());
		assertEquals("Front Image", flip.getFrontComponent().getCaption());
		flip.flip();
		assertEquals("Back Image", flip.getBackComponent().getCaption());

		flip.flip();
		assertEquals("Front Image", flip.getFrontComponent().getCaption());
		flip.setFlipMode(FlipMode.ON_HOVER);

		flip.flip();
		assertEquals("Front Image", flip.getFrontComponent().getCaption());
	}

	@Test
	public void testFlip_on_hover_should_not_flip() {
		Flip flip = new Flip(new Image("Front Image"), new Image("Back Image"), FlipMode.ON_HOVER);
		assertFalse(flip.isBackVisible());

		flip.flip();
		assertFalse(flip.isBackVisible());
		assertEquals("Front Image", flip.getFrontComponent().getCaption());
	}
}