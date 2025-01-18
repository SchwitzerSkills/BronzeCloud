package at.phillip.utils;

import java.io.File;

public class SetupManager {

    public void createFolders(){
        File folder = new File("./static");

        if(!folder.exists()){
            if (!folder.mkdirs()) {
                System.out.println("Failed to create the folder.");
                return;
            }
        }

    }

}
