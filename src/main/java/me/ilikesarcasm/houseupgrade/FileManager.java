package me.ilikesarcasm.houseupgrade;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.io.FileUtils;

enum FileType { FILE, FOLDER }

public class FileManager {

    class FileConfiguration {

        public FileType type;
        public File file;
        public String srcPath;

        public FileConfiguration(FileType type, File file) {
            this(type, file, (String)null);
        }

        public FileConfiguration(FileType type, File file, String srcPath) {
            this.type = type;
            this.file = file;
            this.srcPath = srcPath;
        }

    }

    private static FileManager instance;

    private static final String HOUSE_FOLDER = "houses";
    private static final String SCHEMATIC_FOLDER = "schematics";
    private static final String LANG_FOLDER = "lang";
    private static final String CONFIG_FILE = "config.yml";

    private final HashMap<String, FileConfiguration> files;

    public static FileManager getInstance() {
        if (FileManager.instance == null) {
            FileManager.instance = new FileManager();
        }

        return FileManager.instance;
    }

    private FileManager() {
        this.files = new HashMap<String, FileConfiguration>() {{
            put("houseFolder", new FileConfiguration(FileType.FOLDER, makeDestFile(FileManager.HOUSE_FOLDER)));
            put("schematicFolder", new FileConfiguration(FileType.FOLDER, makeDestFile(FileManager.SCHEMATIC_FOLDER)));
            put("configFile", new FileConfiguration(FileType.FILE, makeDestFile(FileManager.CONFIG_FILE), FileManager.CONFIG_FILE));
            put("langFolder", new FileConfiguration(FileType.FOLDER, makeDestFile(FileManager.LANG_FOLDER), FileManager.LANG_FOLDER));
        }};
    }

    private File makeDestFile(String fileName) {
        return new File(HouseUpgrade.getPluginFolder() + File.separator + fileName);
    }

    private void copyFileFromJar(String srcPath, File destFile) {
        try {
            FileUtils.copyToFile(HouseUpgrade.getInstance().getResource(srcPath), destFile);
        } catch (IOException e) {
            HouseUpgrade.error("Couldn't copy file " + srcPath + " to file " + destFile.getAbsolutePath());
        }
    }

    private void copyFolderFromJar(String srcPath, File destFolder) {
        File srcFolder;
        try {
            srcFolder = new File(HouseUpgrade.getInstance().getClass().getProtectionDomain().getCodeSource().getLocation().toURI());
        } catch (URISyntaxException e) {
            HouseUpgrade.error("Couldn't copy folder " + srcPath + " to folder " + destFolder.getAbsolutePath());
            return;
        }

        ZipFile srcFolderAsZip;
        try {
            srcFolderAsZip = new ZipFile(srcFolder);
        } catch (IOException e) {
            HouseUpgrade.error("Couldn't copy folder " + srcPath + " to folder " + destFolder.getAbsolutePath());
            return;
        }

        Enumeration<? extends ZipEntry> entries = srcFolderAsZip.entries();
        
        while(entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            String entryName = entry.getName();

            if(!entry.isDirectory() && entryName.startsWith(srcPath + "/")) {
                String entryFileName = entryName.substring(entryName.lastIndexOf("/"));
                File entryDestFile = new File(destFolder.getAbsolutePath() + File.separator + entryFileName);
                this.copyFileFromJar(entry.getName(), entryDestFile);
            }
        }

        try {
            srcFolderAsZip.close();
        } catch (IOException e) {
            HouseUpgrade.error("Couldn't copy folder " + srcPath + " to folder " + destFolder.getAbsolutePath());
            return;
        }
    }

    public void createFileStructure() {
        for (FileConfiguration fileConfiguration: this.files.values()) {
            FileType type = fileConfiguration.type;
            File file = fileConfiguration.file;

            if (!file.exists()) {
                try {
                    if (type == FileType.FILE && !file.createNewFile()) {
                        HouseUpgrade.error("Couldn't create file: " + file.getAbsolutePath());
                    } else if (fileConfiguration.type == FileType.FOLDER && !file.mkdirs()) {
                        HouseUpgrade.error("Couldn't create folder: " + file.getAbsolutePath());
                    }
                } catch (IOException e) {
                    HouseUpgrade.error(e.getMessage());
                }
            }

            if (fileConfiguration.srcPath != null) {
                if (type == FileType.FILE) {
                    this.copyFileFromJar(fileConfiguration.srcPath, file);
                } else {
                    this.copyFolderFromJar(fileConfiguration.srcPath, file);
                }
            }
        }
    }

    public File get(String key) {
        return this.files.get(key).file;
    }

}
