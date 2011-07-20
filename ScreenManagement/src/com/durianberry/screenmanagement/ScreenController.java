package com.durianberry.screenmanagement;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.UiApplication;

public class ScreenController {
	public static void showMe() {
		SingletonScreen.get().show();
	}
	
	private static SingletonScreen her;
	public static void showHer() {
		if (her == null) {
			her = new SingletonScreen();
			her.update(Bitmap.getBitmapResource("img/her.jpg"), 
				"Anak kecil cakep.");
		}
		her.show();
	}
	
	private static SingletonScreen him;
	public static void showHim() {
		if (him == null) {
			him = new SingletonScreen();
			him.update(Bitmap.getBitmapResource("img/him.jpg"), 
				"Anak kecil jadi milyarder gara-gara diputusin.");
		}
		him.show();
	}
}
