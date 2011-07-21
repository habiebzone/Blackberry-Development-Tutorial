package com.durianberry.takingpicture;

import net.rim.device.api.applicationcontrol.ApplicationPermissions;
import net.rim.device.api.applicationcontrol.ApplicationPermissionsManager;
import net.rim.device.api.ui.UiApplication;

public class TakingPicture extends UiApplication {
	private static boolean setupPermissions() {
		ApplicationPermissionsManager man = ApplicationPermissionsManager
				.getInstance();
		int[] requiredPerms = new int[] { ApplicationPermissions.PERMISSION_INPUT_SIMULATION };
		ApplicationPermissions perms = man.getApplicationPermissions();
		boolean change = false;
		for (int i = 0; i < requiredPerms.length; i++) {
			if (perms.containsPermissionKey(requiredPerms[i])) {
				if (perms.getPermission(requiredPerms[i]) != ApplicationPermissions.VALUE_ALLOW) {
					change = true;
					perms.addPermission(requiredPerms[i]);
				}
			} else {
				change = true;
				perms.addPermission(requiredPerms[i]);
			}
		}
		if (change) {
			return man.invokePermissionsRequest(perms);
		} else {
			return true;
		}
	}

	public static void main(String[] args) {
		if (setupPermissions()) {
			new TakingPicture().enterEventDispatcher();
		}

	}

	public TakingPicture() {
		pushScreen(new TheScreen());
	}
}
