package com.youshi.zebra.lesson.model;
public class MonitorInfo {
		private int preCount;
		private int nextCount;
		private int excepCount;
		private long currTime;

		
		public MonitorInfo(int preCount, int nextCount, int excepCount, long currTime) {
			this.preCount = preCount;
			this.nextCount = nextCount;
			this.excepCount = excepCount;
			this.currTime = currTime;
		}

		public long getCurrTime() {
			return currTime;
		}

		public int getPreCount() {
			return preCount;
		}

		public int getNextCount() {
			return nextCount;
		}

		public int getExcepCount() {
			return excepCount;
		}
	}
	
	