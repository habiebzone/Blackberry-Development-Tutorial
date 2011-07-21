package com.durianberry.takingpicture;

import java.io.IOException;

import javax.microedition.media.control.MetaDataControl;

import net.rim.blackberry.api.invoke.CameraArguments;
import net.rim.blackberry.api.invoke.Invoke;
import net.rim.device.api.io.file.FileSystemJournal;
import net.rim.device.api.io.file.FileSystemJournalEntry;
import net.rim.device.api.io.file.FileSystemJournalListener;
import net.rim.device.api.system.Characters;
import net.rim.device.api.system.DeviceInfo;
import net.rim.device.api.system.EncodedImage;
import net.rim.device.api.system.EventInjector;
import net.rim.device.api.ui.Touchscreen;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;

public class PictureTaker implements FileSystemJournalListener {
	private static PictureTaker taker;
	public static PictureTaker get() {
		if (taker == null) taker = new PictureTaker();
		return taker;
	}
	
	private long lastUSN = 0;
	private String photoPath;
	private PictureTakerListener listener;
	
	public PictureTaker() {
	}
	
	public void setListener(PictureTakerListener listener) {
		this.listener = listener;
	}
	
	public void takePicture() {
		UiApplication.getUiApplication().addFileSystemJournalListener(this);
		Invoke.invokeApplication(Invoke.APP_TYPE_CAMERA,
				new CameraArguments());
	}

	public void fileJournalChanged() {
		long USN = FileSystemJournal.getNextUSN();
		for (long i = USN - 1; i >= lastUSN; --i) {
			FileSystemJournalEntry entry = FileSystemJournal.getEntry(i);
			if (entry != null) {
				if (entry.getEvent() == FileSystemJournalEntry.FILE_ADDED) {
					if (entry.getPath().indexOf(".jpg") != -1) {
						lastUSN = USN;
						photoPath = entry.getPath();
						
						EventInjector.KeyEvent inject = new EventInjector.KeyEvent(EventInjector.KeyEvent.KEY_DOWN, Characters.ESCAPE, 0); 
						inject.post(); 
						inject.post(); 
						
						byte[] bytes;
						try {
							bytes = FileTools.getFileContent(photoPath);
							EncodedImage image = EncodedImage.createEncodedImage(bytes, 0, bytes.length);
							
							if (!DeviceInfo.isSimulator() && Touchscreen.isSupported()) {
								// Fact: image taken by Storm and Torch nearly always flipped
								// Todo: I need to find a way to automatically
								// detect image's orientation and then transform the image 
								// into normal image, not a filled one.
								
								MetaDataControl m = image.getMetaData();
								String[] arrKey = m.getKeys();
					            for(int j=0; j<arrKey.length; j++) {
					                System.out.println(arrKey[j]+" : "+m.getKeyValue(arrKey[j]));
					            }
								
								String orientation = "";
								try {
									orientation = m.getKeyValue("orientation");
									Dialog.alert("Orientation: "+orientation);
								} catch (Exception e) {
								}
							}
							
							if (listener != null)
								listener.onPictureCaptured(image);
						} catch (IOException e) {
						}
							
					}
				}
			}
		}
		lastUSN = USN;
	}

}
