/* Imports */
package com.illumio.utils;

/* Rules is a pojo class containing getters and setters method*/
/* It set/get values of the variables of the input.csv */

public class Rules {
	private int direction;
	private String protocol;
	private int startPort;
	private int endPort;
	private long startIp;
	private long endIp;
	
	public int getDirection() {
		return direction;
	}
	public void setDirection(int direction) {
		this.direction = direction;
	}
	
	public String getProtocol() {
		return protocol;
	}
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	public int getStartPort() {
		return startPort;
	}
	public void setStartPort(int startPort) {
		this.startPort = startPort;
	}
	public int getEndPort() {
		return endPort;
	}
	public void setEndPort(int endPort) {
		this.endPort = endPort;
	}
	public long getStartIp() {
		return startIp;
	}
	public void setStartIp(long startIp) {
		this.startIp = startIp;
	}
	public long getEndIp() {
		return endIp;
	}
	public void setEndIp(long endIp) {
		this.endIp = endIp;
	}
}
