package org.jiahuan.crc;

public class Crc16 {

	public int tocrc16(byte[] buf) {

		int r = 0xffff;

		for (int j = 0; j < buf.length; j++) {
			int hi = r >> 8;
			hi ^= buf[j];
			r = hi;

			for (int i = 0; i < 8; i++) {
				int flag = r & 0x0001;
				r = r >> 1;
				if (flag == 1) {
					r ^= 0xa001;
				}
			}
		}
		return r;
	}
}
