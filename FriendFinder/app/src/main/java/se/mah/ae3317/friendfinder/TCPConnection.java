package se.mah.ae3317.friendfinder;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by xisth on 2016-09-30.
 */
public class TCPConnection extends Service {
	public static final String IP = "IP", PORT = "PORT";
	private RunOnThread thread;
	private Receive receive;
	private Buffer<String> receiveBuffer;
	private Socket socket;
	private DataOutputStream output;
	private DataInputStream input;
	private InetAddress address;
	private static final int connectionPort = 7117;
	private static final String ip = "195.178.227.53";
	private Exception exception;
	private boolean connected;


	@Override 
	public int onStartCommand(Intent intent, int flags, int startId) {
		thread = new RunOnThread();
		receiveBuffer = new Buffer<String>();
		return Service.START_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return new LocalService();
	}

	public void setConnected(boolean connected) {
		this.connected = connected;
	}

	public void connect() {
		if (!connected) {
			thread.start();
			thread.execute(new Connect());
		}
		else {
			Log.d("Friend Finder: ", "Already Connected!");
		}
	}

	public void disconnect() {
		thread.execute(new Disconnect());
	}

	public void send(String message) {
		thread.execute(new Send(message));
	}
	
	public String receive() throws InterruptedException {
		return receiveBuffer.get();
	}

    public class LocalService extends Binder {
        public TCPConnection getService() {
            return TCPConnection.this;
        }
    }

	private class Connect implements Runnable {
		public void run() {
			try {
				address = InetAddress.getByName(ip);
				socket = new Socket(address, connectionPort);

				InputStream is = socket.getInputStream();
				input = new DataInputStream(is);
				OutputStream os = socket.getOutputStream();
				output = new DataOutputStream(os);

				output.flush();
                receiveBuffer.put("CONNECTED");
				receive = new Receive();
				receive.start();
			}
			catch (Exception e) {
				exception = e;
				receiveBuffer.put("EXCEPTION");
			}
		}
	}

	private class Disconnect implements Runnable {
		public void run() {
			try {
			    if (output != null) {
					input.close();
				    output.close();
					socket.close();
				}
			    thread.stop();
			    receiveBuffer.put("CLOSED");
			}
			catch(IOException e) {
				exception = e;
				receiveBuffer.put("EXCEPTION");
			}
		}
	}

	private class Send implements Runnable {
		private String message;

		public Send(String message) {
			this.message = message;
		}

		public void run() {
			try {
				output.writeUTF(message);
				output.flush();
			}
			catch (IOException e) {
				exception = e;
				receiveBuffer.put("EXCEPTION");
				Log.d("FriendFinder error: ", e.getMessage());
			}
		}
	}

	private class Receive extends Thread {
		public void run() {
			String result;
			try {
				while (receive != null) {
					result = input.readUTF();
					receiveBuffer.put(result);
				}
			}
			catch (Exception e) {
				receive = null;
			}
		}
	}
}
