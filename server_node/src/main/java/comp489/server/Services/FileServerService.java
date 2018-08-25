package comp489.server.Services;

import org.h2.Driver;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

/*
A service to execute all prepared statements against remote H2 DB server, for CORBA client to query CORBA
server, and obtain filesystem related information
 */
public class FileServerService {
    private Connection conn;

    public FileServerService() {
        Driver.load();
        String h2ConnectionString = "jdbc:h2:tcp://localhost/~/Projects/assign2_p2pFileServer";
        Properties info = new Properties();
        info.setProperty("user", "sa");
        info.setProperty("password", "");
        try {
            this.conn = DriverManager.getConnection(h2ConnectionString, info);

            //build schema with sample file names to exist, holding all file data
            ScriptRunner runner = new ScriptRunner(conn, false, false);
            //todo fix this
            String dbStartup = "/Users/arsalan.khalid/IdeaProjects/assign3_webp2p/server_node/src/main/resources/backend.sql";
            runner.runScript(new BufferedReader(new FileReader(dbStartup)));

        } catch (SQLException | FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getAllFiles() {

    }

    /*
    Update the sharing/permission flag on a file if the owner checks out, and fileName exists
     */
    public void registerFile(String owner, String fileName) {
        try {
            Statement query = conn.createStatement();
            int rs = query.executeUpdate("UPDATE SHARED_FILESYSTEM SET PERMISSION=TRUE WHERE OWNER='" + owner + "'" + "AND filename='" + fileName + "'");
            if(rs == 1) {
                System.out.println("Thanks " + owner + ", " + fileName + " is now being shared with the network");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        FileServerService fileServerService = new FileServerService();
        System.out.println("Connected!");

    }

    /*
    Client issues a search signal to determine if a file exists on the network
     */
    public String searchNetwork(String fileName) {
        //todo: if the client program finds that anyone is sharing the file, the client program shows the file name to the user without
        //revealing who owns the file
        //if the client program finds that no one is sharing the file, the client program shows "no match result" to the user
        try {

            Statement query = conn.createStatement();
            ResultSet rs = query.executeQuery("SELECT OWNER FROM SHARED_FILESYSTEM  WHERE filename=" + fileName);
            if(rs.first()) {
                //get content from rs
                System.out.println(rs.getArray(0));
                System.out.println("Filename was found, owner is: ");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "searchNetwork";
    }

    /*
        Check if owner exists with a query for matching requester client ID against initialized data
     */
    public boolean checkOwnerExists(String clientId) {
        try {
            Statement query = conn.createStatement();
            ResultSet rs = query.executeQuery("SELECT owner FROM SHARED_FILESYSTEM WHERE owner='" + clientId + "'");

            //proof that the owner exists
            return rs.first();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public void unRegisterFile(String owner, String fileName) {
        try {
            Statement query = conn.createStatement();
            int rs = query.executeUpdate("UPDATE SHARED_FILESYSTEM SET PERMISSION=FALSE WHERE OWNER='" + owner + "'" + "AND filename='" + fileName + "'");
            if(rs == 1) {
                System.out.println("Thanks " + owner + ", " + fileName + " is now removed from the network");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
