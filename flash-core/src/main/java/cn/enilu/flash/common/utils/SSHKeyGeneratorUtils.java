package cn.enilu.flash.common.utils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author liunian-jk
 */
public class SSHKeyGeneratorUtils {

    public static void main(String[] args) {
        String email = "test22Rsa@gmail.com";
        String userName = email.substring(0, email.indexOf("@"));

        try {
            generateSSHKey(userName, email);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void generateSSHKey(String keyName, String email) throws IOException, InterruptedException {
        String rsa_name = "id_rsa_" + keyName;

        // Command to generate SSH key
        String[] command = {
                "ssh-keygen",
                "-t", "rsa",
                "-b", "2048",
                "-f", System.getProperty("user.home") + "/.ssh/" + rsa_name,
                "-C", email,
                "-N", "" // No passphrase
        };

        // ProcessBuilder to run the command
        ProcessBuilder pb = new ProcessBuilder(command);
        pb.redirectErrorStream(true);

        // Start the process
        Process process = pb.start();

        // Read the output
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        }

        // Wait for the process to complete
        int exitCode = process.waitFor();
        if (exitCode == 0) {
            System.out.println("SSH key generated successfully.");
        } else {
            System.out.println("Error in generating SSH key. Exit code: " + exitCode);
        }
    }
}
