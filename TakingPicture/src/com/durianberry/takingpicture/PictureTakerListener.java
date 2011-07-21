package com.durianberry.takingpicture;

import net.rim.device.api.system.EncodedImage;

public interface PictureTakerListener {
	public void onPictureCaptured(EncodedImage image);
}
