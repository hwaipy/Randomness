/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package remotefiletele;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.channels.FileLock;
import java.util.Arrays;
import java.util.HashSet;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author Hwaipy 2015-3-23
 */
public class Client implements Runnable {

  private final String serverAddress;
  private final LinkedBlockingQueue<File> waitingFiles = new LinkedBlockingQueue<>();

  public Client(String serverAddress) {
    this.serverAddress = serverAddress;
  }

  @Override
  public void run() {
    monite();
    communication();
  }

  private void communication() {
    new Thread(new Runnable() {

      @Override
      public void run() {
        try {
          doCommunication();
        } catch (Exception e) {
        }
      }

    }).start();
  }

  private void monite() {
    new Thread(new Runnable() {

      @Override
      public void run() {
        try {
          doMonite();
        } catch (Exception e) {
        }
      }

    }).start();
  }

  private void doMonite() throws Exception {
    File path = new File(".");
    String[] listFiles = path.list();
    HashSet<String> fileSet = new HashSet<>();
    fileSet.addAll(Arrays.asList(listFiles));

    while (true) {
      Thread.sleep(2000);
      listFiles = path.list();
      for (String listFile : listFiles) {
        if (!fileSet.contains(listFile)) {
          transport(listFile);
        }
      }
    }
  }

  private void doCommunication() throws Exception {
//    Socket socket = new Socket(InetAddress.getByName(serverAddress), 10086);
//    OutputStream outputStream = socket.getOutputStream();
    while (true) {
      File file = waitingFiles.take();
      System.out.println("New File: " + file);
      FileLock lock = null;
      FileInputStream in = new FileInputStream(file);
      while (true) {
        lock = in.getChannel().tryLock();
        if (lock == null) {
          Thread.sleep(1000);
        }
        else {
          break;
        }
      }
      BufferedInputStream bin = new BufferedInputStream(in);

      System.out.println("LockAndLoad");
      bin.close();
      lock.release();
    }
  }

  public static void main(String[] args) {
    new Client("localhost").run();
  }

  private void transport(String fileName) {
    File file = new File(fileName);
    if (file.exists() && file.isFile()) {
      waitingFiles.add(file);
    }
  }

}
