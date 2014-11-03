package org.transdroid.daemon.Qbittorrent;

public enum SizeUnit implements IUnit{
	BYTE(1) {
		@Override
		public String key() {
			return "size.bytes";
		}
	},
	KIBIBYTE(1024L) {
		@Override
		public String key() {
			return "size.kibibytes";
		}
	},
	MEBIBYTE(1024L*1024L) {
		@Override
		public String key() {
			return "size.mebibytes";
		}
	},
	GIBIBYTE(1024L*1024L*1024L) {
		@Override
		public String key() {
			return "size.gibibytes";
		}
	},
	TEBIBYTE(1024L*1024L*1024L*1024L) {
		@Override
		public String key() {
			return "size.tebibytes";
		}
	};
	
	public abstract String key();

	private long multiplier;
	
	public long getMultiplier() {
		return multiplier;
	}
	
	private SizeUnit(long multipleValue) {
		multiplier = multipleValue;
	}
}
