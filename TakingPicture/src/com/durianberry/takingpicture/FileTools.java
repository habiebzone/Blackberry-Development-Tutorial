package com.durianberry.takingpicture;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Vector;

import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import javax.microedition.io.file.FileSystemRegistry;

public class FileTools {
	public static String DEVICE_PHOTO_DIRECTORY = System
			.getProperty("fileconn.dir.photos");
	public static String MEMORYCARD_PHOTO_DIRECTORY = System
			.getProperty("fileconn.dir.memorycard.photos");
	public static String DEVICE_DOCUMENT_DIRECTORY = System
			.getProperty("fileconn.dir.memorycard");

	public static boolean isSDCardAvailable() {
		String root = null;
		boolean result = false;
		Enumeration e = FileSystemRegistry.listRoots();
		while (e.hasMoreElements()) {
			root = (String) e.nextElement();
			if (root.equalsIgnoreCase("sdcard/")) {
				result = true;
			}
		}
		return result;
	}

	public static String getSaveDirectory() {
		return "store/home/user/";
	}
	
	public static String[] listAllFilesInDirectory(String path, String[] extension) {
		if (path.startsWith("file://")) {
			// do nothing
		} else if (!path.startsWith("/"))
			path = "file:///" + path;
		else if (path.startsWith("//"))
			path = "file:/" + path;
		else
			path = "file://" + path;
		
		try {
			FileConnection fc = (FileConnection)Connector.open(path);
			Enumeration fileEnum = fc.list();
			String currentFile;
			Vector result = new Vector();
			int numExt = extension.length;
			int i = 0;
			
			while (fileEnum.hasMoreElements()) {
				currentFile = path + ((String)fileEnum.nextElement());
				
				if (currentFile.lastIndexOf('/') != 
                    (currentFile.length() - 1)) {
					
					if (extension == null) {
						result.addElement(currentFile);
					} else {
						i = 0;
						while (i<numExt) {
							if (currentFile.toLowerCase().endsWith(extension[i].toLowerCase())) {
								result.addElement(currentFile);
							}
							i++;
						}
					}
					
				}
			}
			String[] res = new String[result.size()];
			i = 0;
			numExt = result.size();
			while (i < numExt) {
				res[i] = (String) result.elementAt(i);
				i++;
			}
			return res;
		} catch (IOException e) {
			return null;
		}
		
	}
	
	public static void deleteFile(String path) {
		if (path.startsWith("file://")) {
			// do nothing
		} else if (!path.startsWith("/"))
			path = "file:///" + path;
		else if (path.startsWith("//"))
			path = "file:/" + path;
		else
			path = "file://" + path;
		
		try {
			FileConnection file = (FileConnection) Connector.open(path,
					Connector.READ_WRITE);
			if (file.exists()) {
				file.setWritable(true);
				file.delete();
			}
			file.close();
		} catch (IOException ex) {
			System.out.println("File cannot be deleted");
		}
	}
	
	public static long getFileLastModified(String path) {
		if (path.startsWith("file://")) {
			// do nothing
		} else if (!path.startsWith("/"))
			path = "file:///" + path;
		else if (path.startsWith("//"))
			path = "file:/" + path;
		else
			path = "file://" + path;
		
		
		long result = 0;
		
		try {
			FileConnection file = (FileConnection) Connector.open(path,
					Connector.READ_WRITE);
			if (file.exists()) {
				result = file.lastModified();
			}
			file.close();
		} catch (IOException ex) {
		}
		return result;
	}

	/**
	 * Method to get all bytes from a file.
	 * 
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public static byte[] getFileContent(String path) throws IOException {
		FileConnection file = null;
		
		if (path.startsWith("file://"))
			path = path.substring(7);
		else if (!path.startsWith("/"))
			path = "/" + path;

		try {
			file = (FileConnection) Connector.open("file://" + path,
					Connector.READ);
			int fileSize = (int) file.fileSize();
			if (fileSize > 0) {
				byte[] data = new byte[fileSize];
				InputStream input = file.openInputStream();
				input.read(data);
				Thread.yield();
				input.close();
				return data;
			} else {
				throw new NullPointerException("File " + path + " is empty.");
			}
		} catch (IOException e) {
			throw e;
		} finally {
			if (file != null && file.isOpen())
				file.close();
		}
	}

	public static synchronized String saveToFile(String basePath, byte[] data)
			throws IOException {
		try {
			if (basePath.startsWith("file://")) {
				// do nothing
			} else if (!basePath.startsWith("/"))
				basePath = "file:///" + basePath;
			else if (basePath.startsWith("//"))
				basePath = "file:/" + basePath;
			else
				basePath = "file://" + basePath;

			System.out.println("Save to file: "+basePath);
			
			FileConnection fconn = (FileConnection) Connector.open(basePath,
					Connector.READ_WRITE);
			if (!fconn.exists())
				fconn.create();
			fconn.setWritable(true);
			OutputStream outputStream = fconn.openOutputStream();
			outputStream.write(data);
			outputStream.close();
			fconn.close();

			return basePath;
		} catch (IOException e) {
			System.out.println("Save to file: "+e.getMessage());
			throw e;
		}
	}

	/**
	 * Create directory in file system
	 * 
	 * @param path
	 *            the final slash in the folder path is required
	 * @return
	 */
	public static boolean createFolder(String path) {
		if (path.startsWith("file://")) {
			// do nothing
		} else if (!path.startsWith("/"))
			path = "file:///" + path;
		else if (path.startsWith("//"))
			path = "file:/" + path;
		else
			path = "file://" + path;

		try { // the final slash in the folder path is required
			FileConnection fc = (FileConnection) Connector.open(path);
			// If no exception is thrown, the URI is valid but the folder may
			// not exist.
			if (!fc.exists()) {
				fc.mkdir(); // create the folder if it doesn't exist
			}
			fc.close();
			return true;
		} catch (IOException ioe) {
			System.out.println("Error create folder "+path+": "+ioe.getMessage());
			return false;
		}
	}
}
