import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Finding the location of the downloads folder
        Scanner scanner = new Scanner(System.in);
        boolean directoryFound = false;
        String rootFolder = "";
        File folder = null;

        while (!directoryFound) {
            System.out.println("Please input user name: ");
            rootFolder = "C:/Users/" + scanner.nextLine() + "/Downloads";
            folder = new File(rootFolder);

            // Check if the folder exists
            if (!folder.exists() || !folder.isDirectory()) {
                System.out.println("Invalid folder path. Please check and try again.");
            } else {
                directoryFound = true;
            }
        }

        // Define file category mappings
        Map<String, String> fileCategories = new HashMap<>();
        fileCategories.put("jpg", "images");
        fileCategories.put("png", "images");
        fileCategories.put("gif", "images");
        fileCategories.put("mp4", "videos");
        fileCategories.put("mkv", "videos");
        fileCategories.put("mp3", "music");
        fileCategories.put("wav", "music");
        fileCategories.put("pdf", "documents");
        fileCategories.put("docx", "documents");
        fileCategories.put("txt", "documents");
        fileCategories.put("java", "code");
        fileCategories.put("js", "code");
        fileCategories.put("py", "code");

        // Create or update LOCATIONS.txt file
        File logFile = new File(rootFolder + "/LOCATIONS.txt");

        // Getting the files in the downloads folder
        File[] files = folder.listFiles();
        if (files == null || files.length == 0) {
            System.out.println("No files found in the folder.");
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFile, true))) {
            // Process each file
            for (File file : files) {
                if (!file.isFile() || file.getName().equals("LOCATIONS.txt")) continue;

                String fileName = file.getName();
                int dotIndex = fileName.lastIndexOf(".");
                String extension = (dotIndex > 0) ? fileName.substring(dotIndex + 1).toLowerCase() : "";

                // Determine target folder
                String category = fileCategories.getOrDefault(extension, "unknown");
                File targetFolder = new File(rootFolder + "/" + category);

                // Create target folder if it doesn't exist
                if (!targetFolder.exists()) {
                    targetFolder.mkdir();
                }

                // Move file to target folder
                Path sourcePath = file.toPath();
                Path destinationPath = targetFolder.toPath().resolve(file.getName());

                try {
                    Files.move(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
                    System.out.println("Moved: " + fileName + " -> " + category);

                    // Log the file move
                    writer.write(fileName + " -> " + destinationPath.toString());
                    writer.newLine();
                } catch (IOException e) {
                    System.out.println("Error moving file: " + fileName);
                    e.printStackTrace();
                }
            }

            System.out.println("File sorting complete! Log updated in LOCATIONS.txt");
        } catch (IOException e) {
            System.out.println("Error writing to LOCATIONS.txt");
            e.printStackTrace();
        }
    }
}
