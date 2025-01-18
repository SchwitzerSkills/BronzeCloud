package at.phillip.utils;

import java.io.File;

public class SetupManager {

    public void createFolders(){
        File staticFolder = new File("./static");

        if(!staticFolder.exists()){
            if (!staticFolder.mkdirs()) {
                System.out.println("Failed to create the folder.");
                return;
            }
        }

        File softwareFolder = new File("./software");

        if(!softwareFolder.exists()){
            if (!softwareFolder.mkdirs()) {
                System.out.println("Failed to create the folder.");
                return;
            }
        }

    }

}
