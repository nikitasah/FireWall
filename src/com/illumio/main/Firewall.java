/* Imports */

package com.illumio.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.illumio.utils.Rules;

/* Firewall class implements the firewall functionality in a network */

public class Firewall {
	private List<Rules> rules;
	private final String path;

	Firewall(String path){
		this.path = path;
		rules = new ArrayList<Rules>();
	}

	/* @Function : parseInputCsv				  		  			*/
	/* @Description : To parse the input csv file containing rules  */
	/* @Input :  													*/
	/* line : line in the scv file 									*/
	
	private void parseInputCsv(String line) {
		if(line.isEmpty()) {
			return;
		}

		String[] input = line.split(",");
		Rules r = new Rules();

		//Parse direction : Assuming inbound = 0 and outbound = 1
		if(input[0].equals("inbound")) {
			r.setDirection(0);
		}
		else{
			r.setDirection(1);
		}

		//Parse protocol : Supported protocols UDP and TCP
		r.setProtocol(input[1]);

		//Parse port Range 
		if(input[2].contains("-")) {
			int index = input[2].indexOf('-');
			r.setStartPort(Integer.valueOf(input[2].substring(0, index)));
			r.setEndPort(Integer.valueOf(input[2].substring(index + 1, input[2].length())));
		}
		//In case if single Port store same value in start and end port
		else {
			r.setStartPort(Integer.valueOf(input[2]));
			r.setEndPort(Integer.valueOf(input[2]));
		}

		//Parse ip address
		if(input[3].contains("-")) {
			int index = input[3].indexOf('-');
			try {
				r.setStartIp(convertIpToLong(InetAddress.getByName(input[3].substring(0, index))));
				r.setEndIp(convertIpToLong(InetAddress.getByName(input[3].substring(index + 1, input[3].length()))));	
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
		}

		//In case of single Ip store same value in start and end
		else {
			try {
				long startIp = convertIpToLong(InetAddress.getByName(input[3]));
				r.setStartIp(startIp);
				r.setEndIp(startIp);			
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
		}
		rules.add(r);
	}

	/* @Function : ipToLong 				  		  */
	/* @Description : To convert ip address to long */
	/* @Input : 									*/
	/* inputIp : Ip to be converted from string to long */
	
	private static long convertIpToLong(InetAddress inputIp) {
		byte[] octets = inputIp.getAddress();
		long resultIp = 0;
		for (byte octet : octets) {
			resultIp <<= 8;
			resultIp |= octet & 0xff;
		}
		return resultIp;
	}

	/* @Function : accept_packet 				  		  */
	/* @Description : To check whether the packet can be accepted by the firewall rule or not */
	/* @Input : Rules csv file, input parameters to test */
	/* direction : direction of the input packet */
	/* protocol : protocol of the input packet */
	/* port :  port of the input packet */
	/* ip : ip of the input packet */
	/* @Output : boolean returning true or false*/
	
	private boolean accept_packet(String direction, String protocol, int port, String ip) {
		int packetDir = 0;
		long packetIp = 0;
		
		//Parse direction
		if(direction.equals("outbound")) {
			packetDir = 1;
		}
		
		//Parse ip address
		try {
			packetIp = convertIpToLong(InetAddress.getByName(ip));
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		}
		
		//Traverse the arraylist of all rules to find a match
		for(Rules r : rules) {
			if((r.getDirection() == packetDir) && (r.getProtocol().equals(protocol)) &&
					(port >= r.getStartPort() && port <= r.getEndPort()) &&
					(packetIp >= r.getStartIp() && packetIp <= r.getEndIp())) {
				return true;
			}
		}
		return false;
	}

	/* @Function : Main									  */
	/* @Input : Rules csv file, input parameters to test */
	/* args[0] : csv rules file path */
	/* args[1] : direction for packet to be tested */
	/* args[2] : protocol type */
	/* args[3] : Port  */
	/* args[4] : Ip address */

	public static void main(String[] args){
		if(args.length < 5) {
			System.out.println("Invalid number of arguments");
			return;
		}
		Firewall fw = new Firewall(args[0]);
		Scanner scanner;
		try {
			scanner = new Scanner(new File(fw.path));
			while (scanner.hasNext()) {
				fw.parseInputCsv(scanner.nextLine());
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		boolean result = fw.accept_packet(args[1], args[2], Integer.valueOf(args[3]), args[4]);
		System.out.println("The packet accepted by firewall : " + result );
		
	}
}
