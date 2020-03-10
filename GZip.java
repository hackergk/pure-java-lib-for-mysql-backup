	package backup;
   //file: GZip.java
    import java.io.*;
    import java.util.zip.*;

    public class GZip {
      public static int sChunk = 8192;
	  
	  public GZip(File [] args,String name){
        if (args.length < 1) {
          System.out.println("Usage: GZip source");
          return;
        }
        // create output stream
        String zipname = name + ".zip";
	    ZipOutputStream zipout;
	    try {
	      FileOutputStream out = new FileOutputStream(zipname);
	      zipout = new ZipOutputStream(out);
	    }
        catch (IOException e) {
          System.out.println("Couldn't create " + zipname + ".");
          return;
        }
        byte[] buffer = new byte[sChunk];
        // compress the file
        try {
			for(File n : args){
				ZipEntry entry = new ZipEntry(n.getName());
				//System.out.println(entry.getName());
				entry.setComment("this is comment");
				zipout.putNextEntry(entry);	
				FileInputStream in = new FileInputStream(n.getAbsolutePath());
				int length;
				while ((length = in.read(buffer, 0, sChunk)) != -1)
					zipout.write(buffer, 0, length);
				in.close(  );				
			}
        }
        catch (IOException e) {
          System.out.println("Couldn't compress " + zipname + "." + e.toString());
        }
        try { zipout.close(  ); }
        catch (IOException e) {}		
	  }
	  
    }
