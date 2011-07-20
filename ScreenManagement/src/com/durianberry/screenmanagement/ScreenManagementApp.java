package com.durianberry.screenmanagement;

import net.rim.device.api.ui.UiApplication;

public class ScreenManagementApp extends UiApplication {
	public static void main(String[] args) {
		new ScreenManagementApp().enterEventDispatcher();
	}

	public ScreenManagementApp() {
		ScreenController.showMe();
	}
}
