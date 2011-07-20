package com.durianberry.screenmanagement;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Screen;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.TextField;
import net.rim.device.api.ui.container.MainScreen;

public final class SingletonScreen extends MainScreen implements FieldChangeListener {
	private static SingletonScreen screen;
	public static SingletonScreen get() {
		if (screen == null) screen = new SingletonScreen();
		return screen;
	}
	
	private BitmapField photoField;
	private TextField contentField;
	private LabelField screenCountField;
	private ButtonField meButton, herButton, himButton;

	public SingletonScreen() {
		setTitle("A Singleton Screen");
		
		screenCountField = new LabelField("", FOCUSABLE);
		add(screenCountField);

		photoField = new BitmapField(Bitmap.getBitmapResource("img/me.jpg"),
				FIELD_HCENTER);
		add(photoField);

		contentField = new TextField(USE_ALL_WIDTH) {
			public void drawFocus(Graphics g, boolean on) {
			}
		};
		contentField.setText("Mobile Application Developer (especially for " +
				"Blackberry, Android or J2ME based) of some client server mobile " +
				"applications. Particularly interested in client/server and " +
				"security enabled software development. Always interested in " +
				"mobile application development projects, as well as close " +
				"interaction web API.");
		add(contentField);
		
		meButton = new ButtonField("It's me", ButtonField.CONSUME_CLICK);
		meButton.setChangeListener(this);
		add(meButton);
		herButton = new ButtonField("That's her", ButtonField.CONSUME_CLICK);
		herButton.setChangeListener(this);
		add(herButton);
		himButton = new ButtonField("That's him", ButtonField.CONSUME_CLICK);
		himButton.setChangeListener(this);
		add(himButton);
	}
	
	public void update(Bitmap photo, String text) {
		photoField.setBitmap(photo);
		contentField.setText(text);
	}
	
	public void fieldChanged(Field field, int context) {
		if (field == meButton) {
			ScreenController.showMe();
		} else if (field == herButton) {
			ScreenController.showHer();
		} else if (field == himButton) {
			ScreenController.showHim();
		}
	}
	
	public void updateNumberOfScreen() {
		screenCountField.setText("Jumlah screen: "+String.valueOf(UiApplication.getUiApplication().getScreenCount()));
		screenCountField.setFocus();
	}
	
	private Screen getThis() {
		return this;
	}
	
	public void show() {
		if (!isDisplayed()) {
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				public void run() {
					UiApplication.getUiApplication().pushScreen(getThis());
				}
			});
		} else {
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				public void run() {
					UiApplication.getUiApplication().popScreen(getThis());
					UiApplication.getUiApplication().pushScreen(getThis());
				}
			});
		}
	}
	
	public void hide() {
		if (get().isDisplayed()) {
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				public void run() {
					UiApplication.getUiApplication().popScreen(getThis());
				}
			});
		}
	}
	
	public void onDisplay() {
		super.onDisplay();
		updateNumberOfScreen();
	}
}
