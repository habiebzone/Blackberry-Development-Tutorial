package com.durianberry.struktur;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.EditField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.ObjectChoiceField;
import net.rim.device.api.ui.container.MainScreen;

public class HelloWorld extends UiApplication implements FieldChangeListener {
	public static void main(String[] args) {
		new HelloWorld().enterEventDispatcher();
	}
	
	private ButtonField buttonField;
	private LabelField labelField;
	private EditField editField;
	public HelloWorld() {
		MainScreen screen = new MainScreen();
		screen.setTitle("Struktur");
		labelField = new LabelField("HelloWorld!");
		screen.add(labelField);
		pushScreen(screen);
		
		editField = 
			new EditField("Nama Anda: ", "[Input nama Anda]");
		screen.add(editField);
		
		String[] jenisKelamin = 
			new String[] {"Pria", "Wanita"};
		ObjectChoiceField choiceField = 
			new ObjectChoiceField("Jenis kelamin:", jenisKelamin);
		screen.add(choiceField);
		
		buttonField = 
			new ButtonField("Lanjut", ButtonField.CONSUME_CLICK);
		buttonField.setChangeListener(this);
		screen.add(buttonField);
	}

	public void fieldChanged(Field field, int context) {
		if (field == buttonField) {
			labelField.setText("Selamat siang "+editField.getText());
		}
		
	}
}
