package com.durianberry.takingpicture;

import net.rim.blackberry.api.invoke.CameraArguments;
import net.rim.blackberry.api.invoke.Invoke;
import net.rim.device.api.io.file.FileSystemJournal;
import net.rim.device.api.io.file.FileSystemJournalEntry;
import net.rim.device.api.io.file.FileSystemJournalListener;
import net.rim.device.api.system.Characters;
import net.rim.device.api.system.Display;
import net.rim.device.api.system.EncodedImage;
import net.rim.device.api.system.EventInjector;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.container.MainScreen;

public final class TheScreen extends MainScreen implements
		PictureTakerListener, FieldChangeListener {
	private ButtonField button;
	private BitmapField photoField;

	public TheScreen() {
		setTitle("Taking Picture");

		button = new ButtonField("Take Picture", ButtonField.CONSUME_CLICK);
		button.setChangeListener(this);
		add(button);

		photoField = new BitmapField();
		add(photoField);
	}

	public void fieldChanged(Field field, int context) {
		if (field == button) {
			PictureTaker.get().setListener(this);
			PictureTaker.get().takePicture();
		}
	}
	
	public void onPictureCaptured(EncodedImage image) {
		photoField.setImage(ImageTools.scaleImageToWidth(image, Display.getWidth()));
	}
}
