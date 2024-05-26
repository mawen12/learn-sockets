package com.mawen.learn.basic.sockets;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

/**
 * Use {@link NetworkInterface} to access all of a host's interfaces.
 *
 * <pre>{@code
 *  java InetAddressExample www.baidu.com blah.blah 129.35.69.7
 * }</pre>
 *
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/5/22
 */
public class InetAddressExample {

	public static void main(String[] args) {
		// Get the network interfaces and associated addresses for this host
		try {
			// Get a list of this host's network interfaces
			Enumeration<NetworkInterface> interfaceList = NetworkInterface.getNetworkInterfaces();
			if (interfaceList == null) {
				System.err.println("--No interfaces found--");
			}
			else {
				while (interfaceList.hasMoreElements()) {
					NetworkInterface iface = interfaceList.nextElement();
					System.out.println("Interface " + iface.getName() + ":");

					// Get the addresses associated with the interface
					Enumeration<InetAddress> addrList = iface.getInetAddresses();
					if (!addrList.hasMoreElements()) {
						System.err.println("\t(No addresses for this interface)");
					}

					while (addrList.hasMoreElements()) {
						InetAddress address = addrList.nextElement();
						System.out.print("\tAddress " + (address instanceof Inet4Address ? "(v4)" : (address instanceof Inet6Address ? "(v6)" : "(?)")));
						System.out.println(": " + address.getHostAddress());
					}
				}
			}
		}
		catch (SocketException e) {
			System.err.println("Error getting network interfaces: " + e.getMessage());
		}

		// Get name(s)/address(es) of hosts given on command line
		for (String host : args) {
			try {
				System.out.println(host + ":");
				InetAddress[] addressList = InetAddress.getAllByName(host);

				for (InetAddress address : addressList) {
					System.out.println("\t" + address.getHostName() + "/" + address.getHostAddress());
				}
			}
			catch (UnknownHostException e) {
				System.out.println("\tUnable to find address for " + host);
			}
		}
	}

}
